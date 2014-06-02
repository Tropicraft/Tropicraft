package net.tropicraft.item.tool;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftHoe extends ItemHoe {

	protected ToolMaterial toolMaterial;
	
	public ItemTropicraftHoe(ToolMaterial toolMaterial, String textureName) {
		super(toolMaterial);
		this.toolMaterial = toolMaterial;
        this.maxStackSize = 1;
        this.setMaxDamage(toolMaterial.getMaxUses());
        this.setTextureName(textureName);
        this.setCreativeTab(TCCreativeTabRegistry.tabTools);
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

	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(TCInfo.TOOL_ICON_LOCATION + this.getIconString());
	}
}
