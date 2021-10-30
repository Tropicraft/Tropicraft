package net.tropicraft.core.common.entity.neutral;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.hostile.TropicraftCreatureEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;
import java.util.UUID;

public class IguanaEntity extends TropicraftCreatureEntity {

    /** Timer for how much longer the iggy will be enraged */
    private int angerLevel;
    private UUID angerTargetUUID;

    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, AttributeModifier.Operation.ADDITION);

    public IguanaEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.IGUANA_SPAWN_EGG.get());
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entity) {
        super.setLastHurtByMob(entity);
        if (entity != null) {
            angerTargetUUID = entity.getUUID();
        }

    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        targetSelector.addGoal(1, new HurtByAggressorGoal(this));
        targetSelector.addGoal(2, new TargetAggressorGoal(this));
    }

    @Override
    public void addAdditionalSaveData(final CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("Anger", (short)angerLevel);

        if (angerTargetUUID != null) {
            compound.putString("HurtBy", angerTargetUUID.toString());
        } else {
            compound.putString("HurtBy", "");
        }
    }

    @Override
    public void readAdditionalSaveData(final CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        angerLevel = compound.getShort("Anger");
        String hurtBy = compound.getString("HurtBy");

        if (!hurtBy.isEmpty()) {
            angerTargetUUID = UUID.fromString(hurtBy);
            final PlayerEntity entityplayer = level.getPlayerByUUID(angerTargetUUID);
            setLastHurtByMob(entityplayer);

            if (entityplayer != null) {
                lastHurtByPlayer = entityplayer;
                lastHurtByPlayerTime = getLastHurtByMobTimestamp();
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        ModifiableAttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);

        if (this.isAngry()) {
            if (!this.isBaby() && !attribute.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
                attribute.addTransientModifier(ATTACK_SPEED_BOOST_MODIFIER);
            }

            --this.angerLevel;
        } else if (attribute.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
            attribute.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
        }

        if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getLastHurtByMob() == null) {
            PlayerEntity entityplayer = this.level.getPlayerByUUID(this.angerTargetUUID);
            this.setLastHurtByMob(entityplayer);
            this.lastHurtByPlayer = entityplayer;
            this.lastHurtByPlayerTime = this.getLastHurtByMobTimestamp();
        }

        super.customServerAiStep();
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if (isInvulnerableTo(damageSource)) {
            return false;
        } else {
            Entity sourceEntity = damageSource.getEntity();
            if (sourceEntity instanceof PlayerEntity && !((PlayerEntity)sourceEntity).isCreative() && canSee(sourceEntity)) {
                becomeAngryAt(sourceEntity);
            }

            return super.hurt(damageSource, amount);
        }
    }

    private boolean becomeAngryAt(Entity target) {
        angerLevel = 400 + random.nextInt(400);
        if (target instanceof LivingEntity) {
            setLastHurtByMob((LivingEntity)target);
        }

        return true;
    }

    public boolean isAngry() {
        return this.angerLevel > 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.IGGY_LIVING;
    }

    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSource) {
        return Sounds.IGGY_ATTACK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.IGGY_DEATH;
    }

    static class TargetAggressorGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        public TargetAggressorGoal(IguanaEntity iggy) {
            super(iggy, PlayerEntity.class, true);
        }

        public boolean canUse() {
            return ((IguanaEntity)this.mob).isAngry() && super.canUse();
        }
    }

    static class HurtByAggressorGoal extends HurtByTargetGoal {
        public HurtByAggressorGoal(IguanaEntity iguana) {
            super(iguana);
            this.setAlertOthers(IguanaEntity.class);
        }

        protected void alertOther(MobEntity mob, LivingEntity target) {
            if (mob instanceof IguanaEntity && this.mob.canSee(target) && ((IguanaEntity)mob).becomeAngryAt(target)) {
                mob.setTarget(target);
            }

        }
    }
}
