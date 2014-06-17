package net.tropicraft.registry;

import net.minecraft.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.pool.EntityPoolFloat;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import cpw.mods.fml.common.registry.EntityRegistry;

public class TCEntityRegistry {

	private static int entityID = 0;
	
	public static void init() {
		registerEntity(EntityChair.class, "beachChair", 120, 10, true);
		registerEntity(EntityUmbrella.class, "beachUmbrella", 120, 10, false);
		registerEntity(EntityPoolFloat.class, "poolFloat", 120, 10, false);
		registerEntity(EntitySeahorse.class, "seahorse", 120, 5, true);
		registerEntity(EntityDart.class, "Dart", 120, 4, true);
	}
	
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, entityID++, Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

}
