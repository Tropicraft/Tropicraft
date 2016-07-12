package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.config.ConfigGenRates;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.worldgen.WorldGenBamboo;
import net.tropicraft.core.common.worldgen.WorldGenCurvedPalms;
import net.tropicraft.core.common.worldgen.WorldGenEIH;
import net.tropicraft.core.common.worldgen.WorldGenFruitTrees;
import net.tropicraft.core.common.worldgen.WorldGenLargePalmTrees;
import net.tropicraft.core.common.worldgen.WorldGenNormalPalms;
import net.tropicraft.core.common.worldgen.WorldGenTropicalFlowers;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeDecoratorTropics extends BiomeDecoratorTropicraft {

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
		int i = 0;
		int k = 0;

		if (BiomeGenTropicraft.DISABLEDECORATION) {
			System.out.println("decoration disabled via BiomeGenTropics.DISABLEDECORATION, " + this);
			return;
		}

		if (ConfigGenRates.BAMBOO_CHANCE != 0 && rand.nextInt(ConfigGenRates.BAMBOO_CHANCE) == 0) {
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenBamboo(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
		}

		if (ConfigGenRates.NORMAL_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.NORMAL_PALM_CHANCE) == 0) {
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenNormalPalms(world, rand).generate(new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		if (ConfigGenRates.CURVED_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.CURVED_PALM_CHANCE) == 0) {
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenCurvedPalms(world, rand).generate(new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		if (ConfigGenRates.EIH_CHANCE != 0 && rand.nextInt(ConfigGenRates.EIH_CHANCE) == 0) {
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenEIH(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
		}

		i = randCoord(rand, chunkPos.getX(), 16);
		k = randCoord(rand, chunkPos.getZ(), 16);
		new WorldGenTropicalFlowers(world, rand, BlockRegistry.flowers).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));

		if (ConfigGenRates.LARGE_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.LARGE_PALM_CHANCE) == 0) {
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenLargePalmTrees(world, rand).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		if (ConfigGenRates.FRUIT_TREE_CHANCE != 0 &&  rand.nextInt(ConfigGenRates.FRUIT_TREE_CHANCE) == 0) {
			int treeType = new Random((long)(chunkPos.getX() >> 2) << 32 | (long)(chunkPos.getZ() >> 2)).nextInt(4);
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenFruitTrees(world, rand, treeType).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
		}

		//		if(rand.nextInt(TREASURE_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicsTreasure(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
	}

}
