package net.tropicraft.fishing;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.tropicraft.entity.EntityHook;

public class FishingEventReelComplete extends FishingEvent {

	public Entity hookedEnt;
	public FishingEventReelComplete(EntityHook h, EntityPlayer p) {
		super(h, p);
		hookedEnt = h.bobber;
		this.totalTicks = 200;
	}

	@Override
	public void onUpdate() {
		if(hookedEnt != null) {
			paralyze(hookedEnt);
		}
	}
	
	public void paralyze(Entity entity) { // contains status effect		
			if (entity instanceof EntityLiving) {

				if (entity instanceof EntityCreeper) {
					//((EntityCreeper) entity.
				}

				entity.rotationYaw = entity.prevRotationYaw;
				if (entity instanceof EntityCreature) {
					/*EntityDartHelper.setEntityToAttack((EntityCreature) entity,
							null);*/
					((EntityCreature) entity).setAttackTarget(null);
				}

				//EntityDartHelper.setEntityAttackTime((EntityLiving) entity, 60);
				((EntityLiving) entity).attackTime = 60;
				//EntityDartHelper.setEntityMoveSpeed((EntityLiving) entity, 0);
				((EntityLiving) entity).setAIMoveSpeed(0);
				//EntityDartHelper.setIsEntityJumping((EntityLiving) entity, false);
				((EntityLiving) entity).setJumping(false);

				if (entity.onGround) {
					entity.motionX = 0;
					//entity.motionY = 0;
					entity.motionZ = 0;
					entity.posX = ((EntityLiving) entity).lastTickPosX;
					entity.posY = ((EntityLiving) entity).lastTickPosY;
					entity.posZ = ((EntityLiving) entity).lastTickPosZ;
					entity.rotationPitch = entity.prevRotationPitch;
					entity.rotationYaw = entity.prevRotationYaw;
				} else {
					entity.motionX = 0;
					//entity.motionY = -.5D;
					entity.motionZ = 0;
					entity.posX = ((EntityLiving) entity).lastTickPosX;
					entity.posZ = ((EntityLiving) entity).lastTickPosZ;
					entity.rotationPitch = entity.prevRotationPitch;
					entity.rotationYaw = entity.prevRotationYaw;

					if ((int) entity.posY > entity.worldObj.getHeightValue(
							(int) entity.posX, (int) entity.posY)) {
						//entity.motionY = -0.5D;
					}
				}

				//EntityDartHelper.setEntityAITasks((EntityLiving) entity, null);
			}
	}

}
