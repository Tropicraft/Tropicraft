package net.tropicraft.core.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.item.IColoredItem;

import javax.annotation.Nonnull;

public class BasicColorHandler implements IItemColor {

    @Override
    public int getColor(@Nonnull ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof IColoredItem) {
            return ((IColoredItem)stack.getItem()).getColor(stack, tintIndex);
        }

        return 0xffffff;
    }
}
