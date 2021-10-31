package net.tropicraft.core.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.*;
import net.tropicraft.core.client.item.MaskArmorProvider;
import net.tropicraft.core.client.item.StacheArmorProvider;
import net.tropicraft.core.client.tileentity.BambooChestBlockEntityRenderer;
import net.tropicraft.core.client.tileentity.BambooChestRenderer;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.registry.TropicraftItems;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TropicraftRenderLayers {
    public static ModelLayerLocation KOA_HUNTER_LAYER;
    public static ModelLayerLocation TROPI_CREEPER_LAYER;
    public static ModelLayerLocation IGUANA_LAYER;
    public static ModelLayerLocation UMBRELLA_LAYER;
    public static ModelLayerLocation BEACH_FLOAT_LAYER;
    public static ModelLayerLocation CHAIR_LAYER;
    public static ModelLayerLocation TROPI_SKELLY_LAYER;
    public static ModelLayerLocation EIH_LAYER;
    public static ModelLayerLocation SEA_TURTLE_LAYER;
    public static ModelLayerLocation MARLIN_LAYER;
    public static ModelLayerLocation FAILGULL_LAYER;
    public static ModelLayerLocation DOLPHIN_LAYER;
    public static ModelLayerLocation SEAHORSE_LAYER;
    public static ModelLayerLocation TREE_FROG_LAYER;
    public static ModelLayerLocation SEA_URCHIN_LAYER;
    public static ModelLayerLocation SEA_URCHIN_EGG_ENTITY_LAYER;
    public static ModelLayerLocation STARFISH_EGG_LAYER;
    public static ModelLayerLocation V_MONKEY_LAYER;
    public static ModelLayerLocation PIRANHA_LAYER;
    public static ModelLayerLocation RIVER_SARDINE_LAYER;
    public static ModelLayerLocation TROPICAL_FISH_LAYER;
    public static ModelLayerLocation EAGLE_RAY_LAYER;
    public static ModelLayerLocation TROPI_SPIDER_EGG_LAYER;
    public static ModelLayerLocation ASHEN_LAYER;
    public static ModelLayerLocation HAMMERHEAD_LAYER;
    public static ModelLayerLocation SEA_TURTLE_EGG_LAYER;
    public static ModelLayerLocation TROPI_BEE_LAYER;
    public static ModelLayerLocation COWKTAIL_LAYER;
    public static ModelLayerLocation MAN_O_WAR_LAYER;
    public static ModelLayerLocation BAMBOO_MUG;
    public static ArrayList<ModelLayerLocation> ASHEN_MASK_LAYERS = new ArrayList<>();//= registerMain("mask", PlayerHeadpieceModel::getTexturedModelData);
    public static ModelLayerLocation STACHE_LAYER;
    public static ModelLayerLocation BAMBOO_CHEST;
    public static ModelLayerLocation BAMBOO_DOUBLE_CHEST_LEFT;
    public static ModelLayerLocation BAMBOO_DOUBLE_CHEST_RIGHT;
    public static ModelLayerLocation EIHMACHINE_LAYER;
    public static ModelLayerLocation AIRCOMPRESSOR_LAYER;

    public static void setupEntityModelLayers() {
        KOA_HUNTER_LAYER = registerMain("koa_hunter", () -> KoaModel.getTexturedModelData());
        TROPI_CREEPER_LAYER = registerMain("tropi_creeper", () -> TropiCreeperModel.getTexturedModelData());
        IGUANA_LAYER = registerMain("iguana", () -> IguanaModel.getTexturedModelData());
        UMBRELLA_LAYER = registerMain("umbrella", () -> UmbrellaModel.getTexturedModelData());
        BEACH_FLOAT_LAYER = registerMain("beach_float", () -> BeachFloatModel.getTexturedModelData());
        CHAIR_LAYER = registerMain("chair", () -> ChairModel.getTexturedModelData());
        TROPI_SKELLY_LAYER = registerMain("tropi_skelly", () -> TropiSkellyModel.getTexturedModelData());
        EIH_LAYER = registerMain("eih", () -> EIHModel.getTexturedModelData());
        SEA_TURTLE_LAYER = registerMain("sea_turtle", () -> SeaTurtleModel.getTexturedModelData());
        MARLIN_LAYER = registerMain("marlin", () -> MarlinModel.getTexturedModelData());
        FAILGULL_LAYER = registerMain("failgull", () -> FailgullModel.getTexturedModelData());
        DOLPHIN_LAYER = registerMain("dolphin", () -> TropicraftDolphinModel.getTexturedModelData());
        SEAHORSE_LAYER = registerMain("seahorse", () -> SeahorseModel.getTexturedModelData());
        TREE_FROG_LAYER = registerMain("tree_frog", () -> TreeFrogModel.getTexturedModelData());
        SEA_URCHIN_LAYER = registerMain("sea_urchin", () -> SeaUrchinModel.getTexturedModelData());
        SEA_URCHIN_EGG_ENTITY_LAYER = registerMain("sea_urchin_egg", () -> EggModel.getTexturedModelData());
        STARFISH_EGG_LAYER = registerMain("starfish_egg", () -> EggModel.getTexturedModelData());
        V_MONKEY_LAYER = registerMain("v_monkey", () -> VMonkeyModel.getTexturedModelData());
        PIRANHA_LAYER = registerMain("piranha", PiranhaModel.getTexturedModelData());
        RIVER_SARDINE_LAYER = registerMain("river_sardine", SardineModel::getTexturedModelData);
        TROPICAL_FISH_LAYER = registerMain("tropical_fish", TropicraftTropicalFishModel::getTexturedModelData);
        EAGLE_RAY_LAYER = registerMain("eagle_ray", EagleRayModel::getTexturedModelData);
        TROPI_SPIDER_EGG_LAYER = registerMain("tropi_spider_egg", () -> EggModel.getTexturedModelData());
        ASHEN_LAYER = registerMain("ashen", AshenModel::getTexturedModelData);
        HAMMERHEAD_LAYER = registerMain("hammerhead", () -> SharkModel.getTexturedModelData());
        SEA_TURTLE_EGG_LAYER  = registerMain("turtle_egg", () -> EggModel.getTexturedModelData());
        TROPI_BEE_LAYER = registerMain("tropi_bee", () -> TropiBeeModel.createBodyLayer());
        COWKTAIL_LAYER = registerMain("cowktail", () -> CowModel.createBodyLayer());
        MAN_O_WAR_LAYER = registerMain("man_o_war", () -> ManOWarModel.getTexturedModelData());
    }

    public static void setupBlockEntityLayers(){
        BAMBOO_MUG = registerMain("bamboo_mug", () -> BambooMugModel.getTexturedModelData());

        //BAMBOO_CHEST = registerMain("bamboo_chest", BambooChestRenderer);
        //BAMBOO_DOUBLE_CHEST_LEFT = registerMain("bamboo_double_chest_left", () -> BambooChestBlockEntityRenderer.getLeftDoubleTexturedModelData());
        //BAMBOO_DOUBLE_CHEST_RIGHT = registerMain("bamboo_double_chest_right", () -> BambooChestBlockEntityRenderer.getRightDoubleTexturedModelData());
        EIHMACHINE_LAYER = registerMain("drink_mixer", ()-> EIHMachineModel.getTexturedModelData());
        AIRCOMPRESSOR_LAYER = registerMain("air_compressor", ()-> EIHMachineModel.getTexturedModelData());
    }


    private static ModelLayerLocation registerMain(String id, LayerDefinition textureModelData) {
        ModelLayerLocation modelLayer = new ModelLayerLocation(new ResourceLocation(Constants.MODID, id), "main");
        EntityModelLayerRegistry.registerModelLayer(modelLayer, textureModelData);
        return modelLayer;
    }
}
