package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.common.enums.TropicraftTallPlants;

public class BlockPineapple extends BlockTallPlant implements IGrowable, IPlantable {

	/** Number of total ticks it takes for this pineapple to grow */
	public static final int TOTAL_GROW_TICKS = 7;

	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 1, TOTAL_GROW_TICKS);

	public BlockPineapple() {
		super();
		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().
				withProperty(VARIANT, TropicraftTallPlants.PINEAPPLE).
				withProperty(HALF, PlantHalf.LOWER).
				withProperty(STAGE, Integer.valueOf(1))
				);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, HALF, STAGE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_HEIGHT_AABB;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state,
			boolean isClient) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos,
			IBlockState state) {
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(HALF).ordinal() << 3) | (state.getValue(STAGE) & 7);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		int half = (meta >> 3) & 1;
		int stage = meta & 7;
		IBlockState ret = getDefaultState().withProperty(HALF, PlantHalf.values()[half]);
		if (stage > 0) {
			ret = ret.withProperty(STAGE, stage);
		}
		return ret;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int currentStage = ((Integer)state.getValue(STAGE)).intValue();
		if (currentStage < TOTAL_GROW_TICKS) {
			IBlockState growthState = state.withProperty(STAGE, currentStage + 1);
			world.setBlockState(pos, growthState, 4);
		} else {
			IBlockState above = world.getBlockState(pos.up());

			// Don't bother placing if it's already there
			if (above.getBlock() == this) return;
			if (((PlantHalf)state.getValue(HALF)) == PlantHalf.UPPER) return;

			// Place actual pineapple plant above stem
			IBlockState fullGrowth = state.withProperty(BlockTallPlant.HALF, BlockTallPlant.PlantHalf.UPPER);
			world.setBlockState(pos.up(), fullGrowth, 3);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		checkFlowerChange(world, pos, state);
		if (pos.getY() > world.getHeight() - 2) {
			return;
		}

		// Current metadata
		int growth = ((Integer)state.getValue(STAGE));

		if (state.getBlock() == this && growth <= TOTAL_GROW_TICKS && world.isAirBlock(pos.up()) && ((PlantHalf)state.getValue(HALF)) == PlantHalf.LOWER) {
			if (growth >= TOTAL_GROW_TICKS - 1) {
				// Set current state
				IBlockState growthState = state.withProperty(STAGE, TOTAL_GROW_TICKS);
				world.setBlockState(pos, growthState, 4);

				// Place actual pineapple plant above stem
				IBlockState fullGrowth = state.withProperty(BlockTallPlant.HALF, BlockTallPlant.PlantHalf.UPPER);
				world.setBlockState(pos.up(), fullGrowth, 3);
			} else {
				IBlockState growthState = state.withProperty(STAGE, growth + 1);
				world.setBlockState(pos, growthState, 4);
			}
		}
	}

	private boolean isFullyGrown(IBlockState state) {
		return ((Integer)state.getValue(STAGE)) == TOTAL_GROW_TICKS || ((PlantHalf)state.getValue(BlockTallPlant.HALF)) == PlantHalf.UPPER;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		this.checkForDrop(worldIn, pos, state);
	}

	protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (this.canBlockStay(worldIn, pos, state)) {
			return true;
		} else {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		}
	}

	// Called by ItemBlock after the (lower) block has been placed
	// Use it to add the top half of the block
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return null;
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

	protected void checkFlowerChange(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote && !canBlockStay(world, pos, state)) {
			if (isFullyGrown(state)) {
				dropBlockAsItem(world, pos, state, 0);
			}
			world.setBlockToAir(pos);
		}
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		// Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		if (state.getBlock() != this)
			return super.canBlockStay(worldIn, pos, state);

		if (state.getValue(HALF) == PlantHalf.UPPER) {
			return worldIn.getBlockState(pos.down()).getBlock() == this;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos.up());
			if (iblockstate.getBlock() == this) {
				return super.canBlockStay(worldIn, pos, iblockstate);
			} else { // If just the stem
				return this.canPlaceBlockAt(worldIn, pos);
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null; // Handled in BlockEvents
	}

}
