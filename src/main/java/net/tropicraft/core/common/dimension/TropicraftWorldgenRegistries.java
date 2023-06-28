package net.tropicraft.core.common.dimension;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.*;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseRouterData;

public class TropicraftWorldgenRegistries {
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
            .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, TropicraftBiomeBuilder::bootstrap);

    public static HolderLookup.Provider createLookup(final HolderLookup.Provider vanillaProvider) {
        final RegistryAccess.Frozen registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        return BUILDER.buildPatch(registryAccess, vanillaProvider);
    }
}
