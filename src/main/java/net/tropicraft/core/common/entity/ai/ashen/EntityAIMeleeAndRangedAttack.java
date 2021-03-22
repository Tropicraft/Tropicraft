package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.item.AshenMaskItem;

import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class EntityAIMeleeAndRangedAttack extends Goal
{
    /** The entity the AI instance has been applied to */
    private final AshenEntity entityHost;
    /** The entity (as a RangedAttackMob) the AI instance has been applied to. */
    private final IRangedAttackMob rangedAttackEntityHost;
    private LivingEntity attackTarget;
    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackTime.
     */
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int seeTime;
    private int maxMeleeAttackTime;
    /** The maximum time the AI has to wait before peforming another ranged attack. */
    private int maxRangedAttackTime;
    private float shootCutoffRange;
    private float shootCutoffRangeSqr;
    private float meleeHitRange = 2F;

    public EntityAIMeleeAndRangedAttack(AshenEntity attacker, double movespeed, int maxMeleeAttackTime, int maxRangedAttackTime, float maxAttackDistanceIn) {
        this(attacker, movespeed, maxMeleeAttackTime, maxRangedAttackTime, maxAttackDistanceIn, 2F);
    }

    public EntityAIMeleeAndRangedAttack(AshenEntity attacker, double movespeed, int maxMeleeAttackTime, int maxRangedAttackTime, float maxAttackDistanceIn, float meleeHitRange) {
        this.rangedAttackTime = -1;
        this.rangedAttackEntityHost = attacker;
        this.entityHost = attacker;
        this.entityMoveSpeed = movespeed;
        this.maxMeleeAttackTime = maxMeleeAttackTime;
        this.maxRangedAttackTime = maxRangedAttackTime;
        this.shootCutoffRange = maxAttackDistanceIn;
        this.shootCutoffRangeSqr = maxAttackDistanceIn * maxAttackDistanceIn;
        this.meleeHitRange = meleeHitRange;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        LivingEntity entitylivingbase = entityHost.getRevengeTarget();

        if (entitylivingbase == null) {
            return false;
        } else {
            attackTarget = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute() || !entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        attackTarget = null;
        seeTime = 0;
        rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick() {
        if (attackTarget != null) {
            ItemStack headGear = attackTarget.getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (headGear.getItem() instanceof AshenMaskItem) {
                return;
            }
        }
        double d0 = entityHost.getDistanceSq(attackTarget.getPosX(), attackTarget.getBoundingBox().minY, attackTarget.getPosZ());
        boolean flag = entityHost.getEntitySenses().canSee(attackTarget);

        if (flag) {
            ++seeTime;
        } else {
            seeTime = 0;
        }

        if (d0 <= (double)shootCutoffRangeSqr && seeTime >= 20) {
            //this.entityHost.getNavigation().clearPathEntity();
        } else {
            //this.entityHost.getNavigation().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        if (seeTime >= 20) {
            entityHost.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
        }

        entityHost.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
        float f;

        //System.out.println(rangedAttackTime);

        if (--rangedAttackTime <= 0) {
            f = MathHelper.sqrt(d0) / shootCutoffRange;
            float f1 = f;

            if (f < 0.1F) {
                f1 = 0.1F;
            }

            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            if (d0 >= (double)shootCutoffRange * (double)shootCutoffRange) {
                rangedAttackEntityHost.attackEntityWithRangedAttack(attackTarget, f1);
                rangedAttackTime = maxRangedAttackTime;
            } else if (d0 <= meleeHitRange * meleeHitRange) {
                entityHost.attackEntityAsMob(attackTarget);
                entityHost.swingArm(Hand.MAIN_HAND);
                rangedAttackTime = maxMeleeAttackTime;
            }
        }
    }
}
