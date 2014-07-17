package net.tropicraft.registry;

import net.minecraft.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.passive.EntityTreeFrogGreen;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.pool.EntityPoolFloat;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.entity.underdasea.EntityEagleRay;
import net.tropicraft.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import net.tropicraft.entity.underdasea.EntityTurtleEgg;
import cpw.mods.fml.common.registry.EntityRegistry;

public class TCEntityRegistry {

	private static int entityID = 0;
	
	public static void init() {
		registerEntity(EntityChair.class, "beachChair", 120, 10, true);
		registerEntity(EntityUmbrella.class, "beachUmbrella", 120, 10, false);
		registerEntity(EntityPoolFloat.class, "poolFloat", 120, 10, false);
		registerEntity(EntitySeahorse.class, "Seahorse", 120, 3, true);
		registerEntity(EntityDart.class, "Dart", 120, 4, true);
		registerEntity(EntityIguana.class, "Iguana", 80, 3, true);
		registerEntity(EntityEIH.class, "EIH", 80, 3, true);
		registerEntity(EntityTreeFrogGreen.class, "TreeFrogGreen", 80, 3, true);
		registerEntity(EntityTreeFrogRed.class, "TreeFrogRed", 80, 3, true);
		registerEntity(EntityTreeFrogBlue.class, "TreeFrogBlue", 80, 3, true);
		registerEntity(EntityTreeFrogYellow.class, "TreeFrogYellow", 80, 3, true);
		registerEntity(EntityPoisonBlot.class, "PoisonBlot", 120, 2, true);
		registerEntity(EntitySeaTurtle.class, "SeaTurtle", 80, 3, true);
		registerEntity(EntityTurtleEgg.class, "SeaTurtleEgg", 80, 5, false);
		registerEntity(EntityEagleRay.class, "EagleRay", 80, 1, true);
	}
	
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, entityID++, Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

}
