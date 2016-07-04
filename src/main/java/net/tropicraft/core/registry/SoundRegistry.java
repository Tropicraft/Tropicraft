package net.tropicraft.core.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Info;

public class SoundRegistry {

	public static SoundEvent EIH_LAUGH;
	
	public static void init() {
		EIH_LAUGH = register("headlaughing");
	}
	
	public static SoundEvent register(String soundPath) {
		ResourceLocation resLoc = new ResourceLocation(Info.MODID, soundPath);
		SoundEvent event = new SoundEvent(resLoc);
		GameRegistry.register(event, resLoc);
		return event;
	}
	
}
