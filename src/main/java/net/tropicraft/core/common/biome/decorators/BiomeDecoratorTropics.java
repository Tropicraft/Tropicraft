package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.configuration.GenRates;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.worldgen.WorldGenBamboo;
import net.tropicraft.core.common.worldgen.WorldGenCurvedPalms;
import net.tropicraft.core.common.worldgen.WorldGenEIH;
import net.tropicraft.core.common.worldgen.WorldGenFruitTrees;
import net.tropicraft.core.common.worldgen.WorldGenLargePalmTrees;
import net.tropicraft.core.common.worldgen.WorldGenNormalPalms;
import net.tropicraft.core.common.worldgen.WorldGenTallFlower;
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

		if (GenRates.BAMBOO_CHANCE != 0 && rand.nextInt(GenRates.BAMBOO_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
			k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			new WorldGenBamboo(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
		}

		if (GenRates.NORMAL_PALM_CHANCE != 0 && rand.nextInt(GenRates.NORMAL_PALM_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
			k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			new WorldGenNormalPalms(world, rand).generate(new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		if (GenRates.CURVED_PALM_CHANCE != 0 && rand.nextInt(GenRates.CURVED_PALM_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
			k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			new WorldGenCurvedPalms(world, rand).generate(new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		if (GenRates.EIH_CHANCE != 0 && rand.nextInt(GenRates.EIH_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
			k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			new WorldGenEIH(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
		}

		i = randDecorationCoord(rand, chunkPos.getX(), 16);
		k = randDecorationCoord(rand, chunkPos.getZ(), 16);
		new WorldGenTropicalFlowers(world, rand, BlockRegistry.flowers).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));

		if (GenRates.LARGE_PALM_CHANCE != 0 && rand.nextInt(GenRates.LARGE_PALM_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
			k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			new WorldGenLargePalmTrees(world, rand).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		if (GenRates.FRUIT_TREE_CHANCE != 0 && rand.nextInt(GenRates.FRUIT_TREE_CHANCE) == 0) {
			int treeType = new Random((long)(chunkPos.getX() >> 2) << 32 | (long)(chunkPos.getZ() >> 2)).nextInt(4);
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
			k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			new WorldGenFruitTrees(world, rand, treeType).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
		}

		if (GenRates.TALL_GRASS_CHANCE != 0 && rand.nextInt(GenRates.TALL_GRASS_CHANCE) == 0) {
			for (int a = 0; a < 10; a++) {
		        int xRand = rand.nextInt(16) + 8;
		        int zRand = rand.nextInt(16) + 8;
		        int yRand = world.getHeight(this.chunkPos.add(xRand, 0, zRand)).getY() * 2;

		        if (yRand > 0) {
		            int rando = rand.nextInt(yRand);
		            biome.getRandomWorldGenForGrass(rand).generate(world, rand, this.chunkPos.add(xRand, rando, zRand));
		        }	
			}
		}
		
		// Pineapples
		if (GenRates.TALL_FLOWERS_CHANCE != 0 && rand.nextInt(GenRates.TALL_FLOWERS_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
	        int y = getTerrainHeightAt(world, i, k);
	        k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			BlockPos pos = new BlockPos(i, y, k);
			(new WorldGenTallFlower(world, rand, BlockRegistry.pineapple.getDefaultState())).generate(pos);
		}
		
		// Irises
		if (GenRates.TALL_FLOWERS_CHANCE != 0 && rand.nextInt(GenRates.TALL_FLOWERS_CHANCE) == 0) {
			i = randDecorationCoord(rand, chunkPos.getX(), 16);
	        int y = getTerrainHeightAt(world, i, k);
	        k = randDecorationCoord(rand, chunkPos.getZ(), 16);
			BlockPos pos = new BlockPos(i, y, k);
			(new WorldGenTallFlower(world, rand, BlockRegistry.iris.getDefaultState())).generate(pos);
		}

		//		if(rand.nextInt(TREASURE_CHANCE) == 0) {
		//			int i = randDecorationCoord(rand, x, 16);
		//			int k = randDecorationCoord(rand, z, 16);
		//			new WorldGenTropicsTreasure(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}

        //
        //      for(int a = 0; a < ConfigGenRates.WATERFALL_AMOUNT; a++) {
        //          new WorldGenWaterfall(world, rand).generate(randDecorationCoord(rand, x, 16), WorldProviderTropicraft.MID_HEIGHT + rand.nextInt(WorldProviderTropicraft.INTER_HEIGHT), randDecorationCoord(rand, z, 16));
        //      }
	}

}
