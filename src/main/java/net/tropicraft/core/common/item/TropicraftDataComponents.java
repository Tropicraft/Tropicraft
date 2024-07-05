package net.tropicraft.core.common.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;
import net.tropicraft.core.common.drinks.Cocktail;

public class TropicraftDataComponents {
    public static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Constants.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SCUBA_AIR = REGISTER.registerComponentType(
            "scuba_air",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> EXPLOSION_RADIUS = REGISTER.registerComponentType(
            "explosion_radius",
            builder -> builder.persistent(Codec.floatRange(0.0f, 16.0f)).networkSynchronized(ByteBufCodecs.FLOAT)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SHELL_NAME = REGISTER.registerComponentType(
            "shell_name",
            builder -> builder.persistent(Codec.sizeLimitedString(64)).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Cocktail>> COCKTAIL = REGISTER.registerComponentType(
            "cocktail",
            builder -> builder.persistent(Cocktail.CODEC).networkSynchronized(Cocktail.STREAM_CODEC)
    );
}
