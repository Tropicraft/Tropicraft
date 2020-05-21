package net.tropicraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tropicraft.core.client.entity.render.AshenMaskRenderer;
import net.tropicraft.core.client.entity.render.AshenRenderer;
import net.tropicraft.core.client.entity.render.BambooItemFrameRenderer;
import net.tropicraft.core.client.entity.render.BeachFloatRenderer;
import net.tropicraft.core.client.entity.render.ChairRenderer;
import net.tropicraft.core.client.entity.render.EIHRenderer;
import net.tropicraft.core.client.entity.render.EagleRayRenderer;
import net.tropicraft.core.client.entity.render.EggRenderer;
import net.tropicraft.core.client.entity.render.FailgullRenderer;
import net.tropicraft.core.client.entity.render.IguanaRenderer;
import net.tropicraft.core.client.entity.render.KoaRenderer;
import net.tropicraft.core.client.entity.render.MarlinRenderer;
import net.tropicraft.core.client.entity.render.PiranhaRenderer;
import net.tropicraft.core.client.entity.render.PoisonBlotRenderer;
import net.tropicraft.core.client.entity.render.SardineRenderer;
import net.tropicraft.core.client.entity.render.SeaTurtleRenderer;
import net.tropicraft.core.client.entity.render.SeaUrchinRenderer;
import net.tropicraft.core.client.entity.render.SeahorseRenderer;
import net.tropicraft.core.client.entity.render.SharkRenderer;
import net.tropicraft.core.client.entity.render.StarfishRenderer;
import net.tropicraft.core.client.entity.render.TreeFrogRenderer;
import net.tropicraft.core.client.entity.render.TropiCreeperRenderer;
import net.tropicraft.core.client.entity.render.TropiSkellyRenderer;
import net.tropicraft.core.client.entity.render.TropiSpiderRenderer;
import net.tropicraft.core.client.entity.render.TropicraftDolphinRenderer;
import net.tropicraft.core.client.entity.render.TropicraftTropicalFishRenderer;
import net.tropicraft.core.client.entity.render.UmbrellaRenderer;
import net.tropicraft.core.client.entity.render.VMonkeyRenderer;
import net.tropicraft.core.client.entity.render.WallItemRenderer;
import net.tropicraft.core.client.tileentity.AirCompressorRenderer;
import net.tropicraft.core.client.tileentity.BambooChestRenderer;
import net.tropicraft.core.client.tileentity.DrinkMixerRenderer;
import net.tropicraft.core.client.tileentity.SifterRenderer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class ClientSetup {

    public static void setupBlockRenderLayers() {
        RenderType renderType = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.AIR_COMPRESSOR.get(), renderType);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.COCONUT.get(), renderType);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.DRINK_MIXER.get(), renderType);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.SIFTER.get(), renderType);
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.TIKI_TORCH.get(), renderType);

        renderType = RenderType.getCutoutMipped();
        RenderTypeLookup.setRenderLayer(TropicraftBlocks.THATCH_STAIRS_FUZZY.get(), renderType);
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
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.ASHEN.get(), AshenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.ASHEN_MASK.get(), AshenMaskRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.EXPLODING_COCONUT.get(), manager -> new SpriteRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(TropicraftEntities.HAMMERHEAD.get(), SharkRenderer::new);
    }

    public static void setupTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.BAMBOO_CHEST.get(), BambooChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.SIFTER.get(), SifterRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.DRINK_MIXER.get(), DrinkMixerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TropicraftTileEntityTypes.AIR_COMPRESSOR.get(), AirCompressorRenderer::new);
    }
}
