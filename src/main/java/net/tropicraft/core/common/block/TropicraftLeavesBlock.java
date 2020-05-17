package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TropicraftLeavesBlock extends LeavesBlock {
    public TropicraftLeavesBlock(Properties props) {
        super(props);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        // ignore decay
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        // ignore decay
    }
}
