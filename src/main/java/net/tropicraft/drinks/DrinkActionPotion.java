package net.tropicraft.drinks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class DrinkActionPotion extends DrinkAction {
    public int potionId;
    public int duration;
    public int amplifier;

    public DrinkActionPotion(int potionId, int duration, int amplifier) {
        this.potionId = potionId;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public void onDrink(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(this.potionId, this.duration * 20, this.amplifier));
    }
}
