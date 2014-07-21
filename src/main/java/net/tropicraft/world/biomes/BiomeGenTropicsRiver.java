package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;

public class BiomeGenTropicsRiver extends BiomeGenTropicraft {

	public BiomeGenTropicsRiver(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		//TODO: Add tropics river generation
		super.decorate(world, rand, x, z);
	}

}
