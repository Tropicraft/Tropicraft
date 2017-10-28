package net.tropicraft.core.common.enums;

public enum TropicraftBundles implements ITropicraftVariant {
	
	THATCH(0), BAMBOO(1);

	private final int meta;
	private static final TropicraftBundles[] META_LOOKUP = new TropicraftBundles[values().length];
	
	private TropicraftBundles(int meta) {
		this.meta = meta;
	}

	@Override
	public int getMeta() {
		return this.meta;
	}

	public static TropicraftBundles byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}
    
    @Override
    public String getTypeName() {
        return "bundle";
    }

	// Set META_LOOKUP table
	static {
		for (TropicraftBundles bundle : values()) {
			META_LOOKUP[bundle.getMeta()] = bundle;
		}
	}
}
