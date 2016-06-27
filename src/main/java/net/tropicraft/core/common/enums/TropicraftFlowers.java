package net.tropicraft.core.common.enums;

import net.minecraft.util.IStringSerializable;

public enum TropicraftFlowers implements IStringSerializable {
	
	COMMELINA_DIFFUSA, CROCOSMIA, ORCHID, CANNA, ANEMONE, ORANGE_ANTHURIUM, RED_ANTHURIUM, MAGIC_MUSHROOM, PATHOS, ACAI_VINE, CROTON, DRACAENA, FERN, FOLIAGE, BROMELIAD;
	
	public static final TropicraftFlowers VALUES[] = values();
	
    @Override
    public String getName() {
    	return this.name().toLowerCase() + "_flower";
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
