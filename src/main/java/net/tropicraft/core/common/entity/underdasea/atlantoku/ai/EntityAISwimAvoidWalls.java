package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class EntityAISwimAvoidWalls extends EntityAISwimBase {

	public EntityTropicraftWaterBase entity;
	public Random rand;

	public EntityAISwimAvoidWalls(int mutex, EntityTropicraftWaterBase entityObjIn)
    {
        this.entity = entityObjIn;
        rand = this.entity.getRNG();
        this.setMutexBits(mutex);
    }

	@Override
	public boolean shouldExecute() {
		return entity.isInWater();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		// Wall correction
		Vec3d angle = entity.getHeading();
		double frontDist = 1f+rand.nextInt(4);
		
		Vec3d diff = new Vec3d(entity.posX + (angle.x * frontDist), entity.posY + angle.y, entity.posZ + (angle.z * frontDist));

		BlockPos bp = new BlockPos((int)diff.x, (int)entity.posY, (int)diff.z);

		if(!entity.world.getBlockState(bp).getMaterial().isLiquid() && !entity.isMovingAwayFromWall) 
		{
			entity.setRandomTargetHeadingForce(32);
			entity.isMovingAwayFromWall = true;
		}
		
		if(entity.ticksExisted % 20 == 0 && entity.isMovingAwayFromWall)
			entity.isMovingAwayFromWall = false;
		
		
		if(entity.targetVector != null && entity.isMovingAwayFromWall) {
			bp = new BlockPos((int)entity.targetVector.x, (int)entity.targetVector.y, (int)entity.targetVector.z);

			if(entity.getPosition().equals(bp) && entity.ticksExisted % 80 == 0) {
				entity.isMovingAwayFromWall = false;
			}
		}
		
		
		// Near surface check
		bp = entity.getPosition();
		if(!entity.world.getBlockState(bp).getMaterial().isLiquid()) {
			if(entity.swimPitch > 0f) {
				angle = entity.getHeading();
				frontDist = 5f;
				diff = new Vec3d(entity.posX + (angle.x * frontDist), entity.posY + angle.y, entity.posZ + (angle.z * frontDist));	
				entity.isPanicking = false;
				entity.setRandomTargetHeadingForce(32);
			//	entity.setTargetHeading(diff.xCoord, posY - 2, diff.zCoord, true);
				//entity.swimPitch -= 15f;
			}
			
		}
		

		
		
		

		bp = new BlockPos(entity.getPosition().down(2));

		// Hitting bottom check
		if(!entity.world.getBlockState(bp).getMaterial().isLiquid()) {
			if(entity.swimPitch < 0f) {
				entity.swimPitch+= 2f;
			}
		}


	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {

		return entity.isInWater();
	}
}
