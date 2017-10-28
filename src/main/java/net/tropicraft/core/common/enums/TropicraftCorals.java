package net.tropicraft.core.common.enums;

public enum TropicraftCorals implements ITropicraftVariant {

	PINK(0), TEALY(1), BRAIN(2), FIRE(3), GREEN(4), SPIRAL(5), HOTPINK(6);
	
	private final int meta;
	private static final TropicraftCorals[] META_LOOKUP = new TropicraftCorals[values().length];
	public static final TropicraftCorals VALUES[] = values();
	
	private TropicraftCorals(int meta) {
		this.meta = meta;
	}
	
	@Override
	public int getMeta() {
		return this.meta;
	}

	public static TropicraftCorals byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}
	
	@Override
	public String getTypeName() {
	    return "coral";
	}
    
	// Set META_LOOKUP table
	static {
		for (TropicraftCorals coral : VALUES) {
			META_LOOKUP[coral.getMeta()] = coral;
		}
	}
}
