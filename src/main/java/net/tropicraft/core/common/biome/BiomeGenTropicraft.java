package net.tropicraft.core.common.biome;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicraft;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityIguana;
import net.tropicraft.core.common.entity.hostile.EntityTropiCreeper;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeGenTropicraft extends Biome {

	public static final int[] DEFAULT_FLOWER_META = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

	public static BiomeGenTropicraft tropicsOcean = (BiomeGenTropicraft) new BiomeGenTropicsOcean((new Biome.BiomeProperties("TROPICS_OCEAN")).setBaseHeight(-1.0F).setHeightVariation(0.4F).setTemperature(1.5F).setRainfall(1.25F)) {
	    
	    private IBlockState mineralSand = BlockRegistry.sands.getDefaultState().withProperty(BlockTropicraftSands.VARIANT, TropicraftSands.MINERAL);

	    @Override
	    public IBlockState getStateForLayer(int yStart, int layer) {
	        IBlockState ret = super.getStateForLayer(yStart, layer);
	        if (ret == sandBlock && layer == 4) {
	            ret = mineralSand;
	        }
	        return ret;
	    }
	}.setRegistryName("tc_tropics_ocean");
	public static Biome kelpForest = new BiomeGenKelpForest((new Biome.BiomeProperties("KELP_FOREST")).setBaseHeight(-1.5F).setHeightVariation(0.3F).setTemperature(2.0F).setRainfall(1.25F)).setRegistryName("tc_tropics_kelp_forest");
	public static Biome tropics = new BiomeGenTropics((new Biome.BiomeProperties("TROPICS")).setBaseHeight(0.15F).setHeightVariation(0.15F).setTemperature(2.0F).setRainfall(1.5F)).setRegistryName("tc_tropics");
	public static Biome rainforestPlains = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_PLAINS")).setBaseHeight(0.25F).setHeightVariation(0.1F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_plains");
	public static Biome rainforestHills = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_HILLS")).setBaseHeight(0.45F).setHeightVariation(0.425F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_hills");
	public static Biome rainforestMountains = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_MOUNTAINS")).setBaseHeight(1.0F).setHeightVariation(1.2F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_mountains");
	public static Biome islandMountains = new BiomeGenRainforest((new Biome.BiomeProperties("TROPICS_RAINFOREST_ISLAND_MOUNTAINS")).setBaseHeight(0.1F).setHeightVariation(2.5F).setTemperature(1.5F).setRainfall(2.0F)).setRegistryName("tc_tropics_rainforest_island_mountains");
	public static Biome tropicsRiver = new BiomeGenTropicsRiver((new Biome.BiomeProperties("TROPICS_RIVER")).setBaseHeight(-0.7F).setHeightVariation(0.05F).setTemperature(1.5F).setRainfall(1.25F)).setRegistryName("tc_tropics_river");
	public static Biome tropicsBeach = new BiomeGenTropicsBeach((new Biome.BiomeProperties("TROPICS_BEACH")).setBaseHeight(-0.1F).setHeightVariation(0.1F).setTemperature(1.5F).setRainfall(1.25F)).setRegistryName("tc_tropics_beach");
	public static Biome tropicsLake = new BiomeGenTropicsOcean((new Biome.BiomeProperties("TROPICS_LAKE")).setBaseHeight(-0.6F).setHeightVariation(0.1F).setTemperature(1.5F).setRainfall(1.5F)).setRegistryName("tc_tropics_lake");

	public static boolean DISABLEDECORATION = false;

	public static void registerBiomes() {
		ForgeRegistries.BIOMES.register(tropicsOcean);
		ForgeRegistries.BIOMES.register(kelpForest);
		ForgeRegistries.BIOMES.register(tropics);
		ForgeRegistries.BIOMES.register(rainforestPlains);
		ForgeRegistries.BIOMES.register(rainforestHills);
		ForgeRegistries.BIOMES.register(rainforestMountains);
		ForgeRegistries.BIOMES.register(islandMountains);
		ForgeRegistries.BIOMES.register(tropicsRiver);
		ForgeRegistries.BIOMES.register(tropicsBeach);
		ForgeRegistries.BIOMES.register(tropicsLake);
	}
	
	protected IBlockState sandBlock = BlockRegistry.sands.getDefaultState();

	public BiomeGenTropicraft(BiomeProperties bgprop) {
		super(bgprop);

		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableWaterCreatureList.clear();

		this.theBiomeDecorator = new BiomeDecoratorTropicraft();

        this.spawnableCreatureList.add(new SpawnListEntry(EntityVMonkey.class, 20, 1, 3));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityIguana.class, 20, 1, 1));

        this.spawnableCreatureList.add(new SpawnListEntry(EntityTropiCreeper.class, 4, 1, 2));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityEIH.class, 10, 1, 1));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityTropiSkeleton.class, 4, 1, 8));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityAshenHunter.class, 4, 3, 12));

        //this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityTurtle.class, 6, 1 ,3));

        //this.spawnableMonsterList.add(new SpawnListEntry(SpiderAdult.class, 50, 1, 3));
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		this.theBiomeDecorator.decorate(world, rand, this, pos);
	}

	public IBlockState getStateForLayer(int yStart, int layer) {
	    if (yStart < 63 + 3) {
	        return sandBlock;
	    }
	    if (layer == 0) {
	        return topBlock;
	    } else {
	        return fillerBlock;
	    }
	}

}

