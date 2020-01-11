package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

import java.util.function.Function;
import java.util.function.Supplier;

public class Builder {
    
    public static Supplier<Item> item() {
        return item(getDefaultProperties());
    }
    
    public static Supplier<Item> item(Item.Properties properties) {
        return item(Item::new, properties);
    }
    
    public static <T> Supplier<T> item(Function<Item.Properties, T> ctor) {
        return item(ctor, getDefaultProperties());
    }
    
    public static <T> Supplier<T> item(Function<Item.Properties, T> ctor, Item.Properties properties) {
        return item(ctor, () -> properties);
    }
    
    public static <T> Supplier<T> item(Function<Item.Properties, T> ctor, Supplier<Item.Properties> properties) {
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

    public static <T extends AbstractFishEntity> Supplier<Item> fishBucket(final Supplier<EntityType<T>> type) {
        return item(p -> new TropicraftFishBucketItem<>(type, Fluids.WATER, getDefaultProperties().maxStackSize(1)));
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

    public static Supplier<AshenMaskItem> mask(final AshenMasks mask) {
        return item(p -> new AshenMaskItem(ArmorMaterials.ASHEN_MASK, mask, p));
    }
    
    public static Supplier<TropicalMusicDiscItem> musicDisc(RecordMusic type) {
        return item(p -> new TropicalMusicDiscItem(type, p) {}, () -> getDefaultProperties().rarity(Rarity.RARE));
    }

    public static <T extends Entity> Supplier<Item> spawnEgg(final RegistryObject<EntityType<T>> type) {
        return item(p -> new TropicraftSpawnEgg(type, p), Builder::getDefaultProperties);
    }

    private static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP);
    }
}
