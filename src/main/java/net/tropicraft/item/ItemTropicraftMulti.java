package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ItemTropicraft superclass that should be implemented if you want to make an item
 * with different names for each damage value, and different images for each
 * damage value as well.
 */
public class ItemTropicraftMulti extends ItemTropicraft {

	/** Array of icons associated with this item */
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	/** Names of each of the images */
	private String[] names;
	
	public ItemTropicraftMulti(String[] names) {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.maxStackSize = 64;
		this.names = names;
	}
	
    /**
     * Gets an icon index based on an item's damage value
     */
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        int j = MathHelper.clamp_int(par1, 0, names.length - 1);
        return this.icons[j];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
	@Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "_" + names[i];
    }
	
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < names.length; i++) {
        	list.add(new ItemStack(item, 1, i));
        }
    }
	
	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		icons = new IIcon[names.length];
		
		for (int i = 0 ; i < names.length ; i++) {
			icons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[i]);
		}
	}

}
