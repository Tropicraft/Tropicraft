package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class AIAshenHunt extends EntityAIBase {

    public EntityAshen ashen;
    public long huntRange = 24;
    public long keepDistantRange = 14;
    
    public boolean xRay = false;
    
    public boolean useMelee = false;
    public int useMeleeCountdown = 0;
    public int useMeleeCountdownMax = 80;
    
    public Vec3d targetLastPos = null;
    public int targetNoMoveTicks = 0;
    public int targetNoMoveTicksMax = 4;
    public int panicTicks = 0;
    
    public EntityLivingBase target;
    
    public AIAshenHunt(EntityAshen ashen) {
        this.ashen = ashen;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.ashen.getAttackTarget();

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

    public void resetTask() {
        this.target = null;
    //    this.field_75318_f = 0;
   //s     this.rangedAttackTime = -1;
    }
}
