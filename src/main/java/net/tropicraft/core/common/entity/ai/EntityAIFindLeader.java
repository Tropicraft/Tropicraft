package net.tropicraft.core.common.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;

public class EntityAIFindLeader extends Goal {
	
	/** The follower that is following its leader. */
	EntityTropicalFish fish;
	private int delayCounter;
	
	public EntityAIFindLeader(EntityTropicalFish fish) {
		this.fish = fish;
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		if (!this.fish.isAlive()) {
			return false;
		}

		if (this.fish.getIsLeader()) {
			return false;
		}
		
		if (this.fish.leader != null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.delayCounter = 0;
	}

	@Override
	public boolean shouldExecute() {
		return this.fish.leader == null && !this.fish.getIsLeader();
	}
	
	/**
	 * Updates the task
	 */
	@Override
	public void tick() {
		if (--this.delayCounter <= 0) {
			this.delayCounter = 10;
			this.checkForLeader();
		}
	}

	public void checkForLeader(){
		List<EntityTropicalFish> list = fish.world.getEntitiesWithinAABB(EntityTropicalFish.class, fish.getBoundingBox().grow(10D, 10D, 10D));
		for (EntityTropicalFish ent : list){
			System.out.println("Checking for leader");
			if (ent.getColor() == fish.getColor()) {
				System.out.println("yeah");
				if (fish.getEntityId() > ent.getEntityId()) {
					System.out.println("Found leader!");
					fish.leader = ent;
					fish.setIsLeader(false);
					ent.setIsLeader(true);
					ent.leader = null;
				} else {
					fish.setIsLeader(true);
					fish.leader = null;
				}
			}
		}
	}
}



