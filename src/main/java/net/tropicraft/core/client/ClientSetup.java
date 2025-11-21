package net.tropicraft.core.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.render.EggRenderer;
import net.tropicraft.core.common.dimension.TropicraftDimension;

@EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
public class ClientSetup {
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
