package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.world.worldgen.WorldGenCoral;

public class BiomeGenTropicsOcean extends BiomeGenTropicraft {

	public BiomeGenTropicsOcean(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		if(rand.nextInt(5) == 0) {
			new WorldGenCoral().generate(world, rand, x + 6 + rand.nextInt(4), 64, z + 6 + rand.nextInt(4));
		}
		
		super.decorate(world, rand, x, z);
	}

}
