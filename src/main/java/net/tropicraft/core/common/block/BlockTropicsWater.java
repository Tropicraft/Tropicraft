package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicsWater extends BlockFluidClassic {

	public BlockTropicsWater(Fluid fluid, Material material) {
		super(fluid, material);
		this.lightOpacity = 0;
		this.setCreativeTab(null);
		this.displacements.put(BlockRegistry.coral, Boolean.valueOf(false));
		//TODO: this.displacements.put(BlockRegistry.bambooFence, Boolean.valueOf(false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		/*
		 * TODO: Doesn't work in 1.9. FIXME :)
		 * Fix so that tropics water can form infinite water sources again.
		 * Turns blocks into source blocks if they are between two other source blocks.
		 */    	
		int currentMeta = getMetaFromState(state);
		if (currentMeta > 0 && state.getActualState(world, pos.down()).getMaterial() != Material.AIR) {
			int neighbourSources = 0;
			BlockPos npos = new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
			if (isNeighbourSource (world, state, npos))
				neighbourSources++;
			npos = new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
			if (isNeighbourSource (world, state, npos))
				neighbourSources++;
			npos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
			if (isNeighbourSource (world, state, npos))
				neighbourSources++;
			npos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
			if (isNeighbourSource (world, state, npos))
				neighbourSources++;

			if (neighbourSources >= 2) {
				world.setBlockState(pos, state.withProperty(LEVEL, 0));
			}
		}

		// Need to do this for the water to flow !!
		super.updateTick(world, pos, state, rand);
	}

	private boolean isNeighbourSource(World world, IBlockState state, BlockPos pos) {
		if (state.getActualState(world, pos).getBlock() == this && this.getMetaFromState(state) == 0)
			return true;

		return false;
	}
}
