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

import net.minecraft.block.AbstractBlock.Properties;

public final class PropaguleBlock extends WaterloggableSaplingBlock {
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    private static final int GROW_CHANCE = 7;

    public static final BooleanProperty PLANTED = BooleanProperty.create("planted");

    public PropaguleBlock(Tree tree, Properties properties) {
        super(tree, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0).setValue(WATERLOGGED, false).setValue(PLANTED, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent(getDescriptionId() + ".desc").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        if (state.getValue(PLANTED)) {
            BlockPos groundPos = pos.below();
            return this.mayPlaceOn(world.getBlockState(groundPos), world, groundPos);
        } else {
            BlockPos topPos = pos.above();
            return world.getBlockState(topPos).is(BlockTags.LEAVES);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, IBlockReader world, BlockPos pos) {
        return super.mayPlaceOn(state, world, pos) || state.is(BlockTags.SAND)
                || state.is(TropicraftTags.Blocks.MUD);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(PLANTED);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isAreaLoaded(pos, 1)) return;

        if (world.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(GROW_CHANCE) == 0) {
            this.advanceTree(world, pos, state, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(PLANTED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .setValue(PLANTED, context.getClickedFace() != Direction.DOWN);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PLANTED);
    }
}
