package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class MonkeyAngryThrowGoal extends Goal {
  private final VMonkeyEntity entity;
  private final float speedModifier;
  private final float stopDistance;
  private final PathNavigator navigation;
  private float oldWaterCost;
  private int timeToRecalcPath;
  private int madMeter;
  private ItemEntity trackedMug;
  private LivingEntity trackedPlayer;

  public MonkeyAngryThrowGoal(VMonkeyEntity monkeyEntity) {
    this.entity = monkeyEntity;
    setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    this.speedModifier = 1.2F;
    this.stopDistance = 1.0F;
    this.navigation = monkeyEntity.getNavigation();
  }

  @Override
  public void stop() {
    navigation.stop();
    this.madMeter = 0;
    entity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    this.trackedMug = null;
    this.trackedPlayer = null;
  }

  @Override
  public void start() {
    this.timeToRecalcPath = 0;
    this.madMeter = 100;
    this.oldWaterCost = entity.getPathfindingMalus(PathNodeType.WATER);
    entity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    this.trackedMug = null;
    this.trackedPlayer = null;
  }

  @Override
  public boolean canUse() {
    return !entity.isTame() && !entity.isLeashed() && this.entity.isMadAboutStolenAlcohol();
  }

  @Override
  public boolean canContinueToUse() {
    return !entity.isTame() && !entity.isLeashed() && this.entity.isMadAboutStolenAlcohol();
  }

  @Override
  public void tick() {
    if (this.trackedMug != null && this.entity.getMainHandItem().getItem() == TropicraftItems.BAMBOO_MUG.get().getItem()) {
      this.trackedPlayer = nearbyPlayer();

      if (this.trackedPlayer != null) {
        this.entity.getLookControl().setLookAt(this.trackedPlayer, 10.0F, (float) entity.getMaxHeadXRot());

        if (entity.distanceToSqr(this.trackedPlayer) < 4) {
          leapTowardTarget(this.trackedPlayer);
          entity.spawnAtLocation(this.entity.getMainHandItem());
          entity.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
          entity.setMadAboutStolenAlcohol(false);
        } else {
          moveTowardsEntity(this.trackedPlayer);
        }
      }
      return;
    }

    if (this.trackedMug != null && this.trackedMug.isAlive()) {
      this.entity.getLookControl().setLookAt(this.trackedMug, 10.0F, (float) entity.getMaxHeadXRot());

      if (entity.distanceToSqr(this.trackedMug) > (double) (stopDistance * stopDistance)) {
        moveTowardsEntity(this.trackedMug);
      } else {
        entity.setItemInHand(Hand.MAIN_HAND, this.trackedMug.getItem());
        this.trackedMug.remove();
      }
      return;
    }

    if (--this.madMeter <= 0) {
      this.entity.setMadAboutStolenAlcohol(false);
      return;
    }
    this.trackedMug = nearbyMug();
  }

  private LivingEntity nearbyPlayer() {
    List<PlayerEntity> list = entity.level.getEntitiesOfClass(PlayerEntity.class, entity.getBoundingBox().inflate(20.0D));

    if (!list.isEmpty()) {
      for (PlayerEntity entityliving : list) {
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

      if (d3 > (double)(stopDistance * stopDistance)) {
        navigation.moveTo(itemEntity, speedModifier);
      } else {
        navigation.stop();

        if (d3 <= (double)stopDistance) {
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
    float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
    final Vector3d motion = entity.getDeltaMovement();

    if ((double)f >= 1.0E-4D) {
      entity.setDeltaMovement(motion.add(d0 / (double)f * 0.5D * 0.800000011920929D + motion.x * 0.20000000298023224D, 0, d1 / (double)f * 0.5D * 0.800000011920929D + motion.z * 0.20000000298023224D));
    }

    entity.setDeltaMovement(new Vector3d(motion.x, 0.25, motion.z));
  }

  private ItemEntity nearbyMug() {
    List<ItemEntity> list = entity.level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(10.0D));

    if (!list.isEmpty()) {
      for (ItemEntity item : list) {
        if (!item.isInvisible()) {
          if (item.getItem().sameItem(new ItemStack(TropicraftItems.BAMBOO_MUG.get())) && item.isAlive()) {
            return item;
          }
        }
      }
    }
    return null;
  }
}
