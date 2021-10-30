package net.tropicraft.core.common.item;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class WaterWandItem extends Item {
    public WaterWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        double inc = Math.PI / 12;

        ItemStack itemstack = player.getItemInHand(hand);

        player.swing(InteractionHand.MAIN_HAND);
        if (!world.isClientSide) {
            for (double lat = 0; lat < 2 * Math.PI; lat += inc) {
                for (double lng = 0; lng < 2 * Math.PI; lng += inc) {
                    for (double len = 1; len < 3; len += 0.5D) {
                        int x1 = (int)(Math.cos(lat) * len);
                        int z1 = (int)(Math.sin(lat) * len);
                        int y1 = (int)(Math.sin(lng) * len);
                        if (!removeWater(world, itemstack, player, new BlockPos(player.blockPosition().offset(x1, y1, z1)))) {
                            break;
                        }
                    }
                }
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
    }

    private boolean removeWater(Level world, ItemStack itemstack, Player player, BlockPos pos) {
        if (!world.isClientSide) {
            if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                itemstack.hurtAndBreak(1, player, (e) -> {
                    e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return true;
            }

            return world.isEmptyBlock(pos);
        }

        return false;
    }
}
