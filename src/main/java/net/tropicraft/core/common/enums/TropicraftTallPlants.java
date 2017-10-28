package net.tropicraft.core.common.enums;

public enum TropicraftTallPlants implements ITropicraftVariant {

	PINEAPPLE(0);

	private final int meta;
	private static final TropicraftTallPlants[] META_LOOKUP = new TropicraftTallPlants[values().length];

	private TropicraftTallPlants(int meta) {
		this.meta = meta;
	}

	@Override
	public int getMeta() {
		return this.meta;
	}

	public static TropicraftTallPlants byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getTypeName() {
	    return "tall_plant";
	}

	// Set META_LOOKUP table
	static {
		for (TropicraftTallPlants plant : values()) {
			META_LOOKUP[plant.getMeta()] = plant;
		}
	}
}
