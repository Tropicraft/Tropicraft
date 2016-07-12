package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.configuration.ConfigGenRates;
import net.tropicraft.core.common.worldgen.WorldGenCurvedPalms;
import net.tropicraft.core.common.worldgen.WorldGenLargePalmTrees;
import net.tropicraft.core.common.worldgen.WorldGenNormalPalms;

public class BiomeDecoratorTropicsBeach extends BiomeDecoratorTropicraft {

	private static final int TREASURE_CHANCE = 25;
	private static final int VILLAGE_CHANCE = 10;

	public BiomeDecoratorTropicsBeach() {

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

		if (ConfigGenRates.LARGE_PALM_CHANCE != 0 && rand.nextInt(ConfigGenRates.LARGE_PALM_CHANCE) == 0) {
			i = randCoord(rand, chunkPos.getX(), 16);
			k = randCoord(rand, chunkPos.getZ(), 16);
			new WorldGenLargePalmTrees(world, rand).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
		}

		//		if(rand.nextInt(TREASURE_CHANCE) == 0) {
		//			int i = randCoord(rand, x, 16);
		//			int k = randCoord(rand, z, 16);
		//			new WorldGenTropicsTreasure(world, rand).generate(i, getTerrainHeightAt(world, i, k), k);
		//		}
		//		
		//		if(rand.nextInt(VILLAGE_CHANCE) == 0) {
		//			boolean success = false;
		//			for (int ii = 0; ii < 3 && !success; ii++) {
		//				int i = randCoord(rand, x, 16);
		//				int k = randCoord(rand, z, 16);
		//				int y = world.getTopSolidOrLiquidBlock(i, k);
		//				if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
		//				success = TownKoaVillageGenHelper.hookTryGenVillage(new ChunkCoordinates(i, getTerrainHeightAt(world, i, k), k), world);
		//			}
		//		}
	}

}
