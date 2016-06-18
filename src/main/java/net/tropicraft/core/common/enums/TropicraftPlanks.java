package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftPlanks implements IStringSerializable {
    MAHOGANY, PALM;
    
    public static final TropicraftPlanks VALUES[] = values();

    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_plank";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
};
