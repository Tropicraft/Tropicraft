package net.tropicraft.core.common.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.core.common.block.BlockPineapple;
import net.tropicraft.core.common.block.BlockTallPlant;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockEvents {
    
    @SubscribeEvent
    public void Table_Additives(LootTableLoadEvent event){
        String name = event.getName().toString();

        System.err.println("LOADING LOOT:" + name);
    }

	@SubscribeEvent
	public void handlePineappleBreakEvent(HarvestDropsEvent event) {
		World world = event.getWorld();
		Block block = event.getState().getBlock();

		if (world.isRemote) {
			return;
		}

		if (block != BlockRegistry.pineapple) {
			return;
		}		

		int stage = event.getState().getValue(BlockPineapple.STAGE);
		boolean isFullyGrown = ((BlockTallPlant.PlantHalf)event.getState().getValue(BlockPineapple.HALF)) == BlockTallPlant.PlantHalf.UPPER
				&& stage >= BlockPineapple.TOTAL_GROW_TICKS;
				int x = event.getPos().getX();
				int y = event.getPos().getY();
				int z = event.getPos().getZ();
				EntityPlayer player = event.getHarvester();

				ItemStack handItemStack = getPlayerItem(world, player);
				Item inHand;

				if (handItemStack != null)
					inHand = handItemStack.getItem();
				else
					inHand = null;

				ItemStack drop = null;

				if (inHand != null && (inHand instanceof ItemSword || inHand.getItemUseAction(new ItemStack(inHand)) == EnumAction.BLOCK)) {
					drop = new ItemStack(ItemRegistry.pineappleCubes);
				} else {
					drop = new ItemStack(block, 1, 0);
				}

				int numDrops = isFullyGrown && drop.getItem() == ItemRegistry.pineappleCubes ? world.rand.nextInt(3) + 2 : 1;

				// If it is the top block that is destroyed, drop a pineapple item
				// Then set the metadata of the bottom to 0 so it can restart its
				// Growth cycle
				if (isFullyGrown) {
					world.setBlockState(event.getPos().down(), event.getState().withProperty(BlockPineapple.STAGE, Integer.valueOf(1)));
					world.setBlockToAir(event.getPos());
					for (int i = 0; i < numDrops; i++) {
						dropBlockAsItem(world, x, y, z, drop);
					}
				} else
					if (stage < BlockPineapple.TOTAL_GROW_TICKS) {
						IBlockState stateUp = world.getBlockState(event.getPos().up());

						if (stateUp.getBlock() instanceof BlockPineapple) {
							boolean aboveBlockFullyGrown = stateUp.getValue(BlockPineapple.STAGE) > BlockPineapple.TOTAL_GROW_TICKS;
							if (aboveBlockFullyGrown) {
								world.setBlockToAir(event.getPos().up());
								dropBlockAsItem(world, x, y + 1, z, drop);
							} else {
								world.setBlockToAir(event.getPos());
							}	
						}
					} else {
						world.setBlockToAir(event.getPos());
						world.setBlockToAir(event.getPos().down());
					}
	}

	@SubscribeEvent
	public void handleCoconutBreakEvent(HarvestDropsEvent event) {
		World world = event.getWorld();
		Block block = event.getState().getBlock();
		int x = event.getPos().getX();
		int y = event.getPos().getY();
		int z = event.getPos().getZ();
		EntityPlayer player = event.getHarvester();

		if (world.isRemote)
			return;

		if (block != BlockRegistry.coconut)
			return;

		ItemStack handItemStack = getPlayerItem(world, player);
		Item inHand;

		if (handItemStack != null)
			inHand = handItemStack.getItem();
		else
			inHand = null;

		ItemStack drop = null;

		if (inHand != null && (inHand instanceof ItemSword || inHand.getItemUseAction(new ItemStack(inHand)) == EnumAction.BLOCK))
			drop = new ItemStack(ItemRegistry.coconutChunk);
		else
			drop = new ItemStack(block, 1, 0);

		int numDrops = drop.getItem() == ItemRegistry.coconutChunk ? world.rand.nextInt(3) + 2 : 1;

		world.setBlockToAir(event.getPos());

		for (int i = 0; i < numDrops; i++)
			dropBlockAsItem(world, x, y, z, drop);
	}

	/**
	 * Spawns EntityItem in the world for the given ItemStack if the world is not remote.
	 */
	protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
		if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
			float f = 0.7F;
			double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack);
			entityitem.setPickupDelay(10);
			world.spawnEntity(entityitem);
		}
	}

	private ItemStack getPlayerItem(World world, EntityPlayer player) {
		if (player == null)
			return null;

		ItemStack inHand = player.inventory.getStackInSlot(player.inventory.currentItem);

		return inHand;
	}
}
