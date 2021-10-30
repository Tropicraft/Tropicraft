package net.tropicraft.core.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;

import net.minecraft.world.item.Item.Properties;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

public class ExplodingCoconutItem extends Item {

    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // TODO config option
        final boolean canPlayerThrow = player.isCreative() || player.canUseGameMasterBlocks();
        //allow to use anywhere but in the main area of the server
        final boolean ltOverride = world.dimension() != TropicraftDimension.WORLD;
        ItemStack itemstack = player.getItemInHand(hand);
        if (!canPlayerThrow && !ltOverride) {
            if (!world.isClientSide) {
                player.displayClientMessage(new TranslatableComponent("tropicraft.coconutBombWarning"), false);
            }
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        }
        
        if (!player.isCreative()) {
            itemstack.shrink(1);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            ExplodingCoconutEntity snowballentity = new ExplodingCoconutEntity(world, player);
            snowballentity.setItem(itemstack);
            snowballentity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(snowballentity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }
}
