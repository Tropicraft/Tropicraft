package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftCorals implements IStringSerializable {

	PINK, TEALY, BRAIN, FIRE, GREEN, SPIRAL, HOTPINK;
	
    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_coral";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
