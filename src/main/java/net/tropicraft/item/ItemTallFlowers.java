package net.tropicraft.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTallFlowers extends ItemBlockTropicraft {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	public ItemTallFlowers(Block block, ArrayList<String> names) {
		super(block, names);
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
			icons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "Item_" + names[i]);
		}
	}
	
    /**
     * Gets an icon index based on an item's damage value
     */
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return icons[damage - 1];
    }
    
    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {

       if (!world.setBlock(x, y, z, block, 0, 3)) {
           return false;
       }

       if (world.getBlock(x, y, z) == block) {
           block.onBlockPlacedBy(world, x, y, z, player, stack);
           block.onPostBlockPlaced(world, x, y, z, metadata);
       }

       return true;
    }
    
    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1) {
        return par1;
    }
    
	/**
	 * returns a list of items with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < this.icons.length; ++i) {
			list.add(new ItemStack(item, 1, i + 1));
		}
	}
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);

        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z)) {
            if (side == 0) {
                --y;
            }

            if (side == 1) {
                ++y;
            }

            if (side == 2) {
                --z;
            }

            if (side == 3) {
                ++z;
            }

            if (side == 4) {
                --x;
            }

            if (side == 5) {
                ++x;
            }
        }

        if (itemstack.stackSize == 0) {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, side, itemstack)) {
            return false;
        }
        else if (y == 255 && this.block.getMaterial().isSolid()) {
            return false;
        }
        else if (world.canPlaceEntityOnSide(this.block, x, y, z, false, side, player, itemstack)) {
            int meta = this.block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);

            if (placeBlockAt(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ, meta)) {
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.block.stepSound.func_150496_b(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getPitch() * 0.8F);
                --itemstack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }
}
