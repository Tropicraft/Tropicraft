package net.tropicraft.core.common.enums;

import net.minecraft.block.material.MapColor;

public enum TropicraftSlabs implements ITropicraftVariant {
	BAMBOO(0, MapColor.GREEN, BlockHardnessValues.BAMBOO.hardness, BlockHardnessValues.BAMBOO.resistance),
	THATCH(1, MapColor.YELLOW, BlockHardnessValues.THATCH.hardness, BlockHardnessValues.THATCH.resistance),
	CHUNK(2, MapColor.BLACK, BlockHardnessValues.CHUNK.hardness, BlockHardnessValues.CHUNK.resistance),
	PALM(3, MapColor.BROWN, BlockHardnessValues.PALM.hardness, BlockHardnessValues.PALM.resistance);

    private final float hardness;
    private final float resistance;
	private final int meta;
	private final MapColor mapColor;
	private static final TropicraftSlabs[] META_LOOKUP = new TropicraftSlabs[values().length];
	public static final TropicraftSlabs VALUES[] = values();

	private TropicraftSlabs(int meta, MapColor mapColor, float hardness, float resistance) {
		this.meta = meta;
		this.mapColor = mapColor;
		this.hardness = hardness;
		this.resistance = resistance;
	}

	public float getHardness() {
	    return this.hardness;
	}

	public float getResistance() {
	    return this.getResistance();
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
