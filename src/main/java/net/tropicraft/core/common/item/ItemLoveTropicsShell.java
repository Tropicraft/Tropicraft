package net.tropicraft.core.common.item;

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

public class ItemLoveTropicsShell extends ItemTropicraftColored {

	private static final List<Integer> colors = new ArrayList<Integer>();
	private static final List<String> names = new ArrayList<String>();

	public ItemLoveTropicsShell() {
		super("ltshell");
		Random rand = new Random();
		for (int i = 0; i < Names.LOVE_TROPICS_NAMES.length; i++) {
			colors.add(rand.nextInt(Integer.MAX_VALUE));
			names.add(Names.LOVE_TROPICS_NAMES[i]);
		}
	}

	@Override
	public int getColor(ItemStack itemstack, int pass) {
		return (pass == 0 ? 16777215 : colors.get(itemstack.getItemDamage()));
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
        return names.get(itemstack.getItemDamage()) + " " + I18n.translateToLocal("item.tropicraft.shell.name"); 
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
    	return getUnlocalizedNameInefficiently(stack);
    }
}
