package net.tropicraft.core.common.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class FailgullEntity extends FlyingEntity {

	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	public boolean inFlock;
	public FailgullEntity leader;

	public int flockCount = 0;

	public int flockPosition = 0;

	public FailgullEntity(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		experienceValue = 1;
		moveController = new FailgullMoveHelper(this);
	}

	@Override
	public void registerGoals() {
		goalSelector.addGoal(5, new FailgullEntity.AIRandomFly(this));
		goalSelector.addGoal(7, new FailgullEntity.AILookAround(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
	}

	private void poop() {
		if (!world.isRemote && world.rand.nextInt(20) == 0) {
			SnowballEntity s = new SnowballEntity(world, getPosX(), getPosY(), getPosZ());
			s.shoot(0, 0, 0, 0, 0);
			world.addEntity(s);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	static class AILookAround extends Goal {
		private FailgullEntity parentEntity;

		public AILookAround(FailgullEntity failgull) {
			this.parentEntity = failgull;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean shouldExecute() {
			return true;
		}

		@Override
		public void tick() {
			if (parentEntity.getAttackTarget() == null) {
				final Vec3d motion = parentEntity.getMotion();
				parentEntity.renderYawOffset = parentEntity.rotationYaw = -1 * ((float) MathHelper.atan2(motion.x, motion.z)) * (180F / (float) Math.PI);
			} else {
				LivingEntity entitylivingbase = parentEntity.getAttackTarget();
				double d0 = 64.0D;

				if (entitylivingbase.getDistanceSq(parentEntity) < d0 * d0) {
					double d1 = entitylivingbase.getPosX() - parentEntity.getPosX();
					double d2 = entitylivingbase.getPosZ() - parentEntity.getPosZ();
					parentEntity.renderYawOffset = parentEntity.rotationYaw = -1 * ((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
				}
			}
		}
	}

	static class AIRandomFly extends Goal {
		private FailgullEntity parentEntity;

		public AIRandomFly(FailgullEntity gull) {
			this.parentEntity = gull;
			setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			MovementController entitymovehelper = parentEntity.getMoveHelper();

			if (!entitymovehelper.isUpdating()) {
				return true;
			} else {
				double d0 = entitymovehelper.getX() - parentEntity.getPosX();
				double d1 = entitymovehelper.getY() - parentEntity.getPosY();
				double d2 = entitymovehelper.getZ() - parentEntity.getPosZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}

		@Override
		public void startExecuting() {
			final Random random = parentEntity.getRNG();
			double nextXPos = parentEntity.getPosX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double nextYPos = parentEntity.getPosY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double nextZPos = parentEntity.getPosZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			parentEntity.getMoveHelper().setMoveTo(nextXPos, nextYPos, nextZPos, 1.0D);
		}
	}

	static class FailgullMoveHelper extends MovementController {
		private FailgullEntity failgull;
		private int courseChangeCooldown;
		
		public double waypointX;
		public double waypointY;
		public double waypointZ;

		public FailgullMoveHelper(FailgullEntity failgull) {
			super(failgull);
			this.failgull = failgull;
		}

		@Override
		public void tick() {
			double d0 = waypointX - posX;
			double d1 = waypointY - posY;
			double d2 = waypointZ - posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d3 < 1.0D || d3 > 3600.0D) {
				waypointX = posX + (double)((failgull.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
				waypointY = posY + (double)((failgull.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
				waypointZ = posZ + (double)((failgull.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			}

			if (courseChangeCooldown-- <= 0) {
				courseChangeCooldown += failgull.rand.nextInt(5) + 2;
				d3 = MathHelper.sqrt(d3);

				if (isNotColliding(waypointX, waypointY, waypointZ, d3)) {
					failgull.setMotion(failgull.getMotion().add(d0 / d3 * 0.1D, d1 / d3 * 0.1D, d2 / d3 * 0.1D));
				} else {
					waypointX = posX;
					waypointY = posY;
					waypointZ = posZ;
				}
			}

			if (failgull.leader != null) {
				if (failgull.flockPosition % 2 == 0) {
					waypointX = failgull.leader.waypointX;
					waypointY = failgull.leader.waypointY;
					waypointZ = failgull.leader.waypointZ;
				} else {
					waypointX = failgull.leader.waypointX;
					waypointY = failgull.leader.waypointY;
					waypointZ = failgull.leader.waypointZ;
				}
			}

			if (!failgull.inFlock) {
				List<FailgullEntity> list = failgull.world.getEntitiesWithinAABB(FailgullEntity.class, failgull.getBoundingBox().grow(10D, 10D, 10D));

				for (final FailgullEntity f : list) {
					if (f.leader != null) {
						failgull.flockPosition = ++f.leader.flockCount;
						f.inFlock = true;
						failgull.leader = f.leader;
						break;
					}
				}
			}

			if (!failgull.inFlock && failgull.leader == null) {
				failgull.leader = failgull;
				failgull.inFlock = true;
			}
		}

		/**
		 * Checks if entity bounding box is not colliding with terrain
		 */
		private boolean isNotColliding(double x, double y, double z, double p_179926_7_) {
			double d0 = (x - failgull.getPosX()) / p_179926_7_;
			double d1 = (y - failgull.getPosY()) / p_179926_7_;
			double d2 = (z - failgull.getPosZ()) / p_179926_7_;
			AxisAlignedBB axisalignedbb = failgull.getBoundingBox();

			for (int i = 1; (double) i < p_179926_7_; ++i) {
				axisalignedbb = axisalignedbb.offset(d0, d1, d2);

				if (!failgull.world.getEntitiesWithinAABBExcludingEntity(failgull, axisalignedbb).isEmpty()) {
					return false;
				}
			}

			return true;
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.FAILGULL_SPAWN_EGG.get());
	}
}
