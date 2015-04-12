package net.tropicraft.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EffectEntry {

	private Vec3 startPos;
	private EntityLivingBase entity;
	private int effectID;
	//a value of -1 means forever
	private int effectTime = 100;
	
	public EffectEntry(EntityLivingBase entity) {
		//System.out.println("new entry: " + entity);
		this.entity = entity;
		startPos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		init();
	}
	
	public int getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(int effectTime) {
		this.effectTime = effectTime;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}

	public void setEntity(EntityLivingBase entity) {
		this.entity = entity;
	}
	
	public void tick() {
		if (entity instanceof EntityPlayer) {
			if (entity.worldObj.isRemote) {
				tickClientPlayer();
			}
		} else {
			if (!entity.worldObj.isRemote) {
				tickServerAI();
			}
		}
		
		if (isFinished()) {
			deinit();
		}
	}
	
	public void init() {
		
	}
	
	public void deinit() {
		
	}
	
	public boolean isFinished() {
		return effectTime == 0;
	}
	
	public void cleanup() {
		//System.out.println("remove entry: " + entity);
		entity = null;
	}
	
	public void tickServerAI() {
		if (effectTime > 0) effectTime--;
		EntityLiving ent = (EntityLiving) entity;
		
		ent.motionX = ent.motionZ = 0;
		if (ent.motionY > 0) ent.motionY = 0;
		ent.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
		
		//TODO: AI task manip perhaps
		//TODO: preventing creepers from exploding
	}
	
	public void tickClientPlayer() {
		if (effectTime > 0) effectTime--;
		EntityPlayer ent = (EntityPlayer) entity;
		
		ent.motionX = ent.motionZ = 0;
		if (ent.motionY > 0) ent.motionY = 0;
		ent.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
	}
}
