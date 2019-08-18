package net.tropicraft.core.common.dimension.surfacebuilders;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftSurfaceBuilders {
    public static final TropicsSurfaceBuilder TROPICS = new TropicsSurfaceBuilder(SurfaceBuilderConfig::deserialize);

    @SubscribeEvent
    public static void onChunkGeneratorTypeRegistry(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
        register(event, TROPICS, "tropics");
    }

    private static final void register(final RegistryEvent.Register<SurfaceBuilder<?>> event, final SurfaceBuilder<?> type, final String name) {
        event.getRegistry().register(type.setRegistryName(new ResourceLocation(Constants.MODID, name)));
    }
}
