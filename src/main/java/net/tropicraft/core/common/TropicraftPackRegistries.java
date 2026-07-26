package net.tropicraft.core.common;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.tropicraft.core.common.attribute.TropicraftTimelines;
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
import net.tropicraft.core.common.dimension.df.TropicraftDensityFunctions;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import net.tropicraft.core.common.dimension.noise.TropicraftNoises;
import net.tropicraft.core.common.drinks.TropicraftDrinkIngredients;
import net.tropicraft.core.common.drinks.TropicraftDrinks;
import net.tropicraft.core.common.item.TropicraftJukeboxSongs;
import net.tropicraft.core.common.trade.TropicraftTradeSets;
import net.tropicraft.core.common.trade.TropicraftTrades;

public class TropicraftPackRegistries {
    public static void addTo(Output output) {
        output.add(Registries.CONFIGURED_FEATURE, context -> {
            TropicraftTreeFeatures.bootstrap(context);
            TropicraftVegetationFeatures.bootstrap(context);
            TropicraftMiscFeatures.bootstrap(context);
        });
        output.add(Registries.PLACED_FEATURE, context -> {
            TropicraftTreePlacements.boostrap(context);
            TropicraftVegetationPlacements.bootstrap(context);
            TropicraftMiscPlacements.boostrap(context);
        });
        output.add(Registries.PROCESSOR_LIST, TropicraftProcessorLists::bootstrap);
        output.add(Registries.CONFIGURED_CARVER, TropicraftConfiguredCarvers::bootstrap);
        output.add(Registries.TEMPLATE_POOL, TropicraftTemplatePools::bootstrap);
        output.add(Registries.STRUCTURE, TropicraftStructures::bootstrap);
        output.add(Registries.STRUCTURE_SET, TropicraftStructureSets::bootstrap);
        output.add(Registries.DENSITY_FUNCTION, TropicraftDensityFunctions::bootstrap);
        output.add(Registries.NOISE, TropicraftNoises::bootstrap);
        output.add(Registries.NOISE_SETTINGS, TropicraftNoiseGenSettings::bootstrap);
        output.add(Registries.BIOME, TropicraftBiomes::bootstrap);
        output.add(Registries.DIMENSION_TYPE, TropicraftDimension::bootstrapDimensionType);
        output.add(Registries.LEVEL_STEM, TropicraftDimension::bootstrapLevelStem);
        output.add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, TropicraftBiomeBuilder::bootstrap);
        output.add(Registries.JUKEBOX_SONG, TropicraftJukeboxSongs::bootstrap);
        output.add(Registries.TIMELINE, TropicraftTimelines::bootstrap);
        output.add(Registries.TRADE_SET, TropicraftTradeSets::bootstrap);
        output.add(Registries.VILLAGER_TRADE, TropicraftTrades::bootstrap);
        output.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TropicraftBiomeModifiers::bootstrap);
        output.add(TropicraftRegistries.DRINK_INGREDIENT, TropicraftDrinkIngredients::bootstrap);
        output.add(TropicraftRegistries.DRINK, TropicraftDrinks::bootstrap);
    }

    public static RegistrySetBuilder createRegistrySet() {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        addTo(builder::add);
        return builder;
    }

    @FunctionalInterface
    public interface Output {
        <T> void add(ResourceKey<Registry<T>> registry, RegistrySetBuilder.RegistryBootstrap<T> bootstrap);
    }
}
