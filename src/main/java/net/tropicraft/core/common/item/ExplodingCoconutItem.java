package net.tropicraft.core.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;

public class ExplodingCoconutItem extends Item {

    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        boolean canPlayerThrow = player.isCreative() || player.canUseGameMasterBlocks() || TropicsConfigs.COMMON.allowExplodingCoconutsByNonOPs.get();
        ItemStack item = player.getItemInHand(hand);
        if (!canPlayerThrow) {
            if (!world.isClientSide) {
                player.displayClientMessage(TropicraftLangKeys.EXPLODING_COCONUT_WARNING.component(), false);
            }
            return new InteractionResultHolder<>(InteractionResult.PASS, item);
        }

        if (!player.isCreative()) {
            item.shrink(1);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (player.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClientSide) {
            float explosionRadius = item.getOrDefault(TropicraftDataComponents.EXPLOSION_RADIUS, ExplodingCoconutEntity.DEFAULT_EXPLOSION_RADIUS);
            ExplodingCoconutEntity coconut = new ExplodingCoconutEntity(world, player, explosionRadius);
            coconut.setItem(item);
            coconut.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
            world.addFreshEntity(coconut);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
    }
}
