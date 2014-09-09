package net.tropicraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import CoroUtil.componentAI.AIAgent;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.componentAI.jobSystem.JobHunt;
import CoroUtil.diplomacy.DiplomacyHelper;
import CoroUtil.diplomacy.TeamTypes;

public class EntityCoroAI extends EntityLand implements ICoroAI, IMob {

	public AIAgent agent;
	
	public EntityCoroAI(World par1World) {
		super(par1World);
		setSize(.7F, 1.95F);
		
		agent.jobMan.addPrimaryJob(new JobHunt(agent.jobMan));
		
		agent.shouldFixBadYPathing = true;
		//agent.setSpeedNormalBase(0.28F);
		agent.dipl_info = TeamTypes.getType("hostile");
	}
	
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		checkNewAgent();
		return agent.hookInteract(par1EntityPlayer);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		agent.setSpeedFleeAdditive(0.1F); //additive
		agent.setSpeedNormalBase(0.5F);
		agent.applyEntityAttributes();
	}
	
	@Override
	public void setDead() {
		super.setDead();
		agent.hookSetDead();
	}

	@Override
	public void cleanup() {
		agent = null;
	}
	
	protected boolean isValidLightLevel()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int l = this.worldObj.getBlockLightValue(i, j, k);

            if (this.worldObj.isThundering())
            {
                int i1 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                l = this.worldObj.getBlockLightValue(i, j, k);
                this.worldObj.skylightSubtracted = i1;
            }

            return l <= this.rand.nextInt(8);
        }
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
    {
		checkNewAgent();
		agent.spawnedOrNBTReloadedInit();
		return super.onSpawnWithEgg(par1EntityLivingData);
    }
	
	/*@Override
	public void initCreature() {
		super.initCreature();
		checkNewAgent();
		agent.spawnedOrNBTReloadedInit();
	}*/
	
	@Override
	public boolean getCanSpawnHere() {
		// TODO Auto-generated method stub
		return super.getCanSpawnHere();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (!worldObj.isRemote) {
			if (par1DamageSource == DamageSource.inWall) motionY += 1.5F;
			if (agent != null && agent.jobMan != null) agent.jobMan.hookHit(par1DamageSource, (int)par2);
		} else {
			
		}
		return super.attackEntityFrom(par1DamageSource, par2);
	}
	
	public void checkNewAgent() {
		if (agent == null) agent = new AIAgent(this, false);
	}
	
	@Override
	public void updateAITasks() {
		agent.updateAITasks();
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		checkNewAgent();
		agent.entityInit();
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		checkNewAgent();
		agent.readEntityFromNBT(par1nbtTagCompound);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		agent.writeEntityToNBT(par1nbtTagCompound);
	}
	
	@Override
	public boolean isAIEnabled() {
        return true;
    }

	@Override
	public AIAgent getAIAgent() {
		return agent;
	}

	@Override
	public void setPathResultToEntity(PathEntity pathentity) {
		if (agent != null) agent.setPathToEntity(pathentity);
	}

	@Override
	public int getCooldownMelee() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int getCooldownRanged() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void attackMelee(Entity ent, float dist) {
		ent.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
	}

	@Override
	public void attackRanged(Entity ent, float dist) {
		
	}

	@Override
	public boolean isBreaking() {
		return false;
	}

	@Override
	public boolean isEnemy(Entity ent) {
		if (ent instanceof EntityPlayer && !((EntityPlayer) ent).capabilities.isCreativeMode) return true;
		return DiplomacyHelper.shouldTargetEnt(this, ent);
	}
	
	//special mob methods
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		agent.onLivingUpdateTick();
		
		if (getAIAgent().activeFormation != null && this == getAIAgent().activeFormation.leader) {
			this.motionX *= 0.8D;
			this.motionZ *= 0.8D;
		}
		
		
		
		/*if (!worldObj.isRemote) {
			EntityPlayer entP = worldObj.getClosestPlayerToEntity(this, -1);
			if (entP != null) {
				agent.jobMan.getPrimaryJob().tamable.owner = entP.username;
			}
		}*/
		
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
		
		if (worldObj.isRemote) {
			
			//motionY = 0F;
			//fall through ground fix, moved to proper new spot in AIAgent
			//onGround = true;
			
			if (isInWater()) {
				motionY += 0.03D;
			}
		} else {
			//agent.shouldFixBadYPathing = false;
		}
	}
	
	protected String getLivingSound()
    {
        return null;
    }
	
	@Override
	public boolean allowLeashing() {
		if (agent != null && agent.jobMan != null && agent.jobMan.getPrimaryJob() != null && agent.jobMan.getPrimaryJob().tamable.isTame()) return true;
		return super.allowLeashing();
	}

}
