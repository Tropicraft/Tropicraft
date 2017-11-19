package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.AxisAlignedBB;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.IPredatorDiet;

public class EntityAISwimAvoidPredator extends EntityAISwimBase {

	public EntityTropicraftWaterBase entity;
	public Random rand;
	public double distanceToAvoid;

	public EntityAISwimAvoidPredator(int mutex, EntityTropicraftWaterBase entityObjIn, double dist) {
		this.entity = entityObjIn;
		rand = this.entity.getRNG();
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

		if (!entity.isAggressing && (entity.ticksExisted) % 20 == 0) {
			List<EntityTropicraftWaterBase> ents = entity.world.getEntitiesWithinAABB(EntityTropicraftWaterBase.class,
					new AxisAlignedBB(entity.getPosition()).expandXyz(distanceToAvoid));
			for (int i = 0; i < ents.size(); i++) {
				EntityTropicraftWaterBase f = ents.get(i);
				if (entity.getDistanceSqToEntity(f) < this.distanceToAvoid && entity.canEntityBeSeen(f)) {
					if (f.aggressTarget != null) {
						if (f.aggressTarget.equals(entity)) {
							entity.fleeEntity(f);
							entity.isPanicking = true;
							// System.out.println("Avoiding!");
							break;
						}
					}
					if(f instanceof IPredatorDiet) {
						if(Arrays.asList(((IPredatorDiet)f).getPreyClasses()).contains(entity.getClass())) {
							entity.fleeEntity(f);
							entity.isPanicking = true;
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {

		return entity.isInWater();
	}
}
