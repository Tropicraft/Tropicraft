package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class TikiTorchBlock extends Block {

    public enum TorchSection implements StringRepresentable {
        UPPER(2), MIDDLE(1), LOWER(0);
        
        final int height;
        
        private TorchSection(int height) {
            this.height = height;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return this.getSerializedName();
        }
    };

    public static final EnumProperty<TorchSection> SECTION = EnumProperty.create("section", TorchSection.class);

    protected static final VoxelShape BASE_SHAPE = Shapes.create(new AABB(0.4, 0.0D, 0.4, 0.6, 0.999999, 0.6));
    protected static final VoxelShape TOP_SHAPE = Shapes.create(new AABB(0.4, 0.0D, 0.4, 0.6, 0.6, 0.6));

    public TikiTorchBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(SECTION, TorchSection.UPPER));
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
        return blockpos.getY() < context.getLevel().getHeight() - 1 &&
                context.getLevel().getBlockState(blockpos.above()).canBeReplaced(context) &&
                context.getLevel().getBlockState(blockpos.above(2)).canBeReplaced(context) ? ret : null;
    }
    
    @Override
    @Deprecated
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis() == Axis.Y && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TorchSection section = state.getValue(SECTION);

        if (section == TorchSection.UPPER) return;

        worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(SECTION, TorchSection.MIDDLE), Block.UPDATE_ALL);
        worldIn.setBlock(pos.above(2), this.defaultBlockState().setValue(SECTION, TorchSection.UPPER), Block.UPDATE_ALL);
    }
    
    private boolean placeShortTorchOn(BlockState state) {
        // Only place top block if it's on a fence/wall
        return BlockTags.FENCES.contains(state.getBlock()) || BlockTags.WALLS.contains(state.getBlock());
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
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        boolean ret = false;
        TorchSection section = state.getValue(SECTION);
        BlockPos base = pos.below(section.height);
        for (TorchSection otherSection : TorchSection.values()) {
            BlockPos pos2 = base.above(otherSection.height);
            BlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() == this && state2.getValue(SECTION) == otherSection) {
                if (player.isCreative()) {
                    ret |= super.removedByPlayer(state2, world, pos2, player, willHarvest, fluid);
                } else {
                    this.playerWillDestroy(world, pos2, state2, player);
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
        boolean isTop = state.getValue(SECTION) == TorchSection.UPPER;
        if (isTop) {
            double d = pos.getX() + 0.5F;
            double d1 = pos.getY() + 0.7F;
            double d2 = pos.getZ() + 0.5F;

            world.addParticle(ParticleTypes.SMOKE, d, d1, d2, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
