package net.tropicraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import net.tropicraft.entity.underdasea.EntityTropicraftWaterMob.WaterMobType;

public class ItemWaterWand extends ItemTropicraft {

	public ItemWaterWand() {
		this.setMaxStackSize(1);
		this.setMaxDamage(2000);
	}

	/**
	 * Triggered on item use
	 * @param itemstack Item in hand (water wand)
	 * @param world World
	 * @param player Player holding the water wand
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		double inc = Math.PI/12;
		if(!world.isRemote){
			/*for(double lat = 0; lat < 2 * Math.PI; lat += inc){
				for( double lng = 0; lng < 2 * Math.PI; lng += inc){
					for(double len = 1; len < 3; len += 0.5D){
						int x1 = (int)(Math.cos(lat) * len);
						int z1 = (int)(Math.sin(lat) * len);
						int y1 = (int)(Math.sin(lng) * len);
						if (!removeWater(world, itemstack, player, (int)player.posX + x1, (int)(player.posY) + y1, (int)(player.posZ) + z1)){
							break;
						}
					}
				}
			}*/
		//	player.setItemInUse(itemstack, 1);
			EntitySeahorse seahorse = new EntitySeahorse(world);
			seahorse.setLocationAndAngles(player.posX, player.posY - 2, player.posZ, 0, 0);
			world.spawnEntityInWorld(seahorse);
		}

		return itemstack;
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
	private boolean removeWater(World world, ItemStack itemstack, EntityPlayer player, int x, int y, int z) {
		if (!world.isRemote) {

			if (world.getBlock(x, y, z).getMaterial() == Material.water) {
				itemstack.damageItem(1, player);
				world.setBlockToAir(x, y, z);
				return true;
			}

			if(world.isAirBlock(x, y, z)) {
				return true;
			}
		}

		return false;
	}
}
