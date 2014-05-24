package net.tropicraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftMulti extends BlockTropicraft {

	/** Array of icons associated with this item */
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	/** Names of each of the images */
	protected String[] names;

	public BlockTropicraftMulti(String[] names) {
		super();
		this.names = names;
	}
	
	public BlockTropicraftMulti(String[] names, Material material) {
		super(material);
		this.names = names;
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int id, int metadata) {
		if (metadata < 0 || metadata > names.length - 1)
			metadata = names.length - 1;

		return icons[metadata];
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < names.length; i++)
			list.add(new ItemStack(item, 1, i));        
    }

	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[names.length];

		for (int i = 0 ; i < names.length ; i++) {
			icons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[i]);
		}
	}
}
