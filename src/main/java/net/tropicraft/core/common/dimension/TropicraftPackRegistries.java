package net.tropicraft.core.common.dimension;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftMiscFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftMiscPlacements;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureSets;
import net.tropicraft.core.common.dimension.feature.TropicraftStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftTreeFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftTreePlacements;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationPlacements;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseRouterData;
import net.tropicraft.core.common.item.TropicraftJukeboxSongs;

import java.util.concurrent.CompletableFuture;

public class TropicraftPackRegistries {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> {
                TropicraftTreeFeatures.bootstrap(context);
                TropicraftVegetationFeatures.bootstrap(context);
                TropicraftMiscFeatures.bootstrap(context);
            })
            .add(Registries.PLACED_FEATURE, context -> {
                TropicraftTreePlacements.boostrap(context);
                TropicraftVegetationPlacements.bootstrap(context);
                TropicraftMiscPlacements.boostrap(context);
            })
            .add(Registries.PROCESSOR_LIST, TropicraftProcessorLists::bootstrap)
            .add(Registries.CONFIGURED_CARVER, TropicraftConfiguredCarvers::bootstrap)
            .add(Registries.TEMPLATE_POOL, TropicraftTemplatePools::bootstrap)
            .add(Registries.STRUCTURE, TropicraftStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, TropicraftStructureSets::bootstrap)
            .add(Registries.DENSITY_FUNCTION, TropicraftNoiseRouterData::bootstrap)
            .add(Registries.NOISE_SETTINGS, TropicraftNoiseGenSettings::bootstrap)
            .add(Registries.BIOME, TropicraftBiomes::bootstrap)
            .add(Registries.DIMENSION_TYPE, TropicraftDimension::bootstrapDimensionType)
            .add(Registries.LEVEL_STEM, TropicraftDimension::bootstrapLevelStem)
            .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, TropicraftBiomeBuilder::bootstrap)
            .add(Registries.JUKEBOX_SONG, TropicraftJukeboxSongs::bootstrap);

    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanillaProvider) {
        return RegistryPatchGenerator.createLookup(vanillaProvider, BUILDER);
    }
}
