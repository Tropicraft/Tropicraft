package net.tropicraft.core.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;

public class Util {

    /*public static boolean removeTask(CreatureEntity ent, Class taskToReplace) {
        for (Goal entry : ent.goalSelector..taskEntries) {
            if (taskToReplace.isAssignableFrom(entry.action.getClass())) {
                ent.tasks.removeTask(entry.action);
                return true;
            }
        }

        return false;
    }*/

    public static boolean tryMoveToEntityLivingLongDist(Mob entSource, Entity entityTo, double moveSpeedAmp) {
        return tryMoveToXYZLongDist(entSource, entityTo.blockPosition(), moveSpeedAmp);
    }

    public static boolean tryMoveToXYZLongDist(Mob ent, BlockPos pos, double moveSpeedAmp) {
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
    public static boolean tryMoveToXYZLongDist(Mob ent, int x, int y, int z, double moveSpeedAmp) {

        Level world = ent.level();

        boolean success = false;

        if (ent.getNavigation().isDone()) {

            double distToPlayer = getDistance(ent, x, y, z);//ent.getDistanceToEntity(player);

            double followDist = ent.getAttribute(Attributes.FOLLOW_RANGE).getValue();

            if (distToPlayer <= followDist) {
                //boolean success = ent.getNavigator().tryMoveToEntityLiving(player, moveSpeedAmp);
                success = ent.getNavigation().moveTo(x, y, z, moveSpeedAmp);
                //System.out.println("success? " + success + "- move to player: " + ent + " -> " + player);
            } else {
                /*int x = MathHelper.floor(player.posX);
                int y = MathHelper.floor(player.posY);
                int z = MathHelper.floor(player.posZ);*/

                double d = x + 0.5F - ent.getX();
                double d2 = z + 0.5F - ent.getZ();
                double d1;
                d1 = y + 0.5F - (ent.getY() + (double) ent.getEyeHeight());

                double d3 = Mth.sqrt((float) (d * d + d2 * d2));
                float f2 = (float) ((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
                float f3 = (float) (-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
                float rotationPitch = -f3;//-ent.updateRotation(rotationPitch, f3, 180D);
                float rotationYaw = f2;//updateRotation(rotationYaw, f2, 180D);

                LivingEntity center = ent;

                RandomSource rand = world.random;

                float randLook = rand.nextInt(90) - 45;
                //int height = 10;
                double dist = (followDist * 0.75D) + rand.nextInt((int) followDist / 2);//rand.nextInt(26)+(queue.get(0).retryState * 6);
                int gatherX = (int) Math.floor(center.getX() + ((double) (-Math.sin((rotationYaw + randLook) / 180.0F * 3.1415927F)/* * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)*/) * dist));
                int gatherY = (int) center.getY();//Math.floor(center.posY-0.5 + (double)(-MathHelper.sin(center.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D); //center.posY - 0D;
                int gatherZ = (int) Math.floor(center.getZ() + ((double) (Math.cos((rotationYaw + randLook) / 180.0F * 3.1415927F)/* * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)*/) * dist));

                BlockPos pos = new BlockPos(gatherX, gatherY, gatherZ);

                if (!world.hasChunkAt(pos)) return false;

                BlockState state = world.getBlockState(pos);
                int tries = 0;
                if (!world.isEmptyBlock(pos)) {
                    //int offset = -5;

                    while (tries < 30) {
                        if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above())/* || !block.isSideSolid(block.defaultBlockState(), world, new BlockPos(gatherX, gatherY, gatherZ), EnumFacing.UP)*/) {
                            break;
                        }
                        gatherY += 1;//offset++;
                        pos = new BlockPos(gatherX, gatherY, gatherZ);
                        state = world.getBlockState(pos);
                        tries++;
                    }
                } else {
                    //int offset = 0;
                    while (tries < 30) {
                        if (!world.isEmptyBlock(pos) && (state.isSolid() || world.getFluidState(pos).is(FluidTags.WATER))) {
                            break;
                        }
                        gatherY -= 1;//offset++;
                        pos = new BlockPos(gatherX, gatherY, gatherZ);
                        state = world.getBlockState(pos);
                        tries++;
                    }
                }

                if (tries < 30) {
                    /*if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                        gatherY--;
                    }*/
                    success = ent.getNavigation().moveTo(gatherX, gatherY, gatherZ, moveSpeedAmp);
                    //System.out.println("pp success? " + success + "- move to player: " + ent + " -> " + player);
                }
            }
        }

        return success;
    }

    public static BlockPos findBlock(Mob entity, int scanRange, BiPredicate<Level, BlockPos> predicate) {

        int scanSize = scanRange;
        int scanSizeY = scanRange / 2;
        int adjustRangeY = 10;

        int tryX;
        int tryY = Mth.floor(entity.getY()) - 1;
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
            tryX = Mth.floor(entity.getX()) + (entity.level().random.nextInt(scanSize) - scanSize / 2);
            int i = tryY + entity.level().random.nextInt(scanSizeY) - (scanSizeY / 2);
            tryZ = Mth.floor(entity.getZ()) + entity.level().random.nextInt(scanSize) - scanSize / 2;
            BlockPos posTry = new BlockPos(tryX, tryY, tryZ);

            boolean foundBlock = false;
            int newY = i;

            if (!entity.level().isEmptyBlock(posTry)) {
                //scan up
                int tryMax = adjustRangeY;
                while (!entity.level().isEmptyBlock(posTry) && tryMax-- > 0) {
                    newY++;
                    posTry = new BlockPos(tryX, newY, tryZ);
                }

                //if found air and water below it
                /*if (entity.world.isAirBlock(posTry) && entity.world.getBlockState(posTry.add(0, -1, 0)).getMaterial().isLiquid()) {
                    foundWater = true;
                }*/

                if (entity.level().isEmptyBlock(posTry) && predicate.test(entity.level(), posTry.offset(0, -1, 0))) {
                    foundBlock = true;
                }
            } else {
                //scan down
                int tryMax = adjustRangeY;
                while (entity.level().isEmptyBlock(posTry) && tryMax-- > 0) {
                    newY--;
                    posTry = new BlockPos(tryX, newY, tryZ);
                }
                /*if (!entity.world.isAirBlock(posTry) && entity.world.getBlockState(posTry.add(0, 1, 0)).getMaterial().isLiquid()) {
                    foundWater = true;
                }*/
                if (entity.level().isEmptyBlock(posTry.offset(0, 1, 0)) && predicate.test(entity.level(), posTry)) {
                    foundBlock = true;
                }
            }

            if (foundBlock) {
                return posTry;
            }
        }

        return null;
    }

    public static boolean isDeepWater(Level world, BlockPos pos) {
        boolean clearAbove = world.isEmptyBlock(pos.above(1)) && world.isEmptyBlock(pos.above(2)) && world.isEmptyBlock(pos.above(3));
        boolean deep = world.getFluidState(pos).is(FluidTags.WATER) && world.getFluidState(pos.below()).is(FluidTags.WATER);
        boolean notUnderground = false;
        if (deep) {
            int height = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
            notUnderground = height == pos.getY();
        }

        return deep && notUnderground && clearAbove;
    }

    public static boolean isLand(Level world, BlockPos pos) {
        return world.getBlockState(pos).isSolid();
    }

    public static double getDistance(Entity ent, double x, double y, double z) {
        double d0 = ent.getX() - x;
        double d1 = ent.getY() - y;
        double d2 = ent.getZ() - z;
        return Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
    }

    // Returns the axis that a rotatable block should face based on a start and end position
    public static Direction.Axis getAxisBetween(BlockPos start, BlockPos end) {
        Direction.Axis axis = Direction.Axis.Y;
        int xOffset = Math.abs(end.getX() - start.getX());
        int zOffset = Math.abs(end.getZ() - start.getZ());
        int maxOffset = Math.max(xOffset, zOffset);

        if (maxOffset > 0) {
            if (xOffset == maxOffset) {
                axis = Direction.Axis.X;
            } else {
                axis = Direction.Axis.Z;
            }
        }

        return axis;
    }

    @Nullable
    public static BlockPos findLowestBlock(List<BlockPos> blocks) {
        if (blocks.isEmpty()) return null;

        BlockPos lowest = blocks.getFirst();
        for (int i = 1; i < blocks.size(); i++) {
            BlockPos block = blocks.get(i);
            if (lowest.getY() > block.getY()) {
                lowest = block;
            }
        }

        return lowest;
    }
}
