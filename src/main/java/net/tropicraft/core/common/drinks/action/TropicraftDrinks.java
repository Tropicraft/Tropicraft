package net.tropicraft.core.common.drinks.action;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.List;
import java.util.Optional;

public interface TropicraftDrinks {
    ResourceKey<Drink> LEMONADE = createKey("lemonade");
    ResourceKey<Drink> LIMEADE = createKey("limeade");
    ResourceKey<Drink> ORANGEADE = createKey("orangeade");
    ResourceKey<Drink> CAIPIRINHA = createKey("caipirinha");
    ResourceKey<Drink> BLACK_COFFEE = createKey("black_coffee");
    ResourceKey<Drink> PINA_COLADA = createKey("pina_colada");
    ResourceKey<Drink> COCONUT_WATER = createKey("coconut_water");
    ResourceKey<Drink> MAI_TAI = createKey("mai_tai");

    static void bootstrap(BootstrapContext<Drink> context) {
        context.register(LEMONADE, new Drink(
                TropicraftLangKeys.LEMONADE.component(ChatFormatting.YELLOW),
                0xfadb41,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1))
        ));
        context.register(LIMEADE, new Drink(
                TropicraftLangKeys.LIMEADE.component(ChatFormatting.GREEN),
                0x84e88a,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1))
        ));
        context.register(ORANGEADE, new Drink(
                TropicraftLangKeys.ORANGEADE.component(ChatFormatting.GOLD),
                0xf3be36,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1))
        ));
        context.register(CAIPIRINHA, new Drink(
                TropicraftLangKeys.CAIPIRINHA.component(ChatFormatting.GREEN),
                0x94ff36,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1))
        ));
        context.register(BLACK_COFFEE, new Drink(
                TropicraftLangKeys.BLACK_COFFEE.component(ChatFormatting.WHITE),
                0x68442c,
                List.of(
                        new PotionDrinkAction(MobEffects.REGENERATION, 5, 1),
                        new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 2)
                )
        ));
        context.register(PINA_COLADA, new Drink(
                TropicraftLangKeys.PINA_COLADA.component(ChatFormatting.GOLD),
                0xefefef,
                List.of(
                        new PotionDrinkAction(MobEffects.CONFUSION, 10, 0),
                        new PortalDrinkAction(
                                TropicraftDimension.WORLD,
                                Optional.of(HolderSet.direct(TropicraftEntities.CHAIR)),
                                12200,
                                14000
                        )
                )
        ));
        context.register(COCONUT_WATER, new Drink(
                TropicraftLangKeys.COCONUT_WATER.component(ChatFormatting.WHITE),
                0xdfdfdf,
                List.of(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1))
        ));
        context.register(MAI_TAI, new Drink(
                TropicraftLangKeys.MAI_TAI.component(ChatFormatting.GOLD), 0xff772e,
                List.of(new PotionDrinkAction(MobEffects.CONFUSION, 5, 0))
        ));
    }

    static ResourceKey<Drink> createKey(String name) {
        return Tropicraft.resourceKey(TropicraftRegistries.DRINK, name);
    }
}
