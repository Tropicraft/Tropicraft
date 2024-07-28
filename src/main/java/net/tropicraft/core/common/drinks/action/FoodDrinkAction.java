package net.tropicraft.core.common.drinks.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public record FoodDrinkAction(
        int healAmount,
        float saturationModifier
) implements DrinkAction {
    public static final MapCodec<FoodDrinkAction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("heal_amount").forGetter(FoodDrinkAction::healAmount),
            Codec.FLOAT.fieldOf("saturation_modifier").forGetter(FoodDrinkAction::saturationModifier)
    ).apply(i, FoodDrinkAction::new));

    @Override
    public void onDrink(ServerPlayer player) {
        player.getFoodData().eat(healAmount, saturationModifier);
    }

    @Override
    public MapCodec<FoodDrinkAction> codec() {
        return CODEC;
    }
}
