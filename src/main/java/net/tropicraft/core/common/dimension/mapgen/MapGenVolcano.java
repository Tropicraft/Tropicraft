package net.tropicraft.core.common.dimension.mapgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.noise.NoiseModule;
import net.tropicraft.core.common.dimension.noise.generator.Billowed;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MapGenVolcano {

	protected HashMap coordMap = new HashMap();

	public static List<Biome> volcanoSpawnBiomesLand = Arrays.asList(new Biome[] {
			TropicraftBiomes.TROPICS, TropicraftBiomes.RAINFOREST_PLAINS
	});
	public static List<Biome> volcanoSpawnBiomesOcean = Arrays.asList(new Biome[] {
			TropicraftBiomes.TROPICS_OCEAN
	});

	private IWorld world;

	private boolean useArrays;

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

	private final static BlockState VOLCANO_BLOCK = TropicraftBlocks.CHUNK.getDefaultState();
	private final static BlockState LAVA_BLOCK = Blocks.LAVA.getDefaultState();
	private final static BlockState SAND_BLOCK = TropicraftBlocks.VOLCANIC_SAND.getDefaultState();

	public MapGenVolcano(IWorld worldObj, boolean useArrays) {
		this.world = worldObj;
		this.useArrays = useArrays;
	}

	public ChunkPrimer generate(int i, int k, ChunkPrimer primer) {
		BlockPos volcanoCoords = MapGenVolcano.getVolcanoNear(this.world, i, k);

		if (volcanoCoords == null) {
			return primer;
		}

		int HEIGHT_OFFSET = MapGenVolcano.getHeightOffsetForBiome(volcanoCoords.getY());
		int calderaCutoff = CALDERA_CUTOFF + HEIGHT_OFFSET;
		int lavaLevel = LAVA_LEVEL + HEIGHT_OFFSET;
		int volcanoTop = VOLCANO_TOP + HEIGHT_OFFSET;
		int volcanoCrust = VOLCANO_CRUST + HEIGHT_OFFSET;

		int[] heightmap = new int[256];
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < calderaCutoff; y++)
				{
					pos.setPos(x, y, z);
					Block blockID = primer.getBlockState(pos).getBlock();
					if (blockID == Blocks.AIR || blockID == Blocks.WATER) {
						heightmap[pos.getX() * 16 + pos.getZ()] = pos.getY();
						break;
					}
					if(pos.getY() > lavaLevel - 4) {
						heightmap[pos.getX() * 16 + pos.getZ()] = pos.getY();
						break;
					}
				}
			}
		}

		i *= CHUNK_SIZE_X;
		k *= CHUNK_SIZE_Z;

		int volcCenterX = volcanoCoords.getX();
		int volcCenterZ = volcanoCoords.getZ();

		long seed = (long)volcCenterX * 341873128712L + (long)volcCenterZ * 132897987541L + world.getWorldInfo().getSeed() + (long)4291726;
		Random rand = new Random(seed);

		int radiusX = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
		int radiusZ = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;

		NoiseModule volcNoise = new Billowed(seed, 1, 1);
		volcNoise.amplitude = 0.45;

		for (int x = 0; x < CHUNK_SIZE_X; x++) {
			for (int z = 0; z < CHUNK_SIZE_Z; z++) {
				float relativeX = ((x + i) - volcCenterX);
				float relativeZ = ((z + k) - volcCenterZ);

				float distanceSquared = ((relativeX / radiusX) * (relativeX / radiusX) + (relativeZ / radiusZ) * (relativeZ / radiusZ));

				//float perlin = (float)volcNoise.getNoise(relativeX * 0.05 + 0.0001, relativeZ * 0.05 + 0.0001) + 1;
				float perlin = (float)volcNoise.getNoise(relativeX * 0.21 + 0.01, relativeZ * 0.21 + 0.01) + 1;

				//double volcanoHeight = steepnessMod / (distanceSquared) * perlin - steepnessMod - 2;
				double steepness = 10.2;
				double volcanoHeight = steepness / distanceSquared * perlin - steepness - 2;

				int groundHeight = heightmap[x * 16 + z];

				if (distanceSquared < 1) {
					for (int y = CHUNK_SIZE_Y; y > 0; y--) {
						pos.setPos(x, y, z);

						if (volcanoHeight + groundHeight < calderaCutoff) {
							if (volcanoHeight + groundHeight <= volcanoTop) {
								if (y <= volcanoHeight + groundHeight) {
									if (y >= groundHeight) {
										this.placeBlock(pos, VOLCANO_BLOCK, primer);
										// TODO: Change to check against only tropicraft sands
									} else if (primer.getBlockState(pos).getMaterial() == Material.SAND) {
										this.placeBlock(pos, SAND_BLOCK, primer);
										// TODO: Change to check against only tropicraft sands
										if (primer.getBlockState(pos.setPos(x, y + 1, z)).getMaterial() == Material.SAND) {
											this.placeBlock(pos, SAND_BLOCK, primer);
										}
									}
								}
							} else if (y == volcanoCrust - 1) {
								if (this.world.getRandom().nextInt(3) != 0) {
									this.placeBlock(pos, VOLCANO_BLOCK, primer);
								}
							} else if(y <= volcanoTop) {
								this.placeBlock(pos, VOLCANO_BLOCK, primer);
							}
						} else {
							// Flat area on top of the volcano
							if (y == volcanoCrust  && rand.nextInt(CRUST_HOLE_CHANCE) != 0) {
								this.placeBlock(pos, VOLCANO_BLOCK, primer);
							} else if (y <= lavaLevel) {
								this.placeBlock(pos, LAVA_BLOCK, primer);
							} else {
								this.placeBlock(pos, Blocks.AIR.getDefaultState(), primer);
							}
						}
					}
				}
			}
		}

		return primer;
	}

	public void placeBlock(BlockPos pos, BlockState blockState, ChunkPrimer primer) {
		primer.setBlockState(pos, blockState, false);
	}

	public BlockState getBlockState(BlockPos pos, ChunkPrimer primer) {
		return primer.getBlockState(pos);
	}

	/**
	 * Method to choose spawn locations for volcanos (borrowed from village gen)
	 * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers
	 * mean more spawning)
	 */
	protected static int canGenVolcanoAtCoords(IWorld world, int i, int j) {
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
		long seed = (long)randX * 341873128712L + (long)randZ * 132897987541L + world.getWorldInfo().getSeed() + (long)4291726;
		Random rand = new Random(seed);
		randX *= numChunks;
		randZ *= numChunks;
		randX += rand.nextInt(numChunks - offsetChunks);
		randZ += rand.nextInt(numChunks - offsetChunks);

		if (oldi == randX && oldj == randZ) {
			if(hasAllBiomes(world, oldi * 16 + 8, oldj * 16 + 8, volcanoSpawnBiomesLand)) {
				return SURFACE_BIOME;
			}
			if(hasAllBiomes(world, oldi * 16 + 8, oldj * 16 + 8, volcanoSpawnBiomesOcean)) {
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
	public static BlockPos getVolcanoNear(IWorld world, int i, int j) {
		//Check 4 chunks in each direction (volcanoes are never more than 4 chunks wide)
		int range = 4;
		for (int x = i - range; x <= i + range; x++) {
			for (int z = j - range; z <= j + range; z++) {
				int biome = canGenVolcanoAtCoords(world, x, z);
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

	private static boolean hasAllBiomes(IWorld world, int centerX, int centerY, List<Biome> allowedBiomes) {
		BiomeProvider biomeProvider = world.getChunkProvider().getChunkGenerator().getBiomeProvider();
		for (Biome biome : biomeProvider.getBiomesInSquare(centerX, centerY, 0)) {
			if (!allowedBiomes.contains(biome)) {
				return false;
			}
		}

		return true;
	}

}