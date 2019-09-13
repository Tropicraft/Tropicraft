package net.tropicraft.core.common.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Map;

public class BambooFlowerPotBlock extends FlowerPotBlock {
    private static final Map<Block, Block> flowers = Maps.newHashMap();

    public BambooFlowerPotBlock(final Block block, final Properties properties) {
        super(block, properties);
        flowers.put(block, this);
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        Item item = itemstack.getItem();
        Block block = item instanceof BlockItem ? flowers.getOrDefault(((BlockItem)item).getBlock(), Blocks.AIR) : Blocks.AIR;
        boolean flag = block == Blocks.AIR;
        boolean flag1 = func_220276_d() == Blocks.AIR;
        if (flag != flag1) {
            if (flag1) {
                worldIn.setBlockState(pos, block.getDefaultState(), 3);
                player.addStat(Stats.POT_FLOWER);
                if (!player.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
            } else {
                ItemStack itemstack1 = new ItemStack(func_220276_d());
                if (itemstack.isEmpty()) {
                    player.setHeldItem(handIn, itemstack1);
                } else if (!player.addItemStackToInventory(itemstack1)) {
                    player.dropItem(itemstack1, false);
                }

                worldIn.setBlockState(pos, TropicraftBlocks.BAMBOO_FLOWER_POT.getDefaultState(), 3);
            }
        }

        return true;
    }
}
