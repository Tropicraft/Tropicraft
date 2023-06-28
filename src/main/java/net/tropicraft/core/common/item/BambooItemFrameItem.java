package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player playerentity = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();
        if (playerentity != null && !this.mayPlace(playerentity, direction, itemstack, blockpos1)) {
           return InteractionResult.FAIL;
        } else {
           Level world = context.getLevel();
           HangingEntity hangingentity = new BambooItemFrame(world, blockpos1, direction);

           CompoundTag compoundnbt = itemstack.getTag();
           if (compoundnbt != null) {
              EntityType.updateCustomEntityTag(world, playerentity, hangingentity, compoundnbt);
           }

           if (hangingentity.survives()) {
              if (!world.isClientSide) {
                 hangingentity.playPlacementSound();
                 world.addFreshEntity(hangingentity);
              }

              itemstack.shrink(1);
           }

           return InteractionResult.SUCCESS;
        }
     }

     @Override
    protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos pos) {
        return !player.level().isOutsideBuildHeight(pos) && player.mayUseItemAt(pos, direction, itemStack);
    }
}