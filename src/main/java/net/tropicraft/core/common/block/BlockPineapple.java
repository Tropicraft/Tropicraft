package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.common.enums.TropicraftTallPlants;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockPineapple extends BlockTallPlant implements IGrowable, IPlantable {

	/** Number of total ticks it takes for this pineapple to grow */
	public static final int TOTAL_GROW_TICKS = 7;

	/** 
	 * Constant value that holds the metadata value of the pineapple
	 * when it is fully grown
	 */
	private static final int FULL_GROWTH = TOTAL_GROW_TICKS + 1;

	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 1, 7);

	public BlockPineapple(String[] names) {
		super(names);
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().
				withProperty(VARIANT, TropicraftTallPlants.PINEAPPLE).
				withProperty(HALF, PlantHalf.LOWER).
				withProperty(STAGE, Integer.valueOf(1))
				);
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT, HALF, STAGE};
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
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		System.err.println("Grow");
		int currentStage = ((Integer)state.getValue(STAGE)).intValue();
		if (currentStage < TOTAL_GROW_TICKS) {
			IBlockState growthState = state.withProperty(STAGE, currentStage + 1);
			world.setBlockState(pos, growthState, 4);
		} else {
			// Place actual pineapple plant above stem
			IBlockState fullGrowth = state.withProperty(BlockTallPlant.HALF, BlockTallPlant.PlantHalf.UPPER);
			world.setBlockState(pos.up(), fullGrowth, 3);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		System.err.println("TICK");
		checkFlowerChange(world, pos, state);
		if (pos.getY() > world.getHeight() - 2) {
			return;
		}

		// Current metadata
		int growth = ((Integer)state.getValue(STAGE));

		System.err.println(growth);

		if (state.getBlock() == this && growth <= TOTAL_GROW_TICKS && world.isAirBlock(pos.up())) {
			System.err.println("Can grow");
			if (growth >= TOTAL_GROW_TICKS - 1) {
				System.err.println("SHOULD GROW");

				// Set current state
				IBlockState growthState = state.withProperty(STAGE, TOTAL_GROW_TICKS);
				world.setBlockState(pos, growthState, 4);

				// Place actual pineapple plant above stem
				IBlockState fullGrowth = state.withProperty(BlockTallPlant.HALF, BlockTallPlant.PlantHalf.UPPER);
				world.setBlockState(pos.up(), fullGrowth, 3);
			} else {
				System.err.println("Increasing stage");
				IBlockState growthState = state.withProperty(STAGE, growth + 1);
				world.setBlockState(pos, growthState, 4);
			}
		}
	}

	private boolean isFullyGrown(IBlockState state) {
		return ((Integer)state.getValue(STAGE)) == TOTAL_GROW_TICKS;
	}

	private boolean canBlockStay(World worldIn, BlockPos pos) {
		return this.canPlaceBlockAt(worldIn, pos);
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		this.checkForDrop(worldIn, pos, state);
	}

	protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (this.canBlockStay(worldIn, pos)) {
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
		//worldIn.setBlockState(pos.up(), this.getStateFromMeta(stack.getMetadata()).withProperty(HALF, PlantHalf.UPPER), 3);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getValue(HALF) == PlantHalf.LOWER) return null;
		return new ItemStack(ItemRegistry.coconutChunk);
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		Block block = state.getBlock();

		if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) {
			return true;
		}

		if (state.getValue(HALF) == PlantHalf.LOWER)
			return true;

		if (block == this) {
			return true;
		} else if (block != Blocks.GRASS && block != Blocks.DIRT) {
			return false;
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

}
