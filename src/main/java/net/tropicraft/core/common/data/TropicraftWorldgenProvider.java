package net.tropicraft.core.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureSets;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public final class TropicraftWorldgenProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger(TropicraftWorldgenProvider.class);

    private final Path root;

    public TropicraftWorldgenProvider(DataGenerator dataGenerator) {
        this.root = dataGenerator.getOutputFolder().resolve("data");
    }

    @Override
    public void run(HashCache cache) {
        var consumers = buildConsumers(cache);

        var features = new TropicraftConfiguredFeatures(consumers.configuredFeatures(), consumers.placedFeatures());
        var carvers = new TropicraftConfiguredCarvers(consumers.configuredCarvers());
        var processors = new TropicraftProcessorLists(consumers.processorLists());
        var templates = new TropicraftTemplatePools(consumers.templatePools(), features, processors);
        var biomes = new TropicraftBiomes(consumers.biomes(), features, carvers);
        var structures = new TropicraftConfiguredStructures(consumers.configuredStructures(), biomes, templates);

        new TropicraftStructureSets(consumers.structureSets(), structures);
        new TropicraftNoiseGenSettings(consumers.noiseGenSettings());
    }

    private Consumers buildConsumers(HashCache cache) {
        var dynamicRegistries = RegistryAccess.builtinCopy();
        var ops = RegistryOps.create(JsonOps.INSTANCE, dynamicRegistries);
        return new Consumers(root, cache, dynamicRegistries, ops);
    }

    @Override
    public String getName() {
        return "Tropicraft Worldgen";
    }

    private record Consumers(
            Path root, HashCache cache,
            RegistryAccess dynamicRegistries,
            DynamicOps<JsonElement> ops
    ) {
        public WorldgenDataConsumer<ConfiguredFeature<?, ?>> configuredFeatures() {
            return consumer("worldgen/configured_feature", BuiltinRegistries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<PlacedFeature> placedFeatures() {
            return consumer("worldgen/placed_feature", BuiltinRegistries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<ConfiguredWorldCarver<?>> configuredCarvers() {
            return consumer("worldgen/configured_carver", BuiltinRegistries.CONFIGURED_CARVER, ConfiguredWorldCarver.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<StructureProcessorList> processorLists() {
            return consumer("worldgen/processor_list", BuiltinRegistries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<StructureTemplatePool> templatePools() {
            return consumer("worldgen/template_pool", BuiltinRegistries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<NoiseGeneratorSettings> noiseGenSettings() {
            return consumer("worldgen/noise_settings", BuiltinRegistries.NOISE_GENERATOR_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> configuredStructures() {
            return consumer("worldgen/configured_structure_feature", BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<Biome> biomes() {
            return consumer("worldgen/biome", BuiltinRegistries.BIOME, Biome.DIRECT_CODEC);
        }

        public WorldgenDataConsumer<StructureSet> structureSets() {
            return consumer("worldgen/structure_sets", BuiltinRegistries.STRUCTURE_SETS, StructureSet.DIRECT_CODEC);
        }

        public <T> WorldgenDataConsumer<T> consumer(String path, Registry<T> builtinRegistry, Codec<T> codec) {
            return (id, value) -> {
                var entryPath = root.resolve(id.getNamespace()).resolve(path).resolve(id.getPath() + ".json");

                try {
                    DataResult<JsonElement> result = codec.encodeStart(ops, value);
                    var serialized = result.result();
                    if (serialized.isPresent()) {
                        DataProvider.save(GSON, cache, serialized.get(), entryPath);
                    } else if (result.error().isPresent()) {
                        LOGGER.error("Couldn't serialize worldgen entry at {}: '{}'", entryPath, result.error().get());
                    }
                } catch (IOException e) {
                    LOGGER.error("Couldn't save worldgen entry at {}", entryPath, e);
                }

                final Registry<T> registry = dynamicRegistries.registry(builtinRegistry.key())
                        .orElseThrow(() -> new IllegalStateException("Missing dynamic registry for " + builtinRegistry.key()));
                return BuiltinRegistries.register(registry, id, value);
            };
        }
    }
}
