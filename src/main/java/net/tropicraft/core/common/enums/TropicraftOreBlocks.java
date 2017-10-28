package net.tropicraft.core.common.enums;

public enum TropicraftOreBlocks implements ITropicraftVariant {
    AZURITE, EUDIALYTE, ZIRCON;
    
    public static final TropicraftOreBlocks VALUES[] = values();

    @Override
    public String getTypeName() {
        return "oreblock";
    }
};
