package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Set;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

// TODO use TualungConfig, but requires extending Feature instead, which is a pain
public class TualungFeature extends RainforestTreeFeature {

    private final int baseHeight;
    private final int maxHeight;

    public TualungFeature(Codec<NoneFeatureConfiguration> codec, int maxHeight, int baseHeight) {
        super(codec);
        this.baseHeight = baseHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        BlockPos pos = context.origin();

        pos = pos.immutable();
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        int height = rand.nextInt(maxHeight - baseHeight) + baseHeight + j;
        int branches = rand.nextInt(3) + 3;

        if (goesBeyondWorldSize(world, pos.getY(), height - j)) {
            return false;
        }

        if (height + 6 > world.getMaxY()) {
            return false;
        }

        if (!isBBAvailable(world, pos, height - j)) {
            return false;
        }

        if (!getSapling().defaultBlockState().canSurvive(world, pos)) {
            return false;
        }

        Set<BlockPos> logs = Sets.newHashSet();
        Set<BlockPos> leaves = Sets.newHashSet();

        setState(null, world, new BlockPos(i, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i - 1, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i + 1, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i, j - 1, k - 1), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i, j - 1, k + 1), Blocks.DIRT.defaultBlockState());

        for (int y = j; y < height; y++) {
            placeLog(logs, world, i, y, k);
            placeLog(logs, world, i - 1, y, k);
            placeLog(logs, world, i + 1, y, k);
            placeLog(logs, world, i, y, k - 1);
            placeLog(logs, world, i, y, k + 1);
        }

        for (int x = 0; x < branches; x++) {
            int branchHeight = rand.nextInt(4) + 2 + height;
            int bx = rand.nextInt(15) - 8 + i;
            int bz = rand.nextInt(15) - 8 + k;

            placeBlockLine(logs, world, new int[]{i + sign((bx - i) / 2), height, k + sign((bz - k) / 2)}, new int[]{bx, branchHeight, bz}, getLog());

            genCircle(leaves, world, bx, branchHeight, bz, 2, 1, getLeaf(), false);
            genCircle(leaves, world, bx, branchHeight + 1, bz, 3, 2, getLeaf(), false);
        }

        return TropicraftLeavesFixer.updateLeaves(world, logs, leaves, getLeaf());
    }

    private int sign(int i) {
        return i == 0 ? 0 : i <= 0 ? -1 : 1;
    }
}
