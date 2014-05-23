package net.tropicraft.item;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockTropicraft extends ItemBlock {

	/** Names to associate with this ItemBlock */
	private String[] names;
	
	private Block block;
	
	public ItemBlockTropicraft(Block block, ArrayList<String> names) {
		super(block);
		this.block = block;
		this.names = names.toArray(new String[names.size()]);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
	@Override
    public int getMetadata(int id) {
        return id;
    }
	
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
	@Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (this.names == null) {
            return super.getUnlocalizedName(itemstack);
        }
        else {
            int i = itemstack.getItemDamage();
            return i >= 0 && i < this.names.length ? super.getUnlocalizedName(itemstack) + "_" + this.names[i] : super.getUnlocalizedName(itemstack);
        }
    }
	
    /**
     * Gets an icon index based on an item's damage value
     */
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.block.getIcon(0, meta);
    }
}
