package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.Info;
import net.tropicraft.core.registry.CreativeTabRegistry;

/**
 * Class for items such as chairs, umbrellas, and beach floats to implement to handle
 * the majority of the color code.
 * 
 * NOTE: To ensure this class functions correctly, make sure to set the texture name accordingly
 */
public class ItemTropicraftColored extends ItemTropicraft {

	/** Item name used to determine name via itemstack damage */
	private String itemName;

	public ItemTropicraftColored(String itemName) {
		super();
		this.itemName = itemName;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
	}
//
//	/**
//	 * Gets an icon index based on an item's damage value and the given render pass
//	 */
//	@SideOnly(Side.CLIENT)
//	@Override
//	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
//		return pass > 0 ? this.overlayIcon : super.getIconFromDamageForRenderPass(damage, pass);
//	}
//
/*//	@SideOnly(Side.CLIENT)
//	@Override
//	public int getColorFromItemStack(ItemStack itemstack, int pass) {
//		Integer color = ColorHelper.getColorFromDamage(itemstack.getItemDamage());
//		return (pass == 0 ? 16777215 : color.intValue());
//	}
*/
	public int getColor(ItemStack itemstack, int pass) {
		Integer color = ColorHelper.getColorFromDamage(itemstack.getItemDamage());
		return (pass == 0 ? 16777215 : color.intValue());
	}
	
	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		for (int i = 0; i < ColorHelper.getNumColors(); i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		String name = ("" + I18n.translateToLocal(this.getUnlocalizedName().replace("item.", String.format("item.%s:", Info.MODID)).split(":")[0]
				+ ":" + this.itemName + "_" +  EnumDyeColor.byDyeDamage(itemstack.getItemDamage()).getUnlocalizedName() + ".name")).trim();

		return name;
	}
}
