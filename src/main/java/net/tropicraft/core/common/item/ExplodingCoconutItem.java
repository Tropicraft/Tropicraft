package net.tropicraft.core.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;

import net.minecraft.item.Item.Properties;

public class ExplodingCoconutItem extends Item {

    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // TODO config option
        final boolean canPlayerThrow = player.isCreative() || player.canUseCommandBlock();
        //allow to use anywhere but in the main area of the server
        final boolean ltOverride = world.getDimensionKey() != TropicraftDimension.WORLD;
        ItemStack itemstack = player.getHeldItem(hand);
        if (!canPlayerThrow && !ltOverride) {
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
            ExplodingCoconutEntity snowballentity = new ExplodingCoconutEntity(world, player);
            snowballentity.setItem(itemstack);
            snowballentity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(snowballentity);
        }

        player.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
