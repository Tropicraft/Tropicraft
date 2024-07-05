package net.tropicraft.core.common.dimension;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

// TODO: this could do with some significant rethinking & refactoring!
public class TropicsPortalLinker {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Block PORTAL_WALL_BLOCK = Blocks.SANDSTONE; // todo tropics portal
    private static final Block PORTAL_BLOCK = TropicraftBlocks.PORTAL_WATER.get();
    private static final Block TELEPORTER_BLOCK = TropicraftBlocks.TELEPORT_WATER.get();

    private final ServerLevel world;

    public TropicsPortalLinker(ServerLevel world) {
        this.world = world;
    }

    @Nullable
    public PortalInfo findOrCreatePortal(Entity entity) {
        long startTime = System.currentTimeMillis();
        PortalInfo portalInfo = findExistingPortal(entity);
        if (portalInfo == null) {
            makePortal(entity);
            // TODO: We just created the portal, why do we need to scan for it again?
            portalInfo = findExistingPortal(entity);
        }
        long finishTime = System.currentTimeMillis();
        LOGGER.debug("It took {} seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0f);
        return portalInfo;
    }

    private PortalInfo findExistingPortal(Entity entity) {
        int searchArea = 148;
        double closestPortalDistance = -1D;
        int foundX = 0;
        int foundY = 0;
        int foundZ = 0;
        int entityX = Mth.floor(entity.getOnPos().getX());
        int entityZ = Mth.floor(entity.getOnPos().getZ());

        // TODO: Use POIs to speed up scan
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
            double distX = x + 0.5 - entity.getOnPos().getX();

            for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                double distZ = z + 0.5 - entity.getOnPos().getZ();

                LevelChunk chunk = world.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));

                for (int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z); y >= chunk.getMinBuildHeight(); y--) {
                    mutablePos.set(x, y, z);
                    if (chunk.getBlockState(mutablePos).is(PORTAL_BLOCK)) {
                        mutablePos.move(Direction.DOWN);
                        while (chunk.getBlockState(mutablePos).is(PORTAL_BLOCK)) {
                            --y;
                            mutablePos.move(Direction.DOWN);
                        }

                        double distY = y + 0.5 - mutablePos.getY();
                        double distance = distX * distX + distY * distY + distZ * distZ;
                        if (closestPortalDistance < 0.0 || distance < closestPortalDistance) {
                            closestPortalDistance = distance;
                            foundX = x;
                            foundY = y;
                            foundZ = z;
                        }
                    }
                }
            }
        }

        if (closestPortalDistance >= 0.0) {
            double newLocX = foundX + 0.5;
            double newLocY = foundY + 0.5;
            double newLocZ = foundZ + 0.5;

            BlockPos pos = new BlockPos(foundX, foundY, foundZ);

            if (world.getBlockState(pos.west()).getBlock() == PORTAL_BLOCK) newLocX -= 0.5;
            if (world.getBlockState(pos.east()).getBlock() == PORTAL_BLOCK) newLocX += 0.5;
            if (world.getBlockState(pos.north()).getBlock() == PORTAL_BLOCK) newLocZ -= 0.5;
            if (world.getBlockState(pos.south()).getBlock() == PORTAL_BLOCK) newLocZ += 0.5;

            return new PortalInfo(new Vec3(newLocX, newLocY + 2, newLocZ), entity.getYRot(), entity.getXRot());
        } else {
            return null;
        }
    }

    private boolean makePortal(Entity entity) {
        int searchArea = 16;
        double closestSpot = -1D;
        int entityX = Mth.floor(entity.getX());
        int entityY = Mth.floor(entity.getY());
        int entityZ = Mth.floor(entity.getZ());
        int foundX = entityX;
        int foundY = entityY;
        int foundZ = entityZ;

        for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
            double distX = (x + 0.5) - entity.getX();
            nextCoords:
            for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                double distZ = (z + 0.5) - entity.getZ();

                // Find topmost solid block at this x,z location
                int y = world.getMaxBuildHeight() - 1;
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
                            int otherY = world.getMaxBuildHeight() - 1;
                            BlockPos pos1 = new BlockPos(x + xOffset, otherY, z + zOffset);
                            BlockPos pos2 = tryPos.mutable();
                            for (; otherY >= 63 && (world.getBlockState(pos1).getBlock() == Blocks.AIR ||
                                    !world.getBlockState(pos2).isAir()); pos1 = pos1.below()) {
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

                    double distY = (y + 0.5) - entity.getY();
                    double distance = distX * distX + distY * distY + distZ * distZ;
                    if (closestSpot < 0.0 || distance < closestSpot) {
                        closestSpot = distance;
                        foundX = x;
                        foundY = y;
                        foundZ = z;
                    }
                }
            }
        }

        int worldSpawnX = Mth.floor(foundX);//TODO + ((RandomSource.create()).nextBoolean() ? 3 : -3);
        int worldSpawnZ = Mth.floor(foundZ);//TODO + ((RandomSource.create()).nextBoolean() ? 3 : -3);
        int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;

        // Max distance to search in every direction for the nearest landmass to build a bridge to
        int SEARCH_FOR_LAND_DISTANCE_MAX = 200;

        // If we can't find a spot (e.g. we're in the middle of the ocean),
        // just put the portal at sea level
        if (closestSpot < 0.0) {
            // Perhaps this was the culprit
            RandomSource r = RandomSource.create();
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

        buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ);

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
     *
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The terrain height at the specified coordinates
     */

    private int getTerrainHeightAt(int x, int z) {
        LevelChunk chunk2 = world.getChunk(x >> 4, z >> 4);
        int worldSpawnY = chunk2.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);

        for (int y = worldSpawnY; y > 0; y--) {
            BlockState state = world.getBlockState(new BlockPos(x, y, z));

            //TODO [1.17]: Confirm that these tags are going to work with modded blocks
            if (state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.WATER) || state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                return y;
            }
        }
        return 0;
    }

    private void buildTeleporterAt(int x, int y, int z) {
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
                    if (yOffset == 0 && isCorner) {
                        world.setBlock(pos.above(), TropicraftBlocks.TIKI_TORCH.get().defaultBlockState().setValue(TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.LOWER), 3);
                        world.setBlock(pos.above(2), TropicraftBlocks.TIKI_TORCH.get().defaultBlockState().setValue(TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.MIDDLE), 3);
                        world.setBlock(pos.above(3), TropicraftBlocks.TIKI_TORCH.get().defaultBlockState().setValue(TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER), 3);
                    }
                }
            }
        }
    }

    /**
     * TODO why in the world is this a thing?
     *
     * @return List of valid block states to build portal on
     */

    private List<BlockState> getValidBuildBlocks() {
        return Arrays.asList(
                Blocks.SAND.defaultBlockState(),
                Blocks.GRASS_BLOCK.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                TropicraftBlocks.PURIFIED_SAND.get().defaultBlockState());
    }

    public record PortalInfo(
            Vec3 position,
            float yRot,
            float xRot
    ) {
    }
}

