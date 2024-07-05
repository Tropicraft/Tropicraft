package net.tropicraft.core.common.drinks;

import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public record DrinkActionPotion(
        Holder<MobEffect> potion,
        int duration,
        int amplifier
) implements DrinkAction {
    @Override
    public void onDrink(Player player) {
        player.addEffect(new MobEffectInstance(potion, duration * SharedConstants.TICKS_PER_SECOND, amplifier));
    }
}
