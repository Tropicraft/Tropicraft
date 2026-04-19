package net.tropicraft.core.client.renderer.special;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.tropicraft.Tropicraft;

@EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
public final class TropicraftSpecialRenderers {
    @SubscribeEvent
    public static void register(RegisterSpecialModelRendererEvent event) {
        event.register(Tropicraft.id("drink_mixer"), DrinkMixerSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(Tropicraft.id("air_compressor"), AirCompressorSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
