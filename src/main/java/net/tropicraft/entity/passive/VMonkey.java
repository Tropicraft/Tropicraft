package net.tropicraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityCoroAI;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCItemRegistry;
import CoroUtil.componentAI.AIAgent;
import CoroUtil.componentAI.jobSystem.JobPlay;
import CoroUtil.componentAI.jobSystem.JobTamable;
import CoroUtil.diplomacy.TeamTypes;
import CoroUtil.util.CoroUtilBlock;

public class VMonkey extends EntityCoroAI {

	//Client side used, datawatcher it instead
	public boolean isSitting;
	public boolean isClimbing;
	
	public int noMoveTicks = 0;
	public Vec3 prevPos = null;

	public VMonkey(World par1World) {
		super(par1World);
		setSize(0.8F, 0.8F);
		
		//TODO: change to coctail for tame item
		agent.jobMan.addJob(new JobTamable(agent.jobMan, new ItemStack(TCItemRegistry.cocktail)));
		agent.jobMan.addJob(new JobPlay(agent.jobMan));
		agent.dipl_info = TeamTypes.getType("animal");
		this.experienceValue = 4;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
	}
	
	public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return CoroUtilBlock.isAir(this.worldObj.getBlock(i, j - 1, k)) && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }
	
	@Override
	public void entityInit() {
		super.entityInit();
		getDataWatcher().addObject(16, (byte)0); //anger state
	}
	
	@Override
	public void checkNewAgent() {
		if (agent == null) agent = new AIAgent(this, true);
	}

	@Override
	public boolean isEnemy(Entity ent) {
		return false;
	}
	
	@Override
	public void onLivingUpdate() {
		// TODO Auto-generated method stub
		super.onLivingUpdate();
		
		if (worldObj.isRemote) {
			Vec3 curPos = Vec3.createVectorHelper(posX, posY, posZ);
			if (prevPos == null) prevPos = curPos;
			
			double speed = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
			if (/*agent.getDWStateAI() == EnumActState.IDLE && */curPos.distanceTo(prevPos) < 0.01 && onGround) {
				noMoveTicks++;
			} else {
				noMoveTicks = 0;
			}
			
			isSitting = noMoveTicks > 5;
			isClimbing = false;
			
			prevPos = curPos;
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		worldObj.playSoundAtEntity(this, "monkeyhurt", 1.0f, 1.0f);
		return super.attackEntityFrom(par1DamageSource, par2);
		
	}
	
	@Override
	public void attackMelee(Entity ent, float dist) {
		super.attackMelee(ent, dist);
		//yeah i doubt this will fire since monkey uses inventory, routes to internal method, an issue
		worldObj.playSoundAtEntity(this, worldObj.rand.nextInt(3) == 0 ? "monkeyangry" : "monkeyhiccup", 1.0f, 1.0f);
	}
	
	@Override
	protected String getLivingSound() {
		return TCInfo.MODID + ":" + (this.isPotionActive(Potion.confusion.id) ? "monkeyhiccup" : "monkeyliving");
	}
	
	@Override
	protected String getHurtSound() {
		return TCInfo.MODID + ":" + "monkeyhurt";
	}
	
	@Override
	protected String getDeathSound() {
		return "";
	}
	
	@Override
	public boolean allowLeashing() {
		return true;
	}

}
