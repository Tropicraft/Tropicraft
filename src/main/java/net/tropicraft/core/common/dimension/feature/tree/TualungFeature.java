package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;

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

        WorldgenRandom r = new WorldgenRandom(new LegacyRandomSource(world.getSeed()));
        r.setLargeFeatureSeed(world.getSeed(), i >> 4, k >> 4);
        if (r.nextInt(10) == 0) {
            return false;
        }

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

            if (y - j > 4 && rand.nextInt(3) == 0) {
                int nx = rand.nextInt(3) - 1 + i;
                int nz = rand.nextInt(3) - 1 + k;

                genCircle(leaves, world, new BlockPos(nx, y + 1, nz), 1, 0, getLeaf(), false);
                genCircle(leaves, world, nx, y, nz, 2, 1, getLeaf(), false);
            }
        }

        int branches = rand.nextInt(3) + 3;
        double branchDelta = rand.nextDouble() * (Math.TAU / branches);
        for (int b = 0; b < branches; b++) {
            int size = 8 + rand.nextInt(5);
            for (int by = 0; by < size; by++) {
                int dx = (int) (Math.cos(branchDelta) * (by / 1.5));
                int dz = (int) (Math.sin(branchDelta) * (by / 1.5));
                int dy = by - 4;

                BlockPos local = pos.atY(height + dy).offset(dx, 0, dz);
                placeLog(logs, world, local);

                // place leaves
                if (by == size - 1) {
                    genCircle(leaves, world, local.above(), 3, 0, getLeaf(), false);
                    genCircle(leaves, world, local, 4, 3, getLeaf(), false);
                    genCircle(leaves, world, local.below(), 5, 4, getLeaf(), false);
                }
            }

            branchDelta += (Math.TAU / branches);
        }

        double delta = rand.nextDouble() * (Math.TAU / 3);
        for (int v = 0; v < 3; v++) {

            for (int w = 0; w < 3; w++) {
                int dx = (int) (Math.cos(delta) * (w + 2));
                int dz = (int) (Math.sin(delta) * (w + 2));
                int dy = -w;

                placeLog(logs, world, pos.offset(dx, dy, dz));
                placeLog(logs, world, pos.offset(dx + 1, dy, dz));
                placeLog(logs, world, pos.offset(dx, dy, dz + 1));
                placeLog(logs, world, pos.offset(dx - 1, dy, dz));
                placeLog(logs, world, pos.offset(dx, dy, dz - 1));
            }

            delta += (Math.TAU / 3);
        }

        // try place some magic mushrooms or fireflies around to light the darkness under the tree
        BlockState state = TropicraftBlocks.FLOWERS.get(TropicraftFlower.MAGIC_MUSHROOM).getDefaultState();
        if (rand.nextInt(4) == 0) {
            state = Blocks.FIREFLY_BUSH.defaultBlockState();
        }
        for (int m = 0; m < 24; m++) {
            int dx = rand.nextInt(12) - rand.nextInt(12);
            int dz = rand.nextInt(12) - rand.nextInt(12);
            int dy = rand.nextInt(5) - rand.nextInt(5);

            BlockPos local = pos.offset(dx, dy, dz);
            if (world.getBlockState(local).canBeReplaced() && world.getBlockState(local.below()).is(Blocks.GRASS_BLOCK)) {
                world.setBlock(local, state, 3);
            }
        }

        return TropicraftLeavesFixer.updateLeaves(world, logs, leaves, getLeaf());
    }

    private int sign(int i) {
        return i == 0 ? 0 : i <= 0 ? -1 : 1;
    }
}
