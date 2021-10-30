package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class CoffeeBushBlock extends CropsBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);

    /** Number of bushes high this plant can grow */
    public static final int MAX_HEIGHT = 3;

    /** The growth rate when this plant is fertile */
    public static final int GROWTH_RATE_FERTILE = 10;

    /** The growth rate when this plant is infertile */
    public static final int GROWTH_RATE_INFERTILE = 20;

    public CoffeeBushBlock(Properties properties) {
        super(properties);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 6;
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return TropicraftItems.RAW_COFFEE_BEAN.get();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        // Try to grow up
        if (worldIn.isEmptyBlock(pos.above())) {
            int height;
            BlockPos ground = pos;
            for (height = 1; worldIn.getBlockState(ground = ground.below()).getBlock() == this; ++height);

            final BlockState blockState = worldIn.getBlockState(ground);
            if (height < MAX_HEIGHT && worldIn.random.nextInt(blockState.getBlock().isFertile(blockState, worldIn, ground) ? GROWTH_RATE_FERTILE : GROWTH_RATE_INFERTILE) == 0) {
                worldIn.setBlockAndUpdate(pos.above(), defaultBlockState());
            }
        }

        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (state.getValue(AGE) == getMaxAge()) {
            world.setBlockAndUpdate(pos, state.setValue(AGE, 0));
            final int count = 1 + player.getRandom().nextInt(3);
            ItemStack stack = new ItemStack(TropicraftItems.RAW_COFFEE_BEAN.get(), count);
            popResource(world, pos, stack);
            return world.isClientSide ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.block();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.GRASS_BLOCK || net.minecraftforge.common.Tags.Blocks.DIRT.contains(this) || state.getBlock() == Blocks.FARMLAND || state.getBlock() == this;
    }
}
