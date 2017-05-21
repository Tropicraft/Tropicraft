package net.tropicraft;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.block.ITropicraftBlock;

public class SandItemColorHandler implements IItemColor {

	public SandItemColorHandler() {

	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		System.err.println("please dawg");
		IBlockState state = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
		IBlockColor blockColor = ((ITropicraftBlock)state.getBlock()).getBlockColor();
		return SandColors.getColor(state.getBlock().getMetaFromState(state));
		//return blockColor == null ? 0xFFFFFF : blockColor.colorMultiplier(state, null, null, tintIndex);
	}

}
