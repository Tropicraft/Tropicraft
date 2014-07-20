package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;

public class BiomeGenTropicsOcean extends BiomeGenTropicraft {

	public BiomeGenTropicsOcean(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		//TODO: Add ocean generation
		super.decorate(world, rand, x, z);
	}

}
