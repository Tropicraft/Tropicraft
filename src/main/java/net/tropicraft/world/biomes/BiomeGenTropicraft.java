package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenTropicraft extends BiomeGenBase {

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
	public static BiomeGenBase tropics = new BiomeGenTropicraft(tropicsID).setHeight(new Height(0.15F, 0.15F)).setTemperatureRainfall(2.0F, 1.5F).setBiomeName("Tropics");
	public static BiomeGenBase rainforestPlains = new BiomeGenTropicraft(rainforestPlainsID).setHeight(new Height(0.25F, 0.1F)).setColor(0x11882f).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Rainforest Plains");
	public static BiomeGenBase rainforestHills = new BiomeGenTropicraft(rainforestHillsID).setHeight(new Height(0.45F, 0.425F)).setColor(0x11882f).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Rainforest Hills");
	public static BiomeGenBase rainforestMountains = new BiomeGenTropicraft(rainforestMountainsID).setHeight(new Height(1.0F, 1.2F)).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Rainforest Mountains");
	public static BiomeGenBase islandMountains = new BiomeGenTropicraft(islandMountainsID).setHeight(new Height(0.1F, 2.5F)).setTemperatureRainfall(1.5F, 2.0F).setBiomeName("Extreme Rainforest Mountains");
	public static BiomeGenBase tropicsRiver = new BiomeGenTropicraft(tropicsRiverID).setHeight(new Height(-0.7F, 0.05F)).setTemperatureRainfall(1.5F, 1.25F).setBiomeName("Tropical River");
	public static BiomeGenBase tropicsBeach = new BiomeGenTropicraft(tropicsBeachID).setHeight(new Height(-0.1F, 0.1F)).setTemperatureRainfall(1.5F, 1.25F).setBiomeName("Tropical Beach");
	public static BiomeGenBase tropicsLake = new BiomeGenTropicraft(tropicsLakeID).setHeight(new Height(-0.6F, 0.1F)).setTemperatureRainfall(1.5F, 1.5F).setBiomeName("Tropical Lake");
	
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
	public void decorate(World world, Random rand, int i, int k) {
		
	}
	
}
