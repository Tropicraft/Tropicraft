package net.tropicraft.registry;

import net.minecraft.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.entity.hostile.EntityAshenHunter;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.entity.hostile.EntityLostMask;
import net.tropicraft.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.entity.hostile.EntityTreeFrogYellow;
import net.tropicraft.entity.hostile.EntityTropiCreeper;
import net.tropicraft.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.entity.hostile.SpiderAdult;
import net.tropicraft.entity.hostile.SpiderChild;
import net.tropicraft.entity.hostile.SpiderEgg;
import net.tropicraft.entity.koa.EntityKoaFisher;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.entity.koa.EntityKoaShaman;
import net.tropicraft.entity.koa.EntityKoaTrader;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.passive.EntityTreeFrogGreen;
import net.tropicraft.entity.passive.VMonkey;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntitySnareTrap;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.entity.projectile.EntityTropicraftLeafballNew;
import net.tropicraft.entity.underdasea.EntityEagleRay;
import net.tropicraft.entity.underdasea.EntityMarlin;
import net.tropicraft.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.entity.underdasea.EntityTurtleEgg;
import CoroUtil.entity.EntityTropicalFishHook;
import cpw.mods.fml.common.registry.EntityRegistry;

public class TCEntityRegistry {

	private static int entityID = 0;
	
	public static void init() {
		registerEntity(EntityChair.class, "beachChair", 120, 10, true);
		registerEntity(EntityUmbrella.class, "beachUmbrella", 120, 10, false);
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
		registerEntity(EntityAshenHunter.class, "AshenHunter", 80, 3, true);
		registerEntity(EntityCoconutGrenade.class, "CoconutBomb", 120, 5, true);
		registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);
		registerEntity(EntityTropiSkeleton.class, "TropiSkeleton", 80, 3, true);
		registerEntity(EntityTropiCreeper.class, "TropiCreeper", 80, 3, true);
		registerEntity(SpiderAdult.class, "SpiderAdult", 48, 3, true);
		registerEntity(SpiderChild.class, "SpiderChild", 48, 3, true);
		registerEntity(SpiderEgg.class, "SpiderEgg", 48, 3, true);
		registerEntity(VMonkey.class, "VMonkey", 64, 3, true);
		registerEntity(EntityKoaHunter.class, "KoaHunter", 64, 3, true);
		registerEntity(EntityKoaFisher.class, "KoaFisher", 64, 3, true);
		registerEntity(EntityKoaTrader.class, "KoaTrader", 64, 3, true);
		registerEntity(EntityKoaShaman.class, "KoaShaman", 64, 3, true);
		registerEntity(EntityTropicalFishHook.class, "TropiFishHook", 64, 1, true);
		registerEntity(EntityTCItemFrame.class, "TCItemFrame", 64, 10, false);
		registerEntity(EntityTropicraftLeafballNew.class, "TropiLeafball", 64, 1, true);
		registerEntity(EntityTropicalFish.class, "TropicalFish", 80, 3, true);
		registerEntity(EntityLostMask.class, "LostMask", 64, 3, true);
		registerEntity(EntityMarlin.class, "Marlin", 80, 3, true);
		registerEntity(EntitySnareTrap.class, "SnareTrap", 80, 20, false);
		
		
	}
	
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, entityID++, Tropicraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

}
