package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.egg.EggEntity;

public abstract class EchinodermEntity extends WaterMobEntity {
    /**
     * How many ticks it takes for a baby to grow into an adult.
     */
    public static final int GROWTH_TICKS = 10 * 60 * 20; // 10 minutes

    /**
     * How many ticks to wait between breeding sessions.
     */
    public static final int BREEDING_COOLDOWN = 10 * 60 * 20; // 10 minutes

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
    public static final int MATE_SCAN_INTERVAL = 5 * 20; // 5 seconds

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
    private static final DataParameter<Integer> GROWING_AGE = EntityDataManager.createKey(EchinodermEntity.class, DataSerializers.VARINT);

    /**
     * Custom yOffset variable
     */
    private double yOffset = -1;

    public EchinodermEntity(final EntityType<? extends WaterMobEntity> type, final World world) {
        super(type, world);
        setEchinodermSize();
    }

    public EchinodermEntity(final EntityType<? extends WaterMobEntity> type, final World world, final boolean isBaby) {
        super(type, world);
        setGrowingAge(isBaby ? -GROWTH_TICKS : 0);
        setEchinodermSize();
    }

    public abstract EggEntity createEgg();

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(GROWING_AGE, 0);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.read(compound);
        setGrowingAge(compound.getInt("Age"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Age", getGrowingAge());
    }

    @Override
    public void knockBack(Entity entity, float par2, double par3, double par5) {
        // don't move when hit
    }

    @Override
    public boolean isChild() {
        return getGrowingAge() < 0;
    }

    public boolean isHorny() {
        return getGrowingAge() == 0;
    }

    private EchinodermEntity findMate() {
        int neighbors = 0;
        EchinodermEntity closestMate = null;
        double closestSqDist = -1f;

        AxisAlignedBB aabb = getBoundingBox().grow(NEIGHBORHOOD_SIZE, NEIGHBORHOOD_SIZE, NEIGHBORHOOD_SIZE);
        for (Object obj : world.getEntitiesWithinAABB(getClass(), aabb)) {
            // don't masturbate
            if (obj == this) {
                continue;
            }

            neighbors++;

            EchinodermEntity other = (EchinodermEntity) obj;

            if (!isPotentialMate(other)) {
                continue;
            }

            double sqDist = getDistanceSq(other);

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

    public boolean isPotentialMate(final EchinodermEntity other) {
        // we are no pedophiles or rapists
        return !other.isChild() && other.isHorny();
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
        this.dataManager.set(GROWING_AGE, age);
    }

    /**
     * Calculates the growth progress of this sea urchin.
     * @return number between 0 and 1: 0 = freshly hatched, 1 = adult
     */
    public float getGrowthProgress() {
        int growingAge = getGrowingAge();
        return growingAge < 0 ? 1f + ((float) growingAge) / GROWTH_TICKS : 1f;
    }

    private void setEchinodermSize() {
        float growthProgress = getGrowthProgress();
        float width = getBabyWidth() + growthProgress*(getAdultWidth() - getBabyWidth());
        float height = getBabyHeight() + growthProgress*(getAdultHeight() - getBabyHeight());
        float yO = getBabyYOffset() + growthProgress*(getAdultYOffset() - getBabyYOffset());

        recalculateSize();
    //TODO    setSize(width, height);
        yOffset = yO;
    }

    //todo
//    public EntitySize getSize(Pose poseIn) {
//        return getSize(poseIn).scale()
//    }

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
