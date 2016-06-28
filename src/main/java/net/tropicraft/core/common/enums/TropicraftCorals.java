package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftCorals implements IStringSerializable {

	PINK(0), TEALY(1), BRAIN(2), FIRE(3), GREEN(4), SPIRAL(5), HOTPINK(6);
	
	private final int meta;
	private static final TropicraftCorals[] META_LOOKUP = new TropicraftCorals[values().length];
	public static final TropicraftCorals VALUES[] = values();
	
	private TropicraftCorals(int meta) {
		this.meta = meta;
	}
	
	public int getMetadata() {
		return this.meta;
	}

	public static TropicraftCorals byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}
	
    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_coral";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
	// Set META_LOOKUP table
	static {
		for (TropicraftCorals coral : VALUES) {
			META_LOOKUP[coral.getMetadata()] = coral;
		}
	}
}
