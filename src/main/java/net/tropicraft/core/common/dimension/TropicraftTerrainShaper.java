package net.tropicraft.core.common.dimension;

import net.minecraft.util.CubicSpline;
import net.minecraft.util.Mth;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.level.biome.TerrainShaper;

public final class TropicraftTerrainShaper {
    private static final ToFloatFunction<Float> NO_TRANSFORM = value -> value;

    public static TerrainShaper tropics() {
        ToFloatFunction<Float> offsetTransform = offset -> offset + 0.5f;

        CubicSpline<TerrainShaper.Point> nearInlandDepth = buildErosionOffsetSpline(-0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, offsetTransform);
        CubicSpline<TerrainShaper.Point> midInlandDepth = buildErosionOffsetSpline(-0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, offsetTransform);
        CubicSpline<TerrainShaper.Point> farInlandDepth = buildErosionOffsetSpline(-0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, offsetTransform);
        CubicSpline<TerrainShaper.Point> peaksDepth = buildErosionOffsetSpline(-0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, offsetTransform);

        CubicSpline<TerrainShaper.Point> lagoonSpline = buildLagoonOffset(offsetTransform);

        // Depth sampler
        CubicSpline<TerrainShaper.Point> offsetSampler = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS, offsetTransform)
                .addPoint(-1.1F, 0.140F, 0.0F)
                .addPoint(-0.92F, -0.2222F, 0.0F)
                .addPoint(-0.41F, -0.2222F, 0.0F)
                .addPoint(-0.34F, -0.12F, 0.0F)
                .addPoint(-0.22F, lagoonSpline, 0.0F)
                .addPoint(-0.18F, lagoonSpline, 0.0F)
                .addPoint(-0.16F, nearInlandDepth, 0.0F)
                .addPoint(-0.15F, nearInlandDepth, 0.0F)
                .addPoint(-0.1F, midInlandDepth, 0.0F)
                .addPoint(0.25F, farInlandDepth, 0.0F)
                .addPoint(1.0F, peaksDepth, 0.0F).build();

        final float beachFactorStrength = 6.25f;
        final float nearinlandFactorStrength = 4.22f; // 5.47
        final float midInlandFactorStrength = 3.8f; // Was 5.08
        final float furtherInlandFactorStrength = 2.44f; // Was 4.69

        // Scale sampler
        CubicSpline<TerrainShaper.Point> factorSampler = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS, NO_TRANSFORM)
                .addPoint(-1.1F, 0.54F, 0.0F)
                .addPoint(-0.92F, 3.95F, 0.0F)
                .addPoint(-0.19F, 3.95F, 0.0F)
                .addPoint(-0.15F, getErosionFactor(beachFactorStrength, true, NO_TRANSFORM), 0.0F)
                .addPoint(-0.1F, getErosionFactor(nearinlandFactorStrength, true, NO_TRANSFORM), 0.0F)
                .addPoint(0.03F, getErosionFactor(midInlandFactorStrength, true, NO_TRANSFORM), 0.0F)
                .addPoint(0.06F, getErosionFactor(furtherInlandFactorStrength, false, NO_TRANSFORM), 0.0F)
                .build();

        float f4 = 0.65F;
        CubicSpline<TerrainShaper.Point> jaggednessSampler = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS, NO_TRANSFORM)
                .addPoint(-0.11F, 0.0F, 0.0F)
                .addPoint(0.03F, buildErosionJaggednessSpline(1.0F, 0.5F, 0.0F, 0.0F, NO_TRANSFORM), 0.0F)
                .addPoint(0.65F, buildErosionJaggednessSpline(1.0F, 1.0F, 1.0F, 0.0F, NO_TRANSFORM), 0.0F).
                build();
        return new TerrainShaper(offsetSampler, factorSampler, jaggednessSampler);
    }

    public static float peaksAndValleys(float p_187266_) {
        return -(Math.abs(Math.abs(p_187266_) - 0.6666667F) - 0.33333334F) * 3.0F;
    }

    private static CubicSpline<TerrainShaper.Point> buildErosionJaggednessSpline(float p_187295_, float p_187296_, float p_187297_, float p_187298_, ToFloatFunction<Float> p_187299_) {
        float f = -0.5775F;
        CubicSpline<TerrainShaper.Point> cubicspline = buildRidgeJaggednessSpline(p_187295_, p_187297_, p_187299_);
        CubicSpline<TerrainShaper.Point> cubicspline1 = buildRidgeJaggednessSpline(p_187296_, p_187298_, p_187299_);
        return CubicSpline.builder(TerrainShaper.Coordinate.EROSION, p_187299_).addPoint(-1.0F, cubicspline, 0.0F)
                .addPoint(-0.78F, cubicspline1, 0.0F)
                .addPoint(-0.5775F, cubicspline1, 0.0F)
                .addPoint(-0.375F, 0.0F, 0.0F).build();
    }

    private static CubicSpline<TerrainShaper.Point> buildRidgeJaggednessSpline(float p_187301_, float p_187302_, ToFloatFunction<Float> p_187303_) {
        float f = peaksAndValleys(0.4F);
        float f1 = peaksAndValleys(0.56666666F);

        float f2 = (f + f1) / 2.0F;
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187303_);
        builder.addPoint(f, 0.0F, 0.0F);
        if (p_187302_ > 0.0F) {
            builder.addPoint(f2, buildWeirdnessJaggednessSpline(p_187302_, p_187303_), 0.0F);
        } else {
            builder.addPoint(f2, 0.0F, 0.0F);
        }

        if (p_187301_ > 0.0F) {
            builder.addPoint(1.0F, buildWeirdnessJaggednessSpline(p_187301_, p_187303_), 0.0F);
        } else {
            builder.addPoint(1.0F, 0.0F, 0.0F);
        }

        return builder.build();
    }

    private static CubicSpline<TerrainShaper.Point> buildWeirdnessJaggednessSpline(float p_187305_, ToFloatFunction<Float> p_187306_) {
        float f = 0.63F * p_187305_;
        float f1 = 0.3F * p_187305_;
        return CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, p_187306_)
                .addPoint(-0.01F, f, 0.0F)
                .addPoint(0.01F, f1, 0.0F)
                .build();
    }

    private static CubicSpline<TerrainShaper.Point> getErosionFactor(float strengthForContinentalness, boolean coastal, ToFloatFunction<Float> p_187310_) {
        final float defaultScale = 3.1f; // Was 6.3
        // More hilly, shattered terrain
        final float roughScale = 2.23f; // Was 2.67

        CubicSpline<TerrainShaper.Point> defaultPVScaleSpline = CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, p_187310_)
                .addPoint(-0.2F, defaultScale, 0.0F)
                .addPoint(0.2F, strengthForContinentalness, 0.0F)
                .build();

        CubicSpline.Builder<TerrainShaper.Point> erosionSplineBuilder = CubicSpline.builder(TerrainShaper.Coordinate.EROSION, p_187310_)
                .addPoint(-0.6F, defaultPVScaleSpline, 0.0F)
                .addPoint(-0.5F, CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, p_187310_)
                        .addPoint(-0.05F, defaultScale, 0.0F)
                        .addPoint(0.05F, roughScale, 0.0F)
                        .build(), 0.0F)
                .addPoint(-0.35F, defaultPVScaleSpline, 0.0F)
                .addPoint(-0.25F, defaultPVScaleSpline, 0.0F)
                .addPoint(-0.1F, CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, p_187310_)
                        .addPoint(-0.05F, roughScale, 0.0F)
                        .addPoint(0.05F, defaultScale, 0.0F).build(), 0.0F)
                .addPoint(0.03F, defaultPVScaleSpline, 0.0F);

        // Extreme areas

        if (coastal) {
            CubicSpline<TerrainShaper.Point> coastalWeirdnessForShattered = CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, p_187310_)
                    .addPoint(0.0F, strengthForContinentalness, 0.0F)
                    .addPoint(0.1F, 0.625F, 0.0F)
                    .build();

            CubicSpline<TerrainShaper.Point> coastalRidgeForShattered = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187310_)
                    .addPoint(-0.9F, strengthForContinentalness, 0.0F)
                    .addPoint(-0.69F, coastalWeirdnessForShattered, 0.0F)
                    .build();

            erosionSplineBuilder.addPoint(0.35F, strengthForContinentalness, 0.0F)
                    .addPoint(0.45F, coastalRidgeForShattered, 0.0F)
                    .addPoint(0.55F, coastalRidgeForShattered, 0.0F)
                    .addPoint(0.62F, strengthForContinentalness, 0.0F);
        } else {
            CubicSpline<TerrainShaper.Point> ridgeLowShattered = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187310_)
                    .addPoint(-0.7F, defaultPVScaleSpline, 0.0F)
                    .addPoint(-0.15F, 1.37F, 0.0F)
                    .build();

            CubicSpline<TerrainShaper.Point> ridgeHighShattered = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187310_)
                    // Was 0.45
                    .addPoint(0.35F, defaultPVScaleSpline, 0.0F)
                    .addPoint(0.7F, 1.56F, 0.0F)
                    .build();

            erosionSplineBuilder
                    .addPoint(0.05F, ridgeHighShattered, 0.0F)
                    .addPoint(0.4F, ridgeHighShattered, 0.0F)
                    .addPoint(0.45F, ridgeLowShattered, 0.0F)
                    .addPoint(0.55F, ridgeLowShattered, 0.0F)
                    .addPoint(0.58F, strengthForContinentalness, 0.0F);
        }

        return erosionSplineBuilder.build();
    }

    private static CubicSpline<TerrainShaper.Point> buildLagoonOffset(ToFloatFunction<Float> offsetTransform) {
        return CubicSpline.builder(TerrainShaper.Coordinate.EROSION, offsetTransform)
                .addPoint(-1.0f, -0.12F, 0)
                .addPoint(0.4f, -0.06F, 0)
                .addPoint(1.0f, -0.01F, 0)
                .build();
    }

    private static CubicSpline<TerrainShaper.Point> buildMountainRidgeSplineWithPoints(float p_187331_, boolean p_187332_, ToFloatFunction<Float> p_187333_) {
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187333_);
        float f = -0.7F;
        float f1 = -1.0F;
        float f2 = mountainContinentalness(-1.0F, p_187331_, -0.7F);
        float f3 = 1.0F;
        float f4 = mountainContinentalness(1.0F, p_187331_, -0.7F);
        float f5 = calculateMountainRidgeZeroContinentalnessPoint(p_187331_);
        float f6 = -0.65F;
        if (-0.65F < f5 && f5 < 1.0F) {
            float f14 = mountainContinentalness(-0.65F, p_187331_, -0.7F);
            float f8 = -0.75F;
            float f9 = mountainContinentalness(-0.75F, p_187331_, -0.7F);
            float f10 = calculateSlope(f2, f9, -1.0F, -0.75F);
            builder.addPoint(-1.0F, f2, f10);
            builder.addPoint(-0.75F, f9, 0.0F);
            builder.addPoint(-0.65F, f14, 0.0F);
            float f11 = mountainContinentalness(f5, p_187331_, -0.7F);
            float f12 = calculateSlope(f11, f4, f5, 1.0F);
            float f13 = 0.01F;
            builder.addPoint(f5 - 0.01F, f11, 0.0F);
            builder.addPoint(f5, f11, f12);
            builder.addPoint(1.0F, f4, f12);
        } else {
            float f7 = calculateSlope(f2, f4, -1.0F, 1.0F);
            if (p_187332_) {
                builder.addPoint(-1.0F, Math.max(0.2F, f2), 0.0F);
                builder.addPoint(0.0F, Mth.lerp(0.5F, f2, f4), f7);
            } else {
                builder.addPoint(-1.0F, f2, f7);
            }

            builder.addPoint(1.0F, f4, f7);
        }

        return builder.build();
    }

    private static float calculateSlope(float p_187272_, float p_187273_, float p_187274_, float p_187275_) {
        return (p_187273_ - p_187272_) / (p_187275_ - p_187274_);
    }

    private static float mountainContinentalness(float p_187327_, float p_187328_, float p_187329_) {
        float f = 1.17F;
        float f1 = 0.46082947F;
        float f2 = 1.0F - (1.0F - p_187328_) * 0.5F;
        float f3 = 0.5F * (1.0F - p_187328_);
        float f4 = (p_187327_ + 1.17F) * 0.46082947F;
        float f5 = f4 * f2 - f3;
        return p_187327_ < p_187329_ ? Math.max(f5, -0.2222F) : Math.max(f5, 0.0F);
    }

    private static float calculateMountainRidgeZeroContinentalnessPoint(float p_187344_) {
        float f = 1.17F;
        float f1 = 0.46082947F;
        float f2 = 1.0F - (1.0F - p_187344_) * 0.5F;
        float f3 = 0.5F * (1.0F - p_187344_);
        return f3 / (0.46082947F * f2) - 1.17F;
    }

    private static CubicSpline<TerrainShaper.Point> buildErosionOffsetSpline(float p_187285_, float p_187286_, float p_187287_, float p_187288_, float p_187289_, float p_187290_, boolean p_187291_, boolean p_187292_, ToFloatFunction<Float> p_187293_) {
        float f = 0.6F;
        float f1 = 0.5F;
        float f2 = 0.5F;
        CubicSpline<TerrainShaper.Point> cubicspline = buildMountainRidgeSplineWithPoints(Mth.lerp(p_187288_, 0.6F, 1.5F), p_187292_, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline1 = buildMountainRidgeSplineWithPoints(Mth.lerp(p_187288_, 0.6F, 1.0F), p_187292_, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline2 = buildMountainRidgeSplineWithPoints(p_187288_, p_187292_, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline3 = ridgeSpline(p_187285_ - 0.15F, 0.5F * p_187288_, Mth.lerp(0.5F, 0.5F, 0.5F) * p_187288_, 0.5F * p_187288_, 0.6F * p_187288_, 0.5F, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline4 = ridgeSpline(p_187285_, p_187289_ * p_187288_, p_187286_ * p_187288_, 0.5F * p_187288_, 0.6F * p_187288_, 0.5F, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline5 = ridgeSpline(p_187285_, p_187289_, p_187289_, p_187286_, p_187287_, 0.5F, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline6 = ridgeSpline(p_187285_, p_187289_, p_187289_, p_187286_, p_187287_, 0.5F, p_187293_);
        CubicSpline<TerrainShaper.Point> cubicspline7 = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187293_).addPoint(-1.0F, p_187285_, 0.0F).addPoint(-0.4F, cubicspline5, 0.0F).addPoint(0.0F, p_187287_ + 0.07F, 0.0F).build();
        CubicSpline<TerrainShaper.Point> cubicspline8 = ridgeSpline(-0.02F, p_187290_, p_187290_, p_187286_, p_187287_, 0.0F, p_187293_);
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.EROSION, p_187293_).addPoint(-0.85F, cubicspline, 0.0F).addPoint(-0.7F, cubicspline1, 0.0F).addPoint(-0.4F, cubicspline2, 0.0F).addPoint(-0.35F, cubicspline3, 0.0F).addPoint(-0.1F, cubicspline4, 0.0F).addPoint(0.2F, cubicspline5, 0.0F);
        if (p_187291_) {
            builder.addPoint(0.4F, cubicspline6, 0.0F).addPoint(0.45F, cubicspline7, 0.0F).addPoint(0.55F, cubicspline7, 0.0F).addPoint(0.58F, cubicspline6, 0.0F);
        }

        builder.addPoint(0.7F, cubicspline8, 0.0F);
        return builder.build();
    }

    private static CubicSpline<TerrainShaper.Point> ridgeSpline(float p_187277_, float p_187278_, float p_187279_, float p_187280_, float p_187281_, float p_187282_, ToFloatFunction<Float> p_187283_) {
        float f = Math.max(0.5F * (p_187278_ - p_187277_), p_187282_);
        float f1 = 5.0F * (p_187279_ - p_187278_);
        return CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, p_187283_).addPoint(-1.0F, p_187277_, f).addPoint(-0.4F, p_187278_, Math.min(f, f1)).addPoint(0.0F, p_187279_, f1).addPoint(0.4F, p_187280_, 2.0F * (p_187280_ - p_187279_)).addPoint(1.0F, p_187281_, 0.7F * (p_187281_ - p_187280_)).build();
    }
}
