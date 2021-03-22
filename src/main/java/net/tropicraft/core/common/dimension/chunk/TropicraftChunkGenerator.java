package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;

import java.util.function.Supplier;

public class TropicraftChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<TropicraftChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                BiomeProvider.CODEC.fieldOf("biome_source").forGetter(g -> g.biomeProvider),
                Codec.LONG.fieldOf("seed").stable().forGetter(g -> g.seed),
                DimensionSettings.DIMENSION_SETTINGS_CODEC.fieldOf("settings").forGetter(g -> g.field_236080_h_)
        ).apply(instance, instance.stable(TropicraftChunkGenerator::new));
    });

    private final VolcanoGenerator volcano;
    private final long seed;

    public TropicraftChunkGenerator(BiomeProvider biomeProvider, long seed, Supplier<DimensionSettings> settings) {
        super(biomeProvider, seed, settings);
        this.seed = seed;
        this.volcano = new VolcanoGenerator(seed, biomeProvider);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR_CODEC, new ResourceLocation(Constants.MODID, "tropics"), CODEC);
    }

    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator func_230349_a_(long seed) {
        return new TropicraftChunkGenerator(this.biomeProvider.getBiomeProvider(seed), seed, this.field_236080_h_);
    }

    @Override
    public int getGroundHeight() {
        return getSeaLevel() + 1;
    }

    @Override
    public void func_230352_b_(IWorld world, StructureManager structures, IChunk chunk) {
        super.func_230352_b_(world, structures, chunk);

        ChunkPos chunkPos = chunk.getPos();
        volcano.generate(chunkPos.x, chunkPos.z, chunk, randomSeed);
    }

    @Override
    public int getHeight(int x, int z, Type heightmapType) {
        int height = super.getHeight(x, z, heightmapType);
        if (heightmapType != Type.OCEAN_FLOOR && heightmapType != Type.OCEAN_FLOOR_WG) {
            return Math.max(height, this.volcano.getVolcanoHeight(height, x, z));
        }
        return height;
    }
}
