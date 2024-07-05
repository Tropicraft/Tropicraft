package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TropicraftBiomeBuilder {
    private static final MultiNoiseBiomeSourceParameterList.Preset PRESET = registerMultiNoisePreset(TropicraftDimension.ID, new MultiNoiseBiomeSourceParameterList.Preset.SourceProvider() {
        @Override
        public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> function) {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> points = ImmutableList.builder();
            new TropicraftBiomeBuilder().addBiomes((point, key) -> points.add(Pair.of(point, function.apply(key))));
            return new Climate.ParameterList<>(points.build());
        }
    });

    public static final ResourceKey<MultiNoiseBiomeSourceParameterList> PARAMETER_LIST = ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, TropicraftDimension.ID);

    private final Climate.Parameter islandContinentalness = Climate.Parameter.span(-1.1F, -0.92F);
    private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.92F, -0.19F);
    private final Climate.Parameter landContinentalness = Climate.Parameter.span(-0.1F, 1.0F);

    private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.1F);
    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.1F, 0.03F);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
    private final Climate.Parameter fullRange = Climate.Parameter.span(-1.0F, 1.0F);

    // 0 is the coldest, 4 is the hottest
    private final Climate.Parameter[] temperatures = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.45F),
            Climate.Parameter.span(-0.45F, -0.15F),
            Climate.Parameter.span(-0.15F, 0.2F),
            Climate.Parameter.span(0.2F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    // 0 is the driest, 4 is the wettest
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.35F),
            Climate.Parameter.span(-0.35F, -0.1F),
            Climate.Parameter.span(-0.1F, 0.1F),
            Climate.Parameter.span(0.1F, 0.3F),
            Climate.Parameter.span(0.3F, 1.0F)
    };

    // 0 is the most shattered, 6 is the most flat
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.78F),
            Climate.Parameter.span(-0.78F, -0.375F),
            Climate.Parameter.span(-0.375F, -0.2225F),
            Climate.Parameter.span(-0.2225F, 0.05F),
            Climate.Parameter.span(0.05F, 0.45F),
            Climate.Parameter.span(0.45F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    private final Climate.Parameter wet = Climate.Parameter.span(humidities[2], humidities[4]);
    private final Climate.Parameter lessWet = Climate.Parameter.span(humidities[0], humidities[1]);
    private final Climate.Parameter mostWet = Climate.Parameter.span(humidities[3], humidities[4]);

    private static MultiNoiseBiomeSourceParameterList.Preset registerMultiNoisePreset(ResourceLocation id, MultiNoiseBiomeSourceParameterList.Preset.SourceProvider sourceProvider) {
        MultiNoiseBiomeSourceParameterList.Preset preset = new MultiNoiseBiomeSourceParameterList.Preset(id, sourceProvider);
        MultiNoiseBiomeSourceParameterList.Preset.BY_NAME = Util.copyAndPut(
                MultiNoiseBiomeSourceParameterList.Preset.BY_NAME,
                id, preset
        );
        return preset;
    }

    public static void bootstrap(final BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
        context.register(PARAMETER_LIST, new MultiNoiseBiomeSourceParameterList(PRESET, context.lookup(Registries.BIOME)));
    }

    public void addBiomes(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer) {
        addInlandBiomes(consumer);

        addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(lessWet, humidities[2]), oceanContinentalness, fullRange, fullRange, 0.0F, TropicraftBiomes.OCEAN);
        addSurfaceBiome(consumer, fullRange, mostWet, oceanContinentalness, fullRange, fullRange, 0.0F, TropicraftBiomes.KELP_FOREST);

        addSurfaceBiome(consumer, fullRange, fullRange, islandContinentalness, fullRange, fullRange, 0.0F, TropicraftBiomes.RAINFOREST);
    }

    private void addInlandBiomes(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer) {
        addMidSlice(consumer, Climate.Parameter.span(-1.0F, -0.93333334F));
        addHighSlice(consumer, Climate.Parameter.span(-0.93333334F, -0.7666667F));
        addPeaks(consumer, Climate.Parameter.span(-0.7666667F, -0.56666666F));
        addHighSlice(consumer, Climate.Parameter.span(-0.56666666F, -0.4F));
        addMidSlice(consumer, Climate.Parameter.span(-0.4F, -0.26666668F));
        addLowSlice(consumer, Climate.Parameter.span(-0.26666668F, -0.05F));
        addValleys(consumer, Climate.Parameter.span(-0.05F, 0.05F));
        addLowSlice(consumer, Climate.Parameter.span(0.05F, 0.26666668F));
        addMidSlice(consumer, Climate.Parameter.span(0.26666668F, 0.4F));
        addHighSlice(consumer, Climate.Parameter.span(0.4F, 0.56666666F));
        addPeaks(consumer, Climate.Parameter.span(0.56666666F, 0.7666667F));
        addHighSlice(consumer, Climate.Parameter.span(0.7666667F, 0.93333334F));
        addMidSlice(consumer, Climate.Parameter.span(0.93333334F, 1.0F));
    }

    private void addValleys(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer, Climate.Parameter weirdness) {
        addSurfaceBiome(consumer, fullRange, fullRange, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, TropicraftBiomes.RIVER);
        addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, erosions[6], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
        addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, erosions[4], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
        addSurfaceBiome(consumer, fullRange, fullRange, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, TropicraftBiomes.RIVER);
        addSurfaceBiome(consumer, fullRange, fullRange, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[5], erosions[5]), weirdness, 0.0F, TropicraftBiomes.RIVER);

        addSurfaceBiome(consumer, fullRange, fullRange, coastContinentalness, erosions[6], weirdness, 0.0F, TropicraftBiomes.RIVER);

        // Saddle valley plateau
        addSurfaceBiome(consumer, fullRange, lessWet, farInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, TropicraftBiomes.TROPICS);
    }

    private void addLowSlice(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer, Climate.Parameter weirdness) {
        addSurfaceBiome(consumer, fullRange, fullRange, coastContinentalness, Climate.Parameter.span(erosions[4], erosions[6]), weirdness, 0.0F, TropicraftBiomes.BEACH);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, erosions[6], weirdness, 0.0F, TropicraftBiomes.OVERGROWN_MANGROVES);
            addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, erosions[4], weirdness, 0.0F, TropicraftBiomes.OVERGROWN_MANGROVES);
        } else {
            addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, erosions[6], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
            addSurfaceBiome(consumer, fullRange, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, erosions[4], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
        }

        addSurfaceBiome(consumer, fullRange, fullRange, landContinentalness, erosions[3], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[3], weirdness, 0.0F, TropicraftBiomes.OSA_RAINFOREST);
        } else {
            addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        }
        addSurfaceBiome(consumer, fullRange, lessWet, landContinentalness, erosions[2], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[1], weirdness, 0.0F, TropicraftBiomes.OSA_RAINFOREST);
            addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[0], weirdness, 0.0F, TropicraftBiomes.OSA_RAINFOREST);
        } else {
            addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        }
    }

    private void addMidSlice(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer, Climate.Parameter weirdness) {
        addSurfaceBiome(consumer, fullRange, fullRange, coastContinentalness, Climate.Parameter.span(erosions[4], erosions[6]), weirdness, 0.0F, TropicraftBiomes.BEACH);

        addSurfaceBiome(consumer, fullRange, fullRange, landContinentalness, erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);

        addSurfaceBiome(consumer, fullRange, wet, landContinentalness, erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        addSurfaceBiome(consumer, fullRange, lessWet, landContinentalness, erosions[3], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        addSurfaceBiome(consumer, fullRange, fullRange, landContinentalness, erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        addSurfaceBiome(consumer, fullRange, fullRange, landContinentalness, erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        addSurfaceBiome(consumer, fullRange, fullRange, landContinentalness, erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
    }

    private void addHighSlice(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer, Climate.Parameter weirdness) {
        Climate.Parameter coastInwards = Climate.Parameter.span(coastContinentalness, farInlandContinentalness);
        Climate.Parameter midInwards = Climate.Parameter.span(coastContinentalness, farInlandContinentalness);

        addSurfaceBiome(consumer, fullRange, fullRange, coastInwards, erosions[6], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, wet, coastInwards, erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            addSurfaceBiome(consumer, fullRange, wet, coastInwards, erosions[5], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }

        addSurfaceBiome(consumer, fullRange, lessWet, coastInwards, erosions[5], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        addSurfaceBiome(consumer, fullRange, fullRange, coastInwards, erosions[4], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[3], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[2], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[1], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[0], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }
    }

    private void addPeaks(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer, Climate.Parameter weirdness) {
        Climate.Parameter coastInwards = Climate.Parameter.span(coastContinentalness, farInlandContinentalness);
        Climate.Parameter midInwards = Climate.Parameter.span(coastContinentalness, farInlandContinentalness);

        addSurfaceBiome(consumer, fullRange, fullRange, coastInwards, erosions[6], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, wet, coastInwards, erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            addSurfaceBiome(consumer, fullRange, wet, coastInwards, erosions[5], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }

        addSurfaceBiome(consumer, fullRange, lessWet, coastInwards, erosions[5], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        addSurfaceBiome(consumer, fullRange, fullRange, coastInwards, erosions[4], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0) {
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[3], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            addSurfaceBiome(consumer, fullRange, fullRange, midInwards, erosions[2], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }

        addSurfaceBiome(consumer, fullRange, fullRange, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0F, TropicraftBiomes.TROPICAL_PEAKS);
        addSurfaceBiome(consumer, fullRange, fullRange, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0F, TropicraftBiomes.TROPICAL_PEAKS);
    }

    private void addSurfaceBiome(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        // Depth 0
        consumer.accept(Climate.parameters(
                        temperature,
                        humidity,
                        continentalness,
                        erosion,
                        Climate.Parameter.point(0.0F),
                        weirdness,
                        offset),
                biome
        );

        // Depth 1
        consumer.accept(Climate.parameters(
                        temperature,
                        humidity,
                        continentalness,
                        erosion,
                        Climate.Parameter.point(1.0F),
                        weirdness,
                        offset),
                biome
        );
    }

    public String getDebugStringForContinentalness(double continentalness) {
        double quantized = Climate.quantizeCoord((float) continentalness);
        if (quantized < islandContinentalness.max()) {
            return "Islands";
        } else if (quantized < oceanContinentalness.max()) {
            return "Ocean";
        } else if (quantized < coastContinentalness.max()) {
            return "Coast";
        } else if (quantized < nearInlandContinentalness.max()) {
            return "Near inland";
        } else if (quantized < midInlandContinentalness.max()) {
            return "Mid inland";
        } else {
            return "Far inland";
        }
    }

    public String getDebugStringForErosion(double erosion) {
        return getDebugStringForNoiseValue(erosion, erosions);
    }

    public String getDebugStringForTemperature(double temperature) {
        return getDebugStringForNoiseValue(temperature, temperatures);
    }

    public String getDebugStringForHumidity(double humidity) {
        return getDebugStringForNoiseValue(humidity, humidities);
    }

    private static String getDebugStringForNoiseValue(double value, Climate.Parameter[] parameters) {
        double quantizedCoord = Climate.quantizeCoord((float) value);
        for (int i = 0; i < parameters.length; ++i) {
            if (quantizedCoord < parameters[i].max()) {
                return String.valueOf(i);
            }
        }
        return "?";
    }
}
