package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;

import java.util.List;
import java.util.Optional;

public record Cocktail(
        Optional<Holder<Drink>> drink,
        List<Holder<DrinkIngredient>> ingredients,
        int color
) {
    public static final int DEFAULT_COLOR = 0xf3be36;

    public static final Cocktail EMPTY = new Cocktail(Optional.empty(), List.of(), DEFAULT_COLOR);

    private static final Codec<Cocktail> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(
            Drink.CODEC.optionalFieldOf("drink").forGetter(Cocktail::drink),
            DrinkIngredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(Cocktail::ingredients)
    ).apply(i, Cocktail::new));

    public static final Codec<Cocktail> CODEC = Codec.withAlternative(FULL_CODEC, Drink.CODEC, Cocktail::ofDrink);

    public static final StreamCodec<RegistryFriendlyByteBuf, Cocktail> STREAM_CODEC = StreamCodec.composite(
            Drink.STREAM_CODEC.apply(ByteBufCodecs::optional), Cocktail::drink,
            DrinkIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), Cocktail::ingredients,
            Cocktail::new
    );

    public Cocktail(Optional<Holder<Drink>> drink, List<Holder<DrinkIngredient>> ingredients) {
        this(drink, ingredients, computeColor(drink, ingredients));
    }

    public static Cocktail ofDrink(Holder<Drink> drink) {
        return new Cocktail(Optional.of(drink), List.of());
    }

    public static Cocktail ofIngredients(List<Holder<DrinkIngredient>> ingredients) {
        return new Cocktail(Optional.empty(), ingredients);
    }

    private static int computeColor(Optional<Holder<Drink>> drink, List<Holder<DrinkIngredient>> ingredients) {
        if (drink.isPresent()) {
            return drink.get().value().color();
        }

        int color = DEFAULT_COLOR;

        for (Holder<DrinkIngredient> ingredientHolder : ingredients) {
            DrinkIngredient ingredient = ingredientHolder.value();
            color = FastColor.ARGB32.lerp(ingredient.alpha(), color, ingredient.color());
        }

        return color;
    }

    public void onDrink(ServerPlayer player) {
        drink.ifPresent(holder -> holder.value().onDrink(player));
    }
}
