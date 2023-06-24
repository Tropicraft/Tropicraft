package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Constants;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TropicraftChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<TropicraftChunkGenerator> CODEC = RecordCodecBuilder.create(i -> commonCodec(i).and(i.group(
            RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(c -> c.parameters),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(c -> c.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(c -> c.settings)
    )).apply(i, i.stable(TropicraftChunkGenerator::new)));

    private final VolcanoGenerator volcano;
    private final Registry<NormalNoise.NoiseParameters> parameters;

    public TropicraftChunkGenerator(Registry<StructureSet> structureset, Registry<NormalNoise.NoiseParameters> registry, BiomeSource biomes, Holder<NoiseGeneratorSettings> settings) {
        super(structureset, registry, biomes, settings);
        this.parameters = registry;
        this.volcano = new VolcanoGenerator();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Constants.MODID, "tropics"), CODEC);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return super.fillFromNoise(executor, blender, randomState, structureManager, chunk)
                .thenApply(volcanoChunk -> {
                    ChunkPos chunkPos = volcanoChunk.getPos();
                    WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(randomState.legacyLevelSeed()));
                    // keysmashes ftw
                    random.setFeatureSeed(chunkPos.toLong(), 59317, 31931);
                    this.volcano.generate(chunkPos.x, chunkPos.z, volcanoChunk, randomState, biomeSource, random);
                    return volcanoChunk;
                });
    }

    @Override
    public int getBaseHeight(int x, int z, Types heightmapType, LevelHeightAccessor accessor, RandomState random) {
        int height = super.getBaseHeight(x, z, heightmapType, accessor, random);
        if (heightmapType != Types.OCEAN_FLOOR && heightmapType != Types.OCEAN_FLOOR_WG) {
            return Math.max(height, this.volcano.getVolcanoHeight(random, biomeSource, height, x, z));
        }
        return height;
    }

    public VolcanoGenerator getVolcano() {
        return volcano;
    }
}
