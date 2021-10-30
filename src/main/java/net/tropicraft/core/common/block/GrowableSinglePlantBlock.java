package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.RegistryObject;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public final class GrowableSinglePlantBlock extends BushBlock implements IGrowable {
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    private final Supplier<RegistryObject<DoublePlantBlock>> growInto;

    public GrowableSinglePlantBlock(Properties properties, Supplier<RegistryObject<DoublePlantBlock>> growInto) {
        super(properties);
        this.growInto = growInto;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        DoublePlantBlock growBlock = this.growInto.get().get();
        BlockState growState = growBlock.defaultBlockState();
        if (growState.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
            growBlock.placeAt(world, pos, Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public AbstractBlock.OffsetType getOffsetType() {
        return AbstractBlock.OffsetType.XZ;
    }
}
