package net.tropicraft.core.common.item;

import java.util.Random;

import net.minecraft.block.BlockTallGrass;
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
import net.minecraftforge.event.ForgeEventFactory;
import net.tropicraft.core.common.block.BlockIris;
import net.tropicraft.core.common.block.BlockPineapple;
import net.tropicraft.core.common.block.BlockTallPlant;
import net.tropicraft.core.common.block.BlockTropicsFlowers;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.registry.BlockRegistry;
import scala.collection.mutable.Stack;

public class ItemFertilizer extends ItemTropicraft {

	public ItemFertilizer() {
		super();
		this.maxStackSize = 64;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = world.getBlockState(pos);
        ItemStack heldStack = player.getHeldItem(hand);
        
        if (iblockstate.getBlock() == Blocks.GRASS && world.provider.getDimensionType() == TropicraftWorldUtils.tropicsDimension) {
            if (!world.isRemote) {

                // Taken from BlockGrass and reworked to spawn tropics flowers
                BlockPos blockpos = pos.up();

                for (int i = 0; i < 128; ++i) {
                    BlockPos blockpos1 = blockpos;
                    int j = 0;

                    while (true) {
                        if (j >= i / 16) {
                            if (world.isAirBlock(blockpos1)) {
                                if (itemRand.nextInt(8) == 0) {
                                    if (itemRand.nextBoolean()) { // pineapple
                                        IBlockState pineappleBase = BlockRegistry.pineapple.getDefaultState().withProperty(BlockPineapple.HALF, BlockTallPlant.PlantHalf.LOWER)
                                                .withProperty(BlockPineapple.STAGE, BlockPineapple.TOTAL_GROW_TICKS);
                                        IBlockState pineappleTop = BlockRegistry.pineapple.getDefaultState().withProperty(BlockPineapple.HALF, BlockTallPlant.PlantHalf.UPPER);
                                        world.setBlockState(blockpos1, pineappleBase, 3);
                                        world.setBlockState(blockpos1.up(), pineappleTop, 3);
                                        continue;
                                    } else { // iris
                                        IBlockState irisBase = BlockRegistry.iris.getDefaultState().withProperty(BlockIris.HALF, BlockIris.PlantHalf.LOWER);
                                        IBlockState irisTop = BlockRegistry.iris.getDefaultState().withProperty(BlockIris.HALF, BlockIris.PlantHalf.UPPER);
                                        world.setBlockState(blockpos1, irisBase, 3);
                                        world.setBlockState(blockpos1.up(), irisTop, 3);
                                        continue;
                                    }
                                } else {
                                    IBlockState flowerstate = getRandomTropicraftFlowerBlockState(itemRand);
                                    if (BlockRegistry.flowers.canBlockStay(world, blockpos1, flowerstate)) {
                                        world.setBlockState(blockpos1, flowerstate, 3);
                                    }
                                }
                            }
                            break;
                        }

                        blockpos1 = blockpos1.add(itemRand.nextInt(3) - 1, (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2, itemRand.nextInt(3) - 1);

                        if (world.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS || world.getBlockState(blockpos1).isNormalCube()) {
                            break;
                        }

                        ++j;
                    }
                }
                heldStack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }

        int hook = ForgeEventFactory.onApplyBonemeal(player, world, pos, iblockstate, heldStack, hand);
        if (hook != 0)
            return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();

            if (igrowable.canGrow(world, pos, iblockstate, world.isRemote)) {
                if (!world.isRemote) {
                    if (igrowable.canUseBonemeal(world, world.rand, pos, iblockstate)) {
                        igrowable.grow(world, world.rand, pos, iblockstate);
                    }

                    heldStack.shrink(1);
                    world.playEvent(2005, pos, 0);
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.FAIL;
    }

	private IBlockState getRandomTropicraftFlowerBlockState(Random rand) {
		int meta = itemRand.nextInt(TropicraftFlowers.VALUES.length);
		TropicraftFlowers flowerEnum = TropicraftFlowers.VALUES[meta];

		return BlockRegistry.flowers.getDefaultState().withProperty(BlockTropicsFlowers.VARIANT, flowerEnum);
	}
}
