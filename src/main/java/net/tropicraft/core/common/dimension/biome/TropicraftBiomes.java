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
    public static final Biome KELP_FOREST = new TropicsKelpForestBiome();
    public static final Biome RAINFOREST_PLAINS = new TropicraftRainforestBiome(0.25F, 0.1F);
    public static final Biome RAINFOREST_HILLS = new TropicraftRainforestBiome(0.45F, 0.425F);
    public static final Biome RAINFOREST_MOUNTAINS = new TropicraftRainforestBiome(1.0F, 1.2F);
    public static final Biome RAINFOREST_ISLAND_MOUNTAINS = new TropicraftRainforestBiome(0.1F, 2F);
    public static final Biome TROPICS_RIVER = new TropicsRiverBiome();
    public static final Biome TROPICS_BEACH = new TropicsBeachBiome();
   // public static final Biome TROPICS_LAKE = new TropicsLakeBiome();

    @SubscribeEvent
    public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {
        register(event, TROPICS_OCEAN, "tropics_ocean");
        register(event, TROPICS, "tropics");
        register(event, TROPICS_RIVER, "tropics_river");
        register(event, TROPICS_BEACH, "tropics_beach");
        register(event, KELP_FOREST, "kelp_forest");
        register(event, RAINFOREST_PLAINS, "rainforest_plains");
        register(event, RAINFOREST_HILLS, "rainforest_hills");
        register(event, RAINFOREST_MOUNTAINS, "rainforest_mountains");
        register(event, RAINFOREST_ISLAND_MOUNTAINS, "rainforest_island_mountains");
     //   register(event, TROPICS_LAKE, "tropics_lake");
    }


    private static final void register(final RegistryEvent.Register<Biome> event, final Biome biome, final String name) {
        event.getRegistry().register(biome.setRegistryName(new ResourceLocation(Constants.MODID, name)));
    }
}
