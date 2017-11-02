package net.tropicraft.core.common.entity.passive;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityFailgull extends EntityFlying {

	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	public boolean inFlock;
	public EntityFailgull leader;

	public int flockCount = 0;

	public int flockPosition = 0;

	public EntityFailgull(World world) {
		super(world);
		setSize(0.4F, 0.6F);
		this.experienceValue = 1;
		this.moveHelper = new EntityFailgull.FailgullMoveHelper(this);
		this.enablePersistence();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(5, new EntityFailgull.AIRandomFly(this));
		this.tasks.addTask(7, new EntityFailgull.AILookAround(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
	}

	@Override
	public void entityInit() {
		super.entityInit();
	}

	private void poop() {
		if (!world.isRemote && world.rand.nextInt(20) == 0) {
			EntitySnowball s = new EntitySnowball(world, posX, posY, posZ);
			s.setThrowableHeading(0, 0, 0, 0, 0);
			world.spawnEntity(s);
		}
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}

	@Override
	protected SoundEvent getHurtSound() {
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

	static class AILookAround extends EntityAIBase {
		private EntityFailgull parentEntity;

		public AILookAround(EntityFailgull ghast) {
			this.parentEntity = ghast;
			this.setMutexBits(2);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {
			return true;
		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			if (this.parentEntity.getAttackTarget() == null) {
				this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float)Math.PI);
			} else {
				EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
				double d0 = 64.0D;

				if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0) {
					double d1 = entitylivingbase.posX - this.parentEntity.posX;
					double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
					this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
				}
			}
		}
	}

	static class AIRandomFly extends EntityAIBase {
		private EntityFailgull parentEntity;

		public AIRandomFly(EntityFailgull gull) {
			this.parentEntity = gull;
			this.setMutexBits(1);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {
			EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

			if (!entitymovehelper.isUpdating()) {
				return true;
			} else {
				double d0 = entitymovehelper.getX() - this.parentEntity.posX;
				double d1 = entitymovehelper.getY() - this.parentEntity.posY;
				double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean continueExecuting() {
			return false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			Random random = this.parentEntity.getRNG();
			double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
		}
	}

	static class FailgullMoveHelper extends EntityMoveHelper {
		private EntityFailgull failgull;
		private int courseChangeCooldown;
		
		public double waypointX;
		public double waypointY;
		public double waypointZ;

		public FailgullMoveHelper(EntityFailgull gull) {
			super(gull);
			this.failgull = gull;
		}

		@Override
		public void onUpdateMoveHelper() {
			double d0 = this.waypointX - this.posX;
			double d1 = this.waypointY - this.posY;
			double d2 = this.waypointZ - this.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d3 < 1.0D || d3 > 3600.0D) {
				this.waypointX = this.posX + (double)((failgull.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
				this.waypointY = this.posY + (double)((failgull.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
				this.waypointZ = this.posZ + (double)((failgull.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			}

			if (this.courseChangeCooldown-- <= 0) {
				this.courseChangeCooldown += failgull.rand.nextInt(5) + 2;
				d3 = (double)MathHelper.sqrt(d3);

				if (this.isNotColliding(this.waypointX, this.waypointY, this.waypointZ, d3)) {
					failgull.motionX += d0 / d3 * 0.1D;
					failgull.motionY += d1 / d3 * 0.1D;
					failgull.motionZ += d2 / d3 * 0.1D;
				} else {
					this.waypointX = this.posX;
					this.waypointY = this.posY;
					this.waypointZ = this.posZ;
				}
			}

			if (failgull.leader != null) {
				if (failgull.flockPosition % 2 == 0) {
					this.waypointX = failgull.leader.waypointX;
					this.waypointY = failgull.leader.waypointY;
					this.waypointZ = failgull.leader.waypointZ;
				} else {
					this.waypointX = failgull.leader.waypointX;
					this.waypointY = failgull.leader.waypointY;
					this.waypointZ = failgull.leader.waypointZ;
				}
			}

			if (!failgull.inFlock) {
				List list = failgull.world.getEntitiesWithinAABB(EntityFailgull.class, failgull.getEntityBoundingBox().expand(10D, 10D, 10D));

				int lowest = failgull.getEntityId();
				EntityFailgull f = null;

				for (Object o : list) {
					f = (EntityFailgull) o;

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
			double d0 = (x - this.failgull.posX) / p_179926_7_;
			double d1 = (y - this.failgull.posY) / p_179926_7_;
			double d2 = (z - this.failgull.posZ) / p_179926_7_;
			AxisAlignedBB axisalignedbb = this.failgull.getEntityBoundingBox();

			for (int i = 1; (double)i < p_179926_7_; ++i) {
				axisalignedbb = axisalignedbb.offset(d0, d1, d2);

				if (!this.failgull.world.getCollisionBoxes(this.failgull, axisalignedbb).isEmpty()) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * This is required if spawning an IMob based entity in the creature list that is also marked persistant
	 *
	 * @param type
	 * @param forSpawnCount
	 * @return
	 */
	@Override
	public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
		return type == EnumCreatureType.CREATURE;
	}
}
