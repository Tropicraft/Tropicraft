package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;
import java.util.function.Supplier;

public class MangroveLeavesBlock extends LeavesBlock {
    private static final int PROPAGULE_GROW_CHANCE = 200;
    private static final int SPACING = 2;

    private final Supplier<PropaguleBlock> propaguleBlock;

    public MangroveLeavesBlock(Properties props, Supplier<PropaguleBlock> propaguleBlock) {
        super(props);
        this.propaguleBlock = propaguleBlock;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return this.canGrowPropagules(state) || super.isRandomlyTicking(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        if (this.canGrowPropagules(state) && random.nextInt(PROPAGULE_GROW_CHANCE) == 0) {
            this.tryGrowPropagule(world, pos);
        }
    }

    private void tryGrowPropagule(ServerLevel world, BlockPos pos) {
        BlockPos growPos = pos.below();
        if (world.isEmptyBlock(growPos) && world.isEmptyBlock(growPos.below()) && !this.hasNearPropagule(world, pos)) {
            BlockState propagule = this.propaguleBlock.get().defaultBlockState().setValue(PropaguleBlock.PLANTED, false);
            world.setBlockAndUpdate(growPos, propagule);
        }
    }

    private boolean canGrowPropagules(BlockState state) {
        return state.getValue(DISTANCE) <= 3;
    }

    private boolean hasNearPropagule(ServerLevel world, BlockPos source) {
        PropaguleBlock propagule = this.propaguleBlock.get();
        for (BlockPos pos : BlockPos.betweenClosed(source.offset(-SPACING, -SPACING, -SPACING), source.offset(SPACING, 0, SPACING))) {
            if (world.getBlockState(pos).is(propagule)) {
                return true;
            }
        }
        return false;
    }
}
