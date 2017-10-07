package net.tropicraft.core.common.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicraft;

public class BiomeGenTropicraft extends Biome {

	public static final int[] DEFAULT_FLOWER_META = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

	public static Biome tropicsOcean = new BiomeGenTropicsOcean((new Biome.BiomeProperties("TROPICS_OCEAN")).setBaseHeight(-1.0F).setHeightVariation(0.4F).setTemperature(1.5F).setRainfall(1.25F)).setRegistryName("tc_tropics_ocean");
	public static Biome tropics = new BiomeGenTropics((new Biome.BiomeProperties("TROPICS")).setBaseHeight(0.15F).setHeightVariation(0.15F).setTemperature(2.0F).setRainfall(1.5F)).setRegistryName("tc_tropics");
	public static Biome rainforestPlains = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_PLAINS")).setBaseHeight(0.25F).setHeightVariation(0.1F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_plains");
	public static Biome rainforestHills = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_HILLS")).setBaseHeight(0.45F).setHeightVariation(0.425F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_hills");
	public static Biome rainforestMountains = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_MOUNTAINS")).setBaseHeight(1.0F).setHeightVariation(1.2F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_mountains");
	public static Biome islandMountains = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_ISLAND_MOUNTAINS")).setBaseHeight(0.1F).setHeightVariation(2.5F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_island_mountains");
	public static Biome tropicsRiver = new BiomeGenTropicsRiver((new Biome.BiomeProperties("TROPICS_RIVER")).setBaseHeight(-0.7F).setHeightVariation(0.05F).setTemperature(1.5F).setRainfall(1.25F)).setRegistryName("tc_tropics_river");
	public static Biome tropicsBeach = new BiomeGenTropicsBeach((new Biome.BiomeProperties("TROPICS_BEACH")).setBaseHeight(-0.1F).setHeightVariation(0.1F).setTemperature(1.5F).setRainfall(1.25F)).setRegistryName("tc_tropics_beach");
	public static Biome tropicsLake = new BiomeGenTropicsOcean((new Biome.BiomeProperties("TROPICS_LAKE")).setBaseHeight(-0.6F).setHeightVariation(0.1F).setTemperature(1.5F).setRainfall(1.5F)).setRegistryName("tc_tropics_lake");

	public Block sandBlock;
	public short sandMetadata;

	public static boolean DISABLEDECORATION = false;

	public static void registerBiomes() {
		ForgeRegistries.BIOMES.register(tropicsOcean);
		ForgeRegistries.BIOMES.register(tropics);
		ForgeRegistries.BIOMES.register(rainforestPlains);
		ForgeRegistries.BIOMES.register(rainforestHills);
		ForgeRegistries.BIOMES.register(rainforestMountains);
		ForgeRegistries.BIOMES.register(islandMountains);
		ForgeRegistries.BIOMES.register(tropicsRiver);
		ForgeRegistries.BIOMES.register(tropicsBeach);
		ForgeRegistries.BIOMES.register(tropicsLake);
	}

	public BiomeGenTropicraft(BiomeProperties bgprop) {
		super(bgprop);

		this.sandBlock = Blocks.SAND;
		this.sandMetadata = 0;

		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableWaterCreatureList.clear();

		this.theBiomeDecorator = new BiomeDecoratorTropicraft();

		//TODO: Add mobs that spawn in all biomes here
		//		if (bgprop. == ConfigBiomes.rainforestMountainsID || biomeID == ConfigBiomes.rainforestHillsID
		//				|| biomeID == ConfigBiomes.rainforestPlainsID) {
		//			this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogBlue.class, 25, 1, 2));
		//			this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogGreen.class, 25, 1, 2));
		//			this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogRed.class, 25, 1, 2));
		//			this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogYellow.class, 25, 1, 2));
		//		}
		//
		//		this.spawnableMonsterList.add(new SpawnListEntry(VMonkey.class, 20, 1, 3));
		//		this.spawnableMonsterList.add(new SpawnListEntry(EntityIguana.class, 20, 1, 1));
		//
		//		this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiCreeper.class, 2, 1, 2));
		//		this.spawnableMonsterList.add(new SpawnListEntry(EntityEIH.class, 10, 1, 1));
		//		this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiSkeleton.class, 25, 1, 8));
		//		this.spawnableMonsterList.add(new SpawnListEntry(EntityAshenHunter.class, 2, 3, 12));
		//
		//		this.spawnableMonsterList.add(new SpawnListEntry(Failgull.class, 30, 5, 15));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityTropicalFish.class, 10, 1, 12));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityEagleRay.class, 6, 1 ,3));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaTurtle.class, 6, 1 ,3));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeahorse.class, 6, 1 ,3));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityMarlin.class, 10, 1, 3));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityManOWar.class, 4, 1, 2));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityStarfish.class, 4, 1, 4));
		//		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaUrchin.class, 4, 1, 4));
		//
		//		this.spawnableMonsterList.add(new SpawnListEntry(SpiderAdult.class, 50, 1, 3));
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		this.theBiomeDecorator.decorate(world, rand, this, pos);

		//		if (biome == tropicsOcean) {
		//			if(rand.nextInt(5) == 0) {
		//				new WorldGenCoral().generate(world, rand, x + 6 + rand.nextInt(4), 64, z + 6 + rand.nextInt(4));
		//			}
		//		}
		//		
		//		if (ConfigGenRates.BAMBOO_CHANCE != 0 && rand.nextInt(ConfigGenRates.BAMBOO_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenBamboo(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.EIH_CHANCE != 0 && rand.nextInt(ConfigGenRates.EIH_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenEIH(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.TALL_FLOWERS_CHANCE != 0 && rand.nextInt(ConfigGenRates.TALL_FLOWERS_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			(new WorldGenTallFlower(world, rand, TCBlockRegistry.tallFlowers, 0, 1)).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.TALL_FLOWERS_CHANCE != 0 && rand.nextInt(ConfigGenRates.TALL_FLOWERS_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			(new WorldGenTallFlower(world, rand, TCBlockRegistry.pineapple, 7, 8)).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		{ //For scope
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicraftFlowers(world, rand, TCBlockRegistry.flowers, DEFAULT_FLOWER_META).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.SHIPWRECK_CHANCE != 0 && rand.nextInt(ConfigGenRates.SHIPWRECK_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenSunkenShip(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.CURVED_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.CURVED_PALM_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicraftCurvedPalm(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.LARGE_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.LARGE_PALM_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicraftLargePalmTrees(false).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.NORMAL_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.NORMAL_PALM_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicraftNormalPalms(false).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		if (ConfigGenRates.TALL_GRASS_CHANCE != 0 && rand.nextInt(ConfigGenRates.TALL_GRASS_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTallGrass(Blocks.tallgrass, 1).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
		//		}
		//
		//		for(int a = 0; a < ConfigGenRates.WATERFALL_AMOUNT; a++) {
		//			new WorldGenWaterfall(world, rand).generate(randCoord(rand, x, 16), WorldProviderTropicraft.MID_HEIGHT + rand.nextInt(WorldProviderTropicraft.INTER_HEIGHT), randCoord(rand, z, 16));
		//		}
	}

	//	public int getTerrainHeightAt(World world, int x, int z)
	//	{
	//		for(int y = world.getHeight(x, z) + 1; y > 0; y--)
	//		{
	//			Block id = world.getBlock(x, y, z);
	//			if(id == Blocks.grass || id == Blocks.dirt || id == Blocks.sand)
	//			{
	//				return y + 1;
	//			}
	//		}
	//		return 0;
	//	}



}
