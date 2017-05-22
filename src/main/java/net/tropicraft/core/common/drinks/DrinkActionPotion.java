package net.tropicraft.core.common.drinks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class DrinkActionPotion extends DrinkAction {
    public Potion potion;
    public int duration;
    public int amplifier;

    public DrinkActionPotion(Potion potion, int duration, int amplifier) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public void onDrink(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(this.potion, this.duration * 20, this.amplifier));
    }
}
