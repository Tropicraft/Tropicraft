package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class UnderwaterSurfaceBuilder extends SurfaceBuilder<UnderwaterSurfaceBuilder.Config> {
    public UnderwaterSurfaceBuilder(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, Config config) {
        SurfaceBuilderConfig selectedConfig = config.beach;
        if (startHeight > seaLevel + 5) {
            selectedConfig = config.land;
        }
        if (chunk.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, x, z) + 1 < seaLevel) {
            selectedConfig = config.underwater;
        }

        SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, selectedConfig);
    }

    public static final class Config implements ISurfaceBuilderConfig {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    SurfaceBuilderConfig.CODEC.fieldOf("beach").forGetter(c -> c.beach),
                    SurfaceBuilderConfig.CODEC.fieldOf("land").forGetter(c -> c.land),
                    SurfaceBuilderConfig.CODEC.fieldOf("underwater").forGetter(c -> c.underwater)
            ).apply(instance, Config::new);
        });

        public final SurfaceBuilderConfig beach;
        public final SurfaceBuilderConfig land;
        public final SurfaceBuilderConfig underwater;

        public Config(SurfaceBuilderConfig beach, SurfaceBuilderConfig land, SurfaceBuilderConfig underwater) {
            this.beach = beach;
            this.land = land;
            this.underwater = underwater;
        }

        @Override
        public BlockState getTopMaterial() {
            return this.beach.getTopMaterial();
        }

        @Override
        public BlockState getUnderMaterial() {
            return this.beach.getUnderMaterial();
        }
    }
}
