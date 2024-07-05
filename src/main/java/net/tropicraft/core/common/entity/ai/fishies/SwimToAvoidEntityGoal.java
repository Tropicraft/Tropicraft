package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SwimToAvoidEntityGoal extends Goal {

    public TropicraftFishEntity entity;
    public RandomSource rand;
    public Class<? extends Entity>[] entityClassToAvoid;
    public double distanceToAvoid;

    public SwimToAvoidEntityGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn, double dist, Class<? extends Entity>[] classes) {
        this.entity = entityObjIn;
        rand = this.entity.getRandom();
        entityClassToAvoid = classes;
        distanceToAvoid = dist;
        setFlags(flags);
    }

    @Override
    public boolean canUse() {
        return entity.isInWater();
    }

    @Override
    public void tick() {
        super.tick();

        List<Entity> ents = entity.level().getEntities(entity, entity.getBoundingBox().inflate(this.distanceToAvoid));
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
    public boolean canContinueToUse() {
        return entity.isInWater();
    }
}
