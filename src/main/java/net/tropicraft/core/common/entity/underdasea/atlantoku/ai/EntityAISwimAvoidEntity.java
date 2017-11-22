package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class EntityAISwimAvoidEntity extends EntityAISwimBase {

	public EntityTropicraftWaterBase entity;
	public Random rand;
	public Class<? extends Entity>[] entityClassToAvoid;
	public double distanceToAvoid;

	public EntityAISwimAvoidEntity(int mutex, EntityTropicraftWaterBase entityObjIn, double dist, Class<? extends Entity>[] classes)
    {
        this.entity = entityObjIn;
        rand = this.entity.getRNG();
        entityClassToAvoid = classes;
        distanceToAvoid = dist;
        this.setMutexBits(mutex);
    }

	@Override
	public boolean shouldExecute() {
		return entity.isInWater();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		
		List<Entity> ents = entity.world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().grow(this.distanceToAvoid));
		List<Class<? extends Entity>> classes = Arrays.asList(entityClassToAvoid);
		for(int i = 0; i < ents.size(); i++) {
			if(classes.contains(ents.get(i).getClass())) {
				entity.fleeEntity(ents.get(i));
				break;
			}
		}			
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {

		return entity.isInWater();
	}
}
