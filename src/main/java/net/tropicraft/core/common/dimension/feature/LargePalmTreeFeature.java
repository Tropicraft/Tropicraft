package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class LargePalmTreeFeature extends PalmTreeFeature {
    public LargePalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, boolean doesNotifyOnPlace) {
        super(func, doesNotifyOnPlace);
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox bb) {
        pos = pos.toImmutable();

        int height = rand.nextInt(7) + 7;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!isSand(world, pos.down(), getSapling())) {
            return false;
        }

        setState(changedBlocks, world, pos.down(), TropicraftBlocks.PURIFIED_SAND.get().getDefaultState(), bb);

        // Place trunk
        for (int y = 0; y <= height; y++) {
            setState(changedBlocks, world, pos.up(y), getLog(), bb);
        }
        
        final int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        // Wheeee, auto-generated code!
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 1, k + -7);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 1, k + -6);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 1, k + -6);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 1, k + -5);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 1, k + -5);
        placeLeaf(changedBlocks, world, bb, i + -6, j + height + 1, k + -1);
        placeLog(changedBlocks, world, bb, i + 0, j + height + 1, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 6, j + height + 1, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -7, j + height + 1, k + 0);
        placeLog(changedBlocks, world, bb, i + -1, j + height + 1, k + 0);
        placeLog(changedBlocks, world, bb, i + 0, j + height + 1, k + 0);
        placeLog(changedBlocks, world, bb, i + 1, j + height + 1, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 7, j + height + 1, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -6, j + height + 1, k + 1);
        placeLog(changedBlocks, world, bb, i + 0, j + height + 1, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 6, j + height + 1, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 1, k + 5);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 1, k + 5);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 1, k + 6);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 1, k + 6);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 1, k + 7);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 2, k + -6);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 2, k + -5);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + -5);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + -5);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 2, k + -5);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 2, k + -4);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 2, k + -4);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + -4);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + -4);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 2, k + -4);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 2, k + -4);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 2, k + -3);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 2, k + -3);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + -3);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + -3);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 2, k + -3);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 2, k + -3);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 2, k + -2);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + -2);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + -2);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 2, k + -2);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 2, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -6, j + height + 2, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 2, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 6, j + height + 2, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 2, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 2, k + 2);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + 2);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + 2);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 2, k + 2);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 2, k + 3);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 2, k + 3);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + 3);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + 3);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 2, k + 3);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 2, k + 3);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 2, k + 4);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 2, k + 4);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 2, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 2, k + 4);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 2, k + 5);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 2, k + 5);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 2, k + 5);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 2, k + 5);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 2, k + 6);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + -5);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 3, k + -4);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + -4);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 3, k + -4);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 3, k + -3);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + -3);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 3, k + -3);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 3, k + -2);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + -2);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 3, k + -2);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 3, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + -1);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 3, k + -1);
        placeLeaf(changedBlocks, world, bb, i + -5, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + 5, j + height + 3, k + 0);
        placeLeaf(changedBlocks, world, bb, i + -1, j + height + 3, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 3, k + 1);
        placeLeaf(changedBlocks, world, bb, i + -2, j + height + 3, k + 2);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + 2);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 3, k + 2);
        placeLeaf(changedBlocks, world, bb, i + -3, j + height + 3, k + 3);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + 3);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 3, k + 3);
        placeLeaf(changedBlocks, world, bb, i + -4, j + height + 3, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height + 3, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 0, j + height + 3, k + 5);

        for (int y = height - 4; y < height - 1; y++) {
            TropicraftFeatureUtil.spawnCoconuts(world,  new BlockPos(i, j + y, k), rand, 2, getLeaf());
        }

        return true;
    }
}
