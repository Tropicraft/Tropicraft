package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class MangroveLeavesBlock extends LeavesBlock {
    private static final int PROPAGULE_GROW_CHANCE = 100;
    private static final int SPACING = 2;

    private final Supplier<PropaguleBlock> propaguleBlock;

    public MangroveLeavesBlock(Properties props, Supplier<PropaguleBlock> propaguleBlock) {
        super(props);
        this.propaguleBlock = propaguleBlock;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return this.canGrowPropagules(state) || super.ticksRandomly(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        if (this.canGrowPropagules(state) && random.nextInt(PROPAGULE_GROW_CHANCE) == 0) {
            this.tryGrowPropagule(world, pos);
        }
    }

    private void tryGrowPropagule(ServerWorld world, BlockPos pos) {
        BlockPos growPos = pos.down();
        if (world.isAirBlock(growPos) && world.isAirBlock(growPos.down()) && !this.hasNearPropagule(world, pos)) {
            BlockState propagule = this.propaguleBlock.get().getDefaultState().with(PropaguleBlock.PLANTED, false);
            world.setBlockState(growPos, propagule);
        }
    }

    private boolean canGrowPropagules(BlockState state) {
        return state.get(DISTANCE) <= 3;
    }

    private boolean hasNearPropagule(ServerWorld world, BlockPos source) {
        PropaguleBlock propagule = this.propaguleBlock.get();
        for (BlockPos pos : BlockPos.getAllInBoxMutable(source.add(-SPACING, -SPACING, -SPACING), source.add(SPACING, 0, SPACING))) {
            if (world.getBlockState(pos).matchesBlock(propagule)) {
                return true;
            }
        }
        return false;
    }
}
