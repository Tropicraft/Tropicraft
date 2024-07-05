package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;

public class RandomSwimGoal extends Goal {
    public TropicraftFishEntity entity;
    public RandomSource rand;

    public RandomSwimGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn) {
        this.entity = entityObjIn;
        rand = this.entity.getRandom();
        setFlags(flags);
    }

    @Override
    public boolean canUse() {
        return entity.isInWater() && entity.tickCount % 10 + rand.nextInt(20) == 0;
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
    public boolean canContinueToUse() {
        return entity.targetVector == null;
    }
}
