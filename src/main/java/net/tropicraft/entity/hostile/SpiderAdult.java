package net.tropicraft.entity.hostile;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.entity.ai.jobs.JobEggManage;

public class SpiderAdult extends SpiderBase {

	//adults have chance to be female on spawn and can then sometimes spawn eggs in a remembered location in caves
	//this can happen while female is active, player doesnt need to be near, but limit amount of eggs, keep female near them
	//eggs do nothing but animate, then once player is close (and can see?) a timer starts
	//if timer hits zero, egg hatches, 2-6 child spiders come out
	
	//there should be maybe a few adult spiders in area of eggs for player to fight while trying to get to eggs
	
	//make simply avoiding these not be a solution, if you are near and it hatches, maybe them seek player out? - not spread, just seek out, too much work otherwise, keep it simple
	
	public boolean isMother = false;
	public int maxEggsToSpawn = -1;
	
	public SpiderAdult(World par1World) {
		super(par1World);
		
		//texture = "/mods/TropicraftMod/textures/entities/spideradult.png";
		this.setSize(1.4F, 0.9F);
		//agent.setMoveSpeed(0.28F);
		this.experienceValue = 6;
	}
	
	@Override
	public void attackMelee(Entity ent, float dist) {
		ent.attackEntityFrom(DamageSource.causeMobDamage(this), isMother ? 6 : 4);
	}
	
	@Override
	public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlock(i, j - 1, k) == Blocks.stone && super.getCanSpawnHere();
    }
	
	@Override
	protected void entityInit() {
		super.entityInit();
		getDataWatcher().addObject(29, Integer.valueOf(0));
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}
	
	@Override
	protected boolean canDespawn()
    {
        return !isMother;
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingBaseData)
    {
		Random rand = new Random();
		if (rand.nextInt(4) == 0) {
			//System.out.println("Spider spawned as mother!");
			isMother = true;
		}
		maxEggsToSpawn = 4 + rand.nextInt(6);
		initSpiderType();
		return super.onSpawnWithEgg(par1EntityLivingBaseData);
    }
	
	public void initSpiderType() {
		if (isMother) {
			JobEggManage job = new JobEggManage(agent.jobMan);
			job.eggSpawnMax = maxEggsToSpawn;
			agent.jobMan.addJob(job);
			//agent.setMoveSpeed(0.38F);
			getDataWatcher().updateObject(29, isMother ? 1 : 0);
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		isMother = par1nbtTagCompound.getBoolean("isMother");
		maxEggsToSpawn = par1nbtTagCompound.getInteger("maxEggsToSpawn");
		initSpiderType();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setBoolean("isMother", isMother);
		par1nbtTagCompound.setInteger("maxEggsToSpawn", maxEggsToSpawn);
	}

}
