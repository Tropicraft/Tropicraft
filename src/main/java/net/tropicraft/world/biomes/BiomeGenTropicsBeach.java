package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

	public BiomeGenTropicsBeach(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		//TODO: Add beach generation
		super.decorate(world, rand, x, z);
	}

}
