package net.tropicraft.core.mixin.worldgen;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import net.tropicraft.core.common.dimension.df.BakeableDensityFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net/minecraft/world/level/levelgen/RandomState$1NoiseWiringHelper")
public class RandomStateNoiseWirerMixin {
    @Final
    @Shadow
    RandomState this$0;

    @ModifyVariable(method = "wrapNew", at = @At("HEAD"), argsOnly = true, name = "function")
    private DensityFunction wrapNew(DensityFunction function) {
        if (function instanceof BakeableDensityFunction bakeable) {
            return bakeable.bake(this$0::getOrCreateRandomFactory);
        }
        return function;
    }
}
