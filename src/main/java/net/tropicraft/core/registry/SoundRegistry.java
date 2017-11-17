package net.tropicraft.core.registry;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.tropicraft.Info;

@Mod.EventBusSubscriber
public class SoundRegistry {

	private static HashMap<String, SoundEvent> lookupStringToEvent = new HashMap<String, SoundEvent>();

	@SubscribeEvent
	public static void init(RegistryEvent.Register<SoundEvent> event) {
	    IForgeRegistry<SoundEvent> registry = event.getRegistry();
		register(registry, "headlaughing");
		register(registry, "headattack");
		register(registry, "headshort");
		register(registry, "headmed");
		register(registry, "headpain");
		register(registry, "headdeath");
		// records
		register(registry, "buried_treasure");
		register(registry, "eastern_isles");
		register(registry, "low_tide");
		register(registry, "summering");
		register(registry, "the_tribe");
		register(registry, "trade_winds");
		
		register(registry, "pageFlip");

		register(registry, "iggyattack");
		register(registry, "iggydeath");
		register(registry, "iggyliving");
		
		register(registry, "ashenLaugh");
		// bongos
		register(registry, "bongo.low");
		register(registry, "bongo.medium");
		register(registry, "bongo.high");
		register(registry, "dolphin");
	}

	public static void register(IForgeRegistry<SoundEvent> registry, String soundPath) {
		ResourceLocation resLoc = new ResourceLocation(Info.MODID, soundPath);
		SoundEvent event = new SoundEvent(resLoc);
		registry.register(event.setRegistryName(resLoc));
		if (lookupStringToEvent.containsKey(soundPath)) {
			System.out.println("TCWARNING: duplicate sound registration for " + soundPath);
		}
		lookupStringToEvent.put(soundPath, event);
	}

	public static SoundEvent get(String soundPath) {
		return lookupStringToEvent.get(soundPath);
	}

}
