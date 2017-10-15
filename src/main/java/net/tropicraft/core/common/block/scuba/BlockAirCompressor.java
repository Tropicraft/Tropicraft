package net.tropicraft.core.common.block.scuba;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraft;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockAirCompressor extends BlockTropicraft implements ITileEntityProvider {

	public BlockAirCompressor() {
		super(Material.ROCK);
		//this.setBlockBounds(0, 0, 0, 1, 1.8F, 1);
		this.isBlockContainer = true;
	}

	@Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		int var6 = MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		int meta = 0;
		if (var6 == 0) {
			meta = 2;
		} else if (var6 == 1) {
			meta = 5;
		} else if (var6 == 2) {
			meta = 3;
		} else if (var6 == 3) {
			meta = 4;
		}

		world.setBlockState(pos, state, 2);
	}

	@Override
	 public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = playerIn.getHeldItemMainhand();

		TileEntityAirCompressor mixer = (TileEntityAirCompressor)world.getTileEntity(pos);

		if (mixer.isDoneCompressing()) {
			mixer.ejectTank();
			return true;
		}

		if (stack == null) {
			mixer.ejectTank();
			return true;
		}

		ItemStack ingredientStack = stack.copy();
		ingredientStack.stackSize = 1;

		if (mixer.addTank(ingredientStack)) {
			playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
		}

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			TileEntityAirCompressor te = (TileEntityAirCompressor) world.getTileEntity(pos);
			te.ejectTank();
		}

		super.breakBlock(world, pos, state);
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World world) {
		return 4;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {

		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return TileEntityFactory.getAirCompressorTE();
	}

}
