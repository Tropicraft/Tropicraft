package net.tropicraft.core.common.enums;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public enum TropicraftSlabs implements IStringSerializable {
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

	public int getMetadata() {
		return this.meta;
	}

	public static int getMetaByName(String name) {
		for (TropicraftSlabs slab : META_LOOKUP) {
			if (slab.getName().equals(name)) {
				return slab.getMetadata();
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
	public String getName() {
		return this.name().toLowerCase() + "_slab";
	}

	@Override
	public String toString() {
		return this.getName();
	}

	static {
		for (TropicraftSlabs slab : values()) {
			META_LOOKUP[slab.getMetadata()] = slab;
		}
	}
}
