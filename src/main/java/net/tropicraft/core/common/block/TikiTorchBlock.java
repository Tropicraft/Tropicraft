package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class TikiTorchBlock extends Block {

    public enum TorchSection implements IStringSerializable {
        UPPER(2), MIDDLE(1), LOWER(0);
        
        final int height;
        
        private TorchSection(int height) {
            this.height = height;
        }

        @Override
        public String getString() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return this.getString();
        }
    };

    public static final EnumProperty<TorchSection> SECTION = EnumProperty.create("section", TorchSection.class);

    protected static final VoxelShape BASE_SHAPE = VoxelShapes.create(new AxisAlignedBB(0.4, 0.0D, 0.4, 0.6, 0.999999, 0.6));
    protected static final VoxelShape TOP_SHAPE = VoxelShapes.create(new AxisAlignedBB(0.4, 0.0D, 0.4, 0.6, 0.6, 0.6));

    public TikiTorchBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(getDefaultState().with(SECTION, TorchSection.UPPER));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(SECTION);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TorchSection section = state.get(SECTION);

        if (section == TorchSection.UPPER) {
            return TOP_SHAPE;
        } else {
            return BASE_SHAPE;
        }
    }

    @Override
    @Deprecated
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        if (hasEnoughSolidSide(world, pos.down(), Direction.UP)) { // can block underneath support torch
            return true;
        } else { // if not, is the block underneath a lower 2/3 tiki torch segment?
            BlockState blockstate = world.getBlockState(pos.down());
            return (blockstate.getBlock() == this && blockstate.get(SECTION) != TorchSection.UPPER) && super.isValidPosition(state, world, pos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        if (placeShortTorchOn(context.getWorld().getBlockState(blockpos.down()))) {
            return getDefaultState().with(SECTION, TorchSection.UPPER);
        }
        BlockState ret = getDefaultState().with(SECTION, TorchSection.LOWER);
        return blockpos.getY() < context.getWorld().func_234938_ad_() - 1 &&
                context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) &&
                context.getWorld().getBlockState(blockpos.up(2)).isReplaceable(context) ? ret : null;
    }
    
    @Override
    @Deprecated
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis() == Axis.Y && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TorchSection section = state.get(SECTION);

        if (section == TorchSection.UPPER) return;

        worldIn.setBlockState(pos.up(), this.getDefaultState().with(SECTION, TorchSection.MIDDLE), Constants.BlockFlags.DEFAULT);
        worldIn.setBlockState(pos.up(2), this.getDefaultState().with(SECTION, TorchSection.UPPER), Constants.BlockFlags.DEFAULT);  
    }
    
    private boolean placeShortTorchOn(BlockState state) {
        // Only place top block if it's on a fence/wall
        return state.getBlock().isIn(BlockTags.FENCES) || state.getBlock().isIn(BlockTags.WALLS);
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        TorchSection section = state.get(SECTION);
        BlockPos base = pos.down(section.height);
        for (TorchSection otherSection : TorchSection.values()) {
            BlockPos pos2 = base.up(otherSection.height);
            BlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() == this && state2.get(SECTION) == otherSection) {
                super.harvestBlock(world, player, pos2, state2, te, stack);
                world.setBlockState(pos2, world.getFluidState(pos2).getBlockState(), world.isRemote ? 11 : 3);
            }
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        boolean ret = false;
        TorchSection section = state.get(SECTION);
        BlockPos base = pos.down(section.height);
        for (TorchSection otherSection : TorchSection.values()) {
            BlockPos pos2 = base.up(otherSection.height);
            BlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() == this && state2.get(SECTION) == otherSection) {
                if (player.isCreative()) {
                    ret |= super.removedByPlayer(state2, world, pos2, player, willHarvest, fluid);
                } else {
                    this.onBlockHarvested(world, pos2, state2, player);
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        boolean isTop = state.get(SECTION) == TorchSection.UPPER;
        if (isTop) {
            double d = pos.getX() + 0.5F;
            double d1 = pos.getY() + 0.7F;
            double d2 = pos.getZ() + 0.5F;

            world.addParticle(ParticleTypes.SMOKE, d, d1, d2, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
