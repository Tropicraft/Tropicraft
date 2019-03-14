package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTropicraftSand extends BlockFalling {
    public static final BooleanProperty UNDERWATER = BooleanProperty.create("underwater");

    public BlockTropicraftSand(final Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(UNDERWATER, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(UNDERWATER);
    }

    @Override
    public void onEntityWalk(final World world, final BlockPos pos, final Entity entity) {
        final IBlockState state = world.getBlockState(pos);

        // If not black sands
        if (state.getBlock() != TropicraftBlocks.VOLCANIC_SAND) {
            return;
        }

        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

            // If player isn't wearing anything on their feetsies
            if (stack.isEmpty()) {
                player.attackEntityFrom(DamageSource.LAVA, 0.5F);
            }
        } else {
            entity.attackEntityFrom(DamageSource.LAVA, 0.5F);
        }
    }

    @Override
    public IBlockState getStateForPlacement(final BlockItemUseContext context) {
        final IFluidState upState = context.getWorld().getFluidState(context.getPos().up());
        boolean waterAbove = false;
        if (!upState.isEmpty()) {
            waterAbove = true;
        }
        return this.getDefaultState().with(UNDERWATER, waterAbove);
    }

    @Override
    public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos pos2) {
        final IFluidState upState = world.getFluidState(pos.up());
        if (!upState.isEmpty()) {
            world.setBlockState(pos, state.with(UNDERWATER, true), 2);
        }
        super.neighborChanged(state, world, pos, block, pos2);
    }
}
