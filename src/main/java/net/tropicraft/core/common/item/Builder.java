package net.tropicraft.core.common.item;

import net.minecraft.item.Item;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.ColorHelper;

public class Builder {

    public static UmbrellaItem umbrella(final ColorHelper.Color color) {
        return new UmbrellaItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP), color);
    }

    public static Item shell() {
        return new Item(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP));
    }
}
