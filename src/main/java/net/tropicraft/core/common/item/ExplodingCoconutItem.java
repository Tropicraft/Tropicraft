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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // TODO config option
        final boolean canPlayerThrow = player.isCreative() || player.canUseGameMasterBlocks();
        //allow to use anywhere but in the main area of the server
        final boolean ltOverride = world.dimension() != TropicraftDimension.WORLD;
        ItemStack itemstack = player.getItemInHand(hand);
        if (!canPlayerThrow && !ltOverride) {
            if (!world.isClientSide) {
                player.displayClientMessage(new TranslationTextComponent("tropicraft.coconutBombWarning"), false);
            }
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        }
        
        if (!player.isCreative()) {
            itemstack.shrink(1);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            ExplodingCoconutEntity snowballentity = new ExplodingCoconutEntity(world, player);
            snowballentity.setItem(itemstack);
            snowballentity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(snowballentity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
