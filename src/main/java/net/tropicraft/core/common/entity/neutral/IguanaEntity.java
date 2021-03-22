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
    public void setRevengeTarget(@Nullable LivingEntity entity) {
        super.setRevengeTarget(entity);
        if (entity != null) {
            angerTargetUUID = entity.getUniqueID();
        }

    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0);
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
    public void writeAdditional(final CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putShort("Anger", (short)angerLevel);

        if (angerTargetUUID != null) {
            compound.putString("HurtBy", angerTargetUUID.toString());
        } else {
            compound.putString("HurtBy", "");
        }
    }

    @Override
    public void readAdditional(final CompoundNBT compound) {
        super.readAdditional(compound);
        angerLevel = compound.getShort("Anger");
        String hurtBy = compound.getString("HurtBy");

        if (!hurtBy.isEmpty()) {
            angerTargetUUID = UUID.fromString(hurtBy);
            final PlayerEntity entityplayer = world.getPlayerByUuid(angerTargetUUID);
            setRevengeTarget(entityplayer);

            if (entityplayer != null) {
                attackingPlayer = entityplayer;
                recentlyHit = getRevengeTimer();
            }
        }
    }

    @Override
    protected void updateAITasks() {
        ModifiableAttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);

        if (this.isAngry()) {
            if (!this.isChild() && !attribute.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
                attribute.applyNonPersistentModifier(ATTACK_SPEED_BOOST_MODIFIER);
            }

            --this.angerLevel;
        } else if (attribute.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
            attribute.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
        }

        if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getRevengeTarget() == null) {
            PlayerEntity entityplayer = this.world.getPlayerByUuid(this.angerTargetUUID);
            this.setRevengeTarget(entityplayer);
            this.attackingPlayer = entityplayer;
            this.recentlyHit = this.getRevengeTimer();
        }

        super.updateAITasks();
    }

    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        if (isInvulnerableTo(damageSource)) {
            return false;
        } else {
            Entity sourceEntity = damageSource.getTrueSource();
            if (sourceEntity instanceof PlayerEntity && !((PlayerEntity)sourceEntity).isCreative() && canEntityBeSeen(sourceEntity)) {
                becomeAngryAt(sourceEntity);
            }

            return super.attackEntityFrom(damageSource, amount);
        }
    }

    private boolean becomeAngryAt(Entity target) {
        angerLevel = 400 + rand.nextInt(400);
        if (target instanceof LivingEntity) {
            setRevengeTarget((LivingEntity)target);
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

        public boolean shouldExecute() {
            return ((IguanaEntity)this.goalOwner).isAngry() && super.shouldExecute();
        }
    }

    static class HurtByAggressorGoal extends HurtByTargetGoal {
        public HurtByAggressorGoal(IguanaEntity iguana) {
            super(iguana);
            this.setCallsForHelp(IguanaEntity.class);
        }

        protected void setAttackTarget(MobEntity mob, LivingEntity target) {
            if (mob instanceof IguanaEntity && this.goalOwner.canEntityBeSeen(target) && ((IguanaEntity)mob).becomeAngryAt(target)) {
                mob.setAttackTarget(target);
            }

        }
    }
}
