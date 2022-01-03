package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Random;

public class CoffeeBushBlock extends CropBlock {

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
    protected ItemLike getBaseSeedId() {
        return TropicraftItems.RAW_COFFEE_BEAN.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (state.getValue(AGE) == getMaxAge()) {
            world.setBlockAndUpdate(pos, state.setValue(AGE, 0));
            final int count = 1 + player.getRandom().nextInt(3);
            ItemStack stack = new ItemStack(TropicraftItems.RAW_COFFEE_BEAN.get(), count);
            popResource(world, pos, stack);
            return world.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.GRASS_BLOCK || net.minecraftforge.common.Tags.Blocks.DIRT.contains(this) || state.getBlock() == Blocks.FARMLAND || state.getBlock() == this;
    }
}
