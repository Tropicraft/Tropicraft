package net.tropicraft.entity.ai.jobs;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobAttackTargetShare extends JobBase {
	
	public int rangeTargetShare = 24;
	public Class classToShareWith = null;
	public int shareTriggerCooldown = 0;
	public int shareTriggerCooldownMax = 60;
	
	public JobAttackTargetShare(JobManager jm, Class parClass) {
		super(jm);
		classToShareWith = parClass;
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
		if (shareTriggerCooldown > 0) shareTriggerCooldown--;
	}
	
	@Override
	public boolean hookHit(DamageSource ds, int damage) {
		
		Entity target = ds.getEntity();
		
		if (isEnemy(target) && shareTriggerCooldown == 0) {
			shareTriggerCooldown = shareTriggerCooldownMax;
			
			List<EntityLivingBase> notifyList = ent.worldObj.getEntitiesWithinAABB(classToShareWith, ent.boundingBox.expand(rangeTargetShare, rangeTargetShare, rangeTargetShare));
			
			for (int i = 0; i < notifyList.size(); i++) {
				EntityLivingBase entN = notifyList.get(i);
				if (!ent.isDead && ent instanceof ICoroAI && ((ICoroAI)ent).getAIAgent().entityToAttack == null) {
					System.out.println("sharing targetting");
					((ICoroAI)ent).getAIAgent().entityToAttack = target;
				}
			}
		}
		
		return super.hookHit(ds, damage);
	}

}
