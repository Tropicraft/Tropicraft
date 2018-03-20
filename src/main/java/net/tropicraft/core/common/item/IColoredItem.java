package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.client.BasicColorHandler;
import net.tropicraft.core.registry.TropicraftRegistry;

public interface IColoredItem {
    
    int getColor(ItemStack stack, int pass);
    
    @SideOnly(Side.CLIENT)
    default IItemColor getColorHandler() {
        return new BasicColorHandler();
    }
    
    /** Helpers **/
    
    default void addColoredSubitems(CreativeTabs tab, List<ItemStack> list, ItemTropicraft item) {
        if (item.isInCreativeTab(tab)) {
            for (int i = 0; i < ColorHelper.getNumColors(); i++) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
    
    default String getColorUnlocName(String itemName, ItemStack stack) {
        return "item." + TropicraftRegistry.getNamePrefixed(itemName) + "." + EnumDyeColor.byDyeDamage(stack.getItemDamage()).getUnlocalizedName(); 
    }
}
