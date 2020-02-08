package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;
import java.util.Random;

public class AvoidWallsGoal extends Goal {
	public TropicraftFishEntity entity;
	public Random rand;

	public AvoidWallsGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn) {
        entity = entityObjIn;
        rand = entity.getRNG();
        setMutexFlags(flags);
    }

	@Override
	public boolean shouldExecute() {
		return entity.isInWater();
	}

	@Override
	public void tick() {
		super.tick();
		// Wall correction
		Vec3d angle = entity.getHeading();
		double frontDist = 1 + rand.nextInt(4);
		
		Vec3d diff = new Vec3d(entity.posX + (angle.x * frontDist), entity.posY + angle.y, entity.posZ + (angle.z * frontDist));

		BlockPos bp = new BlockPos((int)diff.x, (int)entity.posY, (int)diff.z);

		if (!entity.world.getBlockState(bp).getMaterial().isLiquid() && !entity.isMovingAwayFromWall) {
			entity.setRandomTargetHeadingForce(32);
			entity.isMovingAwayFromWall = true;
		}
		
		if (entity.ticksExisted % 20 == 0 && entity.isMovingAwayFromWall)
			entity.isMovingAwayFromWall = false;
		
		
		if (entity.targetVector != null && entity.isMovingAwayFromWall) {
			bp = new BlockPos((int)entity.targetVector.x, (int)entity.targetVector.y, (int)entity.targetVector.z);

			if(entity.getPosition().equals(bp) && entity.ticksExisted % 80 == 0) {
				entity.isMovingAwayFromWall = false;
			}
		}
		
		
		// Near surface check
		bp = entity.getPosition();
		if (!entity.world.getBlockState(bp).getMaterial().isLiquid()) {
			if (entity.swimPitch > 0f) {
				entity.isPanicking = false;
				entity.setRandomTargetHeadingForce(32);
			}
		}

		bp = new BlockPos(entity.getPosition().down(2));

		// Hitting bottom check
		if (!entity.world.getBlockState(bp).getMaterial().isLiquid()) {
			if (entity.swimPitch < 0f) {
				entity.swimPitch+= 2f;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
    public boolean shouldContinueExecuting() {

		return entity.isInWater();
	}
}
