package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

public class AshenMaskItem extends Item {
    public AshenMaskItem(Properties properties) {
        super(TropicraftArmorMaterials.applyNoDurability(properties, TropicraftArmorMaterials.ASHEN_MASK, ArmorType.HELMET));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos offsetPos = pos.relative(direction);
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (player != null && !canPlace(player, direction, itemStack, offsetPos)) {
            return InteractionResult.FAIL;
        } else {
            Level world = context.getLevel();
            WallItemEntity wallItem = new WallItemEntity(world, offsetPos, direction);
            wallItem.setItem(itemStack);

            if (wallItem.survives()) {
                if (!world.isClientSide()) {
                    wallItem.playPlacementSound();
                    world.addFreshEntity(wallItem);
                }

                itemStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
    }

    private boolean canPlace(Player player, Direction direction, ItemStack heldStack, BlockPos pos) {
        return player.mayUseItemAt(pos, direction, heldStack);
    }
}
