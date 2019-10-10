package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockTropicraftSand extends FallingBlock {
    public static final BooleanProperty UNDERWATER = BooleanProperty.create("underwater");

    public BlockTropicraftSand(final Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(UNDERWATER, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UNDERWATER);
    }
    
    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return Blocks.SAND.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public void onEntityWalk(final World world, final BlockPos pos, final Entity entity) {
        final BlockState state = world.getBlockState(pos);

        // If not black sands
        if (state.getBlock() != TropicraftBlocks.VOLCANIC_SAND.get()) {
            return;
        }

        if (entity instanceof LivingEntity) {
            final LivingEntity living = (LivingEntity)entity;
            final ItemStack stack = living.getItemStackFromSlot(EquipmentSlotType.FEET);

            // If entity isn't wearing anything on their feetsies
            if (stack.isEmpty()) {
                living.attackEntityFrom(DamageSource.LAVA, 0.5F);
            }
        } else {
            entity.attackEntityFrom(DamageSource.LAVA, 0.5F);
        }
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final IFluidState upState = context.getWorld().getFluidState(context.getPos().up());
        boolean waterAbove = false;
        if (!upState.isEmpty()) {
            waterAbove = true;
        }
        return this.getDefaultState().with(UNDERWATER, waterAbove);
    }

    @Override
    public void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos pos2, boolean neighborChanged) {
        final IFluidState upState = world.getFluidState(pos.up());
        if (!upState.isEmpty()) {
            world.setBlockState(pos, state.with(UNDERWATER, true), 2);
        }
        super.neighborChanged(state, world, pos, block, pos2, neighborChanged);
    }
}
