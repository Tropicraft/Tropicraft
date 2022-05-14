package net.tropicraft.core.mixin.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseRouterData.class)
public interface NoiseRouterDataAccessor {
    @Invoker
    static DensityFunction callGetFunction(ResourceKey<DensityFunction> key) {
        throw new UnsupportedOperationException();
    }
}
