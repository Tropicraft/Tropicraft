package net.tropicraft.core.common.enums;

public enum TropicraftFruitLeaves implements ITropicraftVariant {
    GRAPEFRUIT(0), LEMON(1), LIME(2), ORANGE(3);
    
    private static final TropicraftFruitLeaves[] META_LOOKUP = new TropicraftFruitLeaves[values().length];
    private final int meta;
    public static final TropicraftFruitLeaves[] VALUES = values();
    
    private TropicraftFruitLeaves(int meta) {
    	this.meta = meta;
    }
    
    @Override
	public int getMeta() {
		return this.meta;
	}
	
	public static TropicraftFruitLeaves byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

    @Override
    public String getTypeName() {
        return "fruitleaves";
    }

	// Set META_LOOKUP table
	static {
		for (TropicraftFruitLeaves leaf : values()) {
			META_LOOKUP[leaf.getMeta()] = leaf;
		}
	}
};
