package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;

public class TropicraftBiomeBuilder {
    private final Climate.Parameter islandContinentalness = Climate.Parameter.span(-1.1F, -0.92F);
    private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.92F, -0.19F);
    private final Climate.Parameter landContinentalness = Climate.Parameter.span(-0.1F, 1.0F);

    private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.1F);
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.05F, 0.55F);
    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.1F, 0.03F);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

    // 0 is the coldest, 4 is the hottest
    private final Climate.Parameter[] temperatures = new Climate.Parameter[] {
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
    private final Climate.Parameter[] erosions = new Climate.Parameter[] {
            Climate.Parameter.span(-1.0F, -0.78F),
            Climate.Parameter.span(-0.78F, -0.375F),
            Climate.Parameter.span(-0.375F, -0.2225F),
            Climate.Parameter.span(-0.2225F, 0.05F),
            Climate.Parameter.span(0.05F, 0.45F),
            Climate.Parameter.span(0.45F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    private final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
    private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);

    private final Climate.Parameter WET = Climate.Parameter.span(this.humidities[2], this.humidities[4]);
    private final Climate.Parameter LESS_WET = Climate.Parameter.span(this.humidities[0], this.humidities[1]);
    private final Climate.Parameter MED_WET = Climate.Parameter.span(this.humidities[2], this.humidities[3]);
    private final Climate.Parameter MOST_WET = Climate.Parameter.span(this.humidities[3], this.humidities[4]);

    public void addBiomes(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer) {
        addInlandBiomes(consumer);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(LESS_WET, this.humidities[2]), this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, TropicraftBiomes.OCEAN);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.MOST_WET, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, TropicraftBiomes.KELP_FOREST);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.islandContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, TropicraftBiomes.RAINFOREST);
    }

    private void addInlandBiomes(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer) {
        this.addMidSlice(consumer, Climate.Parameter.span(-1.0F, -0.93333334F));
        this.addHighSlice(consumer, Climate.Parameter.span(-0.93333334F, -0.7666667F));
        this.addPeaks(consumer, Climate.Parameter.span(-0.7666667F, -0.56666666F));
        this.addHighSlice(consumer, Climate.Parameter.span(-0.56666666F, -0.4F));
        this.addMidSlice(consumer, Climate.Parameter.span(-0.4F, -0.26666668F));
        this.addLowSlice(consumer, Climate.Parameter.span(-0.26666668F, -0.05F));
        this.addValleys(consumer, Climate.Parameter.span(-0.05F, 0.05F));
        this.addLowSlice(consumer, Climate.Parameter.span(0.05F, 0.26666668F));
        this.addMidSlice(consumer, Climate.Parameter.span(0.26666668F, 0.4F));
        this.addHighSlice(consumer, Climate.Parameter.span(0.4F, 0.56666666F));
        this.addPeaks(consumer, Climate.Parameter.span(0.56666666F, 0.7666667F));
        this.addHighSlice(consumer, Climate.Parameter.span(0.7666667F, 0.93333334F));
        this.addMidSlice(consumer, Climate.Parameter.span(0.93333334F, 1.0F));
    }

    private void addValleys(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter weirdness) {
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, TropicraftBiomes.RIVER);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(humidities[3], humidities[4]), landContinentalness, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, TropicraftBiomes.RIVER);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[5], this.erosions[5]), weirdness, 0.0F, TropicraftBiomes.RIVER);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.RIVER);

        // Saddle valley plateau
        this.addSurfaceBiome(consumer, this.FULL_RANGE, LESS_WET, farInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, TropicraftBiomes.TROPICS);
    }

    private void addLowSlice(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter weirdness) {
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[4], this.erosions[6]), weirdness, 0.0F, TropicraftBiomes.BEACH);

        if (weirdness.max() < 0) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(humidities[3], humidities[4]), this.landContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.OVERGROWN_MANGROVES);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(humidities[3], humidities[4]), this.landContinentalness, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.OVERGROWN_MANGROVES);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(humidities[3], humidities[4]), this.landContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(humidities[3], humidities[4]), this.landContinentalness, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.MANGROVES);
        }

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.WET, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.OSA_RAINFOREST);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.WET, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        }
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.LESS_WET, this.landContinentalness, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.WET, this.landContinentalness, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.OSA_RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.WET, this.landContinentalness, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.OSA_RAINFOREST);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.WET, this.landContinentalness, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.WET, this.landContinentalness, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        }
    }

    private void addMidSlice(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter weirdness) {
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[4], this.erosions[6]), weirdness, 0.0F, TropicraftBiomes.BEACH);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, WET, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, LESS_WET, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
    }

    private void addHighSlice(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter weirdness) {
        Climate.Parameter coastInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);
        Climate.Parameter midInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);

        if (weirdness.max() < 0L) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, WET, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, WET, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }

        this.addSurfaceBiome(consumer, this.FULL_RANGE, LESS_WET, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0L) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }
    }

    private void addPeaks(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter weirdness) {
        Climate.Parameter coastInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);
        Climate.Parameter midInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);


        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);

        if (weirdness.max() < 0L) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, WET, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, WET, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }

        this.addSurfaceBiome(consumer, this.FULL_RANGE, LESS_WET, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.TROPICS);

        if (weirdness.max() < 0L) {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST);
        } else {
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
            this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.BAMBOO_RAINFOREST);
        }

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, TropicraftBiomes.TROPICAL_PEAKS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, TropicraftBiomes.TROPICAL_PEAKS);
    }

    private void addSurfaceBiome(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter p_187182_, Climate.Parameter p_187183_, Climate.Parameter p_187184_, Climate.Parameter p_187185_, Climate.Parameter p_187186_, float p_187187_, RegistryObject<Biome> p_187188_) {
        // Depth 0
        consumer.accept(Climate.parameters(
                p_187182_,
                p_187183_,
                p_187184_,
                p_187185_,
                Climate.Parameter.point(0.0F),
                p_187186_,
                p_187187_),
                p_187188_
        );

        // Depth 1
        consumer.accept(Climate.parameters(
                p_187182_,
                p_187183_,
                p_187184_,
                p_187185_,
                Climate.Parameter.point(1.0F),
                p_187186_,
                p_187187_),
                p_187188_
        );
    }

    private void addUndergroundBiome(BiConsumer<Climate.ParameterPoint, RegistryObject<Biome>> consumer, Climate.Parameter p_187202_, Climate.Parameter p_187203_, Climate.Parameter p_187204_, Climate.Parameter p_187205_, Climate.Parameter p_187206_, float p_187207_, RegistryObject<Biome> p_187208_) {
        consumer.accept(Climate.parameters(
                p_187202_,
                p_187203_,
                p_187204_,
                p_187205_,
                Climate.Parameter.span(0.2F, 0.9F),
                p_187206_,
                p_187207_),
                p_187208_
        );
    }

    public String getDebugStringForContinentalness(double p_187190_) {
        double d0 = (double)Climate.quantizeCoord((float)p_187190_);
        if (d0 < (double)this.islandContinentalness.max()) {
            return "Islands";
        } else if (d0 < (double)this.oceanContinentalness.max()) {
            return "Ocean";
        } else if (d0 < (double)this.coastContinentalness.max()) {
            return "Coast";
        } else if (d0 < (double)this.nearInlandContinentalness.max()) {
            return "Near inland";
        } else {
            return d0 < (double)this.midInlandContinentalness.max() ? "Mid inland" : "Far inland";
        }
    }

    public String getDebugStringForErosion(double p_187210_) {
        return getDebugStringForNoiseValue(p_187210_, this.erosions);
    }

    public String getDebugStringForTemperature(double p_187221_) {
        return getDebugStringForNoiseValue(p_187221_, this.temperatures);
    }

    public String getDebugStringForHumidity(double p_187232_) {
        return getDebugStringForNoiseValue(p_187232_, this.humidities);
    }

    private static String getDebugStringForNoiseValue(double p_187158_, Climate.Parameter[] p_187159_) {
        double d0 = (double)Climate.quantizeCoord((float)p_187158_);

        for(int i = 0; i < p_187159_.length; ++i) {
            if (d0 < (double)p_187159_[i].max()) {
                return "" + i;
            }
        }

        return "?";
    }
}
