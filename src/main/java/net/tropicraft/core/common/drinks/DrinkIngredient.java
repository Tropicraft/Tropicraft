package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nullable;
import java.util.List;

public record DrinkIngredient (
    Holder<Item> item,
    int color,
    float alpha
) implements Comparable<DrinkIngredient> {

    public static final Codec<DrinkIngredient> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(DrinkIngredient::item),
            Codec.INT.fieldOf("color").forGetter(DrinkIngredient::color),
            Codec.FLOAT.fieldOf("alpha").forGetter(DrinkIngredient::alpha)
    ).apply(i, DrinkIngredient::new));

    public static final Codec<DrinkIngredient> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(DrinkIngredient::item),
            Codec.INT.fieldOf("color").forGetter(DrinkIngredient::color),
            Codec.FLOAT.fieldOf("alpha").forGetter(DrinkIngredient::alpha)
    ).apply(i, DrinkIngredient::new));

    public static final Codec<Holder<DrinkIngredient>> CODEC = RegistryFixedCodec.create(TropicraftRegistries.DRINK_INGREDIENT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<DrinkIngredient>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TropicraftRegistries.DRINK_INGREDIENT);

    @Nullable
    public static Holder<DrinkIngredient> findMatchingIngredient(HolderLookup.Provider registries, ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        List<Holder.Reference<DrinkIngredient>> holders = registries.lookupOrThrow(TropicraftRegistries.DRINK_INGREDIENT).listElements().toList();

        for (Holder.Reference<DrinkIngredient> ingredientHolder : holders) {
            DrinkIngredient ingredient = ingredientHolder.value();
            if (ingredient.matches(stack)) {
                return ingredientHolder;
            }
        }

        return null;
    }

    public boolean matches(ItemStack stack) {
        return stack.is(item.value().asItem());
    }

    public static List<Holder<DrinkIngredient>> listIngredients(HolderLookup.Provider registries, ItemStack stack) {
        List<Holder<DrinkIngredient>> ingredients = CocktailItem.getCocktail(stack).ingredients();
        if (!ingredients.isEmpty()) {
            return ingredients;
        }

        Holder<DrinkIngredient> matchingIngredient = findMatchingIngredient(registries, stack);
        return matchingIngredient != null ? List.of(matchingIngredient) : List.of();
    }

    public Component getDisplayName() {
        return ComponentUtils.mergeStyles(new ItemStack(item.value()).getHoverName().copy(), Style.EMPTY.withColor(color));
    }

    @Override
    public int compareTo(DrinkIngredient other) {
        return Integer.compare(item.hashCode(), other.item.hashCode());
    }
}


