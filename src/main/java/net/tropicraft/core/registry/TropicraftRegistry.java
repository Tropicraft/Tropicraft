package net.tropicraft.core.registry;

import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;

public abstract class TropicraftRegistry {

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Info.MODID, name);
	}
	
	public static String getNamePrefixed(String name) {
		return Info.MODID + ":" + name;
	}
}
