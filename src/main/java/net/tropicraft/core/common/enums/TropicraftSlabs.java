package net.tropicraft.core.common.enums;

import net.minecraft.block.material.MapColor;

public enum TropicraftSlabs implements ITropicraftVariant {
	BAMBOO(0, MapColor.GREEN), THATCH(1, MapColor.YELLOW), CHUNK(2, MapColor.BLACK), PALM(3, MapColor.BROWN);

	private final int meta;
	private final MapColor mapColor;
	private static final TropicraftSlabs[] META_LOOKUP = new TropicraftSlabs[values().length];
	public static final TropicraftSlabs VALUES[] = values();

	private TropicraftSlabs(int meta, MapColor mapColor) {
		this.meta = meta;
		this.mapColor = mapColor;
	}
	
	public MapColor getMapColor() {
		return this.mapColor;
	}

	@Override
	public int getMeta() {
		return this.meta;
	}

	public static int getMetaByName(String name) {
		for (TropicraftSlabs slab : META_LOOKUP) {
			if (slab.getName().equals(name)) {
				return slab.getMeta();
			}
		}

		return -1;
	}

	public static TropicraftSlabs byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String getTypeName() {
	    return "slab";
	}

	static {
		for (TropicraftSlabs slab : values()) {
			META_LOOKUP[slab.getMeta()] = slab;
		}
	}
}
