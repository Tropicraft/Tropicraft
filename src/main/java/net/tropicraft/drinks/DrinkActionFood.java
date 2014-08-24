package net.tropicraft.drinks;

import net.minecraft.entity.player.EntityPlayer;

public class DrinkActionFood extends DrinkAction {
    public int healAmount;
    public float saturationModifier;

    public DrinkActionFood(int healAmount, float saturationModifier) {
        this.healAmount = healAmount;
        this.saturationModifier = saturationModifier;
    }

    @Override
    public void onDrink(EntityPlayer player) {
        player.getFoodStats().addStats(this.healAmount, this.saturationModifier);
    }
}
