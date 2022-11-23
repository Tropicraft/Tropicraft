package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftTerrainShaper;

import java.text.DecimalFormat;
import java.util.List;
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

    private static final TropicraftBiomeBuilder DEBUG_BIOME_HOLDER = new TropicraftBiomeBuilder();
    private static final TerrainShaper DEBUG_TERRAIN_HOLDER = TropicraftTerrainShaper.tropics();
    private static final DecimalFormat DEBUG_DECIMAL_FORMAT = new DecimalFormat("0.000");

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
            TropicraftBiomes.OSA_RAINFOREST,
            TropicraftBiomes.TROPICAL_PEAKS
    ).map(RegistryObject::getKey).collect(Collectors.toSet());

    private final long seed;
    private final Registry<Biome> biomes;
    private final Climate.ParameterList<Holder<Biome>> parameters;

    public TropicraftBiomeSource(long seed, Registry<Biome> biomes) {
        super(POSSIBLE_BIOMES.stream().map(biomes::getHolderOrThrow));

        this.seed = seed;
        this.biomes = biomes;

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
        return this.parameters.findValue(sampler.sample(x, y, z));
    }

    public void addDebugInfo(List<String> p_207895_, BlockPos p_207896_, Climate.Sampler p_207897_) {
        int i = QuartPos.fromBlock(p_207896_.getX());
        int j = QuartPos.fromBlock(p_207896_.getY());
        int k = QuartPos.fromBlock(p_207896_.getZ());
        Climate.TargetPoint climate$targetpoint = p_207897_.sample(i, j, k);
        float f = Climate.unquantizeCoord(climate$targetpoint.continentalness());
        float f1 = Climate.unquantizeCoord(climate$targetpoint.erosion());
        float f2 = Climate.unquantizeCoord(climate$targetpoint.temperature());
        float f3 = Climate.unquantizeCoord(climate$targetpoint.humidity());
        float f4 = Climate.unquantizeCoord(climate$targetpoint.weirdness());
        double d0 = TropicraftTerrainShaper.peaksAndValleys(f4);
        TerrainShaper.Point point = TerrainShaper.makePoint(f, f1, f4);
        p_207895_.add(
                "Biome builder PV: " + OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(d0) +
                " C: " + DEBUG_BIOME_HOLDER.getDebugStringForContinentalness((double)f) +
                " E: " + DEBUG_BIOME_HOLDER.getDebugStringForErosion((double)f1) +
                " T: " + DEBUG_BIOME_HOLDER.getDebugStringForTemperature((double)f2) +
                " H: " + DEBUG_BIOME_HOLDER.getDebugStringForHumidity((double)f3));
        p_207895_.add("TerrainData O: " + DEBUG_DECIMAL_FORMAT.format(DEBUG_TERRAIN_HOLDER.offset(point))
                + " F: " +  DEBUG_DECIMAL_FORMAT.format(DEBUG_TERRAIN_HOLDER.factor(point))
                + " J: " +  DEBUG_DECIMAL_FORMAT.format(DEBUG_TERRAIN_HOLDER.jaggedness(point)));
    }
}
