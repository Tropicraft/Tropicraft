package net.tropicraft.core.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
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
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockDrinkMixer extends BlockTropicraft implements
		ITileEntityProvider {
	
	public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

	public BlockDrinkMixer() {
		super(Material.ROCK);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
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

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return TileEntityFactory.getDrinkMixerTE();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = entityPlayer.getHeldItemMainhand();

		TileEntityDrinkMixer mixer = (TileEntityDrinkMixer)world.getTileEntity(pos);

		if (mixer.isDoneMixing()) {
			mixer.retrieveResult();
			return true;
		}

		if (stack == null) {
			mixer.emptyMixer();
			return true;
		}	

		ItemStack ingredientStack = stack.copy();
		ingredientStack.stackSize = 1;

		if (mixer.addToMixer(ingredientStack)) {
			entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
		}

		if (stack.getItem() == ItemRegistry.bambooMug && mixer.canMix()) {
			mixer.startMixing();
			entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
		}

		return true;    	
	}
	
	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		int var6 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		EnumFacing dir = null;
		if (var6 == 0) {
			dir = EnumFacing.NORTH;
		} else if (var6 == 1) {
			dir = EnumFacing.EAST;
		} else if (var6 == 2) {
			dir = EnumFacing.SOUTH;
		} else if (var6 == 3) {
			dir = EnumFacing.WEST;
		} else {
			dir = null;
		}

		worldIn.setBlockState(pos, state.withProperty(FACING, dir));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing dir = null;
		switch(meta) {
		case 0:
			dir = EnumFacing.NORTH;
			break;
		case 1:
			dir = EnumFacing.EAST;
			break;
		case 2:
			dir = EnumFacing.SOUTH;
			break;
		default:
			dir = EnumFacing.WEST;
			break;
		}

		return this.getDefaultState().withProperty(FACING, dir);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing dir = (EnumFacing) state.getValue(FACING);
		switch(dir) {
		case NORTH:
			return 0;
		case EAST:
			return 1;
		case SOUTH:
			return 2;
		default:
			return 3;
		}
	}

}
