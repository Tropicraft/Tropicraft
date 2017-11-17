package net.tropicraft.core.client;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.item.ItemTropicraftColored;

public class ChairColorHandler implements IItemColor {

	@Override
	public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
		if (stack.getItem() instanceof ItemTropicraftColored) {
			Integer color = ((ItemTropicraftColored)stack.getItem()).getColor(stack, tintIndex);

			return color;
		}

		return 0xffffff;
	}
}
