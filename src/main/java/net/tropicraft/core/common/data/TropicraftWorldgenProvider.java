package net.tropicraft.core.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.LazyValue;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.util.registry.WorldGenSettingsExport;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
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

public final class TropicraftWorldgenProvider implements IDataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger(TropicraftWorldgenProvider.class);

    private static final LazyValue<DynamicRegistries.Impl> DYNAMIC_REGISTRIES = new LazyValue<>(() -> {
        DynamicRegistries.Impl dynamicRegistries = new DynamicRegistries.Impl();
        for (Registry<?> registry : WorldGenRegistries.ROOT_REGISTRIES) {
            copyAllToDynamicRegistry(registry, dynamicRegistries);
        }
        return dynamicRegistries;
    });

    private static <T> void copyAllToDynamicRegistry(Registry<T> from, DynamicRegistries dynamicRegistries) {
        dynamicRegistries.func_230521_a_(from.getRegistryKey()).ifPresent(dynamicRegistry -> {
            copyAllToRegistry(from, dynamicRegistry);
        });
    }

    private static <T> void copyAllToRegistry(Registry<T> from, Registry<T> to) {
        for (Map.Entry<RegistryKey<T>, T> entry : from.getEntries()) {
            Registry.register(to, entry.getKey().getLocation(), entry.getValue());
        }
    }

    private final Path root;
    private final Consumer<Generator> generatorFunction;

    public TropicraftWorldgenProvider(DataGenerator dataGenerator, Consumer<Generator> generatorFunction) {
        this.root = dataGenerator.getOutputFolder().resolve("data");
        this.generatorFunction = generatorFunction;
    }

    @Override
    public void act(DirectoryCache cache) {
        DynamicRegistries.Impl dynamicRegistries = DYNAMIC_REGISTRIES.getValue();
        DynamicOps<JsonElement> ops = WorldGenSettingsExport.create(JsonOps.INSTANCE, dynamicRegistries);

        Generator generator = new Generator(root, cache, dynamicRegistries, ops);
        this.generatorFunction.accept(generator);
    }

    @Override
    public String getName() {
        return "Tropicraft Worldgen";
    }

    public static final class Generator {
        private final Path root;
        private final DirectoryCache cache;
        private final DynamicRegistries.Impl dynamicRegistries;
        private final DynamicOps<JsonElement> ops;

        Generator(Path root, DirectoryCache cache, DynamicRegistries.Impl dynamicRegistries, DynamicOps<JsonElement> ops) {
            this.root = root;
            this.cache = cache;
            this.dynamicRegistries = dynamicRegistries;
            this.ops = ops;
        }

        public <R> R addConfiguredFeatures(EntryGenerator<ConfiguredFeature<?, ?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_feature", WorldGenRegistries.CONFIGURED_FEATURE, ConfiguredFeature.field_236264_b_,
                    entryGenerator
            );
        }

        public <R> R addConfiguredSurfaceBuilders(EntryGenerator<ConfiguredSurfaceBuilder<?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_surface_builder", WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, ConfiguredSurfaceBuilder.field_244393_b_,
                    entryGenerator
            );
        }

        public <R> R addConfiguredCarvers(EntryGenerator<ConfiguredCarver<?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_carver", WorldGenRegistries.CONFIGURED_CARVER, ConfiguredCarver.field_244390_b_,
                    entryGenerator
            );
        }

        public <R> R addProcessorLists(EntryGenerator<StructureProcessorList, R> entryGenerator) {
            return add(
                    "worldgen/processor_list", WorldGenRegistries.STRUCTURE_PROCESSOR_LIST, IStructureProcessorType.PROCESSOR_LIST_CODEC,
                    entryGenerator
            );
        }

        public <R> R addTemplatePools(EntryGenerator<JigsawPattern, R> entryGenerator) {
            return add(
                    "worldgen/template_pool", WorldGenRegistries.JIGSAW_POOL, JigsawPattern.field_244392_b_,
                    entryGenerator
            );
        }

        public <R> R addConfiguredStructures(EntryGenerator<StructureFeature<?, ?>, R> entryGenerator) {
            return add(
                    "worldgen/configured_structure_feature", WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, StructureFeature.field_244391_b_,
                    entryGenerator
            );
        }

        public <R> R addBiomes(EntryGenerator<Biome, R> entryGenerator) {
            return add(
                    "worldgen/biome", null, Biome.BIOME_CODEC,
                    entryGenerator
            );
        }

        public <T, R> R add(
                String path, @Nullable Registry<T> registry, Codec<Supplier<T>> codec,
                EntryGenerator<T, R> entryGenerator
        ) {
            return entryGenerator.generate((id, entry) -> {
                Path entryPath = root.resolve(id.getNamespace()).resolve(path).resolve(id.getPath() + ".json");

                Function<Supplier<T>, DataResult<JsonElement>> function = ops.withEncoder(codec);

                try {
                    Optional<JsonElement> serialized = function.apply(() -> entry).result();
                    if (serialized.isPresent()) {
                        IDataProvider.save(GSON, cache, serialized.get(), entryPath);
                    } else {
                        LOGGER.error("Couldn't serialize worldgen entry at {}", entryPath);
                    }
                } catch (IOException e) {
                    LOGGER.error("Couldn't save worldgen entry at {}", entryPath, e);
                }

                if (registry != null) {
                    Registry.register(registry, id, entry);
                    dynamicRegistries.func_230521_a_(registry.getRegistryKey()).ifPresent(dynamicRegistry -> {
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
