package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.worldgen.WorldGenBamboo;
import net.tropicraft.core.common.worldgen.WorldGenTropicraftNormalPalms;

public class BiomeDecoratorTropics extends BiomeDecoratorTropicraft {

	private static final int FRUIT_TREE_CHANCE = 2;
	private static final int TREASURE_CHANCE = 25;

	public BiomeDecoratorTropics() {

	}

	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) {
		if (this.decorating) {
			throw new RuntimeException("Already decorating");
		} else {
			this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
			this.chunkPos = pos;
			this.genDecorations(biome, worldIn, random);
			this.decorating = false;
		}
	}

	public void genDecorations(Biome biome, World world, Random rand) {
		System.out.println("decorate");
		if (BiomeGenTropicraft.DISABLEDECORATION) {
			System.out.println("decoration disabled via BiomeGenTropics.DISABLEDECORATION, " + this);
			return;
		}
		
	//	if(ConfigGenRates.BAMBOO_CHANCE != 0 && rand.nextInt(ConfigGenRates.BAMBOO_CHANCE) == 0) {
		int i = randCoord(rand, chunkPos.getX(), 16);
		int k = randCoord(rand, chunkPos.getZ(), 16);
		System.out.println(i + " " + k);
		new WorldGenBamboo(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
	//	}
		
			//	if (ConfigGenRates.NORMAL_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.NORMAL_PALM_CHANCE) == 0) {
		i = randCoord(rand, chunkPos.getX(), 16);
		k = randCoord(rand, chunkPos.getZ(), 16);
		new WorldGenTropicraftNormalPalms(world, rand).generate(new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
				//}

		//		{ //For scope, extra flowers for the tropics biome
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicraftFlowers(world, rand, TCBlockRegistry.flowers, DEFAULT_FLOWER_META).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//		
		//		if(rand.nextInt(FRUIT_TREE_CHANCE) == 0) {
		//			int treeType = new Random((long)(x >> 2) << 32 | (long)(z >> 2)).nextInt(4);
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicraftFruitTrees(world, rand, treeType).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//		
		//		if(rand.nextInt(TREASURE_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicsTreasure(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
	}

}
