package net.tropicraft.core.registry;

import net.minecraft.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityIguana;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogGreen;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.core.common.entity.hostile.EntityTropiCreeper;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.core.common.entity.passive.EntityFailgull;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.entity.placeable.EntityBambooItemFrame;
import net.tropicraft.core.common.entity.placeable.EntityChair;
import net.tropicraft.core.common.entity.placeable.EntityUmbrella;
import net.tropicraft.core.common.entity.placeable.EntityWallItem;
import net.tropicraft.core.common.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.core.common.entity.projectile.EntityPoisonBlot;
import net.tropicraft.core.common.entity.underdasea.EntityManOWar;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchin;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchinEgg;
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;
import net.tropicraft.core.common.entity.underdasea.EntityStarfishEgg;
import net.tropicraft.core.common.entity.underdasea.EntityTropicalFish;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityEagleRay;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntitySeahorse;

public class EntityRegistry {

	private static int entityID = 0;

	public static void init() {
		registerEntity(EntityEIH.class, "eih", 80, 3, true);
		registerEntity(EntityTropiCreeper.class, "tropicreeper", 80, 3, true);
		registerEntity(EntityIguana.class, "iguana", 80, 3, true);
		registerEntity(EntityTreeFrogGreen.class, "greenfrog", 80, 3, true);
		registerEntity(EntityTreeFrogRed.class, "redfrog", 80, 3, true);
		registerEntity(EntityTreeFrogBlue.class, "bluefrog", 80, 3, true);
		registerEntity(EntityTreeFrogYellow.class, "yellowfrog", 80, 3, true);
		registerEntity(EntityTropiSkeleton.class, "tropiskelly", 80, 3, true);
		registerEntity(EntityVMonkey.class, "monkey", 80, 3, true);
		registerEntity(EntityPoisonBlot.class, "PoisonBlot", 32, 1, true);
		registerEntity(EntityMarlin.class, "marlin", 80, 1, true);
		registerEntity(EntityLavaBall.class, "Lava Ball", 120, 4, true);
		registerEntity(EntitySeahorse.class, "seahorse", 80, 3, true);
		registerEntity(EntityFailgull.class, "failgull", 80, 3, true);
		registerEntity(EntityChair.class, "beachChair", 120, 10, true);
		registerEntity(EntityUmbrella.class, "beachUmbrella", 120, 10, false);
		registerEntity(EntityCoconutGrenade.class, "CoconutBomb", 120, 5, true);
		registerEntity(EntityTropicalFish.class, "fish", 80, 3, true);
		registerEntity(EntityManOWar.class, "mow", 64, 3, true);
		registerEntity(EntityEagleRay.class, "eagleray", 80, 1, true);
		registerEntity(EntitySeaUrchin.class, "seaurchin", 64, 3, true);
		registerEntity(EntitySeaUrchinEgg.class, "SeaUrchinEgg", 64, 3, false);
		registerEntity(EntityStarfish.class, "starfish", 64, 3, false);
		registerEntity(EntityStarfishEgg.class, "StarfishEgg", 64, 3, false);
		registerEntity(EntityBambooItemFrame.class, "TCItemFrame", 64, 10, false);
		registerEntity(EntityWallItem.class, "WallItem", 64, 10, false);
		registerEntity(EntityKoaHunter.class, "KoaHunter", 64, 3, true);
	}
	
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(entityClass, entityName, entityID++, Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
}
