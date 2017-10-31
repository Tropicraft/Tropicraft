package net.tropicraft.core.common.enums;

public enum TropicraftLogs implements ITropicraftVariant {
	MAHOGANY(0, BlockHardnessValues.MAHOGANY.hardness, BlockHardnessValues.MAHOGANY.resistance),
	PALM(1, BlockHardnessValues.PALM.hardness, BlockHardnessValues.PALM.resistance);

    private final float hardness;
    private final float resistance;
	private final int meta;
	private static final TropicraftLogs[] META_LOOKUP = new TropicraftLogs[values().length];

	private TropicraftLogs(int meta, float hardness, float resistance) {
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

	public static int getMetaByName(String name) {
		for (TropicraftLogs log : META_LOOKUP) {
			if (log.getName().equals(name)) {
				return log.getMeta();
			}
		}

		return -1;
	}

	public static TropicraftLogs byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    @Override
    public String getTypeName() {
        return "log";
    }

	// Set META_LOOKUP table
	static {
		for (TropicraftLogs log : values()) {
			META_LOOKUP[log.getMeta()] = log;
		}
	}
};
