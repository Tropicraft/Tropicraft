package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Optional;

public record Cocktail(
        Optional<Holder<Drink>> drink,
        List<Ingredient> ingredients,
        int color
) {
    public static final int DEFAULT_COLOR = 0xf3be36;

    public static final Cocktail EMPTY = new Cocktail(Optional.empty(), List.of(), DEFAULT_COLOR);

    private static final Codec<Cocktail> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(
            Drink.CODEC.optionalFieldOf("drink").forGetter(Cocktail::drink),
            Ingredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(Cocktail::ingredients)
    ).apply(i, Cocktail::new));

    public static final Codec<Cocktail> CODEC = Codec.withAlternative(FULL_CODEC, Drink.CODEC, Cocktail::ofDrink);

    public static final StreamCodec<RegistryFriendlyByteBuf, Cocktail> STREAM_CODEC = StreamCodec.composite(
            Drink.STREAM_CODEC.apply(ByteBufCodecs::optional), Cocktail::drink,
            Ingredient.STREAM_CODEC.apply(ByteBufCodecs.list()), Cocktail::ingredients,
            Cocktail::new
    );

    public Cocktail(Optional<Holder<Drink>> drink, List<Ingredient> ingredients) {
        this(drink, ingredients, computeColor(drink, ingredients));
    }

    public static Cocktail ofDrink(Holder<Drink> drink) {
        return new Cocktail(Optional.of(drink), List.of());
    }

    public static Cocktail ofIngredients(List<Ingredient> ingredients) {
        return new Cocktail(Optional.empty(), ingredients);
    }

    private static int computeColor(Optional<Holder<Drink>> drink, List<Ingredient> ingredients) {
        if (drink.isPresent()) {
            return drink.get().value().color();
        }

        int red = 0;
        int green = 0;
        int blue = 0;
        float weight = 0.0f;

        for (Ingredient ingredient : ingredients) {
            int color = ingredient.getColor();
            red += FastColor.ARGB32.red(color);
            green += FastColor.ARGB32.green(color);
            blue += FastColor.ARGB32.blue(color);
            weight += ingredient.getWeight();
        }

        return FastColor.ARGB32.color(255, Mth.floor(red / weight), Mth.floor(green / weight), Mth.floor(blue / weight));
    }

    public void onDrink(ServerPlayer player) {
        drink.ifPresent(holder -> holder.value().onDrink(player));
    }
}
