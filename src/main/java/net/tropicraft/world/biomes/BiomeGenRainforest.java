package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.world.worldgen.WorldGenBamboo;
import net.tropicraft.world.worldgen.WorldGenCoffeePlant;

public class BiomeGenRainforest extends BiomeGenTropicraft {

	private static final int COFFEE_PLANT_AMOUNT = 3;
	
	public BiomeGenRainforest(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		for(int a = 0; a < COFFEE_PLANT_AMOUNT; a++) {
			new WorldGenCoffeePlant(world, rand).generate(randCoord(rand, x, 16), 0, randCoord(rand, z, 16));
		}
	}
	
}
