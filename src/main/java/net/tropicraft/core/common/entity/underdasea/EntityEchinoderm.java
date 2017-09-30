package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class EntityEchinoderm extends EntityWaterMob {

	/**
	 * How many ticks it takes for a baby to grow into an adult.
	 */
	public static final int GROWTH_TICKS = 10*60*20; // 10 minutes
	
	/**
	 * How many ticks to wait between breeding sessions.
	 */
	public static final int BREEDING_COOLDOWN = 10*60*20; // 10 minutes
	
	/**
	 * How close another sea urchin has to be for it to be considered a
	 * potential mate.
	 */
	public static final int BREEDING_PROXIMITY = 4;
	
	/**
	 * Number of neighboring sea urchins above which breeding doesn't happen.
	 */
	public static final int MAX_NEIGHBORS = 6;
	
	/**
	 * Number of blocks around this sea urchin within which to look for
	 * neighbors.
	 */
	public static final int NEIGHBORHOOD_SIZE = 8;
	
	/**
	 * Number of ticks to wait between trying to scan for mates.
	 */
	public static final int MATE_SCAN_INTERVAL = 5*20; // 5 seconds
	
	/**
	 * Growing age from previous tick (client side). Used for updating bounding
	 * box and yOffset on change.
	 */
	private int prevGrowingAge;
	
	/**
	 * Number of ticks until next mate finding attempt.
	 */
	private int mateScanCooldown;
	
	/**
	 * Growing Age. Replaced old data watcher DW_GROWING_AGE
	 */
	private static final DataParameter<Integer> GROWING_AGE = EntityDataManager.<Integer>createKey(EntityEchinoderm.class, DataSerializers.VARINT);
	
	/**
	 * Custom yOffset variable
	 */
	private double yOffset = -1;

	public EntityEchinoderm(World world) {
		super(world);
		setEchinodermSize();
	}
	
	public EntityEchinoderm(World world, boolean baby) {
		super(world);
		setGrowingAge(baby ? -GROWTH_TICKS : 0);
		setEchinodermSize();
	}
	
	@Override
    protected void initEntityAI() {

    }

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		isJumping = false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amt) {
		if (source == DamageSource.inWall) {
			return false;
		}

		return super.attackEntityFrom(source, amt);
	}
	
//	@Override
//	public boolean isMovementCeased() {
//		return true;
//	}
	
	@Override
	public void knockBack(Entity ent, float par2, double par3, double par5) {
		// don't move when hit
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		int growingAge = getGrowingAge();

		if (world.isRemote) {
			motionY = 0D;

			if (growingAge != prevGrowingAge) {
				setEchinodermSize();
				prevGrowingAge = growingAge;
			}
		} else {
			this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);

			if (growingAge < 0) {
				setGrowingAge(growingAge+1);
				setEchinodermSize();
			} else if (growingAge > 0) {
				// update inter-breeding cooldown
				setGrowingAge(growingAge-1);
			}

			if (isHorny()) {
				if (mateScanCooldown > 0) {
					mateScanCooldown--;
				} else {
					mateScanCooldown = MATE_SCAN_INTERVAL;
					EntityEchinoderm mate = findMate();

					if (mate != null) {
						setGrowingAge(BREEDING_COOLDOWN);
						mate.setGrowingAge(BREEDING_COOLDOWN);

						EntityEchinodermEgg egg = createEgg();
						double newX = posX+0.5*(mate.posX-posX);
						double newY = posY+1;
						double newZ = posZ+0.5*(mate.posZ-posZ);
						egg.setLocationAndAngles(newX, newY, newZ, 0f, 0f);
						world.spawnEntity(egg);
					}
				}
			}
		}
    }
	
	public abstract EntityEchinodermEgg createEgg();
	
	public boolean isChild() {
		return getGrowingAge() < 0;
	}
	
	public boolean isHorny() {
		return getGrowingAge() == 0;
	}

	private EntityEchinoderm findMate() {
		int neighbors = 0;
		EntityEchinoderm closestMate = null;
		double closestSqDist = -1f;

		AxisAlignedBB aabb = this.getEntityBoundingBox().expand(NEIGHBORHOOD_SIZE, NEIGHBORHOOD_SIZE, NEIGHBORHOOD_SIZE);
		for (Object obj : world.getEntitiesWithinAABB(getClass(), aabb)) {
			// don't masturbate
			if (obj == this) {
				continue;
			}

			neighbors++;

			EntityEchinoderm other = (EntityEchinoderm) obj;
			
			if (!isPotentialMate(other)) {
				continue;
			}
			
			double sqDist = getDistanceSqToEntity(other);
			
			if (sqDist < BREEDING_PROXIMITY && (closestSqDist == -1f || sqDist < closestSqDist)) {
				closestMate = other;
				closestSqDist = sqDist;
			}
		}
		
		if (neighbors > MAX_NEIGHBORS) {
			return null;
		} else {
			return closestMate;
		}
	}
	
	public boolean isPotentialMate(EntityEchinoderm other) {
		// we are no pedophiles or rapists
		return !other.isChild() && other.isHorny();
	}

	@Override
    public void entityInit() {
        super.entityInit();
        this.getDataManager().register(GROWING_AGE, Integer.valueOf(0));
    }
	
	/**
	 * Negative, to be incremented if a child. Positive, to be decremented, if
	 * an adult that has just procreated, as a cooldown.
	 * @return the number of ticks.
	 */
	public int getGrowingAge() {
		return this.dataManager.get(GROWING_AGE);
	}
	
	public void setGrowingAge(int age) {
		this.dataManager.set(GROWING_AGE, Integer.valueOf(age));
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Age", this.getGrowingAge());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setGrowingAge(compound.getInteger("Age"));
    }

	/**
	 * Calculates the growth progress of this sea urchin.
	 * @return number between 0 and 1: 0 = freshly hatched, 1 = adult
	 */
	public float getGrowthProgress() {
		int growingAge = getGrowingAge();
		float growthProgress = growingAge < 0 ? 1f+((float)growingAge)/GROWTH_TICKS : 1f;
		return growthProgress;
	}
	
	private void setEchinodermSize() {
		float growthProgress = getGrowthProgress();
		float width = getBabyWidth() + growthProgress*(getAdultWidth()-getBabyWidth());
		float height = getBabyHeight() + growthProgress*(getAdultHeight()-getBabyHeight());
		float yO = getBabyYOffset() + growthProgress*(getAdultYOffset()-getBabyYOffset());
		
		setSize(width, height);
		yOffset = yO;
	}
	
	@Override
	public double getYOffset() {
		if (yOffset < 0) {
			return super.getYOffset();
		} else {
			return yOffset;
		}
	}
	
	public abstract float getBabyWidth();
	public abstract float getAdultWidth();
	public abstract float getBabyHeight();
	public abstract float getAdultHeight();
	public abstract float getBabyYOffset();
	public abstract float getAdultYOffset();
}