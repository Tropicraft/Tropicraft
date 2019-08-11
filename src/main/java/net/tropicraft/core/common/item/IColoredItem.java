package net.tropicraft.core.common.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.BasicColorHandler;

public interface IColoredItem {

    int getColor(ItemStack stack, int pass);

    @OnlyIn(Dist.CLIENT)
    default IItemColor getColorHandler() {
        return new BasicColorHandler();
    }

}