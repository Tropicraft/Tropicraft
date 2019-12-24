package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

import java.util.List;
import java.util.function.Predicate;

public class MonkeyFollowNearestPinaColadaHolderGoal extends Goal {
    private final VMonkeyEntity monkey;
    private final float areaSize;

    private final double speedModifier;
    private final PathNavigator navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;

    public MonkeyFollowNearestPinaColadaHolderGoal(final VMonkeyEntity entity, double speedModifier, float stopDistance, float areaSize) {
        this.monkey = entity;
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;

        navigation = monkey.getNavigator();
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (monkey.isSitting()) return false;
        if (monkey.isTamed()) return false;
        if (monkey.selfHoldingDrink(Drink.PINA_COLADA)) return false;

        List<PlayerEntity> list = monkey.world.getEntitiesWithinAABB(PlayerEntity.class, monkey.getBoundingBox().grow(areaSize), VMonkeyEntity.FOLLOW_PREDICATE);

        if (!list.isEmpty()) {
            for (PlayerEntity entityliving : list) {
                if (!entityliving.isInvisible()) {
                    monkey.setFollowing(entityliving);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (monkey.getFollowing() == null) {
            return false;
        }
        return VMonkeyEntity.FOLLOW_PREDICATE.test(monkey.getFollowing()) && shouldExecute()
                && !navigation.noPath() && monkey.getDistanceSq(monkey.getFollowing()) > (double)(stopDistance * stopDistance);
    }

    @Override
    public void startExecuting() {
        timeToRecalcPath = 0;
        oldWaterCost = monkey.getPathPriority(PathNodeType.WATER);
        monkey.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        monkey.setFollowing(null);
        navigation.clearPath();
        monkey.setPathPriority(PathNodeType.WATER, oldWaterCost);
    }

    public void tick() {
        LivingEntity following = monkey.getFollowing();
        if (following != null && !monkey.getLeashed()) {
            monkey.getLookController().setLookPositionWithEntity(following, 10.0F, (float) monkey.getVerticalFaceSpeed());
            if (--timeToRecalcPath <= 0) {
                timeToRecalcPath = 10;
                double lvt_1_1_ = monkey.posX - following.posX;
                double lvt_3_1_ = monkey.posY - following.posY;
                double lvt_5_1_ = monkey.posZ - following.posZ;
                double lvt_7_1_ = lvt_1_1_ * lvt_1_1_ + lvt_3_1_ * lvt_3_1_ + lvt_5_1_ * lvt_5_1_;
                if (lvt_7_1_ > (double)(stopDistance * stopDistance)) {
                    navigation.tryMoveToEntityLiving(following, speedModifier);
                } else {
                    navigation.clearPath();
                    if (lvt_7_1_ <= (double)stopDistance) {
                        double lvt_10_1_ = following.posX - monkey.posX;
                        double lvt_12_1_ = following.posZ - monkey.posZ;
                        navigation.tryMoveToXYZ(monkey.posX - lvt_10_1_, monkey.posY, monkey.posZ - lvt_12_1_, speedModifier);
                    }

                }
            }
        }
    }
}