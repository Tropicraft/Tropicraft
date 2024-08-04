package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.drinks.action.DrinkAction;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Drink(
        Component name,
        int color,
        List<DrinkAction> actions,
        List<Holder<DrinkIngredient>> ingredients
) {
    public static final Codec<Drink> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Drink::name),
            Codec.INT.fieldOf("color").forGetter(Drink::color),
            DrinkAction.CODEC.listOf().optionalFieldOf("actions", List.of()).forGetter(Drink::actions),
            DrinkIngredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(Drink::ingredients)
    ).apply(i, Drink::new));

    public static final Codec<Drink> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Drink::name),
            Codec.INT.fieldOf("color").forGetter(Drink::color),
            // The client does not need to know about drink actions
            MapCodec.unit(List.<DrinkAction>of()).forGetter(Drink::actions),
            DrinkIngredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(Drink::ingredients)
    ).apply(i, Drink::new));

    public static final Codec<Holder<Drink>> CODEC = RegistryFixedCodec.create(TropicraftRegistries.DRINK);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Drink>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TropicraftRegistries.DRINK);

    private static List<Holder.Reference<Drink>> getDrinks(Level level) {
        return level.registryAccess().lookupOrThrow(TropicraftRegistries.DRINK).listElements().toList();
    }

    @Nullable
    public static ResourceKey<Drink> getDrink(final Level level, NonNullList<ItemStack> ingredientStacks) {
        List<Holder.Reference<Drink>> registeredDrinks = getDrinks(level);

        for (Holder.Reference<Drink> drinkReference : registeredDrinks) {
            if (drinkReference.value().matches(ingredientStacks)) {
                return drinkReference.getKey();
            }
        }

        return null;
    }

    public void onDrink(ServerPlayer player) {
        for (DrinkAction action : actions) {
            action.onDrink(player);
        }
    }

    public boolean matches(final List<ItemStack> itemStacks) {
        List<ItemStack> stacksCopy = new ArrayList<>(itemStacks);
        int count = 0;
        for (Holder<DrinkIngredient> ingredientHolder : ingredients) {
            final DrinkIngredient ingredient = ingredientHolder.value();

            for (final ItemStack stack : itemStacks) {
                if (ingredient.matches(stack)) {
                    stacksCopy.remove(stack);
                    count++;
                }
            }
        }

        return count == itemStacks.size();
    }

    public static ItemStack getResult(final Level level, NonNullList<ItemStack> ingredients) {
        List<Holder.Reference<Drink>> registeredDrinks = getDrinks(level);

        Optional<Holder.Reference<Drink>> optionalDrinkHolder = registeredDrinks.stream().filter(drink -> drink != null && drink.value().matches(ingredients)).findFirst();
        if (optionalDrinkHolder.isPresent()) {
            return CocktailItem.makeDrink(optionalDrinkHolder.get());
        }
        return CocktailItem.makeCocktail(level.registryAccess(), ingredients);
    }
}
