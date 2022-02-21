package net.tropicraft.core.common.dimension.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

public class TropicraftBiomeBuilder {
    private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-1.05F, -0.19F);
    private final Climate.Parameter landContinentalness = Climate.Parameter.span(-0.05F, 1.0F);

    private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.05F);
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.05F, 0.55F);
    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.05F, 0.03F);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    private final Climate.Parameter[] temperatures = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.45F), Climate.Parameter.span(-0.45F, -0.15F), Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.2F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.35F), Climate.Parameter.span(-0.35F, -0.1F), Climate.Parameter.span(-0.1F, 0.1F), Climate.Parameter.span(0.1F, 0.3F), Climate.Parameter.span(0.3F, 1.0F)};
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.78F), Climate.Parameter.span(-0.78F, -0.375F), Climate.Parameter.span(-0.375F, -0.2225F), Climate.Parameter.span(-0.2225F, 0.05F), Climate.Parameter.span(0.05F, 0.45F), Climate.Parameter.span(0.45F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
    private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);

    public void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        addInlandBiomes(consumer);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, TropicraftBiomes.TROPICS_OCEAN);
    }

    private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
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

    private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, TropicraftBiomes.TROPICS_RIVER);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, TropicraftBiomes.TROPICS_RIVER);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.TROPICS_RIVER);
    }

    private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.TROPICS_BEACH);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.TROPICS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.TROPICS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
    }

    private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.TROPICS_BEACH);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.TROPICS_BEACH);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.TROPICS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.TROPICS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.landContinentalness, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
    }

    private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        Climate.Parameter coastInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);
        Climate.Parameter midInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);

        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.TROPICS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
    }

    private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        Climate.Parameter coastInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);
        Climate.Parameter midInwards = Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness);


        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[6], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[5], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, coastInwards, this.erosions[4], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[3], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[2], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[1], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, midInwards, this.erosions[0], weirdness, 0.0F, TropicraftBiomes.RAINFOREST_PLAINS);
    }

    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter p_187182_, Climate.Parameter p_187183_, Climate.Parameter p_187184_, Climate.Parameter p_187185_, Climate.Parameter p_187186_, float p_187187_, ResourceKey<Biome> p_187188_) {
        // Depth 0
        consumer.accept(Pair.of(Climate.parameters(
                p_187182_,
                p_187183_,
                p_187184_,
                p_187185_,
                Climate.Parameter.point(0.0F),
                p_187186_,
                p_187187_),
                p_187188_)
        );

        // Depth 1
        consumer.accept(Pair.of(Climate.parameters(
                p_187182_,
                p_187183_,
                p_187184_,
                p_187185_,
                Climate.Parameter.point(1.0F),
                p_187186_,
                p_187187_),
                p_187188_)
        );
    }

    private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter p_187202_, Climate.Parameter p_187203_, Climate.Parameter p_187204_, Climate.Parameter p_187205_, Climate.Parameter p_187206_, float p_187207_, ResourceKey<Biome> p_187208_) {
        consumer.accept(Pair.of(Climate.parameters(
                p_187202_,
                p_187203_,
                p_187204_,
                p_187205_,
                Climate.Parameter.span(0.2F, 0.9F),
                p_187206_,
                p_187207_),
                p_187208_)
        );
    }
}
