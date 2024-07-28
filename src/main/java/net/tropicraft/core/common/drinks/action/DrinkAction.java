package net.tropicraft.core.common.drinks.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;
import net.tropicraft.core.common.TropicraftRegistries;

import java.util.function.Function;

public interface DrinkAction {
    Codec<DrinkAction> CODEC = TropicraftRegistries.DRINK_ACTION_REGISTRY.byNameCodec().dispatch(DrinkAction::codec, Function.identity());

    void onDrink(ServerPlayer player);

    MapCodec<? extends DrinkAction> codec();
}
