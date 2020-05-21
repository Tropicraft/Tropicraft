package net.tropicraft.core.common.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
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
        // Game crashes if we pass to super, we don't need it anyway
        super(null, builder);
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
           HangingEntity hangingentity = new BambooItemFrame(world, blockpos1, direction);

           CompoundNBT compoundnbt = itemstack.getTag();
           if (compoundnbt != null) {
              EntityType.applyItemNBT(world, playerentity, hangingentity, compoundnbt);
           }

           if (hangingentity.onValidSurface()) {
              if (!world.isRemote) {
                 hangingentity.playPlaceSound();
                 world.addEntity(hangingentity);
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