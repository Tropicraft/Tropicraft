package net.tropicraft.core.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Util {

    /*public class PartialPathfindTracker {

        public EntityCreature entity;
        public BlockPos posEndPoint;
        public int pathfindAttempts;
        public double moveSpeedAmp;

        public PartialPathfindTracker(EntityCreature entity) {
            this.entity = entity;
        }

        public void pathNew(BlockPos pos) {
            this.posEndPoint = pos;
            boolean result = Util.tryMoveToXYZLongDist(entity, pos, moveSpeedAmp);
        }

        public boolean pathNext(BlockPos pos) {
            boolean result = Util.tryMoveToXYZLongDist(entity, pos, moveSpeedAmp);
            pathfindAttempts++;
            return result;
        }

        public void reset() {
            pathfindAttempts = 0;
            posEndPoint = null;
        }
    }*/

    public static BlockPos findBlock(EntityLiving entity, int scanRange, BiPredicate<World, BlockPos> predicate) {

        int scanSize = scanRange;
        int scanSizeY = scanRange / 2;
        int adjustRangeY = 10;

        int tryX;
        int tryY = MathHelper.floor(entity.posY) - 1;
        int tryZ;

        for (int ii = 0; ii <= 10; ii++) {
            //try close to entity first few times
            if (ii <= 3) {
                scanSize = 20;
                scanSizeY = 10 / 2;
            } else {
                scanSize = scanRange;
                scanSizeY = scanRange / 2;
            }
            tryX = MathHelper.floor(entity.posX) + (entity.world.rand.nextInt(scanSize)-scanSize/2);
            int i = tryY + entity.world.rand.nextInt(scanSizeY)-(scanSizeY/2);
            tryZ = MathHelper.floor(entity.posZ) + entity.world.rand.nextInt(scanSize)-scanSize/2;
            BlockPos posTry = new BlockPos(tryX, tryY, tryZ);

            boolean foundBlock = false;
            int newY = i;

            if (!entity.world.isAirBlock(posTry)) {
                //scan up
                int tryMax = adjustRangeY;
                while (!entity.world.isAirBlock(posTry) && tryMax-- > 0) {
                    newY++;
                    posTry = new BlockPos(tryX, newY, tryZ);
                }

                //if found air and water below it
                /*if (entity.world.isAirBlock(posTry) && entity.world.getBlockState(posTry.add(0, -1, 0)).getMaterial().isLiquid()) {
                    foundWater = true;
                }*/

                if (entity.world.isAirBlock(posTry) && predicate.test(entity.world, posTry.add(0, -1, 0))) {
                    foundBlock = true;
                }
            } else {
                //scan down
                int tryMax = adjustRangeY;
                while (entity.world.isAirBlock(posTry) && tryMax-- > 0) {
                    newY--;
                    posTry = new BlockPos(tryX, newY, tryZ);
                }
                /*if (!entity.world.isAirBlock(posTry) && entity.world.getBlockState(posTry.add(0, 1, 0)).getMaterial().isLiquid()) {
                    foundWater = true;
                }*/
                if (entity.world.isAirBlock(posTry.add(0, 1, 0)) && predicate.test(entity.world, posTry)) {
                    foundBlock = true;
                }
            }

            if (foundBlock) {
                return posTry;
            }
        }

        return null;
    }

    public static boolean isWater(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isLiquid();
    }

    public static boolean isDeepWater(World world, BlockPos pos) {
        boolean deep = world.getBlockState(pos).getMaterial().isLiquid() && world.getBlockState(pos.down()).getMaterial().isLiquid();
        boolean notUnderground = false;
        if (deep) {
            int height = world.getPrecipitationHeight(pos).getY() - 1;
            notUnderground = height == pos.getY();
        }

        return deep && notUnderground;
    }

    public static boolean isLand(World world, BlockPos pos) {
        return world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP);
    }

    public static boolean isFire(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.FIRE;
    }

    public static boolean tryMoveToEntityLivingLongDist(EntityLiving entSource, Entity entityTo, double moveSpeedAmp) {
        return tryMoveToXYZLongDist(entSource, entityTo.getPosition(), moveSpeedAmp);
    }

    public static boolean tryMoveToXYZLongDist(EntityLiving ent, BlockPos pos, double moveSpeedAmp) {
        return tryMoveToXYZLongDist(ent, pos.getX(), pos.getY(), pos.getZ(), moveSpeedAmp);
    }

    /**
     * From CoroUtilPath
     * If close enough, paths to coords, if too far based on attribute, tries to find best spot towards target to pathfind to
     *
     * @param ent
     * @param x
     * @param y
     * @param z
     * @param moveSpeedAmp
     * @return
     */
    public static boolean tryMoveToXYZLongDist(EntityLiving ent, int x, int y, int z, double moveSpeedAmp) {

        World world = ent.world;

        boolean success = false;

        if (ent.getNavigator().noPath()) {

            double distToPlayer = ent.getDistance(x, y, z);//ent.getDistanceToEntity(player);

            double followDist = ent.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();

            if (distToPlayer <= followDist) {
                //boolean success = ent.getNavigator().tryMoveToEntityLiving(player, moveSpeedAmp);
                success = ent.getNavigator().tryMoveToXYZ(x, y, z, moveSpeedAmp);
                //System.out.println("success? " + success + "- move to player: " + ent + " -> " + player);
            } else {
		        /*int x = MathHelper.floor(player.posX);
		        int y = MathHelper.floor(player.posY);
		        int z = MathHelper.floor(player.posZ);*/

                double d = x+0.5F - ent.posX;
                double d2 = z+0.5F - ent.posZ;
                double d1;
                d1 = y+0.5F - (ent.posY + (double)ent.getEyeHeight());

                double d3 = MathHelper.sqrt(d * d + d2 * d2);
                float f2 = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
                float f3 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
                float rotationPitch = -f3;//-ent.updateRotation(rotationPitch, f3, 180D);
                float rotationYaw = f2;//updateRotation(rotationYaw, f2, 180D);

                EntityLiving center = ent;

                Random rand = world.rand;

                float randLook = rand.nextInt(90)-45;
                //int height = 10;
                double dist = (followDist * 0.75D) + rand.nextInt((int)followDist / 2);//rand.nextInt(26)+(queue.get(0).retryState * 6);
                int gatherX = (int)Math.floor(center.posX + ((double)(-Math.sin((rotationYaw+randLook) / 180.0F * 3.1415927F)/* * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)*/) * dist));
                int gatherY = (int)center.posY;//Math.floor(center.posY-0.5 + (double)(-MathHelper.sin(center.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D); //center.posY - 0D;
                int gatherZ = (int)Math.floor(center.posZ + ((double)(Math.cos((rotationYaw+randLook) / 180.0F * 3.1415927F)/* * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)*/) * dist));

                BlockPos pos = new BlockPos(gatherX, gatherY, gatherZ);

                if (!world.isBlockLoaded(pos)) return false;

                Block block = world.getBlockState(pos).getBlock();
                int tries = 0;
                if (!world.isAirBlock(pos)) {
                    int offset = -5;

                    while (tries < 30) {
                        if (world.isAirBlock(pos) || !block.isSideSolid(block.getDefaultState(), world, new BlockPos(gatherX, gatherY, gatherZ), EnumFacing.UP)) {
                            break;
                        }
                        gatherY += offset++;
                        pos = new BlockPos(gatherX, gatherY, gatherZ);
                        block = world.getBlockState(pos).getBlock();
                        tries++;
                    }
                } else {
                    //int offset = 0;
                    while (tries < 30) {
                        if (!world.isAirBlock(pos) && block.isSideSolid(block.getDefaultState(), world, new BlockPos(gatherX, gatherY, gatherZ), EnumFacing.UP)) {
                            break;
                        }
                        gatherY -= 1;//offset++;
                        pos = new BlockPos(gatherX, gatherY, gatherZ);
                        block = world.getBlockState(pos).getBlock();
                        tries++;
                    }
                }

                if (tries < 30) {
                    success = ent.getNavigator().tryMoveToXYZ(gatherX, gatherY, gatherZ, moveSpeedAmp);
                    //System.out.println("pp success? " + success + "- move to player: " + ent + " -> " + player);
                }
            }
        }

        return success;
    }

    public static boolean removeTask(EntityCreature ent, Class taskToReplace) {
        for (EntityAITasks.EntityAITaskEntry entry : ent.tasks.taskEntries) {
            if (taskToReplace.isAssignableFrom(entry.action.getClass())) {
                ent.tasks.removeTask(entry.action);
                return true;
            }
        }

        return false;

    }

}
