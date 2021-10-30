package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class MonkeySitInChairGoal extends Goal {
    private VMonkeyEntity entity;

    public MonkeySitInChairGoal(VMonkeyEntity monkey) {
        this.entity = monkey;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    private Optional<ChairEntity> getNearestEmptyChair() {
        List<ChairEntity> list = entity.level.getEntitiesOfClass(ChairEntity.class, entity.getBoundingBox().inflate(32D));
        return list.stream().filter(chair -> !chair.isInvisible() && !chair.isVehicle()).findFirst();
    }

    private boolean isOwnerNear() {
        return entity != null && entity.getOwner() != null && entity.getOwner().distanceToSqr(entity) < 32D;
    }

    private boolean isOwnerNearAndSitting() {
        Entity ridingEntity = entity.getOwner().getVehicle();
        return isOwnerNear() && ridingEntity instanceof ChairEntity;
    }

    @Override
    public void stop() {
        entity.stopRiding();
        entity.setOrderedToSit(false);
        // TODO - no longer needed?
        // entity.resetRideCooldown();
    }

    @Override
    public boolean canUse() {
        if (entity == null || !entity.isTame() || entity.getOwner() == null) {
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
    public boolean canContinueToUse() {
        return isOwnerNearAndSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        final Optional<ChairEntity> nearbyChair = getNearestEmptyChair();
        if (nearbyChair.isPresent()) {
            entity.setOrderedToSit(true);
            entity.startRiding(nearbyChair.get());
        }
    }
}
