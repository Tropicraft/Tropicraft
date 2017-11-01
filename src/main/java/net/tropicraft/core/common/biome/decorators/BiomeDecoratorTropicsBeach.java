package net.tropicraft.core.common.biome.decorators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.configuration.GenRates;
import net.tropicraft.core.common.dimension.WorldProviderTropicraft;
import net.tropicraft.core.common.worldgen.WorldGenCurvedPalms;
import net.tropicraft.core.common.worldgen.WorldGenLargePalmTrees;
import net.tropicraft.core.common.worldgen.WorldGenNormalPalms;
import net.tropicraft.core.common.worldgen.WorldGenTropicsTreasure;
import net.tropicraft.core.common.worldgen.village.TownKoaVillageGenHelper;

import java.util.Random;

public class BiomeDecoratorTropicsBeach extends BiomeDecoratorTropicraft {

    public static final int TREASURE_CHANCE = 3;
    private static final int VILLAGE_CHANCE = 15;

    public BiomeDecoratorTropicsBeach() {

    }

    public void genDecorations(Biome biome, World world, Random rand) {
        int i = 0;
        int k = 0;

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

        if (GenRates.LARGE_PALM_CHANCE != 0 && rand.nextInt(GenRates.LARGE_PALM_CHANCE) == 0) {
            i = randDecorationCoord(rand, chunkPos.getX(), 16);
            k = randDecorationCoord(rand, chunkPos.getZ(), 16);
            new WorldGenLargePalmTrees(world, rand).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(world, i, k), k));
        }

        if(rand.nextInt(TREASURE_CHANCE) == 0) {
            i = randDecorationCoord(rand, chunkPos.getX(), 16);
            k = randDecorationCoord(rand, chunkPos.getZ(), 16);
            new WorldGenTropicsTreasure(world, rand).generate(world, rand, new BlockPos(i, getTerrainHeightAt(world, i, k), k));
        }

        decorateForVillage(world, rand, chunkPos);
    }

    public static void decorateForVillage(World world, Random rand, BlockPos pos) {
        if(rand.nextInt(VILLAGE_CHANCE) == 0) {
            boolean success = false;
            for (int ii = 0; ii < 20 && !success; ii++) {
                int i = randCoord(rand, pos.getX(), 16);
                int k = randCoord(rand, pos.getZ(), 16);
                int y = world.getTopSolidOrLiquidBlock(new BlockPos(i, 0, k)).getY();
                if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
                success = TownKoaVillageGenHelper.hookTryGenVillage(new BlockPos(i, y, k), world);
            }
        }
    }

    public static final int randCoord(Random rand, int base, int variance) {
        return base + rand.nextInt(variance);
    }

}
