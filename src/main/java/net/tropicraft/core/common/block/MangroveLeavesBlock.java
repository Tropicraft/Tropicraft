package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class MangroveLeavesBlock extends LeavesBlock {
    private static final int PROPAGULE_GROW_CHANCE = 80;
    public static final int SPACING = 2;

    private final Supplier<PropaguleBlock> propaguleBlock;

    public MangroveLeavesBlock(Properties props, Supplier<PropaguleBlock> propaguleBlock) {
        super(props);
        this.propaguleBlock = propaguleBlock;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        if (random.nextInt(PROPAGULE_GROW_CHANCE) == 0) {
            this.tryGrowPropagule(state, world, pos);
        }
    }

    private void tryGrowPropagule(BlockState state, ServerWorld world, BlockPos pos) {
        if (state.get(DISTANCE) > 3) {
            return;
        }

        BlockPos growPos = pos.down();
        if (world.isAirBlock(growPos) && world.isAirBlock(growPos.down())) {
            if (this.hasNearPropagule(world, pos)) {
                return;
            }

            world.setBlockState(growPos, this.propaguleBlock.get().getDefaultState().with(PropaguleBlock.PLANTED, false));
        }
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
