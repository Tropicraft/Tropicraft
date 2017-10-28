package net.tropicraft.core.common.enums;

public enum TropicraftOres implements ITropicraftVariant {
    AZURITE, EUDIALYTE, ZIRCON;
    
    public static final TropicraftOres VALUES[] = values();

    @Override
    public String getTypeName() {
        return "ore";
    }
};
