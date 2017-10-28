package net.tropicraft.core.common.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;
import net.tropicraft.core.registry.TropicraftRegistry;

/**
 * Intended to be implemented on enums, as the default impls will cast to it without checks.
 * 
 * However, overriding these can allow it to be used on any type.
 */
public interface ITropicraftVariant extends IStringSerializable {

    default String getSimpleName() {
        return ((Enum<?>) this).name().toLowerCase(Locale.ROOT);
    }

    String getTypeName();

    default String getUnlocName() {
        return TropicraftRegistry.getNamePrefixed(getTypeName() + "." + getSimpleName());
    }

    @Override
    default String getName() {
        return getSimpleName();
    }

    default int getMeta() {
        return ((Enum<?>) this).ordinal();
    }

    String toString();
}
