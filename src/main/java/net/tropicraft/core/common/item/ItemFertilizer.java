package net.tropicraft.core.common.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockIris;
import net.tropicraft.core.common.block.BlockPineapple;
import net.tropicraft.core.common.block.BlockTallPlant;
import net.tropicraft.core.common.block.BlockTropicsFlowers;
import net.tropicraft.core.common.block.BlockTropicsSapling;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.registry.BlockRegistry;

public class ItemFertilizer extends ItemTropicraft {

	public ItemFertilizer() {
		super();
		this.maxStackSize = 64;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block == BlockRegistry.saplings) {
			if (!world.isRemote) {
				((BlockTropicsSapling) BlockRegistry.saplings).grow(world, world.rand, pos, state);
				itemstack.stackSize--;
			}
			return EnumActionResult.SUCCESS;
		}

		boolean decrementStack = false;

		if (block instanceof IGrowable) {
			IGrowable igrowable = (IGrowable)block;
			if (igrowable.canGrow(world, pos, state, world.isRemote)) {
				if (!world.isRemote) {
					if (igrowable.canUseBonemeal(world, world.rand, pos, state)) {
						decrementStack = true;
						igrowable.grow(world, world.rand, pos, state);
					}
				}
			}
		}

		if (block == Blocks.GRASS) {
			if (!world.isRemote) {
				decrementStack = true;
				label0:
					for (int j1 = 0; j1 < 128; j1++) {
						int k1 = pos.getX();
						int l1 = pos.getY() + 1;
						int i2 = pos.getZ();
						for (int j2 = 0; j2 < j1 / 16; j2++) {
							k1 += itemRand.nextInt(3) - 1;
							l1 += ((itemRand.nextInt(3) - 1) * itemRand.nextInt(3)) / 2;
							i2 += itemRand.nextInt(3) - 1;
							BlockPos pos2 = new BlockPos(k1, l1, i2);
							if (world.getBlockState(pos2.down()).getBlock() != Blocks.GRASS || world.isBlockNormalCube(pos2, true)) {
								continue label0;
							}
						}
						BlockPos pos2 = new BlockPos(k1, l1, i2);
						if (!world.isAirBlock(pos2)) {
							continue;
						}

						if (itemRand.nextInt(7) == 0) {
							world.setBlockState(pos2, getRandomTropicraftFlowerBlockState(itemRand), 3);
							continue;
						}

						if (itemRand.nextInt(9) == 0) {
							world.setBlockState(pos2, getRandomTropicraftFlowerBlockState(itemRand), 3);
							continue;
						}

						if (itemRand.nextInt(9) == 0) {    //pineapple
							IBlockState pineappleBase = BlockRegistry.pineapple.getDefaultState().withProperty(BlockPineapple.HALF, BlockTallPlant.PlantHalf.LOWER)
									.withProperty(BlockPineapple.STAGE, BlockPineapple.TOTAL_GROW_TICKS);
							IBlockState pineappleTop = BlockRegistry.pineapple.getDefaultState().withProperty(BlockPineapple.HALF, BlockTallPlant.PlantHalf.UPPER);
							world.setBlockState(pos2, pineappleBase, 3);
							world.setBlockState(pos2.up(), pineappleTop, 3);
							continue;
						} else if (itemRand.nextInt(8) == 0) {        //iris
							IBlockState irisBase = BlockRegistry.iris.getDefaultState().withProperty(BlockIris.HALF, BlockIris.PlantHalf.LOWER);
							IBlockState irisTop = BlockRegistry.iris.getDefaultState().withProperty(BlockIris.HALF, BlockIris.PlantHalf.UPPER);
							world.setBlockState(pos2, irisBase, 3);
							world.setBlockState(pos2.up(), irisTop, 3);
							continue;
						}
					}
			}

			if (decrementStack)
				itemstack.stackSize--;

			return EnumActionResult.SUCCESS;
		}

		if (decrementStack)
			itemstack.stackSize--;

		return EnumActionResult.FAIL;
	}

	private IBlockState getRandomTropicraftFlowerBlockState(Random rand) {
		int meta = itemRand.nextInt(TropicraftFlowers.VALUES.length);
		TropicraftFlowers flowerEnum = TropicraftFlowers.VALUES[meta];

		return BlockRegistry.flowers.getDefaultState().withProperty(BlockTropicsFlowers.VARIANT, flowerEnum);
	}
}
