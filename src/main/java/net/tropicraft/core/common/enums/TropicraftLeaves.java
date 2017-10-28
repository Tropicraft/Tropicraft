package net.tropicraft.core.common.enums;

public enum TropicraftLeaves implements ITropicraftVariant {
    MAHOGANY(false, 0), PALM(true, 1), KAPOK(true, 2), FRUIT(false, 3);
    
    private static final TropicraftLeaves[] META_LOOKUP = new TropicraftLeaves[values().length];
    private final boolean solid;
    private final int meta;
    public static final TropicraftLeaves[] VALUES = values();
    
    private TropicraftLeaves(boolean solid, int meta) {
    	this.solid = solid;
    	this.meta = meta;
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
