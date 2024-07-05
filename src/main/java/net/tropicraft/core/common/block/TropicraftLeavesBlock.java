package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TropicraftLeavesBlock extends LeavesBlock {
    public static final MapCodec<TropicraftLeavesBlock> CODEC = simpleCodec(TropicraftLeavesBlock::new);

    public TropicraftLeavesBlock(Properties props) {
        super(props);
    }

    @Override
    public MapCodec<TropicraftLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        // ignore decay
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        // ignore decay
    }
}
