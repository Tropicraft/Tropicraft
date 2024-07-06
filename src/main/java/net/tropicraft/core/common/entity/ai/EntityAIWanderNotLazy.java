package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIWanderNotLazy extends Goal {

    private final PathfinderMob entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;

    public EntityAIWanderNotLazy(PathfinderMob creatureIn, double speedIn) {
        this(creatureIn, speedIn, 120);
    }

    public EntityAIWanderNotLazy(PathfinderMob creatureIn, double speedIn, int chance) {
        entity = creatureIn;
        speed = speedIn;
        executionChance = chance;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!mustUpdate) {
            /*if (this.entity.getAge() >= 100)
            {
                return false;
            }*/

            if (entity.getRandom().nextInt(executionChance) != 0) {
                return false;
            }
        }

        Vec3 vec = DefaultRandomPos.getPos(entity, 10, 7);
        if (vec == null) {
            return false;
        } else {
            xPosition = vec.x;
            yPosition = vec.y;
            zPosition = vec.z;
            mustUpdate = false;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        entity.getNavigation().moveTo(xPosition, yPosition, zPosition, speed);
    }

    /**
     * Makes task to bypass chance
     */
    public void makeUpdate() {
        mustUpdate = true;
    }

    /**
     * Changes task random possibility for execution
     */
    public void setExecutionChance(int newchance) {
        executionChance = newchance;
    }
}



