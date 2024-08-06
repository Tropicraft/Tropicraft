package net.tropicraft.core.common.drinks;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.TropicraftItems;
import org.apache.commons.lang3.ArrayUtils;

public interface TropicraftDrinkIngredients {
    ResourceKey<DrinkIngredient> SUGAR = createKey("sugar");
    ResourceKey<DrinkIngredient> LEMON = createKey("lemon");
    ResourceKey<DrinkIngredient> LIME = createKey("lime");
    ResourceKey<DrinkIngredient> ORANGE = createKey("orange");
    ResourceKey<DrinkIngredient> GRAPEFRUIT = createKey("grapefruit");
    ResourceKey<DrinkIngredient> PINEAPPLE = createKey("pineapple");
    ResourceKey<DrinkIngredient> COCONUT = createKey("coconut");
    ResourceKey<DrinkIngredient> SUGAR_CANE = createKey("sugarcane");
    ResourceKey<DrinkIngredient> ROASTED_COFFEE_BEAN = createKey("roasted_coffee_bean");
    ResourceKey<DrinkIngredient> WATER_BUCKET = createKey("water_bucket");
    ResourceKey<DrinkIngredient> MILK_BUCKET = createKey("milk_bucket");
    ResourceKey<DrinkIngredient> COCOA_BEAN = createKey("cocoa_bean");
    ResourceKey<DrinkIngredient> PASSIONFRUIT = createKey("passionfruit");
    ResourceKey<DrinkIngredient> JOCOTE = createKey("jocote");
    ResourceKey<DrinkIngredient> PAPAYA = createKey("papaya");

    static void bootstrap(BootstrapContext<DrinkIngredient> context) {
        register(context, SUGAR, Items.SUGAR.builtInRegistryHolder(), 0xffffff, 0.1f);
        register(context, LEMON, TropicraftItems.LEMON, 0xffff00, 1.0f);
        register(context, LIME, TropicraftItems.LIME, 0x7fff00, 1.0f);
        register(context, ORANGE, TropicraftItems.ORANGE, 0xffa500, 1.0f);
        register(context, GRAPEFRUIT, TropicraftItems.GRAPEFRUIT, 0xff6347, 1.0f);
        register(context, PINEAPPLE, TropicraftBlocks.PINEAPPLE.asItem().builtInRegistryHolder(), 0xeeff00, 1.0f, TropicraftItems.PINEAPPLE_CUBES);
        register(context, COCONUT, TropicraftBlocks.COCONUT.asItem().builtInRegistryHolder(), 0xefefef, 1.0f, TropicraftItems.COCONUT_CHUNK);
        register(context, SUGAR_CANE, Items.SUGAR_CANE.builtInRegistryHolder(), 0xb1ff6b, 0.1f);
        register(context, ROASTED_COFFEE_BEAN, TropicraftItems.ROASTED_COFFEE_BEAN, 0x68442c, 0.95f);
        register(context, WATER_BUCKET, Items.WATER_BUCKET.builtInRegistryHolder(), 0xffffff, 1.0f);
        register(context, MILK_BUCKET, Items.MILK_BUCKET.builtInRegistryHolder(), 0xffffff, 0.1f);
        register(context, COCOA_BEAN, Items.COCOA_BEANS.builtInRegistryHolder(), 0x805A3E, 0.95f);
        register(context, PASSIONFRUIT, TropicraftItems.PASSIONFRUIT, 0x690b2d, 1.0f);
        register(context, JOCOTE, TropicraftItems.JOCOTE, 0xc1cd02, 1.0f);
        register(context, PAPAYA, TropicraftItems.PAPAYA, 0x3fbf3f, 1.0f);
    }

    private static void register(BootstrapContext<DrinkIngredient> context, ResourceKey<DrinkIngredient> key, Holder<Item> item, int color, float weight, Holder<Item>... extraItems) {
        Component description = Component.translatable(item.value().getDescriptionId());
        HolderSet.Direct<Item> validItems = HolderSet.direct(ArrayUtils.add(extraItems, item));
        context.register(key, new DrinkIngredient(description, validItems, color, weight));
    }

    static ResourceKey<DrinkIngredient> createKey(String name) {
        return Tropicraft.resourceKey(TropicraftRegistries.DRINK_INGREDIENT, name);
    }
}
