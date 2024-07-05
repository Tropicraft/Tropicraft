package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class EntityAIAvoidEntityOnLowHealth<T extends Entity> extends Goal {
    private final Predicate<Entity> canBeSeenSelector;
    /**
     * The entity we are attached to
     */
    protected PathfinderMob theEntity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    /**
     * The PathEntity of our entity
     */
    private Path entityPathEntity;
    /**
     * The PathNavigate of our entity
     */
    private final PathNavigation entityPathNavigate;
    /**
     * Class of entity this behavior seeks to avoid
     */
    private final Class<T> classToAvoid;
    private final Predicate<Entity> avoidTargetSelector;
    private float healthToAvoid = 0.0f;

    public EntityAIAvoidEntityOnLowHealth(PathfinderMob theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, float healthToAvoid) {
        this(theEntityIn, classToAvoidIn, (entity) -> true, avoidDistanceIn, farSpeedIn, nearSpeedIn, healthToAvoid);
    }

    public EntityAIAvoidEntityOnLowHealth(PathfinderMob theEntityIn, Class<T> classToAvoidIn, Predicate<Entity> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, float healthToAvoid) {
        canBeSeenSelector = entity -> entity.isAlive() && theEntity.getSensing().hasLineOfSight(entity);
        theEntity = theEntityIn;
        classToAvoid = classToAvoidIn;
        avoidTargetSelector = avoidTargetSelectorIn;
        avoidDistance = avoidDistanceIn;
        farSpeed = farSpeedIn;
        nearSpeed = nearSpeedIn;
        entityPathNavigate = theEntityIn.getNavigation();
        this.healthToAvoid = healthToAvoid;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {

        if (theEntity.getHealth() > healthToAvoid) return false;

        List<T> list = theEntity.level().getEntitiesOfClass(classToAvoid,
                theEntity.getBoundingBox().expandTowards((double) avoidDistance, 3.0, (double) avoidDistance),
                EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(canBeSeenSelector).and(avoidTargetSelector)
        );

        if (list.isEmpty()) {
            return false;
        } else {
            closestLivingEntity = list.getFirst();
            Vec3 Vector3d = DefaultRandomPos.getPosAway(theEntity, 16, 7, new Vec3(closestLivingEntity.getX(), closestLivingEntity.getY(), closestLivingEntity.getZ()));

            if (Vector3d == null) {
                return false;
            } else if (closestLivingEntity.distanceToSqr(Vector3d.x, Vector3d.y, Vector3d.z) < closestLivingEntity.distanceToSqr(theEntity)) {
                return false;
            } else {
                entityPathEntity = entityPathNavigate.createPath(Vector3d.x, Vector3d.y, Vector3d.z, 0);
                return entityPathEntity != null;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return entityPathNavigate.isInProgress();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        entityPathNavigate.moveTo(entityPathEntity, farSpeed);
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick() {
        if (theEntity.distanceToSqr(closestLivingEntity) < 49.0) {
            theEntity.getNavigation().setSpeedModifier(nearSpeed);
        } else {
            theEntity.getNavigation().setSpeedModifier(farSpeed);
        }
    }
}


