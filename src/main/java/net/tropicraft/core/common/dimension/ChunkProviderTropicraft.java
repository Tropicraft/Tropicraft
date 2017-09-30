package net.tropicraft.core.common.dimension;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.worldgen.mapgen.MapGenVolcano;
import net.tropicraft.core.registry.BlockRegistry;

public class ChunkProviderTropicraft implements IChunkGenerator { //NOTE: THIS WILL MOST LIKELY BE COMPLETELY REDONE

	private static final int CHUNK_SIZE_Y = 256;

	private static final int HOME_TREE_RARITY = 350;

	private World worldObj;
	private long seed;
	protected Random rand;
	private Biome[] biomesForGeneration;
	private double[] depthBuffer = new double[256];
	/** Used in initializeNoiseField */
	private float[] parabolicField;

	private NoiseGeneratorPerlin surfaceNoise;

	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	private NoiseGeneratorOctaves noiseGen5;

	private MapGenVolcano volcanoGen;

	public ChunkProviderTropicraft(World world, long seed, boolean mapFeaturesEnabled) {
		this.worldObj = world;
		this.rand = new Random(seed);
		this.seed = seed;

		this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 16);

		volcanoGen = new MapGenVolcano(worldObj, true);
	}
	
	private boolean hasSpawned = false;

	@Override
	public void populate(int x, int z) {
		BlockFalling.fallInstantly = true;
		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		BiomeGenTropicraft biome = (BiomeGenTropicraft) this.worldObj.getBiome(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.worldObj.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)x * k + (long)z * l ^ this.worldObj.getSeed());
		boolean flag = false;
		ChunkPos chunkpos = new ChunkPos(i, j);

		biome.decorate(worldObj, rand, blockpos);

		if (!hasSpawned) {
			BlockPos volcanoCoords = volcanoGen.getVolcanoNear(worldObj, x, z);
			if (volcanoCoords != null) {
				BlockPos posVolcanoTE = new BlockPos(volcanoCoords.getX(), 1, volcanoCoords.getZ());
				if (worldObj.getBlockState(posVolcanoTE).getBlock() != BlockRegistry.volcano) {
					worldObj.setBlockState(posVolcanoTE, BlockRegistry.volcano.getDefaultState());
					hasSpawned = true;
				}
			}
		}

		// generateOres(x,z);

		// SpawnerAnimals.performWorldGenSpawning(worldObj, biome, x + 8, z + 8, 16, 16, rand);

		BlockSand.fallInstantly = false;
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(
			EnumCreatureType creatureType, BlockPos pos) {
		return null;
	}

	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName,
			BlockPos position) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

	@Override
	public Chunk provideChunk(int x, int z) {
		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, chunkprimer);
		this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);

		this.volcanoGen.generate(x, z, chunkprimer);

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i)
		{
			abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn) {
		if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.worldObj)) return;
		double d0 = 0.03125D;
		this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

		int a = -1;

		boolean flag = false;

		int k = (int)63;
		double d = 0.03125D;
		for (int xValue = 0; xValue < 16; ++xValue)
		{
			for (int zValue = 0; zValue < 16; ++zValue)
			{
				//				Biome biome = biomesIn[j + i * 16];
				//				biome.genTerrainBlocks(this.worldObj, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);

				BiomeGenTropicraft biome = (BiomeGenTropicraft)biomesIn[zValue + xValue * 16];
				Block top = biome.topBlock.getBlock();
				Block filler = biome.fillerBlock.getBlock();

				Block btop = Blocks.SAND.getDefaultState().getBlock();
				Block bfiller = btop;

				// for colored sand
				if (biome == BiomeGenTropicraft.tropicsOcean) {
					btop = biome.sandBlock;
				}

				for(int yValue = 128 - 1; yValue >= 0; yValue--) {
					int xx = xValue;
					int zz = zValue;
					Block block = primer.getBlockState(xx, yValue, zz).getBlock();

					if(yValue <= 0) {
						primer.setBlockState(xx, yValue, zz, Blocks.BEDROCK.getDefaultState());
						continue;
					}

					if(block == Blocks.AIR || block == BlockRegistry.tropicsWater)
					{
						a = 0;
						continue;
					}

					if(a >= 0 && a < 5)
					{
						Block blockUsed = Blocks.STONE;
						if(a == 0 && yValue < 63 + 3)
						{
							flag = true;
						}

						if(flag)
						{
							if(a < 5) {
								blockUsed = btop;
							}
						}
						else
						{
							if(top != Blocks.SAND)
							{
								if(a == 0)
								{
									blockUsed = top;
								}
								else if(a < 5)
								{
									blockUsed = filler;
								}
							}
						}
						primer.setBlockState(xx, yValue, zz, blockUsed.getDefaultState());
						a++;
						continue;
					}

					flag = false;
					a = -1;

				}

				a = -1;

			}
		}
	}

	private void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
		byte chunkSizeGenXZ = 4;
		byte chunkSizeGenY = 16;
		byte midHeight = 63;
		int k_size = chunkSizeGenXZ + 1;
		byte b3 = 17;
		int l_size = chunkSizeGenXZ + 1;
		this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, k_size + 5, l_size + 5);
		double[] noiseArray = null;
		noiseArray = this.initializeNoiseField(noiseArray, x * chunkSizeGenXZ, 0, z * chunkSizeGenXZ, k_size, b3, l_size);

		for (int genX = 0; genX < chunkSizeGenXZ; ++genX)
		{

			for (int genY = 0; genY < chunkSizeGenXZ; ++genY)
			{

				for (int genZ = 0; genZ < chunkSizeGenY; ++genZ)
				{
					double d0 = 0.125D;
					double d1 = noiseArray[((genX + 0) * l_size + genY + 0) * b3 + genZ + 0];
					double d2 = noiseArray[((genX + 0) * l_size + genY + 1) * b3 + genZ + 0];
					double d3 = noiseArray[((genX + 1) * l_size + genY + 0) * b3 + genZ + 0];
					double d4 = noiseArray[((genX + 1) * l_size + genY + 1) * b3 + genZ + 0];
					double d5 = (noiseArray[((genX + 0) * l_size + genY + 0) * b3 + genZ + 1] - d1) * d0;
					double d6 = (noiseArray[((genX + 0) * l_size + genY + 1) * b3 + genZ + 1] - d2) * d0;
					double d7 = (noiseArray[((genX + 1) * l_size + genY + 0) * b3 + genZ + 1] - d3) * d0;
					double d8 = (noiseArray[((genX + 1) * l_size + genY + 1) * b3 + genZ + 1] - d4) * d0;

					for (int j2 = 0; j2 < 8; ++j2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int k2 = 0; k2 < 4; ++k2)
						{
							double d14 = 0.25D;
							double d15 = (d11 - d10) * d14;
							double d16 = d10 - d15;

							for (int l2 = 0; l2 < 4; ++l2)
							{
								if ((d16 += d15) > 0.0D)
								{
									primer.setBlockState(genX * 4 + k2, genZ * 8 + j2, genY * 4 + l2, Blocks.STONE.getDefaultState());
								}
								else if (genZ * 8 + j2 < 64)
								{
									primer.setBlockState(genX * 4 + k2, genZ * 8 + j2, genY * 4 + l2, BlockRegistry.tropicsWater.getDefaultState());
								}
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	private double[] initializeNoiseField(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7)
	{
		if (par1ArrayOfDouble == null)
		{
			par1ArrayOfDouble = new double[par5 * par6 * par7];
		}

		if (this.parabolicField == null)
		{
			this.parabolicField = new float[25];

			for (int k1 = -2; k1 <= 2; ++k1)
			{
				for (int l1 = -2; l1 <= 2; ++l1)
				{
					float f = 10.0F / MathHelper.sqrt((float)(k1 * k1 + l1 * l1) + 0.2F);
					this.parabolicField[k1 + 2 + (l1 + 2) * 5] = f;
				}
			}
		}

		double d0 = 684.412D;
		double d1 = 684.412D;
		double[] noise1 = null;
		double[] noise2 = null;
		double[] noise3 = null;
		double[] noise4 = null;
		double[] noise5 = null;
		noise1 = this.noiseGen1.generateNoiseOctaves(noise1, par2, par3, par4, par5, par6, par7, d0, d1, d0);
		noise2 = this.noiseGen2.generateNoiseOctaves(noise2, par2, par3, par4, par5, par6, par7, d0, d1, d0);
		noise3 = this.noiseGen3.generateNoiseOctaves(noise3, par2, par3, par4, par5, par6, par7, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
		noise4 = this.noiseGen4.generateNoiseOctaves(noise4, par2, par4, par5, par7, 1.121D, 1.121D, 0.5D);
		noise5 = this.noiseGen5.generateNoiseOctaves(noise5, par2, par4, par5, par7, 200.0D, 200.0D, 0.5D);
		int i2 = 0;
		int j2 = 0;

		for (int k2 = 0; k2 < par5; ++k2)
		{
			for (int l2 = 0; l2 < par7; ++l2)
			{
				float f1 = 0.0F;
				float f2 = 0.0F;
				float f3 = 0.0F;
				byte b0 = 2;
				Biome biome = this.biomesForGeneration[k2 + 2 + (l2 + 2) * (par5 + 5)];

				for (int i3 = -b0; i3 <= b0; ++i3)
				{
					for (int j3 = -b0; j3 <= b0; ++j3)
					{
						Biome biome1 = this.biomesForGeneration[k2 + i3 + 2 + (l2 + j3 + 2) * (par5 + 5)];
						float f4 = this.parabolicField[i3 + 2 + (j3 + 2) * 5] / (biome1.getBaseHeight() + 2.0F);

						if (biome1.getBaseHeight() > biome.getBaseHeight())
						{
							f4 /= 2.0F;
						}

						f1 += biome1.getHeightVariation() * f4;
						f2 += biome1.getBaseHeight() * f4;
						f3 += f4;
					}
				}

				f1 /= f3;
				f2 /= f3;
				f1 = f1 * 0.9F + 0.1F;
				f2 = (f2 * 4.0F - 1.0F) / 8.0F;
				double d2 = noise5[j2] / 8000.0D;

				if (d2 < 0.0D)
				{
					d2 = -d2 * 0.3D;
				}

				d2 = d2 * 3.0D - 2.0D;

				if (d2 < 0.0D)
				{
					d2 /= 2.0D;

					if (d2 < -1.0D)
					{
						d2 = -1.0D;
					}

					d2 /= 1.4D;
					d2 /= 2.0D;
				}
				else
				{
					if (d2 > 1.0D)
					{
						d2 = 1.0D;
					}

					d2 /= 8.0D;
				}

				++j2;

				for (int k3 = 0; k3 < par6; ++k3)
				{
					double d3 = (double)f2;
					double d4 = (double)f1;
					d3 += d2 * 0.2D;
					d3 = d3 * (double)par6 / 16.0D;
					double d5 = (double)par6 / 2.0D + d3 * 4.0D;
					double d6 = 0.0D;
					double d7 = ((double)k3 - d5) * 12.0D * 128.0D / 128.0D / d4;

					if (d7 < 0.0D)
					{
						d7 *= 4.0D;
					}

					double d8 = noise1[i2] / 512.0D;
					double d9 = noise2[i2] / 512.0D;
					double d10 = (noise3[i2] / 10.0D + 1.0D) / 2.0D;

					if (d10 < 0.0D)
					{
						d6 = d8;
					}
					else if (d10 > 1.0D)
					{
						d6 = d9;
					}
					else
					{
						d6 = d8 + (d9 - d8) * d10;
					}

					d6 -= d7;

					if (k3 > par6 - 4)
					{
						double d11 = (double)((float)(k3 - (par6 - 4)) / 3.0F);
						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					par1ArrayOfDouble[i2] = d6;
					++i2;
				}
			}
		}

		return par1ArrayOfDouble;
	}
}
