package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class TikiTorchBlock extends Block {

    public enum TorchSection implements IStringSerializable {
        UPPER, MIDDLE, LOWER;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
        @Override
        public String toString() {
            return this.getName();
        }
    };
    
    private enum PlaceMode {
        FULL,
        TOP_ONLY,
        BLOCKED,
        ;
    }

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
        TorchSection section = (TorchSection)state.get(SECTION);

        if (section == TorchSection.UPPER) {
            return TOP_SHAPE;
        } else {
            return BASE_SHAPE;
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        if (!super.isValidPosition(state, world, pos)) {
            return false;
        }
        PlaceMode mode = canPlaceTikiTorchOn(state, world, pos);
        if (mode == PlaceMode.FULL) {
            return world.isAirBlock(pos.up()) && world.isAirBlock(pos.up(2));
        } else if (mode == PlaceMode.TOP_ONLY) {
            return true;
        }
        return false;
    }

    private PlaceMode canPlaceTikiTorchOn(BlockState state, IWorldReader world, BlockPos pos) {
        if (Blocks.TORCH.getDefaultState().isValidPosition(world, pos)) {
            if (state.getBlock() instanceof FenceBlock || state.getBlock() instanceof WallBlock) {
                return PlaceMode.TOP_ONLY;
            }
            return PlaceMode.FULL;
        }
        return state.get(SECTION) == TorchSection.LOWER ? PlaceMode.BLOCKED : PlaceMode.TOP_ONLY;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(SECTION, TorchSection.LOWER);
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TorchSection section = state.get(SECTION);

        if (section == TorchSection.UPPER) return;

        // Only place top block if it's on a fence
        BlockState stateBelow = worldIn.getBlockState(pos.down());
        if (stateBelow.getBlock() instanceof FenceBlock ||
                stateBelow.getBlock() instanceof WallBlock) {
            worldIn.setBlockState(pos, this.getDefaultState().with(SECTION, TorchSection.UPPER), Constants.BlockFlags.DEFAULT);
        } else {
            worldIn.setBlockState(pos.up(), this.getDefaultState().with(SECTION, TorchSection.MIDDLE), Constants.BlockFlags.DEFAULT);
            worldIn.setBlockState(pos.up(2), this.getDefaultState().with(SECTION, TorchSection.UPPER), Constants.BlockFlags.DEFAULT);  
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, world, pos, newState, isMoving);
        if (!world.isRemote) {
            TorchSection section = state.get(SECTION);
            if (section == TorchSection.LOWER) {
                if (world.getBlockState(pos.up()).getBlock() == this) {
                    world.destroyBlock(pos.up(), false);
                }

                if (world.getBlockState(pos.up(2)).getBlock() == this) {
                    world.destroyBlock(pos.up(2), false);
                }
            } else if (section == TorchSection.MIDDLE){
                if (world.getBlockState(pos.down()).getBlock() == this) {
                    world.destroyBlock(pos.down(), false);
                }

                if (world.getBlockState(pos.up()).getBlock() == this) {
                    world.destroyBlock(pos.up(), false);
                }
            } else {

                if (world.getBlockState(pos.down()).getBlock() == this) {
                    world.destroyBlock(pos.down(), false);
                }

                if (world.getBlockState(pos.down(2)).getBlock() == this) {
                    world.destroyBlock(pos.down(2), false);
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        boolean isTop = (TorchSection)state.get(SECTION) == TorchSection.UPPER;
        if (isTop) {
            double d = (float) pos.getX() + 0.5F;
            double d1 = (float) pos.getY() + 0.7F;
            double d2 = (float) pos.getZ() + 0.5F;

            world.addParticle(ParticleTypes.SMOKE, d, d1, d2, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public int getLightValue(BlockState state) {
        if (state.get(SECTION) == TorchSection.UPPER) {
            return 15;
        }
        return super.getLightValue(state);
    }
    
    @Override
    public int getLightValue(BlockState state, IEnviromentBlockReader world, BlockPos pos) {
        return getLightValue(state);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
