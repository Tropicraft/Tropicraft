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

        if ((!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.hasNoSky())
        {
            /*if (this.entityObj.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else */if (entityObj.posLastFireplaceFound != null && this.entityObj.getDistanceSq(entityObj.posLastFireplaceFound.getX(), entityObj.posLastFireplaceFound.getY(), entityObj.posLastFireplaceFound.getZ()) < 4.0D)
            {
                //return true and lock koa from moving in update?
                return true;
            }
            else if (entityObj.posLastFireplaceFound != null && this.entityObj.getDistanceSq(entityObj.posLastFireplaceFound.getX(), entityObj.posLastFireplaceFound.getY(), entityObj.posLastFireplaceFound.getZ()) >= 4.0D)
            {
                return true;
            }
            else if (entityObj.posLastFireplaceFound == null)
            {

                //TODO: line of sight check
                BlockPos pos = Util.findBlock(entityObj, 20, Util::isFire);

                if (pos != null) {
                    pos = pos.add(0, -1, 0);
                    entityObj.setFirelacePos(pos);
                    IBlockState state = entityObj.world.getBlockState(pos);
                    System.out.println("found fire place spot to chill");
                    return true;
                } else {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
        return false;
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
            if (entityObj.posLastFireplaceFound != null) {
                IBlockState state = entityObj.world.getBlockState(entityObj.posLastFireplaceFound);
                if (state.getMaterial() != Material.FIRE) {
                    entityObj.posLastFireplaceFound = null;
                    return false;
                }
            }
            return true;
            /*if (entityObj.posLastFireplaceFound != null && this.entityObj.getDistanceSq(entityObj.posLastFireplaceFound.getX(), entityObj.posLastFireplaceFound.getY(), entityObj.posLastFireplaceFound.getZ()) < 4.0D) {
                return true;
            }*/
        } else {

        }

        return false;
    }

    @Override
    public void updateTask() {
        super.updateTask();

        //prevent walking into the fire
        if (!this.entityObj.getNavigator().noPath()) {
            PathPoint pp = this.entityObj.getNavigator().getPath().getFinalPathPoint();
            double dist = entityObj.getPositionVector().distanceTo(new Vec3d(pp.xCoord, pp.yCoord, pp.zCoord));
            if (dist < 3D) {
                entityObj.setSitting(true);
                entityObj.getNavigator().clearPathEntity();
            }
        } else {

        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        //this.insidePosX = -1;
        BlockPos blockpos = this.entityObj.posLastFireplaceFound;
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();

        if (this.entityObj.getDistanceSq(blockpos) > 256.0D)
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3d((double)i + 0.5D, (double)j, (double)k + 0.5D));

            if (vec3d != null)
            {
                this.entityObj.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
            }
        }
        else
        {
            this.entityObj.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        entityObj.setSitting(false);
        /*this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;*/
    }
}