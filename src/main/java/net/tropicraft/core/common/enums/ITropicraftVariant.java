package net.tropicraft.core.common.enums;

import java.util.Locale;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.tropicraft.core.registry.TropicraftRegistry;

/**
 * Intended to be implemented on enums, as the default impls will cast to it without checks.
 * 
 * However, overriding these can allow it to be used on any type.
 */
public interface ITropicraftVariant extends IStringSerializable {

    default @Nonnull String getSimpleName() {
        return ((Enum<?>) this).name().toLowerCase(Locale.ROOT);
    }

    String getTypeName();

    default String getUnlocName() {
        return TropicraftRegistry.getNamePrefixed(getTypeName() + "." + getSimpleName());
    }

    @Override
    default @Nonnull String getName() {
        return getSimpleName();
    }

    default int getMeta() {
        return ((Enum<?>) this).ordinal();
    }

    String toString();
    
    default @Nonnull ItemStack makeStack(@Nonnull Item item) {
        return new ItemStack(item, 1, getMeta());
    }
    
    default @Nonnull ItemStack makeStack(@Nonnull Block block) {
        return new ItemStack(block, 1, getMeta());
    }
}
