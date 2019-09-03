package net.tropicraft.core.common.item;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.drinks.Drink;

public class Builder {

    public static UmbrellaItem umbrella(final ColorHelper.Color color) {
        return new UmbrellaItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP), color);
    }

    public static Item shell() {
        return new ShellItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP));
    }

    public static Item food(final Food food) {
        return new Item(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP).food(food));
    }

    public static Item cocktail(final Drink drink) {
        return new CocktailItem(new Item.Properties().maxDamage(0).maxStackSize(1).containerItem(TropicraftItems.BAMBOO_MUG));
    }
}
