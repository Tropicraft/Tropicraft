package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;
import java.util.List;

public class EntityAIPlayKoa extends Goal
{
    private final EntityKoaBase villagerObj;
    private LivingEntity targetVillager;
    private final double speed;
    private int playTime;

    public EntityAIPlayKoa(EntityKoaBase villagerObjIn, double speedIn)
    {
        this.villagerObj = villagerObjIn;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (this.villagerObj.getGrowingAge() >= 0)
        {
            return false;
        }
        else if (this.villagerObj.getRNG().nextInt(200) != 0)
        {
            return false;
        }
        else
        {
            List<EntityKoaBase> list = this.villagerObj.world.getEntitiesWithinAABB(EntityKoaBase.class, this.villagerObj.getBoundingBox().grow(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;

            for (EntityKoaBase entityvillager : list)
            {
                if (entityvillager != this.villagerObj && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0)
                {
                    double d1 = entityvillager.getDistanceSq(this.villagerObj);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        this.targetVillager = entityvillager;
                    }
                }
            }

            if (this.targetVillager == null)
            {
                Vector3d vec = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
                return vec != null;
            }

            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.playTime > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        if (this.targetVillager != null)
        {
            this.villagerObj.setPlaying(true);
        }

        this.playTime = 1000;
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.villagerObj.setPlaying(false);
        this.targetVillager = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick()
    {
        --this.playTime;

        if (villagerObj.isOnGround() && villagerObj.world.rand.nextInt(30) == 0) {
            this.villagerObj.getJumpController().setJumping();
        }

        if (this.targetVillager != null)
        {
            if (this.villagerObj.getDistanceSq(this.targetVillager) > 4.0D)
            {
                this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
            }
        }
        else if (this.villagerObj.getNavigator().noPath())
        {
            Vector3d vec = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (vec == null)
            {
                return;
            }

            this.villagerObj.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, this.speed);
        }
    }
}


