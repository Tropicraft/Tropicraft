package net.tropicraft.core.common.enums;

public enum TropicraftLogs implements ITropicraftVariant {
	MAHOGANY(0), PALM(1);

	private final int meta;
	private static final TropicraftLogs[] META_LOOKUP = new TropicraftLogs[values().length];

	private TropicraftLogs(int meta) {
		this.meta = meta;
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
