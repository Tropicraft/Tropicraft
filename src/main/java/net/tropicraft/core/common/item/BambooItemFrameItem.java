package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.BambooItemFrame;

public class BambooItemFrameItem extends HangingEntityItem {
    public BambooItemFrameItem(Item.Properties builder) {
        // Game crashes if we pass to super, we don't need it anyway
        super(null, builder);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos relativePos = pos.relative(clickedFace);
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (player != null && !mayPlace(player, clickedFace, itemStack, relativePos)) {
            return InteractionResult.FAIL;
        }

        Level level = context.getLevel();
        HangingEntity itemFrame = new BambooItemFrame(level, relativePos, clickedFace);

        CustomData entityData = itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        if (!entityData.isEmpty()) {
            EntityType.updateCustomEntityTag(level, player, itemFrame, entityData);
        }

        if (itemFrame.survives()) {
            if (!level.isClientSide) {
                itemFrame.playPlacementSound();
                level.addFreshEntity(itemFrame);
            }

            itemStack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos pos) {
        return !player.level().isOutsideBuildHeight(pos) && player.mayUseItemAt(pos, direction, itemStack);
    }
}
