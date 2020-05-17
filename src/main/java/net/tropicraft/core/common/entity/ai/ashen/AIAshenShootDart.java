package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
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
    public boolean shouldExecute() {
        if (entity.getAttackTarget() != null) {
            ItemStack headGear = entity.getAttackTarget().getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (headGear.getItem() instanceof AshenMaskItem) {
                return false;
            }
        }
        return entity.getAttackTarget() != null && entity.hasMask();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute() || !entity.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        super.resetTask();
        seeTime = 0;
        attackTime = -1;
        entity.resetActiveHand();
    }
    
    @Override
    public void tick() {
        LivingEntity target = entity.getAttackTarget();

        if (target != null) {
            ItemStack headGear = target.getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (headGear.getItem() instanceof AshenMaskItem) {
                return;
            }

            double d0 = entity.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
            boolean canSeeEnemy = entity.getEntitySenses().canSee(target);
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
                entity.getNavigator().clearPath();
                ++strafingTime;
            } else {
                entity.getNavigator().tryMoveToEntityLiving(target, moveSpeedAmplifier);
                strafingTime = -1;
            }

            if (strafingTime >= 20) {
                if ((double) entity.getRNG().nextFloat() < 0.3D) {
                    strafingClockwise = !strafingClockwise;
                }

                if ((double) entity.getRNG().nextFloat() < 0.3D) {
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

                entity.getMoveHelper().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
                entity.faceEntity(target, 30.0F, 30.0F);
            } else {
                entity.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
            }

            if (entity.isHandActive()) {
                if (!canSeeEnemy && seeTime < -60) {
                    entity.resetActiveHand();
                } else if (canSeeEnemy) {
                    int i = entity.getItemInUseMaxCount();

                    if (i >= 20) {
                        entity.resetActiveHand();
                        entity.attackEntityWithRangedAttack(target, (float) (14 - entity.world.getDifficulty().getId() * 4));
                        attackTime = attackCooldown;
                    }
                }
            } else if (--attackTime <= 0 && seeTime >= -60) {
                entity.setActiveHand(Hand.MAIN_HAND);
            }
        }
    }

}
