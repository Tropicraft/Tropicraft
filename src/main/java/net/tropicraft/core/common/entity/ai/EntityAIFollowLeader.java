package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;

// Based on EntityAIFollowParent
public class EntityAIFollowLeader extends EntityAIBase {
	/** The follower that is following its leader. */
	EntityTropicalFish follower;
	double moveSpeed;
	private int delayCounter;

	public EntityAIFollowLeader(EntityTropicalFish child, double speed) {
		this.follower = child;
		this.moveSpeed = speed;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return this.follower.leader != null;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		if (!this.follower.isEntityAlive() || (this.follower.leader != null && !this.follower.leader.isEntityAlive())) {
			return false;
		} else {
			double d0 = this.follower.getDistanceSq(this.follower.leader);
			return d0 >= 9.0D && d0 <= 256.0D;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.delayCounter = 0;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		//this.leader = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		if (--this.delayCounter <= 0) {
			this.delayCounter = 4;
			
			if (this.follower.leader != null) {
				System.out.println("moving");
				this.follower.getNavigator().tryMoveToEntityLiving(this.follower.leader, this.moveSpeed);
			}
		}
	}
}