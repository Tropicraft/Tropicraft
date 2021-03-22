package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

import java.util.List;

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
        if (monkey.isQueuedToSit()) return false;
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
                double xDist = monkey.getPosX() - following.getPosX();
                double yDist = monkey.getPosY() - following.getPosY();
                double zDist = monkey.getPosZ() - following.getPosZ();
                double sqrDist = xDist * xDist + yDist * yDist + zDist * zDist;
                if (sqrDist > (double)(stopDistance * stopDistance)) {
                    navigation.tryMoveToEntityLiving(following, speedModifier);
                } else {
                    navigation.clearPath();
                    if (sqrDist <= (double)stopDistance) {
                        double xDist2 = following.getPosX() - monkey.getPosX();
                        double zDist2 = following.getPosZ() - monkey.getPosZ();
                        navigation.tryMoveToXYZ(monkey.getPosX() - xDist2, monkey.getPosY(), monkey.getPosZ() - zDist2, speedModifier);
                    }

                }
            }
        }
    }
}