package net.tropicraft.core.common.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWaterWand extends ItemTropicraft {

	public ItemWaterWand() {
		this.setMaxStackSize(1);
		this.setMaxDamage(2000);
	}

	/**
	 * Triggered on item use
	 * @param world World
	 * @param player Player holding the water wand
	 * @param hand Hand holding the water wand
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		double inc = Math.PI/12;
		
		ItemStack itemstack = player.getHeldItem(hand);
		
		player.swingArm(EnumHand.MAIN_HAND);
		if (!world.isRemote) {
			for (double lat = 0; lat < 2 * Math.PI; lat += inc) {
				for (double lng = 0; lng < 2 * Math.PI; lng += inc) {
					for (double len = 1; len < 3; len += 0.5D) {
						int x1 = (int)(Math.cos(lat) * len);
						int z1 = (int)(Math.sin(lat) * len);
						int y1 = (int)(Math.sin(lng) * len);
						BlockPos pos = new BlockPos(player.posX + x1, player.posY + y1, player.posZ + z1);
						if (!removeWater(world, itemstack, player, pos)) {
							break;
						}
					}
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
	}

	/**
	 * Perform actual removal of water
	 * @param world World
	 * @param itemstack Item in hand
	 * @param player Player
	 * @param x X coord
	 * @param y Y coord
	 * @param z Z coord
	 * @return Whether the block was successfully removed or not
	 */
	private boolean removeWater(World world, ItemStack itemstack, EntityPlayer player, BlockPos pos) {
		if (!world.isRemote) {
			if (world.getBlockState(pos).getMaterial() == Material.WATER) {
				itemstack.damageItem(1, player);
				world.setBlockToAir(pos);
				return true;
			}

			if(world.isAirBlock(pos)) {
				return true;
			}
		}

		return false;
	}
}
