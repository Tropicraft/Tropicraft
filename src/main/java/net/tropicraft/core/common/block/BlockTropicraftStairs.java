package net.tropicraft.core.common.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockTropicraftStairs extends BlockStairs {

	public BlockTropicraftStairs(IBlockState modelState) {
		super(modelState);
		this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.EAST));
	}

}
