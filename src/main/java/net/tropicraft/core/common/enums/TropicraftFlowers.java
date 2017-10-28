package net.tropicraft.core.common.enums;

import net.minecraft.util.math.AxisAlignedBB;

public enum TropicraftFlowers implements ITropicraftVariant {

	COMMELINA_DIFFUSA,
	CROCOSMIA,
	ORCHID,
	CANNA,
	ANEMONE(9),
	ORANGE_ANTHURIUM(11),
	RED_ANTHURIUM(11),
	MAGIC_MUSHROOM(11),
	PATHOS(15, 12),
	ACAI_VINE(7, 16),
	CROTON(13),
	DRACAENA(13),
	FERN(13),
	FOLIAGE(13),
	BROMELIAD(9);

	public static final TropicraftFlowers VALUES[] = values();

	private final AxisAlignedBB bounds;

	private TropicraftFlowers() {
		this(7);
	}

	private TropicraftFlowers(int w) {
		this(w, 15);
	}

	private TropicraftFlowers(int w, int h) {
		float halfW = (w / 16f) / 2;
		this.bounds = new AxisAlignedBB(0.5 - halfW, 0, 0.5 - halfW, 0.5 + halfW, h / 16f, 0.5 + halfW);
	}

	public AxisAlignedBB getBounds() {
		return bounds;
	}
	
	@Override
	public String getTypeName() {
	    return "flower";
	}
}
