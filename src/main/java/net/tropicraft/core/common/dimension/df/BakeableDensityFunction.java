package net.tropicraft.core.common.dimension.df;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.function.Function;

public interface BakeableDensityFunction extends DensityFunction {
    DensityFunction bake(Function<Identifier, PositionalRandomFactory> randomFactory);
}
