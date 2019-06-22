package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

public class EntityAIMonkeyLeap extends Goal {

    /** The entity that is leaping. */
    MobEntity leaper;
    /** The entity that the leaper is leaping towards. */
    LivingEntity leapTarget;
    /** The entity's motionY after leaping. */
    float leapMotionY;

    public EntityAIMonkeyLeap(MobEntity leapingEntity, float leapMotionYIn) {
        this.leaper = leapingEntity;
        this.leapMotionY = leapMotionYIn;
        this.setMutexBits(4);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        this.leapTarget = this.leaper.getAttackTarget();

        if (this.leapTarget == null) {
            return false;
        } else {
            double d0 = this.leaper.getDistanceSq(this.leapTarget);

            if (d0 >= 2.0D && d0 <= 24.0D) {
                if (!this.leaper.onGround) {
                    return false;
                }
                else {
                    return this.leaper.getRNG().nextInt(5) == 0;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.leaper.onGround;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        double d0 = this.leapTarget.posX - this.leaper.posX;
        double d1 = this.leapTarget.posZ - this.leaper.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

        if ((double)f >= 1.0E-4D) {
            this.leaper.motionX += d0 / (double)f * 0.8D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
            this.leaper.motionZ += d1 / (double)f * 0.8D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
        }

        this.leaper.motionY = (double)this.leapMotionY;
    }
}



