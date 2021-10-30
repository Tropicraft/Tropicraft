package net.tropicraft.core.common.drinks;

import net.minecraft.world.entity.player.Player;

public class DrinkActionFood extends DrinkAction {
    private final int healAmount;
    private final float saturationModifier;

    public DrinkActionFood(final int healAmount, final float saturationModifier) {
        this.healAmount = healAmount;
        this.saturationModifier = saturationModifier;
    }

    @Override
    public void onDrink(Player player) {
        player.getFoodData().eat(healAmount, saturationModifier);
    }
}
