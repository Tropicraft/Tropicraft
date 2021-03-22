package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class TropiCreeperSwellGoal extends Goal {
    private final TropiCreeperEntity creeper;
    private LivingEntity target;

    public TropiCreeperSwellGoal(TropiCreeperEntity creeper) {
        this.creeper = creeper;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity lvt_1_1_ = this.creeper.getAttackTarget();
        return this.creeper.getCreeperState() > 0 || lvt_1_1_ != null && this.creeper.getDistanceSq(lvt_1_1_) < 9.0D;
    }

    @Override
    public void startExecuting() {
        this.creeper.getNavigator().clearPath();
        this.target = this.creeper.getAttackTarget();
    }

    @Override
    public void resetTask() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.creeper.setCreeperState(-1);
        } else if (this.creeper.getDistanceSq(this.target) > 49.0D) {
            this.creeper.setCreeperState(-1);
        } else if (!this.creeper.getEntitySenses().canSee(this.target)) {
            this.creeper.setCreeperState(-1);
        } else {
            this.creeper.setCreeperState(1);
        }
    }
}
