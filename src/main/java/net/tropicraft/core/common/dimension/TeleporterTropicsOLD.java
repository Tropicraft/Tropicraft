
package net.tropicraft.core.common.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class TeleporterTropicsOLD implements ITeleporter {

    private static final Logger LOGGER = LogManager.getLogger("tropicraft");
    private static final Block PORTAL_WALL_BLOCK = Blocks.SANDSTONE; // todo tropics portal
    private static final Block PORTAL_BLOCK = TropicraftBlocks.PORTAL_WATER.get();
    private static final Block TELEPORTER_BLOCK = TropicraftBlocks.TELEPORT_PORTAL_WATER.get();

    private final ServerLevel world;
    private final Random random;

    public TeleporterTropicsOLD(ServerLevel world) {
        this.world = world;
        this.random = new Random(world.getSeed());
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        long startTime = System.currentTimeMillis();
        PortalInfo portalInfo = findPortalInfoPoi(entity, destWorld);

        if (portalInfo == null) {
            createPortal(entity);

            long finishTime = System.currentTimeMillis();
            LOGGER.info(String.format("[Tropicraft Portal Time]: It took %f seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0F));

            return findPortalInfoPoi(entity, destWorld);
        } else {
            long finishTime = System.currentTimeMillis();
            LOGGER.info(String.format("[Tropicraft Portal Time]: It took %f seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0F));

            return portalInfo;
        }
    }

    public PortalInfo findPortalInfoPoi(Entity entity, ServerLevel destWorld) {
        int searchArea = 128;
        int foundX = 0;
        int foundY = 0;
        int foundZ = 0;

        List<PoiRecord> poiRecords;
        boolean foundClosePortal = false;

        PoiManager poimanager = this.world.getPoiManager();
        poimanager.ensureLoadedAndValid(this.world, entity.getOnPos(), searchArea);

        poiRecords = poimanager.getInSquare((p_77654_) -> p_77654_ == TropicraftPoiTypes.TROPICRAFT_PORTAL.get(), entity.getOnPos(), searchArea, PoiManager.Occupancy.ANY).toList();

        if (poiRecords.isEmpty()) {
            return null;
        }

        for (PoiRecord poiRecord : poiRecords) {
            BlockPos portalPoi = poiRecord.getPos();

            boolean fl1 = world.getBlockState(portalPoi.north()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.south()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.east()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.west()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.below()).getBlock() == PORTAL_BLOCK;

            if (fl1) {
                BlockPos pos1 = portalPoi;
                if (world.getBlockState(pos1).getBlock() == PORTAL_BLOCK)
                {
                    int y = portalPoi.getY();
                    pos1 = pos1.below();
                    while (world.getBlockState(pos1).getBlock() == PORTAL_BLOCK)
                    {
                        --y;
                        pos1 = pos1.below();
                    }

                    LOGGER.info("[Tropicraft Portal]: Found a portal close to the player!");
                    foundClosePortal = true;

                    pos1.above();

                    foundX = pos1.getX();
                    foundY = y;
                    foundZ = pos1.getZ();

                    LOGGER.info(String.format("[Tropicraft Portal]: Current block Pos Values that was found using poi finder [x: %d, y: %d, z: %d]", foundX, foundY, foundZ));
                    break;
                }
            }
        }


        if (foundClosePortal) {

            double newLocX = foundX + 0.5D;
            double newLocY = foundY + 0.5D;
            double newLocZ = foundZ + 0.5D;

            BlockPos pos2 = new BlockPos(foundX, foundY, foundZ);

            if (world.getBlockState(pos2.west()).getBlock() == PORTAL_BLOCK) newLocX -= 0.5D;
            if (world.getBlockState(pos2.east()).getBlock() == PORTAL_BLOCK) newLocX += 0.5D;
            if (world.getBlockState(pos2.north()).getBlock() == PORTAL_BLOCK) newLocZ -= 0.5D;
            if (world.getBlockState(pos2.south()).getBlock() == PORTAL_BLOCK) newLocZ += 0.5D;

            //entity.setLocationAndAngles();
            int worldSpawnX = Mth.floor(newLocX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
            int worldSpawnZ = Mth.floor(newLocZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
            int worldSpawnY = foundY + 5; // Move to top of portal

            entity.setDeltaMovement(0, 0, 0);

            //If the player is entering the tropics, spawn an Encyclopedia Tropica
            //in the spawn portal chest (if they don't already have one AND one isn't
            //already in the chest)
            //TODO [1.17] Possible rework this search code if this is too slow, possible add a POI value for the chest separately or something
            if (entity instanceof Player player) {
                if (this.world.dimension() == TropicraftDimension.WORLD) {
                    //TODO improve this logical check to an NBT tag or something?
                    if (!player.getInventory().contains(new ItemStack(TropicraftItems.NIGEL_STACHE.get()))) { //TODO [1.17]: Replace Nigel Stache item with encyclopedia when reimplemented
                        // Search for the spawn chest
                        BambooChestBlockEntity chest = null;
                        int chestX = Mth.floor(newLocX);
                        int chestZ = Mth.floor(newLocZ);
                        chestSearch:
                        for (int searchX = -3; searchX < 4; searchX++) {
                            for (int searchZ = -3; searchZ < 4; searchZ++) {
                                for (int searchY = -4; searchY < 5; searchY++) {
                                    BlockPos chestPos = new BlockPos(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ);
                                    if (world.getBlockState(chestPos).getBlock() == TropicraftBlocks.BAMBOO_CHEST.get()) {
                                        chest = (BambooChestBlockEntity)world.getBlockEntity(chestPos);
                                        if (chest != null && chest.isUnbreakable()) {
                                            break chestSearch;
                                        }
                                    }
                                }
                            }
                        }

                        // Make sure chest doesn't have the encyclopedia
                        ///TODO
                        if (chest!= null && chest.isUnbreakable()) {
                            boolean hasEncyclopedia = false;
                            for (int inv = 0; inv < chest.getContainerSize(); inv++) {
                                ItemStack stack = chest.getItem(inv);
                                if (stack.getItem() == TropicraftItems.NIGEL_STACHE.get()) { //TODO [1.17]: Replace Nigel Stache item with encyclopedia when reimplemented
                                    hasEncyclopedia = true;
                                }
                            }

                            // Give out a new encyclopedia
                            if (!hasEncyclopedia) {
                                for (int inv = 0; inv < chest.getContainerSize(); inv++) {
                                    ItemStack stack = chest.getItem(inv);
                                    if (stack.isEmpty()) {
                                        chest.setItem(inv, new ItemStack(TropicraftItems.NIGEL_STACHE.get(), 1));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            LOGGER.debug(String.format("[Tropicraft Portal]: Portal Information given to the player [x: %f, y: %f, z: %f]", newLocX, newLocY + 3, newLocZ));
            return new PortalInfo(new Vec3(newLocX, newLocY + 3, newLocZ), Vec3.ZERO, entity.getYRot(), entity.getXRot());
        }
        else{
            LOGGER.info("[Tropicraft Portal]: No Portal was found within the search radius");
            return null;
        }
    }

    public boolean createPortal(Entity entity) {
        LOGGER.debug("[Tropicraft Portal]: Start make portal");
        int searchArea = 16;
        double closestSpot = -1D;
        int entityX = Mth.floor(entity.getX());
        int entityY = Mth.floor(entity.getY());
        int entityZ = Mth.floor(entity.getZ());
        int foundX = entityX;
        int foundY = entityY;
        int foundZ = entityZ;
        int seaLevel = getSeaLevel();

        for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
            double distX = (x + 0.5D) - entity.getX();
            nextCoords:
            for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                double distZ = (z + 0.5D) - entity.getZ();

                // Find topmost solid block at this x,z location
                LevelChunk chunk = world.getChunk(entityX >> 4, entityZ >> 4);
                int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, entityX & 15, entityZ & 15);

                BlockPos pos = new BlockPos(x, y, z);
                while(y >= seaLevel - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR || !isValidBuildBlocks(world.getBlockState(pos))) ){
                    y = pos.getY();
                    pos = pos.below();
                }


                for (; y >= seaLevel - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR ||
                        !isValidBuildBlocks(world.getBlockState(pos))); pos = pos.below()) {
                    y = pos.getY();
                }
                // Only generate portal between sea level and sea level + 20
                if (y > seaLevel + 20 || y < seaLevel) {
                    continue;
                }

                BlockPos tryPos = new BlockPos(x, y, z);
                if (isValidBuildBlocks(world.getBlockState(tryPos))) {
                    for (int xOffset = -2; xOffset <= 2; xOffset++) {
                        for (int zOffset = -2; zOffset <= 2; zOffset++) {
                            int otherY = world.getHeight() - 1;
                            BlockPos pos1 = new BlockPos(x + xOffset, otherY, z + zOffset);
//                                BlockPos pos2 = tryPos.toImmutable();
                            for (; otherY >= seaLevel && (world.getBlockState(pos1).getBlock() == Blocks.AIR ||
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

        int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;

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
                    if (isValidBuildBlocks(state)) {
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

        world.getPoiManager().add(new BlockPos(worldSpawnX, worldSpawnY + 1, worldSpawnZ), TropicraftPoiTypes.TROPICRAFT_PORTAL.get());
        LOGGER.debug(String.format("[Tropicraft Portal]: Setting Poi postion at [X: %d , Y: %d , Z: %d ]", worldSpawnX, (worldSpawnY + 1), worldSpawnZ));

        buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ, entity);

        LOGGER.debug("[Tropicraft Portal]: End makePortal");
        return true;
    }

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
        LevelChunk chunk2 = world.getChunk(x >> 4, z >> 4);
        int worldSpawnY = chunk2.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);

        for(int y = worldSpawnY; y > 0; y--) {
            BlockState state = world.getBlockState(new BlockPos(x, y, z));

            //TODO [1.17]: Confirm that these tags are going to work with modded blocks
            if(state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.WATER) || state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                return y;
            }
        }
        return 0;
    }

    public void buildTeleporterAt(int x, int y, int z, Entity entity) {
        LOGGER.debug("[Tropicraft Portal]: Start buildTeleporterAt");
        y = Math.max(y, 9);

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
                    if (yOffset == 0 && isCorner) {
                        world.setBlock(pos.above(), TropicraftBlocks.TIKI_TORCH.get().defaultBlockState().setValue(TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.LOWER), 3);
                        world.setBlock(pos.above(2), TropicraftBlocks.TIKI_TORCH.get().defaultBlockState().setValue(TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.MIDDLE), 3);
                        world.setBlock(pos.above(3), TropicraftBlocks.TIKI_TORCH.get().defaultBlockState().setValue(TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER), 3);
                    }

                }
            }
        }

        // Add an unbreakable chest to place encyclopedia in
        // NOTE: using instanceof instead of world.getWorldInfo().getDimension()
        // because getWorldInfo() may not be set/correct yet
        // TODO
        if (world.dimension() == TropicraftDimension.WORLD) {
            BlockPos chestPos = new BlockPos(x + 2, y + 1, z);

            world.setBlock(chestPos, TropicraftBlocks.BAMBOO_CHEST.get().defaultBlockState(), 3);
            BambooChestBlockEntity tile = (BambooChestBlockEntity)world.getBlockEntity(chestPos);
            if (tile != null) {
                tile.setIsUnbreakable(true);
            }
        }

        LOGGER.debug("[Tropicraft Portal]: End buildTeleporterAt");
    }

    /**
     * TODO why in the world is this a thing?
     *
     * A method to check if the block a valid place to put a portal
     *
     * @param state A block state of the block being tested
     * @return A boolean value if the blockstate is a valid build block
     */

    private Boolean isValidBuildBlocks(BlockState state) {
        if(state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.WATER) || state.is(BlockTags.BASE_STONE_OVERWORLD)) {
            return true;
        }
        else {
            return false;
        }
    }

    private int getSeaLevel(){
        return this.world.getSeaLevel(); //OG Value: 63
    }
}

