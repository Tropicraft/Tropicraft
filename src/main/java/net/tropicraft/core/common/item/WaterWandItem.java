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

public class WaterWandItem extends Item {
    public WaterWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        double inc = Math.PI / 12;

        ItemStack itemstack = player.getHeldItem(hand);

        player.swingArm(Hand.MAIN_HAND);
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

        return new ActionResult<>(ActionResultType.PASS, itemstack);
    }

    private boolean removeWater(World world, ItemStack itemstack, PlayerEntity player, BlockPos pos) {
        if (!world.isRemote) {
            if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                itemstack.damageItem(1, player, (e) -> {
                    e.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return true;
            }

            return world.isAirBlock(pos);
        }

        return false;
    }
}
