package net.tropicraft.core.common.dimension.chunk;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.noise.generator.RidgedMulti;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.underdasea.SharkEntity;

import java.util.Random;
import java.util.function.Supplier;

public class UnderwaterCoveGenerator {

    private ChunkGenerator<?> generator;

    public int centerX = 0;
    public int centerZ = 0;
    public double length;
    public double width;
    public double height;
    public int y;

    private final static Supplier<BlockState> SEAGRASS = Blocks.SEAGRASS::getDefaultState;
    private final static Supplier<BlockState> KELP = Blocks.KELP_PLANT::getDefaultState;
    private final static Supplier<BlockState> PACKED_PURIFIED_SAND = TropicraftBlocks.PACKED_PURIFIED_SAND.lazyMap(Block::getDefaultState);
    private final static Supplier<BlockState> PURIFIED_SAND = TropicraftBlocks.PURIFIED_SAND.lazyMap(Block::getDefaultState);
    private final static Supplier<BlockState> SPONGE = Blocks.WET_SPONGE::getDefaultState;
    private final static ImmutableList<Supplier<BlockState>> CORALS = ImmutableList.of(
            Blocks.BRAIN_CORAL::getDefaultState,
            Blocks.BUBBLE_CORAL::getDefaultState,
            Blocks.FIRE_CORAL::getDefaultState,
            Blocks.HORN_CORAL::getDefaultState,
            Blocks.TUBE_CORAL::getDefaultState
    );
    private static final Supplier<BlockState> WATER = Blocks.WATER::getDefaultState;

    public boolean pendingBossSpawn = true;

    public UnderwaterCoveGenerator(ChunkGenerator<?> chunkGenerator) {
        this.generator = chunkGenerator;
    }

    public void decorate(final WorldGenRegion world, final int x, final int z) {
        if (!canGenCoveAtCoords(generator, x, z)) {
            return;
        }

        if (pendingBossSpawn && !world.isRemote()) {
            SharkEntity shark = new SharkEntity(TropicraftEntities.HAMMERHEAD.get(), world.getWorld());
            shark.setBoss();
            shark.setLocationAndAngles(centerX, y, centerZ, 0, 0);
            world.addEntity(shark);
            System.out.println("shark boss spawned at "+centerX+" "+y+" "+centerZ);
            pendingBossSpawn = false;
        }
    }

    public IChunk generate(int x, int z, IChunk chunk, SharedSeedRandom random) {
        BlockPos coveCoords = getCoveNear(generator, x, z);

        if (coveCoords != null) {
            centerX = coveCoords.getX();
            y = coveCoords.getY();
            centerZ = coveCoords.getZ();
            x *= 16;
            z *= 16;
        } else {
            pendingBossSpawn = true;
            return chunk;
        }

        Random rand = new Random(generator.getSeed() * centerX + centerZ * 57647382913L);

        RidgedMulti ridged = new RidgedMulti(rand.nextLong(), 1);
        ridged.frequency = 0.0325;

        length = rand.nextInt(20) + 30;
        width = rand.nextInt(20) + 30;
        height = rand.nextInt(3) + 5;

        length *= length;
        width *= width;
        height *= height;

        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                int relativeX = (x + blockX) - centerX;
                int relativeZ = (z + blockZ) - centerZ;
                relativeX *= relativeX;
                relativeZ *= relativeZ;
                for (double j = -height; j < height; j++) {
                    if ((relativeX / length) + ((j * j) / height) + (relativeZ / width) <= 1) {
                        if (j >= -1) {
                            placeBlock(new BlockPos(blockX, y + (int) j + 1, blockZ), PACKED_PURIFIED_SAND, chunk);
                            placeBlock(new BlockPos(blockX, y + (int) j + 2, blockZ), PURIFIED_SAND, chunk);
                        }
                        placeBlock(new BlockPos(blockX, y + (int) j, blockZ), WATER, chunk);
                    }
                }

                double noise1 = ridged.getNoise(x + blockX, z + blockZ);
                double noise2 = ridged.getNoise(x + blockX + 15432, z + blockZ + 42314);
                int j = (int) Math.sqrt(height - ((height * relativeX) / length) - ((height * relativeZ) / width));

                placeBlock(new BlockPos(blockX, y - j - 1, blockZ), PURIFIED_SAND, chunk);
                placeBlock(new BlockPos(blockX, y - j - 2, blockZ), PACKED_PURIFIED_SAND, chunk);

                if (noise1 > 0.875 || noise2 > 0.855) {
                    // placeBlock(i, y - j - 1, k, BlockRegistry.sands.getDefaultState(), primer);

                    double tunnelHeight = (8 - (((relativeX / 2500D) + (relativeZ / 2500D)) * 2)) / 3;

                    if (tunnelHeight < 5) {
                        tunnelHeight = 5;
                    }
                    for (int j2 = 0; j2 < tunnelHeight; j2++) {
                        placeBlock(new BlockPos(blockX, y - j + j2, blockZ), WATER, chunk);
                    }

                    // Decorate with coral
                    if (rand.nextInt(15) == 0) {
                        placeBlock(new BlockPos(blockX, y - j, blockZ), CORALS.get(random.nextInt(CORALS.size())), chunk);
                    }

                    if(rand.nextInt(80) == 0) {
                        placeBlock(new BlockPos(blockX, y - j - 1, blockZ), SPONGE, chunk);
                    }

                    // Decorate with seaweed
                    if (rand.nextInt(45) == 0) {
                        placeBlock(new BlockPos(blockX, y - j - 1, blockZ), KELP, chunk);

                        // Remove any potential blocking object
                        placeBlock(new BlockPos(blockX, y - j, blockZ), WATER, chunk);
                    } else if (rand.nextInt(15) == 0) {
                        placeBlock(new BlockPos(blockX, y - j - 1, blockZ), SEAGRASS, chunk);

                        // Remove any potential blocking object
                        placeBlock(new BlockPos(blockX, y - j, blockZ), WATER, chunk);
                    }

                }
            }
        }

        return chunk;
    }

    public void placeBlock(BlockPos pos, Supplier<BlockState> blockState, IChunk chunk) {
        chunk.setBlockState(pos, blockState.get(), false);
    }

    /**
     * Returns the coordinates of a grove if it should be spawned near this chunk,
     * otherwise returns null. The posY of the returned object should be used as the
     * grove radius
     */
    public BlockPos getCoveNear(ChunkGenerator<?> generator, int i, int j) {
        int range = 4;
        for (int x = i - range; x <= i + range; x++) {
            for (int z = j - range; z <= j + range; z++) {
                Random rand = new Random(generator.getSeed() * x + z * 57647382913L);
                if (canGenCoveAtCoords(generator, x, z)) {
                    return new BlockPos(x * 16 + 8, rand.nextInt(5) + 40, z * 16 + 8);
                }
            }
        }
        return null;
    }

    /**
     * Method to choose spawn locations for volcanos (borrowed from village gen)
     * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers mean
     * more spawning)
     */
    protected boolean canGenCoveAtCoords(ChunkGenerator<?> generator, int i, int j) {
        byte numChunks = 32;
        byte offsetChunks = 0;
        int oldi = i;
        int oldj = j;

        if (i < 0) {
            i -= numChunks - 1;
        }

        if (j < 0) {
            j -= numChunks - 1;
        }

        int randX = i / numChunks;
        int randZ = j / numChunks;
        long seed = (long) randX * 341832132712L + (long) randZ * 422843987541L + generator.getSeed()
                + (long) 42231726;
        Random rand = new Random(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);

        if (oldi == randX && oldj == randZ) {
            return true;
        }

        return false;
    }
}
