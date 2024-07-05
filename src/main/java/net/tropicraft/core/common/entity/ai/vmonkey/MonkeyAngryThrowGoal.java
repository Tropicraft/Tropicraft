package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.EnumSet;
import java.util.List;

public class MonkeyAngryThrowGoal extends Goal {
    private final VMonkeyEntity entity;
    private final float speedModifier;
    private final float stopDistance;
    private final PathNavigation navigation;
    private float oldWaterCost;
    private int timeToRecalcPath;
    private int madMeter;
    private ItemEntity trackedMug;
    private LivingEntity trackedPlayer;

    public MonkeyAngryThrowGoal(VMonkeyEntity monkeyEntity) {
        entity = monkeyEntity;
        setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        speedModifier = 1.2F;
        stopDistance = 1.0F;
        navigation = monkeyEntity.getNavigation();
    }

    @Override
    public void stop() {
        navigation.stop();
        madMeter = 0;
        entity.setPathfindingMalus(PathType.WATER, oldWaterCost);
        trackedMug = null;
        trackedPlayer = null;
    }

    @Override
    public void start() {
        timeToRecalcPath = 0;
        madMeter = 100;
        oldWaterCost = entity.getPathfindingMalus(PathType.WATER);
        entity.setPathfindingMalus(PathType.WATER, 0.0F);
        trackedMug = null;
        trackedPlayer = null;
    }

    @Override
    public boolean canUse() {
        return !entity.isTame() && !entity.isLeashed() && entity.isMadAboutStolenAlcohol();
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.isTame() && !entity.isLeashed() && entity.isMadAboutStolenAlcohol();
    }

    @Override
    public void tick() {
        if (trackedMug != null && entity.getMainHandItem().getItem() == TropicraftItems.BAMBOO_MUG.get().asItem()) {
            trackedPlayer = nearbyPlayer();

            if (trackedPlayer != null) {
                entity.getLookControl().setLookAt(trackedPlayer, 10.0F, (float) entity.getMaxHeadXRot());

                if (entity.distanceToSqr(trackedPlayer) < 4) {
                    leapTowardTarget(trackedPlayer);
                    entity.spawnAtLocation(entity.getMainHandItem());
                    entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    entity.setMadAboutStolenAlcohol(false);
                } else {
                    moveTowardsEntity(trackedPlayer);
                }
            }
            return;
        }

        if (trackedMug != null && trackedMug.isAlive()) {
            entity.getLookControl().setLookAt(trackedMug, 10.0F, (float) entity.getMaxHeadXRot());

            if (entity.distanceToSqr(trackedMug) > (double) (stopDistance * stopDistance)) {
                moveTowardsEntity(trackedMug);
            } else {
                entity.setItemInHand(InteractionHand.MAIN_HAND, trackedMug.getItem());
                trackedMug.remove(Entity.RemovalReason.DISCARDED);
            }
            return;
        }

        if (--madMeter <= 0) {
            entity.setMadAboutStolenAlcohol(false);
            return;
        }
        trackedMug = nearbyMug();
    }

    private LivingEntity nearbyPlayer() {
        List<Player> list = entity.level().getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(20.0D));

        if (!list.isEmpty()) {
            for (Player entityliving : list) {
                if (!entityliving.isInvisible()) {
                    return entityliving;
                }
            }
        }

        return null;
    }

    private void moveTowardsEntity(Entity itemEntity) {
        if (--timeToRecalcPath <= 0) {
            timeToRecalcPath = 10;
            double d0 = entity.getX() - itemEntity.getX();
            double d1 = entity.getY() - itemEntity.getY();
            double d2 = entity.getZ() - itemEntity.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > (double) (stopDistance * stopDistance)) {
                navigation.moveTo(itemEntity, speedModifier);
            } else {
                navigation.stop();

                if (d3 <= (double) stopDistance) {
                    double d4 = itemEntity.getX() - entity.getX();
                    double d5 = itemEntity.getZ() - entity.getZ();
                    navigation.moveTo(entity.getX() - d4, entity.getY(), entity.getZ() - d5, speedModifier);
                }
            }
        }
    }

    private void leapTowardTarget(LivingEntity leapTarget) {
        if (leapTarget == null) return;

        double d0 = leapTarget.getX() - entity.getX();
        double d1 = leapTarget.getZ() - entity.getZ();
        float f = Mth.sqrt((float) (d0 * d0 + d1 * d1));
        Vec3 motion = entity.getDeltaMovement();

        if ((double) f >= 1.0E-4D) {
            entity.setDeltaMovement(motion.add(d0 / (double) f * 0.5D * 0.800000011920929D + motion.x * 0.20000000298023224D, 0, d1 / (double) f * 0.5D * 0.800000011920929D + motion.z * 0.20000000298023224D));
        }

        entity.setDeltaMovement(new Vec3(motion.x, 0.25, motion.z));
    }

    private ItemEntity nearbyMug() {
        List<ItemEntity> list = entity.level().getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(10.0D));

        if (!list.isEmpty()) {
            for (ItemEntity item : list) {
                if (!item.isInvisible()) {
                    if (item.getItem().is(TropicraftItems.BAMBOO_MUG.get()) && item.isAlive()) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}
