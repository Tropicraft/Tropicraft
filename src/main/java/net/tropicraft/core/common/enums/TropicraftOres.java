package net.tropicraft.core.common.enums;

public enum TropicraftOres implements ITropicraftVariant {
    AZURITE, EUDIALYTE, ZIRCON, MANGANESE, SHAKA;
    
    public static final TropicraftOres VALUES[] = values();
    
    public static final TropicraftOres ORES_WITH_BLOCKS[] = {AZURITE, EUDIALYTE, ZIRCON};

    @Override
    public String getTypeName() {
        return "ore";
    }
};
