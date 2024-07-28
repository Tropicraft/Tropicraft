package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerPlayer;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.drinks.action.DrinkAction;

import java.util.List;

public record Drink(
        Component name,
        int color,
        List<DrinkAction> actions
) {
    public static final Codec<Drink> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Drink::name),
            Codec.INT.fieldOf("color").forGetter(Drink::color),
            DrinkAction.CODEC.listOf().optionalFieldOf("actions", List.of()).forGetter(Drink::actions)
    ).apply(i, Drink::new));

    public static final Codec<Drink> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Drink::name),
            Codec.INT.fieldOf("color").forGetter(Drink::color),
            // The client does not need to know about drink actions
            MapCodec.unit(List.<DrinkAction>of()).forGetter(Drink::actions)
    ).apply(i, Drink::new));

    public static final Codec<Holder<Drink>> CODEC = RegistryFixedCodec.create(TropicraftRegistries.DRINK);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Drink>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TropicraftRegistries.DRINK);

    public void onDrink(ServerPlayer player) {
        for (DrinkAction action : actions) {
            action.onDrink(player);
        }
    }
}
