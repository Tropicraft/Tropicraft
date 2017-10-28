package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;
import net.tropicraft.core.common.enums.TropicraftPlanks;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class WorldGenSunkenShip extends TCDirectionalGen {

	private static final IBlockState PLANK_STATE = BlockRegistry.planks.defaultForVariant(TropicraftPlanks.MAHOGANY);

	public WorldGenSunkenShip(World world, Random random) {
		super(world, random, random.nextInt(4));
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		this.setOrigin(i, k);

		if (TCGenUtils.getBlock(worldObj, i, j + 4, k) != BlockRegistry.tropicsWater) { // Must be water 4 blocks above the sea floor
			return false;
		}

		j += 1; // Move the "origin" up

		final int length = this.rand.nextInt(25) + 25;

		int y = j;

		while (true) { // Acting y loop
			boolean hasGenned = false;

			int fib = 2;
			int lastFib = 1;
			int width = y - j;
			for (int x = 0; x < length; x++) {
				if (x == fib && x <= (length / 3D)) {
					width++;
					fib += lastFib;
					lastFib = fib - lastFib;
				}

				if (x > length - 3) {
					width--;
				}

				if (width >= 0) {
					for (int z = -width; z <= width; z++) {
						if (rand.nextInt(5) < 3) {
							if (y == j || x == length - 1) {
								this.placeBlockWithDir(x, y, z, PLANK_STATE);
								if (z == -width || z == width || x == length - 1) {
									this.placeBlockWithDir(x, y + 1, z, PLANK_STATE);								
								}

								if (x == length / 2 && z == 0) {
									this.placeBlockWithDir(x, y + 1, z, PLANK_STATE);		
									this.placeBlockWithDir(x, y + 2, z, PLANK_STATE);		
									this.placeBlockWithDir(x, y + 3, z, PLANK_STATE);		
								}
							} else if (x == length / 2 && z == 0 && y == j - 2) {
								this.placeBlockWithDir(x, y, z, BlockRegistry.bambooChest.getDefaultState());	
								BlockPos pos2 = new BlockPos(x, y, z);
								TileEntityBambooChest chest = (TileEntityBambooChest)this.getTEWithDir(pos2);

								if(chest != null) {
									chest.setInventorySlotContents(0, this.randLoot());
								}
							} else if (z == -width || z == width) {
								this.placeBlockWithDir(x, y, z, PLANK_STATE);						
							} else {
								this.placeBlockWithDir(x, y, z, Blocks.AIR.getDefaultState());	
							}
						}
					}
					hasGenned = true;
				}
			}

			if (!hasGenned) {
				break;
			}

			y--;
		}

		return false;
	}

	public ItemStack randLoot() {
		int picker = rand.nextInt(20) + 1;
		if (picker < 6) {
			return new ItemStack(BlockRegistry.bambooShoot, rand.nextInt(20) + 1);
		} 

		switch (picker) {
		case 7:
			return new ItemStack(ItemRegistry.scaleHelmet, 1);
		case 8:
			return new ItemStack(ItemRegistry.scaleChestplate, 1);
		case 9:
			return new ItemStack(ItemRegistry.scaleBoots, 1);
		case 10:
			return new ItemStack(ItemRegistry.scale, rand.nextInt(3) + 1);
		case 11:
			return new ItemStack(ItemRegistry.eudialyte, rand.nextInt(3) + 1);
		case 12:
			return new ItemStack(ItemRegistry.azurite, rand.nextInt(4) + 2);
		case 13:
			return new ItemStack(ItemRegistry.scaleLeggings, 1);
		case 14:
			return new ItemStack(ItemRegistry.recordBuriedTreasure, 1);
		case 15:
			return new ItemStack(ItemRegistry.recordEasternIsles, 1);
		case 16:
			return new ItemStack(ItemRegistry.recordLowTide, 1);
		case 17:
			return new ItemStack(ItemRegistry.recordSummering, 1);
		case 18:
			return new ItemStack(ItemRegistry.recordTheTribe, 1);
		case 19:
			return new ItemStack(ItemRegistry.recordTradeWinds, 1);
		default:
			return new ItemStack(ItemRegistry.zircon, 1);
		}
		//
		//			return new ItemStack(TropicraftItems.coconutBomb, rand.nextInt(3) + 1); TODO
		//			return new ItemStack(ItemRegistry.shells, rand.nextInt(5) + 1, rand.nextInt(6));
		////			return new ItemStack(TropicraftItems.ashenMasks, 1, rand.nextInt(7)); TODO
	}
}