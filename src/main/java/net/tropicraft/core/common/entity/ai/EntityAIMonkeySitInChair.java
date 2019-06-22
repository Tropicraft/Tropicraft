package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.entity.placeable.EntityChair;

import java.util.List;

public class EntityAIMonkeySitInChair extends Goal {
    private EntityVMonkey entity;
    private SitGoal aiSit;

    public EntityAIMonkeySitInChair(EntityVMonkey monkey, SitGoal aiSit) {
        this.entity = monkey;
        this.aiSit = aiSit;
        this.setMutexBits(5);
    }

    private boolean isEmptyChairNear() {
        List<EntityChair> list = this.entity.world.<EntityChair>getEntitiesWithinAABB(EntityChair.class, this.entity.getBoundingBox().grow(32D));
        boolean emptyChairFound = false;

        if (!list.isEmpty()) {
            for (EntityChair chair : list) {
                if (!chair.isInvisible()) {
                    if(!chair.isBeingRidden()) {
                        emptyChairFound = true;
                    }
                }
            }
        }

        return emptyChairFound;
    }

    private boolean isOwnerNear() {
        return this.entity.getOwner() != null &&
                this.entity != null &&
                this.entity.getOwner().getDistanceSq(this.entity) < 32D;
    }

    private boolean isOwnerNearAndSitting() {
        return isOwnerNear() && this.entity.getOwner().getRidingEntity() != null
                && this.entity.getOwner().getRidingEntity() instanceof EntityChair;
    }

    @Override
    public void resetTask() {
        this.entity.stopRiding();
        this.entity.setSitting(false);
        this.aiSit.setSitting(false);
        this.entity.resetRideCooldown();
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.isTamed() || this.entity == null) return false;
        return /*isEmptyChairNear() && */isOwnerNearAndSitting();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return isOwnerNearAndSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        List<EntityChair> list = this.entity.world.<EntityChair>getEntitiesWithinAABB(EntityChair.class, this.entity.getBoundingBox().grow(32D));

        if (!list.isEmpty()) {
            for (EntityChair chair : list) {
                if (!chair.isInvisible()) {
                    if(!chair.isBeingRidden()) {
                        this.entity.setSitting(true);
                        this.aiSit.setSitting(true);
                        this.entity.startRiding(chair);
                        return;
                    }
                }
            }
        }
    }
}


