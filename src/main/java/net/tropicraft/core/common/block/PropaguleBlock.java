package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.TropicraftTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public final class PropaguleBlock extends WaterloggableSaplingBlock {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    private static final int GROW_CHANCE = 7;

    public static final BooleanProperty PLANTED = BooleanProperty.create("planted");

    public PropaguleBlock(Tree tree, Properties properties) {
        super(tree, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, 0).with(WATERLOGGED, false).with(PLANTED, true));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".desc").mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        if (state.get(PLANTED)) {
            BlockPos groundPos = pos.down();
            return this.isValidGround(world.getBlockState(groundPos), world, groundPos);
        } else {
            BlockPos topPos = pos.up();
            return world.getBlockState(topPos).isIn(BlockTags.LEAVES);
        }
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return super.isValidGround(state, world, pos) || state.isIn(BlockTags.SAND)
                || state.isIn(TropicraftTags.Blocks.MUD);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(PLANTED);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isAreaLoaded(pos, 1)) return;

        if (world.getLight(pos.up()) >= 9 && random.nextInt(GROW_CHANCE) == 0) {
            this.placeTree(world, pos, state, random);
        }
    }

    @Override
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(PLANTED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .with(PLANTED, context.getFace() != Direction.DOWN);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PLANTED);
    }
}
