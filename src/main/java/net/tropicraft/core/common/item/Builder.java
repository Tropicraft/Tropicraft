package net.tropicraft.core.common.item;

import java.util.function.Supplier;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.drinks.Drink;

public class Builder {

    public static Supplier<UmbrellaItem> umbrella(final ColorHelper.Color color) {
        return () -> new UmbrellaItem(getDefaultProperties(), color);
    }

    public static Supplier<Item> shell() {
        return () -> new ShellItem(getDefaultProperties());
    }

    public static Supplier<Item> food(final Food food) {
        return () -> new Item(getDefaultProperties().food(food));
    }

    public static Supplier<Item> cocktail(final Drink drink) {
        return () -> new CocktailItem(new Item.Properties().maxDamage(0).maxStackSize(1).containerItem(TropicraftItems.BAMBOO_MUG.get()));
    }

    public static Supplier<Item> item() {
        return () -> new Item(getDefaultProperties());
    }

    public static Supplier<Item> mask(final int maskIndex) {
        return () -> new AshenMaskItem(ArmorMaterials.ASHEN_MASK, maskIndex, getDefaultProperties());
    }

    private static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP);
    }
}
