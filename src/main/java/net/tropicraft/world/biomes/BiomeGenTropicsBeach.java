package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.tropicraft.world.WorldProviderTropicraft;
import net.tropicraft.world.location.TownKoaVillageGenHelper;
import net.tropicraft.world.worldgen.WorldGenTropicsTreasure;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

	private static final int TREASURE_CHANCE = 25;
	private static final int VILLAGE_CHANCE = 40;
	
	public BiomeGenTropicsBeach(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		if(rand.nextInt(TREASURE_CHANCE) == 0) {
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicsTreasure(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(VILLAGE_CHANCE) == 0) {
			boolean success = false;
			for (int ii = 0; ii < 3 && !success; ii++) {
				int i = randCoord(rand, x, 16);
				int k = randCoord(rand, z, 16);
				int y = world.getTopSolidOrLiquidBlock(i, k);
				if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
				success = TownKoaVillageGenHelper.hookTryGenVillage(new ChunkCoordinates(i, getTerrainHeightAt(world, i, k), k), world);
			}
		}
		
		
		super.decorate(world, rand, x, z);
	}

}
