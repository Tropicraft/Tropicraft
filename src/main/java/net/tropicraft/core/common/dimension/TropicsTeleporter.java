package net.tropicraft.core.common.dimension;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// TODO: this could do with some significant rethinking & refactoring!
public class TropicsTeleporter implements ITeleporter {
    private static final Logger LOGGER = LogManager.getLogger("tropics portal");

    private static final Block PORTAL_WALL_BLOCK = Blocks.SANDSTONE; // todo tropics portal
    private static final Block PORTAL_BLOCK = TropicraftBlocks.PORTAL_WATER.get();
    private static final Block TELEPORTER_BLOCK = TropicraftBlocks.TELEPORT_WATER.get();

    private final ServerLevel world;

    /**
     * Stores successful portal placement locations for rapid lookup.
     */

    private final Long2ObjectMap<PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap<>(4096);

    static final record PortalPosition(BlockPos pos, long lastUpdateTime) {
        public PortalPosition touch(long time) {
            return new PortalPosition(this.pos, time);
        }
    }

    public TropicsTeleporter(ServerLevel world) {
        this.world = world;
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        long startTime = System.currentTimeMillis();
        PortalInfo portalInfo = placeInExistingPortal(entity);

        if (portalInfo == null) {
            makePortal(entity);

            long finishTime = System.currentTimeMillis();
            LOGGER.debug("It took {} seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0F);

            return placeInExistingPortal(entity);
        } else {
            long finishTime = System.currentTimeMillis();
            LOGGER.debug("It took {} seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0F);

            return portalInfo;
        }
    }

    public PortalInfo placeInExistingPortal(Entity entity) {
        int searchArea = 148;
        double closestPortalDistance = -1D;
        int foundX = 0;
        int foundY = 0;
        int foundZ = 0;
        int entityX = Mth.floor(entity.getOnPos().getX());
        int entityZ = Mth.floor(entity.getOnPos().getZ());
        BlockPos blockpos = BlockPos.ZERO;
        boolean notInCache = true;

        long queryPos = ChunkPos.asLong(entityX, entityZ);

        if (destinationCoordinateCache.containsKey(queryPos)) {
            PortalPosition portal = destinationCoordinateCache.get(queryPos);
            closestPortalDistance = 0.0D;
            blockpos = portal.pos;
            destinationCoordinateCache.put(queryPos, portal.touch(world.getGameTime()));
            notInCache = false;
        } else {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
                double distX = x + 0.5D - entity.getOnPos().getX();

                for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                    double distZ = z + 0.5D - entity.getOnPos().getZ();

                    for (int y = world.getMaxBuildHeight() - 1; y >= 0; y--) {
                        mutablePos.set(x, y, z);
                        if (world.getBlockState(mutablePos).getBlock() == PORTAL_BLOCK) {
                            mutablePos.move(Direction.DOWN);
                            while (world.getBlockState(mutablePos).getBlock() == PORTAL_BLOCK) {
                                --y;
                                mutablePos.move(Direction.DOWN);
                            }

                            double distY = y + 0.5D - mutablePos.getY();
                            double distance = distX * distX + distY * distY + distZ * distZ;
                            if (closestPortalDistance < 0.0D || distance < closestPortalDistance) {
                                closestPortalDistance = distance;
                                foundX = x;
                                foundY = y;
                                foundZ = z;
                            }
                        }
                    }
                }
            }
        }

        if (closestPortalDistance >= 0.0D) {
            if (notInCache) {
                this.destinationCoordinateCache.put(queryPos, new PortalPosition(blockpos, this.world.getGameTime()));
            }

            double newLocX = foundX + 0.5D;
            double newLocY = foundY + 0.5D;
            double newLocZ = foundZ + 0.5D;

            BlockPos pos = new BlockPos(foundX, foundY, foundZ);

            if (world.getBlockState(pos.west()).getBlock() == PORTAL_BLOCK) newLocX -= 0.5D;
            if (world.getBlockState(pos.east()).getBlock() == PORTAL_BLOCK) newLocX += 0.5D;
            if (world.getBlockState(pos.north()).getBlock() == PORTAL_BLOCK) newLocZ -= 0.5D;
            if (world.getBlockState(pos.south()).getBlock() == PORTAL_BLOCK) newLocZ += 0.5D;

            return new PortalInfo(new Vec3(newLocX, newLocY + 2, newLocZ), Vec3.ZERO, entity.getYRot(), entity.getXRot());
        } else {
            return null;
        }
    }

    public boolean makePortal(Entity entity) {
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

                    double distY = (y + 0.5D) - entity.getY();
                    double distance = distX * distX + distY * distY + distZ * distZ;
                    if (closestSpot < 0.0D || distance < closestSpot) {
                        closestSpot = distance;
                        foundX = x;
                        foundY = y;
                        foundZ = z;
                    }
                }
            }
        }

        int worldSpawnX = Mth.floor(foundX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
        int worldSpawnZ = Mth.floor(foundZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
        int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;

        // Max distance to search in every direction for the nearest landmass to build a bridge to
        int SEARCH_FOR_LAND_DISTANCE_MAX = 200;

        // If we can't find a spot (e.g. we're in the middle of the ocean),
        // just put the portal at sea level
        if (closestSpot < 0.0D) {
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

    public int getTerrainHeightAt(int x, int z) {
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

    public void buildTeleporterAt(int x, int y, int z) {
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

    public void tick(long worldTime) {
        if (worldTime % 100L == 0L) {
            this.pruneCoordinateCache(worldTime);
        }
    }

    private void pruneCoordinateCache(long gameTime) {
        long sinceTime = gameTime - 300L;

        ObjectIterator<Long2ObjectMap.Entry<PortalPosition>> iterator = Long2ObjectMaps.fastIterator(this.destinationCoordinateCache);
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<PortalPosition> entry = iterator.next();
            PortalPosition position = entry.getValue();
            if (position.lastUpdateTime < sinceTime) {
                ChunkPos columnPos = new ChunkPos(entry.getLongKey());
                DimensionType dimension = this.world.getLevel().dimensionType();
                LOGGER.debug("Removing tropics portal ticket for {}:{}", dimension, columnPos);
                this.world.getChunkSource().registerTickingTicket(TicketType.PORTAL, columnPos, 3, position.pos);
                iterator.remove();
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
                Blocks.GRASS.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                TropicraftBlocks.PURIFIED_SAND.get().defaultBlockState());
    }
}

