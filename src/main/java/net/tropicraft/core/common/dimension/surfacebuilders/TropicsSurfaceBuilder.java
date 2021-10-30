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

public class TropicsSurfaceBuilder extends SurfaceBuilder<TropicsSurfaceBuilder.Config> {
    public TropicsSurfaceBuilder(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, Config config) {
        SurfaceBuilderConfig selectedConfig = config.land;
        if (noise > 1.5) {
            if (chunk.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, x, z) + 1 >= seaLevel) {
                selectedConfig = config.sandy;
            } else {
                selectedConfig = config.sandyUnderwater;
            }
        }

        SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, selectedConfig);
    }

    public static final class Config implements ISurfaceBuilderConfig {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    SurfaceBuilderConfig.CODEC.fieldOf("land").forGetter(c -> c.land),
                    SurfaceBuilderConfig.CODEC.fieldOf("sandy").forGetter(c -> c.sandy),
                    SurfaceBuilderConfig.CODEC.fieldOf("sandy_underwater").forGetter(c -> c.sandyUnderwater)
            ).apply(instance, Config::new);
        });

        public final SurfaceBuilderConfig land;
        public final SurfaceBuilderConfig sandy;
        public final SurfaceBuilderConfig sandyUnderwater;

        public Config(SurfaceBuilderConfig land, SurfaceBuilderConfig sandy, SurfaceBuilderConfig sandyUnderwater) {
            this.land = land;
            this.sandy = sandy;
            this.sandyUnderwater = sandyUnderwater;
        }

        @Override
        public BlockState getTopMaterial() {
            return this.land.getTopMaterial();
        }

        @Override
        public BlockState getUnderMaterial() {
            return this.land.getUnderMaterial();
        }
    }
}
