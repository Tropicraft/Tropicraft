package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AIAshenHunt extends Goal {

    public AshenEntity ashen;
    public long huntRange = 24;
    public long keepDistantRange = 14;
    
    public boolean xRay = false;
    
    public boolean useMelee = false;
    public int useMeleeCountdown = 0;
    public int useMeleeCountdownMax = 80;
    
    public Vector3d targetLastPos = null;
    public int targetNoMoveTicks = 0;
    public int targetNoMoveTicksMax = 4;
    public int panicTicks = 0;
    
    public LivingEntity target;
    
    public AIAshenHunt(AshenEntity ashen) {
        this.ashen = ashen;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity entitylivingbase = ashen.getAttackTarget();

        if (entitylivingbase == null) {
            return false;
        } else {
            this.target = entitylivingbase;
            return true;
        }
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.ashen.getNavigator().noPath();
    }

    @Override
    public void resetTask() {
        this.target = null;
    }
}
