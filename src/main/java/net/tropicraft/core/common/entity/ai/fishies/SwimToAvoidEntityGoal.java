package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class SwimToAvoidEntityGoal extends Goal {

    public TropicraftFishEntity entity;
    public Random rand;
    public Class<? extends Entity>[] entityClassToAvoid;
    public double distanceToAvoid;

    public SwimToAvoidEntityGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn, double dist, Class<? extends Entity>[] classes) {
        this.entity = entityObjIn;
        rand = this.entity.getRNG();
        entityClassToAvoid = classes;
        distanceToAvoid = dist;
        setMutexFlags(flags);
    }

    @Override
    public boolean shouldExecute() {
        return entity.isInWater();
    }

    @Override
    public void tick() {
        super.tick();
        
        List<Entity> ents = entity.world.getEntitiesWithinAABBExcludingEntity(entity, entity.getBoundingBox().grow(this.distanceToAvoid));
        List<Class<? extends Entity>> classes = Arrays.asList(entityClassToAvoid);
        for (int i = 0; i < ents.size(); i++) {
            if (classes.contains(ents.get(i).getClass())) {
                entity.fleeEntity(ents.get(i));
                break;
            }
        }            
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return entity.isInWater();
    }
}
