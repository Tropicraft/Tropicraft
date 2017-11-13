package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class EntityAISwimRandomLocation extends EntityAIBase {

	public EntityTropicraftWaterBase entity;
	public Random rand;

	public EntityAISwimRandomLocation(int mutex, EntityTropicraftWaterBase entityObjIn)
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
		if(entity.ticksExisted % 10+rand.nextInt(20) == 0) {
			entity.setRandomTargetHeading();
			
			if(entity.eatenFishAmount > 0 && rand.nextInt(10) == 0) {
				entity.eatenFishAmount--;
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
