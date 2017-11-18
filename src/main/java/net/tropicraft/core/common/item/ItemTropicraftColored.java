package net.tropicraft.core.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.registry.CreativeTabRegistry;
import net.tropicraft.core.registry.TropicraftRegistry;

/**
 * Class for items such as chairs, umbrellas, and beach floats to implement to handle
 * the majority of the color code.
 * 
 * NOTE: To ensure this class functions correctly, make sure to set the texture name accordingly
 */
public abstract class ItemTropicraftColored extends ItemTropicraft {

    /** Item name used to determine name via itemstack damage */
    private String itemName;

    public ItemTropicraftColored(String itemName) {
        super();
        this.itemName = itemName;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
    }

    public abstract int getColor(ItemStack itemstack, int pass);

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (tab != CreativeTabRegistry.tropicraftTab) return;
        for (int i = 0; i < ColorHelper.getNumColors(); i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return "item." + TropicraftRegistry.getNamePrefixed(this.itemName) + "." + EnumDyeColor.byDyeDamage(itemstack.getItemDamage()).getUnlocalizedName(); 
    }
}
