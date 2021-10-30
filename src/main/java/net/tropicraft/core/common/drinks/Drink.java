package net.tropicraft.core.common.drinks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.ArrayList;
import java.util.List;

public class Drink {
    public static final Int2ObjectMap<Drink> DRINKS = new Int2ObjectOpenHashMap<>();
    public static final Drink LEMONADE = new Drink(1, 0xfadb41, "lemonade", TextFormatting.YELLOW).addAction(new DrinkActionPotion(Effects.MOVEMENT_SPEED, 5, 1));
    public static final Drink LIMEADE = new Drink(2, 0x84e88a, "limeade", TextFormatting.GREEN).addAction(new DrinkActionPotion(Effects.MOVEMENT_SPEED, 5, 1));
    public static final Drink ORANGEADE = new Drink(3, 0xf3be36, "orangeade", TextFormatting.GOLD).addAction(new DrinkActionPotion(Effects.MOVEMENT_SPEED, 5, 1));
    public static final Drink CAIPIRINHA = new Drink(4, 0x94ff36, "caipirinha", TextFormatting.GREEN).addAction(new DrinkActionPotion(Effects.MOVEMENT_SPEED, 5, 1)).setHasUmbrella(true);
    public static final Drink BLACK_COFFEE = new Drink(5, 0x68442c, "black_coffee", TextFormatting.WHITE).addAction(new DrinkActionPotion(Effects.REGENERATION, 5, 1)).addAction(new DrinkActionPotion(Effects.MOVEMENT_SPEED, 5, 2));
    public static final Drink PINA_COLADA = new Drink(6, 0xefefef, "pina_colada", TextFormatting.GOLD).addAction(new DrinkActionPotion(Effects.CONFUSION, 10, 0)).addAction(new DrinkAction() {
        
        @Override
        public void onDrink(PlayerEntity player) {
            if (!player.level.isClientSide && isSunset(player.level) && player.getVehicle() instanceof ChairEntity) {
                TropicraftDimension.teleportPlayer((ServerPlayerEntity) player, TropicraftDimension.WORLD);
            }
        }

        private boolean isSunset(World world) {
            long timeDay = world.getDayTime() % 24000;
            return timeDay > 12200 && timeDay < 14000;
        }
    }).setAlwaysEdible(true);
    public static final Drink COCONUT_WATER = new Drink(7, 0xdfdfdf, "coconut_water", TextFormatting.WHITE).addAction(new DrinkActionPotion(Effects.MOVEMENT_SPEED, 5, 1));
    public static final Drink MAI_TAI = new Drink(8, 0xff772e, "mai_tai", TextFormatting.GOLD).addAction(new DrinkActionPotion(Effects.CONFUSION, 5, 0));
    public static final Drink COCKTAIL = new Drink(9, 0, "cocktail", TextFormatting.WHITE);

    public int drinkId;
    public int color;
    public String name;
    public boolean alwaysEdible;
    public boolean hasUmbrella;
    public TextFormatting textFormatting;
    public List<DrinkAction> actions = new ArrayList<>();

    public Drink(int id, int color, String name, TextFormatting textFormatting) {
        DRINKS.put(id, this);
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

    public void onDrink(PlayerEntity player) {
        for (DrinkAction action: actions) {
            action.onDrink(player);
        }
    }

    public static boolean isDrink(final Item item) {
        return TropicraftItems.COCKTAILS.values().stream().anyMatch(ri -> ri.get() == item);
    }
}
