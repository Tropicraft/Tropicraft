package net.tropicraft.item.armor;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.tropicraft.info.TCInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftArmor extends ItemArmor {

	public ItemTropicraftArmor(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
	}

	/**
	 * @return The unlocalized item name
	 */
	@Override
	public String getUnlocalizedName() {
		return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
	}

	/**
	 * @param itemStack ItemStack instance of this item
	 * @return The unlocalized item name
	 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
	}

	/**
	 * Get the actual name of the block
	 * @param unlocalizedName Unlocalized name of the block
	 * @return Actual name of the block, without the "tile." prefix
	 */
	protected String getActualName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	protected String getTexturePath(String name) {
		return TCInfo.ARMOR_LOCATION + name;
	}

	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
	}

}
