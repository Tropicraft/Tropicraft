package net.tropicraft.core.common;

import com.tterrag.registrate.providers.DataProviderInitializer;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeModifiers;
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
import net.tropicraft.core.common.drinks.TropicraftDrinkIngredients;
import net.tropicraft.core.common.drinks.TropicraftDrinks;
import net.tropicraft.core.common.item.TropicraftJukeboxSongs;

public class TropicraftPackRegistries {
    public static void addTo(DataProviderInitializer initializer) {
        initializer.add(Registries.CONFIGURED_FEATURE, context -> {
            TropicraftTreeFeatures.bootstrap(context);
            TropicraftVegetationFeatures.bootstrap(context);
            TropicraftMiscFeatures.bootstrap(context);
        });
        initializer.add(Registries.PLACED_FEATURE, context -> {
            TropicraftTreePlacements.boostrap(context);
            TropicraftVegetationPlacements.bootstrap(context);
            TropicraftMiscPlacements.boostrap(context);
        });
        initializer.add(Registries.PROCESSOR_LIST, TropicraftProcessorLists::bootstrap);
        initializer.add(Registries.CONFIGURED_CARVER, TropicraftConfiguredCarvers::bootstrap);
        initializer.add(Registries.TEMPLATE_POOL, TropicraftTemplatePools::bootstrap);
        initializer.add(Registries.STRUCTURE, TropicraftStructures::bootstrap);
        initializer.add(Registries.STRUCTURE_SET, TropicraftStructureSets::bootstrap);
        initializer.add(Registries.DENSITY_FUNCTION, TropicraftNoiseRouterData::bootstrap);
        initializer.add(Registries.NOISE_SETTINGS, TropicraftNoiseGenSettings::bootstrap);
        initializer.add(Registries.BIOME, TropicraftBiomes::bootstrap);
        initializer.add(Registries.DIMENSION_TYPE, TropicraftDimension::bootstrapDimensionType);
        initializer.add(Registries.LEVEL_STEM, TropicraftDimension::bootstrapLevelStem);
        initializer.add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, TropicraftBiomeBuilder::bootstrap);
        initializer.add(Registries.JUKEBOX_SONG, TropicraftJukeboxSongs::bootstrap);
        initializer.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TropicraftBiomeModifiers::bootstrap);
        initializer.add(TropicraftRegistries.DRINK_INGREDIENT, TropicraftDrinkIngredients::bootstrap);
        initializer.add(TropicraftRegistries.DRINK, TropicraftDrinks::bootstrap);
    }
}
