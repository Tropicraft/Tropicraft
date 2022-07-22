package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;

public class TropicraftFeatureUtil {

    public static boolean goesBeyondWorldSize(final WorldGenLevel world, final int y, final int height) {
        return y < 1 || y + height + 1 > world.getHeight();
    }

    public static boolean isBBAvailable(final WorldGenLevel world, final BlockPos pos, final int height) {
        for (int y = 0; y <= 1 + height; y++) {
            BlockPos checkPos = pos.above(y);
            int size = 1;
            if (checkPos.getY() < 0 || checkPos.getY() >= world.getHeight()) {
                return false;
            }

            if (y == 0) {
                size = 0;
            }

            if (y >= (1 + height) - 2) {
                size = 2;
            }

            if (BlockPos.betweenClosedStream(checkPos.offset(-size, 0, -size), checkPos.offset(size, 0, size)).anyMatch(p -> !TreeFeature.isAirOrLeaves(world, p))) {
                return false;
            }
        }

        return true;
    }
    
    public static boolean isSoil(final LevelAccessor world, final BlockPos pos) {
        final BlockState blockState = world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.PODZOL;
    }
}
