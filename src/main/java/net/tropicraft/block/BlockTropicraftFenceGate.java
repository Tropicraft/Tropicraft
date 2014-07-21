package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftFenceGate extends BlockFenceGate {

	private Block blockForTexture;
	
	private int textureMeta;
	
	public BlockTropicraftFenceGate(Block block, int meta, String name, Material material) {
		super();
		this.blockForTexture = block;
		this.textureMeta = meta;
		this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
	}
	
	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockForTexture.getIcon(side, textureMeta);
	}
	
	/**
	 * Register all the icons of the block
	 * @param iconRegister Icon registry
	 */
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
	    
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
