package net.tropicraft.core.registry;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Info;

public class SoundRegistry {

	private static HashMap<String, SoundEvent> lookupStringToEvent = new HashMap<String, SoundEvent>();

	public static void init() {
		register("headlaughing");
		register("headattack");
		register("headshort");
		register("headmed");
		register("headpain");
		register("headdeath");
		// records
		register("buried_treasure");
		register("eastern_isles");
		register("low_tide");
		register("summering");
		register("the_tribe");
		register("trade_winds");
		
		register("pageFlip");

		register("iggyattack");
		register("iggydeath");
		register("iggyliving");
		
		register("ashenLaugh");
		// bongos
		register("bongo.low");
		register("bongo.medium");
		register("bongo.high");
		register("dolphin");
	}

	public static void register(String soundPath) {
		ResourceLocation resLoc = new ResourceLocation(Info.MODID, soundPath);
		SoundEvent event = new SoundEvent(resLoc);
		GameRegistry.register(event, resLoc);
		if (lookupStringToEvent.containsKey(soundPath)) {
			System.out.println("TCWARNING: duplicate sound registration for " + soundPath);
		}
		lookupStringToEvent.put(soundPath, event);
	}

	public static SoundEvent get(String soundPath) {
		return lookupStringToEvent.get(soundPath);
	}

}
