package net.tropicraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockTropicraftFenceGate extends BlockFenceGate {

	public BlockTropicraftFenceGate(String name, String textureName, Material material) {
		super();
		this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
		setBlockName(name);
		setBlockTextureName(textureName);	
	}
	
	/**
	 * Register all the icons of the block
	 * @param iconRegister Icon registry
	 */
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(getActualName(getFormattedTextureName()));
    }
	
	/**
	 * 
	 * @return Tropicraft-mod formattted texture name/location
	 */
	protected String getFormattedTextureName() {
		return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(this.getTextureName()));
	}
	
	/**
	 * @return The unlocalized block name
	 */
	@Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }
	
	/**
	 * Get the true name of the block
	 * @param unlocalizedName tile.%truename%
	 * @return The actual name of the block, rather than tile.%truename%
	 */
	protected String getActualName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
	}
}
