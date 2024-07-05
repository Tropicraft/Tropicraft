package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.item.AshenMaskItem;

import java.util.EnumSet;

public class EntityAIMeleeAndRangedAttack extends Goal {
    /**
     * The entity the AI instance has been applied to
     */
    private final AshenEntity entityHost;
    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final RangedAttackMob rangedAttackEntityHost;
    private LivingEntity attackTarget;
    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackTime.
     */
    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private int seeTime;
    private final int maxMeleeAttackTime;
    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    private final int maxRangedAttackTime;
    private final float shootCutoffRange;
    private final float shootCutoffRangeSqr;
    private float meleeHitRange = 2F;

    public EntityAIMeleeAndRangedAttack(AshenEntity attacker, double movespeed, int maxMeleeAttackTime, int maxRangedAttackTime, float maxAttackDistanceIn) {
        this(attacker, movespeed, maxMeleeAttackTime, maxRangedAttackTime, maxAttackDistanceIn, 2F);
    }

    public EntityAIMeleeAndRangedAttack(AshenEntity attacker, double movespeed, int maxMeleeAttackTime, int maxRangedAttackTime, float maxAttackDistanceIn, float meleeHitRange) {
        rangedAttackTime = -1;
        rangedAttackEntityHost = attacker;
        entityHost = attacker;
        entityMoveSpeed = movespeed;
        this.maxMeleeAttackTime = maxMeleeAttackTime;
        this.maxRangedAttackTime = maxRangedAttackTime;
        shootCutoffRange = maxAttackDistanceIn;
        shootCutoffRangeSqr = maxAttackDistanceIn * maxAttackDistanceIn;
        this.meleeHitRange = meleeHitRange;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        LivingEntity entitylivingbase = entityHost.getLastHurtByMob();

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
    public boolean canContinueToUse() {
        return canUse() || !entityHost.getNavigation().isDone();
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
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
            ItemStack headGear = attackTarget.getItemBySlot(EquipmentSlot.HEAD);
            if (headGear.getItem() instanceof AshenMaskItem) {
                return;
            }
        }
        double d0 = entityHost.distanceToSqr(attackTarget.getX(), attackTarget.getBoundingBox().minY, attackTarget.getZ());
        boolean flag = entityHost.getSensing().hasLineOfSight(attackTarget);

        if (flag) {
            ++seeTime;
        } else {
            seeTime = 0;
        }

        if (d0 <= (double) shootCutoffRangeSqr && seeTime >= 20) {
            //this.entityHost.getNavigation().clearPathEntity();
        } else {
            //this.entityHost.getNavigation().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        if (seeTime >= 20) {
            entityHost.getNavigation().moveTo(attackTarget, entityMoveSpeed);
        }

        entityHost.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
        float f;

        //System.out.println(rangedAttackTime);

        if (--rangedAttackTime <= 0) {
            f = Mth.sqrt((float) d0) / shootCutoffRange;
            float f1 = f;

            if (f < 0.1F) {
                f1 = 0.1F;
            }

            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            if (d0 >= (double) shootCutoffRange * (double) shootCutoffRange) {
                rangedAttackEntityHost.performRangedAttack(attackTarget, f1);
                rangedAttackTime = maxRangedAttackTime;
            } else if (d0 <= meleeHitRange * meleeHitRange) {
                entityHost.doHurtTarget(attackTarget);
                entityHost.swing(InteractionHand.MAIN_HAND);
                rangedAttackTime = maxMeleeAttackTime;
            }
        }
    }
}
