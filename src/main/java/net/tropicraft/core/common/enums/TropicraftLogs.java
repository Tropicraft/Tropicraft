package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftLogs implements IStringSerializable {
	MAHOGANY(0), PALM(1);

	private final int meta;
	private static final TropicraftLogs[] META_LOOKUP = new TropicraftLogs[values().length];

	private TropicraftLogs(int meta) {
		this.meta = meta;
	}

	public int getMetadata() {
		return this.meta;
	}

	public static TropicraftLogs byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getName() {
		return this.name().toLowerCase() + "_log";
	}

	@Override
	public String toString() {
		return this.getName();
	}

	// Set META_LOOKUP table
	static {
		for (TropicraftLogs log : values()) {
			META_LOOKUP[log.getMetadata()] = log;
		}
	}
};
