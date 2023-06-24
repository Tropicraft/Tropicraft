package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;

public class AvoidWallsGoal extends Goal {
    public TropicraftFishEntity entity;
    public RandomSource rand;

    public AvoidWallsGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn) {
        entity = entityObjIn;
        rand = entity.getRandom();
        setFlags(flags);
    }

    @Override
    public boolean canUse() {
        return entity.isInWater();
    }

    @Override
    public void tick() {
        super.tick();
        // Wall correction
        Vec3 angle = entity.getHeading();
        double frontDist = 1 + rand.nextInt(4);
        
        Vec3 diff = new Vec3(entity.getX() + (angle.x * frontDist), entity.getY() + angle.y, entity.getZ() + (angle.z * frontDist));

        BlockPos bp = new BlockPos((int) diff.x, (int) entity.getY(), (int) diff.z);

        if (!entity.level.getBlockState(bp).getMaterial().isLiquid() && !entity.isMovingAwayFromWall) {
            entity.setRandomTargetHeadingForce(32);
            entity.isMovingAwayFromWall = true;
        }
        
        if (entity.tickCount % 20 == 0 && entity.isMovingAwayFromWall)
            entity.isMovingAwayFromWall = false;
        
        
        if (entity.targetVector != null && entity.isMovingAwayFromWall) {
            bp = new BlockPos((int) entity.targetVector.x, (int) entity.targetVector.y, (int) entity.targetVector.z);

            if(entity.blockPosition().equals(bp) && entity.tickCount % 80 == 0) {
                entity.isMovingAwayFromWall = false;
            }
        }
        
        
        // Near surface check
        bp = entity.blockPosition();
        if (!entity.level.getBlockState(bp).getMaterial().isLiquid()) {
            if (entity.swimPitch > 0f) {
                entity.isPanicking = false;
                entity.setRandomTargetHeadingForce(32);
            }
        }

        bp = new BlockPos(entity.blockPosition().below(2));

        // Hitting bottom check
        if (!entity.level.getBlockState(bp).getMaterial().isLiquid()) {
            if (entity.swimPitch < 0f) {
                entity.swimPitch+= 2f;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return entity.isInWater();
    }
}
