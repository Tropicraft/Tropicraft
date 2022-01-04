package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class TropicraftChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<TropicraftChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                RegistryLookupCodec.create(Registry.NOISE_REGISTRY).forGetter(g -> g.parameters),
                BiomeSource.CODEC.fieldOf("biome_source").forGetter(g -> g.biomeSource),
                Codec.LONG.fieldOf("seed").stable().forGetter(g -> g.seed),
                NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(g -> g.settings)
        ).apply(instance, instance.stable(TropicraftChunkGenerator::new));
    });

    private final VolcanoGenerator volcano;
    private final Registry<NormalNoise.NoiseParameters> parameters;
    private final long seed;

    public TropicraftChunkGenerator(Registry<NormalNoise.NoiseParameters> parameters, BiomeSource biomeProvider, long seed, Supplier<NoiseGeneratorSettings> settings) {
        super(parameters, biomeProvider, seed, settings);
        this.parameters = parameters;
        this.seed = seed;
        this.volcano = new VolcanoGenerator(seed, biomeProvider);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Constants.MODID, "tropics"), CODEC);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new TropicraftChunkGenerator(parameters, this.biomeSource.withSeed(seed), seed, this.settings);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structures, ChunkAccess chunk) {
        return super.fillFromNoise(executor, blender, structures, chunk)
                .thenApply(volcanoChunk -> {
                    ChunkPos chunkPos = volcanoChunk.getPos();
                    WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(this.seed));
                    // keysmashes ftw
                    random.setFeatureSeed(chunkPos.toLong(), 59317, 31931);
                    volcano.generate(chunkPos.x, chunkPos.z, volcanoChunk, random);
                    return volcanoChunk;
                });
    }

    @Override
    public int getBaseHeight(int x, int z, Types heightmapType, LevelHeightAccessor accessor) {
        int height = super.getBaseHeight(x, z, heightmapType, accessor);
        if (heightmapType != Types.OCEAN_FLOOR && heightmapType != Types.OCEAN_FLOOR_WG) {
            return Math.max(height, this.volcano.getVolcanoHeight(height, x, z));
        }
        return height;
    }
}
