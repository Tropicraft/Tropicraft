package net.tropicraft.core.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;

public class ExplodingCoconutItem extends Item {

    public ExplodingCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        float explosionRadius = item.getOrDefault(TropicraftDataComponents.EXPLOSION_RADIUS, ExplodingCoconutEntity.DEFAULT_EXPLOSION_RADIUS);
        boolean destroysBlocks = item.getOrDefault(TropicraftDataComponents.DESTROYS_BLOCKS, ExplodingCoconutEntity.DEFAULT_DESTROYS_BLOCKS);

        boolean requiresPermission = explosionRadius > 0.0f && destroysBlocks;

        boolean canPlayerThrow = player.isCreative() || player.canUseGameMasterBlocks() || TropicsConfigs.COMMON.allowExplodingCoconutsByNonOPs.get();
        if (requiresPermission && !canPlayerThrow) {
            player.sendSystemMessage(TropicraftLangKeys.EXPLODING_COCONUT_WARNING.component());
            return InteractionResult.FAIL;
        }

        ItemStack thrownItem = item.consumeAndReturn(1, player);
        ExplodingCoconutEntity coconut = new ExplodingCoconutEntity(level, player, explosionRadius, destroysBlocks, thrownItem);
        coconut.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
        level.addFreshEntity(coconut);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (player.getRandom().nextFloat() * 0.4f + 0.8f));

        player.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResult.SUCCESS.heldItemTransformedTo(item);
    }
}
