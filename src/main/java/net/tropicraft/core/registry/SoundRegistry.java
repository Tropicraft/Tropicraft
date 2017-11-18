package net.tropicraft.core.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tropicraft.Info;
import net.tropicraft.core.common.sound.TropicraftSounds;

public class SoundRegistry {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface SoundName {
        String value();
    }
    
    /**
     * Maintains a list of registered sounds so we can check for duplicate registrations
     */
	private static List<String> registeredSounds = new ArrayList<String>();

	/**
	 * See TropicraftSounds for information on how to register a new sound
	 */
	public static void init() {
        for (Field f : TropicraftSounds.class.getDeclaredFields()) {
            if (f.isAnnotationPresent(SoundName.class)) {
                try {
                    f.set(null, register(f.getAnnotation(SoundName.class).value()));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	private static SoundEvent register(String soundPath) {
		ResourceLocation resLoc = new ResourceLocation(Info.MODID, soundPath);
		SoundEvent event = new SoundEvent(resLoc);
		ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(resLoc));
		if (registeredSounds.contains(soundPath)) {
			System.out.println("TCWARNING: duplicate sound registration for " + soundPath);
		}
		registeredSounds.add(soundPath);
		
		return event;
	}

}
