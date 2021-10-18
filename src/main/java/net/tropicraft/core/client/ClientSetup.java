package net.tropicraft.core.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.client.tileentity.AirCompressorRenderer;
import net.tropicraft.core.client.tileentity.BambooChestRenderer;
import net.tropicraft.core.client.tileentity.DrinkMixerRenderer;
import net.tropicraft.core.client.tileentity.SifterRenderer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class ClientSetup {

    public static void setupBlockRenderLayers() {
        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.AIR_COMPRESSOR.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.COCONUT.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.DRINK_MIXER.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.SIFTER.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.TIKI_TORCH.get(), cutout);
        TropicraftBlocks.FLOWERS.forEach((key, value) -> RenderTypeLookup.setRenderLayer(value.get(), RenderType.getCutout()));
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.PINEAPPLE.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.IRIS.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.COFFEE_BUSH.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.GOLDEN_LEATHER_FERN.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.GRAPEFRUIT_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.LEMON_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.LIME_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.ORANGE_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.MAHOGANY_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.PALM_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.PALM_TRAPDOOR.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.PALM_DOOR.get(), cutout);

        RenderTypeLookup.setRenderLayer(TropicraftBlocks.MANGROVE_TRAPDOOR.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.MANGROVE_DOOR.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.BAMBOO_TRAPDOOR.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.BAMBOO_DOOR.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.BAMBOO_LADDER.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.BAMBOO_FLOWER_POT.get(), cutout);
        TropicraftBlocks.BAMBOO_POTTED_TROPICS_PLANTS.forEach(value -> RenderTypeLookup.setRenderLayer(value.get(), RenderType.getCutout()));
        TropicraftBlocks.BAMBOO_POTTED_VANILLA_PLANTS.forEach(value -> RenderTypeLookup.setRenderLayer(value.get(), RenderType.getCutout()));
        TropicraftBlocks.VANILLA_POTTED_TROPICS_PLANTS.forEach(value -> RenderTypeLookup.setRenderLayer(value.get(), RenderType.getCutout()));
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.REEDS.get(), cutout);

        RenderType cutoutMipped = RenderType.getCutoutMipped();
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.THATCH_STAIRS_FUZZY.get(), cutoutMipped);

        RenderTypeLookup.setRenderLayer(TropicraftBlocks.RED_MANGROVE_PROPAGULE.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.TALL_MANGROVE_PROPAGULE.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.TEA_MANGROVE_PROPAGULE.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE.get(), cutout);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.RED_MANGROVE_ROOTS.get(), cutoutMipped);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get(), cutoutMipped);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.BLACK_MANGROVE_ROOTS.get(), cutoutMipped);
    }

    public static void setupEntityRenderers(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.KOA_HUNTER.get(), KoaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TROPI_CREEPER.get(), TropiCreeperRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.IGUANA.get(), IguanaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.UMBRELLA.get(), UmbrellaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.CHAIR.get(), ChairRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.BEACH_FLOAT.get(), BeachFloatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TROPI_SKELLY.get(), TropiSkellyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.EIH.get(), EIHRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.WALL_ITEM.get(), WallItemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.BAMBOO_ITEM_FRAME.get(), BambooItemFrameRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.SEA_TURTLE.get(), SeaTurtleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.MARLIN.get(), MarlinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.FAILGULL.get(), FailgullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.DOLPHIN.get(), TropicraftDolphinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.SEAHORSE.get(), SeahorseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TREE_FROG.get(), TreeFrogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.POISON_BLOT.get(), PoisonBlotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.SEA_URCHIN.get(), SeaUrchinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.SEA_URCHIN_EGG_ENTITY.get(), EggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.STARFISH.get(), StarfishRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.STARFISH_EGG.get(), EggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.V_MONKEY.get(), VMonkeyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.PIRANHA.get(), PiranhaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.RIVER_SARDINE.get(), SardineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TROPICAL_FISH.get(), TropicraftTropicalFishRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.EAGLE_RAY.get(), EagleRayRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TROPI_SPIDER.get(), TropiSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TROPI_SPIDER_EGG.get(), EggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.ASHEN.get(), AshenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.ASHEN_MASK.get(), AshenMaskRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.EXPLODING_COCONUT.get(), manager -> new SpriteRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.HAMMERHEAD.get(), SharkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.SEA_TURTLE_EGG.get(), EggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TROPI_BEE.get(), TropiBeeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.COWKTAIL.get(), CowktailRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.MAN_O_WAR.get(), ManOWarRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.TAPIR.get(), TapirRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.JAGUAR.get(), JaguarRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.BROWN_BASILISK_LIZARD.get(), BasiliskLizardRenderer::brown);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.GREEN_BASILISK_LIZARD.get(), BasiliskLizardRenderer::green);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.HUMMINGBIRD.get(), HummingbirdRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.FIDDLER_CRAB.get(), FiddlerCrabRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.SPIDER_MONKEY.get(), SpiderMonkeyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), WhiteLippedPeccaryRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.CUBERA.get(), CuberaRenderer::new);
    }

    public static void setupTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.BAMBOO_CHEST.get(), BambooChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.SIFTER.get(), SifterRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.DRINK_MIXER.get(), DrinkMixerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.AIR_COMPRESSOR.get(), AirCompressorRenderer::new);
    }

    public static void setupDimensionRenderInfo() {
        DimensionRenderInfo.field_239208_a_.put(TropicraftDimension.WORLD.getLocation(), new DimensionRenderInfo(192.0F, true, DimensionRenderInfo.FogType.NORMAL, false, false) {
            @Override
            public Vector3d func_230494_a_(Vector3d color, float brightness) {
                return color.mul(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
            }

            @Override
            public boolean func_230493_a_(int x, int z) {
                return false;
            }
        });
    }
}
