package net.tropicraft.entity.ai.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.tropicraft.entity.hostile.SpiderEgg;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobEggManage extends JobBase {
	
	public int eggScanRangeMotherToEgg = 24;
	public int eggScanRangeEggToEgg = 6;
	public int eggSpawnCountdown = -1;
	public int eggSpawnCountdownMax = 60;
	public int eggSpawnMax = -1; //set via entity init/nbt
	public int eggMaxDistGuard = 8;
	public List<SpiderEgg> eggs = new ArrayList<SpiderEgg>();
	
	public JobEggManage(JobManager jm) {
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
	public boolean hookHit(DamageSource ds, int damage) {
		
		//attack based trigger for eggs from mother, for creative mode i guess?
		if (ds.getEntity() instanceof EntityLivingBase) {
			startEggHatchTimer();
		}
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (eggSpawnCountdown > 0) eggSpawnCountdown--;
		
		//init/reload first time scan - 'wait' 80 ticks so eggs loaded in with their ids
		if (eggSpawnCountdown == -1 && ent.worldObj.getWorldTime() % 80 == 0) {
			eggSpawnCountdown = eggSpawnCountdownMax;
			List<SpiderEgg> foundEggs = ent.worldObj.getEntitiesWithinAABB(SpiderEgg.class, ent.boundingBox.expand(eggScanRangeMotherToEgg, eggScanRangeMotherToEgg, eggScanRangeMotherToEgg));
			
			for (int i = 0; i < foundEggs.size(); i++) {
				if (foundEggs.get(i).motherID == ai.entID) {
					eggs.add(foundEggs.get(i));
					//System.out.println("refound egg owned by me!");
				}
			}
		}
		
		//spawn more eggs till maxed
		if (/*ai.entityToAttack == null && */ent.onGround && ent.getNavigator().noPath() && eggs.size() < eggSpawnMax) {
			if (eggSpawnCountdown == 0) {
				eggSpawnCountdown = eggSpawnCountdownMax;
				
				Random rand = new Random();
				Vec3 spawnCoords = null;
				if (eggs.size() == 0) {
					spawnCoords = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);
				} else {
					SpiderEgg spawnBesideEgg = eggs.get(rand.nextInt(eggs.size()));
					double range = 1D;
					spawnCoords = Vec3.createVectorHelper(spawnBesideEgg.posX/* + rand.nextDouble() * range - range/2*/, spawnBesideEgg.posY, spawnBesideEgg.posZ/* + rand.nextDouble() * range - range/2*/);
					
				}
				
				SpiderEgg eggSpawn = new SpiderEgg(ent.worldObj);
				eggSpawn.setPosition(spawnCoords.xCoord, spawnCoords.yCoord, spawnCoords.zCoord);
				eggSpawn.onSpawnWithEgg(null);
				eggSpawn.motherID = ai.entID;
				ent.worldObj.spawnEntityInWorld(eggSpawn);
				eggs.add(eggSpawn);
			}
		}
		
		//manage egg list, stay near eggs while no path
		if (eggs.size() > 0) {
			if (ent.worldObj.getWorldTime() % 80 == 0) {
				for (int i = 0; i < eggs.size(); i++) {
					if (eggs.get(i).isDead) {
						eggs.remove(i);
						i--;
					}
				}
				
				//second check since we removed elements above
				if (eggs.size() > 0) {
					if (ent.getNavigator().noPath() && ent.getDistanceToEntity(eggs.get(0)) > eggMaxDistGuard) {
						//System.out.println("move to eggs");
						ai.walkTo(ent, MathHelper.floor_double(eggs.get(0).posX), MathHelper.floor_double(eggs.get(0).posY), MathHelper.floor_double(eggs.get(0).posZ), ai.maxPFRange, 600);
					}
				}

				//LOS based trigger for eggs from mother
				if (ai.entityToAttack instanceof EntityLivingBase) {
					startEggHatchTimer();
				}
			}
		}
	}
	
	public void startEggHatchTimer() {
		for (int i = 0; i < eggs.size(); i++) {
			if (eggs.get(i).job.inStasis) eggs.get(i).job.startHatching();
		}
	}
}
