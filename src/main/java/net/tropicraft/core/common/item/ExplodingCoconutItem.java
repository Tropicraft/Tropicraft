package net.tropicraft.core.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.tropicraft.core.common.config.TropicraftConfig;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;

import net.minecraft.item.Item.Properties;

public class ExplodingCoconutItem extends Item {

    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        final boolean hasPermission = player.canUseCommandBlock() || TropicraftConfig.coconutBombWhitelist.get().contains(player.getUniqueID().toString());
        ItemStack itemstack = player.getHeldItem(hand);
        if (!hasPermission) {
            if (!world.isRemote) {
                player.sendStatusMessage(new TranslationTextComponent("tropicraft.coconutBombWarning"), false);
            }
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        }
        
        if (!player.isCreative()) {
            itemstack.shrink(1);
        }
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            ExplodingCoconutEntity coconutBomb = new ExplodingCoconutEntity(world, player);
            coconutBomb.setItem(itemstack);
            coconutBomb.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(coconutBomb);
        }

        player.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
