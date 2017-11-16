package net.tropicraft.core.common.worldgen.mapgen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.biome.BiomeTropicraft;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.common.worldgen.perlin.NoiseModule;
import net.tropicraft.core.common.worldgen.perlin.generator.Billowed;
import net.tropicraft.core.registry.BlockRegistry;

public class MapGenVolcano {

	protected HashMap coordMap = new HashMap();

	public static List<Biome> volcanoSpawnBiomesLand = Arrays.asList(new Biome[] {
			BiomeTropicraft.tropics, BiomeTropicraft.rainforestPlains
	});
	public static List<Biome> volcanoSpawnBiomesOcean = Arrays.asList(new Biome[] {
			BiomeTropicraft.tropicsOcean
	});

	private World worldObj;

	private boolean useArrays;

	private final static int CHUNK_SIZE_X = 16;
	private final static int CHUNK_SIZE_Z = 16;
	private final static int CHUNK_SIZE_Y = 256;
	private final static int MAX_RADIUS = 65;
	private final static int MIN_RADIUS = 45;
	private final static int LAND_STEEPNESS_MOD = 2; //usually 4
	private final static int OCEAN_STEEPNESS_MOD = 8;
	private final static int CALDERA_CUTOFF = 124; //The Y level where if the height of the volcano would pass becomes the caldera
	public final static int VOLCANO_TOP = CALDERA_CUTOFF - 7; //The Y level cut off of the sides of the volcano
	public final static int VOLCANO_CRUST = VOLCANO_TOP - 3; //The Y level where the crust of the volcano generates
	public final static int LAVA_LEVEL = 79; //The Y level where the top of the lava column is
	private final static int CRUST_HOLE_CHANCE = 15; //1 / x chance a certain block of the crust will be missing

	private final static IBlockState VOLCANO_BLOCK = BlockRegistry.chunk.getDefaultState();
	private final static IBlockState LAVA_BLOCK = Blocks.LAVA.getDefaultState();
	private final static IBlockState SAND_BLOCK = BlockRegistry.sands.getDefaultState().withProperty(BlockTropicraftSands.VARIANT, TropicraftSands.VOLCANIC);

	public MapGenVolcano(World worldObj, boolean useArrays) {
		this.worldObj = worldObj;
		this.useArrays = useArrays;
	}

	public ChunkPrimer generate(int i, int k, ChunkPrimer primer) {
		BlockPos volcanoCoords = getVolcanoNear(worldObj, i, k);

		if (volcanoCoords == null) {
			return primer;
		}

		int[] heightmap = new int[256];

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 127; y++) {
					Block blockID = primer.getBlockState(x,  y,  z).getBlock();
					if (blockID == Blocks.AIR || blockID == BlockRegistry.tropicsWater) {
						heightmap[x * 16 + z] = y;
						break;
					}
					if(y > 75) { 
						heightmap[x * 16 + z] = y;
						break;
					}
				}
			}
		}

		i *= CHUNK_SIZE_X;
		k *= CHUNK_SIZE_Z;

		int volcCenterX = volcanoCoords.getX();
		int volcCenterZ = volcanoCoords.getZ();
		int steepnessMod = volcanoCoords.getY() == 1 ? LAND_STEEPNESS_MOD : OCEAN_STEEPNESS_MOD;

		long seed = (long)volcCenterX * 341873128712L + (long)volcCenterZ * 132897987541L + worldObj.getWorldInfo().getSeed() + (long)4291726;
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
						if (volcanoHeight + groundHeight < CALDERA_CUTOFF) {
							if (volcanoHeight + groundHeight <= VOLCANO_TOP) {
								if (y <= volcanoHeight + groundHeight) {
								    if (y >= groundHeight) {
                                        placeBlock(x, y, z, VOLCANO_BLOCK, primer);
                                    } else if (primer.getBlockState(x, y, z).getBlock() == BlockRegistry.sands) {
                                        placeBlock(x, y, z, SAND_BLOCK, primer);
                                        if (primer.getBlockState(x, y + 1, z).getBlock() == BlockRegistry.sands) {
                                            placeBlock(x, y + 1, z, SAND_BLOCK, primer);
                                        }
								    }
								}
							} else if (y == VOLCANO_CRUST - 1) {
								if (worldObj.rand.nextInt(3) != 0) {
									placeBlock(x, y, z, VOLCANO_BLOCK, primer);
								}
							} else if(y <= VOLCANO_TOP) {
								placeBlock(x, y, z, VOLCANO_BLOCK, primer);
							}
						} else {
							// Flat area on top of the volcano
							if (y == VOLCANO_CRUST  && rand.nextInt(CRUST_HOLE_CHANCE) != 0) {
								placeBlock(x, y, z, VOLCANO_BLOCK, primer);
							}

							if (y <= LAVA_LEVEL) {
								placeBlock(x, y, z, LAVA_BLOCK, primer);
							}
						}
					}
				}
			}
		}

		return primer;
	}

	public void placeBlock(int x, int y, int z, IBlockState blockState, ChunkPrimer primer) {
		primer.setBlockState(x, y, z, blockState);
	}

	public IBlockState getBlockState(int x, int y, int z, ChunkPrimer primer) {
		return primer.getBlockState(x, y, z);
	}

	/**
	 * Method to choose spawn locations for volcanos (borrowed from village gen)
	 * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers
	 * mean more spawning)
	 */
	protected int canGenVolcanoAtCoords(World worldObj, int i, int j) {
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
		long seed = (long)randX * 341873128712L + (long)randZ * 132897987541L + worldObj.getWorldInfo().getSeed() + (long)4291726;
		Random rand = new Random(seed);
		randX *= numChunks;
		randZ *= numChunks;
		randX += rand.nextInt(numChunks - offsetChunks);
		randZ += rand.nextInt(numChunks - offsetChunks);

		if (oldi == randX && oldj == randZ) {
			if(worldObj.getBiomeProvider().areBiomesViable(oldi * 16 + 8, oldj * 16 + 8, 0, volcanoSpawnBiomesLand)) {
				return 1;
			}
			if(worldObj.getBiomeProvider().areBiomesViable(oldi * 16 + 8, oldj * 16 + 8, 0, volcanoSpawnBiomesOcean)) {
				return 2;
			}
		}

		return 0;
	}

	/**
	 * Returns the coordinates of a volcano if it should be spawned near
	 * this chunk, otherwise returns null.
	 * The posY of the returned object should be used as the volcano radius
	 */
	public BlockPos getVolcanoNear(World worldObj, int i, int j) {
		//Check 4 chunks in each direction (volcanoes are never more than 4 chunks wide)
		int range = 4;
		for (int x = i - range; x <= i + range; x++) {
			for (int z = j - range; z <= j + range; z++) {
				int biome = canGenVolcanoAtCoords(worldObj, x, z);
				if (biome != 0) {
					return new BlockPos(x * 16 + 8, biome, z * 16 + 8);
				}
			}
		}

		return null;
	}

}