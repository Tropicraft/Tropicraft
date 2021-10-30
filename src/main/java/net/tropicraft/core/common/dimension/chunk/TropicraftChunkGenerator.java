package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class TropicraftChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<TropicraftChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                BiomeProvider.CODEC.fieldOf("biome_source").forGetter(g -> g.biomeSource),
                Codec.LONG.fieldOf("seed").stable().forGetter(g -> g.seed),
                DimensionSettings.CODEC.fieldOf("settings").forGetter(g -> g.settings)
        ).apply(instance, instance.stable(TropicraftChunkGenerator::new));
    });

    private final VolcanoGenerator volcano;
    private final long seed;

    public TropicraftChunkGenerator(BiomeProvider biomeProvider, long seed, Supplier<DimensionSettings> settings) {
        super(biomeProvider, seed, settings);
        this.seed = seed;
        this.volcano = new VolcanoGenerator(seed, biomeProvider);

        // maintain parity with old noise. cursed? very. i'm sorry :(
        SharedSeedRandom random = new SharedSeedRandom(seed);
        new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
        new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
        new OctavesNoiseGenerator(random, IntStream.rangeClosed(-7, 0));
        new PerlinNoiseGenerator(random, IntStream.rangeClosed(-3, 0));

        random.consumeCount(2620);
        this.depthNoise = new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
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
    public int getSpawnHeight() {
        return getSeaLevel() + 1;
    }

    @Override
    public void fillFromNoise(IWorld world, StructureManager structures, IChunk chunk) {
        super.fillFromNoise(world, structures, chunk);

        ChunkPos chunkPos = chunk.getPos();
        volcano.generate(chunkPos.x, chunkPos.z, chunk, random);
    }

    @Override
    public int getBaseHeight(int x, int z, Type heightmapType) {
        int height = super.getBaseHeight(x, z, heightmapType);
        if (heightmapType != Type.OCEAN_FLOOR && heightmapType != Type.OCEAN_FLOOR_WG) {
            return Math.max(height, this.volcano.getVolcanoHeight(height, x, z));
        }
        return height;
    }
}
