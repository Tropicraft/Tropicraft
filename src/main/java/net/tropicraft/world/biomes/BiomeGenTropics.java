package net.tropicraft.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.worldgen.WorldGenTropicraftFlowers;
import net.tropicraft.world.worldgen.WorldGenTropicraftFruitTrees;

public class BiomeGenTropics extends BiomeGenTropicraft {

	private static final int FRUIT_TREE_CHANCE = 2;
	
	public BiomeGenTropics(int biomeID) {
		super(biomeID);
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		{ //For scope, extra flowers for the tropics biome
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicraftFlowers(world, world.rand, TCBlockRegistry.flowers, DEFAULT_FLOWER_META).generate(i, getTerrainHeightAt(world, i, k), k);
		}
		
		if(rand.nextInt(FRUIT_TREE_CHANCE) == 0) {
			int treeType = new Random((long)(x >> 2) << 32 | (long)(z >> 2)).nextInt(4);
			int i = randCoord(rand, x, 16);
			int k = randCoord(rand, z, 16);
			new WorldGenTropicraftFruitTrees(world, world.rand, treeType).generate(i, getTerrainHeightAt(world, i, k), k);
		}
	}

}
