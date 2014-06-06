package net.tropicraft.event;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCBlockEvents {
	
	@SubscribeEvent
	public void handlePineappleBreakEvent(HarvestDropsEvent event) {
		World world = event.world;
		Block block = event.block;
		int meta = event.blockMetadata;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		EntityPlayer player = event.harvester;
		
		if (world.isRemote)
			return;
		
/*		System.out.printf("At the start: id=%s meta=%d idabove=%s idbelow=%s\n", 
				world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), world.getBlock(x, y + 1, z),
				world.getBlock(x, y - 1, z));*/
		
		//int meta = world.getBlockMetadata(x, y, z);

		ItemStack handItemStack = getPlayerItem(world, player);
		Item inHand;
		
		if (handItemStack != null)
			inHand = handItemStack.getItem();
		else
			inHand = null;
		
		ItemStack drop = null;
		
		if (inHand != null && (inHand instanceof ItemSword || inHand.getItemUseAction(new ItemStack(inHand)) == EnumAction.block))
			drop = new ItemStack(TCItemRegistry.pineappleCubes);
		else
			drop = new ItemStack(block, 1, 0);
		
		int numDrops = meta == 8 && drop.getItem() == TCItemRegistry.pineappleCubes ? world.rand.nextInt(3) + 2 : 1;

		// If it is the top block that is destroyed, drop a pineapple item
		// Then set the metadata of the bottom to 0 so it can restart its
		// Growth cycle
		if (meta == 8) {
			System.out.println("meta 8" + drop.getDisplayName());
			world.setBlockMetadataWithNotify(x, y - 1, z, 0, 3);
			world.setBlockToAir(x, y, z);
			for (int i = 0; i < numDrops; i++)
				dropBlockAsItem(world, x, y, z, drop);
		} else
			if (meta <= 7) {
				//System.out.println("wut");
				if (world.getBlockMetadata(x, y + 1, z) == 8) {
					System.out.println("wut 1");
					world.setBlockToAir(x, y + 1, z);
					dropBlockAsItem(world, x, y + 1, z, drop);
				} else {
					System.out.println("wut 2 " + meta + " " + world.getBlockMetadata(x, y - 1, z) + " " + 
				world.getBlock(x, y - 1, z));
					world.setBlockToAir(x, y, z);
				}
			} else {
				System.out.println("wut in the but");
				world.setBlockToAir(x, y, z);
				world.setBlockToAir(x, y - 1, z);
			}
	}
	
	   /**
     * Spawns EntityItem in the world for the given ItemStack if the world is not remote.
     */
    protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack p_149642_5_)
    {
        if (!p_149642_1_.isRemote && p_149642_1_.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float f = 0.7F;
            double d0 = (double)(p_149642_1_.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(p_149642_1_.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(p_149642_1_.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(p_149642_1_, (double)p_149642_2_ + d0, (double)p_149642_3_ + d1, (double)p_149642_4_ + d2, p_149642_5_);
            entityitem.delayBeforeCanPickup = 10;
            p_149642_1_.spawnEntityInWorld(entityitem);
        }
    }
	
	private ItemStack getPlayerItem(World world, EntityPlayer player) {
		if (player == null)
			return null;
		
		ItemStack inHand = player.inventory.getStackInSlot(player.inventory.currentItem);

		return inHand;
	}
}
