package net.tropicraft.entity.hostile;

import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import net.tropicraft.entity.ai.jobs.JobAttackTargetShare;
import CoroUtil.componentAI.jobSystem.JobHunt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpiderBase extends EntityCoroAI implements IMob {

	public SpiderBase(World par1World) {
		super(par1World);
		
		agent.jobMan.clearJobs();
		agent.jobMan.addPrimaryJob(new JobHunt(agent.jobMan) { @Override
		public boolean shouldContinue() {
			return true;
		}});
		agent.jobMan.addJob(new JobAttackTargetShare(agent.jobMan, SpiderBase.class));
		
		agent.shouldAvoid = false;
		this.setSize(1.4F, 0.9F);
	}
	
	@Override
    public boolean getCanSpawnHere()
    {
        return this.isValidLightLevel() && super.getCanSpawnHere();
    }
	
	@Override
	protected void dropFewItems(boolean par1, int par2)
    {
        int j = this.rand.nextInt(2) + this.rand.nextInt(1 + par2);
        int k;

        for (k = 0; k < j; ++k)
        {
            this.dropItem(Items.string, 1);
        }
        
        if (this.rand.nextInt(10) == 0) this.dropItem(Items.string, 1);
        
    }
	
	public boolean isOnLadder()
    {
        return agent != null && agent.entityToAttack == null && !this.getNavigator().noPath() && this.isBesideClimbableBlock();
    }
	
	public boolean isBesideClimbableBlock()
    {
        return this.isCollidedHorizontally;//(this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }
	
	@Override
	public void onLivingUpdate() {
		fallDistance = 0;
		super.onLivingUpdate();
		if (!worldObj.isRemote && agent != null && agent.entityToAttack != null && onGround && worldObj.rand.nextInt(3) == 0 && agent.entityToAttack.getDistanceToEntity(this) < 5) {
			this.jump();
			this.motionX *= 2.4F;
			this.motionZ *= 2.4F;
		}
	}

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {}

    @SideOnly(Side.CLIENT)

    /**
     * How large the spider should be scaled.
     */
    public float spiderScaleAmount()
    {
        return 1.2F;
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

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.spider.step", 0.15F, 1.0F);
    }

}
