package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SmoothMax(
        DensityFunction left,
        DensityFunction right,
        double smoothness,
        double k
) implements DensityFunction {
    public static final MapCodec<SmoothMax> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            DensityFunction.CODEC.fieldOf("left").forGetter(SmoothMax::left),
            DensityFunction.CODEC.fieldOf("right").forGetter(SmoothMax::right),
            Codec.doubleRange(0.0, Double.MAX_VALUE).fieldOf("smoothness").forGetter(SmoothMax::smoothness)
    ).apply(i, SmoothMax::new));

    private static final double K_FACTOR = 4.0;

    public SmoothMax(DensityFunction left, DensityFunction right, double smoothness) {
        this(left, right, smoothness, smoothness * K_FACTOR);
    }

    // <https://iquilezles.org/articles/smin/>
    private double apply(double left, double right) {
        if (smoothness == 0.0) {
            return Math.max(left, right);
        }
        double h = Math.max(k - Math.abs(right - left), 0.0) / k;
        return Math.max(left, right) + h * h * smoothness;
    }

    @Override
    public double compute(FunctionContext context) {
        return apply(left.compute(context), right.compute(context));
    }

    @Override
    public void fillArray(double[] output, ContextProvider contextProvider) {
        left.fillArray(output, contextProvider);
        double[] rightOutput = new double[output.length];
        right.fillArray(rightOutput, contextProvider);
        for (int i = 0; i < output.length; i++) {
            output[i] = apply(output[i], rightOutput[i]);
        }
    }

    @Override
    public DensityFunction mapChildren(Visitor visitor) {
        return new SmoothMax(visitor.apply(left), visitor.apply(right), smoothness);
    }

    @Override
    public double minValue() {
        return Math.max(left.minValue(), right.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(left.maxValue(), right.maxValue()) + smoothness;
    }

    @Override
    public KeyDispatchDataCodec<SmoothMax> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }
}
