package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class for items such as chairs, umbrellas, and beach floats to implement to handle
 * the majority of the color code.
 * 
 * NOTE: To ensure this class functions correctly, make sure to set the texture name accordingly
 */
public class ItemTropicraftColored extends ItemTropicraft {

	@SideOnly(Side.CLIENT)
	private IIcon overlayIcon;
	
	public ItemTropicraftColored() {
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		this.itemIcon = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getIconString());
		this.overlayIcon = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getIconString() + "Inverted");
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		return pass > 0 ? this.overlayIcon : super.getIconFromDamageForRenderPass(damage, pass);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack itemstack, int pass) {
		Integer color = ColorHelper.getColorFromDamage(itemstack.getItemDamage());
		return (pass == 0 ? 16777215 : color.intValue());
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < ColorHelper.getNumColors(); i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		String name = ("" + StatCollector.translateToLocal(this.getUnlocalizedName().replace("item.", String.format("item.%s:", TCInfo.MODID)).split(":")[0]
				+ ":" + this.getIconString() + "_" + ItemDye.field_150923_a[itemstack.getItemDamage()] + ".name")).trim();

		return name;
	}
}
