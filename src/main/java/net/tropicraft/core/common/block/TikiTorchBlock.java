package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Direction.Axis;
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

public class TikiTorchBlock extends Block {

    public enum TorchSection implements IStringSerializable {
        UPPER(2), MIDDLE(1), LOWER(0);
        
        final int height;
        
        private TorchSection(int height) {
            this.height = height;
        }

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
        @Override
        public String toString() {
            return this.getName();
        }
    };

    public static final EnumProperty<TorchSection> SECTION = EnumProperty.create("section", TorchSection.class);

    protected static final VoxelShape BASE_SHAPE = VoxelShapes.create(new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.999999D, 0.6000000238418579D));
    protected static final VoxelShape TOP_SHAPE = VoxelShapes.create(new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D));

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
        BlockState ret = getDefaultState().with(SECTION, TorchSection.LOWER);
        return blockpos.getY() < context.getWorld().getDimension().getHeight() - 1 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? ret : null;
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

        // Only place top block if it's on a fence
        BlockState stateBelow = worldIn.getBlockState(pos.down());
        if (stateBelow.getBlock().isIn(BlockTags.FENCES) || stateBelow.getBlock().isIn(BlockTags.WALLS)) {
            worldIn.setBlockState(pos, this.getDefaultState().with(SECTION, TorchSection.UPPER), Constants.BlockFlags.DEFAULT);
        } else {
            worldIn.setBlockState(pos.up(), this.getDefaultState().with(SECTION, TorchSection.MIDDLE), Constants.BlockFlags.DEFAULT);
            worldIn.setBlockState(pos.up(2), this.getDefaultState().with(SECTION, TorchSection.UPPER), Constants.BlockFlags.DEFAULT);  
        }
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TorchSection section = state.get(SECTION);
        // Clean up other pieces
        for (int i = 0; i < 2; i++) {
            int offset = i - section.height;
            BlockPos pos2 = pos.up(offset >= 0 ? offset + 1 : offset);
            BlockState state2 = worldIn.getBlockState(pos2);
            if (state2.getBlock() == this && state2.get(SECTION) != section) {
                worldIn.destroyBlock(pos2, false);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
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
    
    @Override
    @Deprecated
    public int getLightValue(BlockState state) {
        if (state.get(SECTION) == TorchSection.UPPER) {
            return 15;
        }
        return super.getLightValue(state);
    }
}
