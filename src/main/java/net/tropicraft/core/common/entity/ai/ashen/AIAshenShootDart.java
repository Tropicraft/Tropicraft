package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.item.AshenMaskItem;

public class AIAshenShootDart extends Goal {

    private final AshenEntity entity;
    private int attackCooldown;
    private int attackTime = -1;
    private int seeTime;
    private final float maxAttackDistance;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private float moveSpeedAmplifier;
    
    public AIAshenShootDart(AshenEntity entity) {
        this.entity = entity;
        attackCooldown = 60;
        maxAttackDistance = 15 * 15;
        moveSpeedAmplifier = 1.0F;
    }
    
    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        if (entity.getTarget() != null) {
            ItemStack headGear = entity.getTarget().getItemBySlot(EquipmentSlot.HEAD);
            if (headGear.getItem() instanceof AshenMaskItem) {
                return false;
            }
        }
        return entity.getTarget() != null && entity.hasMask();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return canUse() || !entity.getNavigation().isDone();
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        super.stop();
        seeTime = 0;
        attackTime = -1;
        entity.stopUsingItem();
    }
    
    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();

        if (target != null) {
            ItemStack headGear = target.getItemBySlot(EquipmentSlot.HEAD);
            if (headGear.getItem() instanceof AshenMaskItem) {
                return;
            }

            double d0 = entity.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
            boolean canSeeEnemy = entity.getSensing().canSee(target);
            boolean hasSeenEnemy = seeTime > 0;

            if (canSeeEnemy != hasSeenEnemy) {
                seeTime = 0;
            }

            if (canSeeEnemy) {
                ++seeTime;
            } else {
                --seeTime;
            }

            if (d0 <= (double)maxAttackDistance && seeTime >= 20) {
                entity.getNavigation().stop();
                ++strafingTime;
            } else {
                entity.getNavigation().moveTo(target, moveSpeedAmplifier);
                strafingTime = -1;
            }

            if (strafingTime >= 20) {
                if ((double) entity.getRandom().nextFloat() < 0.3D) {
                    strafingClockwise = !strafingClockwise;
                }

                if ((double) entity.getRandom().nextFloat() < 0.3D) {
                    strafingBackwards = !strafingBackwards;
                }

                strafingTime = 0;
            }

            if (strafingTime > -1) {
                if (d0 > (double) (maxAttackDistance * 0.75F)) {
                    strafingBackwards = false;
                } else if (d0 < (double) (maxAttackDistance * 0.25F)) {
                    strafingBackwards = true;
                }

                entity.getMoveControl().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
                entity.lookAt(target, 30.0F, 30.0F);
            } else {
                entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (entity.isUsingItem()) {
                if (!canSeeEnemy && seeTime < -60) {
                    entity.stopUsingItem();
                } else if (canSeeEnemy) {
                    int i = entity.getTicksUsingItem();

                    if (i >= 20) {
                        entity.stopUsingItem();
                        entity.performRangedAttack(target, (float) (14 - entity.level.getDifficulty().getId() * 4));
                        attackTime = attackCooldown;
                    }
                }
            } else if (--attackTime <= 0 && seeTime >= -60) {
                entity.startUsingItem(InteractionHand.MAIN_HAND);
            }
        }
    }

}
