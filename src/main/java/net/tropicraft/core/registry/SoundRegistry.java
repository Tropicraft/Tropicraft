package net.tropicraft.core.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.tropicraft.Info;

public class SoundRegistry {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface SoundName {
        String value();
    }
    
	private static HashMap<String, SoundEvent> lookupStringToEvent = new HashMap<String, SoundEvent>();

	public static void init() {
        for (Field f : SoundRegistry.class.getDeclaredFields()) {
            if (f.isAnnotationPresent(SoundName.class)) {
                try {
                    f.set(null, register(f.getAnnotation(SoundName.class).value()));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
//	    
//	    
//		register(registry, "headlaughing");
//		register(registry, "headattack");
//		register(registry, "headshort");
//		register(registry, "headmed");
//		register(registry, "headpain");
//		register(registry, "headdeath");
//		// records
//		//register(registry, "buried_treasure");
//		register(registry, "eastern_isles");
//		register(registry, "low_tide");
//		register(registry, "summering");
//		register(registry, "the_tribe");
//		register(registry, "trade_winds");
//		
//		register(registry, "pageflip");
//
//		register(registry, "iggyattack");
//		register(registry, "iggydeath");
//		register(registry, "iggyliving");
//		
//		register(registry, "ashen_laugh");
//		// bongos
//		register(registry, "bongo.low");
//		register(registry, "bongo.medium");
//		register(registry, "bongo.high");
//		register(registry, "dolphin");
	}

	public static SoundEvent register(String soundPath) {
		ResourceLocation resLoc = new ResourceLocation(Info.MODID, soundPath);
		SoundEvent event = new SoundEvent(resLoc);
		ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(resLoc));
		if (lookupStringToEvent.containsKey(soundPath)) {
			System.out.println("TCWARNING: duplicate sound registration for " + soundPath);
		}
		lookupStringToEvent.put(soundPath, event);
		
		return event;
	}

	public static SoundEvent get(String soundPath) {
		return lookupStringToEvent.get(soundPath);
	}

}
