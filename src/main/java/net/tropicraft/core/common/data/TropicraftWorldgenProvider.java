package net.tropicraft.core.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
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
        var structures = new TropicraftConfiguredStructures(consumers.configuredStructures(), templates);
        var noiseSettings = new TropicraftNoiseGenSettings(consumers.noiseGenSettings(), structures);

        new TropicraftBiomes(consumers.biomes(), features, structures, carvers);
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
            return consumer("worldgen/configured_feature", BuiltinRegistries.CONFIGURED_FEATURE, ConfiguredFeature.CODEC);
        }

        public WorldgenDataConsumer<PlacedFeature> placedFeatures() {
            return consumer("worldgen/placed_feature", BuiltinRegistries.PLACED_FEATURE, PlacedFeature.CODEC);
        }

        public WorldgenDataConsumer<ConfiguredWorldCarver<?>> configuredCarvers() {
            return consumer("worldgen/configured_carver", BuiltinRegistries.CONFIGURED_CARVER, ConfiguredWorldCarver.CODEC);
        }

        public WorldgenDataConsumer<StructureProcessorList> processorLists() {
            return consumer("worldgen/processor_list", BuiltinRegistries.PROCESSOR_LIST, StructureProcessorType.LIST_CODEC);
        }

        public WorldgenDataConsumer<StructureTemplatePool> templatePools() {
            return consumer("worldgen/template_pool", BuiltinRegistries.TEMPLATE_POOL, StructureTemplatePool.CODEC);
        }

        public WorldgenDataConsumer<NoiseGeneratorSettings> noiseGenSettings() {
            return consumer("worldgen/noise_settings", BuiltinRegistries.NOISE_GENERATOR_SETTINGS, NoiseGeneratorSettings.CODEC);
        }

        public WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> configuredStructures() {
            return consumer("worldgen/configured_structure_feature", BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.CODEC);
        }

        public WorldgenDataConsumer<Biome> biomes() {
            return consumer("worldgen/biome", BuiltinRegistries.BIOME, Biome.CODEC);
        }

        public <T> WorldgenDataConsumer<T> consumer(String path, Registry<T> registry, Codec<Holder<T>> codec) {
            return (id, value) -> {
                var entryPath = root.resolve(id.getNamespace()).resolve(path).resolve(id.getPath() + ".json");

                final Holder<T> holder = BuiltinRegistries.register(registry, id, value);
                dynamicRegistries.registry(registry.key()).ifPresent(dynamicRegistry -> {
                    BuiltinRegistries.register(dynamicRegistry, id, value);
                });

                try {
                    DataResult<JsonElement> result = codec.encodeStart(ops, holder);
                    var serialized = result.result();
                    if (serialized.isPresent()) {
                        DataProvider.save(GSON, cache, serialized.get(), entryPath);
                    } else if (result.error().isPresent()) {
                        LOGGER.error("Couldn't serialize worldgen entry at {}: '{}'", entryPath, result.error().get());
                    }
                } catch (IOException e) {
                    LOGGER.error("Couldn't save worldgen entry at {}", entryPath, e);
                }

                return holder;
            };
        }
    }
}
