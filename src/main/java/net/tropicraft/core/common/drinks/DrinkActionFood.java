package net.tropicraft.core.common.drinks;

import net.minecraft.world.entity.player.Player;

public record DrinkActionFood(
        int healAmount,
        float saturationModifier
) implements DrinkAction {
    @Override
    public void onDrink(Player player) {
        player.getFoodData().eat(healAmount, saturationModifier);
    }
}
