package net.tropicraft.core.mixin.worldgen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    /**
     * Add the tropicraft dimension to newly created worlds.
     */
    @Inject(method = "defaultDimensions(Lnet/minecraft/core/RegistryAccess;JZ)Lnet/minecraft/core/Registry;", at = @At("RETURN"))
    private static void addDefaultDimension(RegistryAccess registries, long seed, boolean report, CallbackInfoReturnable<Registry<LevelStem>> ci) {
        final WritableRegistry<LevelStem> dimensionRegistry = (WritableRegistry<LevelStem>) ci.getReturnValue();

        LevelStem dimension = TropicraftDimension.createDimension(
                registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                registries.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                registries.registryOrThrow(Registry.BIOME_REGISTRY),
                registries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                registries.registryOrThrow(Registry.NOISE_REGISTRY),
                seed
        );
        dimensionRegistry.register(TropicraftDimension.DIMENSION, dimension, Lifecycle.stable());
    }
}
