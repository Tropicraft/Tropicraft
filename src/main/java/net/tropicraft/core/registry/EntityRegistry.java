package net.tropicraft.core.registry;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.entity.egg.EntitySeaTurtleEgg;
import net.tropicraft.core.common.entity.egg.EntitySeaUrchinEgg;
import net.tropicraft.core.common.entity.egg.EntityStarfishEgg;
import net.tropicraft.core.common.entity.egg.EntityTropiSpiderEgg;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityIguana;
import net.tropicraft.core.common.entity.hostile.EntityLostMask;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;
import net.tropicraft.core.common.entity.hostile.EntityTropiCreeper;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.core.common.entity.hostile.EntityTropiSpider;
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
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityDolphin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityEagleRay;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityHook;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityPiranha;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityRiverSardine;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntitySeahorse;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityShark;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;

@Mod.EventBusSubscriber
public class EntityRegistry {

	private static int entityID = 0;
	private static ArrayList<String> registeredEntityNames = new ArrayList<String>();
	private static ArrayList<Class<? extends Entity>> registeredEntityClasses = new ArrayList<Class<? extends Entity>>();

	@SubscribeEvent
	public static void init(RegistryEvent.Register<EntityEntry> event) {
	    IForgeRegistry<EntityEntry> registry = event.getRegistry();
		registerEntity(registry, EntityEIH.class, "eih", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityTropiCreeper.class, "tropicreeper", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityIguana.class, "iguana", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityTreeFrog.class, "treefrog", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityTropiSkeleton.class, "tropiskelly", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityVMonkey.class, "monkey", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityPoisonBlot.class, "poisonblot", 32, 1, true);
		registerEntity(registry, EntityLavaBall.class, "lavaball", 120, 4, true);
		registerEntity(registry, EntityFailgull.class, "failgull", 80, 3, true, SpawnPlacementType.IN_AIR);
		registerEntity(registry, EntityChair.class, "beach_chair", 120, 10, true);
		registerEntity(registry, EntityUmbrella.class, "beach_umbrella", 120, 10, false);
		registerEntity(registry, EntityCoconutGrenade.class, "coconut_bomb", 120, 5, true);
		registerEntity(registry, EntityAshenHunter.class, "ashen", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityLostMask.class, "lost_mask", 64, 3, true);
		registerEntity(registry, EntityManOWar.class, "mow", 64, 3, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntitySeaUrchin.class, "seaurchin", 64, 3, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntitySeaUrchinEgg.class, "seaurchinegg", 64, 3, false);
		registerEntity(registry, EntityStarfish.class, "starfish", 64, 3, false, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityStarfishEgg.class, "starfishegg", 64, 3, false);
		registerEntity(registry, EntityBambooItemFrame.class, "tc_item_frame", 64, 10, false);
		registerEntity(registry, EntityWallItem.class, "wall_item", 64, 10, false);
		registerEntity(registry, EntityKoaHunter.class, "koa", 64, 3, true);
		registerEntity(registry, EntitySeaTurtleEgg.class, "sea_turtle_egg", 80, 5, false);
		registerEntity(registry, EntitySeaTurtle.class, "turtle", 80, 1, true);
		registerEntity(registry, EntityTropicalFish.class, "fish", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntitySeahorse.class, "seahorse", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityEagleRay.class, "eagleray", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityMarlin.class, "marlin", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityPiranha.class, "piranha", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityRiverSardine.class, "sardine", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityDolphin.class, "dolphin", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityShark.class, "hammerhead", 80, 1, true, SpawnPlacementType.IN_WATER);
		registerEntity(registry, EntityFishHook.class, "tropifishhook", 80, 1, true);
		registerEntity(registry, EntityHook.class, "tropihook", 80, 1, true);
		registerEntity(registry, EntityTropiSpider.class, "tropispider", 80, 3, true, SpawnPlacementType.ON_GROUND);
		registerEntity(registry, EntityTropiSpiderEgg.class, "tropispideregg", 80, 5, false);
	}

	private static void registerEntity(IForgeRegistry<EntityEntry> registry, Class<? extends Entity> entityClass, String entityName, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates) {
		if (registeredEntityNames.contains(entityName) || registeredEntityClasses.contains(entityClass)) {
			notifyDuplicate(entityClass, entityName);
			return;
		}
		net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(Info.MODID + ":" + entityName), entityClass, Info.MODID + "." + entityName, entityID++,
				Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		registeredEntityNames.add(entityName);
		registeredEntityClasses.add(entityClass);
	}

	private static void registerEntity(IForgeRegistry<EntityEntry> registry, @Nonnull Class<? extends Entity> entityClass, String entityName, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates, @Nonnull SpawnPlacementType spawnPlacementType) {
		registerEntity(registry, entityClass, entityName, trackingRange, updateFrequency, sendsVelocityUpdates);
		EntitySpawnPlacementRegistry.setPlacementType(entityClass, spawnPlacementType);
	}

	private static void notifyDuplicate(Class<? extends Entity> clazz, String name) {
		System.err.println("Attempted to register duplicate entity: " + name + " : " + clazz.getSimpleName());
	}

}