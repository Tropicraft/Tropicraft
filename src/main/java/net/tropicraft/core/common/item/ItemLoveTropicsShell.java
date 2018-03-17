package net.tropicraft.core.common.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Names;
import net.tropicraft.core.registry.CreativeTabRegistry;
import scala.actors.threadpool.Arrays;

public class ItemLoveTropicsShell extends ItemShell implements IColoredItem {

	private final List<Integer> colors = new ArrayList<Integer>();
	private final List<String> names;

	public ItemLoveTropicsShell() {
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
        Random rand = new Random();
        names = Arrays.asList(Names.LOVE_TROPICS_NAMES);
        for (String name : names) {
            rand.setSeed(name.hashCode());
            colors.add(Color.HSBtoRGB(rand.nextFloat(), (rand.nextFloat() * 0.2f) + 0.7f, 1));
        }
	}

	@Override
	public int getColor(ItemStack itemstack, int pass) {
		return (pass == 0 ? 0xFFFFFFFF : colors.get(itemstack.getItemDamage()));
	}

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
//        if (tab != CreativeTabRegistry.tropicraftTab) return;
//        for (int i = 0; i < Names.LOVE_TROPICS_NAMES.length; i++) {
//            list.add(new ItemStack(this, 1, i));
//        }
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack itemstack) {
        String name = names.get(itemstack.getItemDamage());
        String type = name.endsWith("s") ? "with_s" : "normal";
        return I18n.translateToLocalFormatted("item.tropicraft.shell.owned." + type + ".name", names.get(itemstack.getItemDamage())); 
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
    	return getUnlocalizedNameInefficiently(stack);
    }
}
