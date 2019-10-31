package net.tropicraft.core.common.item;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class Builder {
    
    public static Supplier<Item> item() {
        return item(getDefaultProperties());
    }
    
    public static Supplier<Item> item(Item.Properties properties) {
        return item(Item::new, properties);
    }
    
    private static <T> Supplier<T> item(Function<Item.Properties, T> ctor) {
        return item(ctor, getDefaultProperties());
    }
    
    private static <T> Supplier<T> item(Function<Item.Properties, T> ctor, Item.Properties properties) {
        return item(ctor, () -> properties);
    }
    
    private static <T> Supplier<T> item(Function<Item.Properties, T> ctor, Supplier<Item.Properties> properties) {
        return () -> ctor.apply(properties.get());
    }
    
    private static <T extends FurnitureEntity> Supplier<FurnitureItem<T>> furniture(Supplier<EntityType<T>> type, DyeColor color) {
        return item(p -> new FurnitureItem<>(p, type, color));
    }

    public static Supplier<FurnitureItem<UmbrellaEntity>> umbrella(final DyeColor color) {
        return furniture(TropicraftEntities.UMBRELLA, color);
    }
    
    public static Supplier<FurnitureItem<ChairEntity>> chair(final DyeColor color) {
        return furniture(TropicraftEntities.CHAIR, color);
    }
    
    public static Supplier<FurnitureItem<BeachFloatEntity>> beachFloat(final DyeColor color) {
        return furniture(TropicraftEntities.BEACH_FLOAT, color);
    }

    public static Supplier<Item> shell() {
        return item(ShellItem::new);
    }

    public static Supplier<Item> food(final Food food) {
        return item(getDefaultProperties().food(food));
    }

    public static Supplier<CocktailItem> cocktail(final Drink drink) {
        return item(p -> new CocktailItem(drink, p), () -> getDefaultProperties().maxDamage(0).maxStackSize(1).containerItem(TropicraftItems.BAMBOO_MUG.get()));
    }

    public static Supplier<Item> mask(final int maskIndex) {
        return item(p -> new AshenMaskItem(ArmorMaterials.ASHEN_MASK, maskIndex, p));
    }
    
    public static Supplier<TropicalMusicDiscItem> musicDisc(RecordMusic type) {
        return item(p -> new TropicalMusicDiscItem(type, p) {}, () -> getDefaultProperties().rarity(Rarity.RARE));
    }

    private static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP);
    }
}
