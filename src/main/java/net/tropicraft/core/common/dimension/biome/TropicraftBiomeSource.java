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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftTerrainProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TropicraftBiomeSource extends BiomeSource {
    public static final Codec<TropicraftBiomeSource> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(b -> b.biomes)
    ).apply(i, i.stable(TropicraftBiomeSource::new)));

    private static final TropicraftBiomeBuilder DEBUG_BIOME_HOLDER = new TropicraftBiomeBuilder();

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

    private final Registry<Biome> biomes;
    private final Climate.ParameterList<Holder<Biome>> parameters;

    public TropicraftBiomeSource(Registry<Biome> biomes) {
        super(POSSIBLE_BIOMES.stream().map(biomes::getHolderOrThrow));

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
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return this.parameters.findValue(sampler.sample(x, y, z));
    }

    @Override
    public void addDebugInfo(List<String> result, BlockPos pos, Climate.Sampler sampler) {
        Climate.TargetPoint climate = sampler.sample(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()));
        float continentalness = Climate.unquantizeCoord(climate.continentalness());
        float erosion = Climate.unquantizeCoord(climate.erosion());
        float temperature = Climate.unquantizeCoord(climate.temperature());
        float humidity = Climate.unquantizeCoord(climate.humidity());
        float weirdness = Climate.unquantizeCoord(climate.weirdness());
        double peaksAndValleys = TropicraftTerrainProvider.peaksAndValleys(weirdness);
        result.add(
                "Biome builder PV: " + OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(peaksAndValleys) +
                        " C: " + DEBUG_BIOME_HOLDER.getDebugStringForContinentalness(continentalness) +
                        " E: " + DEBUG_BIOME_HOLDER.getDebugStringForErosion(erosion) +
                        " T: " + DEBUG_BIOME_HOLDER.getDebugStringForTemperature(temperature) +
                        " H: " + DEBUG_BIOME_HOLDER.getDebugStringForHumidity(humidity));
    }
}
