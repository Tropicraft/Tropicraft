package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.IPredatorDiet;

public class EntityAISwimTargetPrey extends EntityAISwimBase {

	public EntityTropicraftWaterBase entity;
	public Random rand;

	public EntityAISwimTargetPrey(int mutex, EntityTropicraftWaterBase entityObjIn)
    {
        entity = entityObjIn;
        rand = entity.getRNG();
        this.setMutexBits(mutex);
    }

	@Override
	public boolean shouldExecute() {
		return entity.isInWater() && entity.canAggress && entity.eatenFishAmount < entity.maximumEatAmount && entity instanceof IPredatorDiet;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		
		// Target selection
			if(entity.ticksExisted % 80 == 0 && entity.aggressTarget == null|| !entity.world.loadedEntityList.contains(entity.aggressTarget)) {
				List<Entity> list = entity.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().grow(20D, 20D, 20D).offset(0.0D, -8.0D, 0.0D), EntitySelectors.IS_ALIVE);
				if(list.size() > 0) {
					Entity ent = list.get(rand.nextInt(list.size()));
					boolean skip = false;
					if(ent.equals(entity)) skip = true;	
					if(ent.getClass().getName().equals(entity.getClass().getName())) skip = true;	
					if(entity instanceof IPredatorDiet) {
						Class[] prey = ((IPredatorDiet)entity).getPreyClasses();
						boolean contains = false;
						for(int i =0; i < prey.length; i++) {
							if(prey[i].getName().equals(ent.getClass().getName())) {
								contains = true;
							}
						}
						if(!contains) {
							skip = true;
						}
					}
					if(!ent.isInWater()) skip = true;				
					if(!entity.canEntityBeSeen(ent)) skip = true;
					
					if(!skip) {
						if (ent instanceof EntityLivingBase){
							if (((EntityLivingBase)ent).isInWater()) {
								entity.aggressTarget = ent;
							}
						}
					}
				}
			}
			if(rand.nextInt(200) == 0) {					
				entity.aggressTarget = null;
				entity.setRandomTargetHeading();
			}
			
	
		
		// Hunt Target and/or Do damage
		if(entity.aggressTarget != null) {
			if(entity.getDistanceSq(entity.aggressTarget) <= entity.width) {
				if(entity.aggressTarget instanceof EntityLivingBase) {
					((EntityLivingBase)entity.aggressTarget).attackEntityFrom(DamageSource.causeMobDamage(entity), entity.attackDamage);
				}
				if(entity.aggressTarget instanceof EntityTropicraftWaterBase) {
					// Was eaten, cancel smoke
					
					if(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY > entity.aggressTarget.getEntityBoundingBox().maxY - entity.aggressTarget.getEntityBoundingBox().minY)
					{
						((EntityTropicraftWaterBase) entity.aggressTarget).setExpRate(0);
						entity.aggressTarget.setDropItemsWhenDead(false);
						
						entity.aggressTarget.setDead();
						entity.heal(1);
						entity.eatenFishAmount++;
					}
				}
				entity.setRandomTargetHeading();
			}else {
				if(entity.canEntityBeSeen(entity.aggressTarget) && entity.ticksExisted % 20 == 0) {	
					entity.setTargetHeading(entity.aggressTarget.posX, entity.aggressTarget.posY, entity.aggressTarget.posZ, true);
				}
			}
			if(entity.aggressTarget != null) {
				if(!entity.canEntityBeSeen(entity.aggressTarget) || !entity.aggressTarget.isInWater()) {
					entity.aggressTarget = null;
					entity.setRandomTargetHeading();
				}
			}
		}

		if(!entity.world.loadedEntityList.contains(entity.aggressTarget) || entity.aggressTarget.isDead) {
			entity.aggressTarget = null;
			entity.setRandomTargetHeading();
		}
		
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {

		return this.shouldExecute();
	}
}
