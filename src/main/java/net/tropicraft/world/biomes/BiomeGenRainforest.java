package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.world.worldgen.WorldGenBamboo;
import net.tropicraft.world.worldgen.WorldGenCoffeePlant;
import net.tropicraft.world.worldgen.WorldGenForestAltarRuin;
import net.tropicraft.world.worldgen.WorldGenTCUndergrowth;
import net.tropicraft.world.worldgen.WorldGenTallTree;

public class BiomeGenRainforest extends BiomeGenTropicraft {

	private static final int COFFEE_PLANT_AMOUNT = 2;
	private static final int ALTAR_CHANCE = 70;
	private static final int TALL_TREE_CHANCE = 2;
	private static final int UNDERGROWTH_AMOUNT = 15;
	
	public BiomeGenRainforest(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		if(rand.nextInt(ALTAR_CHANCE) == 0) {
			new WorldGenForestAltarRuin(world, rand).generate(randCoord(rand, x, 16), 0, randCoord(rand, x, 16));
		}
		
		if(rand.nextInt(TALL_TREE_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTallTree(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		for(int a = 0; a < UNDERGROWTH_AMOUNT; a++) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTCUndergrowth(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		for(int a = 0; a < COFFEE_PLANT_AMOUNT; a++) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenCoffeePlant(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
	}
	
}
