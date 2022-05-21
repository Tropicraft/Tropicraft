package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TropicraftBiomeSource extends BiomeSource {
    public static final Codec<TropicraftBiomeSource> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(b -> b.seed),
                RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(b -> b.biomes)
        ).apply(instance, instance.stable(TropicraftBiomeSource::new));
    });

    private static final Set<ResourceKey<Biome>> POSSIBLE_BIOMES = Stream.of(
            TropicraftBiomes.TROPICS,
            TropicraftBiomes.OCEAN,
            TropicraftBiomes.RIVER,
            TropicraftBiomes.BEACH,
            TropicraftBiomes.RAINFOREST,
            TropicraftBiomes.BAMBOO_RAINFOREST,
            TropicraftBiomes.KELP_FOREST,
            TropicraftBiomes.MANGROVES,
            TropicraftBiomes.OVERGROWN_MANGROVES,
            TropicraftBiomes.OSA_RAINFOREST
    ).map(RegistryObject::getKey).collect(Collectors.toSet());

    private final long seed;
    private final Registry<Biome> biomes;
    private final Climate.ParameterList<Holder<Biome>> parameters;

//    private final Layer noiseLayer;

    public TropicraftBiomeSource(long seed, Registry<Biome> biomes) {
        super(POSSIBLE_BIOMES.stream().map(biomes::getHolderOrThrow));

        this.seed = seed;
        this.biomes = biomes;

//        this.noiseLayer = TropicraftLayerUtil.buildTropicsProcedure(seed, biomes);

        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        new TropicraftBiomeBuilder().addBiomes((point, biome) -> builder.add(Pair.of(point, biomes.getHolderOrThrow(biome.getKey()))));

        parameters = new Climate.ParameterList<>(builder.build());
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
        return new TropicraftBiomeSource(seed, biomes);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
//        return noiseLayer.get(biomes, x, z);
        // TODO 1.18: temporary, needs proper gen!
        return this.parameters.findValue(sampler.sample(x, y, z));
    }
}
