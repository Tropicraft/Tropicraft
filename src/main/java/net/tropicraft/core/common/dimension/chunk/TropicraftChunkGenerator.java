package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;
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
                BiomeSource.CODEC.fieldOf("biome_source").forGetter(g -> g.biomeSource),
                Codec.LONG.fieldOf("seed").stable().forGetter(g -> g.seed),
                NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(g -> g.settings)
        ).apply(instance, instance.stable(TropicraftChunkGenerator::new));
    });

    private final VolcanoGenerator volcano;
    private final long seed;

    public TropicraftChunkGenerator(BiomeSource biomeProvider, long seed, Supplier<NoiseGeneratorSettings> settings) {
        super(biomeProvider, seed, settings);
        this.seed = seed;
        this.volcano = new VolcanoGenerator(seed, biomeProvider);

        // maintain parity with old noise. cursed? very. i'm sorry :(
        WorldgenRandom random = new WorldgenRandom(seed);
        new PerlinNoise(random, IntStream.rangeClosed(-15, 0));
        new PerlinNoise(random, IntStream.rangeClosed(-15, 0));
        new PerlinNoise(random, IntStream.rangeClosed(-7, 0));
        new PerlinSimplexNoise(random, IntStream.rangeClosed(-3, 0));

        random.consumeCount(2620);
        new PerlinNoise(random, IntStream.rangeClosed(-15, 0));
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
        return new TropicraftChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structures, ChunkAccess chunk) {
        CompletableFuture<ChunkAccess> future = super.fillFromNoise(executor, structures, chunk);

        future.join();

        ChunkPos chunkPos = chunk.getPos();
        WorldgenRandom random = new WorldgenRandom(this.seed);
        random.setFeatureSeed(chunkPos.toLong(), 0, 3);
        volcano.generate(chunkPos.x, chunkPos.z, chunk, random);

        return future;
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
