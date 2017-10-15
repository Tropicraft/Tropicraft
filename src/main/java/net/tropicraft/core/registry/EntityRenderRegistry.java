package net.tropicraft.core.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.tropicraft.core.client.entity.model.ModelAshen;
import net.tropicraft.core.client.entity.model.ModelDolphin;
import net.tropicraft.core.client.entity.model.ModelKoaMan;
import net.tropicraft.core.client.entity.model.ModelManOWar;
import net.tropicraft.core.client.entity.model.ModelMarlin;
import net.tropicraft.core.client.entity.model.ModelSeaTurtle;
import net.tropicraft.core.client.entity.model.ModelSeahorse;
import net.tropicraft.core.client.entity.model.ModelTurtleEgg;
import net.tropicraft.core.client.entity.model.ModelScubaGear;
import net.tropicraft.core.client.entity.model.ModelSeahorse;
import net.tropicraft.core.client.entity.render.RenderAshen;
import net.tropicraft.core.client.entity.render.RenderBambooItemFrame;
import net.tropicraft.core.client.entity.render.RenderChair;
import net.tropicraft.core.client.entity.render.RenderDolphin;
import net.tropicraft.core.client.entity.render.RenderEIH;
import net.tropicraft.core.client.entity.render.RenderEagleRay;
import net.tropicraft.core.client.entity.render.RenderEchinodermEgg;
import net.tropicraft.core.client.entity.render.RenderFailgull;
import net.tropicraft.core.client.entity.render.RenderIguana;
import net.tropicraft.core.client.entity.render.RenderKoaMan;
import net.tropicraft.core.client.entity.render.RenderLavaBall;
import net.tropicraft.core.client.entity.render.RenderLostMask;
import net.tropicraft.core.client.entity.render.RenderManOWar;
import net.tropicraft.core.client.entity.render.RenderMarlin;
import net.tropicraft.core.client.entity.render.RenderSeaTurtle;
import net.tropicraft.core.client.entity.render.RenderSeaUrchin;
import net.tropicraft.core.client.entity.render.RenderSeahorse;
import net.tropicraft.core.client.entity.render.RenderStarfish;
import net.tropicraft.core.client.entity.render.RenderTreeFrog;
import net.tropicraft.core.client.entity.render.RenderTropiCreeper;
import net.tropicraft.core.client.entity.render.RenderTropiSkeleton;
import net.tropicraft.core.client.entity.render.RenderTropicalFish;
import net.tropicraft.core.client.entity.render.RenderTurtleEgg;
import net.tropicraft.core.client.entity.render.RenderUmbrella;
import net.tropicraft.core.client.entity.render.RenderVMonkey;
import net.tropicraft.core.client.entity.render.RenderWallItem;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityIguana;
import net.tropicraft.core.common.entity.hostile.EntityLostMask;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBase;
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
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;

public class EntityRenderRegistry {

    public static ModelScubaGear chestModel;
    public static ModelScubaGear legsModel;
    public static ModelScubaGear feetModel;
    public static ModelScubaGear headModel;

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

    public static void init() {
        registerEntityRender(EntityEIH.class, new RenderEIH());
        registerEntityRender(EntityTropiCreeper.class, new RenderTropiCreeper());
        registerEntityRender(EntityIguana.class, new RenderIguana());
        registerEntityRender(EntityTreeFrogBase.class, new RenderTreeFrog());
        registerEntityRender(EntityTropiSkeleton.class, new RenderTropiSkeleton());
        registerEntityRender(EntityVMonkey.class, new RenderVMonkey());
        registerEntityRender(EntityMarlin.class, new RenderMarlin(new ModelMarlin(), 0.25F));
        registerEntityRender(EntityLavaBall.class, new RenderLavaBall());
        registerEntityRender(EntitySeahorse.class, new RenderSeahorse(new ModelSeahorse(), 0.25F));
        registerEntityRender(EntityFailgull.class, new RenderFailgull(0.25F));
        registerEntityRender(EntityChair.class, new RenderChair());
        registerEntityRender(EntityUmbrella.class, new RenderUmbrella());
        registerEntityRender(EntityCoconutGrenade.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
                ItemRegistry.coconutBomb,
                Minecraft.getMinecraft().getRenderItem()));
        registerEntityRender(EntityManOWar.class, new RenderManOWar(new ModelManOWar(32, 20, true), 0.35F));
        registerEntityRender(EntityEagleRay.class, new RenderEagleRay());
        registerEntityRender(EntitySeaUrchin.class, new RenderSeaUrchin());
        registerEntityRender(EntitySeaUrchinEgg.class, new RenderEchinodermEgg());
        registerEntityRender(EntityStarfish.class, new RenderStarfish());
        registerEntityRender(EntityStarfishEgg.class, new RenderEchinodermEgg());
        registerEntityRender(EntityBambooItemFrame.class, new RenderBambooItemFrame());
        registerEntityRender(EntityWallItem.class, new RenderWallItem());
        registerEntityRender(EntityKoaHunter.class, new RenderKoaMan(Minecraft.getMinecraft().getRenderManager(), new ModelKoaMan(), 0.5F));
        //registerEntityRender(EntityWallStarfish.class, new RenderWallStarfish());
        registerEntityRender(EntitySeaTurtle.class, new RenderSeaTurtle(new ModelSeaTurtle(), 0.75F));
        registerEntityRender(EntityTurtleEgg.class, new RenderTurtleEgg(new ModelTurtleEgg(), 0.75F));
        
        chestModel = new ModelScubaGear(0, EntityEquipmentSlot.CHEST);
        feetModel = new ModelScubaGear(0, EntityEquipmentSlot.FEET);
        legsModel = new ModelScubaGear(0, EntityEquipmentSlot.LEGS);
        headModel = new ModelScubaGear(0, EntityEquipmentSlot.HEAD);

        registerEntityRender(EntityTropicalFish.class, new RenderTropicalFish());
        registerEntityRender(EntityPiranha.class, new RenderTropicalFish());
        registerEntityRender(EntityRiverSardine.class, new RenderTropicalFish());
        registerEntityRender(EntityDolphin.class, new RenderDolphin(new ModelDolphin(), 0.25F));
    }

    private static void registerEntityRender(Class<? extends Entity> entityClass, Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }

}
