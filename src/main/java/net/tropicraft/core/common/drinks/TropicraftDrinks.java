package net.tropicraft.core.common.drinks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.drinks.action.DrinkAction;
import net.tropicraft.core.common.drinks.action.PortalDrinkAction;
import net.tropicraft.core.common.drinks.action.PotionDrinkAction;
import net.tropicraft.core.common.entity.TropicraftEntities;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

public interface TropicraftDrinks {
    ResourceKey<Drink> LEMONADE = createKey("lemonade");
    ResourceKey<Drink> LIMEADE = createKey("limeade");
    ResourceKey<Drink> ORANGEADE = createKey("orangeade");
    ResourceKey<Drink> CAIPIRINHA = createKey("caipirinha");
    ResourceKey<Drink> BLACK_COFFEE = createKey("black_coffee");
    ResourceKey<Drink> PINA_COLADA = createKey("pina_colada");
    ResourceKey<Drink> PINA_COLADA_1 = createKey("pina_colada_1");
    ResourceKey<Drink> PINA_COLADA_2 = createKey("pina_colada_2");
    ResourceKey<Drink> PINA_COLADA_3 = createKey("pina_colada_3");
    ResourceKey<Drink> COCONUT_WATER = createKey("coconut_water");
    ResourceKey<Drink> MAI_TAI = createKey("mai_tai");

    static void bootstrap(BootstrapContext<Drink> context) {
        HolderGetter<DrinkIngredient> lookup = context.lookup(TropicraftRegistries.DRINK_INGREDIENT);

        register(context, lookup,
                LEMONADE,
                TropicraftLangKeys.LEMONADE.component(ChatFormatting.YELLOW),
                0xfadb41,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1)),
                List.of(TropicraftDrinkIngredients.LEMON, TropicraftDrinkIngredients.SUGAR, TropicraftDrinkIngredients.WATER_BUCKET)
        );
        register(context, lookup,
                LIMEADE,
                TropicraftLangKeys.LIMEADE.component(ChatFormatting.GREEN),
                0x84e88a,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1)),
                List.of(TropicraftDrinkIngredients.LIME, TropicraftDrinkIngredients.SUGAR, TropicraftDrinkIngredients.WATER_BUCKET)
        );
        register(context, lookup,
                ORANGEADE,
                TropicraftLangKeys.ORANGEADE.component(ChatFormatting.GOLD),
                0xf3be36,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1)),
                List.of(TropicraftDrinkIngredients.ORANGE, TropicraftDrinkIngredients.SUGAR, TropicraftDrinkIngredients.WATER_BUCKET)
        );
        register(context, lookup,
                CAIPIRINHA,
                TropicraftLangKeys.CAIPIRINHA.component(ChatFormatting.GREEN),
                0x94ff36,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1)),
                List.of(TropicraftDrinkIngredients.LIME, TropicraftDrinkIngredients.SUGAR_CANE, TropicraftDrinkIngredients.WATER_BUCKET)
        );
        register(context, lookup,
                BLACK_COFFEE,
                TropicraftLangKeys.BLACK_COFFEE.component(ChatFormatting.WHITE),
                0x68442c,
                List.of(
                        new PotionDrinkAction(MobEffects.REGENERATION, 5, 1),
                        new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 2)
                ),
                List.of(TropicraftDrinkIngredients.ROASTED_COFFEE_BEAN, TropicraftDrinkIngredients.WATER_BUCKET)
        );
        registerPinaColada(context, lookup, PINA_COLADA, List.of(TropicraftDrinkIngredients.PINEAPPLE_CUBES, TropicraftDrinkIngredients.COCONUT_CHUNK));
        registerPinaColada(context, lookup, PINA_COLADA_1, List.of(TropicraftDrinkIngredients.PINEAPPLE_CUBES, TropicraftDrinkIngredients.COCONUT));
        registerPinaColada(context, lookup, PINA_COLADA_2, List.of(TropicraftDrinkIngredients.PINEAPPLE, TropicraftDrinkIngredients.COCONUT_CHUNK));
        registerPinaColada(context, lookup, PINA_COLADA_3, List.of(TropicraftDrinkIngredients.PINEAPPLE, TropicraftDrinkIngredients.COCONUT));

        register(context, lookup,
                COCONUT_WATER,
                TropicraftLangKeys.COCONUT_WATER.component(ChatFormatting.WHITE),
                0xdfdfdf,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1)),
                List.of(TropicraftDrinkIngredients.COCONUT, TropicraftDrinkIngredients.WATER_BUCKET)
        );
        register(context, lookup,
                MAI_TAI,
                TropicraftLangKeys.MAI_TAI.component(ChatFormatting.GOLD),
                0xff772e,
                List.of(new PotionDrinkAction(MobEffects.CONFUSION, 5, 0)),
                List.of(TropicraftDrinkIngredients.ORANGE, TropicraftDrinkIngredients.LIME, TropicraftDrinkIngredients.WATER_BUCKET)
        );
    }

    static void registerPinaColada(BootstrapContext<Drink> context, HolderGetter<DrinkIngredient> lookup, ResourceKey<Drink> drinkResourceKey, List<ResourceKey<DrinkIngredient>> ingredients) {
        register(context, lookup,
                drinkResourceKey,
                TropicraftLangKeys.PINA_COLADA.component(ChatFormatting.GOLD),
                0xefefef,
                List.of(
                        new PotionDrinkAction(MobEffects.CONFUSION, 10, 0),
                        new PortalDrinkAction(
                                TropicraftDimension.WORLD,
                                Optional.of(HolderSet.direct(TropicraftEntities.CHAIR)),
                                12200,
                                14000
                        )
                ),
                ingredients
        );
    }

    static void register(BootstrapContext<Drink> context, HolderGetter<DrinkIngredient> lookup, ResourceKey<Drink> drinkResourceKey, Component name,
                         int color, List<DrinkAction> actions, List<ResourceKey<DrinkIngredient>> ingredients) {
        List<Holder<DrinkIngredient>> ingredientHolders = Lists.newArrayList();
        ingredients.forEach(drinkIngredientResourceKey -> ingredientHolders.add(lookup.getOrThrow(drinkIngredientResourceKey)));

        context.register(drinkResourceKey, new Drink(name, color, actions, ingredientHolders));
    }

    static ResourceKey<Drink> createKey(String name) {
        return Tropicraft.resourceKey(TropicraftRegistries.DRINK, name);
    }
}
