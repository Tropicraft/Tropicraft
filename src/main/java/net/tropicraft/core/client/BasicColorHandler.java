package net.tropicraft.core.client;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.item.IColoredItem;

public class BasicColorHandler implements IItemColor {

	@Override
	public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
		if (stack.getItem() instanceof IColoredItem) {
			Integer color = ((IColoredItem)stack.getItem()).getColor(stack, tintIndex);
			return color;
		}

		return 0xffffff;
	}
}
