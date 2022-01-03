package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil;

import java.util.Objects;
import java.util.Set;

public class TropicraftBiomeProvider extends BiomeSource {
    public static final Codec<TropicraftBiomeProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(b -> b.seed),
                RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(b -> b.biomes)
        ).apply(instance, instance.stable(TropicraftBiomeProvider::new));
    });

    private static final Set<ResourceKey<Biome>> POSSIBLE_BIOMES = ImmutableSet.of(
            TropicraftBiomes.TROPICS,
            TropicraftBiomes.TROPICS_OCEAN,
            TropicraftBiomes.TROPICS_RIVER,
            TropicraftBiomes.TROPICS_BEACH,
            TropicraftBiomes.RAINFOREST_HILLS,
            TropicraftBiomes.RAINFOREST_PLAINS,
            TropicraftBiomes.RAINFOREST_MOUNTAINS,
            TropicraftBiomes.BAMBOO_RAINFOREST,
            TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS,
            TropicraftBiomes.KELP_FOREST,
            TropicraftBiomes.MANGROVES,
            TropicraftBiomes.OVERGROWN_MANGROVES,
            TropicraftBiomes.OSA_RAINFOREST
    );

    private final long seed;
    private final Registry<Biome> biomes;

//    private final Layer noiseLayer;

    public TropicraftBiomeProvider(long seed, Registry<Biome> biomes) {
        super(POSSIBLE_BIOMES.stream().map(biomes::get).filter(Objects::nonNull).map(biome -> () -> biome));

        this.seed = seed;
        this.biomes = biomes;

//        this.noiseLayer = TropicraftLayerUtil.buildTropicsProcedure(seed, biomes);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(Constants.MODID, "tropics"), CODEC);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new TropicraftBiomeProvider(seed, biomes);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
//        return noiseLayer.get(biomes, x, z);
    }
}
