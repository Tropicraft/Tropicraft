package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class LargePalmTreeFeature extends PalmTreeFeature {
    public LargePalmTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();
        pos = pos.immutable();

        int height = random.nextInt(7) + 7;

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

        // Place trunk
        for (int y = 0; y <= height; y++) {
            placeLog(world, pos.getX(), pos.getY() + y, pos.getZ());
        }
        
        final int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        // Wheeee, auto-generated code!
        placeLeaf(world, i + 0, j + height + 1, k + -7);
        placeLeaf(world, i + -1, j + height + 1, k + -6);
        placeLeaf(world, i + 1, j + height + 1, k + -6);
        placeLeaf(world, i + -5, j + height + 1, k + -5);
        placeLeaf(world, i + 5, j + height + 1, k + -5);
        placeLeaf(world, i + -6, j + height + 1, k + -1);
        placeLog(world, i + 0, j + height + 1, k + -1);
        placeLeaf(world, i + 6, j + height + 1, k + -1);
        placeLeaf(world, i + -7, j + height + 1, k + 0);
        placeLog(world, i + -1, j + height + 1, k + 0);
        placeLog(world, i + 0, j + height + 1, k + 0);
        placeLog(world, i + 1, j + height + 1, k + 0);
        placeLeaf(world, i + 7, j + height + 1, k + 0);
        placeLeaf(world, i + -6, j + height + 1, k + 1);
        placeLog(world, i + 0, j + height + 1, k + 1);
        placeLeaf(world, i + 6, j + height + 1, k + 1);
        placeLeaf(world, i + -5, j + height + 1, k + 5);
        placeLeaf(world, i + 5, j + height + 1, k + 5);
        placeLeaf(world, i + -1, j + height + 1, k + 6);
        placeLeaf(world, i + 1, j + height + 1, k + 6);
        placeLeaf(world, i + 0, j + height + 1, k + 7);
        placeLeaf(world, i + 0, j + height + 2, k + -6);
        placeLeaf(world, i + -4, j + height + 2, k + -5);
        placeLeaf(world, i + -1, j + height + 2, k + -5);
        placeLeaf(world, i + 1, j + height + 2, k + -5);
        placeLeaf(world, i + 4, j + height + 2, k + -5);
        placeLeaf(world, i + -5, j + height + 2, k + -4);
        placeLeaf(world, i + -3, j + height + 2, k + -4);
        placeLeaf(world, i + -1, j + height + 2, k + -4);
        placeLeaf(world, i + 1, j + height + 2, k + -4);
        placeLeaf(world, i + 3, j + height + 2, k + -4);
        placeLeaf(world, i + 5, j + height + 2, k + -4);
        placeLeaf(world, i + -4, j + height + 2, k + -3);
        placeLeaf(world, i + -2, j + height + 2, k + -3);
        placeLeaf(world, i + -1, j + height + 2, k + -3);
        placeLeaf(world, i + 1, j + height + 2, k + -3);
        placeLeaf(world, i + 2, j + height + 2, k + -3);
        placeLeaf(world, i + 4, j + height + 2, k + -3);
        placeLeaf(world, i + -3, j + height + 2, k + -2);
        placeLeaf(world, i + -1, j + height + 2, k + -2);
        placeLeaf(world, i + 1, j + height + 2, k + -2);
        placeLeaf(world, i + 3, j + height + 2, k + -2);
        placeLeaf(world, i + -5, j + height + 2, k + -1);
        placeLeaf(world, i + -4, j + height + 2, k + -1);
        placeLeaf(world, i + -3, j + height + 2, k + -1);
        placeLeaf(world, i + -2, j + height + 2, k + -1);
        placeLeaf(world, i + -1, j + height + 2, k + -1);
        placeLeaf(world, i + 0, j + height + 2, k + -1);
        placeLeaf(world, i + 1, j + height + 2, k + -1);
        placeLeaf(world, i + 2, j + height + 2, k + -1);
        placeLeaf(world, i + 3, j + height + 2, k + -1);
        placeLeaf(world, i + 4, j + height + 2, k + -1);
        placeLeaf(world, i + 5, j + height + 2, k + -1);
        placeLeaf(world, i + -6, j + height + 2, k + 0);
        placeLeaf(world, i + -1, j + height + 2, k + 0);
        placeLeaf(world, i + 0, j + height + 2, k + 0);
        placeLeaf(world, i + 1, j + height + 2, k + 0);
        placeLeaf(world, i + 6, j + height + 2, k + 0);
        placeLeaf(world, i + -5, j + height + 2, k + 1);
        placeLeaf(world, i + -4, j + height + 2, k + 1);
        placeLeaf(world, i + -3, j + height + 2, k + 1);
        placeLeaf(world, i + -2, j + height + 2, k + 1);
        placeLeaf(world, i + -1, j + height + 2, k + 1);
        placeLeaf(world, i + 0, j + height + 2, k + 1);
        placeLeaf(world, i + 1, j + height + 2, k + 1);
        placeLeaf(world, i + 2, j + height + 2, k + 1);
        placeLeaf(world, i + 3, j + height + 2, k + 1);
        placeLeaf(world, i + 4, j + height + 2, k + 1);
        placeLeaf(world, i + 5, j + height + 2, k + 1);
        placeLeaf(world, i + -3, j + height + 2, k + 2);
        placeLeaf(world, i + -1, j + height + 2, k + 2);
        placeLeaf(world, i + 1, j + height + 2, k + 2);
        placeLeaf(world, i + 3, j + height + 2, k + 2);
        placeLeaf(world, i + -4, j + height + 2, k + 3);
        placeLeaf(world, i + -2, j + height + 2, k + 3);
        placeLeaf(world, i + -1, j + height + 2, k + 3);
        placeLeaf(world, i + 1, j + height + 2, k + 3);
        placeLeaf(world, i + 2, j + height + 2, k + 3);
        placeLeaf(world, i + 4, j + height + 2, k + 3);
        placeLeaf(world, i + -5, j + height + 2, k + 4);
        placeLeaf(world, i + -3, j + height + 2, k + 4);
        placeLeaf(world, i + -1, j + height + 2, k + 4);
        placeLeaf(world, i + 1, j + height + 2, k + 4);
        placeLeaf(world, i + 3, j + height + 2, k + 4);
        placeLeaf(world, i + 5, j + height + 2, k + 4);
        placeLeaf(world, i + -4, j + height + 2, k + 5);
        placeLeaf(world, i + -1, j + height + 2, k + 5);
        placeLeaf(world, i + 1, j + height + 2, k + 5);
        placeLeaf(world, i + 4, j + height + 2, k + 5);
        placeLeaf(world, i + 0, j + height + 2, k + 6);
        placeLeaf(world, i + 0, j + height + 3, k + -5);
        placeLeaf(world, i + -4, j + height + 3, k + -4);
        placeLeaf(world, i + 0, j + height + 3, k + -4);
        placeLeaf(world, i + 4, j + height + 3, k + -4);
        placeLeaf(world, i + -3, j + height + 3, k + -3);
        placeLeaf(world, i + 0, j + height + 3, k + -3);
        placeLeaf(world, i + 3, j + height + 3, k + -3);
        placeLeaf(world, i + -2, j + height + 3, k + -2);
        placeLeaf(world, i + 0, j + height + 3, k + -2);
        placeLeaf(world, i + 2, j + height + 3, k + -2);
        placeLeaf(world, i + -1, j + height + 3, k + -1);
        placeLeaf(world, i + 0, j + height + 3, k + -1);
        placeLeaf(world, i + 1, j + height + 3, k + -1);
        placeLeaf(world, i + -5, j + height + 3, k + 0);
        placeLeaf(world, i + -4, j + height + 3, k + 0);
        placeLeaf(world, i + -3, j + height + 3, k + 0);
        placeLeaf(world, i + -2, j + height + 3, k + 0);
        placeLeaf(world, i + -1, j + height + 3, k + 0);
        placeLeaf(world, i + 1, j + height + 3, k + 0);
        placeLeaf(world, i + 2, j + height + 3, k + 0);
        placeLeaf(world, i + 3, j + height + 3, k + 0);
        placeLeaf(world, i + 4, j + height + 3, k + 0);
        placeLeaf(world, i + 5, j + height + 3, k + 0);
        placeLeaf(world, i + -1, j + height + 3, k + 1);
        placeLeaf(world, i + 0, j + height + 3, k + 1);
        placeLeaf(world, i + 1, j + height + 3, k + 1);
        placeLeaf(world, i + -2, j + height + 3, k + 2);
        placeLeaf(world, i + 0, j + height + 3, k + 2);
        placeLeaf(world, i + 2, j + height + 3, k + 2);
        placeLeaf(world, i + -3, j + height + 3, k + 3);
        placeLeaf(world, i + 0, j + height + 3, k + 3);
        placeLeaf(world, i + 3, j + height + 3, k + 3);
        placeLeaf(world, i + -4, j + height + 3, k + 4);
        placeLeaf(world, i + 0, j + height + 3, k + 4);
        placeLeaf(world, i + 4, j + height + 3, k + 4);
        placeLeaf(world, i + 0, j + height + 3, k + 5);

        for (int c = 0; c < 4; c++) {
            spawnCoconuts(world, new BlockPos(i, j + height + 1, k).relative(Direction.from2DDataValue(i)), random, 2, getLeaf());
        }

        return true;
    }
}
