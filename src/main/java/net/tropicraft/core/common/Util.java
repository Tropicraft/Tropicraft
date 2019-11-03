package net.tropicraft.core.common;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.tropicraft.Constants;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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

    public static boolean tryMoveToEntityLivingLongDist(MobEntity entSource, Entity entityTo, double moveSpeedAmp) {
        return tryMoveToXYZLongDist(entSource, entityTo.getPosition(), moveSpeedAmp);
    }

    public static boolean tryMoveToXYZLongDist(MobEntity ent, BlockPos pos, double moveSpeedAmp) {
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
    public static boolean tryMoveToXYZLongDist(MobEntity ent, int x, int y, int z, double moveSpeedAmp) {

        World world = ent.world;

        boolean success = false;

        if (ent.getNavigator().noPath()) {

            double distToPlayer = getDistance(ent, x, y, z);//ent.getDistanceToEntity(player);

            double followDist = ent.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();

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

                LivingEntity center = ent;

                Random rand = world.rand;

                float randLook = rand.nextInt(90)-45;
                //int height = 10;
                double dist = (followDist * 0.75D) + rand.nextInt((int)followDist / 2);//rand.nextInt(26)+(queue.get(0).retryState * 6);
                int gatherX = (int)Math.floor(center.posX + ((double)(-Math.sin((rotationYaw+randLook) / 180.0F * 3.1415927F)/* * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)*/) * dist));
                int gatherY = (int)center.posY;//Math.floor(center.posY-0.5 + (double)(-MathHelper.sin(center.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D); //center.posY - 0D;
                int gatherZ = (int)Math.floor(center.posZ + ((double)(Math.cos((rotationYaw+randLook) / 180.0F * 3.1415927F)/* * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)*/) * dist));

                BlockPos pos = new BlockPos(gatherX, gatherY, gatherZ);

                if (!world.isBlockLoaded(pos)) return false;

                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                int tries = 0;
                if (!world.isAirBlock(pos)) {
                    //int offset = -5;

                    while (tries < 30) {
                        if (world.isAirBlock(pos) && world.isAirBlock(pos.up())/* || !block.isSideSolid(block.getDefaultState(), world, new BlockPos(gatherX, gatherY, gatherZ), EnumFacing.UP)*/) {
                            break;
                        }
                        gatherY += 1;//offset++;
                        pos = new BlockPos(gatherX, gatherY, gatherZ);
                        state = world.getBlockState(pos);
                        block = state.getBlock();
                        tries++;
                    }
                } else {
                    //int offset = 0;
                    while (tries < 30) {
                        if (!world.isAirBlock(pos) && (state.getMaterial().isSolid() || world.getBlockState(pos).getMaterial() == Material.WATER)) {
                            break;
                        }
                        gatherY -= 1;//offset++;
                        pos = new BlockPos(gatherX, gatherY, gatherZ);
                        state = world.getBlockState(pos);
                        block = state.getBlock();
                        tries++;
                    }
                }

                if (tries < 30) {
                    /*if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                        gatherY--;
                    }*/
                    success = ent.getNavigator().tryMoveToXYZ(gatherX, gatherY, gatherZ, moveSpeedAmp);
                    //System.out.println("pp success? " + success + "- move to player: " + ent + " -> " + player);
                }
            }
        }

        return success;
    }

    public static BlockPos findBlock(MobEntity entity, int scanRange, BiPredicate<World, BlockPos> predicate) {

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
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }

    public static boolean isDeepWater(World world, BlockPos pos) {
        boolean clearAbove = world.isAirBlock(pos.up(1)) && world.isAirBlock(pos.up(2)) && world.isAirBlock(pos.up(3));
        boolean deep = world.getBlockState(pos).getMaterial() == Material.WATER && world.getBlockState(pos.down()).getMaterial() == Material.WATER;
        boolean notUnderground = false;
        if (deep) {
            int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos).getY() - 1;
            notUnderground = height == pos.getY();
        }

        return deep && notUnderground && clearAbove;
    }

    public static boolean isLand(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isSolid();
    }

    public static boolean isFire(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.FIRE;
    }

    public static double getDistance(Entity ent, double x, double y, double z)
    {
        double d0 = ent.posX - x;
        double d1 = ent.posY - y;
        double d2 = ent.posZ - z;
        return (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public static Field findField(Class<?> clazz, String... fieldNames)
    {
        Exception failed = null;
        for (String fieldName : fieldNames)
        {
            try
            {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindFieldException(failed);
    }

    public static class UnableToFindFieldException extends RuntimeException
    {
        private UnableToFindFieldException(Exception e)
        {
            super(e);
        }
    }

    public static int randFlip(final Random rand, final int i) {
        return rand.nextBoolean() ? rand.nextInt(i) : -(rand.nextInt(i));
    }

    public static final String toEnglishName(String internalName) {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static ResourceLocation resource(String location) {
        return new ResourceLocation(Constants.MODID, location);
    }

    /**
     * Removes random unique elements from provided list and returns a new list with
     * extracted elements.
     *
     * @param rand The random used for the randomization of element selection.
     * @param list The list to extract from.
     * @param amount The amount of elements to randomly extract.
     * @param <T>
     * @return A list of random elements extracted from the provided list.
     */
    public static <T> List<T> extractRandomElements(Random rand, List<T> list, int amount) {
        if (amount > list.size()) {
            throw new IllegalArgumentException("Amount of random elements can not be greater than size of provided list.");
        }

        List<T> randValues = Lists.newArrayList();

        for (int i = 0; i < amount; i++) {
            T obj = list.get(rand.nextInt(list.size()));

            randValues.add(obj);
            list.remove(obj);
        }

        return randValues;
    }
}
