package net.tropicraft.core.common.drinks;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class DrinkActionPotion extends DrinkAction {
    private final MobEffect potion;
    private final int duration;
    private final int amplifier;

    public DrinkActionPotion(MobEffect potion, int duration, int amplifier) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public void onDrink(Player player) {
        player.addEffect(new MobEffectInstance(potion, duration * 20, amplifier));
    }
}
