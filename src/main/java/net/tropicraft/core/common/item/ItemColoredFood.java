package net.tropicraft.core.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.registry.CreativeTabRegistry;

/**
 * Class for items such as chairs, umbrellas, and beach floats to implement to handle
 * the majority of the color code.
 * 
 * NOTE: To ensure this class functions correctly, make sure to set the texture name accordingly
 */
public abstract class ItemColoredFood extends ItemFood implements IColoredItem {

    /** Item name used to determine name via itemstack damage */
    private String itemName;

    public ItemColoredFood(int healAmount, float saturation, String itemName) {
        super(healAmount, saturation, false);
        this.itemName = itemName;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < ColorHelper.getNumColors(); i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }
    
    // Expose this method as public
    @Override
    public boolean isInCreativeTab(CreativeTabs targetTab) {
        return super.isInCreativeTab(targetTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return getColorUnlocName(itemName, itemstack);
    }
}
