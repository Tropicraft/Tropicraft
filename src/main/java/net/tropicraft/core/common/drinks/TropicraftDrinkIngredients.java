package net.tropicraft.core.common.drinks;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.TropicraftItems;

public interface TropicraftDrinkIngredients {
    ResourceKey<DrinkIngredient> SUGAR = createKey("sugar");
    ResourceKey<DrinkIngredient> LEMON = createKey("lemon");
    ResourceKey<DrinkIngredient> LIME = createKey("lime");
    ResourceKey<DrinkIngredient> ORANGE = createKey("orange");
    ResourceKey<DrinkIngredient> GRAPEFRUIT = createKey("grapefruit");
    ResourceKey<DrinkIngredient> PINEAPPLE = createKey("pineapple");
    ResourceKey<DrinkIngredient> PINEAPPLE_CUBES = createKey("pineapple_cubes");
    ResourceKey<DrinkIngredient> COCONUT = createKey("coconut");
    ResourceKey<DrinkIngredient> COCONUT_CHUNK = createKey("coconut_chunk");
    ResourceKey<DrinkIngredient> SUGAR_CANE = createKey("sugarcane");
    ResourceKey<DrinkIngredient> ROASTED_COFFEE_BEAN = createKey("roasted_coffee_bean");
    ResourceKey<DrinkIngredient> WATER_BUCKET = createKey("water_bucket");
    ResourceKey<DrinkIngredient> MILK_BUCKET = createKey("milk_bucket");
    ResourceKey<DrinkIngredient> COCOA_BEAN = createKey("cocoa_bean");
    ResourceKey<DrinkIngredient> PASSIONFRUIT = createKey("passionfruit");
    ResourceKey<DrinkIngredient> JOCOTE = createKey("jocote");
    ResourceKey<DrinkIngredient> PAPAYA = createKey("papaya");

    static void bootstrap(BootstrapContext<DrinkIngredient> context) {
        context.register(SUGAR, new DrinkIngredient(Items.SUGAR.builtInRegistryHolder(), 0xffffff, 0.1f));
        context.register(LEMON, new DrinkIngredient(TropicraftItems.LEMON, 0xffff00, 1.0f));
        context.register(LIME, new DrinkIngredient(TropicraftItems.LIME, 0x7fff00, 1.0f));
        context.register(ORANGE, new DrinkIngredient(TropicraftItems.ORANGE, 0xffa500, 1.0f));
        context.register(GRAPEFRUIT, new DrinkIngredient(TropicraftItems.GRAPEFRUIT, 0xff6347, 1.0f));
        context.register(PINEAPPLE, new DrinkIngredient(TropicraftBlocks.PINEAPPLE.asItem().builtInRegistryHolder(), 0xeeff00, 1.0f));
        context.register(PINEAPPLE_CUBES, new DrinkIngredient(TropicraftItems.PINEAPPLE_CUBES, 0xeeff00, 0.1f));
        context.register(COCONUT, new DrinkIngredient(TropicraftBlocks.COCONUT.asItem().builtInRegistryHolder(), 0xefefef, 1.0f));
        context.register(COCONUT_CHUNK, new DrinkIngredient(TropicraftItems.COCONUT_CHUNK, 0xefefef, 1.0f));
        context.register(SUGAR_CANE, new DrinkIngredient(Items.SUGAR_CANE.builtInRegistryHolder(), 0xb1ff6b, 0.1f));
        context.register(ROASTED_COFFEE_BEAN, new DrinkIngredient(TropicraftItems.ROASTED_COFFEE_BEAN, 0x68442c, 0.95f));
        context.register(WATER_BUCKET, new DrinkIngredient(Items.WATER_BUCKET.builtInRegistryHolder(), 0xffffff, 1.0f));
        context.register(MILK_BUCKET, new DrinkIngredient(Items.MILK_BUCKET.builtInRegistryHolder(), 0xffffff, 0.1f));
        context.register(COCOA_BEAN, new DrinkIngredient(Items.COCOA_BEANS.builtInRegistryHolder(), 0x805A3E, 0.95f));
        context.register(PASSIONFRUIT, new DrinkIngredient(TropicraftItems.PASSIONFRUIT, 0x690b2d, 1.0f));
        context.register(JOCOTE, new DrinkIngredient(TropicraftItems.JOCOTE, 0xc1cd02, 1.0f));
        context.register(PAPAYA, new DrinkIngredient(TropicraftItems.PAPAYA, 0x3fbf3f, 1.0f));
    }

    default Holder<DrinkIngredient> getHolder(HolderGetter<DrinkIngredient> lookup, ResourceKey<DrinkIngredient> resourceKey) {
        return lookup.getOrThrow(resourceKey);
    }

    static ResourceKey<DrinkIngredient> createKey(String name) {
        return Tropicraft.resourceKey(TropicraftRegistries.DRINK_INGREDIENT, name);
    }
}
