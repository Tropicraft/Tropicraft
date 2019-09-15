package net.tropicraft.core.common.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HangingEntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class BambooItemFrameItem extends HangingEntityItem {
    public BambooItemFrameItem(Item.Properties builder) {
        super(TropicraftEntities.BAMBOO_ITEM_FRAME, builder);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos blockpos = context.getPos();
        Direction direction = context.getFace();
        BlockPos blockpos1 = blockpos.offset(direction);
        PlayerEntity playerentity = context.getPlayer();
        ItemStack itemstack = context.getItem();
        if (playerentity != null && !this.canPlace(playerentity, direction, itemstack, blockpos1)) {
            return ActionResultType.FAIL;
        } else {
            World world = context.getWorld();
            BambooItemFrame hangingEntity = new BambooItemFrame(TropicraftEntities.BAMBOO_ITEM_FRAME, world);
            hangingEntity.setHangingPosition(blockpos);
            hangingEntity.updateFacingWithBoundingBox(direction);
            hangingEntity.setDisplayedItem(itemstack);

            CompoundNBT compoundnbt = itemstack.getTag();
            if (compoundnbt != null) {
                EntityType.applyItemNBT(world, playerentity, hangingEntity, compoundnbt);
            }

            if (hangingEntity.onValidSurface()) {
                if (!world.isRemote) {
                    hangingEntity.playPlaceSound();
                    world.addEntity(hangingEntity);
                }

                itemstack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }

    protected boolean canPlace(PlayerEntity player, Direction direction, ItemStack itemStack, BlockPos pos) {
        return !World.isOutsideBuildHeight(pos) && player.canPlayerEdit(pos, direction, itemStack);
    }
}