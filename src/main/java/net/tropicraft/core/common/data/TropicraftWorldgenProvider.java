package net.tropicraft.core.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryWriteOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class TropicraftWorldgenProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger(TropicraftWorldgenProvider.class);

    private final Path root;

    public TropicraftWorldgenProvider(DataGenerator dataGenerator) {
        this.root = dataGenerator.getOutputFolder().resolve("data");
    }

    @Override
    public void run(HashCache cache) {
        var dynamicRegistries = buildDynamicRegistries();
        var ops = RegistryWriteOps.create(JsonOps.INSTANCE, dynamicRegistries);

        var consumers = new Consumers(root, cache, dynamicRegistries, ops);

        var features = new TropicraftConfiguredFeatures(consumers.configuredFeatures());
        var carvers = new TropicraftConfiguredCarvers(consumers.configuredCarvers());
        var processors = new TropicraftProcessorLists(consumers.processorLists());
        var templates = new TropicraftTemplatePools(consumers.templatePools(), features, processors);
        var structures = new TropicraftConfiguredStructures(consumers.configuredStructures(), templates);

        new TropicraftBiomes(consumers.biomes(), features, structures, carvers);
    }

    private RegistryAccess.RegistryHolder buildDynamicRegistries() {
        RegistryAccess.RegistryHolder dynamicRegistries = new RegistryAccess.RegistryHolder();
        for (Registry<?> registry : BuiltinRegistries.REGISTRY) {
            copyAllToDynamicRegistry(registry, dynamicRegistries);
        }
        return dynamicRegistries;
    }

    private static <T> void copyAllToDynamicRegistry(Registry<T> from, RegistryAccess dynamicRegistries) {
        dynamicRegistries.registry(from.key()).ifPresent(dynamicRegistry -> {
            copyAllToRegistry(from, dynamicRegistry);
        });
    }

    private static <T> void copyAllToRegistry(Registry<T> from, Registry<T> to) {
        for (Map.Entry<ResourceKey<T>, T> entry : from.entrySet()) {
            Registry.register(to, entry.getKey().location(), entry.getValue());
        }
    }

    @Override
    public String getName() {
        return "Tropicraft Worldgen";
    }

    private record Consumers(
            Path root, HashCache cache,
            RegistryAccess.RegistryHolder dynamicRegistries,
            DynamicOps<JsonElement> ops
    ) {

        public WorldgenDataConsumer<ConfiguredFeature<?, ?>> configuredFeatures() {
            return consumer("worldgen/configured_feature", BuiltinRegistries.CONFIGURED_FEATURE, ConfiguredFeature.CODEC);
        }

        public WorldgenDataConsumer<ConfiguredSurfaceBuilder<?>> configuredSurfaceBuilders() {
            return consumer("worldgen/configured_surface_builder", BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, ConfiguredSurfaceBuilder.CODEC);
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

        public WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> configuredStructures() {
            return consumer("worldgen/configured_structure_feature", BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.CODEC);
        }

        public WorldgenDataConsumer<Biome> biomes() {
            return consumer("worldgen/biome", null, Biome.CODEC);
        }

        public <T> WorldgenDataConsumer<T> consumer(String path, @Nullable Registry<? super T> registry, Codec<Supplier<T>> codec) {
            return (id, entry) -> {
                Path entryPath = root.resolve(id.getNamespace()).resolve(path).resolve(id.getPath() + ".json");

                try {
                    Optional<JsonElement> serialized = codec.encodeStart(ops, () -> entry).result();
                    if (serialized.isPresent()) {
                        DataProvider.save(GSON, cache, serialized.get(), entryPath);
                    } else {
                        LOGGER.error("Couldn't serialize worldgen entry at {}", entryPath);
                    }
                } catch (IOException e) {
                    LOGGER.error("Couldn't save worldgen entry at {}", entryPath, e);
                }

                if (registry != null) {
                    Registry.register(registry, id, entry);
                    dynamicRegistries.registry(registry.key()).ifPresent(dynamicRegistry -> {
                        Registry.register(dynamicRegistry, id, entry);
                    });
                }

                return entry;
            };
        }
    }
}
