package net.tropicraft.core.common.dimension.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.noise.NoiseModule;
import net.tropicraft.core.common.dimension.noise.generator.Billowed;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class VolcanoGenerator {

	public static List<ResourceLocation> volcanoSpawnBiomesLand = Arrays.asList(
			TropicraftBiomes.TROPICS.getId(), TropicraftBiomes.RAINFOREST_PLAINS.getId()
	);
	public static List<ResourceLocation> volcanoSpawnBiomesOcean = Arrays.asList(
			TropicraftBiomes.TROPICS_OCEAN.getId()
	);

	private ChunkGenerator<?> generator;

	private final static int MAX_RADIUS = 65;
	private final static int MIN_RADIUS = 45;
	private final static int CALDERA_CUTOFF = 194; //The Y level where if the height of the volcano would pass becomes the caldera
	public final static int VOLCANO_TOP = CALDERA_CUTOFF - 7; //The Y level cut off of the sides of the volcano
	public final static int VOLCANO_CRUST = VOLCANO_TOP - 3; //The Y level where the crust of the volcano generates
	public final static int LAVA_LEVEL = 149; //The Y level where the top of the lava column is
	private final static int CRUST_HOLE_CHANCE = 15; //1 / x chance a certain block of the crust will be missing
	private final static int OCEAN_HEIGHT_OFFSET = -50;

	public final static int SURFACE_BIOME = 1;
	public final static int OCEAN_BIOME = 2;

	public final static int CHUNK_SIZE_X = 16;
	public final static int CHUNK_SIZE_Z = 16;
	public final static int CHUNK_SIZE_Y = 256;

	private final static Supplier<BlockState> VOLCANO_BLOCK = TropicraftBlocks.CHUNK.lazyMap(Block::getDefaultState);
	private final static Supplier<BlockState> LAVA_BLOCK = () -> Blocks.LAVA.getDefaultState();
	private final static Supplier<BlockState> SAND_BLOCK = TropicraftBlocks.VOLCANIC_SAND.lazyMap(Block::getDefaultState);

	public VolcanoGenerator(ChunkGenerator<?> chunkGenerator) {
		this.generator = chunkGenerator;
	}

	public IChunk generate(int i, int k, IChunk chunk, SharedSeedRandom random) {
		BlockPos volcanoCoords = VolcanoGenerator.getVolcanoNear(this.generator, i, k);

		if (volcanoCoords == null) {
			return chunk;
		}

		int HEIGHT_OFFSET = VolcanoGenerator.getHeightOffsetForBiome(volcanoCoords.getY());
		int calderaCutoff = CALDERA_CUTOFF + HEIGHT_OFFSET;
		int lavaLevel = LAVA_LEVEL + HEIGHT_OFFSET;
		int volcanoTop = VOLCANO_TOP + HEIGHT_OFFSET;
		int volcanoCrust = VOLCANO_CRUST + HEIGHT_OFFSET;

		i *= CHUNK_SIZE_X;
		k *= CHUNK_SIZE_Z;

		int volcCenterX = volcanoCoords.getX();
		int volcCenterZ = volcanoCoords.getZ();

		long seed = getPositionSeed(volcCenterX, volcCenterZ);
		Random rand = new Random(seed);

		int radiusX = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
		int radiusZ = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;

		NoiseModule volcNoise = getNoise(seed);

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int x = 0; x < CHUNK_SIZE_X; x++) {
			for (int z = 0; z < CHUNK_SIZE_Z; z++) {
			    
		        int relativeX = ((x + i) - volcCenterX);
		        int relativeZ = ((z + k) - volcCenterZ);
		        
			    double volcanoHeight = getVolcanoHeight(relativeX, relativeZ, radiusX, radiusZ, volcNoise);
			    float distanceSquared = getDistanceSq(relativeX, relativeZ, radiusX, radiusZ);

                int groundHeight = chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);
                groundHeight = Math.min(groundHeight, lavaLevel - 3);

				if (distanceSquared < 1) {
					for (int y = CHUNK_SIZE_Y; y > 0; y--) {
						pos.setPos(x, y, z);

						if (volcanoHeight + groundHeight < calderaCutoff) {
							if (volcanoHeight + groundHeight <= volcanoTop) {
								if (y <= volcanoHeight + groundHeight) {
									if (y > groundHeight) {
										this.placeBlock(pos, VOLCANO_BLOCK, chunk);
									} else if (y > groundHeight - 2) {
										this.placeBlock(pos, SAND_BLOCK, chunk);
									}
								}
							} else if (y == volcanoCrust - 1) {
								if (random.nextInt(3) != 0) {
									this.placeBlock(pos, VOLCANO_BLOCK, chunk);
								}
							} else if (y <= volcanoTop) {
								placeBlock(pos, VOLCANO_BLOCK, chunk);
							}
						} else {
							// Flat area on top of the volcano
							if (y == volcanoCrust  && rand.nextInt(CRUST_HOLE_CHANCE) != 0) {
								placeBlock(pos, VOLCANO_BLOCK, chunk);
							} else if (y <= lavaLevel) {
								placeBlock(pos, LAVA_BLOCK, chunk);
							} else {
								placeBlock(pos, Blocks.AIR::getDefaultState, chunk);
							}
						}
					}
				}
			}
		}

		return chunk;
	}
	
	private long getPositionSeed(int volcCenterX, int volcCenterZ) {
        return (long)volcCenterX * 341873128712L + (long)volcCenterZ * 132897987541L + generator.getSeed() + (long)4291726;
	}
	
	private NoiseModule getNoise(long seed) {
        NoiseModule volcNoise = new Billowed(seed, 1, 1);
        volcNoise.amplitude = 0.45;
        return volcNoise;
	}
	
    /**
     * Get the height of volcano generation at the given x/z coordinates, without needing
     * to generate any blocks.
     * 
     * @param groundHeight The current known heightmap level at the given coordinates.
     * @param x Block x position
     * @param z Block z position
     * 
     * @return The height value of the volcano generating near the given coordinates, at the
     * specified x and z. If there is no nearby volcano, returns -1.
     * 
     * @apiNote This only does a somewhat rough calculation -- it ignores the caldera "edge"
     * and treats volcanoes as completely flat at the top (ignoring random holes).
     * <p>
     * The latter is actually beneficial to the main use, village gen, because otherwise
     * village pieces may generate directly on top of a hole (and thus inside the volcano).
     * <p>
     * It also duplicates a significant amount of logic from {@link #generate(int, int, IChunk, SharedSeedRandom)},
     * but I have yet to find a nice way to deduplicate that logic. This whole class could use
     * a rewrite at some point with these goals in mind.
     * <p>
     */
	// TODO Fix the above issues
	public int getVolcanoHeight(int groundHeight, int x, int z) {
	    BlockPos volcanoCoords = getVolcanoNear(this.generator, x >> 4, z >> 4);
	    if (volcanoCoords == null) {
	        return -1;
	    }

        int volcCenterX = volcanoCoords.getX();
        int volcCenterZ = volcanoCoords.getZ();

        long seed = getPositionSeed(volcCenterX, volcCenterZ);
        Random rand = new Random(seed);

        int radiusX = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
        int radiusZ = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
        
        NoiseModule volcNoise = getNoise(seed);
        
        int relativeX = x - volcCenterX;
        int relativeZ = z - volcCenterZ;
        
        double ret = getVolcanoHeight(relativeX, relativeZ, radiusX, radiusZ, volcNoise);
        
        int heightOffset = getHeightOffsetForBiome(volcanoCoords.getY());
        int lavaLevel = LAVA_LEVEL + heightOffset;
        int volcanoCrust = VOLCANO_CRUST + heightOffset;
        groundHeight = Math.min(groundHeight, lavaLevel - 3);
        
        return Math.min(volcanoCrust + 1, MathHelper.ceil(ret + groundHeight));
	}
	
	private float getDistanceSq(float relativeX, float relativeZ, float radiusX, float radiusZ) {
        return ((relativeX / radiusX) * (relativeX / radiusX) + (relativeZ / radiusZ) * (relativeZ / radiusZ));
	}
	
	private double getVolcanoHeight(float relativeX, float relativeZ, float radiusX, float radiusZ, NoiseModule volcNoise) {
        float distanceSquared = getDistanceSq(relativeX, relativeZ, radiusX, radiusZ);

        //float perlin = (float)volcNoise.getNoise(relativeX * 0.05 + 0.0001, relativeZ * 0.05 + 0.0001) + 1;
        float perlin = (float)volcNoise.getNoise(relativeX * 0.21 + 0.01, relativeZ * 0.21 + 0.01) + 1;

        //double volcanoHeight = steepnessMod / (distanceSquared) * perlin - steepnessMod - 2;
        double steepness = 10.2;
        return steepness / distanceSquared * perlin - steepness - 2;
	}

	public void placeBlock(BlockPos pos, Supplier<BlockState> blockState, IChunk chunk) {
		chunk.setBlockState(pos, blockState.get(), false);
	}

	public BlockState getBlockState(BlockPos pos, IChunk chunk) {
		return chunk.getBlockState(pos);
	}

	/**
	 * Method to choose spawn locations for volcanos (borrowed from village gen)
	 * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers
	 * mean more spawning)
	 */
	public static int canGenVolcanoAtCoords(ChunkGenerator<?> generator, int i, int j) {
		byte numChunks = 64; // was 32
		byte offsetChunks = 16; // was 8
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
		long seed = (long)randX * 341873128712L + (long)randZ * 132897987541L + generator.getSeed() + (long)4291726;
		Random rand = new Random(seed);
		randX *= numChunks;
		randZ *= numChunks;
		randX += rand.nextInt(numChunks - offsetChunks);
		randZ += rand.nextInt(numChunks - offsetChunks);

		if (oldi == randX && oldj == randZ) {
			if(hasAllBiomes(generator, oldi * 16 + 8, 0,oldj * 16 + 8, volcanoSpawnBiomesLand)) {
				return SURFACE_BIOME;
			}
			if(hasAllBiomes(generator, oldi * 16 + 8, 0,oldj * 16 + 8, volcanoSpawnBiomesOcean)) {
				return OCEAN_BIOME;
			}

			return SURFACE_BIOME;
		}

		return 0;
	}

	/**
	 * Returns the coordinates of a volcano if it should be spawned near
	 * this chunk, otherwise returns null.
	 * The posY of the returned object should be used as the volcano radius
	 */
	public static BlockPos getVolcanoNear(ChunkGenerator<?> generator, int i, int j) {
		//Check 4 chunks in each direction (volcanoes are never more than 4 chunks wide)
		int range = 4;
		for (int x = i - range; x <= i + range; x++) {
			for (int z = j - range; z <= j + range; z++) {
				int biome = canGenVolcanoAtCoords(generator, x, z);
				if (biome != 0) {
					BlockPos pos = new BlockPos(x * 16 + 8, biome, z * 16 + 8);

					return pos;
				}
			}
		}

		return null;
	}

	public static int getHeightOffsetForBiome(int biome) {
		return biome == SURFACE_BIOME ? 0: OCEAN_HEIGHT_OFFSET;
	}

	private static boolean hasAllBiomes(ChunkGenerator<?> generator, int centerX, int centerY, int centerZ, List<ResourceLocation> allowedBiomes) {
		BiomeProvider biomeProvider = generator.getBiomeProvider();
		for (Biome biome : biomeProvider.getBiomes(centerX, centerY, centerZ,0)) {
			if (!allowedBiomes.contains(biome.getRegistryName())) {
				return false;
			}
		}

		return true;
	}

}