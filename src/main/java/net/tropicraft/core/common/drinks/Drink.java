package net.tropicraft.core.common.drinks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.text.TextFormatting;

public class Drink {
	public static final Drink[] drinkList = new Drink[128];
	public static final Drink lemonade = new Drink(1, 0xfadb41, "lemonade", TextFormatting.YELLOW).addAction(new DrinkActionPotion(MobEffects.SPEED, 5, 1));
	public static final Drink limeade = new Drink(2, 0x84e88a, "limeade", TextFormatting.GREEN).addAction(new DrinkActionPotion(MobEffects.SPEED, 5, 1));
	public static final Drink orangeade = new Drink(3, 0xf3be36, "orangeade", TextFormatting.GOLD).addAction(new DrinkActionPotion(MobEffects.SPEED, 5, 1));
	public static final Drink caipirinha = new Drink(4, 0x94ff36, "caipirinha", TextFormatting.GREEN).addAction(new DrinkActionPotion(MobEffects.SPEED, 5, 1)).setHasUmbrella(true);

	public static final Drink pinaColada = new Drink(6, 0xefefef, "pina_colada", TextFormatting.GOLD).addAction(new DrinkActionPotion(MobEffects.NAUSEA, 10, 0)).setAlwaysEdible(true);
	public static final Drink coconutWater = new Drink(7, 0xdfdfdf, "coconut_water", TextFormatting.WHITE).addAction(new DrinkActionPotion(MobEffects.SPEED, 5, 1));
	public static final Drink maiTai = new Drink(8, 0xff772e, "mai_tai", TextFormatting.GOLD).addAction(new DrinkActionPotion(MobEffects.NAUSEA, 5, 0));
	public static final Drink blackCoffee = new Drink(5, 0x68442c, "black_coffee", TextFormatting.BLACK).addAction(new DrinkActionPotion(MobEffects.REGENERATION, 5, 1)).addAction(new DrinkActionPotion(MobEffects.SPEED, 5, 2));

	public int drinkId;
	public int color;
	public String name;
	public boolean alwaysEdible;
	public boolean hasUmbrella;
	public TextFormatting textFormatting;
	public List<DrinkAction> actions = new ArrayList<DrinkAction>();

	public Drink(int id, int color, String name, TextFormatting textFormatting) {
		drinkList[id] = this;
		this.drinkId = id;
		this.color = color;
		this.name = name;
		this.textFormatting = textFormatting;
		this.alwaysEdible = true; // Set all of them always edible for now
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
