package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.entity.placeable.EntityChair;

import java.util.List;

public class EntityAIMonkeySitInChair extends EntityAIBase {
    private EntityVMonkey entity;

    public EntityAIMonkeySitInChair(EntityVMonkey monkey) {
        this.entity = monkey;
        this.setMutexBits(5);
    }

    private boolean isEmptyChairNear() {
        List<EntityChair> list = this.entity.world.<EntityChair>getEntitiesWithinAABB(EntityChair.class, this.entity.getEntityBoundingBox().grow(16D));
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
                this.entity.getOwner().getDistanceSq(this.entity) < 16D;
    }

    @Override
    public void resetTask() {
        this.entity.dismountRidingEntity();
        this.entity.setSitting(false);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getOwner() == null || this.entity == null) return false;
        return isOwnerNear() && isEmptyChairNear();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.isRiding();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        List<EntityChair> list = this.entity.world.<EntityChair>getEntitiesWithinAABB(EntityChair.class, this.entity.getEntityBoundingBox().grow(16D));

        if (!list.isEmpty()) {
            for (EntityChair chair : list) {
                if (!chair.isInvisible()) {
                    if(!chair.isBeingRidden()) {
                        this.entity.startRiding(chair);
                        this.entity.setSitting(true);
                    }
                }
            }
        }
    }
}
