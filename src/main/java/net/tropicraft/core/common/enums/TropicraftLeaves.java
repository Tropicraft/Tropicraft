package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftLeaves implements IStringSerializable {
    MAHOGANY(0), PALM(1), KAPOK(2);
    
    private static final TropicraftLeaves[] META_LOOKUP = new TropicraftLeaves[values().length];
    private final int meta;
    public static final TropicraftLeaves[] VALUES = values();
    
    private TropicraftLeaves(int meta) {
    	this.meta = meta;
    }
    
	public int getMetadata() {
		return this.meta;
	}
	
	public static TropicraftLeaves byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_leaves";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
	// Set META_LOOKUP table
	static {
		for (TropicraftLeaves leaf : values()) {
			META_LOOKUP[leaf.getMetadata()] = leaf;
		}
	}
};
