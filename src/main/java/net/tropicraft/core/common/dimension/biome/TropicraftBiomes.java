package net.tropicraft.core.common.dimension.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftBiomes {

    public static final Biome TROPICS_OCEAN = new TropicsOceanBiome();
    public static final Biome TROPICS = new TropicsBiome();
//    public static final Biome KELP_FOREST;
//    public static final Biome RAINFOREST_PLAINS;
//    public static final Biome RAINFOREST_HILLS;
//    public static final Biome RAINFOREST_MOUNTAINS;
//    public static final Biome RAINFOREST_ISLAND_MOUNTAINS;
//    public static final Biome TROPICS_RIVER;
//    public static final Biome TROPICS_BEACH;
//    public static final Biome TROPICS_LAKE;

    @SubscribeEvent
    public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {
        register(event, TROPICS_OCEAN, "tropics_ocean");
        register(event, TROPICS, "tropics");
    }

    private static final void register(final RegistryEvent.Register<Biome> event, final Biome biome, final String name) {
        event.getRegistry().register(biome.setRegistryName(new ResourceLocation(Constants.MODID, name)));
    }
}
