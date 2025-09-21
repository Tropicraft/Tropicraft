package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class CustomSeagrassBlock extends SeagrassBlock {

    @Nullable
    private final Supplier<? extends TallSeagrassBlock> tall;

    public CustomSeagrassBlock(Properties properties, @Nullable Supplier<? extends TallSeagrassBlock> tall) {
        super(properties);
        this.tall = tall;
    }

    @Override
    public MapCodec<SeagrassBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (tall == null) {
            return;
        }

        BlockState bottomState = tall.get().defaultBlockState();
        BlockState topState = bottomState.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);

        BlockPos topPos = pos.above();
        if (level.getBlockState(topPos).is(Blocks.WATER)) {
            level.setBlock(pos, bottomState, Block.UPDATE_CLIENTS);
            level.setBlock(topPos, topState, Block.UPDATE_CLIENTS);
        }
    }
}
