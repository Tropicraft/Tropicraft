package net.tropicraft.core.common.enums;

public enum TropicraftSaplings implements ITropicraftVariant {

	PALM(0), MAHOGANY(1), GRAPEFRUIT(2), LEMON(3), LIME(4), ORANGE(5);
	
	private final int meta;
	private static final TropicraftSaplings[] META_LOOKUP = new TropicraftSaplings[values().length];
	public static final TropicraftSaplings VALUES[] = values();
	
	private TropicraftSaplings(int meta) {
		this.meta = meta;
	}
	
	public int getMetadata() {
		return this.meta;
	}

	public static TropicraftSaplings byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getTypeName() {
	    return "sapling";
	}
    
	// Set META_LOOKUP table
	static {
		for (TropicraftSaplings sapling : VALUES) {
			META_LOOKUP[sapling.getMetadata()] = sapling;
		}
	}
}
