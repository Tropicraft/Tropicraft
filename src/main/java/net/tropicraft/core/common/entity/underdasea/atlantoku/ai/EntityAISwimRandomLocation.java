package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.Random;

import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class EntityAISwimRandomLocation extends EntityAISwimBase {

	public EntityTropicraftWaterBase entity;
	public Random rand;

	public EntityAISwimRandomLocation(int mutex, EntityTropicraftWaterBase entityObjIn) {
        this.entity = entityObjIn;
        rand = this.entity.getRNG();
        this.setMutexBits(mutex);
    }

	@Override
	public boolean shouldExecute() {
		boolean result = entity.isInWater() && entity.ticksExisted % 10+rand.nextInt(20) == 0;
		return result;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		
		entity.setRandomTargetHeading();
		if(entity.eatenFishAmount > 0 && rand.nextInt(10) == 0) {
			entity.eatenFishAmount--;
		}
	}

	@Override
    public boolean shouldContinueExecuting() {

		return entity.targetVector == null;
	}
}
