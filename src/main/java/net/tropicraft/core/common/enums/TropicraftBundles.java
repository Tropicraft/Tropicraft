package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftBundles implements IStringSerializable {
	
	THATCH(0), BAMBOO(1);

	private final int meta;
	private static final TropicraftBundles[] META_LOOKUP = new TropicraftBundles[values().length];
	
	private TropicraftBundles(int meta) {
		this.meta = meta;
	}

	public int getMetadata() {
		return this.meta;
	}

	public static TropicraftBundles byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getName() {
		return this.name().toLowerCase() + "_bundle";
	}

	@Override
	public String toString() {
		return this.getName();
	}

	// Set META_LOOKUP table
	static {
		for (TropicraftBundles bundle : values()) {
			META_LOOKUP[bundle.getMetadata()] = bundle;
		}
	}
}
