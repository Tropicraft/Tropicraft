package net.tropicraft.entity.ai.jobs;

import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobExtraTargetting extends JobBase {
	
	//Extra targetting for player, atm range based omnipotence
	public int rangeTarget = 48;
	
	public JobExtraTargetting(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (ai.entityToAttack == null) ai.entityToAttack = ent.worldObj.getClosestVulnerablePlayerToEntity(ent, rangeTarget);
	}

}
