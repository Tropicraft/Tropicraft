package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftTallPlants implements IStringSerializable {

	PINEAPPLE(0), IRIS(1);

	private final int meta;
	private static final TropicraftTallPlants[] META_LOOKUP = new TropicraftTallPlants[values().length];

	private TropicraftTallPlants(int meta) {
		this.meta = meta;
	}

	public int getMetadata() {
		return this.meta;
	}

	public static TropicraftTallPlants byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}

	@Override
	public String toString() {
		return this.getName();
	}

	// Set META_LOOKUP table
	static {
		for (TropicraftTallPlants plant : values()) {
			META_LOOKUP[plant.getMetadata()] = plant;
		}
	}
}
