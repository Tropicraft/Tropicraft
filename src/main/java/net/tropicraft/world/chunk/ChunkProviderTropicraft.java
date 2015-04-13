package net.tropicraft.world.chunk;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.mapgen.MapGenTropicsCaves;
import net.tropicraft.world.mapgen.MapGenUndergroundGrove;
import net.tropicraft.world.mapgen.MapGenVolcano;

public class ChunkProviderTropicraft implements IChunkProvider { //NOTE: THIS WILL MOST LIKELY BE COMPLETELY REDONE

	private static final int CHUNK_SIZE_Y = 256;

	private static final int HOME_TREE_RARITY = 350;

	private World worldObj;
	private long seed;
	protected Random rand;
	private BiomeGenBase[] biomesForGeneration;
	private MapGenUndergroundGrove groveGen;
	private MapGenTropicsCaves caveGenerator;
	//private MapGenKoaVillage villageGenerator; TODO
	private MapGenVolcano volcanoGen;

	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	private NoiseGeneratorOctaves noiseGen5;

	private WorldGenerator eudialyteGen;
	private WorldGenerator zirconGen;
	private WorldGenerator azuriteGen;
	private WorldGenerator ironGen;
	private WorldGenerator coalGen;
	private WorldGenerator lapisGen;

	public ChunkProviderTropicraft(World worldObj, long seed, boolean par4) {
		this.worldObj = worldObj;
		this.rand = new Random(seed);

		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 16);

		caveGenerator = new MapGenTropicsCaves();
		//villageGenerator = new MapGenKoaVillage(); TODO
		volcanoGen = new MapGenVolcano(worldObj, true);
		groveGen = new MapGenUndergroundGrove(worldObj);

		this.seed = seed;



	}

	public Chunk provideChunk(int x, int z)
	{
		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		Block[] blocks = new Block[16 * 16 * CHUNK_SIZE_Y];
		byte[] metas = new byte[16 * 16 * CHUNK_SIZE_Y];
		this.generateTerrain(x, z, blocks, metas);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		this.replaceBlocksForBiome(x, z, blocks, metas, this.biomesForGeneration);
		this.volcanoGen.generate(x, z, blocks, metas);
		this.groveGen.generate(x, z, blocks, metas);
		this.caveGenerator.generate(this, this.worldObj, x, z, blocks);
		//this.villageGenerator.generate(this, worldObj, x, z, null); TODO

		Chunk chunk = new Chunk(this.worldObj, blocks, metas, x, z);
		byte[] abyte1 = chunk.getBiomeArray();

		for (int k = 0; k < abyte1.length; ++k)
		{
			abyte1[k] = (byte)this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void generateTerrain(int x, int z, Block[] blocks, byte[] metas)
	{
		byte chunkSizeGenXZ = 4;
		byte chunkSizeGenY = 16;
		byte midHeight = 63;
		int k = chunkSizeGenXZ + 1;
		byte b3 = 17;
		int l = chunkSizeGenXZ + 1;
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, k + 5, l + 5);
		double[] noiseArray = null;
		noiseArray = this.initializeNoiseField(noiseArray, x * chunkSizeGenXZ, 0, z * chunkSizeGenXZ, k, b3, l);

		for (int i1 = 0; i1 < chunkSizeGenXZ; ++i1)
		{
			for (int j1 = 0; j1 < chunkSizeGenXZ; ++j1)
			{
				for (int k1 = 0; k1 < chunkSizeGenY; ++k1)
				{
					double d0 = 0.125D;
					double d1 = noiseArray[((i1 + 0) * l + j1 + 0) * b3 + k1 + 0];
					double d2 = noiseArray[((i1 + 0) * l + j1 + 1) * b3 + k1 + 0];
					double d3 = noiseArray[((i1 + 1) * l + j1 + 0) * b3 + k1 + 0];
					double d4 = noiseArray[((i1 + 1) * l + j1 + 1) * b3 + k1 + 0];
					double d5 = (noiseArray[((i1 + 0) * l + j1 + 0) * b3 + k1 + 1] - d1) * d0;
					double d6 = (noiseArray[((i1 + 0) * l + j1 + 1) * b3 + k1 + 1] - d2) * d0;
					double d7 = (noiseArray[((i1 + 1) * l + j1 + 0) * b3 + k1 + 1] - d3) * d0;
					double d8 = (noiseArray[((i1 + 1) * l + j1 + 1) * b3 + k1 + 1] - d4) * d0;

					for (int l1 = 0; l1 < 8; ++l1)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int i2 = 0; i2 < 4; ++i2)
						{
							double d14 = 0.25D;
							double d15 = (d11 - d10) * d14;
							double d16 = d10 - d15;

							for (int k2 = 0; k2 < 4; ++k2)
							{
								int index =  (i1 * 4 + i2) * CHUNK_SIZE_Y * 16 | (j1 * 4 + k2) * CHUNK_SIZE_Y | (k1 * 8 + l1);

								if ((d16 += d15) > 0.0D)
								{
									blocks[index] = Blocks.stone;
								}
								else if (k1 * 8 + l1 < midHeight)
								{
									blocks[index] = TCBlockRegistry.tropicsWater;
								}
								else
								{
									blocks[index] = Blocks.air;
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

	private float[] parabolicField;

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
					float f = 10.0F / MathHelper.sqrt_float((float)(k1 * k1 + l1 * l1) + 0.2F);
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
		boolean flag = false;
		boolean flag1 = false;
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
				BiomeGenBase biomegenbase = this.biomesForGeneration[k2 + 2 + (l2 + 2) * (par5 + 5)];

				for (int i3 = -b0; i3 <= b0; ++i3)
				{
					for (int j3 = -b0; j3 <= b0; ++j3)
					{
						BiomeGenBase biomegenbase1 = this.biomesForGeneration[k2 + i3 + 2 + (l2 + j3 + 2) * (par5 + 5)];
						float f4 = this.parabolicField[i3 + 2 + (j3 + 2) * 5] / (biomegenbase1.rootHeight + 2.0F);

						if (biomegenbase1.rootHeight > biomegenbase.rootHeight)
						{
							f4 /= 2.0F;
						}

						f1 += biomegenbase1.heightVariation * f4;
						f2 += biomegenbase1.rootHeight * f4;
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

	public void replaceBlocksForBiome(int x, int z, Block[] blocks, byte[] metas, BiomeGenBase[] biomes) //TODO: Move to biomes
	{
		Block sandBlock;
		short sandMetadata;

		int sandType = rand.nextInt(200);

		switch(sandType) {
		case 0:
			sandBlock = TCBlockRegistry.mineralSands;
			sandMetadata = (short) 0;
			break;
		case 1:
			sandBlock = TCBlockRegistry.mineralSands;
			sandMetadata = (short) 1;
			break;
		case 2:
			sandBlock = TCBlockRegistry.mineralSands;
			sandMetadata = (short) 2;
			break;
		case 3:
			sandBlock = TCBlockRegistry.mineralSands;
			sandMetadata = (short) 3;
			break;
		default:
			sandBlock = Blocks.sand;
			sandMetadata = (short) 0;
			break;
		}

		int a = -1;

		boolean flag = false;

		int k = (int)63;
		double d = 0.03125D;
		for(int l = 0; l < 16; l++)
		{
			for(int i1 = 0; i1 < 16; i1++)
			{
				BiomeGenTropicraft biome = (BiomeGenTropicraft)biomes[i1 + l * 16];
				Block top = biome.topBlock;
				Block filler = biome.fillerBlock;

				Block btop = sandBlock;
				Block bfiller = btop;

				// for colored sand
				if (biome == BiomeGenTropicraft.tropicsOcean) {
					btop = biome.sandBlock;
					sandMetadata = 0;
				}

				for(int l1 = 128 - 1; l1 >= 0; l1--)
				{
					int i2 = i1 * CHUNK_SIZE_Y * 16 | l * CHUNK_SIZE_Y | l1;

					Block block = blocks[i2];

					if(l1 <= 0)
					{
						blocks[i2] = Blocks.bedrock;
						continue;
					}

					if(block == Blocks.air || block == TCBlockRegistry.tropicsWater)
					{
						a = 0;
						continue;
					}

					if(a >= 0 && a < 5)
					{
						Block blockUsed = Blocks.stone;
						if(a == 0 && l1 < 63 + 3)
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
							if(top != Blocks.sand)
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
						blocks[i2] = blockUsed;
						metas[i2] = (byte) sandMetadata;
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

	public void populate(IChunkProvider par1IChunkProvider, int i, int j)
	{
		BlockSand.fallInstantly = true;
		int x = i * 16;
		int z = j * 16;
		BiomeGenTropicraft biome = (BiomeGenTropicraft) worldObj.getWorldChunkManager().getBiomeGenAt(x, z);
		rand.setSeed(worldObj.getSeed());
		long l1 = (rand.nextLong() / 2L) * 2L + 1L;
		long l2 = (rand.nextLong() / 2L) * 2L + 1L;
		rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());

		biome.decorate(worldObj, rand, x, z);

		//boolean flag = villageGenerator.generateStructuresInChunk(worldObj, rand, i, j); TODO

		/*if(this.groveGen.isActive) { TODO
			for(int r = 0; r < 4; r++) {
				int x = rand.nextInt(16) + x + 8;
				int z = rand.nextInt(16) + z + 8;
				new WorldGenUndergroundFruitTree(true, rand.nextInt(3)).generate(worldObj, rand, x, groveGen.getHeightAt(x, z), z);
			}
		}*/

		SpawnerAnimals.performWorldGenSpawning(worldObj, biome, x + 8, z + 8, 16, 16, rand);

		BlockSand.fallInstantly = false;
	}

	/**
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	int getTerrainHeightAt(int x, int z)
	{
		for(int y = CHUNK_SIZE_Y; y > 0; y--)
		{
			Block block = worldObj.getBlock(x, y, z);
			if(block == Blocks.dirt || block == Blocks.grass || block == Blocks.sand || block == Blocks.stone)
			{
				return y + 1;
			}
		}
		return 0;
	}

	public String makeString()
	{
		return "TropiLevelSource";
	}

	@Override
	public boolean chunkExists(int x, int z) {
		return true;
	}

	@Override
	public Chunk loadChunk(int x, int z) {
		return this.provideChunk(x, z);
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		//if (!par1EnumCreatureType.equals(EnumCreatureType.waterCreature)) System.out.println("getPossibleCreatures: " + par1EnumCreatureType);
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(par2, par4);
		return biomegenbase == null ? null : biomegenbase.getSpawnableList(par1EnumCreatureType);
	}

	@Override
	public ChunkPosition func_147416_a(World world, String s, int i, int j, int k) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int i, int j) {

	}

	@Override

	/**
	 * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
	 * unimplemented.
	 */
	public void saveExtraData() { }

}
