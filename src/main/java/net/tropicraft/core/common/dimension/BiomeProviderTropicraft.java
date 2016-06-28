package net.tropicraft.core.common.dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.worldgen.genlayer.GenLayerTropicraft;

public class BiomeProviderTropicraft extends BiomeProvider {

	public static final List<Biome> allowedBiomes = Arrays.asList(BiomeGenTropicraft.tropics, BiomeGenTropicraft.rainforestPlains);
	private GenLayer genBiomes;
	private GenLayer biomeIndexLayer;
	private final BiomeCache biomeCache;
	private List<Biome> biomesToSpawnIn;

	public BiomeProviderTropicraft() {
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList<Biome>();
		this.biomesToSpawnIn.addAll(allowedBiomes);
	}

	private BiomeProviderTropicraft(long seed, WorldType worldType, String options) {
		this();
		GenLayer[] agenlayer = GenLayerTropicraft.initializeAllBiomeGenerators(seed, worldType);
		this.genBiomes = agenlayer[0];
		this.biomeIndexLayer = agenlayer[1];
	}

	public BiomeProviderTropicraft(WorldInfo info) {
		this(info.getSeed(), info.getTerrainType(), info.getGeneratorOptions());
	}

	/**
	 * Returns the biome generator
	 */
	 @Override
	 public Biome getBiomeGenerator(BlockPos pos) {
		 return this.getBiomeGenerator(pos, BiomeGenTropicraft.tropicsOcean);
	 }

	 @Override
	 public List<Biome> getBiomesToSpawnIn() {
		 return this.biomesToSpawnIn;
	 }

	 @Override
	 public Biome getBiomeGenerator(BlockPos pos, Biome biomeGenBaseIn) {
		 return this.biomeCache.getBiome(pos.getX(), pos.getZ(), biomeGenBaseIn);
	 }

	 /**
	  * Returns an array of biomes for the location input.
	  */
	 @Override
	 public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
		 IntCache.resetIntCache();

		 if (biomes == null || biomes.length < width * height)
		 {
			 biomes = new Biome[width * height];
		 }

		 int[] aint = this.genBiomes.getInts(x, z, width, height);

		 try
		 {
			 for (int i = 0; i < width * height; ++i)
			 {
				 biomes[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
			 }

			 return biomes;
		 }
		 catch (Throwable throwable)
		 {
			 CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			 CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
			 crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
			 crashreportcategory.addCrashSection("x", Integer.valueOf(x));
			 crashreportcategory.addCrashSection("z", Integer.valueOf(z));
			 crashreportcategory.addCrashSection("w", Integer.valueOf(width));
			 crashreportcategory.addCrashSection("h", Integer.valueOf(height));
			 throw new ReportedException(crashreport);
		 }
	 }

	 /**
	  * Gets biomes to use for the blocks and loads the other data like temperature and humidity onto the
	  * WorldChunkManager.
	  */
	 @Override
	 public Biome[] loadBlockGeneratorData(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
		 return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
	 }

	 /**
	  * Gets a list of biomes for the specified blocks.
	  */
	 @Override
	 public Biome[] getBiomeGenAt(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
		 IntCache.resetIntCache();

		 if (listToReuse == null || listToReuse.length < width * length)
		 {
			 listToReuse = new Biome[width * length];
		 }

		 if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
		 {
			 Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
			 System.arraycopy(abiome, 0, listToReuse, 0, width * length);
			 return listToReuse;
		 }
		 else
		 {
			 int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

			 for (int i = 0; i < width * length; ++i)
			 {
				 listToReuse[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
			 }

			 return listToReuse;
		 }
	 }

	 /**
	  * checks given Chunk's Biomes against List of allowed ones
	  */
	 @Override
	 public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
		 IntCache.resetIntCache();
		 int i = x - radius >> 2;
			 int j = z - radius >> 2;
			 int k = x + radius >> 2;
			 int l = z + radius >> 2;
			 int i1 = k - i + 1;
			 int j1 = l - j + 1;
			 int[] aint = this.genBiomes.getInts(i, j, i1, j1);

			 try
			 {
				 for (int k1 = 0; k1 < i1 * j1; ++k1)
				 {
					 Biome biome = Biome.getBiome(aint[k1]);

					 if (!allowed.contains(biome))
					 {
						 return false;
					 }
				 }

				 return true;
			 }
			 catch (Throwable throwable)
			 {
				 CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				 CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
				 crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
				 crashreportcategory.addCrashSection("x", Integer.valueOf(x));
				 crashreportcategory.addCrashSection("z", Integer.valueOf(z));
				 crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
				 crashreportcategory.addCrashSection("allowed", allowed);
				 throw new ReportedException(crashreport);
			 }
	 }

	 @Override
	 @Nullable
	 public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
		 IntCache.resetIntCache();
		 int i = x - range >> 2;
				 int j = z - range >> 2;
				 int k = x + range >> 2;
				 int l = z + range >> 2;
				 int i1 = k - i + 1;
				 int j1 = l - j + 1;
				 int[] aint = this.genBiomes.getInts(i, j, i1, j1);
				 BlockPos blockpos = null;
				 int k1 = 0;

				 for (int l1 = 0; l1 < i1 * j1; ++l1) {
					 int i2 = i + l1 % i1 << 2;
					 int j2 = j + l1 / i1 << 2;
					 Biome biome = Biome.getBiome(aint[l1]);

					 if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0)) {
						 blockpos = new BlockPos(i2, 0, j2);
						 ++k1;
					 }
				 }

				 return blockpos;
	 }

	 /**
	  * Calls the WorldChunkManager's biomeCache.cleanupCache()
	  */
	 @Override
	 public void cleanupCache() {
		 this.biomeCache.cleanupCache();
	 }
}
