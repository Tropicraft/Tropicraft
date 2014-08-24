package net.tropicraft.drinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.StringTranslate;

public class Drink {
    public static final Drink[] drinkList = new Drink[128];
    public static final Drink lemonade = new Drink(1, 0xfadb41, "Lemonade").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
    public static final Drink limeade = new Drink(2, 0x84e88a, "Limeade").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
    public static final Drink orangeade = new Drink(3, 0xf3be36, "Orangeade").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
    public static final Drink caipirinha = new Drink(4, 0x94ff36, "Caipirinha").addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1)).setHasUmbrella(true);
    public static final Drink blackCoffee = new Drink(5, 0x68442c, "Black Coffee");
    public static final Drink pinaColada = new Drink(6, 0xeeff00, "Pi\u00f1a Colada").addAction(new DrinkActionPotion(Potion.confusion.id, 10, 0)).setAlwaysEdible(true);
    
    public int drinkId;
    public int color;
    public String displayName;
    public boolean alwaysEdible;
    public boolean hasUmbrella;
    public List<DrinkAction> actions = new ArrayList<DrinkAction>();

    public Drink(int id, int color, String displayName) {
        drinkList[id] = this;
        this.drinkId = id;
        this.color = color;
        this.displayName = displayName;
    }
    
    public Drink setHasUmbrella(boolean has) {
        this.hasUmbrella = has;
        return this;
    }
    
    public Drink setAlwaysEdible(boolean edible) {
        this.alwaysEdible = edible;
        return this;
    }
    
    public Drink addAction(DrinkAction action) {
        this.actions.add(action);
        return this;
    }
    
    public void onDrink(EntityPlayer player) {
        for (DrinkAction action: actions) {
            action.onDrink(player);
        }
    }
}
