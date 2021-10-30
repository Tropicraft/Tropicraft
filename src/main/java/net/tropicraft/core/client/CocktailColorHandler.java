package net.tropicraft.core.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.item.IColoredItem;

public class CocktailColorHandler implements ItemColor {

	@Override
	public int getColor(final ItemStack stack, final int tintIndex) {
		if (stack.getItem() instanceof IColoredItem) {
			return ((IColoredItem)stack.getItem()).getColor(stack, tintIndex);
		}

		return 0xffffff;
	}
}
