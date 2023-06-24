package net.tropicraft.core.common.block;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public final class GrowableSinglePlantBlock extends BushBlock implements BonemealableBlock {
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    private final Supplier<RegistryEntry<GrowableDoublePlantBlock>> growInto;

    public GrowableSinglePlantBlock(Properties properties, Supplier<RegistryEntry<GrowableDoublePlantBlock>> growInto) {
        super(properties);
        this.growInto = growInto;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        DoublePlantBlock growBlock = this.growInto.get().get();
        BlockState growState = growBlock.defaultBlockState();
        if (growState.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
            DoublePlantBlock.placeAt(world, growState, pos, Block.UPDATE_CLIENTS);
        }
    }
}
