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
    setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    this.speedModifier = 1.2F;
    this.stopDistance = 1.0F;
    this.navigation = monkeyEntity.getNavigator();
  }

  @Override
  public void resetTask() {
    navigation.clearPath();
    this.madMeter = 0;
    entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    this.trackedMug = null;
    this.trackedPlayer = null;
  }

  @Override
  public void startExecuting() {
    this.timeToRecalcPath = 0;
    this.madMeter = 100;
    this.oldWaterCost = entity.getPathPriority(PathNodeType.WATER);
    entity.setPathPriority(PathNodeType.WATER, 0.0F);
    this.trackedMug = null;
    this.trackedPlayer = null;
  }

  @Override
  public boolean shouldExecute() {
    return !entity.isTamed() && !entity.getLeashed() && this.entity.isMadAboutStolenAlcohol();
  }

  @Override
  public boolean shouldContinueExecuting() {
    return !entity.isTamed() && !entity.getLeashed() && this.entity.isMadAboutStolenAlcohol();
  }

  @Override
  public void tick() {
    if (this.trackedMug != null && this.entity.getHeldItemMainhand().getItem() == TropicraftItems.BAMBOO_MUG.get().getItem()) {
      this.trackedPlayer = nearbyPlayer();

      if (this.trackedPlayer != null) {
        this.entity.getLookController().setLookPositionWithEntity(this.trackedPlayer, 10.0F, (float) entity.getVerticalFaceSpeed());

        if (entity.getDistanceSq(this.trackedPlayer) < 4) {
          leapTowardTarget(this.trackedPlayer);
          entity.entityDropItem(this.entity.getHeldItemMainhand());
          entity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
          entity.setMadAboutStolenAlcohol(false);
        } else {
          moveTowardsEntity(this.trackedPlayer);
        }
      }
      return;
    }

    if (this.trackedMug != null && this.trackedMug.isAlive()) {
      this.entity.getLookController().setLookPositionWithEntity(this.trackedMug, 10.0F, (float) entity.getVerticalFaceSpeed());

      if (entity.getDistanceSq(this.trackedMug) > (double) (stopDistance * stopDistance)) {
        moveTowardsEntity(this.trackedMug);
      } else {
        entity.setHeldItem(Hand.MAIN_HAND, this.trackedMug.getItem());
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
    List<PlayerEntity> list = entity.world.getEntitiesWithinAABB(PlayerEntity.class, entity.getBoundingBox().grow(20.0D));

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
      double d0 = entity.getPosX() - itemEntity.getPosX();
      double d1 = entity.getPosY() - itemEntity.getPosY();
      double d2 = entity.getPosZ() - itemEntity.getPosZ();
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;

      if (d3 > (double)(stopDistance * stopDistance)) {
        navigation.tryMoveToEntityLiving(itemEntity, speedModifier);
      } else {
        navigation.clearPath();

        if (d3 <= (double)stopDistance) {
          double d4 = itemEntity.getPosX() - entity.getPosX();
          double d5 = itemEntity.getPosZ() - entity.getPosZ();
          navigation.tryMoveToXYZ(entity.getPosX() - d4, entity.getPosY(), entity.getPosZ() - d5, speedModifier);
        }
      }
    }
  }

  private void leapTowardTarget(LivingEntity leapTarget) {
    if (leapTarget == null) return;

    double d0 = leapTarget.getPosX() - entity.getPosX();
    double d1 = leapTarget.getPosZ() - entity.getPosZ();
    float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
    final Vector3d motion = entity.getMotion();

    if ((double)f >= 1.0E-4D) {
      entity.setMotion(motion.add(d0 / (double)f * 0.5D * 0.800000011920929D + motion.x * 0.20000000298023224D, 0, d1 / (double)f * 0.5D * 0.800000011920929D + motion.z * 0.20000000298023224D));
    }

    entity.setMotion(new Vector3d(motion.x, 0.25, motion.z));
  }

  private ItemEntity nearbyMug() {
    List<ItemEntity> list = entity.world.getEntitiesWithinAABB(ItemEntity.class, entity.getBoundingBox().grow(10.0D));

    if (!list.isEmpty()) {
      for (ItemEntity item : list) {
        if (!item.isInvisible()) {
          if (item.getItem().isItemEqual(new ItemStack(TropicraftItems.BAMBOO_MUG.get())) && item.isAlive()) {
            return item;
          }
        }
      }
    }
    return null;
  }
}
