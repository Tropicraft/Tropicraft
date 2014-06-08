package net.tropicraft.entity.pool;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftUtils;

public class EntityPoolFloat extends Entity {

	/** Combined rgba of the chair */
	private static final int DATAWATCHER_COLOR = 2;

	/** Current damage to the chair (from punching it) */
	private static final int DATAWATCHER_DAMAGE = 3;

	/** The time to count down from since the last time entity was hit. */
	private static final int DATAWATCHER_TIME_SINCE_HIT = 6;


	/** Is any entity sitting in the float? */
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

	public EntityPoolFloat(World par1World) {
		super(par1World);
		this.setSize(2.0F, 0.8F);
		this.ignoreFrustumCheck = true;
		this.isChairEmpty = true;
		this.speedMultiplier = 0.10D;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = .95F;
	}

	public EntityPoolFloat(World world, double x, double y, double z, int color) {
		this(world);
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		setColor(color);		
		//	rotationYaw = this.getAngleToPlayer(player);
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(DATAWATCHER_COLOR, new Integer(ColorHelper.DEFAULT_VALUE));
		this.dataWatcher.addObject(DATAWATCHER_DAMAGE, new Float(0));
		this.dataWatcher.addObject(DATAWATCHER_TIME_SINCE_HIT, new Integer(0));
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

		for (int i = 0; i < b0; ++i) {
			double d1 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i + 0) / (double)b0 - 0.125D;
			double d3 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i + 1) / (double)b0 - 0.125D;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(this.boundingBox.minX, d1, this.boundingBox.minZ, this.boundingBox.maxX, d3, this.boundingBox.maxZ);

			if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water))
			{
				d0 += 1.0D / (double)b0;
			}
		}

		double d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double d2;
		double d4;
		int j;

		if (d10 > 0.26249999999999996D) {
			d2 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
			d4 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);


			for (j = 0; (double)j < 1.0D + d10 * 60.0D; ++j) {
				double d5 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
				double d6 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
				double d8;
				double d9;

				if (this.rand.nextBoolean()) {
					d8 = this.posX - d2 * d5 * 0.8D + d4 * d6;
					d9 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
					this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
				} else {
					d8 = this.posX + d2 + d4 * d5 * 0.7D;
					d9 = this.posZ + d4 - d2 * d5 * 0.7D;
					this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
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
				d12 = MathHelper.wrapAngleTo180_double(this.chairYaw - (double)this.rotationYaw);
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

			if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
				float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
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

			for (l = 0; l < 4; ++l) {
				int i1 = MathHelper.floor_double(this.posX + ((double)(l % 2) - 0.5D) * 0.8D);
				j = MathHelper.floor_double(this.posZ + ((double)(l / 2) - 0.5D) * 0.8D);

				for (int j1 = 0; j1 < 2; ++j1) {
					int k = MathHelper.floor_double(this.posY) + j1;
					Block block = this.worldObj.getBlock(i1, k, j);

					if (block == Blocks.snow_layer) {
						this.worldObj.setBlockToAir(i1, k, j);
						this.isCollidedHorizontally = false;
					} else 
						if (block == Blocks.waterlily) {
							this.worldObj.setBlockToAir(i1, k, j);
							this.worldObj.func_147480_a(i1, k, j, true);
							this.isCollidedHorizontally = false;
						}
				}
			}

			if (this.onGround) {
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

			this.motionX *= 0.9900000095367432D;
			this.motionY *= 0.949999988079071D;
			this.motionZ *= 0.9900000095367432D;


			this.rotationPitch = 0.0F;
			d4 = (double)this.rotationYaw;
			d11 = this.prevPosX - this.posX;
			d12 = this.prevPosZ - this.posZ;

			if (d11 * d11 + d12 * d12 > 0.001D) {
				d4 = (double)((float)(Math.atan2(d12, d11) * 180.0D / Math.PI));
			}

			double d7 = MathHelper.wrapAngleTo180_double(d4 - (double)this.rotationYaw);

			if (d7 > 20.0D) {
				d7 = 20.0D;
			}

			if (d7 < -20.0D) {
				d7 = -20.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d7);
			this.setRotation(this.rotationYaw, this.rotationPitch);

			if (!this.worldObj.isRemote) {
				List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

				if (list != null && !list.isEmpty()) {
					for (int k1 = 0; k1 < list.size(); ++k1) {
						Entity entity = (Entity)list.get(k1);

						if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityChair) {
							entity.applyEntityCollision(this);
						}
					}
				}

				if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
					this.riddenByEntity = null;
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		}
		else if (!this.worldObj.isRemote && !this.isDead) {
			//	this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamage(this.getDamage() + par2 * 10.0F);
			this.setBeenAttacked();
			boolean flag = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;

			if (flag || this.getDamage() > 40.0F) {
				if (this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}

				if (!flag) {
					this.entityDropItem(new ItemStack(TCItemRegistry.chair, 1,  getDamageFromColor()), 0.0F);
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
		//	this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamage(this.getDamage() * 11.0F);
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public boolean interactFirst(EntityPlayer player) {
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != player) {
			return true;
		} else {
			if (!this.worldObj.isRemote) {
				System.out.println("mount");
				player.mountEntity(this);
			}

			return true;
		}
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
	 * posY, posZ, yaw, pitch
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		if (this.isChairEmpty)
		{
			this.chairPosRotationIncrements = par9 + 5;
		}
		else
		{
			double d3 = par1 - this.posX;
			double d4 = par3 - this.posY;
			double d5 = par5 - this.posZ;
			double d6 = d3 * d3 + d4 * d4 + d5 * d5;

			if (d6 <= 1.0D)
			{
				return;
			}

			this.chairPosRotationIncrements = 3;
		}

		this.chairX = par1;
		this.chairY = par3;
		this.chairZ = par5;
		this.chairYaw = (double)par7;
		this.chairPitch = (double)par8;
		this.motionX = this.velocityX;
		this.motionY = this.velocityY;
		this.motionZ = this.velocityZ;
	}

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
	 * Update rider position with x, y, and z offsets
	 */
	@Override
	public void updateRiderPosition() {
		if (this.riddenByEntity != null) {
			double xOffset = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			double zOffset = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + xOffset, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + zOffset);
		}
	}

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
	 * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
	 * pushable on contact, like chairs or minecarts.
	 */
	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	/**
	 * returns the bounding box for this entity
	 */
	@Override
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return true;
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
	@Override
	protected void updateFallState(double distanceFallenThisTick, boolean onGround) {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posY);
		int k = MathHelper.floor_double(this.posZ);

		if (onGround) {
			if (this.fallDistance > 180.0F) {
				this.fall(this.fallDistance);

				if (!this.worldObj.isRemote && !this.isDead) {
					this.setDead();
					int l;

					for (l = 0; l < 3; ++l) {
						this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getDamageFromColor()), 0.0F);
					}//TODO
				}

				this.fallDistance = 0.0F;
			}
		}
		else if (this.worldObj.getBlock(i, j - 1, k).getMaterial() != Material.water && distanceFallenThisTick < 0.0D)
		{
			this.fallDistance = (float)((double)this.fallDistance - distanceFallenThisTick);
		}
	}

	public void setColor(int color) {
		this.dataWatcher.updateObject(DATAWATCHER_COLOR, Integer.valueOf(color));
	}

	public void setColor(float red, float green, float blue) {
		this.dataWatcher.updateObject(DATAWATCHER_COLOR, Integer.valueOf(TropicraftUtils.getColor(red, green, blue)));
	}

	public int getColor() {
		return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_COLOR);
	}

	public void setDamage(float damage) {
		this.dataWatcher.updateObject(DATAWATCHER_DAMAGE, Float.valueOf(damage));
	}

	public float getDamage() {
		return this.dataWatcher.getWatchableObjectFloat(DATAWATCHER_DAMAGE);
	}

	/**
	 * Sets the time to count down from since the last time entity was hit.
	 */
	public void setTimeSinceHit(int time) {
		this.dataWatcher.updateObject(DATAWATCHER_TIME_SINCE_HIT, Integer.valueOf(time));
	}

	/**
	 * Gets the time since the last hit.
	 */
	public int getTimeSinceHit() {
		return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_TIME_SINCE_HIT);
	}

}
