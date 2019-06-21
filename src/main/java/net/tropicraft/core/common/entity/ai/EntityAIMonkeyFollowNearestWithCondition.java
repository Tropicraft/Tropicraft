package net.tropicraft.core.common.entity.ai;

import com.google.common.base.Predicate;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.item.ItemCocktail;

public class EntityAIMonkeyFollowNearestWithCondition extends Goal
{
    private final EntityVMonkey entity;
    private final Predicate<LivingEntity> followPredicate;
    private final double speedModifier;
    private final PathNavigator navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;
    private int followCounter = 0;

    public EntityAIMonkeyFollowNearestWithCondition(final EntityVMonkey entity, double speedModifier, float stopDistance, float areaSize, Predicate<LivingEntity> predicate)
    {
        this.entity = entity;
        this.followPredicate = predicate;
        this.speedModifier = speedModifier;
        this.navigation = entity.getNavigator();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setMutexBits(1);

        if (!(entity.getNavigator() instanceof GroundPathNavigator) && !(entity.getNavigator() instanceof FlyingPathNavigator))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.entity.isSitting()) return false;
        if (this.entity.isTamed()) return false;
        if (this.entity.selfHoldingDrink(Drink.pinaColada)) return false;

        List<PlayerEntity> list = this.entity.world.<PlayerEntity>getEntitiesWithinAABB(PlayerEntity.class, this.entity.getEntityBoundingBox().grow((double)this.areaSize), this.followPredicate);

        if (!list.isEmpty()) {
            for (PlayerEntity entityliving : list) {
                if (!entityliving.isInvisible()) {
                    this.entity.setFollowingEntity(entityliving);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.entity.getFollowingEntity() != null &&
                this.entity.followingHoldingPinaColada() &&
                !this.entity.isTamed();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.entity.setFollowingEntity(null);
        this.entity.setAngry(false);
        this.entity.setAttackTarget(null);
        this.navigation.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        this.followCounter = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
        if (this.entity.getFollowingEntity() != null && !this.entity.getLeashed()) {
            followCounter++;

            if (followCounter >= 20) {
                this.entity.setAngry(true);
                this.entity.setAttackTarget(this.entity.getFollowingEntity());
            }

            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getFollowingEntity(), 10.0F, (float)this.entity.getVerticalFaceSpeed());

            if (this.entity.getDistanceSq(this.entity.getFollowingEntity()) > (double)(this.stopDistance * this.stopDistance)) {
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = 10;
                    double d0 = this.entity.posX - this.entity.getFollowingEntity().posX;
                    double d1 = this.entity.posY - this.entity.getFollowingEntity().posY;
                    double d2 = this.entity.posZ - this.entity.getFollowingEntity().posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d3 > (double)(this.stopDistance * this.stopDistance)) {
                        this.navigation.tryMoveToEntityLiving(this.entity.getFollowingEntity(), this.speedModifier);
                    } else {
                        this.navigation.clearPath();

                        if (d3 <= (double)this.stopDistance) {
                            double d4 = this.entity.getFollowingEntity().posX - this.entity.posX;
                            double d5 = this.entity.getFollowingEntity().posZ - this.entity.posZ;
                            this.navigation.tryMoveToXYZ(this.entity.posX - d4, this.entity.posY, this.entity.posZ - d5, this.speedModifier);
                        }
                    }
                }
            }
        }
    }
}