package net.tropicraft.entity.hostile;

import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import CoroUtil.componentAI.jobSystem.JobFormation;

public class EntityTropiSkeleton extends EntityCoroAI implements IMob {

	public EntityTropiSkeleton(World par1World) {
		super(par1World);
		
		//TODO: skeleton held item
		//this.setCurrentItemOrArmor(0, new ItemStack(TCItemRegistry.spearBamboo));
		
		for (int i = 0; i < this.equipmentDropChances.length; ++i)
        {
            this.equipmentDropChances[i] = 0;
        }
		

		agent.jobMan.addJob(new JobFormation(agent.jobMan));
		agent.shouldAvoid = false;
		
		this.experienceValue = 6;
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
            this.dropItem(Items.bone, 1);
        }
        
        //TODO: skeleton drop item
        //if (this.rand.nextInt(10) == 0) this.dropItem(TropicraftItems.spearBamboo.itemID, 1);
        
    }
	
	@Override
	protected String getLivingSound()
    {
        return "mob.skeleton.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.skeleton.death";
    }

}
