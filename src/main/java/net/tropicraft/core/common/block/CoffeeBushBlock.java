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
    protected IItemProvider getSeedsItem() {
        return TropicraftItems.RAW_COFFEE_BEAN.get();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        // Try to grow up
        if (worldIn.isAirBlock(pos.up())) {
            int height;
            BlockPos ground = pos;
            for (height = 1; worldIn.getBlockState(ground = ground.down()).getBlock() == this; ++height);

            final BlockState blockState = worldIn.getBlockState(ground);
            if (height < MAX_HEIGHT && worldIn.rand.nextInt(blockState.getBlock().isFertile(blockState, worldIn, ground) ? GROWTH_RATE_FERTILE : GROWTH_RATE_INFERTILE) == 0) {
                worldIn.setBlockState(pos.up(), getDefaultState());
            }
        }

        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (state.get(AGE) == getMaxAge()) {
            world.setBlockState(pos, state.with(AGE, 0));
            final int count = 1 + player.getRNG().nextInt(3);
            ItemStack stack = new ItemStack(TropicraftItems.RAW_COFFEE_BEAN.get(), count);
            spawnAsEntity(world, pos, stack);
            return world.isRemote ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.GRASS_BLOCK || net.minecraftforge.common.Tags.Blocks.DIRT.contains(this) || state.getBlock() == Blocks.FARMLAND || state.getBlock() == this;
    }
}
