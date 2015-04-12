package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.tropicraft.entity.hostile.EntityAshenHunter;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.entity.hostile.EntityTropiCreeper;
import net.tropicraft.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.entity.hostile.SpiderAdult;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.passive.EntityTreeFrogGreen;
import net.tropicraft.entity.passive.VMonkey;
import net.tropicraft.entity.underdasea.EntityEagleRay;
import net.tropicraft.entity.underdasea.EntityMarlin;
import net.tropicraft.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.WorldProviderTropicraft;
import net.tropicraft.world.worldgen.WorldGenBamboo;
import net.tropicraft.world.worldgen.WorldGenEIH;
import net.tropicraft.world.worldgen.WorldGenSunkenShip;
import net.tropicraft.world.worldgen.WorldGenTallFlower;
import net.tropicraft.world.worldgen.WorldGenTropicraftCurvedPalm;
import net.tropicraft.world.worldgen.WorldGenTropicraftFlowers;
import net.tropicraft.world.worldgen.WorldGenTropicraftLargePalmTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftNormalPalms;
import net.tropicraft.world.worldgen.WorldGenWaterfall;

public class BiomeGenTropicraft extends BiomeGenBase {

	public static final int[] DEFAULT_FLOWER_META = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
	
	private static final int EIH_CHANCE = 50;
	private static final int SHIPWRECK_CHANCE = 200;
	private static final int TALL_FLOWERS_CHANCE = 3;
	private static final int BAMBOO_CHANCE = 2;
	private static final int WATERFALL_AMOUNT = 25;
	private static final int TALL_GRASS_CHANCE = 4;
	public static final int CURVED_PALM_CHANCE = 3;
	public static final int LARGE_PALM_CHANCE = 3;
	public static final int NORMAL_PALM_CHANCE = 3;
	
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
	
	public static boolean DISABLEDECORATION = false;
	
	public BiomeGenTropicraft(int biomeID) {
		super(biomeID);
		
		this.sandBlock = Blocks.sand;
		this.sandMetadata = 0;
		
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		
		//TODO: Add mobs that spawn in all biomes here
		if (biomeID == rainforestMountainsID) {
        	this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogBlue.class, 25, 1, 2));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogGreen.class, 25, 1, 2));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogRed.class, 25, 1, 2));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogYellow.class, 25, 1, 2));
        }
        
        this.spawnableMonsterList.add(new SpawnListEntry(VMonkey.class, 20, 1, 3));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityIguana.class, 20, 1, 1));
        
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiCreeper.class, 2, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEIH.class, 10, 1, 1));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiSkeleton.class, 25, 1, 8));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityAshenHunter.class, 2, 3, 12));
        
        //this.spawnableMonsterList.add(new SpawnListEntry(Failgull.class, 30, 5, 20));
        //this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityTropicalFish.class, 10, 1 ,12));
        //this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityMarlin.class, 10, 1 ,16));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityEagleRay.class, 6, 1 ,3));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaTurtle.class, 6, 1 ,3));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeahorse.class, 6, 1 ,3));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityMarlin.class, 6, 1 ,3));
        
        
        this.spawnableMonsterList.add(new SpawnListEntry(SpiderAdult.class, 50, 1, 3));
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
		
		if(rand.nextInt(TALL_FLOWERS_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			(new WorldGenTallFlower(world, rand, TCBlockRegistry.tallFlowers, 0, 1)).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(TALL_FLOWERS_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			(new WorldGenTallFlower(world, rand, TCBlockRegistry.pineapple, 7, 8)).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		{ //For scope
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicraftFlowers(world, rand, TCBlockRegistry.flowers, DEFAULT_FLOWER_META).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(SHIPWRECK_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenSunkenShip(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(CURVED_PALM_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicraftCurvedPalm(world, rand).generate(i, this.getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(LARGE_PALM_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicraftLargePalmTrees(false).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(NORMAL_PALM_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicraftNormalPalms(false).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(TALL_GRASS_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTallGrass(Blocks.tallgrass, 1).generate(world, rand, i, this.getTerrainHeightAt(world, i, k), k);
		}
		
		for(int a = 0; a < WATERFALL_AMOUNT; a++) {
			new WorldGenWaterfall(world, rand).generate(randCoord(rand, x, 16), WorldProviderTropicraft.MID_HEIGHT + rand.nextInt(WorldProviderTropicraft.INTER_HEIGHT), randCoord(rand, z, 16));
		}
	}
	
	public int getTerrainHeightAt(World world, int x, int z)
	{
		for(int y = world.getHeightValue(x, z) + 1; y > 0; y--)
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
		return base + rand.nextInt(variance);
	}
	
}
