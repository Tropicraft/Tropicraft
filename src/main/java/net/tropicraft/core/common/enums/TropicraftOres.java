package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftOres implements IStringSerializable {
    AZURITE, EUDIALYTE, ZIRCON;
    
    public static final TropicraftOres VALUES[] = values();

    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_oreblock";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
};
