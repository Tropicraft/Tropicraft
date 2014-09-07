package net.tropicraft.entity.koa;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import CoroUtil.componentAI.AIAgent;
import CoroUtil.componentAI.IInvUser;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobHunt;
import CoroUtil.diplomacy.DiplomacyHelper;
import CoroUtil.diplomacy.TeamTypes;

public class EntityKoaBase extends EntityCoroAI {
	
	public boolean wasInWater = false;
	public int inWaterTick, outWaterTick;
	public float waterOffsetY = 0f;
	
	public String koaName = "";
	public final String[] tribalMaleNames = new String[]{"Akamu", "Ekewaka", "Ikaika", "Iukini", "Kai", "Kaimana", "Kaimi", "Kanoa", "Kapena", "Keahi", "Keaweaheulu","Kekipi",
    		"Kekoa", "Konani", "Makani", "Mano", "Nahele"};
	
	public EntityKoaBase(World par1World) {
		super(par1World);
		
		//texture = "/mods/TropicraftMod/textures/entities/koa/KoaMan3.png";
		
		agent.jobMan.clearJobs();
		JobBase jb = new JobHunt(agent.jobMan);
		jb.dontStrayFromHome = true;
		agent.jobMan.addPrimaryJob(jb);
		
		agent.collideResistPathing = 0.7F;
		//agent.setMoveSpeed(0.35F);
		//agent.fleeSpeed = 0.4F;
		agent.dipl_info = TeamTypes.getType("koa");
		/*agent.entInv.shouldLookForPickups = true;*/
		agent.entInv.grabItems = true;
		agent.scanForHomeChest = true;
		agent.shouldFixBadYPathing = true;
		//agent.PFRangePathing = 64;
		
		agent.collideResistClose = agent.collideResistFormation = agent.collideResistPathing = entityCollisionReduction = 0.9F;
		this.experienceValue = 15;
		
		this.func_110163_bv();
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		//agent.setSpeedFleeAdditive(0.1F); //additive
		//agent.setSpeedNormalBase(0.6F);
		agent.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		koaName = par1nbtTagCompound.getString("koaName");
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setString("koaName", koaName);
	}
	
	@Override
	public void checkNewAgent() {
		if (agent == null) {
			agent = new AIAgent(this, true);
		}
	}
	
	@Override
	protected boolean canDespawn()
    {
        return false;
    }

	/*@Override
	public void postInitFakePlayer() {
		if (koaName == null || koaName.equals("")) {
			koaName = tribalMaleNames[worldObj.rand.nextInt(tribalMaleNames.length)];
		}
		this.setCustomNameTag(koaName);
	}*/
	
	@Override
	public boolean isEnemy(Entity ent) {
		//if (ent instanceof EntityPlayer && !((EntityPlayer) ent).capabilities.isCreativeMode) return true;
		return DiplomacyHelper.shouldTargetEnt(this, ent);
	}
	
	@Override
	public void onLivingUpdate() {
		// TODO Auto-generated method stub
		super.onLivingUpdate();
		if(isInWater()){
			if(this.outWaterTick != 0){
				this.outWaterTick = 0;
			}
			this.inWaterTick++;
		}else{
			if(this.inWaterTick != 0){
				this.inWaterTick = 0;
			}
			this.outWaterTick++;
		}
		if (!isInWater() && wasInWater == true && isCollidedHorizontally) motionY = 0.54F; 
		
		wasInWater = isInWater();
		
		if (isInWater() && motionY < -0.001) motionY = -0.001;
		
		if (isInWater() && isCollidedHorizontally) {
			motionX *= 1.2F;
			motionZ *= 1.2F;
		}
	}
	
	/*@Override
	public String getLocalizedName() {
		return "a Koa";
	}*/

}
