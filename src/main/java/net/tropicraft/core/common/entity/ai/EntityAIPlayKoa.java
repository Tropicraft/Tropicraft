package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

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
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse()
    {
        if (this.villagerObj.getAge() >= 0)
        {
            return false;
        }
        else if (this.villagerObj.getRandom().nextInt(200) != 0)
        {
            return false;
        }
        else
        {
            List<EntityKoaBase> list = this.villagerObj.level.getEntitiesOfClass(EntityKoaBase.class, this.villagerObj.getBoundingBox().inflate(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;

            for (EntityKoaBase entityvillager : list)
            {
                if (entityvillager != this.villagerObj && !entityvillager.isPlaying() && entityvillager.getAge() < 0)
                {
                    double d1 = entityvillager.distanceToSqr(this.villagerObj);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        this.targetVillager = entityvillager;
                    }
                }
            }

            if (this.targetVillager == null)
            {
                Vec3 vec = RandomPos.getPos(this.villagerObj, 16, 3);
                return vec != null;
            }

            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse()
    {
        return this.playTime > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start()
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
    public void stop()
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

        if (villagerObj.isOnGround() && villagerObj.level.random.nextInt(30) == 0) {
            this.villagerObj.getJumpControl().jump();
        }

        if (this.targetVillager != null)
        {
            if (this.villagerObj.distanceToSqr(this.targetVillager) > 4.0D)
            {
                this.villagerObj.getNavigation().moveTo(this.targetVillager, this.speed);
            }
        }
        else if (this.villagerObj.getNavigation().isDone())
        {
            Vec3 vec = RandomPos.getPos(this.villagerObj, 16, 3);
            if (vec == null)
            {
                return;
            }

            this.villagerObj.getNavigation().moveTo(vec.x, vec.y, vec.z, this.speed);
        }
    }
}


