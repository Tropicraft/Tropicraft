package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil;

import java.util.Objects;
import java.util.Set;

public class TropicraftBiomeProvider extends BiomeProvider {
    public static final Codec<TropicraftBiomeProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(b -> b.seed),
                RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter(b -> b.biomes)
        ).apply(instance, instance.stable(TropicraftBiomeProvider::new));
    });

    private static final Set<RegistryKey<Biome>> POSSIBLE_BIOMES = ImmutableSet.of(
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
            TropicraftBiomes.OSA_RAINFOREST,
            TropicraftBiomes.LAKE
    );

    private final long seed;
    private final Registry<Biome> biomes;

    private final Layer noiseLayer;

    public TropicraftBiomeProvider(long seed, Registry<Biome> biomes) {
        super(POSSIBLE_BIOMES.stream().map(biomes::getValueForKey).filter(Objects::nonNull).map(biome -> () -> biome));

        this.seed = seed;
        this.biomes = biomes;

        this.noiseLayer = TropicraftLayerUtil.buildTropicsProcedure(seed, biomes);
    }

    public static void register() {
        Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(Constants.MODID, "tropics"), CODEC);
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider getBiomeProvider(long seed) {
        return new TropicraftBiomeProvider(seed, biomes);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return noiseLayer.func_242936_a(biomes, x, z);
    }
}
