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

import static net.tropicraft.core.client.TropicraftRenderLayers.SEA_TURTLE_EGG_LAYER;
import static net.tropicraft.core.client.TropicraftRenderLayers.SEA_URCHIN_EGG_ENTITY_LAYER;
import static net.tropicraft.core.client.TropicraftRenderLayers.STARFISH_EGG_LAYER;
import static net.tropicraft.core.client.TropicraftRenderLayers.TROPI_SPIDER_EGG_LAYER;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

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
}
