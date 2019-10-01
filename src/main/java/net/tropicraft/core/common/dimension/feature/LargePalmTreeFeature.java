package net.tropicraft.core.common.dimension.feature;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

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

        if (!isSoil(world, pos.down(), getSapling())) {
            return false;
        }

        // Place trunk
        for (int y = 0; y <= height; y++) {
            placeLog(changedBlocks, world, bb, pos.getX(), pos.getY() + y, pos.getZ());
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

        for (int c = 0; c < 4; c++) {
            spawnCoconuts(world, new BlockPos(i, j + height + 1, k).offset(Direction.byHorizontalIndex(i)), rand, 2, getLeaf());
        }

        return true;
    }
}
