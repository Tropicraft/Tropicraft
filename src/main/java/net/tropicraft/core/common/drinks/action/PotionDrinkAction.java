package net.tropicraft.core.common.drinks.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record PotionDrinkAction(
        Holder<MobEffect> effect,
        int duration,
        int amplifier
) implements DrinkAction {
    public static final MapCodec<PotionDrinkAction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            MobEffect.CODEC.fieldOf("effect").forGetter(PotionDrinkAction::effect),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("duration").forGetter(PotionDrinkAction::duration),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amplifier").forGetter(PotionDrinkAction::amplifier)
    ).apply(i, PotionDrinkAction::new));

    @Override
    public void onDrink(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(effect, duration * SharedConstants.TICKS_PER_SECOND, amplifier));
    }

    @Override
    public MapCodec<PotionDrinkAction> codec() {
        return CODEC;
    }
}
