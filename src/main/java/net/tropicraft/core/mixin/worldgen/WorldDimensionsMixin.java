package net.tropicraft.core.mixin.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Mark Tropicraft worlds as stable
@Mixin(WorldDimensions.class)
public class WorldDimensionsMixin {
    @Inject(method = "isVanillaLike", at = @At("HEAD"), cancellable = true)
    private static void isDimensionStable(ResourceKey<LevelStem> key, LevelStem levelStem, CallbackInfoReturnable<Boolean> ci) {
        if (key == TropicraftDimension.DIMENSION) {
            // We're not really concerned if somebody makes a datapack with the tropicraft ID, mark it as stable anyway
            ci.setReturnValue(true);
        }
    }
}
