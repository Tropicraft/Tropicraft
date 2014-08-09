package net.tropicraft.entity.hostile;

import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import net.tropicraft.entity.ai.jobs.JobEggHatch;

public class SpiderEgg extends EntityCoroAI implements IMob {

	public int motherID = -1;
	public JobEggHatch job;
	
	public SpiderEgg(World par1World) {
		super(par1World);
		
		//texture = "/mods/TropicraftMod/textures/entities/spideregg.png";
		agent.jobMan.clearJobs();
		agent.jobMan.addPrimaryJob(job = new JobEggHatch(agent.jobMan));
		
		agent.shouldAvoid = false;
	}
	
	@Override
	protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		// TODO Auto-generated method stub
		super.readEntityFromNBT(par1nbtTagCompound);
		motherID = par1nbtTagCompound.getInteger("motherID");
		job.motherID = motherID;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		// TODO Auto-generated method stub
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("motherID", motherID);
	}
	
	@Override
	protected String getLivingSound()
    {
        return "mob.spider.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.spider.death";
    }

}
