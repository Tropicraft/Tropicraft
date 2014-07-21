package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftFence extends BlockFence {

	/** Class that this fence can connect to */
	private BlockTropicraftFenceGate fenceGate;
	
	public BlockTropicraftFence(String name, String textureName, BlockTropicraftFenceGate fenceGate, Material material) {
		super(name, material);
		this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
		this.setBlockName(name);
		this.setBlockTextureName(textureName);
	}

	@Override
	public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if (block != this && block != TCBlockRegistry.bambooFenceGate && block != TCBlockRegistry.palmFenceGate) {
			return block != null && block.getMaterial().isOpaque() && block.renderAsNormalBlock() ? block.getMaterial() != Material.gourd : false;
		} else {
			return true;
		}
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return true;
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
