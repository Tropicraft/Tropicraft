package net.tropicraft.core.registry;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityIguana;
import net.tropicraft.core.common.entity.hostile.EntityLostMask;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogGreen;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.core.common.entity.hostile.EntityTropiCreeper;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.core.common.entity.passive.EntityFailgull;
import net.tropicraft.core.common.entity.passive.EntityFishHook;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.entity.placeable.EntityBambooItemFrame;
import net.tropicraft.core.common.entity.placeable.EntityChair;
import net.tropicraft.core.common.entity.placeable.EntityUmbrella;
import net.tropicraft.core.common.entity.placeable.EntityWallItem;
import net.tropicraft.core.common.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.core.common.entity.projectile.EntityPoisonBlot;
import net.tropicraft.core.common.entity.underdasea.EntityManOWar;
import net.tropicraft.core.common.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchin;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchinEgg;
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;
import net.tropicraft.core.common.entity.underdasea.EntityStarfishEgg;
import net.tropicraft.core.common.entity.underdasea.EntityTurtleEgg;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityDolphin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityEagleRay;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityPiranha;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityRiverSardine;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntitySeahorse;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityShark;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;

public class EntityRegistry {

	private static int entityID = 0;
	private static ArrayList<String> registeredEntityNames = new ArrayList<String>();
	private static ArrayList<Class<? extends Entity>> registeredEntityClasses = new ArrayList<Class<? extends Entity>>();

	public static void init() {
		registerEntity(EntityEIH.class, "eih", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityTropiCreeper.class, "tropicreeper", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityIguana.class, "iguana", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityTreeFrogGreen.class, "greenfrog", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityTreeFrogRed.class, "redfrog", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityTreeFrogBlue.class, "bluefrog", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityTreeFrogYellow.class, "yellowfrog", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityTropiSkeleton.class, "tropiskelly", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityVMonkey.class, "monkey", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityPoisonBlot.class, "PoisonBlot", 32, 1, true);
		registerEntity(EntityLavaBall.class, "Lava Ball", 120, 4, true);
		registerEntity(EntityFailgull.class, "failgull", 80, 3, true, SpawnPlacementType.IN_AIR);
		registerEntity(EntityChair.class, "beachChair", 120, 10, true);
		registerEntity(EntityUmbrella.class, "beachUmbrella", 120, 10, false);
		registerEntity(EntityCoconutGrenade.class, "CoconutBomb", 120, 5, true);
		registerEntity(EntityAshenHunter.class, "ashen", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(EntityLostMask.class, "LostMask", 64, 3, true);
		registerEntity(EntityManOWar.class, "mow", 64, 3, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntitySeaUrchin.class, "seaurchin", 64, 3, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntitySeaUrchinEgg.class, "SeaUrchinEgg", 64, 3, false);
		registerEntity(EntityStarfish.class, "starfish", 64, 3, false, SpawnPlacementType.IN_WATER);
		registerEntity(EntityStarfishEgg.class, "StarfishEgg", 64, 3, false);
		registerEntity(EntityBambooItemFrame.class, "TCItemFrame", 64, 10, false);
		registerEntity(EntityWallItem.class, "WallItem", 64, 10, false);
		registerEntity(EntityKoaHunter.class, "koa", 64, 3, true);
		registerEntity(EntitySeaTurtle.class, "turtle", 80, 3, true);
		registerEntity(EntityTurtleEgg.class, "SeaTurtleEgg", 80, 5, false);
		registerEntity(EntityTropicalFish.class, "fish", 80, 2, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntitySeahorse.class, "seahorse", 80, 2, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityEagleRay.class, "eagleray", 80, 2, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityMarlin.class, "marlin", 80, 2, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityPiranha.class, "piranha", 80, 2, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityRiverSardine.class, "sardine", 80, 2, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityDolphin.class, "dolphin", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityShark.class, "hammerhead", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(EntityFishHook.class, "tropifishhook", 80, 1, true);
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates) {
		if (registeredEntityNames.contains(entityName) || registeredEntityClasses.contains(entityClass)) {
			notifyDuplicate(entityClass, entityName);
			return;
		}
		net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(entityClass, entityName, entityID++,
				Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		registeredEntityNames.add(entityName);
		registeredEntityClasses.add(entityClass);
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates, SpawnPlacementType spawnPlacementType) {
		if (registeredEntityNames.contains(entityName)) {
			notifyDuplicate(entityClass, entityName);
			return;
		}
		registerEntity(entityClass, entityName, trackingRange, updateFrequency, sendsVelocityUpdates);
		EntitySpawnPlacementRegistry.setPlacementType(entityClass, spawnPlacementType);
		registeredEntityNames.add(entityName);
		registeredEntityClasses.add(entityClass);
	}

	private static void notifyDuplicate(Class<? extends Entity> clazz, String name) {
		System.err.println("Attempted to register duplicate entity: " + name + " : " + clazz.getSimpleName());
	}

}