package net.tropicraft.core.common.item;

import net.minecraft.world.item.ItemStack;

public interface IColoredItem {

    int getColor(ItemStack stack, int pass);

}