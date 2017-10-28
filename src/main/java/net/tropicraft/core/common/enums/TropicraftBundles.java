package net.tropicraft.core.common.enums;

public enum TropicraftBundles implements ITropicraftVariant {
	
	THATCH(0, BlockHardnessValues.THATCH.hardness, BlockHardnessValues.THATCH.resistance),
	BAMBOO(1, BlockHardnessValues.BAMBOO.hardness, BlockHardnessValues.BAMBOO.resistance);

    private final float resistance;
    private final float hardness;
	private final int meta;
	private static final TropicraftBundles[] META_LOOKUP = new TropicraftBundles[values().length];
	
	private TropicraftBundles(int meta, float hardness, float resistance) {
		this.meta = meta;
		this.hardness = hardness;
		this.resistance = resistance;
	}

	public float getHardness() {
	    return this.hardness;
	}

	public float getResistance() {
	    return this.resistance;
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
