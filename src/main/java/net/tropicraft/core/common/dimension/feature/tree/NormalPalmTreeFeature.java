package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;

import java.util.Random;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class NormalPalmTreeFeature extends PalmTreeFeature {
    public NormalPalmTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator generator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
        pos = pos.immutable();

        int height = random.nextInt(4) + 6;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!getSapling().canSurvive(getSapling().defaultBlockState(), world, pos)) {
            return false;
        }

        if (world.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK) {
            world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);
        }

        int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        placeLeaf(world, i, j + height + 2, k);
        placeLeaf(world, i, j + height + 1, k + 1);
        placeLeaf(world, i, j + height + 1, k + 2);
        placeLeaf(world, i, j + height + 1, k + 3);
        placeLeaf(world, i, j + height, k + 4);
        placeLeaf(world, i + 1, j + height + 1, k);
        placeLeaf(world, i + 2, j + height + 1, k);
        placeLeaf(world, i + 3, j + height + 1, k);
        placeLeaf(world, i + 4, j + height, k);
        placeLeaf(world, i, j + height + 1, k - 1);
        placeLeaf(world, i, j + height + 1, k - 2);
        placeLeaf(world, i, j + height + 1, k - 3);
        placeLeaf(world, i, j + height, k - 4);
        placeLeaf(world, i - 1, j + height + 1, k);
        placeLeaf(world, i - 1, j + height + 1, k - 1);
        placeLeaf(world, i - 1, j + height + 1, k + 1);
        placeLeaf(world, i + 1, j + height + 1, k - 1);
        placeLeaf(world, i + 1, j + height + 1, k + 1);
        placeLeaf(world, i - 2, j + height + 1, k);
        placeLeaf(world, i - 3, j + height + 1, k);
        placeLeaf(world, i - 4, j + height, k);
        placeLeaf(world, i + 2, j + height + 1, k + 2);
        placeLeaf(world, i + 2, j + height + 1, k - 2);
        placeLeaf(world, i - 2, j + height + 1, k + 2);
        placeLeaf(world, i - 2, j + height + 1, k - 2);
        placeLeaf(world, i + 3, j + height, k + 3);
        placeLeaf(world, i + 3, j + height, k - 3);
        placeLeaf(world, i - 3, j + height, k + 3);
        placeLeaf(world, i - 3, j + height, k - 3);

        for (int j1 = 0; j1 < height + 2; j1++) {
            BlockPos logPos = pos.above(j1);
            if (TreeFeature.validTreePos(world, logPos)) {
                placeLog(world, logPos);
            }
        }

        spawnCoconuts(world, new BlockPos(i, j + height, k), random, 2, getLeaf());

        return true;
    }
}
