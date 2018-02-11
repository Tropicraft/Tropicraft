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
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.core.common.block.BlockPineapple;
import net.tropicraft.core.common.block.BlockTallPlant.PlantHalf;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockEvents {

	@SubscribeEvent
	public void handlePineappleBreakEvent(HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();

		ItemStack held = ItemStack.EMPTY;
		if (player != null)
			held = player.getHeldItemMainhand();

		IBlockState state = event.getState();
		if (state.getBlock() != BlockRegistry.pineapple) {
			return;
		}

		IBlockState stateUp = event.getWorld().getBlockState(event.getPos().up());

		boolean isTop = state.getValue(BlockPineapple.HALF) == PlantHalf.UPPER;
		boolean isGrown = isTop ||
				(state.getValue(BlockPineapple.HALF) == PlantHalf.LOWER
				&& stateUp.getBlock() instanceof BlockPineapple
				&& stateUp.getValue(BlockPineapple.HALF) == PlantHalf.UPPER);

		if (isGrown) {
			if (!held.isEmpty() && held.getItem() instanceof ItemSword) {
				event.getDrops().add(new ItemStack(ItemRegistry.pineappleCubes, event.getWorld().rand.nextInt(3) + 2));
			} else {
				event.getDrops().add(new ItemStack(BlockRegistry.pineapple));
			}
		}

		// If the stem remains after a block break, reset its growth stage so it doesn't insta-create a pineapple
		if (!isTop) {
			event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockPineapple.STAGE, Integer.valueOf(1)));
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

		ItemStack handItemStack = player.getHeldItemMainhand();
		Item inHand;

		if (!handItemStack.isEmpty())
			inHand = handItemStack.getItem();
		else
			inHand = null;

		ItemStack drop = ItemStack.EMPTY;

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
}
