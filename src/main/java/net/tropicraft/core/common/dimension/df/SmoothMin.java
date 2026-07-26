package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SmoothMin(
        DensityFunction left,
        DensityFunction right,
        double smoothness,
        double k
) implements DensityFunction {
    public static final MapCodec<SmoothMin> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            DensityFunction.CODEC.fieldOf("left").forGetter(SmoothMin::left),
            DensityFunction.CODEC.fieldOf("right").forGetter(SmoothMin::right),
            Codec.doubleRange(0.0, Double.MAX_VALUE).fieldOf("smoothness").forGetter(SmoothMin::smoothness)
    ).apply(i, SmoothMin::new));

    private static final double K_FACTOR = 4.0;

    public SmoothMin(DensityFunction left, DensityFunction right, double smoothness) {
        this(left, right, smoothness, smoothness * K_FACTOR);
    }

    // <https://iquilezles.org/articles/smin/>
    private double apply(double left, double right) {
        if (smoothness == 0.0) {
            return Math.min(left, right);
        }
        double h = Math.max(k - Math.abs(left - right), 0.0) / k;
        return Math.min(left, right) - h * h * smoothness;
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
        return new SmoothMin(visitor.apply(left), visitor.apply(right), smoothness);
    }

    @Override
    public double minValue() {
        return Math.min(left.minValue(), right.minValue()) - smoothness;
    }

    @Override
    public double maxValue() {
        return Math.min(left.maxValue(), right.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<SmoothMin> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }
}
