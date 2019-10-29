package net.tropicraft.core.registry;

import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;

public abstract class TropicraftRegistry {

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Constants.MODID, name);
	}
	
	public static String getNamePrefixed(String name) {
		return Constants.MODID + "." + name;
	}
	
	public static String getEntityNamePrefixed(String name) {
	    return Constants.MODID + ":" + name;
	}
}
