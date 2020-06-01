package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class MonkeySitInChairGoal extends Goal {
    private VMonkeyEntity entity;
    private SitGoal aiSit;

    public MonkeySitInChairGoal(VMonkeyEntity monkey, SitGoal aiSit) {
        this.entity = monkey;
        this.aiSit = aiSit;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    private Optional<ChairEntity> getNearestEmptyChair() {
        List<ChairEntity> list = entity.world.getEntitiesWithinAABB(ChairEntity.class, entity.getBoundingBox().grow(32D));
        return list.stream().filter(chair -> !chair.isInvisible() && !chair.isBeingRidden()).findFirst();
    }

    private boolean isOwnerNear() {
        return entity != null && entity.getOwner() != null && entity.getOwner().getDistanceSq(entity) < 32D;
    }

    private boolean isOwnerNearAndSitting() {
        Entity ridingEntity = entity.getOwner().getRidingEntity();
        return isOwnerNear() && ridingEntity instanceof ChairEntity;
    }

    @Override
    public void resetTask() {
        entity.stopRiding();
        entity.setSitting(false);
        aiSit.setSitting(false);
        // TODO - no longer needed?
        // entity.resetRideCooldown();
    }

    @Override
    public boolean shouldExecute() {
        if (entity == null || !entity.isTamed() || entity.getOwner() == null) {
            return false;
        }
        return hasNearbyEmptyChair() && isOwnerNearAndSitting();
    }

    private boolean hasNearbyEmptyChair() {
        return getNearestEmptyChair().isPresent();
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
        final Optional<ChairEntity> nearbyChair = getNearestEmptyChair();
        if (nearbyChair.isPresent()) {
            entity.setSitting(true);
            aiSit.setSitting(true);
            entity.startRiding(nearbyChair.get());
        }
    }
}
