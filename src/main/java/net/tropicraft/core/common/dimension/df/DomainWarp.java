package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record DomainWarp(
        DensityFunction input,
        DensityFunction x,
        DensityFunction y,
        DensityFunction z
) implements DensityFunction {
    public static final MapCodec<DomainWarp> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            DensityFunction.CODEC.fieldOf("input").forGetter(DomainWarp::input),
            DensityFunction.CODEC.optionalFieldOf("x", DensityFunctions.zero()).forGetter(DomainWarp::x),
            DensityFunction.CODEC.optionalFieldOf("y", DensityFunctions.zero()).forGetter(DomainWarp::y),
            DensityFunction.CODEC.optionalFieldOf("z", DensityFunctions.zero()).forGetter(DomainWarp::z)
    ).apply(i, DomainWarp::new));

    @Override
    public double compute(FunctionContext context) {
        int offsetX = Mth.floor(x.compute(context));
        int offsetY = Mth.floor(y.compute(context));
        int offsetZ = Mth.floor(z.compute(context));
        return input.compute(new SinglePointContext(context.blockX() + offsetX, context.blockY() + offsetY, context.blockZ() + offsetZ));
    }

    @Override
    public void fillArray(double[] output, ContextProvider contextProvider) {
        // TODO: Implement?
        contextProvider.fillAllDirectly(output, this);
    }

    @Override
    public DensityFunction mapChildren(Visitor visitor) {
        return new DomainWarp(visitor.apply(input), visitor.apply(x), visitor.apply(y), visitor.apply(z));
    }

    @Override
    public double minValue() {
        return input.minValue();
    }

    @Override
    public double maxValue() {
        return input.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<DomainWarp> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }
}
