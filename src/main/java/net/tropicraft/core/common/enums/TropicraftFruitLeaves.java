package net.tropicraft.core.common.enums;

public enum TropicraftFruitLeaves implements ITropicraftVariant {
    GRAPEFRUIT(0, TropicraftSaplings.GRAPEFRUIT),
	LEMON(1, TropicraftSaplings.LEMON),
	LIME(2, TropicraftSaplings.LIME),
    ORANGE(3, TropicraftSaplings.ORANGE);
    
    private static final TropicraftFruitLeaves[] META_LOOKUP = new TropicraftFruitLeaves[values().length];
    private final int meta;
    /** Sapling that matches with this leaf type */
    private final TropicraftSaplings sapling;
    public static final TropicraftFruitLeaves[] VALUES = values();

    private TropicraftFruitLeaves(int meta, TropicraftSaplings sapling) {
        this.meta = meta;
        this.sapling = sapling;
    }

    public int getSaplingMeta() {
        return this.sapling.getMetadata();
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
