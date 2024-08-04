package net.tropicraft.core.common;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.DrinkIngredient;
import net.tropicraft.core.common.drinks.action.DrinkAction;

@EventBusSubscriber(modid = Tropicraft.ID, bus = EventBusSubscriber.Bus.MOD)
public class TropicraftRegistries {
    public static final ResourceKey<Registry<Drink>> DRINK = createKey("drink");
    public static final ResourceKey<Registry<MapCodec<? extends DrinkAction>>> DRINK_ACTION = createKey("drink_action");
    public static final ResourceKey<Registry<DrinkIngredient>> DRINK_INGREDIENT = createKey("drink_ingredient");

    public static final Registry<MapCodec<? extends DrinkAction>> DRINK_ACTION_REGISTRY = new RegistryBuilder<>(DRINK_ACTION).create();

    @SubscribeEvent
    public static void registerBuiltinRegistries(NewRegistryEvent event) {
        event.register(DRINK_ACTION_REGISTRY);
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DRINK, Drink.DIRECT_CODEC, Drink.NETWORK_CODEC);
        event.dataPackRegistry(DRINK_INGREDIENT, DrinkIngredient.DIRECT_CODEC, DrinkIngredient.NETWORK_CODEC);
    }

    private static <T> ResourceKey<Registry<T>> createKey(String path) {
        return ResourceKey.createRegistryKey(Tropicraft.location(path));
    }
}
