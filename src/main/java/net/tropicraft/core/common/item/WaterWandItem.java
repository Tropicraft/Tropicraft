package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class WaterWandItem extends Item {
    public WaterWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        double inc = Math.PI / 12;

        ItemStack itemstack = player.getItemInHand(hand);
        EquipmentSlot slot = LivingEntity.getSlotForHand(hand);

        player.swing(hand);
        if (!world.isClientSide) {
            for (double lat = 0; lat < 2 * Math.PI; lat += inc) {
                for (double lng = 0; lng < 2 * Math.PI; lng += inc) {
                    for (double len = 1; len < 3; len += 0.5D) {
                        int x1 = (int)(Math.cos(lat) * len);
                        int z1 = (int)(Math.sin(lat) * len);
                        int y1 = (int)(Math.sin(lng) * len);
                        if (!removeWater(world, itemstack, player, new BlockPos(player.blockPosition().offset(x1, y1, z1)), slot)) {
                            break;
                        }
                    }
                }
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
    }

    private boolean removeWater(Level world, ItemStack itemstack, Player player, BlockPos pos, EquipmentSlot slot) {
        if (!world.isClientSide) {
            if (world.getFluidState(pos).is(FluidTags.WATER)) {
                itemstack.hurtAndBreak(1, player, slot);
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return true;
            }

            return world.isEmptyBlock(pos);
        }

        return false;
    }
}
