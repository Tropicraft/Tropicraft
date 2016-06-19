package net.tropicraft.core.registry;

import net.minecraft.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogGreen;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.core.common.entity.hostile.EntityTropiCreeper;
import net.tropicraft.core.common.entity.passive.EntityIguana;

public class EntityRegistry {

	private static int entityID = 0;

	public static void init() {
		registerEntity(EntityEIH.class, "Easter Island Head", 80, 3, true);
		registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);
		registerEntity(EntityIguana.class, "Iguana", 80, 3, true);
		registerEntity(EntityTreeFrogGreen.class, "TreeFrogGreen", 80, 3, true);
		registerEntity(EntityTreeFrogRed.class, "TreeFrogRed", 80, 3, true);
		registerEntity(EntityTreeFrogBlue.class, "TreeFrogBlue", 80, 3, true);
		registerEntity(EntityTreeFrogYellow.class, "TreeFrogYellow", 80, 3, true);
	}
	
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(entityClass, entityName, entityID++, Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		
		//egg for testing
		net.minecraftforge.fml.common.registry.EntityRegistry.registerEgg(entityClass, 0xFFFFFF, 0x000000);
	}
	
}
