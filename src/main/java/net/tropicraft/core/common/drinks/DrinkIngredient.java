package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.TropicraftRegistries;

import javax.annotation.Nullable;
import java.util.List;

public record DrinkIngredient(
        Component description,
        HolderSet<Item> items,
        int color,
        float weight
) implements Comparable<DrinkIngredient> {
    public static final Codec<DrinkIngredient> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("description").forGetter(DrinkIngredient::description),
            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(DrinkIngredient::items),
            Codec.INT.fieldOf("color").forGetter(DrinkIngredient::color),
            Codec.FLOAT.optionalFieldOf("weight", 1.0f).forGetter(DrinkIngredient::weight)
    ).apply(i, DrinkIngredient::new));

    public static final Codec<DrinkIngredient> NETWORK_CODEC = DIRECT_CODEC;

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
        return stack.is(items);
    }

    public Component getDisplayName() {
        return ComponentUtils.mergeStyles(description.copy(), Style.EMPTY.withColor(color));
    }

    @Override
    public int compareTo(DrinkIngredient other) {
        return Integer.compare(items.hashCode(), other.items.hashCode());
    }
}


