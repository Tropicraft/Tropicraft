package net.tropicraft.core.client;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.model.BambooMugModel;
import net.tropicraft.core.client.entity.model.BasiliskLizardModel;
import net.tropicraft.core.client.entity.model.BeachFloatModel;
import net.tropicraft.core.client.entity.model.ChairModel;
import net.tropicraft.core.client.entity.model.CuberaModel;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.client.entity.model.EIHModel;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.client.entity.model.EggModel;
import net.tropicraft.core.client.entity.model.FailgullModel;
import net.tropicraft.core.client.entity.model.FiddlerCrabModel;
import net.tropicraft.core.client.entity.model.HummingbirdModel;
import net.tropicraft.core.client.entity.model.IguanaModel;
import net.tropicraft.core.client.entity.model.JaguarModel;
import net.tropicraft.core.client.entity.model.KoaModel;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.model.MarlinModel;
import net.tropicraft.core.client.entity.model.PiranhaModel;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;
import net.tropicraft.core.client.entity.model.SardineModel;
import net.tropicraft.core.client.entity.model.SeaTurtleModel;
import net.tropicraft.core.client.entity.model.SeaUrchinModel;
import net.tropicraft.core.client.entity.model.SeahorseModel;
import net.tropicraft.core.client.entity.model.SharkModel;
import net.tropicraft.core.client.entity.model.SpiderMonkeyModel;
import net.tropicraft.core.client.entity.model.TapirModel;
import net.tropicraft.core.client.entity.model.TreeFrogModel;
import net.tropicraft.core.client.entity.model.TropiBeeModel;
import net.tropicraft.core.client.entity.model.TropiCreeperModel;
import net.tropicraft.core.client.entity.model.TropiSkellyModel;
import net.tropicraft.core.client.entity.model.TropicraftDolphinModel;
import net.tropicraft.core.client.entity.model.TropicraftTropicalFishModel;
import net.tropicraft.core.client.entity.model.UmbrellaModel;
import net.tropicraft.core.client.entity.model.VMonkeyModel;
import net.tropicraft.core.client.entity.model.WhiteLippedPeccaryModel;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;


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
    public static ModelLayerLocation MAN_O_WAR_OUTER_LAYER;
    public static ModelLayerLocation MAN_O_WAR_GEL_LAYER;
    public static ModelLayerLocation BAMBOO_MUG;
    public static ArrayList<ModelLayerLocation> ASHEN_MASK_LAYERS = new ArrayList<>();//= registerMain("mask", PlayerHeadpieceModel::getTexturedModelData);
    public static ModelLayerLocation STACHE_LAYER;
    public static ModelLayerLocation BAMBOO_CHEST;
    public static ModelLayerLocation BAMBOO_DOUBLE_CHEST_LEFT;
    public static ModelLayerLocation BAMBOO_DOUBLE_CHEST_RIGHT;
    public static ModelLayerLocation EIHMACHINE_LAYER;
    public static ModelLayerLocation AIRCOMPRESSOR_LAYER;
    public static ModelLayerLocation BASILISK_LIZARD_LAYER;
    public static ModelLayerLocation CUBERA_LAYER;
    public static ModelLayerLocation FIDDLER_CRAB_LAYER;
    public static ModelLayerLocation HUMMINGBIRD_LAYER;
    public static ModelLayerLocation JAGUAR_LAYER;
    public static ModelLayerLocation TAPIR_LAYER;
    public static ModelLayerLocation SPIDER_MONKEY_LAYER;
    public static ModelLayerLocation WHITE_LIPPED_PECCARY_LAYER;
    public static ModelLayerLocation CHEST_SCUBA_LAYER;
    public static ModelLayerLocation FEET_SCUBA_LAYER;
    public static ModelLayerLocation HEAD_SCUBA_LAYER;
    public static ModelLayerLocation TANK_SCUBA_LAYER;

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        KOA_HUNTER_LAYER = registerMain("koa_hunter", KoaModel::create, event);
        TROPI_CREEPER_LAYER = registerMain("tropi_creeper", TropiCreeperModel::create, event);
        IGUANA_LAYER = registerMain("iguana", IguanaModel::create, event);
        UMBRELLA_LAYER = registerMain("umbrella", UmbrellaModel::create, event);
        BEACH_FLOAT_LAYER = registerMain("beach_float", BeachFloatModel::create, event);
        CHAIR_LAYER = registerMain("chair", ChairModel::create, event);
        TROPI_SKELLY_LAYER = registerMain("tropi_skelly", TropiSkellyModel::create, event);
        EIH_LAYER = registerMain("eih", EIHModel::create, event);
        SEA_TURTLE_LAYER = registerMain("sea_turtle", SeaTurtleModel::create, event);
        MARLIN_LAYER = registerMain("marlin", MarlinModel::create, event);
        FAILGULL_LAYER = registerMain("failgull", FailgullModel::create, event);
        DOLPHIN_LAYER = registerMain("dolphin", TropicraftDolphinModel::create, event);
        SEAHORSE_LAYER = registerMain("seahorse", SeahorseModel::create, event);
        TREE_FROG_LAYER = registerMain("tree_frog", TreeFrogModel::create, event);
        SEA_URCHIN_LAYER = registerMain("sea_urchin", SeaUrchinModel::create, event);
        SEA_URCHIN_EGG_ENTITY_LAYER = registerMain("sea_urchin_egg", EggModel::create, event);
        STARFISH_EGG_LAYER = registerMain("starfish_egg", EggModel::create, event);
        V_MONKEY_LAYER = registerMain("v_monkey", VMonkeyModel::create, event);
        PIRANHA_LAYER = registerMain("piranha", PiranhaModel::create, event);
        RIVER_SARDINE_LAYER = registerMain("river_sardine", SardineModel::create, event);
        TROPICAL_FISH_LAYER = registerMain("tropical_fish", TropicraftTropicalFishModel::create, event);
        EAGLE_RAY_LAYER = registerMain("eagle_ray", EagleRayModel::create, event);
        TROPI_SPIDER_EGG_LAYER = registerMain("tropi_spider_egg", EggModel::create, event);
        ASHEN_LAYER = registerMain("ashen", AshenModel::create, event);
        HAMMERHEAD_LAYER = registerMain("hammerhead", SharkModel::create, event);
        SEA_TURTLE_EGG_LAYER  = registerMain("turtle_egg", EggModel::create, event);
        TROPI_BEE_LAYER = registerMain("tropi_bee", TropiBeeModel::createBodyLayer, event);
        COWKTAIL_LAYER = registerMain("cowktail", CowModel::createBodyLayer, event);
        MAN_O_WAR_OUTER_LAYER = registerMain("man_o_war", ManOWarModel::createOuterModel, event);
        MAN_O_WAR_OUTER_LAYER = registerMain("man_o_war_gel", ManOWarModel::createGelLayerModel, event);

        BAMBOO_MUG = registerMain("bamboo_mug", BambooMugModel::create, event);

        //BAMBOO_CHEST = registerMain("bamboo_chest", BambooChestRenderer);
        //BAMBOO_DOUBLE_CHEST_LEFT = registerMain("bamboo_double_chest_left", () -> BambooChestBlockEntityRenderer.getLeftDoubleTexturedModelData());
        //BAMBOO_DOUBLE_CHEST_RIGHT = registerMain("bamboo_double_chest_right", () -> BambooChestBlockEntityRenderer.getRightDoubleTexturedModelData());
        EIHMACHINE_LAYER = registerMain("drink_mixer", EIHMachineModel::create, event);
        AIRCOMPRESSOR_LAYER = registerMain("air_compressor", EIHMachineModel::create, event);
        BASILISK_LIZARD_LAYER = registerMain("basilisk_lizard", BasiliskLizardModel::create, event);
        CUBERA_LAYER = registerMain("cubera", CuberaModel::create, event);
        FIDDLER_CRAB_LAYER = registerMain("fiddler_crab", FiddlerCrabModel::create, event);
        HUMMINGBIRD_LAYER = registerMain("hummingbird", HummingbirdModel::create, event);
        JAGUAR_LAYER = registerMain("jaguar", JaguarModel::create, event);
        TAPIR_LAYER = registerMain("tapir", TapirModel::create, event);
        SPIDER_MONKEY_LAYER = registerMain("spider_monkey", SpiderMonkeyModel::create, event);
        WHITE_LIPPED_PECCARY_LAYER = registerMain("white_lipped_peccary", WhiteLippedPeccaryModel::create, event);

        //ArrayList<MaskArmorProvider> MASK_PROVIDER = new ArrayList<>();
        final List<RegistryObject<AshenMaskItem>> masks = TropicraftItems.ASHEN_MASKS.values().asList();

        for (RegistryObject<AshenMaskItem> maskItem : masks) {
            ModelLayerLocation ashen_mask_layer = registerMain("ashen_mask_" + maskItem.get().getMaskType().name().toLowerCase(Locale.ROOT), PlayerHeadpieceModel::create, event);
            ASHEN_MASK_LAYERS.add(ashen_mask_layer);
        }

        STACHE_LAYER = registerMain("nigel_stache", PlayerHeadpieceModel::create, event);

        HEAD_SCUBA_LAYER = registerMain("scuba_goggles", ModelScubaGear::create, event);
        CHEST_SCUBA_LAYER = registerMain("scuba_harness", ModelScubaGear::create, event);
        FEET_SCUBA_LAYER = registerMain("scuba_flippers", ModelScubaGear::create, event);
        TANK_SCUBA_LAYER = registerMain("pony_bottle", ModelScubaGear::create, event);

        setupScubaGearModels();
    }

    private static ModelLayerLocation registerMain(String id, Supplier<LayerDefinition> layerDefinition, EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModelLayerLocation modelLayer = new ModelLayerLocation(new ResourceLocation(Constants.MODID, id), "main");
        event.registerLayerDefinition(modelLayer, layerDefinition);
        return modelLayer;
    }

    public static void setupScubaGearModels() {
        // TODO 1.17
//        ModelScubaGear.HEAD = ModelScubaGear.createModel(HEAD_SCUBA_LAYER, null, EquipmentSlot.HEAD);
//        ModelScubaGear.CHEST = ModelScubaGear.createModel(CHEST_SCUBA_LAYER, null, EquipmentSlot.CHEST);
//        ModelScubaGear.FEET = ModelScubaGear.createModel(FEET_SCUBA_LAYER, null, EquipmentSlot.FEET);
//
//        ModelScubaGear.tankModel = ModelScubaGear.createModel(TANK_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    }
}
