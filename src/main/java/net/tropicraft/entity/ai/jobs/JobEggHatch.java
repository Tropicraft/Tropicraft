package net.tropicraft.entity.ai.jobs;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.tropicraft.entity.hostile.SpiderChild;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobEggHatch extends JobBase {
	
	//nbt loaded via using entity
	public boolean inStasis = true;
	public int countdownHatch = 0;
	public int countdownHatchMax = 20*15;
	public Vec3 lockPos = null;
	
	public int motherID = -1;

	public JobEggHatch(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return !inStasis;
	}
	
	@Override
	public boolean hookHit(DamageSource ds, int damage) {
		
		//this is a backup method of detection #1
		if (ds.getEntity() instanceof EntityPlayer) {
			if (inStasis) {
				startHatching();
			}
		}
		return true;
	}
	
	@Override
	public void onIdleTickAct() {
		//no egg wandering
	}
	
	@Override
	public void tick() {
		super.tick();
		
		//keep egg as dead AI ticking help
		ai.entityToAttack = null;
		if (lockPos == null) {
			lockPos = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);
		} else {
			//perhaps only lock position a while after, so we can let egg post spawning space itself out first
			//ent.motionX = ent.motionY = ent.motionZ = 0F;
			//ent.setPosition(lockPos.xCoord, lockPos.yCoord, lockPos.zCoord);
			ent.getNavigator().clearPathEntity();
		}

		if (!inStasis) {
			if (countdownHatch > 0) {
				countdownHatch--;
				if (countdownHatch == 0) {
					//hatch
					Random rand = new Random();
					int spawnCount = rand.nextInt(3) + 2;
					for (int i = 0; i < spawnCount; i++) {
						SpiderChild spider = new SpiderChild(ent.worldObj);
						spider.setPosition(ent.posX, ent.posY, ent.posZ);
						spider.motionX = (rand.nextDouble() - rand.nextDouble()) * 0.5;
						spider.motionY = (rand.nextDouble() - rand.nextDouble()) * 0.5;
						spider.motionZ = (rand.nextDouble() - rand.nextDouble()) * 0.5;
						ent.worldObj.spawnEntityInWorld(spider);
						ent.setDead();
					}
				}
			}
		} else {
			//this is a backup method of detection #1
			EntityPlayer player = ent.worldObj.getClosestVulnerablePlayerToEntity(ent, 48);
			if (player != null && player.canEntityBeSeen(ent)) {
				startHatching();
			}
		}		
	}
	
	public void startHatching() {
		//System.out.println("start hatching!");
		inStasis = false;
		countdownHatch = countdownHatchMax;
	}
}
