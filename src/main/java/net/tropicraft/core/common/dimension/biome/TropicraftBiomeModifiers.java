package net.tropicraft.core.common.dimension.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationPlacements;

public class TropicraftBiomeModifiers {
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(createKey("overworld_palm_trees"), new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BEACH)),
                HolderSet.direct(placedFeatures.getOrThrow(TropicraftVegetationPlacements.TREES_PALM_OVERWORLD)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(createKey("overworld_pineapples"), new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.JUNGLE), biomes.getOrThrow(Biomes.SPARSE_JUNGLE), biomes.getOrThrow(Biomes.BAMBOO_JUNGLE)),
                HolderSet.direct(placedFeatures.getOrThrow(TropicraftVegetationPlacements.PINEAPPLE)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }

    private static ResourceKey<BiomeModifier> createKey(String name) {
        return Tropicraft.resourceKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS, name);
    }
}
