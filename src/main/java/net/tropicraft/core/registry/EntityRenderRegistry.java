package net.tropicraft.core.registry;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.tropicraft.core.client.entity.model.*;
import net.tropicraft.core.client.entity.render.RenderAshen;
import net.tropicraft.core.client.entity.render.RenderBambooItemFrame;
import net.tropicraft.core.client.entity.render.RenderBeachFloat;
import net.tropicraft.core.client.entity.render.RenderChair;
import net.tropicraft.core.client.entity.render.RenderDolphin;
import net.tropicraft.core.client.entity.render.RenderEIH;
import net.tropicraft.core.client.entity.render.RenderEagleRay;
import net.tropicraft.core.client.entity.render.RenderEgg;
import net.tropicraft.core.client.entity.render.RenderFailgull;
import net.tropicraft.core.client.entity.render.RenderFishingLure;
import net.tropicraft.core.client.entity.render.RenderIguana;
import net.tropicraft.core.client.entity.render.RenderKoaMan;
import net.tropicraft.core.client.entity.render.RenderLavaBall;
import net.tropicraft.core.client.entity.render.RenderLostMask;
import net.tropicraft.core.client.entity.render.RenderLure;
import net.tropicraft.core.client.entity.render.RenderManOWar;
import net.tropicraft.core.client.entity.render.RenderMarlin;
import net.tropicraft.core.client.entity.render.RenderPoisonBlot;
import net.tropicraft.core.client.entity.render.RenderSeaTurtle;
import net.tropicraft.core.client.entity.render.RenderSeaUrchin;
import net.tropicraft.core.client.entity.render.RenderSeahorse;
import net.tropicraft.core.client.entity.render.RenderShark;
import net.tropicraft.core.client.entity.render.RenderStarfish;
import net.tropicraft.core.client.entity.render.RenderTreeFrog;
import net.tropicraft.core.client.entity.render.RenderTropiCreeper;
import net.tropicraft.core.client.entity.render.RenderTropiSkeleton;
import net.tropicraft.core.client.entity.render.RenderTropiSpider;
import net.tropicraft.core.client.entity.render.RenderTropicalFish;
import net.tropicraft.core.client.entity.render.RenderUmbrella;
import net.tropicraft.core.client.entity.render.RenderVMonkey;
import net.tropicraft.core.client.entity.render.RenderWallItem;
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
import net.tropicraft.core.common.entity.placeable.EntityBeachFloat;
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

public class EntityRenderRegistry {

    public static ModelScubaGear chestModel;
    public static ModelScubaGear legsModel;
    public static ModelScubaGear feetModel;
    public static ModelScubaGear headModel;
    
    private static ArrayList<Class<? extends Entity>> entityRenderClasses = new ArrayList<Class<? extends Entity>>();

    public static void init() {
        registerEntityRender(EntityEIH.class, new RenderEIH());
        registerEntityRender(EntityTropiCreeper.class, new RenderTropiCreeper());
        registerEntityRender(EntityIguana.class, new RenderIguana());
        registerEntityRender(EntityTreeFrog.class, new RenderTreeFrog());
        registerEntityRender(EntityTropiSkeleton.class, new RenderTropiSkeleton());
        registerEntityRender(EntityVMonkey.class, new RenderVMonkey(new ModelVMonkey()));
        registerEntityRender(EntityMarlin.class, new RenderMarlin(new ModelMarlin(), 0.25F));
        registerEntityRender(EntityLavaBall.class, new RenderLavaBall());
        registerEntityRender(EntitySeahorse.class, new RenderSeahorse(new ModelSeahorse(), 0.25F));
        registerEntityRender(EntityFailgull.class, new RenderFailgull(0.25F));
        registerEntityRender(EntityChair.class, new RenderChair());
        registerEntityRender(EntityUmbrella.class, new RenderUmbrella());
        registerEntityRender(EntityBeachFloat.class, new RenderBeachFloat());
        registerEntityRender(EntityCoconutGrenade.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
                ItemRegistry.coconutBomb,
                Minecraft.getMinecraft().getRenderItem()));
        registerEntityRender(EntityManOWar.class, new RenderManOWar(new ModelManOWar(32, 20, true), 0.35F));
        registerEntityRender(EntityEagleRay.class, new RenderEagleRay());
        registerEntityRender(EntitySeaUrchin.class, new RenderSeaUrchin());
        registerEntityRender(EntitySeaUrchinEgg.class, new RenderEgg());
        registerEntityRender(EntityStarfish.class, new RenderStarfish());
        registerEntityRender(EntityStarfishEgg.class, new RenderEgg());
        registerEntityRender(EntityBambooItemFrame.class, new RenderBambooItemFrame());
        registerEntityRender(EntityWallItem.class, new RenderWallItem());
        registerEntityRender(EntityKoaHunter.class, new RenderKoaMan(Minecraft.getMinecraft().getRenderManager(), new ModelKoaMan(), 0.5F));
        //registerEntityRender(EntityWallStarfish.class, new RenderWallStarfish());
        registerEntityRender(EntitySeaTurtle.class, new RenderSeaTurtle(new ModelSeaTurtle(), 0.75F));
        registerEntityRender(EntitySeaTurtleEgg.class, new RenderEgg());
        registerEntityRender(EntityLostMask.class, new RenderLostMask());
        registerEntityRender(EntityAshenHunter.class, new RenderAshen(new ModelAshen(), 0.75F));

        
        chestModel = new ModelScubaGear(0, EntityEquipmentSlot.CHEST);
        feetModel = new ModelScubaGear(0, EntityEquipmentSlot.FEET);
        legsModel = new ModelScubaGear(0, EntityEquipmentSlot.LEGS);
        headModel = new ModelScubaGear(0, EntityEquipmentSlot.HEAD);

        registerEntityRender(EntityTropicalFish.class, new RenderTropicalFish());
        registerEntityRender(EntityPiranha.class, new RenderTropicalFish());
        registerEntityRender(EntityRiverSardine.class, new RenderTropicalFish());
        registerEntityRender(EntityDolphin.class, new RenderDolphin(new ModelDolphin(), 0.25F));
        registerEntityRender(EntityShark.class, new RenderShark(new ModelHammerheadShark(), 0.25F));
        registerEntityRender(EntityFishHook.class, new RenderFishingLure(Minecraft.getMinecraft().getRenderManager()));
        registerEntityRender(EntityHook.class, new RenderLure(Minecraft.getMinecraft().getRenderManager()));

        registerEntityRender(EntityTropiSpider.class, new RenderTropiSpider());
        registerEntityRender(EntityTropiSpiderEgg.class, new RenderEgg());
        
        registerEntityRender(EntityPoisonBlot.class, new RenderPoisonBlot());
    }
    
    public static ModelScubaGear getScubaModel(EntityEquipmentSlot slot) {
        switch (slot) {
        case CHEST:
            return chestModel;
        case LEGS:
            return legsModel;
        case FEET:
            return feetModel;
        case HEAD:
            return headModel;
        default:
            return null;
        }
    }

    private static void registerEntityRender(Class<? extends Entity> entityClass, Render render) {
    		if(entityRenderClasses.contains(entityClass)) {
    			System.err.println("Attempted to register an entity renderer twice for entity class '"+entityClass.getSimpleName()+"'");
    			return;
    		}
    		entityRenderClasses.add(entityClass);
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }
}
