package net.tropicraft.core.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.render.EggRenderer;
import net.tropicraft.core.common.dimension.TropicraftDimension;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    private static final ResourceLocation BAMBOO_SIGN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/signs/bamboo.png");
    private static final ResourceLocation MAHOGANY_SIGN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/signs/mahogany.png");
    private static final ResourceLocation MANGROVE_SIGN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/signs/mangrove.png");
    private static final ResourceLocation PALM_SIGN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/signs/palm.png");
    private static final ResourceLocation THATCH_SIGN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/signs/thatch.png");

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.SIGN_SHEET)) {
            event.addSprite(BAMBOO_SIGN_TEXTURE);
            event.addSprite(MAHOGANY_SIGN_TEXTURE);
            event.addSprite(MANGROVE_SIGN_TEXTURE);
            event.addSprite(PALM_SIGN_TEXTURE);
            event.addSprite(THATCH_SIGN_TEXTURE);
        }
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

    public static EggRenderer seaUrchinEggRenderer(EntityRendererProvider.Context ctx) {
        return new EggRenderer(ctx, TropicraftRenderLayers.SEA_URCHIN_EGG_ENTITY_LAYER);
    }

    public static EggRenderer starfishEggRenderer(EntityRendererProvider.Context ctx) {
        return new EggRenderer(ctx, TropicraftRenderLayers.STARFISH_EGG_LAYER);
    }

    public static EggRenderer tropiSpiderEggRenderer(EntityRendererProvider.Context ctx) {
        return new EggRenderer(ctx, TropicraftRenderLayers.TROPI_SPIDER_EGG_LAYER);
    }

    public static EggRenderer seaTurtleEggRenderer(EntityRendererProvider.Context ctx) {
        return new EggRenderer(ctx, TropicraftRenderLayers.SEA_TURTLE_EGG_LAYER);
    }
}
