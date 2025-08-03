package net.tropicraft.core.common.entity.neutral;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;

public class IguanaEntity extends PathfinderMob {

    /**
     * Timer for how much longer the iggy will be enraged
     */
    private int angerLevel;
    @Nullable
    private EntityReference<Player> angerTarget;

    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(Tropicraft.location("attack_speed_boost"), 0.05, AttributeModifier.Operation.ADD_VALUE);

    public IguanaEntity(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entity) {
        super.setLastHurtByMob(entity);
        if (entity instanceof Player player) {
            angerTarget = new EntityReference<>(player);
        }
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByAggressorGoal(this));
        targetSelector.addGoal(2, new TargetAggressorGoal(this));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putShort("Anger", (short) angerLevel);

        EntityReference.store(angerTarget, output, "HurtBy");
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        angerLevel = input.getShortOr("Anger", (short) 0);

        angerTarget = EntityReference.read(input, "HurtBy");

        Player player = EntityReference.get(angerTarget, level()::getPlayerByUUID, Player.class);
        setLastHurtByMob(player);
        if (player != null) {
            lastHurtByPlayer = new EntityReference<>(player);
            lastHurtByPlayerMemoryTime = getLastHurtByMobTimestamp();
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        AttributeInstance attribute = getAttribute(Attributes.MOVEMENT_SPEED);

        if (isAngry()) {
            if (!isBaby() && !attribute.hasModifier(ATTACK_SPEED_BOOST_MODIFIER.id())) {
                attribute.addTransientModifier(ATTACK_SPEED_BOOST_MODIFIER);
            }

            --angerLevel;
        } else if (attribute.hasModifier(ATTACK_SPEED_BOOST_MODIFIER.id())) {
            attribute.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
        }

        if (angerLevel > 0 && angerTarget != null && getLastHurtByMob() == null) {
            Player player = EntityReference.get(angerTarget, level()::getPlayerByUUID, Player.class);
            setLastHurtByMob(player);
            if (player != null) {
                setLastHurtByPlayer(player, PLAYER_HURT_EXPERIENCE_TIME);
            }
        }

        super.customServerAiStep(level);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isInvulnerableTo(level, source)) {
            return false;
        } else {
            Entity sourceEntity = source.getEntity();
            if (sourceEntity instanceof Player sourcePlayer && !sourcePlayer.isCreative() && hasLineOfSight(sourceEntity)) {
                becomeAngryAt(sourceEntity);
            }

            return super.hurtServer(level, source, amount);
        }
    }

    private boolean becomeAngryAt(Entity target) {
        angerLevel = 400 + random.nextInt(400);
        if (target instanceof LivingEntity) {
            setLastHurtByMob((LivingEntity) target);
        }

        return true;
    }

    public boolean isAngry() {
        return angerLevel > 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.IGGY_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return Sounds.IGGY_ATTACK.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.IGGY_DEATH.get();
    }

    static class TargetAggressorGoal extends NearestAttackableTargetGoal<Player> {
        public TargetAggressorGoal(IguanaEntity iggy) {
            super(iggy, Player.class, true);
        }

        @Override
        public boolean canUse() {
            return ((IguanaEntity) mob).isAngry() && super.canUse();
        }
    }

    static class HurtByAggressorGoal extends HurtByTargetGoal {
        public HurtByAggressorGoal(IguanaEntity iguana) {
            super(iguana);
            setAlertOthers(IguanaEntity.class);
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof IguanaEntity && this.mob.hasLineOfSight(target) && ((IguanaEntity) mob).becomeAngryAt(target)) {
                mob.setTarget(target);
            }
        }
    }
}
