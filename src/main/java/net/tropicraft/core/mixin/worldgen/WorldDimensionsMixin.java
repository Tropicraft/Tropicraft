package net.tropicraft.core.mixin.worldgen;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

// Mark Tropicraft worlds as stable
@Mixin(WorldDimensions.class)
public class WorldDimensionsMixin {
    @Mutable
    @Shadow
    @Final
    private static Set<ResourceKey<LevelStem>> BUILTIN_ORDER;
    @Shadow
    @Final
    @Mutable
    private static int VANILLA_DIMENSION_COUNT;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void init(CallbackInfo ci) {
        ImmutableSet.Builder<ResourceKey<LevelStem>> order = ImmutableSet.builderWithExpectedSize(BUILTIN_ORDER.size() + 1);
        order.addAll(BUILTIN_ORDER);
        order.add(TropicraftDimension.DIMENSION);
        BUILTIN_ORDER = order.build();

        VANILLA_DIMENSION_COUNT++;
    }

    @Inject(method = "isVanillaLike", at = @At("HEAD"), cancellable = true)
    private static void isDimensionStable(ResourceKey<LevelStem> key, LevelStem levelStem, CallbackInfoReturnable<Boolean> ci) {
        if (key == TropicraftDimension.DIMENSION) {
            // We're not really concerned if somebody makes a datapack with the tropicraft ID, mark it as stable anyway
            ci.setReturnValue(true);
        }
    }
}
