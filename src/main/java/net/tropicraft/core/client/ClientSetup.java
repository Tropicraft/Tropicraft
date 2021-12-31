package net.tropicraft.core.client;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.*;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.client.tileentity.AirCompressorRenderer;
import net.tropicraft.core.client.tileentity.BambooChestRenderer;
import net.tropicraft.core.client.tileentity.DrinkMixerRenderer;
import net.tropicraft.core.client.tileentity.SifterRenderer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.TropicraftBlockEntityTypes;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

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
    public static ModelLayerLocation TAPIR_LAYER;
    public static ModelLayerLocation JAGUAR_LAYER;
    public static ModelLayerLocation BROWN_BASILISK_LIZARD_LAYER;
    public static ModelLayerLocation GREEN_BASILISK_LIZARD_LAYER;
    public static ModelLayerLocation HUMMINGBIRD_LAYER;
    public static ModelLayerLocation FIDDLER_CRAB_LAYER;
    public static ModelLayerLocation SPIDER_MONKEY_LAYER;
    public static ModelLayerLocation WHITE_LIPPED_PECCARY_LAYER;
    public static ModelLayerLocation CUBERA_LAYER;
    public static ModelLayerLocation BAMBOO_MUG;
    public static ModelLayerLocation EIHMACHINE_LAYER;
    public static ModelLayerLocation AIRCOMPRESSOR_LAYER;
    public static ArrayList<ModelLayerLocation> ASHEN_MASK_LAYERS = new ArrayList<>();//= registerMain("mask", PlayerHeadpieceModel::getTexturedModelData);
    public static ModelLayerLocation STACHE_LAYER;
    public static ModelLayerLocation CHEST_SCUBA_LAYER;
    public static ModelLayerLocation FEET_SCUBA_LAYER;
    public static ModelLayerLocation HEAD_SCUBA_LAYER;
    public static ModelLayerLocation TANK_SCUBA_LAYER;

    public static void setupBlockRenderLayers() {
        RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.AIR_COMPRESSOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.COCONUT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.DRINK_MIXER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.SIFTER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.TIKI_TORCH.get(), cutout);
        TropicraftBlocks.FLOWERS.forEach((key, value) -> ItemBlockRenderTypes.setRenderLayer(value.get(), RenderType.cutout()));
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.PINEAPPLE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.IRIS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.COFFEE_BUSH.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.GOLDEN_LEATHER_FERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.GRAPEFRUIT_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.LEMON_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.LIME_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.ORANGE_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.PAPAYA_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.MAHOGANY_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.PALM_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.PALM_TRAPDOOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.PALM_DOOR.get(), cutout);

        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.MANGROVE_TRAPDOOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.MANGROVE_DOOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.BAMBOO_TRAPDOOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.BAMBOO_DOOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.BAMBOO_LADDER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.BAMBOO_FLOWER_POT.get(), cutout);
        TropicraftBlocks.BAMBOO_POTTED_TROPICS_PLANTS.forEach(value -> ItemBlockRenderTypes.setRenderLayer(value.get(), RenderType.cutout()));
        TropicraftBlocks.BAMBOO_POTTED_VANILLA_PLANTS.forEach(value -> ItemBlockRenderTypes.setRenderLayer(value.get(), RenderType.cutout()));
        TropicraftBlocks.VANILLA_POTTED_TROPICS_PLANTS.forEach(value -> ItemBlockRenderTypes.setRenderLayer(value.get(), RenderType.cutout()));
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.REEDS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.PAPAYA.get(), cutout);

        RenderType cutoutMipped = RenderType.cutoutMipped();
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.THATCH_STAIRS_FUZZY.get(), cutoutMipped);

        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.RED_MANGROVE_PROPAGULE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.TALL_MANGROVE_PROPAGULE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.TEA_MANGROVE_PROPAGULE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.RED_MANGROVE_ROOTS.get(), cutoutMipped);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get(), cutoutMipped);
        ItemBlockRenderTypes.setRenderLayer(TropicraftBlocks.BLACK_MANGROVE_ROOTS.get(), cutoutMipped);

        for (RegistryObject<RedstoneWallTorchBlock> block : TropicraftBlocks.JIGARBOV_WALL_TORCHES.values()) {
            ItemBlockRenderTypes.setRenderLayer(block.get(), cutoutMipped);
        }
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        KOA_HUNTER_LAYER = registerLayer("koa_hunter", () -> KoaModel.create(), event);
        TROPI_CREEPER_LAYER = registerLayer("tropi_creeper", () -> TropiCreeperModel.create(), event);
        IGUANA_LAYER = registerLayer("iguana", () -> IguanaModel.create(), event);
        UMBRELLA_LAYER = registerLayer("umbrella", () -> UmbrellaModel.create(), event);
        BEACH_FLOAT_LAYER = registerLayer("beach_float", () -> BeachFloatModel.create(), event);
        CHAIR_LAYER = registerLayer("chair", () -> ChairModel.create(), event);
        TROPI_SKELLY_LAYER = registerLayer("tropi_skelly", () -> TropiSkellyModel.create(), event);
        EIH_LAYER = registerLayer("eih", () -> EIHModel.create(), event);
        SEA_TURTLE_LAYER = registerLayer("sea_turtle", () -> SeaTurtleModel.create(), event);
        MARLIN_LAYER = registerLayer("marlin", () -> MarlinModel.create(), event);
        FAILGULL_LAYER = registerLayer("failgull", () -> FailgullModel.create(), event);
        DOLPHIN_LAYER = registerLayer("dolphin", () -> TropicraftDolphinModel.create(), event);
        SEAHORSE_LAYER = registerLayer("seahorse", () -> SeahorseModel.create(), event);
        TREE_FROG_LAYER = registerLayer("tree_frog", () -> TreeFrogModel.create(), event);
        SEA_URCHIN_LAYER = registerLayer("sea_urchin", () -> SeaUrchinModel.create(), event);
        SEA_URCHIN_EGG_ENTITY_LAYER = registerLayer("sea_urchin_egg", () -> EggModel.create(), event);
        STARFISH_EGG_LAYER = registerLayer("starfish_egg", () -> EggModel.create(), event);
        V_MONKEY_LAYER = registerLayer("v_monkey", () -> VMonkeyModel.create(), event);
        PIRANHA_LAYER = registerLayer("piranha", () -> PiranhaModel.create(), event);
        RIVER_SARDINE_LAYER = registerLayer("river_sardine", () -> SardineModel.create(), event);
        TROPICAL_FISH_LAYER = registerLayer("tropical_fish", () -> TropicraftTropicalFishModel.create(), event);
        EAGLE_RAY_LAYER = registerLayer("eagle_ray", () ->  EagleRayModel.create(), event);
        TROPI_SPIDER_EGG_LAYER = registerLayer("tropi_spider_egg", () -> EggModel.create(), event);
        ASHEN_LAYER = registerLayer("ashen", () -> AshenModel.create(), event);
        HAMMERHEAD_LAYER = registerLayer("hammerhead", () -> SharkModel.create(), event);
        SEA_TURTLE_EGG_LAYER  = registerLayer("turtle_egg", () -> EggModel.create(), event);
        TROPI_BEE_LAYER = registerLayer("tropi_bee", () -> TropiBeeModel.createBodyLayer(), event);
        COWKTAIL_LAYER = registerLayer("cowktail", () -> CowModel.createBodyLayer(), event);
        MAN_O_WAR_LAYER = registerLayer("man_o_war", () -> ManOWarModel.create(), event);
        TAPIR_LAYER = registerLayer("tapir", () -> TapirModel.create(), event);
        JAGUAR_LAYER = registerLayer("jaguar", () -> JaguarModel.create(), event);
        BROWN_BASILISK_LIZARD_LAYER = registerLayer("brown_basilisk_lizard", () -> BasiliskLizardModel.create(), event);
        GREEN_BASILISK_LIZARD_LAYER = registerLayer("green_basilisk_lizard", () -> BasiliskLizardModel.create(), event);
        HUMMINGBIRD_LAYER = registerLayer("hummingbird", () -> HummingbirdModel.create(), event);
        FIDDLER_CRAB_LAYER = registerLayer("fiddler_crab", () -> FiddlerCrabModel.create(), event);
        SPIDER_MONKEY_LAYER = registerLayer("spider_monkey", () -> SpiderMonkeyModel.create(), event);
        WHITE_LIPPED_PECCARY_LAYER = registerLayer("white_lipped_peccary", () -> WhiteLippedPeccaryModel.create(), event);
        CUBERA_LAYER = registerLayer("cubera", () -> CuberaModel.create(), event);

        //Misc Layers
        BAMBOO_MUG = registerLayer("bamboo_mug", () -> BambooMugModel.create(), event);

        //Block Entity's Layers
        EIHMACHINE_LAYER = registerLayer("drink_mixer", () -> EIHMachineModel.create(), event);
        AIRCOMPRESSOR_LAYER = registerLayer("air_compressor", () -> EIHMachineModel.create(), event);

        //Armor Layers
        for(RegistryObject<AshenMaskItem> maskItem : TropicraftItems.ASHEN_MASKS.values().asList()){
            ModelLayerLocation ashen_mask_layer = registerLayer("ashen_mask_" + maskItem.get().getMaskType().name().toLowerCase(Locale.ROOT), PlayerHeadpieceModel::create, event);
            ASHEN_MASK_LAYERS.add(ashen_mask_layer);
        }
        STACHE_LAYER = registerLayer("nigel_stache", () -> PlayerHeadpieceModel.create(), event);

        HEAD_SCUBA_LAYER = registerLayer("scuba_goggles", () -> ModelScubaGear.create(), event);
        CHEST_SCUBA_LAYER = registerLayer("scuba_harness", () -> ModelScubaGear.create(), event);
        FEET_SCUBA_LAYER = registerLayer("scuba_flippers", () -> ModelScubaGear.create(), event);

        TANK_SCUBA_LAYER = registerLayer("pony_bottle", () -> ModelScubaGear.create(), event);

        setupScubaGearModels();
    }

    //@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TropicraftEntities.KOA_HUNTER.get(), KoaRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TROPI_CREEPER.get(), TropiCreeperRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.IGUANA.get(), IguanaRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.UMBRELLA.get(), UmbrellaRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.CHAIR.get(), ChairRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.BEACH_FLOAT.get(), BeachFloatRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TROPI_SKELLY.get(), TropiSkellyRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.EIH.get(), EIHRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.WALL_ITEM.get(), WallItemRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.BAMBOO_ITEM_FRAME.get(), BambooItemFrameRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.SEA_TURTLE.get(), SeaTurtleRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.MARLIN.get(), MarlinRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.FAILGULL.get(), FailgullRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.DOLPHIN.get(), TropicraftDolphinRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.SEAHORSE.get(), SeahorseRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TREE_FROG.get(), TreeFrogRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.POISON_BLOT.get(), PoisonBlotRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.SEA_URCHIN.get(), SeaUrchinRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.SEA_URCHIN_EGG_ENTITY.get(), (context) -> new EggRenderer(context, SEA_URCHIN_EGG_ENTITY_LAYER));
        event.registerEntityRenderer(TropicraftEntities.STARFISH.get(), StarfishRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.STARFISH_EGG.get(), (context) -> new EggRenderer(context, STARFISH_EGG_LAYER));
        event.registerEntityRenderer(TropicraftEntities.V_MONKEY.get(), VMonkeyRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.PIRANHA.get(), PiranhaRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.RIVER_SARDINE.get(), SardineRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TROPICAL_FISH.get(), TropicraftTropicalFishRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.EAGLE_RAY.get(), EagleRayRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TROPI_SPIDER.get(), TropiSpiderRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TROPI_SPIDER_EGG.get(), (context) -> new EggRenderer(context, TROPI_SPIDER_EGG_LAYER));
        event.registerEntityRenderer(TropicraftEntities.ASHEN.get(), AshenRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.ASHEN_MASK.get(), AshenMaskRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.EXPLODING_COCONUT.get(), (context) -> new ThrownItemRenderer<>(context));
        event.registerEntityRenderer(TropicraftEntities.HAMMERHEAD.get(), SharkRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.SEA_TURTLE_EGG.get(), (context) -> new EggRenderer(context, SEA_TURTLE_EGG_LAYER));
        event.registerEntityRenderer(TropicraftEntities.TROPI_BEE.get(), TropiBeeRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.COWKTAIL.get(), CowktailRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.MAN_O_WAR.get(), ManOWarRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.TAPIR.get(), TapirRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.JAGUAR.get(), JaguarRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.BROWN_BASILISK_LIZARD.get(), BasiliskLizardRenderer::brown);
        event.registerEntityRenderer(TropicraftEntities.GREEN_BASILISK_LIZARD.get(), BasiliskLizardRenderer::green);
        event.registerEntityRenderer(TropicraftEntities.HUMMINGBIRD.get(), HummingbirdRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.FIDDLER_CRAB.get(), FiddlerCrabRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.SPIDER_MONKEY.get(), SpiderMonkeyRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), WhiteLippedPeccaryRenderer::new);
        event.registerEntityRenderer(TropicraftEntities.CUBERA.get(), CuberaRenderer::new);

        setupTileEntityRenderers(event);

    }

    public static void setupTileEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TropicraftBlockEntityTypes.BAMBOO_CHEST.get(), BambooChestRenderer::new);
        event.registerBlockEntityRenderer(TropicraftBlockEntityTypes.SIFTER.get(), SifterRenderer::new);
        event.registerBlockEntityRenderer(TropicraftBlockEntityTypes.DRINK_MIXER.get(), DrinkMixerRenderer::new);
        event.registerBlockEntityRenderer(TropicraftBlockEntityTypes.AIR_COMPRESSOR.get(), AirCompressorRenderer::new);
    }

    public static void setupDimensionRenderInfo() {
        DimensionSpecialEffects.EFFECTS.put(TropicraftDimension.WORLD.location(), new DimensionSpecialEffects(192.0F, true, DimensionSpecialEffects.SkyType.NORMAL, false, false) {
            @Override
            public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
                return color.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
            }

            @Override
            public boolean isFoggyAt(int x, int z) {
                return false;
            }
        });
    }

    public static void setupScubaGearModels(){
        ModelScubaGear.HEAD = ModelScubaGear.createModel(HEAD_SCUBA_LAYER, null, EquipmentSlot.HEAD);
        ModelScubaGear.CHEST = ModelScubaGear.createModel(CHEST_SCUBA_LAYER, null, EquipmentSlot.CHEST);
        ModelScubaGear.FEET = ModelScubaGear.createModel(FEET_SCUBA_LAYER, null, EquipmentSlot.FEET);

        ModelScubaGear.tankModel = ModelScubaGear.createModel(TANK_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    }

    private static ModelLayerLocation registerLayer(String id, Supplier<LayerDefinition> layerDefinition, EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModelLayerLocation modelLayer = new ModelLayerLocation(new ResourceLocation(Constants.MODID, id), "main");
        event.registerLayerDefinition(modelLayer, layerDefinition);
        return modelLayer;
    }
}
