package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class RandomSwimGoal extends Goal {
    public TropicraftFishEntity entity;
    public Random rand;

    public RandomSwimGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn) {
        this.entity = entityObjIn;
        rand = this.entity.getRNG();
        setMutexFlags(flags);
    }

    @Override
    public boolean shouldExecute() {
        return entity.isInWater() && entity.ticksExisted % 10+rand.nextInt(20) == 0;
    }

    @Override
    public void tick() {
        super.tick();
        
        entity.setRandomTargetHeading();
        if (entity.eatenFishAmount > 0 && rand.nextInt(10) == 0) {
            entity.eatenFishAmount--;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.targetVector == null;
    }
}
