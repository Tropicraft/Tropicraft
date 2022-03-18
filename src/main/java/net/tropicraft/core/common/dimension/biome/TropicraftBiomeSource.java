package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.TerrainInfo;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class TropicraftBiomeSource extends BiomeSource {
    public static final Codec<TropicraftBiomeSource> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(b -> b.seed),
                RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(b -> b.biomes)
        ).apply(instance, instance.stable(TropicraftBiomeSource::new));
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
    private final Climate.ParameterList<Supplier<Biome>> parameters;

//    private final Layer noiseLayer;

    public TropicraftBiomeSource(long seed, Registry<Biome> biomes) {
        super(POSSIBLE_BIOMES.stream().map(biomes::get).filter(Objects::nonNull).map(biome -> () -> biome));

        this.seed = seed;
        this.biomes = biomes;

//        this.noiseLayer = TropicraftLayerUtil.buildTropicsProcedure(seed, biomes);

        ImmutableList.Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> builder = ImmutableList.builder();
        new TropicraftBiomeBuilder().addBiomes(consumer -> builder.add(consumer.mapSecond(key -> () -> biomes.getOrThrow(key))));

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

    public void addMultinoiseDebugInfo(List<String> p_187072_, BlockPos p_187073_, Climate.Sampler p_187074_) {
        int i = QuartPos.fromBlock(p_187073_.getX());
        int j = QuartPos.fromBlock(p_187073_.getY());
        int k = QuartPos.fromBlock(p_187073_.getZ());
        Climate.TargetPoint climate$targetpoint = p_187074_.sample(i, j, k);
        float f = Climate.unquantizeCoord(climate$targetpoint.continentalness());
        float f1 = Climate.unquantizeCoord(climate$targetpoint.erosion());
        float f2 = Climate.unquantizeCoord(climate$targetpoint.temperature());
        float f3 = Climate.unquantizeCoord(climate$targetpoint.humidity());
        float f4 = Climate.unquantizeCoord(climate$targetpoint.weirdness());
        double d0 = (double) TerrainShaper.peaksAndValleys(f4);
        DecimalFormat decimalformat = new DecimalFormat("0.000");
        p_187072_.add("Multinoise C: " + decimalformat.format((double)f) + " E: " + decimalformat.format((double)f1) + " T: " + decimalformat.format((double)f2) + " H: " + decimalformat.format((double)f3) + " W: " + decimalformat.format((double)f4));
        OverworldBiomeBuilder overworldbiomebuilder = new OverworldBiomeBuilder();
        p_187072_.add("Biome builder PV: " + OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(d0) + " C: " + overworldbiomebuilder.getDebugStringForContinentalness((double)f) + " E: " + overworldbiomebuilder.getDebugStringForErosion((double)f1) + " T: " + overworldbiomebuilder.getDebugStringForTemperature((double)f2) + " H: " + overworldbiomebuilder.getDebugStringForHumidity((double)f3));
        if (p_187074_ instanceof NoiseSampler) {
            NoiseSampler noisesampler = (NoiseSampler)p_187074_;
            TerrainInfo terraininfo = noisesampler.terrainInfo(p_187073_.getX(), p_187073_.getZ(), f, f4, f1, Blender.empty());
            p_187072_.add("Terrain PV: " + decimalformat.format(d0) + " O: " + decimalformat.format(terraininfo.offset()) + " F: " + decimalformat.format(terraininfo.factor()) + " JA: " + decimalformat.format(terraininfo.jaggedness()));
        }
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
//        return noiseLayer.get(biomes, x, z);
        // TODO 1.18: temporary, needs proper gen!
        return this.parameters.findValue(sampler.sample(x, y, z), () -> Biomes.THE_VOID).get();
    }
}
