package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class UpTreeFeature extends RainforestTreeFeature {

    public UpTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, boolean doesNotifyOnPlace) {
        super(func, doesNotifyOnPlace);
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox bb) {
        pos = pos.toImmutable();
        final int height = rand.nextInt(4) + 6;
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!isSoil(world, pos.down(), getSapling())) {
            return false;
        }

        for (int y = j; y < j + height; y++) {
            placeLog(changedBlocks, world, bb, i, y, k);
            if (rand.nextInt(5) == 0) {
                int x = rand.nextInt(3) - 1 + i;
                int z = k;
                if (x - i == 0) {
                    z += rand.nextBoolean() ? 1 : -1;
                }
                placeLeaf(changedBlocks, world, bb, x, y, z);
            }

            if (y == j + height - 1) {
                placeLog(changedBlocks, world, bb, i + 1, y, k);
                placeLog(changedBlocks, world, bb, i - 1, y, k);
                placeLog(changedBlocks, world, bb, i, y, k + 1);
                placeLog(changedBlocks, world, bb, i, y, k - 1);
            }
        }

        final int radius = rand.nextInt(2) + 3;

        genCircle(world, i, j + height, k, radius, 0, getLeaf(), false);
        genCircle(world, i, j + height + 1, k, radius + 2, radius, getLeaf(), false);
        genCircle(world, i, j + height + 2, k, radius + 3, radius + 2, getLeaf(), false);

        return true;
    }
}
