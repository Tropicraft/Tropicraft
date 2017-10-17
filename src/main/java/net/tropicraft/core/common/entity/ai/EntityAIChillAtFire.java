package net.tropicraft.core.common.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class EntityAIChillAtFire extends EntityAIBase
{
    private final EntityKoaBase entityObj;

    private int walkingTimeoutMax = 20*10;

    private int walkingTimeout;
    private int repathPentalty = 0;

    public EntityAIChillAtFire(EntityKoaBase entityObjIn)
    {
        this.entityObj = entityObjIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        BlockPos blockpos = new BlockPos(this.entityObj);

        if ((!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.hasNoSky()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        BlockPos blockpos = new BlockPos(this.entityObj);
        //return !this.entityObj.getNavigator().noPath();
        if ((!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.hasNoSky())
        {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateTask() {
        super.updateTask();

        boolean isClose = false;

        BlockPos blockposGoal = null;
        if (this.entityObj.posLastFireplaceFound != null) {
            //path to base of fire
            blockposGoal = this.entityObj.posLastFireplaceFound.add(0, -1, 0);
        } else {
            blockposGoal = this.entityObj.getHomePosition();
        }

        if (blockposGoal == null) {
            resetTask();
            return;
        }

        //prevent walking into the fire
        double dist = entityObj.getPositionVector().distanceTo(new Vec3d(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        if (dist < 3D) {
            entityObj.setSitting(true);
            entityObj.getNavigator().clearPathEntity();
            isClose = true;
        } else {
            entityObj.setSitting(false);
        }

        if (!isClose) {
            if ((this.entityObj.getNavigator().noPath() || walkingTimeout <= 0) && repathPentalty <= 0) {

                int i = blockposGoal.getX();
                int j = blockposGoal.getY();
                int k = blockposGoal.getZ();

                boolean success = false;

                if (this.entityObj.getDistanceSq(blockposGoal) > 256.0D) {
                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3d((double) i + 0.5D, (double) j, (double) k + 0.5D));

                    if (vec3d != null) {
                        success = this.entityObj.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
                    }
                } else {
                    success = this.entityObj.getNavigator().tryMoveToXYZ((double) i + 0.5D, (double) j, (double) k + 0.5D, 1.0D);
                }

                if (!success) {
                    repathPentalty = 40;
                } else {
                    walkingTimeout = walkingTimeoutMax;
                }
            } else {
                if (walkingTimeout > 0) {
                    walkingTimeout--;
                } else {

                }
            }
        }

        if (repathPentalty > 0) {
            repathPentalty--;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        //this.insidePosX = -1;
        //reset any previous path so updateTask can start with a fresh path
        this.entityObj.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        super.resetTask();
        entityObj.setSitting(false);
        walkingTimeout = 0;
        /*this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;*/
    }
}