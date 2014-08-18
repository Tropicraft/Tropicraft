package net.tropicraft.entity.placeable;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.ColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityUmbrella extends Entity {

	/** Combined rgba of the umbrella */
	private static final int DATAWATCHER_COLOR = 2;

	/** Current damage to the umbrella (from punching it) */
	private static final int DATAWATCHER_DAMAGE = 3;

	/** The time to count down from since the last time entity was hit. */
	private static final int DATAWATCHER_TIME_SINCE_HIT = 4;

	/** The direction the umbrella is currently rocking in */
	private static final int DATAWATCHER_ROCK_DIRECTION = 5;

	private static final int DAMAGE_THRESHOLD = 40;

	private double umbrellaX;
	private double umbrellaY;
	private double umbrellaZ;
	private double umbrellaYaw;
	private double umbrellaPitch;
	
	private int spawnX;
	private int spawnY;
	private int spawnZ;
	
	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;
	
	private int idk;

	public EntityUmbrella(World world) {
		super(world);
		this.ignoreFrustumCheck = true;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = .95F;
		this.setSize(1F, 4F);
	}

	public EntityUmbrella(World world, double x, double y, double z, int color) {
		this(world);
		spawnX = MathHelper.floor_double(x);
		spawnY = MathHelper.floor_double(y - 1.01);
		spawnZ = MathHelper.floor_double(z);
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		setColor(color);
		idk = 4;
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(DATAWATCHER_COLOR, new Integer(ColorHelper.DEFAULT_VALUE));
		this.dataWatcher.addObject(DATAWATCHER_DAMAGE, new Float(0));
		this.dataWatcher.addObject(DATAWATCHER_TIME_SINCE_HIT, new Integer(0));
		this.dataWatcher.addObject(DATAWATCHER_ROCK_DIRECTION, new Integer(0));
	}
	
	@SideOnly(Side.CLIENT)
    public void setVelocity(double d, double d1, double d2)
    {
        velocityX = motionX = d;
        velocityY = motionY = d1;
        velocityZ = motionZ = d2;
    }

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if(this.getDamage() > 0) {
			this.setDamage(this.getDamage() - 1);
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		int i = 5;
		double d = 0.0D;
		
		for(int j = 0; j < i; j++) {
			double d5 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 0)) / (double)i) - 0.125D;
			double d9 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 1)) / (double)i) - 0.125D;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(boundingBox.minX, d5, boundingBox.minZ, boundingBox.maxX, d9, boundingBox.maxZ);
			if(worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
				d += 1.0D / (double)i;
			}
		}
		
		if(worldObj.isRemote) {
            if(idk > 0) {
                double d1 = posX + (umbrellaX - posX) / (double)idk;
                double d6 = posY + (umbrellaY - posY) / (double)idk;
                double d10 = posZ + (umbrellaZ - posZ) / (double)idk;
                double d14;
                for(d14 = umbrellaYaw - (double)rotationYaw; d14 < -180D; d14 += 360D) { }
                for(; d14 >= 180D; d14 -= 360D) { }
                rotationYaw += d14 / (double)idk;
                rotationPitch += (umbrellaPitch - (double)rotationPitch) / (double)idk;
                idk--;
                setPosition(d1, d6, d10);
                setRotation(rotationYaw, rotationPitch);
            }
        } else {
        	motionX = motionY = motionZ = 0;
        }
		
		if(d < 1.0D)
        {
            double d3 = d * 2D - 1.0D;
            motionY += 0.039999999105930328D * d3;
        } else
        {
            if(motionY < 0.0D)
            {
                motionY /= 2D;
            }
            motionY += 0.0070000002160668373D;
        }
		
		if (this.onGround) {
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
		}
		
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		
		rotationPitch = 0.0F;
        double d13 = rotationYaw;
        double d16 = prevPosX - posX;
        double d17 = prevPosZ - posZ;
        if(d16 * d16 + d17 * d17 > 0.001D)
        {
            d13 = (float)((Math.atan2(d17, d16) * 180D) / 3.1415926535897931D);
        }
        double d19;
        for(d19 = d13 - (double)rotationYaw; d19 >= 180D; d19 -= 360D) { }
        for(; d19 < -180D; d19 += 360D) { }
        if(d19 > 20D)
        {
            d19 = 20D;
        }
        if(d19 < -20D)
        {
            d19 = -20D;
        }
        rotationYaw += d19;
        setRotation(rotationYaw, rotationPitch);
        List<?> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.2D, 0.20000000298023224D));
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);

                if(entity != riddenByEntity && entity.canBePushed() && (entity instanceof EntityUmbrella))
                {
                    entity.applyEntityCollision(this);
                }
            }

        }
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if (this.isEntityInvulnerable()) {
			return false;
		}
		else if (!this.worldObj.isRemote && !this.isDead) {
			this.setRockDirection(-this.getRockDirection());
			this.setTimeSinceHit(10);
			this.setDamage(this.getDamage() + i * 10.0F);
			this.setBeenAttacked();
			boolean flag = damagesource.getEntity() instanceof EntityPlayer && ((EntityPlayer)damagesource.getEntity()).capabilities.isCreativeMode;

			if (flag || this.getDamage() > DAMAGE_THRESHOLD) {
				if (this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}

				if (!flag) {
					this.entityDropItem(new ItemStack(TCItemRegistry.umbrella, 1,  getDamageFromColor()), 0.0F);
				}

				this.setDead();
				
				int y = spawnY + 4;
				
				for (int x = spawnX - 3; x <= spawnX + 2; x++) {
					for (int z = spawnZ - 3; z <= spawnZ + 2; z++) {
						worldObj.setBlockToAir(x, y, z);
					}
				}
			}

			return true;
		} else {
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i) {
		umbrellaX = d;
		umbrellaY = d1;
		umbrellaZ = d2;
		umbrellaYaw = f;
		umbrellaPitch = f1;
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		idk = i + 1;
	}

	/**
	 * @return Returns the damage value associated with the color of this umbrella
	 */
	 public int getDamageFromColor() {
		return ColorHelper.getDamageFromColor(this.getColor());
	 }

	 @Override
	 public AxisAlignedBB getCollisionBox(Entity entity) {
		 return entity.boundingBox;
	 }

	 @Override
	 public AxisAlignedBB getBoundingBox() {
		 return boundingBox;
	 }

	 @Override
	 public double getMountedYOffset() {
		 return 0.0D;
	 }

	 @Override
	 public void performHurtAnimation() {
		 this.setRockDirection(-1 * this.getRockDirection());
		 this.setTimeSinceHit(10);
		 this.setDamage(this.getDamage() * 10.0F);
	 }

	 /**
	  * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	  * prevent them from trampling crops
	  */
	 @Override
	 protected boolean canTriggerWalking() {
		 return false;
	 }

	 @Override
	 public boolean canBeCollidedWith() {
		 return true;
	 }

	 @Override
	 public boolean canBePushed() {
		 return false;
	 }

	 @Override
	 @SideOnly(Side.CLIENT)
	 public float getShadowSize() {
		 return 2.0F;
	 }

	 @Override
	 protected void readEntityFromNBT(NBTTagCompound nbt) {
		 this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
		 this.spawnX = nbt.getInteger("spawnX");
		 this.spawnY = nbt.getInteger("spawnY");
		 this.spawnZ = nbt.getInteger("spawnZ");
	 }

	 @Override
	 protected void writeEntityToNBT(NBTTagCompound nbt) {
		 nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
		 nbt.setInteger("spawnX", Integer.valueOf(this.spawnX));
		 nbt.setInteger("spawnY", Integer.valueOf(this.spawnY));
		 nbt.setInteger("spawnZ", Integer.valueOf(this.spawnZ));
	 }

	 public void setColor(int color) {
		 this.dataWatcher.updateObject(DATAWATCHER_COLOR, Integer.valueOf(color));
	 }

	 public void setColor(float red, float green, float blue) {
		 this.dataWatcher.updateObject(DATAWATCHER_COLOR, Integer.valueOf(ColorHelper.getColor(red, green, blue)));
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

	 public void setRockDirection(int direction) {
		 this.dataWatcher.updateObject(DATAWATCHER_ROCK_DIRECTION, Integer.valueOf(direction));
	 }

	 public int getRockDirection() {
		 return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_ROCK_DIRECTION);
	 }
}
