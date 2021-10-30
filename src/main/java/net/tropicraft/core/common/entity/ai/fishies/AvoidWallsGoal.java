package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class AvoidWallsGoal extends Goal {
    public TropicraftFishEntity entity;
    public Random rand;

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
        Vector3d angle = entity.getHeading();
        double frontDist = 1 + rand.nextInt(4);
        
        Vector3d diff = new Vector3d(entity.getX() + (angle.x * frontDist), entity.getY() + angle.y, entity.getZ() + (angle.z * frontDist));

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
