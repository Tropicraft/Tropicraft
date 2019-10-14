package net.tropicraft.core.common.dimension.biome;

import java.util.function.Supplier;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Info;

public class TropicraftBiomes {
    
    public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, Info.MODID);

    public static final RegistryObject<Biome> TROPICS_OCEAN = register("tropics_ocean", TropicsOceanBiome::new);
    public static final RegistryObject<Biome> TROPICS = register("tropics", TropicsBiome::new);
    public static final RegistryObject<Biome> KELP_FOREST = register("kelp_forest", () -> new TropicsKelpForestBiome());
    public static final RegistryObject<Biome> RAINFOREST_PLAINS = register("rainforest_plains", () -> new TropicraftRainforestBiome(0.25F, 0.1F));
    public static final RegistryObject<Biome> RAINFOREST_HILLS = register("rainforest_hills", () -> new TropicraftRainforestBiome(0.45F, 0.425F));
    public static final RegistryObject<Biome> RAINFOREST_MOUNTAINS = register("rainforest_mountains", () -> new TropicraftRainforestBiome(0.8F, 0.8F));
    public static final RegistryObject<Biome> RAINFOREST_ISLAND_MOUNTAINS = register("rainforest_island_mountains", () -> new TropicraftRainforestBiome(0.1F, 1.2F));
    public static final RegistryObject<Biome> TROPICS_RIVER = register("tropics_river", TropicsRiverBiome::new);
    public static final RegistryObject<Biome> TROPICS_BEACH = register("tropics_beach", TropicsBeachBiome::new);
   // public static final Biome TROPICS_LAKE = new TropicsLakeBiome();

    private static final <T extends Biome> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return BIOMES.register(name, sup);
    }
    
    public static void addFeatures() {
        for (Biome b : ForgeRegistries.BIOMES.getValues()) {
            if (b instanceof TropicraftBiome) {
                ((TropicraftBiome) b).addFeatures();
            } else if (b.getCategory() == Category.BEACH) {
                DefaultTropicsFeatures.addPalmTrees(b);
            }
        }
    }
}
