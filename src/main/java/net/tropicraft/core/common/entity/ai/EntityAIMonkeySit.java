package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.passive.TameableEntity;

public class EntityAIMonkeySit extends SitGoal {

    private final TameableEntity tameable;
    /** If the EntityTameable is sitting. */
    private boolean isSitting;

    public EntityAIMonkeySit(TameableEntity entity) {
        super(entity);
        this.tameable = entity;
    }

    public boolean getSitting() {
        return this.isSitting;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.tameable.isTamed()) {
            return false;
        } else if (this.tameable.isInWater()) {
            return false;
        } else if (!this.tameable.onGround) {
            return false;
        } else {
            LivingEntity entitylivingbase = this.tameable.getOwner();

            if (entitylivingbase == null) {
                return true;
            } else {
                return this.tameable.getDistanceSq(entitylivingbase) < 144.0D && entitylivingbase.getRevengeTarget() != null ? false : this.isSitting;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.tameable.getNavigator().clearPath();
        this.tameable.setSitting(true);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.tameable.setSitting(false);
    }

    /**
     * Sets the sitting flag.
     */
    public void setSitting(boolean sitting) {
        this.isSitting = sitting;
    }
}
