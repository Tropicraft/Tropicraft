package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
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
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TropicraftChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<TropicraftChunkGenerator> CODEC = RecordCodecBuilder.create((p_188643_) -> {
        return commonCodec(p_188643_).and(p_188643_.group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY)
                .forGetter((p_188716_) -> {
            return p_188716_.parameters;
        }), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
            return p_188711_.biomeSource;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((p_188690_) -> {
            return p_188690_.seed;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_204585_) -> {
            return p_204585_.settings;
        }))).apply(p_188643_, p_188643_.stable(TropicraftChunkGenerator::new));
    });

    private final VolcanoGenerator volcano;
    private final Registry<NormalNoise.NoiseParameters> parameters;
    private final long seed;

    public TropicraftChunkGenerator(Registry<StructureSet> structureset, Registry<NormalNoise.NoiseParameters> registry, BiomeSource biomes, long seed, Holder<NoiseGeneratorSettings> settings) {
        super(structureset, registry, biomes, seed, settings);
        this.parameters = registry;
        this.seed = seed;
        this.volcano = new VolcanoGenerator(seed, biomes, this);
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
        return new TropicraftChunkGenerator(this.structureSets, parameters, this.biomeSource.withSeed(seed), seed, this.settings);
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

    public VolcanoGenerator getVolcano() {
        return volcano;
    }
}
