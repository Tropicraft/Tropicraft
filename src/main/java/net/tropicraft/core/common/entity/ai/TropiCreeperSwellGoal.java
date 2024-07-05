package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

import java.util.EnumSet;

public class TropiCreeperSwellGoal extends Goal {
    private final TropiCreeperEntity creeper;
    private LivingEntity target;

    public TropiCreeperSwellGoal(TropiCreeperEntity creeper) {
        this.creeper = creeper;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity lvt_1_1_ = creeper.getTarget();
        return creeper.getCreeperState() > 0 || lvt_1_1_ != null && creeper.distanceToSqr(lvt_1_1_) < 9.0D;
    }

    @Override
    public void start() {
        creeper.getNavigation().stop();
        target = creeper.getTarget();
    }

    @Override
    public void stop() {
        target = null;
    }

    @Override
    public void tick() {
        if (target == null) {
            creeper.setCreeperState(-1);
        } else if (creeper.distanceToSqr(target) > 49.0D) {
            creeper.setCreeperState(-1);
        } else if (!creeper.getSensing().hasLineOfSight(target)) {
            creeper.setCreeperState(-1);
        } else {
            creeper.setCreeperState(1);
        }
    }
}
