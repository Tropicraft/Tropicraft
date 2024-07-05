package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.egg.EggEntity;

public abstract class EchinodermEntity extends WaterAnimal {
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
    private static final EntityDataAccessor<Integer> GROWING_AGE = SynchedEntityData.defineId(EchinodermEntity.class, EntityDataSerializers.INT);

    /**
     * Custom yOffset variable
     */
    private double yOffset = -1;

    public EchinodermEntity(final EntityType<? extends WaterAnimal> type, final Level world) {
        super(type, world);
        setEchinodermSize();
    }

    public void setBaby() {
        setGrowingAge(-GROWTH_TICKS);
        setEchinodermSize();
    }

    public abstract EggEntity createEgg();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GROWING_AGE, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setGrowingAge(compound.getInt("Age"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", getGrowingAge());
    }

    @Override
    public void knockback(double strength, double ratioX, double ratioZ) {

    }

    @Override
    public boolean isBaby() {
        return getGrowingAge() < 0;
    }

    public boolean isHorny() {
        return getGrowingAge() == 0;
    }

    private EchinodermEntity findMate() {
        int neighbors = 0;
        EchinodermEntity closestMate = null;
        double closestSqDist = -1f;

        AABB aabb = getBoundingBox().inflate(NEIGHBORHOOD_SIZE, NEIGHBORHOOD_SIZE, NEIGHBORHOOD_SIZE);
        for (Object obj : level().getEntitiesOfClass(getClass(), aabb)) {
            // don't masturbate
            if (obj == this) {
                continue;
            }

            neighbors++;

            EchinodermEntity other = (EchinodermEntity) obj;

            if (!isPotentialMate(other)) {
                continue;
            }

            double sqDist = distanceToSqr(other);

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
        return !other.isBaby() && other.isHorny();
    }

    /**
     * Negative, to be incremented if a child. Positive, to be decremented, if
     * an adult that has just procreated, as a cooldown.
     *
     * @return the number of ticks.
     */
    public int getGrowingAge() {
        return this.entityData.get(GROWING_AGE);
    }

    public void setGrowingAge(int age) {
        this.entityData.set(GROWING_AGE, age);
    }

    /**
     * Calculates the growth progress of this sea urchin.
     *
     * @return number between 0 and 1: 0 = freshly hatched, 1 = adult
     */
    public float getGrowthProgress() {
        int growingAge = getGrowingAge();
        return growingAge < 0 ? 1f + ((float) growingAge) / GROWTH_TICKS : 1f;
    }

    private void setEchinodermSize() {
        float growthProgress = getGrowthProgress();
        float width = getBabyWidth() + growthProgress * (getAdultWidth() - getBabyWidth());
        float height = getBabyHeight() + growthProgress * (getAdultHeight() - getBabyHeight());
        float yO = getBabyYOffset() + growthProgress * (getAdultYOffset() - getBabyYOffset());

        refreshDimensions();
        //TODO    setSize(width, height);
        yOffset = yO;
    }

    //todo
//    public EntitySize getSize(Pose poseIn) {
//        return getSize(poseIn).scale()
//    }

    @Override
    public Vec3 getVehicleAttachmentPoint(final Entity entity) {
        if (yOffset < 0) {
            return super.getVehicleAttachmentPoint(entity);
        } else {
            return new Vec3(0.0, yOffset, 0.0);
        }
    }

    public abstract float getBabyWidth();

    public abstract float getAdultWidth();

    public abstract float getBabyHeight();

    public abstract float getAdultHeight();

    public abstract float getBabyYOffset();

    public abstract float getAdultYOffset();
}
