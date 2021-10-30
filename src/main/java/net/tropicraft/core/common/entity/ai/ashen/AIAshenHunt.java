package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AIAshenHunt extends Goal {

    public AshenEntity ashen;
    public long huntRange = 24;
    public long keepDistantRange = 14;
    
    public boolean xRay = false;
    
    public boolean useMelee = false;
    public int useMeleeCountdown = 0;
    public int useMeleeCountdownMax = 80;
    
    public Vec3 targetLastPos = null;
    public int targetNoMoveTicks = 0;
    public int targetNoMoveTicksMax = 4;
    public int panicTicks = 0;
    
    public LivingEntity target;
    
    public AIAshenHunt(AshenEntity ashen) {
        this.ashen = ashen;
    }

    @Override
    public boolean canUse() {
        LivingEntity entitylivingbase = ashen.getTarget();

        if (entitylivingbase == null) {
            return false;
        } else {
            this.target = entitylivingbase;
            return true;
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.ashen.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
    }
}
