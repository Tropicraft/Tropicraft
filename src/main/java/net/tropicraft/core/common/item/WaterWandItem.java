package net.tropicraft.core.common.item;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class WaterWandItem extends Item {
    public WaterWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        double inc = Math.PI / 12;

        ItemStack itemstack = player.getItemInHand(hand);

        player.swing(Hand.MAIN_HAND);
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

        return new ActionResult<>(ActionResultType.PASS, itemstack);
    }

    private boolean removeWater(World world, ItemStack itemstack, PlayerEntity player, BlockPos pos) {
        if (!world.isClientSide) {
            if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                itemstack.hurtAndBreak(1, player, (e) -> {
                    e.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
                });
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return true;
            }

            return world.isEmptyBlock(pos);
        }

        return false;
    }
}
