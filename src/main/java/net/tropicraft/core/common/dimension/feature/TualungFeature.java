package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

// TODO use TualungConfig, but requires extending Feature instead, which is a pain
public class TualungFeature extends RainforestTreeFeature {

    private int baseHeight;
    private int maxHeight;

    public TualungFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, boolean doesNotifyOnPlace, int maxHeight, int baseHeight) {
        super(func, doesNotifyOnPlace);
        this.baseHeight = baseHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox bb) {
        pos = pos.toImmutable();
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        int height = rand.nextInt(maxHeight - baseHeight) + baseHeight + j;
        int branches = rand.nextInt(3) + 3;

        if (goesBeyondWorldSize(world, pos.getY(), height - j)) {
            return false;
        }

        if (height + 6 > 256) {
            return false;
        }

        if (!isBBAvailable(world, pos, height - j)) {
            return false;
        }

        if (!isSoil(world, pos.down(), getSapling())) {
            return false;
        }

        setState(changedBlocks, world, new BlockPos(i, j, k), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i - 1, j, k), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i + 1, j, k), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i, j, k - 1), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i, j, k + 1), Blocks.DIRT.getDefaultState(), bb);

        for (int y = j; y < height; y++) {
            placeLog(changedBlocks, world, bb, i, y, k);
            placeLog(changedBlocks, world, bb, i - 1, y, k);
            placeLog(changedBlocks, world, bb, i + 1, y, k);
            placeLog(changedBlocks, world, bb, i, y, k - 1);
            placeLog(changedBlocks, world, bb, i, y, k + 1);
        }

        for (int x = 0; x < branches; x++) {
            int branchHeight = rand.nextInt(4) + 2 + height;
            int bx = rand.nextInt(15) - 8 + i;
            int bz = rand.nextInt(15) - 8 + k;

            placeBlockLine(changedBlocks, world, bb, new int[] { i + sign((bx - i) / 2), height, k + sign((bz - k) / 2) }, new int[] { bx, branchHeight, bz }, getLog());

            genCircle(world, bx, branchHeight, bz, 2, 1, getLeaf(), false);
            genCircle(world, bx, branchHeight + 1, bz, 3, 2, getLeaf(), false);
        }

        return true;
    }

    private int sign(int i) {
        return i == 0 ? 0 : i <= 0 ? -1 : 1;
    }
}
