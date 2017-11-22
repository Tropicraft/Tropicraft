package net.tropicraft.core.common.entity.ai;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidEntityOnLowHealth<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    /** The entity we are attached to */
    protected EntityCreature theEntity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    /** The PathEntity of our entity */
    private Path entityPathEntity;
    /** The PathNavigate of our entity */
    private final PathNavigate entityPathNavigate;
    /** Class of entity this behavior seeks to avoid */
    private final Class<T> classToAvoid;
    private final Predicate <? super T > avoidTargetSelector;
    private float healthToAvoid = 0F;

    public EntityAIAvoidEntityOnLowHealth(EntityCreature theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, float healthToAvoid)
    {
        this(theEntityIn, classToAvoidIn, Predicates.<T>alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn, healthToAvoid);
    }

    public EntityAIAvoidEntityOnLowHealth(EntityCreature theEntityIn, Class<T> classToAvoidIn, Predicate <? super T > avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, float healthToAvoid)
    {
        this.canBeSeenSelector = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_.isEntityAlive() && EntityAIAvoidEntityOnLowHealth.this.theEntity.getEntitySenses().canSee(p_apply_1_);
            }
        };
        this.theEntity = theEntityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.setMutexBits(1);
        this.healthToAvoid = healthToAvoid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {

        if (this.theEntity.getHealth() > healthToAvoid) return false;

        List<T> list = this.theEntity.world.<T>getEntitiesWithinAABB(this.classToAvoid,
                this.theEntity.getEntityBoundingBox().expand((double)this.avoidDistance, 3.0D, (double)this.avoidDistance),
                Predicates.and(new Predicate[] {EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector}));

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null)
            {
                return false;
            }
            else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.theEntity))
            {
                return false;
            }
            else
            {
                this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.entityPathEntity != null;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.entityPathNavigate.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.theEntity.getDistanceSq(this.closestLivingEntity) < 49.0D)
        {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        }
        else
        {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}