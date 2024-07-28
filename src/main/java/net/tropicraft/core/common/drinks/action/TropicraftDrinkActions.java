package net.tropicraft.core.common.drinks.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftRegistries;

public class TropicraftDrinkActions {
    public static final DeferredRegister<MapCodec<? extends DrinkAction>> REGISTER = DeferredRegister.create(TropicraftRegistries.DRINK_ACTION, Tropicraft.ID);

    public static final Holder<MapCodec<? extends DrinkAction>> FOOD = REGISTER.register("food", () -> FoodDrinkAction.CODEC);
    public static final Holder<MapCodec<? extends DrinkAction>> POTION = REGISTER.register("potion", () -> PotionDrinkAction.CODEC);
    public static final Holder<MapCodec<? extends DrinkAction>> PORTAL = REGISTER.register("portal", () -> PortalDrinkAction.CODEC);
}
