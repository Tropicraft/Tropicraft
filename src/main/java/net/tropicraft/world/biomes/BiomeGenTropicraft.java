package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.worldgen.WorldGenBamboo;
import net.tropicraft.world.worldgen.WorldGenEIH;
import net.tropicraft.world.worldgen.WorldGenSunkenShip;
import net.tropicraft.world.worldgen.WorldGenTallFlower;

public class BiomeGenTropicraft extends BiomeGenBase {

	private static final int EIH_CHANCE = 20;
	private static final int SHIPWRECK_CHANCE = 200;
	private static final int FLOWERS_CHANCE = 2;
	private static final int BAMBOO_CHANCE = 2;
	
	//TODO: Add config
	public static int tropicsOceanID = 60;
	public static int tropicsID = 61;
	public static int rainforestPlainsID = 62;
	public static int rainforestHillsID = 63;
	public static int rainforestMountainsID = 64;
	public static int tropicsRiverID = 65;
	public static int tropicsBeachID = 66;
	public static int tropicsLakeID = 67;
	public static int islandMountainsID = 68;
	
	public static BiomeGenBase tropicsOcean = new BiomeGenTropicraft(tropicsOceanID).setHeight(new Height(-1.0F, 0.4F)).setTemperatureRainfall(1.5F, 1.25F).setBiomeName("Tropical Ocean");
	public static BiomeGenBase tropics = new BiomeGenTropics(tropicsID).setHeight(new Height(0.15F, 0.15F)).setTemperatureRainfall(2.0F, 1.5F).setBiomeName("Tropics");
	public static BiomeGenBase rainforestPlains = new BiomeGenRainforest(rainforestPlainsID).setHeight(new Height(0.25F, 0.1F)).setColor(0x11882f).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Rainforest Plains");
	public static BiomeGenBase rainforestHills = new BiomeGenRainforest(rainforestHillsID).setHeight(new Height(0.45F, 0.425F)).setColor(0x11882f).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Rainforest Hills");
	public static BiomeGenBase rainforestMountains = new BiomeGenRainforest(rainforestMountainsID).setHeight(new Height(1.0F, 1.2F)).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Rainforest Mountains");
	public static BiomeGenBase islandMountains = new BiomeGenRainforest(islandMountainsID).setHeight(new Height(0.1F, 2.5F)).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Extreme Rainforest Mountains");
	public static BiomeGenBase tropicsRiver = new BiomeGenTropicsRiver(tropicsRiverID).setHeight(new Height(-0.7F, 0.05F)).setTemperatureRainfall(1.5F, 1.25F).setBiomeName("Tropical River");
	public static BiomeGenBase tropicsBeach = new BiomeGenTropicsBeach(tropicsBeachID).setHeight(new Height(-0.1F, 0.1F)).setTemperatureRainfall(1.5F, 1.25F).setBiomeName("Tropical Beach");
	public static BiomeGenBase tropicsLake = new BiomeGenTropicsOcean(tropicsLakeID).setHeight(new Height(-0.6F, 0.1F)).setTemperatureRainfall(1.5F, 1.5F).setBiomeName("Tropical Lake");
	
	public Block sandBlock;
	public short sandMetadata;
	
	public BiomeGenTropicraft(int biomeID) {
		super(biomeID);
		
		this.sandBlock = Blocks.sand;
		this.sandMetadata = 0;
		
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		
		//TODO: Add mobs that spawn in all biomes here
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {	
		if(rand.nextInt(BAMBOO_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenBamboo(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(EIH_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenEIH(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(FLOWERS_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			(new WorldGenTallFlower(world, rand, TCBlockRegistry.tallFlowers, 0, 1)).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(FLOWERS_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			(new WorldGenTallFlower(world, rand, TCBlockRegistry.pineapple, 7, 8)).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(SHIPWRECK_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenSunkenShip(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
	}
	
	public int getTerrainHeightAt(World world, int x, int z)
	{
		for(int y = 256; y > 0; y--)
		{
			Block id = world.getBlock(x, y, z);
			if(id == Blocks.grass || id == Blocks.dirt || id == Blocks.sand)
			{
				return y + 1;
			}
		}
		return 0;
	}
	
	public final int randCoord(Random rand, int base, int variance) {
		return base + rand.nextInt(variance) + (variance / 2);
	}
	
}
