package net.tropicraft.core.common.drinks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class DrinkActionPotion extends DrinkAction {
    private final Effect potion;
    private final int duration;
    private final int amplifier;

    public DrinkActionPotion(Effect potion, int duration, int amplifier) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public void onDrink(PlayerEntity player) {
        player.addPotionEffect(new EffectInstance(potion, duration * 20, amplifier));
    }
}
