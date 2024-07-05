package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.ArrayList;
import java.util.List;

// TODO: Make a registry
public class Drink {
    public static final Int2ObjectMap<Drink> DRINKS = new Int2ObjectOpenHashMap<>();
    public static final Drink LEMONADE = new Drink(1, 0xfadb41, "lemonade", ChatFormatting.YELLOW).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 1));
    public static final Drink LIMEADE = new Drink(2, 0x84e88a, "limeade", ChatFormatting.GREEN).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 1));
    public static final Drink ORANGEADE = new Drink(3, 0xf3be36, "orangeade", ChatFormatting.GOLD).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 1));
    public static final Drink CAIPIRINHA = new Drink(4, 0x94ff36, "caipirinha", ChatFormatting.GREEN).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 1)).setHasUmbrella(true);
    public static final Drink BLACK_COFFEE = new Drink(5, 0x68442c, "black_coffee", ChatFormatting.WHITE).addAction(new DrinkActionPotion(MobEffects.REGENERATION, 5, 1)).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 2));
    public static final Drink PINA_COLADA = new Drink(6, 0xefefef, "pina_colada", ChatFormatting.GOLD).addAction(new DrinkActionPotion(MobEffects.CONFUSION, 10, 0)).addAction(new DrinkAction() {

        @Override
        public void onDrink(Player player) {
            if (!player.level().isClientSide && isSunset(player.level()) && player.getVehicle() instanceof ChairEntity) {
                TropicraftDimension.teleportPlayerWithPortal((ServerPlayer) player, TropicraftDimension.WORLD);
            }
        }

        private boolean isSunset(Level world) {
            long timeDay = world.getDayTime() % 24000;
            return timeDay > 12200 && timeDay < 14000;
        }
    }).setAlwaysEdible(true);
    public static final Drink COCONUT_WATER = new Drink(7, 0xdfdfdf, "coconut_water", ChatFormatting.WHITE).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 1));
    public static final Drink MAI_TAI = new Drink(8, 0xff772e, "mai_tai", ChatFormatting.GOLD).addAction(new DrinkActionPotion(MobEffects.CONFUSION, 5, 0));
    public static final Drink COCKTAIL = new Drink(9, 0, "cocktail", ChatFormatting.WHITE);

    public static final Codec<Drink> CODEC = ExtraCodecs.idResolverCodec(value -> value.drinkId, DRINKS::get, -1);
    public static final StreamCodec<ByteBuf, Drink> STREAM_CODEC = ByteBufCodecs.idMapper(DRINKS::get, value -> value.drinkId);

    public int drinkId;
    public int color;
    public String name;
    public boolean alwaysEdible;
    public boolean hasUmbrella;
    public ChatFormatting textFormatting;
    public List<DrinkAction> actions = new ArrayList<>();

    public Drink(int id, int color, String name, ChatFormatting textFormatting) {
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

    public void onDrink(Player player) {
        for (DrinkAction action : actions) {
            action.onDrink(player);
        }
    }

    public static boolean isDrink(final Item item) {
        return TropicraftItems.COCKTAILS.values().stream().anyMatch(ri -> ri.get() == item);
    }

    public String getName() {
        return this == PINA_COLADA ? "Pi\u00F1a Colada" : RegistrateLangProvider.toEnglishName(name);
    }
}
