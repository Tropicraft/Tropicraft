package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

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
        int height = rand.nextInt(15) + 15;

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

        for (int y = j; y < j + height; y++) {
            placeLog(logs, world, i, y, k);
            placeLog(logs, world, i - 1, y, k);
            placeLog(logs, world, i + 1, y, k);
            placeLog(logs, world, i, y, k - 1);
            placeLog(logs, world, i, y, k + 1);

            if (y - j > height / 2 && rand.nextInt(SMALL_LEAF_CHANCE) == 0) {
                int nx = rand.nextInt(3) - 1 + i;
                int nz = rand.nextInt(3) - 1 + k;

                genCircle(leaves, world, new BlockPos(nx, y + 1, nz), 1, 0, getLeaf(), false);
                genCircle(leaves, world, nx, y, nz, 2, 1, getLeaf(), false);
            }
            if (y - j > height - (height / 4) && y - j < height - 3 && rand.nextInt(SECOND_CANOPY_CHANCE) == 0) {
                int nx = i + rand.nextInt(9) - 4;
                int nz = k + rand.nextInt(9) - 4;

                int leafSize = rand.nextInt(3) + 5;

                genCircle(leaves, world, nx, y + 3, nz, leafSize - 2, 0, getLeaf(), false);
                genCircle(leaves, world, nx, y + 2, nz, leafSize - 1, leafSize - 3, getLeaf(), false);
                genCircle(leaves, world, nx, y + 1, nz, leafSize, leafSize - 1, getLeaf(), false);

                placeBlockLine(logs, world, new int[]{i, y - 2, k}, new int[]{nx, y + 2, nz}, getLog());
            }
        }

        int leafSize = rand.nextInt(5) + 9;

        genCircle(leaves, world, i, j + height, k, leafSize - 2, 0, getLeaf(), false);
        genCircle(leaves, world, i, j + height - 1, k, leafSize - 1, leafSize - 4, getLeaf(), false);
        genCircle(leaves, world, i, j + height - 2, k, leafSize, leafSize - 1, getLeaf(), false);

        for (BlockPos direction : BRANCH_DIRECTIONS) {
            BlockPos endPos;
            if (direction.distManhattan(BlockPos.ZERO) > 1) {
                endPos = new BlockPos(i, j, k).offset(direction.multiply(leafSize / 2));
            } else {
                endPos = new BlockPos(i, j, k).offset(direction.multiply(leafSize - 4));
            }
            endPos = endPos.offset(1 - rand.nextInt(3), 0, 1 - rand.nextInt(3));
            placeBlockLine(logs, world, new int[]{i, j + height - 1, k}, new int[]{endPos.getX(), j + height - 1, endPos.getZ()}, getLog());
        }

        return TropicraftLeavesFixer.updateLeaves(world, logs, leaves);
    }
}
