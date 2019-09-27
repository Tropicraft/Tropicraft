package net.tropicraft.core.common.dimension.feature;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

public class TropicraftFeatureUtil {

    public static boolean goesBeyondWorldSize(final IWorldGenerationReader world, final int y, final int height) {
        return y < 1 || y + height + 1 > world.getMaxHeight();
    }

    public static boolean isBBAvailable(final IWorldGenerationReader world, final BlockPos pos, final int height) {
        for (int y = 0; y <= 1 + height; y++) {
            BlockPos checkPos = pos.up(y);
            int size = 1;
            if (checkPos.getY() < 0 || checkPos.getY() >= world.getMaxHeight()) {
                return false;
            }

            if (y == 0) {
                size = 0;
            }

            if (y >= (1 + height) - 2) {
                size = 2;
            }

            if (BlockPos.getAllInBox(checkPos.add(-size, 0, -size), checkPos.add(size, 0, size))
                    .filter(p -> !isAirOrLeaves(world, p))
                    .count() > 0) {
                return false;
            }
        }

        return true;
    }

    protected static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (!(worldIn instanceof net.minecraft.world.IWorldReader)) // FORGE: Redirect to state method when possible
            return worldIn.hasBlockState(pos, (p_214581_0_) -> {
                return p_214581_0_.isAir() || p_214581_0_.isIn(BlockTags.LEAVES);
            });
        else return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((net.minecraft.world.IWorldReader)worldIn, pos));
    }
    
    protected static boolean isSoil(final IWorld world, final BlockPos pos) {
        return world.getBlockState(pos).isIn(BlockTags.DIRT_LIKE);
    }

    public static void spawnCoconuts(IWorldWriter world, BlockPos pos, Random random, int chance, final BlockState LEAF_STATE) {
        final BlockState coconut = TropicraftBlocks.COCONUT.get().getDefaultState();

        if (random.nextInt(chance) == 0) {
            world.setBlockState(pos.east(), coconut, 3);
        }

        if (random.nextInt(chance) == 0) {
            world.setBlockState(pos.west(), coconut, 3);
        }

        if (random.nextInt(chance) == 0) {
            world.setBlockState(pos.north(), coconut, 3);
        }

        if (random.nextInt(chance) == 0) {
            world.setBlockState(pos.south(), coconut, 3);
        }
    }
}
