
package net.tropicraft.core.common.dimension;

import com.mojang.math.Vector3d;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;


public class TeleporterTropics extends PortalForcer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static Block PORTAL_WALL_BLOCK;
    private static Block PORTAL_BLOCK;
    private static Block TELEPORTER_BLOCK;

    private final ServerLevel world;
    private final Random random;

    /** Stores successful portal placement locations for rapid lookup. */

    private final Long2ObjectMap<PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap<>(4096);
    private final Object2LongMap<ColumnPos> columnPosCache = new Object2LongOpenHashMap<>();

    static class PortalPosition {
        final BlockPos pos;
        long lastUpdateTime;

        PortalPosition(final BlockPos pos, final long lastUpdateTime) {
            this.pos = pos;
            this.lastUpdateTime = lastUpdateTime;
        }
    }

    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */

    private final List<?> destinationCoordinateKeys = new ArrayList<>();

    public TeleporterTropics(ServerLevel world) {
        super(world);
        PORTAL_BLOCK = Blocks.WATER; // todo tropics portal
        TELEPORTER_BLOCK = TropicraftBlocks.PORTAL_WATER.get();
        PORTAL_WALL_BLOCK = Blocks.SANDSTONE; // todo tropics portal wall
        this.world = world;
        this.random = new Random(world.getSeed());
    }

//    public boolean moveEntityToPortal(Entity entity, float yaw) {
//        long startTime = System.currentTimeMillis();
//        if (!placeInExistingPortal(entity, yaw)) {
//            makePortal(entity);
//            placeInExistingPortal(entity, yaw);
//        }
//
//        long finishTime = System.currentTimeMillis();
//
//        System.out.printf("It took %f seconds for TeleporterTropics.placeInPortal to complete\n", (finishTime - startTime) / 1000.0F);
//
//        return true;
//    }

//    // TODO uh, yeah. need this later at some point.
//    @Override
//    @Nullable
//    public PortalInfo func_222272_a(BlockPos entityPos, Vector3d movement, Direction dir, double x, double y, boolean keepTrying) {
//
//    }

//    @Override
//    public boolean placeInPortal(Entity entity, float yaw) {
//        // TODO Auto-generated method stub
//        return super.placeInPortal(entity, yaw);
//    }


    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        int searchArea = 148;
        double closestPortal = -1D;
        int foundX = 0;
        int foundY = 0;
        int foundZ = 0;
        int entityX = Mth.floor(entity.getX());
        int entityZ = Mth.floor(entity.getZ());
        BlockPos blockpos = BlockPos.ZERO;
        boolean notInCache = true;

        long j1 = ChunkPos.asLong(entity.getBlockX(), entity.getBlockZ());

        if (destinationCoordinateCache.containsKey(j1)) {
            //    System.out.println("Setting closest portal to 0");
            PortalPosition portalposition = (PortalPosition)destinationCoordinateCache.get(j1);
            closestPortal = 0.0D;
            blockpos = portalposition.pos;
            portalposition.lastUpdateTime = world.getGameTime();
            notInCache = false;
        } else {
            for (int x = entityX - searchArea; x <= entityX + searchArea; x ++)
            {
                double distX = x + 0.5D - entity.getX();

                for (int z = entityZ - searchArea; z <= entityZ + searchArea; z ++)
                {
                    double distZ = z + 0.5D - entity.getZ();

                    for (int y = world.getHeight() - 1; y >= 0; y--)
                    {
                        BlockPos pos1 = new BlockPos(x, y, z);
                        if (world.getBlockState(pos1).getBlock() == PORTAL_BLOCK)
                        {
                            pos1 = pos1.below();
                            while (world.getBlockState(pos1).getBlock() == PORTAL_BLOCK)
                            {
                                --y;
                                pos1 = pos1.below();
                            }

                            double distY = y + 0.5D - entity.getY();
                            double distance = distX * distX + distY * distY + distZ * distZ;
                            if (closestPortal < 0.0D || distance < closestPortal)
                            {
                                closestPortal = distance;
                                foundX = x;
                                foundY = y;
                                foundZ = z;
                            }
                        }
                    }
                }
            }
        }

        //    System.out.println("Setting closest portal to " + closestPortal);

        if (closestPortal >= 0.0D)
        {
            if (notInCache) {
                this.destinationCoordinateCache.put(j1, new PortalPosition(blockpos, this.world.getGameTime()));
            }

            int x = foundX;
            int y = foundY;
            int z = foundZ;
            double newLocX = x + 0.5D;
            double newLocY = y + 0.5D;
            double newLocZ = z + 0.5D;

            BlockPos pos2 = new BlockPos(x, y, z);

            if (world.getBlockState(pos2.west()).getBlock() == PORTAL_BLOCK) newLocX -= 0.5D;
            if (world.getBlockState(pos2.east()).getBlock() == PORTAL_BLOCK) newLocX += 0.5D;
            if (world.getBlockState(pos2.north()).getBlock() == PORTAL_BLOCK) newLocZ -= 0.5D;
            if (world.getBlockState(pos2.south()).getBlock() == PORTAL_BLOCK) newLocZ += 0.5D;
//            entity.setLocationAndAngles();
//            int worldSpawnX = MathHelper.floor(newLocX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
//            int worldSpawnZ = MathHelper.floor(newLocZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
//            int worldSpawnY = foundY + 5; // Move to top of portal
//
//            entity.setDeltaMovement(0, 0, 0);

            // If the player is entering the tropics, spawn an Encyclopedia Tropica
            // in the spawn portal chest (if they don't already have one AND one isn't
            // already in the chest)
            //TODO
//            if (entity instanceof EntityPlayer) {
//                EntityPlayer player = (EntityPlayer) entity;
//                if (world.provider instanceof WorldProviderTropicraft) {
//                    //TODO improve this logical check to an NBT tag or something?
//                    if (!player.inventory.hasItemStack(new ItemStack(ItemRegistry.encyclopedia))) {
//                        // Search for the spawn chest
//                        TileEntityBambooChest chest = null;
//                        int chestX = MathHelper.floor(newLocX);
//                        int chestZ = MathHelper.floor(newLocZ);
//                        chestSearch:
//                            for (int searchX = -3; searchX < 4; searchX++) {
//                                for (int searchZ = -3; searchZ < 4; searchZ++) {
//                                    for (int searchY = -4; searchY < 5; searchY++) {
//                                        BlockPos chestPos = new BlockPos(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ);
//                                        if (world.getBlockState(chestPos).getBlock() == BlockRegistry.bambooChest) {
//                                            chest = (TileEntityBambooChest)world.getTileEntity(chestPos);
//                                            if (chest != null && chest.isUnbreakable()) {
//                                                break chestSearch;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                        // Make sure chest doesn't have the encyclopedia
//                        ///TODO
////                        if (chest!= null && chest.isUnbreakable()) {
////                            boolean hasEncyclopedia = false;
////                            for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
////                                ItemStack stack = chest.getStackInSlot(inv);
////                                if (stack.getItem() == ItemRegistry.encyclopedia) {
////                                    hasEncyclopedia = true;
////                                }
////                            }
////
////                            // Give out a new encyclopedia
////                            if (!hasEncyclopedia) {
////                                for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
////                                    ItemStack stack = chest.getStackInSlot(inv);
////                                    if (stack.isEmpty()) {
////                                        chest.setInventorySlotContents(inv, new ItemStack(ItemRegistry.encyclopedia, 1));
////                                        break;
////                                    }
////                                }
////                            }
////                        }
//                   }
            //               }
            //          }

            return new PortalInfo(new Vec3(newLocX, newLocY + 2, newLocZ), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
        } else {
            return defaultPortalInfo.apply(destWorld);//null;
        }
    }

//    @Override
//    public PortalInfo placeInExistingPortal(BlockPos pos, Vec3 motion, Direction directionIn, double posX, double posZ, boolean isPlayer) {
//        int searchArea = 148;
//        double closestPortal = -1D;
//        int foundX = 0;
//        int foundY = 0;
//        int foundZ = 0;
//        int entityX = (int) Math.floor(posX);
//        int entityZ = (int) Math.floor(posZ);
//        BlockPos blockpos = BlockPos.ZERO;
//        boolean notInCache = true;
//
//        long j1 = ChunkPos.asLong(entityX, entityZ);
//
//        if (destinationCoordinateCache.containsKey(j1)) {
//            //    System.out.println("Setting closest portal to 0");
//            PortalPosition portalposition = (PortalPosition)destinationCoordinateCache.get(j1);
//            closestPortal = 0.0D;
//            blockpos = portalposition.pos;
//            portalposition.lastUpdateTime = world.getGameTime();
//            notInCache = false;
//        } else {
//            for (int x = entityX - searchArea; x <= entityX + searchArea; x ++)
//            {
//                double distX = x + 0.5D - posX;
//
//                for (int z = entityZ - searchArea; z <= entityZ + searchArea; z ++)
//                {
//                    double distZ = z + 0.5D - posZ;
//
//                    for (int y = world.getHeight() - 1; y >= 0; y--)
//                    {
//                        BlockPos pos1 = new BlockPos(x, y, z);
//                        if (world.getBlockState(pos1).getBlock() == PORTAL_BLOCK)
//                        {
//                            pos1 = pos1.below();
//                            while (world.getBlockState(pos1).getBlock() == PORTAL_BLOCK)
//                            {
//                                --y;
//                                pos1 = pos1.below();
//                            }
//
//                            double distY = y + 0.5D - pos.getY();
//                            double distance = distX * distX + distY * distY + distZ * distZ;
//                            if (closestPortal < 0.0D || distance < closestPortal)
//                            {
//                                closestPortal = distance;
//                                foundX = x;
//                                foundY = y;
//                                foundZ = z;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        //    System.out.println("Setting closest portal to " + closestPortal);
//
//        if (closestPortal >= 0.0D)
//        {
//            if (notInCache) {
//                this.destinationCoordinateCache.put(j1, new PortalPosition(blockpos, this.world.getGameTime()));
//            }
//
//            int x = foundX;
//            int y = foundY;
//            int z = foundZ;
//            double newLocX = x + 0.5D;
//            double newLocY = y + 0.5D;
//            double newLocZ = z + 0.5D;
//
//            BlockPos pos2 = new BlockPos(x, y, z);
//
//            if (world.getBlockState(pos2.west()).getBlock() == PORTAL_BLOCK) newLocX -= 0.5D;
//            if (world.getBlockState(pos2.east()).getBlock() == PORTAL_BLOCK) newLocX += 0.5D;
//            if (world.getBlockState(pos2.north()).getBlock() == PORTAL_BLOCK) newLocZ -= 0.5D;
//            if (world.getBlockState(pos2.south()).getBlock() == PORTAL_BLOCK) newLocZ += 0.5D;
////            entity.setLocationAndAngles();
////            int worldSpawnX = MathHelper.floor(newLocX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
////            int worldSpawnZ = MathHelper.floor(newLocZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
////            int worldSpawnY = foundY + 5; // Move to top of portal
////
////            entity.setDeltaMovement(0, 0, 0);
//
//            // If the player is entering the tropics, spawn an Encyclopedia Tropica
//            // in the spawn portal chest (if they don't already have one AND one isn't
//            // already in the chest)
//            //TODO
////            if (entity instanceof EntityPlayer) {
////                EntityPlayer player = (EntityPlayer) entity;
////                if (world.provider instanceof WorldProviderTropicraft) {
////                    //TODO improve this logical check to an NBT tag or something?
////                    if (!player.inventory.hasItemStack(new ItemStack(ItemRegistry.encyclopedia))) {
////                        // Search for the spawn chest
////                        TileEntityBambooChest chest = null;
////                        int chestX = MathHelper.floor(newLocX);
////                        int chestZ = MathHelper.floor(newLocZ);
////                        chestSearch:
////                            for (int searchX = -3; searchX < 4; searchX++) {
////                                for (int searchZ = -3; searchZ < 4; searchZ++) {
////                                    for (int searchY = -4; searchY < 5; searchY++) {
////                                        BlockPos chestPos = new BlockPos(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ);
////                                        if (world.getBlockState(chestPos).getBlock() == BlockRegistry.bambooChest) {
////                                            chest = (TileEntityBambooChest)world.getTileEntity(chestPos);
////                                            if (chest != null && chest.isUnbreakable()) {
////                                                break chestSearch;
////                                            }
////                                        }
////                                    }
////                                }
////                            }
////
////                        // Make sure chest doesn't have the encyclopedia
////                        ///TODO
//////                        if (chest!= null && chest.isUnbreakable()) {
//////                            boolean hasEncyclopedia = false;
//////                            for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
//////                                ItemStack stack = chest.getStackInSlot(inv);
//////                                if (stack.getItem() == ItemRegistry.encyclopedia) {
//////                                    hasEncyclopedia = true;
//////                                }
//////                            }
//////
//////                            // Give out a new encyclopedia
//////                            if (!hasEncyclopedia) {
//////                                for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
//////                                    ItemStack stack = chest.getStackInSlot(inv);
//////                                    if (stack.isEmpty()) {
//////                                        chest.setInventorySlotContents(inv, new ItemStack(ItemRegistry.encyclopedia, 1));
//////                                        break;
//////                                    }
//////                                }
//////                            }
//////                        }
////                   }
// //               }
//  //          }
//
//            return new PortalInfo(new Vec3(newLocX, newLocY + 2, newLocZ), motion, directionIn.toYRot(), 0);
//        } else {
//            return null;
//        }
//    }


    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        System.out.println("Start make portal");
        int searchArea = 16;
        double closestSpot = -1D;
        int entityX = Mth.floor(entity.getX());
        int entityY = Mth.floor(entity.getY());
        int entityZ = Mth.floor(entity.getZ());
        int foundX = entityX;
        int foundY = entityY;
        int foundZ = entityZ;

        for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
            double distX = (x + 0.5D) - entity.getX();
            nextCoords:
            for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                double distZ = (z + 0.5D) - entity.getZ();

                // Find topmost solid block at this x,z location
                LevelChunk chunk = world.getChunk(entityX >> 4, entityZ >> 4);
                int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, entityX & 15, entityZ & 15);

                BlockPos pos = new BlockPos(x, y, z);
                for (; y >= 63 - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR ||
                        !getValidBuildBlocks().contains(world.getBlockState(pos))); pos = pos.below()) {
                    y = pos.getY();
                }
                // Only generate portal between sea level and sea level + 20
                if (y > 63 + 20 || y < 63) {
                    continue;
                }

                BlockPos tryPos = new BlockPos(x, y, z);
                if (getValidBuildBlocks().contains(world.getBlockState(tryPos))) {
                    for (int xOffset = -2; xOffset <= 2; xOffset++) {
                        for (int zOffset = -2; zOffset <= 2; zOffset++) {
                            int otherY = world.getHeight() - 1;
                            BlockPos pos1 = new BlockPos(x + xOffset, otherY, z + zOffset);
//                                BlockPos pos2 = tryPos.toImmutable();
                            for (; otherY >= 63 && (world.getBlockState(pos1).getBlock() == Blocks.AIR ||
                                    !world.getBlockState(tryPos).isAir()); pos1 = pos1.below()) {
                                otherY = pos1.getY();
                            }
                            if (Math.abs(y - otherY) >= 3) {
                                continue nextCoords;
                            }

                            //if (!getValidBuildBlocks().contains(world.getBlock(x + xOffset, otherY, z + zOffset))) {
                            //    continue nextCoords;
                            //}
                        }
                    }

                    double distY = (y + 0.5D) - entity.getY();
                    double distance = distX * distX + distY * distY + distZ * distZ;
                    if (closestSpot < 0.0D || distance < closestSpot)
                    {
                        closestSpot = distance;
                        foundX = x;
                        foundY = y;
                        foundZ = z;

                    }
                }
            }
        }

        int worldSpawnX = (int) Math.floor(foundX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
        int worldSpawnZ = (int) Math.floor(foundZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);

        LevelChunk chunk2 = world.getChunk(worldSpawnX >> 4, worldSpawnZ >> 4);
        int worldSpawnY = chunk2.getHeight(Heightmap.Types.WORLD_SURFACE, entityX & 15, entityZ & 15);

        //int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;

        // Max distance to search in every direction for the nearest landmass to build a bridge to
        int SEARCH_FOR_LAND_DISTANCE_MAX = 200;

        // If we can't find a spot (e.g. we're in the middle of the ocean),
        // just put the portal at sea level
        if(closestSpot < 0.0D) {
            // Perhaps this was the culprit

            Random r = new Random();
            foundX += r.nextInt(16) - 8;
            foundZ += r.nextInt(16) - 8;

            foundY = worldSpawnY - 2;
            boolean foundLand = false;

            for (int dist = 1; !foundLand && dist < SEARCH_FOR_LAND_DISTANCE_MAX; dist++) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos pos = new BlockPos(worldSpawnX, worldSpawnY, worldSpawnZ).relative(dir, 3 + dist);
                    BlockState state = world.getBlockState(pos);
                    if (getValidBuildBlocks().contains(state)) {
                        foundLand = true;
                        BlockPos buildpos = new BlockPos(worldSpawnX, worldSpawnY + 1, worldSpawnZ).relative(dir, 3);
                        while (!buildpos.equals(pos.above())) {
                            BlockState thatch = TropicraftBlocks.THATCH_BUNDLE.get().defaultBlockState();
                            world.setBlockAndUpdate(buildpos, thatch);
                            world.setBlockAndUpdate(buildpos.relative(dir.getClockWise()), thatch);
                            world.setBlockAndUpdate(buildpos.relative(dir.getCounterClockWise()), thatch);
                            buildpos = buildpos.relative(dir);
                        }

                        BlockPos stairPosMid = new BlockPos(pos.getX(), worldSpawnY + 1, worldSpawnZ);
                        placeStairs(stairPosMid, dir.getOpposite());
                        generateThatchBorder(worldSpawnX, worldSpawnY + 1, worldSpawnZ);
                        break;
                    }
                }
            }
        }

        //      System.out.printf("Building teleporter at x:<%d>, y:<%d>, z:<%d>\n", foundX, foundY, foundZ);

//        entity.setLocationAndAngles(foundX, foundY + 2, foundZ, entity.rotationYaw, 0.0F);
//        entity.setYRot(entity.getYRot());
        entity.setPosRaw(foundX, foundY + 2, foundZ);
        buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ, entity);

        System.out.println("End makePortal");

        return repositionEntity.apply(true);
    }

//    @Override
//    public boolean makePortal(Entity entity) {
//        System.out.println("Start make portal");
//        int searchArea = 16;
//        double closestSpot = -1D;
//        int entityX = (int) Math.floor(entity.getX());
//        int entityY = (int) Math.floor(entity.getY());
//        int entityZ = (int) Math.floor(entity.getZ());
//        int foundX = entityX;
//        int foundY = entityY;
//        int foundZ = entityZ;
//
//        for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
//            double distX = (x + 0.5D) - entity.getX();
//            nextCoords:
//                for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
//                    double distZ = (z + 0.5D) - entity.getZ();
//
//                    // Find topmost solid block at this x,z location
//                    int y = world.getHeight() - 1;
//                    BlockPos pos = new BlockPos(x, y, z);
//                    for (; y >= 63 - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR ||
//                            !getValidBuildBlocks().contains(world.getBlockState(pos))); pos = pos.below()) {
//                        y = pos.getY();
//                    }
//                    // Only generate portal between sea level and sea level + 20
//                    if (y > 63 + 20 || y < 63) {
//                        continue;
//                    }
//
//                    BlockPos tryPos = new BlockPos(x, y, z);
//                    if (getValidBuildBlocks().contains(world.getBlockState(tryPos))) {
//                        for (int xOffset = -2; xOffset <= 2; xOffset++) {
//                            for (int zOffset = -2; zOffset <= 2; zOffset++) {
//                                int otherY = world.getHeight() - 1;
//                                BlockPos pos1 = new BlockPos(x + xOffset, otherY, z + zOffset);
////                                BlockPos pos2 = tryPos.toImmutable();
//                                for (; otherY >= 63 && (world.getBlockState(pos1).getBlock() == Blocks.AIR ||
//                                        !world.getBlockState(tryPos).isAir()); pos1 = pos1.below()) {
//                                    otherY = pos1.getY();
//                                }
//                                if (Math.abs(y - otherY) >= 3) {
//                                    continue nextCoords;
//                                }
//
//                                //if (!getValidBuildBlocks().contains(world.getBlock(x + xOffset, otherY, z + zOffset))) {
//                                //    continue nextCoords;
//                                //}
//                            }
//                        }
//
//                        double distY = (y + 0.5D) - entity.getY();
//                        double distance = distX * distX + distY * distY + distZ * distZ;
//                        if (closestSpot < 0.0D || distance < closestSpot)
//                        {
//                            closestSpot = distance;
//                            foundX = x;
//                            foundY = y;
//                            foundZ = z;
//
//                        }
//                    }
//                }
//        }
//
//        int worldSpawnX = (int) Math.floor(foundX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
//        int worldSpawnZ = (int) Math.floor(foundZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
//        int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;
//
//        // Max distance to search in every direction for the nearest landmass to build a bridge to
//        int SEARCH_FOR_LAND_DISTANCE_MAX = 200;
//
//        // If we can't find a spot (e.g. we're in the middle of the ocean),
//        // just put the portal at sea level
//        if(closestSpot < 0.0D) {
//            // Perhaps this was the culprit
//
//          Random r = new Random();
//            foundX += r.nextInt(16) - 8;
//            foundZ += r.nextInt(16) - 8;
//
//            foundY = worldSpawnY - 2;
//            boolean foundLand = false;
//
//            for (int dist = 1; !foundLand && dist < SEARCH_FOR_LAND_DISTANCE_MAX; dist++) {
//                for (Direction dir : Direction.Plane.HORIZONTAL) {
//                    BlockPos pos = new BlockPos(worldSpawnX, worldSpawnY, worldSpawnZ).relative(dir, 3 + dist);
//                    BlockState state = world.getBlockState(pos);
//                    if (getValidBuildBlocks().contains(state)) {
//                        foundLand = true;
//                        BlockPos buildpos = new BlockPos(worldSpawnX, worldSpawnY + 1, worldSpawnZ).relative(dir, 3);
//                        while (!buildpos.equals(pos.above())) {
//                            BlockState thatch = TropicraftBlocks.THATCH_BUNDLE.get().defaultBlockState();
//                            world.setBlockAndUpdate(buildpos, thatch);
//                            world.setBlockAndUpdate(buildpos.relative(dir.getClockWise()), thatch);
//                            world.setBlockAndUpdate(buildpos.relative(dir.getCounterClockWise()), thatch);
//                            buildpos = buildpos.relative(dir);
//                        }
//
//                        BlockPos stairPosMid = new BlockPos(pos.getX(), worldSpawnY + 1, worldSpawnZ);
//                        placeStairs(stairPosMid, dir.getOpposite());
//                        generateThatchBorder(worldSpawnX, worldSpawnY + 1, worldSpawnZ);
//                        break;
//                    }
//                }
//            }
//        }
//
//        //      System.out.printf("Building teleporter at x:<%d>, y:<%d>, z:<%d>\n", foundX, foundY, foundZ);
//
////        entity.setLocationAndAngles(foundX, foundY + 2, foundZ, entity.rotationYaw, 0.0F);
////        entity.setYRot(entity.getYRot());
//        entity.setPosRaw(foundX, foundY + 2, foundZ);
//        buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ, entity);
//
//        System.out.println("End makePortal");
//
//        return true;
//    }

    private void placeStairs(BlockPos pos, Direction dir) {
        if (dir == Direction.EAST || dir == Direction.WEST) {
            BlockPos stairPosLeft = pos.offset(0, 0, -1);
            BlockPos stairPosMid = pos;
            BlockPos stairPosRight = pos.offset(0, 0, 1);

            BlockState thatchStairState = TropicraftBlocks.THATCH_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, dir);

            world.setBlockAndUpdate(stairPosLeft, thatchStairState);
            world.setBlockAndUpdate(stairPosMid, thatchStairState);
            world.setBlockAndUpdate(stairPosRight, thatchStairState);
        } else if (dir == Direction.NORTH || dir == Direction.SOUTH) {
            BlockPos stairPosLeft = pos.offset(-1, 0, 0);
            BlockPos stairPosMid = pos;
            BlockPos stairPosRight = pos.offset(1, 0, 0);

            BlockState thatchStairState = TropicraftBlocks.THATCH_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, dir);

            world.setBlockAndUpdate(stairPosLeft, thatchStairState);
            world.setBlockAndUpdate(stairPosMid, thatchStairState);
            world.setBlockAndUpdate(stairPosRight, thatchStairState);
        }
    }

    private void generateThatchBorder(int x, int y, int z) {
        for (int zOffset = -4; zOffset <= 4; zOffset++) {
            for (int xOffset = -4; xOffset <= 4; xOffset++) {
                boolean isWall = xOffset < -2 || xOffset > 2 || zOffset < -2 || zOffset > 2;
                if (isWall) {
                    BlockPos thatchPos = new BlockPos(x + xOffset, y, z + zOffset);
                    world.setBlockAndUpdate(thatchPos, TropicraftBlocks.THATCH_BUNDLE.get().defaultBlockState());
                }
            }
        }
    }


    /**
     * Gets the terrain height at the specified coordinates
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The terrain height at the specified coordinates
     */

    public int getTerrainHeightAt(int x, int z) {
        for(int y = 100; y > 0; y--)
        {
            BlockState state = world.getBlockState(new BlockPos(x, y, z));
            Block block = state.getBlock();
            if(block == Blocks.DIRT || state.getMaterial() == Material.STONE ||
                    state.getMaterial() == Material.WATER || state.getMaterial() == Material.DIRT ||
                    state.getMaterial() == Material.SAND)
            {
                return y;
            }
        }
        return 0;
    }

    public void buildTeleporterAt(int x, int y, int z, Entity entity) {
        System.out.println("start buildTeleporterAt");
        y = y < 9 ? 9 : y;

        for (int yOffset = 4; yOffset >= -7; yOffset--) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                for (int xOffset = -2; xOffset <= 2; xOffset++) {
                    int blockX = x + xOffset;
                    int blockY = y + yOffset;
                    int blockZ = z + zOffset;
                    BlockPos pos = new BlockPos(blockX, blockY, blockZ);

                    if (yOffset == -7) {
                        // Set bottom of portal to be solid
                        world.setBlockAndUpdate(pos, PORTAL_WALL_BLOCK.defaultBlockState());
                    } else if (yOffset > 0) {
                        // Set 4 blocks above portal to air
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    } else {
                        boolean isWall = xOffset == -2 || xOffset == 2 || zOffset == -2 || zOffset == 2;
                        if (isWall) {
                            // Set walls around portal
                            world.setBlockAndUpdate(pos, PORTAL_WALL_BLOCK.defaultBlockState());
                        } else {
                            // Set inside of portal
                            boolean isTeleportBlock = yOffset <= -5;
                            if (isTeleportBlock) {
                                world.setBlockAndUpdate(pos, TELEPORTER_BLOCK.defaultBlockState());
                            } else {
                                world.setBlockAndUpdate(pos, PORTAL_BLOCK.defaultBlockState());
                            }

                        }
                    }

                    boolean isCorner = (xOffset == -2 || xOffset == 2) && (zOffset == -2 || zOffset == 2);
                    // TODO
//                    if (yOffset == 0 && isCorner) {
//                        world.setBlockState(pos.up(), BlockRegistry.tikiTorch.defaultBlockState().withProperty(BlockTikiTorch.SECTION, BlockTikiTorch.TorchSection.LOWER), 3);
//                        world.setBlockState(pos.up(2), BlockRegistry.tikiTorch.defaultBlockState().withProperty(BlockTikiTorch.SECTION, BlockTikiTorch.TorchSection.MIDDLE), 3);
//                        world.setBlockState(pos.up(3), BlockRegistry.tikiTorch.defaultBlockState().withProperty(BlockTikiTorch.SECTION, BlockTikiTorch.TorchSection.UPPER), 3);
//                    }

                }
            }
        }

        // Add an unbreakable chest to place encyclopedia in
        // NOTE: using instanceof instead of world.getWorldInfo().getDimension()
        // because getWorldInfo() may not be set/correct yet
        // TODO
//        if (world.provider instanceof WorldProviderTropicraft) {
//            BlockPos chestPos = new BlockPos(x + 2, y + 1, z);
//            world.setBlockState(chestPos, BlockRegistry.bambooChest.defaultBlockState(), 3);
//            TileEntityBambooChest tile = (TileEntityBambooChest)world.getTileEntity(chestPos);
//            if (tile != null) {
//                tile.setIsUnbreakable(true);
//            }
//        }

        System.out.println("end buildTeleporterAt");
    }

    public void tick(long worldTime) {
        if (worldTime % 100L == 0L) {
            this.pruneColumnPosCache(worldTime);
            this.pruneCoordinateCache(worldTime);
        }
    }

    private void pruneColumnPosCache(long gameTime) {
        LongIterator longiterator = columnPosCache.values().iterator();

        while(longiterator.hasNext()) {
            long i = longiterator.nextLong();
            if (i <= gameTime) {
                longiterator.remove();
            }
        }
    }

    private void pruneCoordinateCache(long gameTime) {
        long i = gameTime - 300L;
        Iterator iterator = this.destinationCoordinateCache.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<ColumnPos, PortalPosition> entry = (Map.Entry)iterator.next();
            PortalPosition position = entry.getValue();
            if (position.lastUpdateTime < i) {
                ColumnPos columnpos = entry.getKey();
                Logger logger = LOGGER;
                Supplier[] asupplier = new Supplier[2];
                DimensionType dimension = this.world.dimensionType();
                asupplier[0] = () -> dimension;
                asupplier[1] = () -> columnpos;
                logger.debug("Removing tropics portal ticket for {}:{}", asupplier);
                this.world.getChunkSource().registerTickingTicket(TicketType.PORTAL, new ChunkPos(position.pos), 3, position.pos);
                iterator.remove();
            }
        }

    }

    /**
     * TODO why in the world is this a thing?
     * @return List of valid block states to build portal on
     */

    private List<BlockState> getValidBuildBlocks() {
        return Arrays.asList(
                Blocks.SAND.defaultBlockState(),
                Blocks.GRASS.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                TropicraftBlocks.PURIFIED_SAND.get().defaultBlockState());
    }
}

