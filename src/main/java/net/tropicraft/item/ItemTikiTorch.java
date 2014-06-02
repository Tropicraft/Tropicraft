package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemTikiTorch extends ItemTropicraft {

	public ItemTikiTorch() {
		this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
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
        else if (y == 255 && block.getMaterial().isSolid()) {
            return false;
        }
        else if (world.canPlaceEntityOnSide(block, x, y, z, false, side, player, itemstack)) {            
            Block blockBelow = world.getBlock(x, y - 1, z);
            if (blockBelow instanceof BlockFence) {
                world.setBlock(x, y, z, TCBlockRegistry.tikiTorch, 0, 3);
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), TCBlockRegistry.tikiTorch.stepSound.soundName, (TCBlockRegistry.tikiTorch.stepSound.getVolume() + 1.0F) / 2.0F, TCBlockRegistry.tikiTorch.stepSound.getPitch() * 0.8F);
                TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
                itemstack.stackSize--;
                return true;
            } else if (world.isAirBlock(x, y + 1, z) && world.isAirBlock(x, y + 2, z)) {
            	TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            	TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
            	TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
                world.setBlock(x, y, z, TCBlockRegistry.tikiTorch, 1, 3);
                world.setBlock(x, y + 1, z, TCBlockRegistry.tikiTorch, 1, 3);
                world.setBlock(x, y + 2, z, TCBlockRegistry.tikiTorch, 0, 3);
                TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
                TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
                TCBlockRegistry.tikiTorch.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, 0);
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), TCBlockRegistry.tikiTorch.stepSound.soundName, (TCBlockRegistry.tikiTorch.stepSound.getVolume() + 1.0F) / 2.0F, TCBlockRegistry.tikiTorch.stepSound.getPitch() * 0.8F);
                itemstack.stackSize--;
                return true;
            }

            return true;
        } else {
            return false;
        }
    }

}
