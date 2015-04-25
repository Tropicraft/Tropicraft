package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.block.BlockTropicraftSapling;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemFertilizer extends ItemTropicraft {

	public ItemFertilizer() {
		super();
		this.maxStackSize = 64;
		setCreativeTab(TCCreativeTabRegistry.tabMaterials);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {
		Block block = world.getBlock(i, j, k);
		if (block == TCBlockRegistry.saplings) {
			if (!world.isRemote) {
				((BlockTropicraftSapling) TCBlockRegistry.saplings).markOrGrowMarked(world, i, j, k, world.rand);
				itemstack.stackSize--;
			}
			return true;
		}

		boolean decrementStack = false;

		if (block instanceof IGrowable) {
			IGrowable igrowable = (IGrowable)block;

			if (igrowable.func_149851_a(world, i, j, k, world.isRemote)) {
				if (!world.isRemote) {
					if (igrowable.func_149852_a(world, world.rand, i, j, k)) {
						decrementStack = true;
						igrowable.func_149853_b(world, world.rand, i, j, k);
					}
				}
			}
		}

		if (block == Blocks.grass) {
			if (!world.isRemote) {
				decrementStack = true;
				label0:
					for (int j1 = 0; j1 < 128; j1++) {
						int k1 = i;
						int l1 = j + 1;
						int i2 = k;
						for (int j2 = 0; j2 < j1 / 16; j2++) {
							k1 += itemRand.nextInt(3) - 1;
							l1 += ((itemRand.nextInt(3) - 1) * itemRand.nextInt(3)) / 2;
							i2 += itemRand.nextInt(3) - 1;
							if (world.getBlock(k1, l1 - 1, i2) != Blocks.grass || world.isBlockNormalCubeDefault(k1, l1, i2, true)) {
								continue label0;
							}
						}

						if (!world.isAirBlock(k1, l1, i2)) {
							continue;
						}
						if (itemRand.nextInt(9) == 0) {
							world.setBlock(k1, l1, i2, TCBlockRegistry.flowers, itemRand.nextInt(16), 3);
							continue;
						}
						if (itemRand.nextInt(9) == 0) {
							world.setBlock(k1, l1, i2, TCBlockRegistry.flowers, itemRand.nextInt(16), 3);
							continue;
						}
						if (itemRand.nextInt(9) == 0) {    //pineapple
							world.setBlock(k1, l1, i2, TCBlockRegistry.pineapple, 7, 3);
							world.setBlock(k1, l1 + 1, i2, TCBlockRegistry.pineapple, 8, 3);
							continue;
						} else if (itemRand.nextInt(8) == 0) {        //iris
							world.setBlock(k1, l1, i2, TCBlockRegistry.tallFlowers, 0, 3);
							world.setBlock(k1, l1 + 1, i2, TCBlockRegistry.tallFlowers, 1, 3);
							continue;
						}
					}
			}
			
			if (decrementStack)
				itemstack.stackSize--;

			return true;
		}

		if (decrementStack)
			itemstack.stackSize--;

		return false;
	}
}
