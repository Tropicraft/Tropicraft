package net.tropicraft.core.common.enums;

public enum TropicraftLeaves implements ITropicraftVariant {
    MAHOGANY(false, 0, TropicraftSaplings.MAHOGANY), PALM(true, 1, TropicraftSaplings.PALM), KAPOK(true, 2, null), FRUIT(false, 3, null);
    
    private static final TropicraftLeaves[] META_LOOKUP = new TropicraftLeaves[values().length];
    private final boolean solid;
    private final int meta;
    /** Sapling that matches with this leaf type */
    private final TropicraftSaplings sapling;
    public static final TropicraftLeaves[] VALUES = values();

    private TropicraftLeaves(boolean solid, int meta, TropicraftSaplings sapling) {
        this.solid = solid;
        this.meta = meta;
        this.sapling = sapling;
    }

    public int getSaplingMeta() {
        if (sapling == null) {
            return -1;
        }
        return this.sapling.getMetadata();
    }

    public boolean isSolid() {
    	return solid;
    }
    
    @Override
	public int getMeta() {
		return this.meta;
	}
	
	public static TropicraftLeaves byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getTypeName() {
	    return "leaves";
	}
    
	// Set META_LOOKUP table
	static {
		for (TropicraftLeaves leaf : values()) {
			META_LOOKUP[leaf.getMeta()] = leaf;
		}
	}
};
