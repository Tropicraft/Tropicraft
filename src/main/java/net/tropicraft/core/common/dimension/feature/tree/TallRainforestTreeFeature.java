package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;

import java.util.List;
import java.util.Set;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class TallRainforestTreeFeature extends RainforestTreeFeature {
    public static final List<BlockPos> BRANCH_DIRECTIONS = BlockPos.betweenClosedStream(-1, 0, -1, 1, 0, 1)
            .map(BlockPos::immutable)
            .filter(blockPos -> !blockPos.equals(BlockPos.ZERO))
            .toList();

    private static final int VINE_CHANCE = 5;
    private static final int SMALL_LEAF_CHANCE = 3;
    private static final int SECOND_CANOPY_CHANCE = 3;

    public TallRainforestTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    private boolean isSoil(LevelAccessor world, BlockPos pos) {
        return getSapling().defaultBlockState().canSurvive(world, pos);
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
        int height = rand.nextInt(10) + 25;

        // TODO: placement!
        WorldgenRandom r = new WorldgenRandom(new LegacyRandomSource(world.getSeed()));
        r.setLargeFeatureSeed(world.getSeed(), i >> 4, k >> 4);
        if (r.nextInt(10) == 0) {
            return false;
        }

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!isSoil(world, pos.below())) {
            return false;
        }

        if (!isSoil(world, pos.east().below())) {
            return false;
        }

        if (!isSoil(world, pos.west().below())) {
            return false;
        }

        if (!isSoil(world, pos.north().below())) {
            return false;
        }

        if (!isSoil(world, pos.south().below())) {
            return false;
        }

        Set<BlockPos> logs = Sets.newHashSet();
        Set<BlockPos> leaves = Sets.newHashSet();

        setState(null, world, new BlockPos(i, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i - 1, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i + 1, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i, j - 1, k - 1), Blocks.DIRT.defaultBlockState());
        setState(null, world, new BlockPos(i, j - 1, k + 1), Blocks.DIRT.defaultBlockState());

        double branchDelta = rand.nextDouble() * (Math.TAU / 3);
        for (int y = j; y < j + height; y++) {
            placeLog(logs, world, i, y, k);
            placeLog(logs, world, i - 1, y, k);
            placeLog(logs, world, i + 1, y, k);
            placeLog(logs, world, i, y, k - 1);
            placeLog(logs, world, i, y, k + 1);

            // Generate small branch
            if (y > j + 8 && y < (j + height - 6) && (y % 3) == 0 && rand.nextInt(3) > 0) {
                // Make branches larger as we progress up the tree
                int baseBranchSize = (int) Mth.clampedMap(y, j, j + height, 4, 8);
                int branchLen = baseBranchSize + rand.nextInt(3);
                for (int by = 0; by < branchLen; by++) {
                    int dx = (int) (Math.cos(branchDelta) * (by));
                    int dz = (int) (Math.sin(branchDelta) * (by));
                    int dy = by / 2;

                    BlockPos local = pos.atY(y + dy).offset(dx, 0, dz);
                    placeLog(logs, world, local);

                    if (by == branchLen - 1) {
                        genCircle(leaves, world, local.above(), 3, 0, getLeaf(), false);
                        genCircle(leaves, world, local, 4, 3, getLeaf(), false);
                        genCircle(leaves, world, local.below(), 5, 4, getLeaf(), false);
                    }
                }

                branchDelta += (Math.TAU / 3) + ((rand.nextDouble() - rand.nextDouble()) * (Math.TAU / 12));
            }

            if (y - j > 4 && rand.nextInt(SMALL_LEAF_CHANCE) == 0) {
                int nx = rand.nextInt(3) - 1 + i;
                int nz = rand.nextInt(3) - 1 + k;

                genCircle(leaves, world, new BlockPos(nx, y + 1, nz), 1, 0, getLeaf(), false);
                genCircle(leaves, world, nx, y, nz, 2, 1, getLeaf(), false);
            }
        }

        int branchSize = rand.nextInt(3) + 6;


        double topBranchDelta = rand.nextDouble() * (Math.TAU / 6);
        for (int v = 0; v < 6; v++) {
            int branchHeight = 3 + (rand.nextInt(3) - rand.nextInt(3));
            int branchMod = branchSize / branchHeight;
            for (int w = 0; w < branchSize; w++) {
                int dx = (int) (Math.cos(topBranchDelta) * (w + 2));
                int dz = (int) (Math.sin(topBranchDelta) * (w + 2));
                int dy = w / branchMod;

                BlockPos local = pos.atY(j + height + dy).offset(dx, 0, dz);
                placeLog(logs, world, local);

                if (w == branchSize - 1) {
                    int canopySize = 4 + rand.nextInt(3);
                    genCircle(leaves, world, local.above(), canopySize, 0, getLeaf(), false);
                    genCircle(leaves, world, local, canopySize + 1, canopySize, getLeaf(), false);
                    genCircle(leaves, world, local.below(), canopySize + 2, canopySize + 1, getLeaf(), false);
                }
            }

            topBranchDelta += (Math.TAU / 6);
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
}
