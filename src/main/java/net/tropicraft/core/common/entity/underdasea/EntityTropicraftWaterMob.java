package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTropicraftWaterMob extends EntityWaterMob {

	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	public float pitch;
	public float prevPitch;
	public float yaw;
	public float prevYaw;
	/** appears to be rotation in radians; we already have pitch & yaw, so this completes the triumvirate. */
	public float rotation;
	/** previous squidRotation in radians */
	public float prevRotation;
	private float randomMotionSpeed;
	/** change in squidRotation in radians. */
	private float rotationVelocity;
	private float rotateSpeed;

	public EntityTropicraftWaterMob(World world) {
		super(world);
		this.rand.setSeed((long)(1 + this.getEntityId()));
		this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new AIMoveRandom(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.5F;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return this.posY > 45.0D && this.posY < (double)this.world.getSeaLevel() && super.getCanSpawnHere();
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
	 * true)
	 */
	@Override
	public boolean isInWater() {
		return super.isInWater();
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.prevPitch = this.pitch;
		this.prevYaw = this.yaw;
		this.prevRotation = this.rotation;
		this.rotation += this.rotationVelocity;

		if ((double)this.rotation > (Math.PI * 2D)) {
			if (this.world.isRemote) {
				this.rotation = ((float)Math.PI * 2F);
			} else {
				this.rotation = (float)((double)this.rotation - (Math.PI * 2D));

				if (this.rand.nextInt(10) == 0) {
					this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
				}

				this.world.setEntityState(this, (byte)19);
			}
		}

		if (this.inWater) {
			if (this.rotation < (float)Math.PI) {
				float f = this.rotation / (float)Math.PI;

				if ((double)f > 0.75D) {
					this.randomMotionSpeed = 1.0F;
					this.rotateSpeed = 0.5F;
				} else {
					this.rotateSpeed *= 0.8F;
				}
			} else {
				this.randomMotionSpeed *= 0.9F;
				this.rotateSpeed *= 0.99F;
			}

			if (!this.world.isRemote) {
				this.motionX = (double)(this.randomMotionVecX * this.randomMotionSpeed);
				this.motionY = (double)(this.randomMotionVecY * this.randomMotionSpeed);
				this.motionZ = (double)(this.randomMotionVecZ * this.randomMotionSpeed);
			}

			float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.renderYawOffset += (-((float)MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float)Math.PI) - this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
			this.yaw = (float)((double)this.yaw + Math.PI * (double)this.rotateSpeed * 1.5D);
			this.pitch += (-((float)MathHelper.atan2((double)f1, this.motionY)) * (180F / (float)Math.PI) - this.pitch) * 0.1F;
		} else {
			if (!this.world.isRemote) {
				this.motionX = 0.0D;
				this.motionZ = 0.0D;

				if (this.isPotionActive(MobEffects.LEVITATION)) {
					this.motionY += 0.05D * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY;
				} else {
					this.motionY -= 0.08D;
				}

				this.motionY *= 0.9800000190734863D;
			}

			this.pitch = (float)((double)this.pitch + (double)(-90.0F - this.pitch) * 0.02D);
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 19) {
            this.rotation = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }
    }

	/**
	 * Moves the entity based on the specified heading.
	 */
	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		this.move(this.motionX, this.motionY, this.motionZ);
	}

	public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
		this.randomMotionVecX = randomMotionVecXIn;
		this.randomMotionVecY = randomMotionVecYIn;
		this.randomMotionVecZ = randomMotionVecZIn;
	}

	public boolean hasMovementVector() {
		return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
	}

	static class AIMoveRandom extends EntityAIBase {
		private EntityTropicraftWaterMob waterMob;

		public AIMoveRandom(EntityTropicraftWaterMob waterMob) {
			this.waterMob = waterMob;
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
        /**
         * Updates the task
         */
        public void updateTask() {
            int i = this.waterMob.getAge();

            if (i > 100) {
                this.waterMob.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.waterMob.getRNG().nextInt(200) == 0 || !this.waterMob.inWater || !this.waterMob.hasMovementVector()) {
                float f = this.waterMob.getRNG().nextFloat() * ((float)Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.waterMob.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.waterMob.setMovementVector(f1, f2, f3);
            }
        }
	}
}
