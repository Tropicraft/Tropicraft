package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

import java.util.List;

public record Cocktail(
        Drink drink,
        List<Ingredient> ingredients,
        int color
) {
    public static final int DEFAULT_COLOR = 0xf3be36;

    public static final Codec<Cocktail> CODEC = RecordCodecBuilder.create(i -> i.group(
            Drink.CODEC.fieldOf("drink").forGetter(Cocktail::drink),
            Ingredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(Cocktail::ingredients)
    ).apply(i, Cocktail::new));

    public static final StreamCodec<ByteBuf, Cocktail> STREAM_CODEC = StreamCodec.composite(
            Drink.STREAM_CODEC, Cocktail::drink,
            Ingredient.STREAM_CODEC.apply(ByteBufCodecs.list()), Cocktail::ingredients,
            Cocktail::new
    );

    public Cocktail(Drink drink, List<Ingredient> ingredients) {
        this(drink, ingredients, computeColor(ingredients));
    }

    public static int computeColor(List<Ingredient> ingredients) {
        Ingredient primary = ingredients.stream().filter(Ingredient::isPrimary).findAny().orElse(null);
        int color = primary == null ? DEFAULT_COLOR : primary.getColor();

        for (Ingredient ingredient : ingredients) {
            if (ingredient != primary) {
                color = FastColor.ARGB32.lerp(ingredient.getAlpha(), color, ingredient.getColor());
            }
        }

        return color;
    }
}
