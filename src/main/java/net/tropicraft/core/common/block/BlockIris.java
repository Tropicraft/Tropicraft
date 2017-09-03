package net.tropicraft.core.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIris extends BlockBush implements ITropicraftBlock {

	public static enum PlantHalf implements IStringSerializable {
		LOWER, UPPER;

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
		@Override
		public String toString() {
			return this.getName();
		}
	};

	public static final PropertyEnum<PlantHalf> HALF = PropertyEnum.create("half", PlantHalf.class);

	public BlockIris(String[] names) {
		super();
		this.setDefaultState(this.blockState.getBaseState().
				withProperty(HALF, PlantHalf.LOWER)
				);
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {HALF};
	}

	// Called by ItemBlock after the (lower) block has been placed
	// Use it to add the top half of the block
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getStateFromMeta(stack.getMetadata()).withProperty(HALF, PlantHalf.UPPER), 3);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, getProperties());
	}

	@Override
	public String getStateName(IBlockState state) {
		return "iris";
	}

	@Override
	public IBlockColor getBlockColor() {
		return null;
	}

	@Override
	public IItemColor getItemColor() {
		return null;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(HALF, PlantHalf.values()[meta >> 3]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((PlantHalf) state.getValue(HALF)).ordinal() * 8;
	}

	public BlockPos getLowerPos(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {return pos;}
		return world.getBlockState(pos).getValue(HALF) == PlantHalf.UPPER ? pos.down() : pos;       
	}

	public BlockPos getUpperPos(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {return pos.up();}
		return world.getBlockState(pos).getValue(HALF) == PlantHalf.UPPER ? pos : pos.up();       
	}

	public IBlockState getStateLower(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(getLowerPos(world, pos));
	}

	public IBlockState getStateUpper(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(getUpperPos(world, pos));
	}

	// Called by ItemBlock before the block is placed - the placed block must always be Half.LOWER
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getStateFromMeta(meta).withProperty(HALF, PlantHalf.LOWER);
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		Block block = state.getBlock();

		if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) {
			return true;
		}

		return false;
	}
	
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        if (state.getValue(HALF) == PlantHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(pos.up());
            return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
        }
    }
    
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    	super.onBlockHarvested(worldIn, pos, state, player);
    	System.out.println("harvests");
    }
}
