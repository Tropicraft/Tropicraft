package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.Tags;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public final class ReedsBlock extends Block implements IWaterLoggable, IPlantable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public ReedsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, Type.SINGLE).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        FluidState fluid = world.getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(TYPE, this.getAppropriateTypeAt(world, context.getClickedPos()))
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (!state.canSurvive(world, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (state.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return state.setValue(TYPE, this.getAppropriateTypeAt(world, currentPos));
    }

    private Type getAppropriateTypeAt(IWorld world, BlockPos pos) {
        if (world.getBlockState(pos.above()).is(this)) {
            return Type.BOTTOM;
        }
        return world.getBlockState(pos.below()).is(this) ? Type.TOP : Type.SINGLE;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos groundPos = pos.below();
        BlockState growOn = world.getBlockState(groundPos);
        if (growOn.canSustainPlant(world, groundPos, Direction.UP, this)) {
            return true;
        }

        return growOn.getBlock() == this || this.canGrowOn(growOn);
    }

    private boolean canGrowOn(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK)
                || state.is(BlockTags.SAND) || state.is(Tags.Blocks.DIRT) || state.is(Tags.Blocks.GRAVEL)
                || state.is(Blocks.CLAY);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TYPE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return this.defaultBlockState();
    }

    public enum Type implements IStringSerializable {
        SINGLE("single", "reeds_top_short"),
        BOTTOM("bottom", "reeds_bottom"),
        TOP("top", "reeds_top_tall", "reeds_top_short");

        private final String key;
        private final String[] textures;

        Type(String key, String... textures) {
            this.key = key;
            this.textures = textures;
        }

        public String[] getTextures() {
            return this.textures;
        }

        @Override
        public String getSerializedName() {
            return this.key;
        }
    }
}
