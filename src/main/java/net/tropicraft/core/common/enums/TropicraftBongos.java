package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftBongos  implements IStringSerializable {
    SMALL("bongohigh"), MEDIUM("bongomedium"), LARGE("bongolow");

    private String soundRegistryName;
    public static final TropicraftBongos VALUES[] = values();

    TropicraftBongos(String soundRegistryName) {
        this.soundRegistryName = soundRegistryName;
    }

    public String getSoundRegistryName() {
        return this.soundRegistryName;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public String getName() {
        return "bongo_" + this.name().toLowerCase();
    }
}
