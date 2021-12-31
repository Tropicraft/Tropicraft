package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class EntityAIWanderNotLazy extends Goal {

    private final PathfinderMob entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;

    public EntityAIWanderNotLazy(PathfinderMob creatureIn, double speedIn)
    {
        this(creatureIn, speedIn, 120);
    }

    public EntityAIWanderNotLazy(PathfinderMob creatureIn, double speedIn, int chance)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse()
    {
        if (!this.mustUpdate)
        {
            /*if (this.entity.getAge() >= 100)
            {
                return false;
            }*/

            if (this.entity.getRandom().nextInt(this.executionChance) != 0)
            {
                return false;
            }
        }

        Vec3 vec = DefaultRandomPos.getPos(this.entity, 10, 7);
        if (vec == null)
        {
            return false;
        }
        else
        {
            this.xPosition = vec.x;
            this.yPosition = vec.y;
            this.zPosition = vec.z;
            this.mustUpdate = false;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse()
    {
        return !this.entity.getNavigation().isDone();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start()
    {
        this.entity.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    /**
     * Makes task to bypass chance
     */
    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    /**
     * Changes task random possibility for execution
     */
    public void setExecutionChance(int newchance)
    {
        this.executionChance = newchance;
    }
}



