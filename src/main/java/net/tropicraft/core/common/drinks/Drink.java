package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.drinks.action.DrinkAction;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Drink(
        Component name,
        int color,
        List<DrinkAction> actions,
        List<Holder<DrinkIngredient>> ingredients
) {
    public static final int MAX_INGREDIENTS = 3;

    public static final Codec<Drink> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Drink::name),
            Codec.INT.fieldOf("color").forGetter(Drink::color),
            DrinkAction.CODEC.listOf().optionalFieldOf("actions", List.of()).forGetter(Drink::actions),
            DrinkIngredient.CODEC.listOf(1, MAX_INGREDIENTS).fieldOf("ingredients").forGetter(Drink::ingredients)
    ).apply(i, Drink::new));

    public static final Codec<Drink> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Drink::name),
            Codec.INT.fieldOf("color").forGetter(Drink::color),
            // The client does not need to know about drink actions
            MapCodec.unit(List.<DrinkAction>of()).forGetter(Drink::actions),
            DrinkIngredient.CODEC.listOf(1, MAX_INGREDIENTS).fieldOf("ingredients").forGetter(Drink::ingredients)
    ).apply(i, Drink::new));

    public static final Codec<Holder<Drink>> CODEC = RegistryFixedCodec.create(TropicraftRegistries.DRINK);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Drink>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TropicraftRegistries.DRINK);

    @Nullable
    public static Holder<Drink> getMatchingDrink(HolderLookup.Provider registries, List<Holder<DrinkIngredient>> ingredients) {
        return registries.lookupOrThrow(TropicraftRegistries.DRINK).listElements()
                .filter(drink -> drink.value().matches(ingredients))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static Holder<Drink> getMatchingDrinkByItems(HolderLookup.Provider registries, List<ItemStack> itemStacks) {
        List<Holder<DrinkIngredient>> ingredients = getIngredientsFromItems(registries, itemStacks);
        if (ingredients.isEmpty()) {
            return null;
        }
        return getMatchingDrink(registries, ingredients);
    }

    public void onDrink(ServerPlayer player) {
        for (DrinkAction action : actions) {
            action.onDrink(player);
        }
    }

    public boolean matches(List<Holder<DrinkIngredient>> matchingAgainst) {
        List<Holder<DrinkIngredient>> matchingAgainstCopy = new ArrayList<>(matchingAgainst);
        for (Holder<DrinkIngredient> ingredientHolder : ingredients) {
            if (matchingAgainst.contains(ingredientHolder)) {
                matchingAgainstCopy.remove(ingredientHolder);
            }
        }

        return matchingAgainstCopy.isEmpty();
    }

    public static ItemStack getResult(HolderLookup.Provider registries, List<ItemStack> ingredientItems) {
        List<Holder<DrinkIngredient>> drinkIngredients = getIngredientsFromItems(registries, ingredientItems);
        if (drinkIngredients.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Holder<Drink> matchingDrink = getMatchingDrink(registries, drinkIngredients);
        if (matchingDrink != null) {
            return CocktailItem.makeDrink(matchingDrink);
        }
        return CocktailItem.makeCocktail(Cocktail.ofIngredients(drinkIngredients));
    }

    private static List<Holder<DrinkIngredient>> getIngredientsFromItems(HolderLookup.Provider registries, List<ItemStack> ingredientItems) {
        return ingredientItems.stream()
                .map(itemStack -> DrinkIngredient.findMatchingIngredient(registries, itemStack))
                .filter(Objects::nonNull)
                .toList();
    }
}
