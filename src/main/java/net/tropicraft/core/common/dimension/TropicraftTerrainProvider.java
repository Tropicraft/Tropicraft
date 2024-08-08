package net.tropicraft.core.common.dimension;

import net.minecraft.util.CubicSpline;
import net.minecraft.util.Mth;
import net.minecraft.util.ToFloatFunction;

public final class TropicraftTerrainProvider {
    private static final ToFloatFunction<Float> NO_TRANSFORM = ToFloatFunction.IDENTITY;

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> offset(I continents, I erosion, I ridges) {
        ToFloatFunction<Float> offsetTransform = ToFloatFunction.createUnlimited(offset -> offset + 0.5f);

        CubicSpline<C, I> nearInlandDepth = buildErosionOffsetSpline(erosion, ridges, -0.15f, 0.0f, 0.0f, 0.1f, 0.0f, -0.03f, false, false, offsetTransform);
        CubicSpline<C, I> midInlandDepth = buildErosionOffsetSpline(erosion, ridges, -0.1f, 0.03f, 0.1f, 0.1f, 0.01f, -0.03f, false, false, offsetTransform);
        CubicSpline<C, I> farInlandDepth = buildErosionOffsetSpline(erosion, ridges, -0.1f, 0.03f, 0.1f, 0.7f, 0.01f, -0.03f, true, true, offsetTransform);
        CubicSpline<C, I> peaksDepth = buildErosionOffsetSpline(erosion, ridges, -0.05f, 0.03f, 0.1f, 1.0f, 0.01f, 0.01f, true, true, offsetTransform);

        CubicSpline<C, I> lagoonSpline = buildLagoonOffset(erosion, offsetTransform);

        // Depth sampler
        return CubicSpline.builder(continents, offsetTransform)
                .addPoint(-1.1f, 0.140f)
                .addPoint(-0.92f, -0.2222f)
                .addPoint(-0.41f, -0.2222f)
                .addPoint(-0.34f, -0.12f)
                .addPoint(-0.22f, lagoonSpline)
                .addPoint(-0.18f, lagoonSpline)
                .addPoint(-0.16f, nearInlandDepth)
                .addPoint(-0.15f, nearInlandDepth)
                .addPoint(-0.1f, midInlandDepth)
                .addPoint(0.25f, farInlandDepth)
                .addPoint(1.0f, peaksDepth)
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> factor(I continents, I erosion, I weirdness, I ridges) {
        final float beachFactorStrength = 6.25f;
        final float nearinlandFactorStrength = 4.22f; // 5.47
        final float midInlandFactorStrength = 3.8f; // Was 5.08
        final float furtherInlandFactorStrength = 2.44f; // Was 4.69

        // Scale sampler
        return CubicSpline.builder(continents, NO_TRANSFORM)
                .addPoint(-1.1f, 0.54f)
                .addPoint(-0.92f, 3.95f)
                .addPoint(-0.19f, 3.95f)
                .addPoint(-0.15f, getErosionFactor(weirdness, erosion, ridges, beachFactorStrength, true, NO_TRANSFORM))
                .addPoint(-0.1f, getErosionFactor(weirdness, erosion, ridges, nearinlandFactorStrength, true, NO_TRANSFORM))
                .addPoint(0.03f, getErosionFactor(weirdness, erosion, ridges, midInlandFactorStrength, true, NO_TRANSFORM))
                .addPoint(0.06f, getErosionFactor(weirdness, erosion, ridges, furtherInlandFactorStrength, false, NO_TRANSFORM))
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> jaggedness(I continents, I erosion, I weirdness, I ridges) {
        float jaggednessMax = 0.65f;
        return CubicSpline.builder(continents, NO_TRANSFORM)
                .addPoint(-0.11f, 0.0f)
                .addPoint(0.03f, buildErosionJaggednessSpline(weirdness, erosion, ridges, 1.0f, 0.5f, 0.0f, 0.0f, NO_TRANSFORM))
                .addPoint(jaggednessMax, buildErosionJaggednessSpline(weirdness, erosion, ridges, 1.0f, 1.0f, 1.0f, 0.0f, NO_TRANSFORM))
                .build();
    }

    // Desmos: -\left(\left|\left|x\right|-\frac{2}{3}\right|-\frac{1}{3}\right)\cdot3
    public static float peaksAndValleys(float weirdness) {
        return -(Math.abs(Math.abs(weirdness) - 0.6666667f) - 0.33333334f) * 3.0f;
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionJaggednessSpline(I weirdness, I erosion, I ridges, float p_187295_, float p_187296_, float p_187297_, float p_187298_, ToFloatFunction<Float> p_187299_) {
        float f = -0.5775f;
        CubicSpline<C, I> cubicspline = buildRidgeJaggednessSpline(weirdness, ridges, p_187295_, p_187297_, p_187299_);
        CubicSpline<C, I> cubicspline1 = buildRidgeJaggednessSpline(weirdness, ridges, p_187296_, p_187298_, p_187299_);
        return CubicSpline.builder(erosion, p_187299_)
                .addPoint(-1.0f, cubicspline)
                .addPoint(-0.78f, cubicspline1)
                .addPoint(-0.5775f, cubicspline1)
                .addPoint(-0.375f, 0.0f)
                .build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildRidgeJaggednessSpline(I weirdness, I ridges, float p_187301_, float p_187302_, ToFloatFunction<Float> p_187303_) {
        float f = peaksAndValleys(0.4f);          // 0.2
        float f1 = peaksAndValleys(0.56666666f);  // 0.7

        float f2 = (f + f1) / 2.0f; // 0.45
        CubicSpline.Builder<C, I> builder = CubicSpline.builder(ridges, p_187303_);
        builder.addPoint(f, 0.0f);
        if (p_187302_ > 0.0f) {
            builder.addPoint(f2, buildWeirdnessJaggednessSpline(weirdness, p_187302_, p_187303_));
        } else {
            builder.addPoint(f2, 0.0f);
        }

        if (p_187301_ > 0.0f) {
            builder.addPoint(1.0f, buildWeirdnessJaggednessSpline(weirdness, p_187301_, p_187303_));
        } else {
            builder.addPoint(1.0f, 0.0f);
        }

        return builder.build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildWeirdnessJaggednessSpline(I weirdness, float p_187305_, ToFloatFunction<Float> p_187306_) {
        float f = 0.63f * p_187305_;
        float f1 = 0.3f * p_187305_;
        return CubicSpline.builder(weirdness, p_187306_)
                .addPoint(-0.01f, f)
                .addPoint(0.01f, f1)
                .build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I weirdness, I erosion, I ridges, float strengthForContinentalness, boolean coastal, ToFloatFunction<Float> transform) {
        final float defaultScale = 3.1f; // Was 6.3
        // More hilly, shattered terrain
        final float roughScale = 2.23f; // Was 2.67

        CubicSpline<C, I> defaultPVScaleSpline = CubicSpline.builder(weirdness, transform)
                .addPoint(-0.2f, defaultScale)
                .addPoint(0.2f, strengthForContinentalness)
                .build();

        CubicSpline.Builder<C, I> erosionSplineBuilder = CubicSpline.builder(erosion, transform)
                .addPoint(-0.6f, defaultPVScaleSpline)
                .addPoint(-0.5f, CubicSpline.builder(weirdness, transform)
                        .addPoint(-0.05f, defaultScale)
                        .addPoint(0.05f, roughScale)
                        .build())
                .addPoint(-0.35f, defaultPVScaleSpline)
                .addPoint(-0.25f, defaultPVScaleSpline)
                .addPoint(-0.1f, CubicSpline.builder(weirdness, transform)
                        .addPoint(-0.05f, roughScale)
                        .addPoint(0.05f, defaultScale).build())
                .addPoint(0.03f, defaultPVScaleSpline);

        // Extreme areas

        if (coastal) {
            CubicSpline<C, I> coastalWeirdnessForShattered = CubicSpline.builder(weirdness, transform)
                    .addPoint(0.0f, strengthForContinentalness)
                    .addPoint(0.1f, 0.625f)
                    .build();

            CubicSpline<C, I> coastalRidgeForShattered = CubicSpline.builder(ridges, transform)
                    .addPoint(-0.9f, strengthForContinentalness)
                    .addPoint(-0.69f, coastalWeirdnessForShattered)
                    .build();

            erosionSplineBuilder.addPoint(0.35f, strengthForContinentalness)
                    .addPoint(0.45f, coastalRidgeForShattered)
                    .addPoint(0.55f, coastalRidgeForShattered)
                    .addPoint(0.62f, strengthForContinentalness);
        } else {
            CubicSpline<C, I> ridgeLowShattered = CubicSpline.builder(ridges, transform)
                    .addPoint(-0.7f, defaultPVScaleSpline)
                    .addPoint(-0.15f, 1.37f)
                    .build();

            // A flatter peaks&valleys spline to handle mangrove areas, which should generate flatter.
            // PV values around -0.2 create the flattest areas, become less flat near the rivers to keep them more interesting
            // and across 0, to make mountainy areas more interesting.
            CubicSpline<C, I> flatPVScaleSpline = CubicSpline.builder(weirdness, transform)
                    .addPoint(-0.8f, defaultScale)
                    .addPoint(-0.2f, 6.5f)
                    .addPoint(-0.02f, 5.5f)
                    .addPoint(0.2f, strengthForContinentalness)
                    .build();

            CubicSpline<C, I> ridgeHighShattered = CubicSpline.builder(ridges, transform)
                    // Was 0.45
                    .addPoint(0.35f, flatPVScaleSpline)
                    .addPoint(0.7f, 1.56f)
                    .build();

            erosionSplineBuilder
                    .addPoint(0.05f, ridgeHighShattered)
                    .addPoint(0.4f, ridgeHighShattered)
                    .addPoint(0.45f, ridgeLowShattered)
                    .addPoint(0.55f, ridgeLowShattered)
                    .addPoint(0.58f, strengthForContinentalness);
        }

        return erosionSplineBuilder.build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildLagoonOffset(I erosion, ToFloatFunction<Float> offsetTransform) {
        return CubicSpline.builder(erosion, offsetTransform)
                .addPoint(-1.0f, -0.12f)
                .addPoint(0.4f, -0.06f)
                .addPoint(1.0f, -0.01f)
                .build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildMountainRidgeSplineWithPoints(I ridges, float p_187331_, boolean p_187332_, ToFloatFunction<Float> transform) {
        CubicSpline.Builder<C, I> builder = CubicSpline.builder(ridges, transform);
        float f2 = mountainContinentalness(-1.0f, p_187331_, -0.7f);
        float f4 = mountainContinentalness(1.0f, p_187331_, -0.7f);
        float f5 = calculateMountainRidgeZeroContinentalnessPoint(p_187331_);
        if (-0.65f < f5 && f5 < 1.0f) {
            float f14 = mountainContinentalness(-0.65f, p_187331_, -0.7f);
            float f9 = mountainContinentalness(-0.75f, p_187331_, -0.7f);
            float f10 = calculateSlope(f2, f9, -1.0f, -0.75f);
            builder.addPoint(-1.0f, f2, f10);
            builder.addPoint(-0.75f, f9);
            builder.addPoint(-0.65f, f14);
            float f11 = mountainContinentalness(f5, p_187331_, -0.7f);
            float f12 = calculateSlope(f11, f4, f5, 1.0f);
            builder.addPoint(f5 - 0.01f, f11);
            builder.addPoint(f5, f11, f12);
            builder.addPoint(1.0f, f4, f12);
        } else {
            float f7 = calculateSlope(f2, f4, -1.0f, 1.0f);
            if (p_187332_) {
                builder.addPoint(-1.0f, Math.max(0.2f, f2));
                builder.addPoint(0.0f, Mth.lerp(0.5f, f2, f4), f7);
            } else {
                builder.addPoint(-1.0f, f2, f7);
            }

            builder.addPoint(1.0f, f4, f7);
        }

        return builder.build();
    }

    private static float calculateSlope(float p_187272_, float p_187273_, float p_187274_, float p_187275_) {
        return (p_187273_ - p_187272_) / (p_187275_ - p_187274_);
    }

    private static float mountainContinentalness(float p_187327_, float p_187328_, float p_187329_) {
        float f = 1.17f;
        float f1 = 0.46082947f;
        float f2 = 1.0f - (1.0f - p_187328_) * 0.5f;
        float f3 = 0.5f * (1.0f - p_187328_);
        float f4 = (p_187327_ + 1.17f) * 0.46082947f;
        float f5 = f4 * f2 - f3;
        return p_187327_ < p_187329_ ? Math.max(f5, -0.2222f) : Math.max(f5, 0.0f);
    }

    private static float calculateMountainRidgeZeroContinentalnessPoint(float p_187344_) {
        float f = 1.17f;
        float f1 = 0.46082947f;
        float f2 = 1.0f - (1.0f - p_187344_) * 0.5f;
        float f3 = 0.5f * (1.0f - p_187344_);
        return f3 / (0.46082947f * f2) - 1.17f;
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionOffsetSpline(I erosion, I ridges, float p_187285_, float p_187286_, float p_187287_, float p_187288_, float p_187289_, float p_187290_, boolean buildSaddleValley, boolean p_187292_, ToFloatFunction<Float> transform) {
        CubicSpline<C, I> lowestErosion = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(p_187288_, 0.6f, 1.5f), p_187292_, transform);
        CubicSpline<C, I> cubicspline1 = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(p_187288_, 0.6f, 1.0f), p_187292_, transform);
        CubicSpline<C, I> cubicspline2 = buildMountainRidgeSplineWithPoints(ridges, p_187288_, p_187292_, transform);

        CubicSpline<C, I> cubicspline3 = ridgeSpline(ridges, p_187285_ - 0.15f, 0.5f * p_187288_, Mth.lerp(0.5f, 0.5f, 0.5f) * p_187288_, 0.5f * p_187288_, 0.6f * p_187288_, 0.5f, transform);
        CubicSpline<C, I> cubicspline4 = ridgeSpline(ridges, p_187285_, p_187289_ * p_187288_, p_187286_ * p_187288_, 0.5f * p_187288_, 0.6f * p_187288_, 0.5f, transform);
        CubicSpline<C, I> cubicspline5 = ridgeSpline(ridges, p_187285_, p_187289_, p_187289_, p_187286_, p_187287_, 0.5f, transform);

        CubicSpline<C, I> saddleValleySlope = ridgeSpline(ridges, p_187285_, p_187289_, p_187289_, p_187286_, p_187287_, 0.5f, transform);
        CubicSpline<C, I> saddleValleyMin = CubicSpline.builder(ridges, transform)
                .addPoint(-1.0f, p_187285_, 0.0f)
                .addPoint(-0.4f, cubicspline5)
                .addPoint(0.0f, p_187287_ + 0.07f, 0.0f).build();
        CubicSpline<C, I> highErosion = ridgeSpline(ridges, -0.02f, p_187290_, p_187290_, p_187286_, p_187287_, 0.0f, transform);

        CubicSpline.Builder<C, I> erosionBuilder = CubicSpline.builder(erosion, transform)
                .addPoint(-0.85f, lowestErosion)
                .addPoint(-0.7f, cubicspline1)
                .addPoint(-0.4f, cubicspline2)
                .addPoint(-0.35f, cubicspline3)
                .addPoint(-0.1f, cubicspline4)
                .addPoint(0.2f, cubicspline5);

        if (buildSaddleValley) {
            erosionBuilder.addPoint(0.4f, saddleValleySlope)
                    .addPoint(0.45f, saddleValleyMin)
                    .addPoint(0.55f, saddleValleyMin)
                    .addPoint(0.58f, saddleValleySlope);
        }

        erosionBuilder.addPoint(0.7f, highErosion);

        return erosionBuilder.build();
    }

    // Build a spline that uses the peaks and valleys type to go from bottom (river, valley) to top (mountain, peak)
    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> ridgeSpline(I ridges, float riverHeight, float riverSlope, float midSlope, float peakSlope, float peakHeight, float minRiverSlope, ToFloatFunction<Float> transform) {
        float riverDerivative = Math.max(0.5f * (riverSlope - riverHeight), minRiverSlope);
        float slopeUpRiver = 5.0f * (midSlope - riverSlope);
        return CubicSpline.builder(ridges, transform)
                .addPoint(-1.0f, riverHeight, riverDerivative)
                .addPoint(-0.4f, riverSlope, Math.min(riverDerivative, slopeUpRiver))
                .addPoint(0.0f, midSlope, slopeUpRiver)
                .addPoint(0.4f, peakSlope, 2.0f * (peakSlope - midSlope))
                .addPoint(1.0f, peakHeight, 0.7f * (peakHeight - peakSlope))
                .build();
    }
}
