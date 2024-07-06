package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SwimToAvoidEntityGoal extends Goal {

    public final TropicraftFishEntity entity;
    public final RandomSource rand;
    public final Class<? extends Entity>[] entityClassToAvoid;
    public final double distanceToAvoid;

    public SwimToAvoidEntityGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn, double dist, Class<? extends Entity>[] classes) {
        entity = entityObjIn;
        rand = entity.getRandom();
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

        List<Entity> ents = entity.level().getEntities(entity, entity.getBoundingBox().inflate(distanceToAvoid));
        List<Class<? extends Entity>> classes = Arrays.asList(entityClassToAvoid);
        for (Entity ent : ents) {
            if (classes.contains(ent.getClass())) {
                entity.fleeEntity(ent);
                break;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return entity.isInWater();
    }
}
