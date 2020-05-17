package net.tropicraft.core.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;

public class ExplodingCoconutItem extends Item {

    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // TODO config option
        final boolean canPlayerThrow = player.isCreative() || player.canUseCommandBlock();
        ItemStack itemstack = player.getHeldItem(hand);
        if (!canPlayerThrow) {
            if (!world.isRemote) {
                player.sendMessage(new TranslationTextComponent("tropicraft.coconutBombWarning"));
            }
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        }
        
        if (!player.isCreative()) {
            itemstack.shrink(1);
        }

        world.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            ExplodingCoconutEntity snowballentity = new ExplodingCoconutEntity(world, player);
            snowballentity.setItem(itemstack);
            snowballentity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(snowballentity);
        }

        player.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
