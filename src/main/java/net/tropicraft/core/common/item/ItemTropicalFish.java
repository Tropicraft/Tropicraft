package net.tropicraft.core.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;

public class ItemTropicalFish extends ItemColoredFood {

    private static final int[] FISH_COLORS = {
            0xfb810d, 0x02cbcf, 0xece43b, 0xaed6e0, 0x5fe3fc,
            0x5386cc, 0x12131e, 0xc72d91, 
    };

    private String cookedStatus;

    // cookedStatus = raw or cooked (used in localization)
    public ItemTropicalFish(int healAmount, float saturation, String itemName, String cookedStatus) {
        super(healAmount, saturation, itemName);
        this.cookedStatus = cookedStatus;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < EntityTropicalFish.NAMES.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getColor(ItemStack stack, int pass) {
        if (cookedStatus == "raw") {
            return FISH_COLORS[stack.getItemDamage()] | 0x222;
        } else {
            return (FISH_COLORS[stack.getItemDamage()] & 0xfefefe) >> 1;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return String.format("item.%s.%s.%d.%s", Info.MODID, this.itemName, itemstack.getItemDamage(), cookedStatus);
    }

}
