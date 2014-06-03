package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftSlab extends BlockSlab {

	@SideOnly(Side.CLIENT)
	private IIcon bambooIcon, thatchIcon, chunkIcon, palmIcon;

	public BlockTropicraftSlab(boolean isFullSlab) {
		super(isFullSlab, Material.rock);
		this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		int type = metadata & 7;

		switch(type) {
		case 0:
			return TCBlockRegistry.bambooBundle.getBlockTextureFromSide(side);
		case 1:
			return TCBlockRegistry.thatchBundle.getBlockTextureFromSide(side);
		case 2:
			return TCBlockRegistry.chunkOHead.getBlockTextureFromSide(side);
		case 3:
			return TCBlockRegistry.planks.getIcon(side, metadata);
		default:
			return TCBlockRegistry.bambooBundle.getBlockTextureFromSide(side);
		}
	}
	
    /**
     * Returns an item stack containing a single instance of the current block type. 'meta' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
	@Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(TCBlockRegistry.singleSlabs, 2, meta & 7);
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {

	}

	@Override
	public String func_150002_b(int meta) {
		if (meta < 0 || meta >= TCNames.slabTypes.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + TCNames.slabTypes[meta];
	}

	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(TCBlockRegistry.singleSlabs);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		if (item != Item.getItemFromBlock(TCBlockRegistry.doubleSlabs)) {
			for (int i = 0; i < TCNames.slabTypes.length; ++i) {
				list.add(new ItemStack(item, 1, i));
			}
		}
	}
	
    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return Item.getItemFromBlock(TCBlockRegistry.singleSlabs);
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
