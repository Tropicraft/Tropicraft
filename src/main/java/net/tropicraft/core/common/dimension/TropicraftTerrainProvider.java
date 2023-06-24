package net.tropicraft.core.common.dimension;

import net.minecraft.util.CubicSpline;
import net.minecraft.util.Mth;
import net.minecraft.util.ToFloatFunction;

public final class TropicraftTerrainProvider {
    private static final ToFloatFunction<Float> NO_TRANSFORM = ToFloatFunction.IDENTITY;

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> offset(I continents, I erosion, I ridges) {
        ToFloatFunction<Float> offsetTransform = ToFloatFunction.createUnlimited(offset -> offset + 0.5f);

        CubicSpline<C, I> nearInlandDepth = buildErosionOffsetSpline(erosion, ridges, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, offsetTransform);
        CubicSpline<C, I> midInlandDepth = buildErosionOffsetSpline(erosion, ridges, -0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, offsetTransform);
        CubicSpline<C, I> farInlandDepth = buildErosionOffsetSpline(erosion, ridges, -0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, offsetTransform);
        CubicSpline<C, I> peaksDepth = buildErosionOffsetSpline(erosion, ridges, -0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, offsetTransform);

        CubicSpline<C, I> lagoonSpline = buildLagoonOffset(erosion, offsetTransform);

        // Depth sampler
        return CubicSpline.builder(continents, offsetTransform)
                .addPoint(-1.1F, 0.140F)
                .addPoint(-0.92F, -0.2222F)
                .addPoint(-0.41F, -0.2222F)
                .addPoint(-0.34F, -0.12F)
                .addPoint(-0.22F, lagoonSpline)
                .addPoint(-0.18F, lagoonSpline)
                .addPoint(-0.16F, nearInlandDepth)
                .addPoint(-0.15F, nearInlandDepth)
                .addPoint(-0.1F, midInlandDepth)
                .addPoint(0.25F, farInlandDepth)
                .addPoint(1.0F, peaksDepth)
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> factor(I continents, I erosion, I weirdness, I ridges) {
        final float beachFactorStrength = 6.25f;
        final float nearinlandFactorStrength = 4.22f; // 5.47
        final float midInlandFactorStrength = 3.8f; // Was 5.08
        final float furtherInlandFactorStrength = 2.44f; // Was 4.69

        // Scale sampler
        return CubicSpline.builder(continents, NO_TRANSFORM)
                .addPoint(-1.1F, 0.54F)
                .addPoint(-0.92F, 3.95F)
                .addPoint(-0.19F, 3.95F)
                .addPoint(-0.15F, getErosionFactor(weirdness, erosion, ridges, beachFactorStrength, true, NO_TRANSFORM))
                .addPoint(-0.1F, getErosionFactor(weirdness, erosion, ridges, nearinlandFactorStrength, true, NO_TRANSFORM))
                .addPoint(0.03F, getErosionFactor(weirdness, erosion, ridges, midInlandFactorStrength, true, NO_TRANSFORM))
                .addPoint(0.06F, getErosionFactor(weirdness, erosion, ridges, furtherInlandFactorStrength, false, NO_TRANSFORM))
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> jaggedness(I continents, I erosion, I weirdness, I ridges) {
        float jaggednessMax = 0.65F;
        return CubicSpline.builder(continents, NO_TRANSFORM)
                .addPoint(-0.11F, 0.0F)
                .addPoint(0.03F, buildErosionJaggednessSpline(weirdness, erosion, ridges, 1.0F, 0.5F, 0.0F, 0.0F, NO_TRANSFORM))
                .addPoint(jaggednessMax, buildErosionJaggednessSpline(weirdness, erosion, ridges, 1.0F, 1.0F, 1.0F, 0.0F, NO_TRANSFORM))
                .build();
    }

    // Desmos: -\left(\left|\left|x\right|-\frac{2}{3}\right|-\frac{1}{3}\right)\cdot3
    public static float peaksAndValleys(float weirdness) {
        return -(Math.abs(Math.abs(weirdness) - 0.6666667F) - 0.33333334F) * 3.0F;
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionJaggednessSpline(I weirdness, I erosion, I ridges, float p_187295_, float p_187296_, float p_187297_, float p_187298_, ToFloatFunction<Float> p_187299_) {
        float f = -0.5775F;
        CubicSpline<C, I> cubicspline = buildRidgeJaggednessSpline(weirdness, ridges, p_187295_, p_187297_, p_187299_);
        CubicSpline<C, I> cubicspline1 = buildRidgeJaggednessSpline(weirdness, ridges, p_187296_, p_187298_, p_187299_);
        return CubicSpline.builder(erosion, p_187299_)
                .addPoint(-1.0F, cubicspline)
                .addPoint(-0.78F, cubicspline1)
                .addPoint(-0.5775F, cubicspline1)
                .addPoint(-0.375F, 0.0F)
                .build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildRidgeJaggednessSpline(I weirdness, I ridges, float p_187301_, float p_187302_, ToFloatFunction<Float> p_187303_) {
        float f = peaksAndValleys(0.4F);
        float f1 = peaksAndValleys(0.56666666F);

        float f2 = (f + f1) / 2.0F;
        CubicSpline.Builder<C, I> builder = CubicSpline.builder(ridges, p_187303_);
        builder.addPoint(f, 0.0F);
        if (p_187302_ > 0.0F) {
            builder.addPoint(f2, buildWeirdnessJaggednessSpline(weirdness, p_187302_, p_187303_));
        } else {
            builder.addPoint(f2, 0.0F);
        }

        if (p_187301_ > 0.0F) {
            builder.addPoint(1.0F, buildWeirdnessJaggednessSpline(weirdness, p_187301_, p_187303_));
        } else {
            builder.addPoint(1.0F, 0.0F);
        }

        return builder.build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildWeirdnessJaggednessSpline(I weirdness, float p_187305_, ToFloatFunction<Float> p_187306_) {
        float f = 0.63F * p_187305_;
        float f1 = 0.3F * p_187305_;
        return CubicSpline.builder(weirdness, p_187306_)
                .addPoint(-0.01F, f)
                .addPoint(0.01F, f1)
                .build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I weirdness, I erosion, I ridges, float strengthForContinentalness, boolean coastal, ToFloatFunction<Float> p_187310_) {
        final float defaultScale = 3.1f; // Was 6.3
        // More hilly, shattered terrain
        final float roughScale = 2.23f; // Was 2.67

        CubicSpline<C, I> defaultPVScaleSpline = CubicSpline.builder(weirdness, p_187310_)
                .addPoint(-0.2F, defaultScale)
                .addPoint(0.2F, strengthForContinentalness)
                .build();

        CubicSpline.Builder<C, I> erosionSplineBuilder = CubicSpline.builder(erosion, p_187310_)
                .addPoint(-0.6F, defaultPVScaleSpline)
                .addPoint(-0.5F, CubicSpline.builder(weirdness, p_187310_)
                        .addPoint(-0.05F, defaultScale)
                        .addPoint(0.05F, roughScale)
                        .build())
                .addPoint(-0.35F, defaultPVScaleSpline)
                .addPoint(-0.25F, defaultPVScaleSpline)
                .addPoint(-0.1F, CubicSpline.builder(weirdness, p_187310_)
                        .addPoint(-0.05F, roughScale)
                        .addPoint(0.05F, defaultScale).build())
                .addPoint(0.03F, defaultPVScaleSpline);

        // Extreme areas

        if (coastal) {
            CubicSpline<C, I> coastalWeirdnessForShattered = CubicSpline.builder(weirdness, p_187310_)
                    .addPoint(0.0F, strengthForContinentalness)
                    .addPoint(0.1F, 0.625F)
                    .build();

            CubicSpline<C, I> coastalRidgeForShattered = CubicSpline.builder(ridges, p_187310_)
                    .addPoint(-0.9F, strengthForContinentalness)
                    .addPoint(-0.69F, coastalWeirdnessForShattered)
                    .build();

            erosionSplineBuilder.addPoint(0.35F, strengthForContinentalness)
                    .addPoint(0.45F, coastalRidgeForShattered)
                    .addPoint(0.55F, coastalRidgeForShattered)
                    .addPoint(0.62F, strengthForContinentalness);
        } else {
            CubicSpline<C, I> ridgeLowShattered = CubicSpline.builder(ridges, p_187310_)
                    .addPoint(-0.7F, defaultPVScaleSpline)
                    .addPoint(-0.15F, 1.37F)
                    .build();

            CubicSpline<C, I> ridgeHighShattered = CubicSpline.builder(ridges, p_187310_)
                    // Was 0.45
                    .addPoint(0.35F, defaultPVScaleSpline)
                    .addPoint(0.7F, 1.56F)
                    .build();

            erosionSplineBuilder
                    .addPoint(0.05F, ridgeHighShattered)
                    .addPoint(0.4F, ridgeHighShattered)
                    .addPoint(0.45F, ridgeLowShattered)
                    .addPoint(0.55F, ridgeLowShattered)
                    .addPoint(0.58F, strengthForContinentalness);
        }

        return erosionSplineBuilder.build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildLagoonOffset(I erosion, ToFloatFunction<Float> offsetTransform) {
        return CubicSpline.builder(erosion, offsetTransform)
                .addPoint(-1.0f, -0.12F)
                .addPoint(0.4f, -0.06F)
                .addPoint(1.0f, -0.01F)
                .build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildMountainRidgeSplineWithPoints(I ridges, float p_187331_, boolean p_187332_, ToFloatFunction<Float> transform) {
        CubicSpline.Builder<C, I> builder = CubicSpline.builder(ridges, transform);
        float f2 = mountainContinentalness(-1.0F, p_187331_, -0.7F);
        float f4 = mountainContinentalness(1.0F, p_187331_, -0.7F);
        float f5 = calculateMountainRidgeZeroContinentalnessPoint(p_187331_);
        if (-0.65F < f5 && f5 < 1.0F) {
            float f14 = mountainContinentalness(-0.65F, p_187331_, -0.7F);
            float f9 = mountainContinentalness(-0.75F, p_187331_, -0.7F);
            float f10 = calculateSlope(f2, f9, -1.0F, -0.75F);
            builder.addPoint(-1.0F, f2, f10);
            builder.addPoint(-0.75F, f9);
            builder.addPoint(-0.65F, f14);
            float f11 = mountainContinentalness(f5, p_187331_, -0.7F);
            float f12 = calculateSlope(f11, f4, f5, 1.0F);
            builder.addPoint(f5 - 0.01F, f11);
            builder.addPoint(f5, f11, f12);
            builder.addPoint(1.0F, f4, f12);
        } else {
            float f7 = calculateSlope(f2, f4, -1.0F, 1.0F);
            if (p_187332_) {
                builder.addPoint(-1.0F, Math.max(0.2F, f2));
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

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionOffsetSpline(I erosion, I ridges, float p_187285_, float p_187286_, float p_187287_, float p_187288_, float p_187289_, float p_187290_, boolean buildSaddleValley, boolean p_187292_, ToFloatFunction<Float> transform) {
        CubicSpline<C, I> cubicspline = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(p_187288_, 0.6F, 1.5F), p_187292_, transform);
        CubicSpline<C, I> cubicspline1 = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(p_187288_, 0.6F, 1.0F), p_187292_, transform);
        CubicSpline<C, I> cubicspline2 = buildMountainRidgeSplineWithPoints(ridges, p_187288_, p_187292_, transform);

        CubicSpline<C, I> cubicspline3 = ridgeSpline(ridges, p_187285_ - 0.15F, 0.5F * p_187288_, Mth.lerp(0.5F, 0.5F, 0.5F) * p_187288_, 0.5F * p_187288_, 0.6F * p_187288_, 0.5F, transform);
        CubicSpline<C, I> cubicspline4 = ridgeSpline(ridges, p_187285_, p_187289_ * p_187288_, p_187286_ * p_187288_, 0.5F * p_187288_, 0.6F * p_187288_, 0.5F, transform);
        CubicSpline<C, I> cubicspline5 = ridgeSpline(ridges, p_187285_, p_187289_, p_187289_, p_187286_, p_187287_, 0.5F, transform);

        CubicSpline<C, I> saddleValleySlope = ridgeSpline(ridges, p_187285_, p_187289_, p_187289_, p_187286_, p_187287_, 0.5F, transform);
        CubicSpline<C, I> saddleValleyMin = CubicSpline.builder(ridges, transform)
                .addPoint(-1.0F, p_187285_, 0.0F)
                .addPoint(-0.4F, cubicspline5)
                .addPoint(0.0F, p_187287_ + 0.07F, 0.0F).build();
        CubicSpline<C, I> cubicspline8 = ridgeSpline(ridges, -0.02F, p_187290_, p_187290_, p_187286_, p_187287_, 0.0F, transform);

        CubicSpline.Builder<C, I> erosionBuilder = CubicSpline.builder(erosion, transform)
                .addPoint(-0.85F, cubicspline)
                .addPoint(-0.7F, cubicspline1)
                .addPoint(-0.4F, cubicspline2)
                .addPoint(-0.35F, cubicspline3)
                .addPoint(-0.1F, cubicspline4)
                .addPoint(0.2F, cubicspline5);

        if (buildSaddleValley) {
            erosionBuilder.addPoint(0.4F, saddleValleySlope)
                    .addPoint(0.45F, saddleValleyMin)
                    .addPoint(0.55F, saddleValleyMin)
                    .addPoint(0.58F, saddleValleySlope);
        }

        erosionBuilder.addPoint(0.7F, cubicspline8);

        return erosionBuilder.build();
    }

    // Build a spline that uses the peaks and valleys type to go from bottom (river, valley) to top (mountain, peak)
    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> ridgeSpline(I ridges, float riverHeight, float p_187278_, float p_187279_, float p_187280_, float peakHeight, float p_187282_, ToFloatFunction<Float> transform) {
        float f = Math.max(0.5F * (p_187278_ - riverHeight), p_187282_);
        float f1 = 5.0F * (p_187279_ - p_187278_);
        return CubicSpline.builder(ridges, transform)
                .addPoint(-1.0F, riverHeight, f)
                .addPoint(-0.4F, p_187278_, Math.min(f, f1))
                .addPoint(0.0F, p_187279_, f1)
                .addPoint(0.4F, p_187280_, 2.0F * (p_187280_ - p_187279_))
                .addPoint(1.0F, peakHeight, 0.7F * (peakHeight - p_187280_))
                .build();
    }
}
