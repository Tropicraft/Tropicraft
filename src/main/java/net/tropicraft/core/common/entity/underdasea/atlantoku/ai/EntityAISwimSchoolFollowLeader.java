package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.Random;

import net.tropicraft.core.common.entity.underdasea.atlantoku.EntitySchoolableFish;

public class EntityAISwimSchoolFollowLeader extends EntityAISwimBase {

	public EntitySchoolableFish entity;
	public Random rand;
	public EntityAISwimSchoolFollowLeader(int mutex, EntitySchoolableFish entityObjIn)
    {
        this.entity = entityObjIn;
        rand = this.entity.getRNG();
        this.setMutexBits(mutex);
    }

	@Override
	public boolean shouldExecute() {
		return entity.isInWater() && !entity.getIsLeader() && entity.leader != null;
	}

	@Override
	public void updateTask() {
		super.updateTask();
			
		if(!entity.world.loadedEntityList.contains(entity.leader)) {
			entity.leader = null;
			entity.setRandomTargetHeading();
			return;
		}
		if(entity.canEntityBeSeen(entity.leader)  && entity.hookTarget == null) {
		entity.setTargetHeading(entity.leader.posX, entity.leader.posY - 5 + rand.nextInt(10), entity.leader.posZ,
				true);
		}
		if (entity.leader.aggressTarget != null) {
			entity.aggressTarget = entity.leader.aggressTarget;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
    public boolean shouldContinueExecuting() {
		return entity.isInWater() && !entity.getIsLeader() && entity.leader != null;
	}
}
