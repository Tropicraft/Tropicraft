package net.tropicraft.core.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataProvider;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryWriteOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TropicraftWorldgenProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger(TropicraftWorldgenProvider.class);

    private static final LazyLoadedValue<RegistryAccess.RegistryHolder> DYNAMIC_REGISTRIES = new LazyLoadedValue<>(() -> {
        RegistryAccess.RegistryHolder dynamicRegistries = new RegistryAccess.RegistryHolder();
        for (Registry<?> registry : BuiltinRegistries.REGISTRY) {
            copyAllToDynamicRegistry(registry, dynamicRegistries);
        }
        return dynamicRegistries;
    });

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

    private final Path root;
    private final Consumer<Generator> generatorFunction;

    public TropicraftWorldgenProvider(DataGenerator dataGenerator, Consumer<Generator> generatorFunction) {
        this.root = dataGenerator.getOutputFolder().resolve("data");
        this.generatorFunction = generatorFunction;
    }

    @Override
    public void run(HashCache cache) {
        RegistryAccess.RegistryHolder dynamicRegistries = DYNAMIC_REGISTRIES.get();
        DynamicOps<JsonElement> ops = RegistryWriteOps.create(JsonOps.INSTANCE, dynamicRegistries);

        Generator generator = new Generator(root, cache, dynamicRegistries, ops);
        this.generatorFunction.accept(generator);
    }

    @Override
    public String getName() {
        return "Tropicraft Worldgen";
    }

    public static final class Generator {
        private final Path root;
        private final HashCache cache;
        private final RegistryAccess.RegistryHolder dynamicRegistries;
        private final DynamicOps<JsonElement> ops;

        Generator(Path root, HashCache cache, RegistryAccess.RegistryHolder dynamicRegistries, DynamicOps<JsonElement> ops) {
            this.root = root;
            this.cache = cache;
            this.dynamicRegistries = dynamicRegistries;
            this.ops = ops;
        }

        public <R> R addConfiguredFeatures(EntryGenerator<ConfiguredFeature<?, ?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_feature", BuiltinRegistries.CONFIGURED_FEATURE, ConfiguredFeature.CODEC,
                    entryGenerator
            );
        }

        public <R> R addConfiguredSurfaceBuilders(EntryGenerator<ConfiguredSurfaceBuilder<?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_surface_builder", BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, ConfiguredSurfaceBuilder.CODEC,
                    entryGenerator
            );
        }

        public <R> R addConfiguredCarvers(EntryGenerator<ConfiguredWorldCarver<?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_carver", BuiltinRegistries.CONFIGURED_CARVER, ConfiguredWorldCarver.CODEC,
                    entryGenerator
            );
        }

        public <R> R addProcessorLists(EntryGenerator<StructureProcessorList, R> entryGenerator) {
            return add(
                    "worldgen/processor_list", BuiltinRegistries.PROCESSOR_LIST, StructureProcessorType.LIST_CODEC,
                    entryGenerator
            );
        }

        public <R> R addTemplatePools(EntryGenerator<StructureTemplatePool, R> entryGenerator) {
            return add(
                    "worldgen/template_pool", BuiltinRegistries.TEMPLATE_POOL, StructureTemplatePool.CODEC,
                    entryGenerator
            );
        }

        public <R> R addConfiguredStructures(EntryGenerator<ConfiguredStructureFeature<?, ?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_structure_feature", BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.CODEC,
                    entryGenerator
            );
        }

        public <R> R addBiomes(EntryGenerator<Biome, R> entryGenerator) {
            return add(
                    "worldgen/biome", null, Biome.CODEC,
                    entryGenerator
            );
        }

        public <T, R> R add(
                String path, @Nullable Registry<? super T> registry, Codec<Supplier<T>> codec,
                EntryGenerator<? extends T, R> entryGenerator
        ) {
            return entryGenerator.generate((id, entry) -> {
                Path entryPath = root.resolve(id.getNamespace()).resolve(path).resolve(id.getPath() + ".json");

                Function<Supplier<T>, DataResult<JsonElement>> function = ops.withEncoder(codec);

                try {
                    Optional<JsonElement> serialized = function.apply(() -> entry).result();
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
            });
        }
    }

    public interface EntryGenerator<T, R> {
        R generate(WorldgenDataConsumer<T> consumer);
    }
}
