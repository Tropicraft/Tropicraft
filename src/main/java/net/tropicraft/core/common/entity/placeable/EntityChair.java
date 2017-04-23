package net.tropicraft.core.common.entity.placeable;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityChair extends Entity {
	// TODO add drips after being wet
	// TODO make it so monkies can sit in the chair ouo
	
	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);
	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntityChair.class, DataSerializers.FLOAT);
	private static final DataParameter<Byte> COMESAILAWAY = EntityDataManager.<Byte>createKey(EntityChair.class, DataSerializers.BYTE);
	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);

	/** Is any entity sitting in the chair? */
	public boolean isChairEmpty;

	private int chairPosRotationIncrements;
	private double chairX;
	private double chairY;
	private double chairZ;
	private double chairPitch;
	private double chairYaw;

	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;

	/** Acceleration */
	private double speedMultiplier;

	public EntityChair(World world) {
		super(world);
		this.ignoreFrustumCheck = true;
		this.isChairEmpty = true;
		this.speedMultiplier = 0.10D;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = .95F;
		this.setSize(1F, 1F);
	}

	public EntityChair(World world, double x, double y, double z, int color, EntityPlayer player) {
		this(world);
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		setColor(color);		
		rotationYaw = this.getAngleToPlayer(player);
	}
	
    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamage() > 0.0F) {
			this.setDamage(this.getDamage() - 1.0F);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte b0 = 5;
		double d0 = 0.0D;

		if (this.getComeSailAway())
			for (int i = 0; i < b0; ++i) {
				double d1 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(i + 0) / (double)b0 - 0.125D;
				double d3 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(i + 1) / (double)b0 - 0.125D;
				AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getEntityBoundingBox().minX, d1, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, d3, this.getEntityBoundingBox().maxZ);

				if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.WATER)) {
					d0 += 1.0D / (double)b0;
				}
			}

		double d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double d2;
		double d4;
		int j;

		if (/*this.getComeSailAway() && */d10 > 0.26249999999999996D) {
			d2 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
			d4 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);

			if (this.getComeSailAway())
				for (j = 0; (double)j < 1.0D + d10 * 60.0D; ++j) {
					double d5 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
					double d6 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
					double d8;
					double d9;

					if (this.rand.nextBoolean()) {
						d8 = this.posX - d2 * d5 * 0.8D + d4 * d6;
						d9 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
						this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
					} else {
						d8 = this.posX + d2 + d4 * d5 * 0.7D;
						d9 = this.posZ + d4 - d2 * d5 * 0.7D;
						this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
					}
				}
		}

		double d11;
		double d12;

		if (this.worldObj.isRemote && this.isChairEmpty) {
			if (this.chairPosRotationIncrements > 0) {
				d2 = this.posX + (this.chairX - this.posX) / (double)this.chairPosRotationIncrements;
				d4 = this.posY + (this.chairY - this.posY) / (double)this.chairPosRotationIncrements;
				d11 = this.posZ + (this.chairZ - this.posZ) / (double)this.chairPosRotationIncrements;
				d12 = MathHelper.wrapDegrees(this.chairYaw - (double)this.rotationYaw);
				this.rotationYaw = (float)((double)this.rotationYaw + d12 / (double)this.chairPosRotationIncrements);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.chairPitch - (double)this.rotationPitch) / (double)this.chairPosRotationIncrements);
				--this.chairPosRotationIncrements;
				this.setPosition(d2, d4, d11);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				d2 = this.posX + this.motionX;
				d4 = this.posY + this.motionY;
				d11 = this.posZ + this.motionZ;
				this.setPosition(d2, d4, d11);

				if (this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		} else {
			if (d0 < 1.0D) {
				d2 = d0 * 2.0D - 1.0D;
				this.motionY += 0.03999999910593033D * d2;
			} else {
				if (this.motionY < 0.0D) {
					this.motionY /= 2.0D;
				}

				this.motionY += 0.007000000216066837D;
			}

			if (this.getComeSailAway() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
				float f = this.getControllingPassenger().rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
				this.motionX += -Math.sin((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
				this.motionZ += Math.cos((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
			}

			d2 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

			if (d2 > 0.45D) {
				d4 = 0.45D / d2;
				this.motionX *= d4;
				this.motionZ *= d4;
				d2 = 0.45D;
			}

			if (d2 > d10 && this.speedMultiplier < 0.45D) {
				this.speedMultiplier += (0.45D - this.speedMultiplier) / 45.0D;

				if (this.speedMultiplier > 0.45D) {
					this.speedMultiplier = 0.45D;
				}
			} else {
				this.speedMultiplier -= (this.speedMultiplier - 0.10D) / 45.0D;

				if (this.speedMultiplier < 0.10D) {
					this.speedMultiplier = 0.10D;
				}
			}

			int l;

			if (this.getComeSailAway())
				for (l = 0; l < 4; ++l) {
					int i1 = MathHelper.floor_double(this.posX + ((double)(l % 2) - 0.5D) * 0.8D);
					j = MathHelper.floor_double(this.posZ + ((double)(l / 2) - 0.5D) * 0.8D);

					for (int j1 = 0; j1 < 2; ++j1) {
						int k = MathHelper.floor_double(this.posY) + j1;
						Block block = this.worldObj.getBlockState(new BlockPos(i1, k, j)).getBlock();
						BlockPos pos = new BlockPos(i1, k, j);
						
						if (block == Blocks.SNOW_LAYER) {
							this.worldObj.setBlockToAir(pos);
							this.isCollidedHorizontally = false;
						} else 
							if (block == Blocks.WATERLILY) {
								this.worldObj.setBlockToAir(pos);
								this.isCollidedHorizontally = false;
							}
					}
				}

			if (this.getComeSailAway() && this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			} else
				if (this.onGround) {
					this.motionX = 0;
					this.motionY = 0;
					this.motionZ = 0;
				}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);

			// This will never trigger since d10 will only ever get up to 0.45 >:D *evil laugh*
			// In other words, when come sail away, there is no stopping this sucker
			if (this.getComeSailAway()) {
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}

			this.rotationPitch = 0.0F;
			d4 = (double)this.rotationYaw;
			d11 = this.prevPosX - this.posX;
			d12 = this.prevPosZ - this.posZ;

			if (d11 * d11 + d12 * d12 > 0.001D) {
				d4 = (double)((float)(Math.atan2(d12, d11) * 180.0D / Math.PI));
			}

			double d7 = MathHelper.wrapDegrees(d4 - (double)this.rotationYaw);

			if (d7 > 20.0D) {
				d7 = 20.0D;
			}

			if (d7 < -20.0D) {
				d7 = -20.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d7);
			this.setRotation(this.rotationYaw, this.rotationPitch);

			if (!this.worldObj.isRemote) {
				List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

				if (list != null && !list.isEmpty()) {
					for (int k1 = 0; k1 < list.size(); ++k1) {
						Entity entity = (Entity)list.get(k1);

						if (entity != this.getControllingPassenger() && entity.canBePushed() && entity instanceof EntityChair) {
							entity.applyEntityCollision(this);
						}
					}
				}

				if (this.getControllingPassenger() != null && this.getControllingPassenger().isDead) {
					this.removePassengers();
				}
			}
		}
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(COLOR, Integer.valueOf(ColorHelper.DEFAULT_VALUE));
		this.getDataManager().register(DAMAGE, new Float(0));
		this.getDataManager().register(COMESAILAWAY, new Byte((byte)0));
		this.getDataManager().register(FORWARD_DIRECTION, new Integer(1));
		this.getDataManager().register(TIME_SINCE_HIT, new Integer(0));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
		this.setComeSailAway(Boolean.valueOf(nbt.getBoolean("COME_SAIL_AWAY")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
		nbt.setBoolean("COME_SAIL_AWAY", Boolean.valueOf(this.getComeSailAway()));
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
		if (this.isEntityInvulnerable(damageSource)) {
			return false;
		}
		else if (!this.worldObj.isRemote && !this.isDead) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamage(this.getDamage() + par2 * 10.0F);
			this.setBeenAttacked();
			boolean flag = damageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)damageSource.getEntity()).capabilities.isCreativeMode;

			if (flag || this.getDamage() > 40.0F) {
				if (!flag) {
					this.entityDropItem(new ItemStack(ItemRegistry.chair, 1,  getDamageFromColor()), 0.0F);
				}

				this.setDead();
			}

			return true;
		} else {
			return true;
		}
	}
	
	/**
	 * @return Returns the damage value associated with the color of this chair
	 */
	public int getDamageFromColor() {
		return ColorHelper.getDamageFromColor(this.getColor());
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamage(this.getDamage() * 11.0F);
	}
	
	@Override
    public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
        if (!this.worldObj.isRemote && !player.isSneaking()) {
            player.startRiding(this);
        }

        return true;
    }

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
	 * posY, posZ, yaw, pitch
	 */
//	@SideOnly(Side.CLIENT)
//	@Override
//	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
//	{
//		if (this.isChairEmpty)
//		{
//			this.chairPosRotationIncrements = par9 + 5;
//		}
//		else
//		{
//			double d3 = par1 - this.posX;
//			double d4 = par3 - this.posY;
//			double d5 = par5 - this.posZ;
//			double d6 = d3 * d3 + d4 * d4 + d5 * d5;
//
//			if (d6 <= 1.0D)
//			{
//				return;
//			}
//
//			this.chairPosRotationIncrements = 3;
//		}
//
//		this.chairX = par1;
//		this.chairY = par3;
//		this.chairZ = par5;
//		this.chairYaw = (double)par7;
//		this.chairPitch = (double)par8;
//		this.motionX = this.velocityX;
//		this.motionY = this.velocityY;
//		this.motionZ = this.velocityZ;
//	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void setVelocity(double xVelocity, double yVelocity, double zVelocity) {
		this.velocityX = this.motionX = xVelocity;
		this.velocityY = this.motionY = yVelocity;
		this.velocityZ = this.motionZ = zVelocity;
	}
	
    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : (Entity)list.get(0);
    }

	/**
	 * Update rider position with x, y, and z offsets
	 */
//	@Override
//	public void updateRiderPosition() {
//		if (this.getControllingPassenger() != null) {
//			double xOffset = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
//			double zOffset = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
//			this.getControllingPassenger().setPosition(this.posX + xOffset, this.posY + this.getMountedYOffset() + this..getControllingPassenger().getYOffset(), this.posZ + zOffset);
//		}
//	}

	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.5F;
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding this one.
	 */
	@Override
	public double getMountedYOffset() {
		return (double)this.height - 0.65D;
	}

	/**
	 * Given a player, get the angle that the chair should be at to face the player
	 * @param player
	 * @return The angle the chair should be at to face the players
	 */
	private float getAngleToPlayer(EntityPlayer player) {
		float angle = MathHelper.wrapDegrees(player.rotationYaw);
		return angle;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return false;
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
	 * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
	 * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
	 */
//	@Override
//	protected void updateFallState(double distanceFallenThisTick, boolean onGround) {
//		int i = MathHelper.floor_double(this.posX);
//		int j = MathHelper.floor_double(this.posY);
//		int k = MathHelper.floor_double(this.posZ);
//
//		if (onGround) {
//			if (this.fallDistance > 3.0F) {
//				this.fall(this.fallDistance);
//
//				if (!this.worldObj.isRemote && !this.isDead) {
//					this.setDead();
//					int l;
//
//					for (l = 0; l < 3; ++l) {
//						this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getDamageFromColor()), 0.0F);
//					}
//
//					for (l = 0; l < rand.nextInt(5) + 1; ++l) {
//						this.dropItem(TCItemRegistry.bambooStick, 1);
//					}
//				}
//
//				this.fallDistance = 0.0F;
//			}
//		}
//		else if (this.worldObj.getBlock(i, j - 1, k).getMaterial() != Material.water && distanceFallenThisTick < 0.0D)
//		{
//			this.fallDistance = (float)((double)this.fallDistance - distanceFallenThisTick);
//		}
//	}

	public void setColor(int color) {
		this.dataManager.set(COLOR, Integer.valueOf(color));
	}

	public void setColor(float red, float green, float blue) {
		this.dataManager.set(COLOR, Integer.valueOf(ColorHelper.getColor(red, green, blue)));
	}

	public int getColor() {
		return ((Integer)this.dataManager.get(COLOR)).intValue();
	}

	public void setComeSailAway(boolean sail) {
		this.dataManager.set(COMESAILAWAY, sail ? Byte.valueOf((byte)1) : Byte.valueOf((byte)0));
	}

	public boolean getComeSailAway() {
		return this.dataManager.get(COMESAILAWAY) == (byte)1;
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int dir) {
		this.dataManager.set(FORWARD_DIRECTION, Integer.valueOf(dir));
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return ((Integer)this.dataManager.get(FORWARD_DIRECTION)).intValue();
	}

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamage(float damageTaken) {
        this.dataManager.set(DAMAGE, Float.valueOf(damageTaken));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamage() {
        return ((Float)this.dataManager.get(DAMAGE)).floatValue();
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit) {
        this.dataManager.set(TIME_SINCE_HIT, Integer.valueOf(timeSinceHit));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return ((Integer)this.dataManager.get(TIME_SINCE_HIT)).intValue();
    }
}
