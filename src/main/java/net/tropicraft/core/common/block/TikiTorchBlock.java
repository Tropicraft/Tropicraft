package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Locale;

public final class TikiTorchBlock extends Block {
    public static final MapCodec<TikiTorchBlock> CODEC = simpleCodec(TikiTorchBlock::new);

    public enum TorchSection implements StringRepresentable {
        UPPER(2), MIDDLE(1), LOWER(0);

        final int height;

        TorchSection(int height) {
            this.height = height;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return getSerializedName();
        }
    }

    public static final EnumProperty<TorchSection> SECTION = EnumProperty.create("section", TorchSection.class);

    protected static final VoxelShape BASE_SHAPE = Shapes.create(new AABB(0.4, 0.0, 0.4, 0.6, 0.999999, 0.6));
    protected static final VoxelShape TOP_SHAPE = Shapes.create(new AABB(0.4, 0.0, 0.4, 0.6, 0.6, 0.6));

    public TikiTorchBlock(Block.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(SECTION, TorchSection.UPPER));
    }

    @Override
    protected MapCodec<TikiTorchBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SECTION);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        TorchSection section = state.getValue(SECTION);

        if (section == TorchSection.UPPER) {
            return TOP_SHAPE;
        } else {
            return BASE_SHAPE;
        }
    }

    @Override
    @Deprecated
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (canSupportCenter(world, pos.below(), Direction.UP)) { // can block underneath support torch
            return true;
        } else { // if not, is the block underneath a lower 2/3 tiki torch segment?
            BlockState blockstate = world.getBlockState(pos.below());
            return (blockstate.getBlock() == this && blockstate.getValue(SECTION) != TorchSection.UPPER) && super.canSurvive(state, world, pos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        if (placeShortTorchOn(context.getLevel().getBlockState(blockpos.below()))) {
            return defaultBlockState().setValue(SECTION, TorchSection.UPPER);
        }
        BlockState ret = defaultBlockState().setValue(SECTION, TorchSection.LOWER);
        return blockpos.getY() < context.getLevel().getMaxBuildHeight() - 1 &&
                context.getLevel().getBlockState(blockpos.above()).canBeReplaced(context) &&
                context.getLevel().getBlockState(blockpos.above(2)).canBeReplaced(context) ? ret : null;
    }

    @Override
    @Deprecated
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis() == Axis.Y && !canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TorchSection section = state.getValue(SECTION);

        if (section == TorchSection.UPPER) return;

        worldIn.setBlock(pos.above(), defaultBlockState().setValue(SECTION, TorchSection.MIDDLE), Block.UPDATE_ALL);
        worldIn.setBlock(pos.above(2), defaultBlockState().setValue(SECTION, TorchSection.UPPER), Block.UPDATE_ALL);
    }

    private boolean placeShortTorchOn(BlockState state) {
        // Only place top block if it's on a fence/wall
        return state.is(BlockTags.FENCES) || state.is(BlockTags.WALLS);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        TorchSection section = state.getValue(SECTION);
        BlockPos base = pos.below(section.height);
        for (TorchSection otherSection : TorchSection.values()) {
            BlockPos pos2 = base.above(otherSection.height);
            BlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() == this && state2.getValue(SECTION) == otherSection) {
                super.playerDestroy(world, player, pos2, state2, te, stack);
                world.setBlock(pos2, world.getFluidState(pos2).createLegacyBlock(), world.isClientSide ? 11 : 3);
            }
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        boolean ret = false;
        TorchSection section = state.getValue(SECTION);
        BlockPos base = pos.below(section.height);
        for (TorchSection otherSection : TorchSection.values()) {
            BlockPos pos2 = base.above(otherSection.height);
            BlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() == this && state2.getValue(SECTION) == otherSection) {
                if (player.isCreative()) {
                    ret |= super.onDestroyedByPlayer(state2, world, pos2, player, willHarvest, fluid);
                } else {
                    playerWillDestroy(world, pos2, state2, player);
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        boolean isTop = state.getValue(SECTION) == TorchSection.UPPER;
        if (isTop) {
            double d = pos.getX() + 0.5f;
            double d1 = pos.getY() + 0.7f;
            double d2 = pos.getZ() + 0.5f;

            world.addParticle(ParticleTypes.SMOKE, d, d1, d2, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.FLAME, d, d1, d2, 0.0, 0.0, 0.0);
        }
    }
}
