package net.tropicraft.core.common.dimension.feature.volcano;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.VolcanoBlockEntity;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.TropicraftStructurePieceTypes;

public class VolcanoStructurePiece extends StructurePiece {
    private final static int CALDERA_CUTOFF = 194; //The Y level where if the height of the volcano would pass becomes the caldera
    private final static int VOLCANO_TOP = CALDERA_CUTOFF - 7; //The Y level cut off of the sides of the volcano
    public final static int VOLCANO_CRUST = VOLCANO_TOP - 3; //The Y level where the crust of the volcano generates
    public final static int LAVA_LEVEL = 149; //The Y level where the top of the lava column is
    private final static int CRUST_HOLE_CHANCE = 15; //1 / x chance a certain block of the crust will be missing
    private static final float STEEPNESS = 10.2f;

    private final int radiusX;
    private final int radiusZ;
    private final long noiseSeed;

    private final ImprovedNoise noise;

    protected VolcanoStructurePiece(LevelHeightAccessor heightAccessor, BlockPos pos, int radiusX, int radiusZ, long noiseSeed) {
        super(TropicraftStructurePieceTypes.VOLCANO.get(), 0, boundingBox(heightAccessor, pos, radiusX, radiusZ));
        this.radiusX = radiusX;
        this.radiusZ = radiusZ;
        this.noiseSeed = noiseSeed;
        noise = createNoise(noiseSeed);
    }

    public VolcanoStructurePiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(TropicraftStructurePieceTypes.VOLCANO.get(), tag);
        radiusX = tag.getInt("radius_x");
        radiusZ = tag.getInt("radius_z");
        noiseSeed = tag.getLong("noise_seed");
        noise = createNoise(noiseSeed);
    }

    private static BoundingBox boundingBox(LevelHeightAccessor heightAccessor, BlockPos pos, int radiusX, int radiusZ) {
        return new BoundingBox(
                pos.getX() - radiusX - 1,
                pos.getY(),
                pos.getZ() - radiusZ - 1,
                pos.getX() + radiusX + 1,
                heightAccessor.getMaxBuildHeight(),
                pos.getZ() + radiusZ + 1
        );
    }

    private static ImprovedNoise createNoise(long seed) {
        return new ImprovedNoise(RandomSource.create(seed));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("radius_x", radiusX);
        tag.putInt("radius_z", radiusZ);
        tag.putLong("noise_seed", noiseSeed);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pos) {
        int calderaCutoffY = pos.getY() + CALDERA_CUTOFF;
        int lavaY = pos.getY() + LAVA_LEVEL;
        int topY = pos.getY() + VOLCANO_TOP;
        int crustY = pos.getY() + VOLCANO_CRUST;

        BlockPos corePos = new BlockPos(pos.getX(), level.getMinBuildHeight() + 1, pos.getZ());
        if (chunkBox.isInside(corePos)) {
            level.setBlock(corePos, TropicraftBlocks.VOLCANO.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            if (level.getBlockEntity(corePos) instanceof VolcanoBlockEntity volcano) {
                volcano.setHeightOffset(pos.getY());
            }
        }

        for (int z = chunkBox.minZ(); z <= chunkBox.maxZ(); z++) {
            for (int x = chunkBox.minX(); x <= chunkBox.maxX(); x++) {
                float height = getColumnHeight(x - pos.getX(), z - pos.getZ());
                if (!Float.isNaN(height)) {
                    placeColumn(level, random, x, z, calderaCutoffY, lavaY, topY, crustY, height);
                }
            }
        }
    }

    private void placeColumn(WorldGenLevel level, RandomSource random, int x, int z, int calderaCutoffY, int lavaY, int topY, int crustY, float height) {
        BlockState volcanoState = TropicraftBlocks.CHUNK.get().defaultBlockState();
        BlockState sandState = TropicraftBlocks.VOLCANIC_SAND.get().defaultBlockState();
        BlockState lavaState = Blocks.LAVA.defaultBlockState();
        BlockState airState = Blocks.AIR.defaultBlockState();

        int terrainY = Math.min(level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z), lavaY - 3);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int y = level.getMaxBuildHeight(); y > level.getMinBuildHeight(); y--) {
            mutablePos.set(x, y, z);

            if (height + terrainY < calderaCutoffY) {
                if (height + terrainY <= topY) {
                    if (y <= height + terrainY) {
                        if (y > terrainY) {
                            level.setBlock(mutablePos, volcanoState, Block.UPDATE_CLIENTS);
                        } else if (y > terrainY - 2) {
                            level.setBlock(mutablePos, sandState, Block.UPDATE_CLIENTS);
                        }
                    }
                } else if (y == crustY - 1) {
                    if (random.nextInt(3) != 0) {
                        level.setBlock(mutablePos, volcanoState, Block.UPDATE_CLIENTS);
                    }
                } else if (y <= topY) {
                    level.setBlock(mutablePos, volcanoState, Block.UPDATE_CLIENTS);
                }
            } else {
                // Flat area on top of the volcano
                if (y == crustY && random.nextInt(CRUST_HOLE_CHANCE) != 0) {
                    level.setBlock(mutablePos, volcanoState, Block.UPDATE_CLIENTS);
                } else if (y <= lavaY) {
                    level.setBlock(mutablePos, lavaState, Block.UPDATE_CLIENTS);
                } else {
                    level.setBlock(mutablePos, airState, Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    private float getColumnHeight(float x, float z) {
        float distanceSquared = (float) Mth.lengthSquared(x / radiusX, z / radiusZ);
        if (distanceSquared >= 1.0f) {
            return Float.NaN;
        }
        float noiseValue = (float) Math.abs(noise.noise(x * 0.21 + 0.01, 0.0, z * 0.21 + 0.01)) * 0.45f + 1.0f;
        return STEEPNESS / distanceSquared * noiseValue - STEEPNESS - 2.0f;
    }
}
