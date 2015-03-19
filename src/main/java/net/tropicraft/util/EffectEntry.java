package net.tropicraft.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EffectEntry {

	private Vec3 startPos;
	private EntityLivingBase entity;
	private int effectID;
	private int effectTime = 100;
	
	public EffectEntry(EntityLivingBase entity) {
		System.out.println("new entry: " + entity);
		this.entity = entity;
		startPos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		init();
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
		return effectTime <= 0;
	}
	
	public void cleanup() {
		System.out.println("remove entry: " + entity);
		entity = null;
	}
	
	public void tickServerAI() {
		effectTime--;
		EntityLiving ent = (EntityLiving) entity;
		
		ent.motionX = ent.motionY = ent.motionZ = 0;
		ent.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
	}
	
	public void tickClientPlayer() {
		effectTime--;
		EntityPlayer ent = (EntityPlayer) entity;
		
		ent.motionX = ent.motionY = ent.motionZ = 0;
		ent.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
	}
}
