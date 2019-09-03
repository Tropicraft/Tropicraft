package net.tropicraft.core.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.item.IColoredItem;

public class CocktailColorHandler implements IItemColor {

	@Override
	public int getColor(final ItemStack stack, final int tintIndex) {
		if (stack.getItem() instanceof IColoredItem) {
			return ((IColoredItem)stack.getItem()).getColor(stack, tintIndex);
		}

		return 0xffffff;
	}
}
