package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftFruitLeaves implements IStringSerializable {
    GRAPEFRUIT(0), LEMON(1), LIME(2), ORANGE(3);
    
    private static final TropicraftFruitLeaves[] META_LOOKUP = new TropicraftFruitLeaves[values().length];
    private final int meta;
    public static final TropicraftFruitLeaves[] VALUES = values();
    
    private TropicraftFruitLeaves(int meta) {
    	this.meta = meta;
    }
    
	public int getMetadata() {
		return this.meta;
	}
	
	public static TropicraftFruitLeaves byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_fruitleaves";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
	// Set META_LOOKUP table
	static {
		for (TropicraftFruitLeaves leaf : values()) {
			META_LOOKUP[leaf.getMetadata()] = leaf;
		}
	}
};
