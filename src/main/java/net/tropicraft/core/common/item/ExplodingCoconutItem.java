package net.tropicraft.core.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ExplodingCoconutItem extends Item {
    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(1);
        }
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
        if (!world.isRemote) {
            // TODO - implement config file: boolean canPlayerThrow = ArrayUtils.contains(TropicsConfigs.coconutBombWhitelist, player.getGameProfile().getName());
            final boolean canPlayerThrow = player.isCreative() || player.canUseCommandBlock();
            if (canPlayerThrow) {
                SnowballEntity explodingCoconutEntity = new SnowballEntity(world, player);
                explodingCoconutEntity.setItem(itemstack);
                explodingCoconutEntity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                world.addEntity(explodingCoconutEntity);
            } else {
                player.sendMessage(new TranslationTextComponent("tropicraft.coconutBombWarning"));
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
