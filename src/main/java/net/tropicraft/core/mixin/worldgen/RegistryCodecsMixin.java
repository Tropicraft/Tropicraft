package net.tropicraft.core.mixin.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.OptionalInt;

@Mixin(targets = "net/minecraft/core/RegistryCodecs$1")
public class RegistryCodecsMixin<E> {
    /**
     * Add the tropicraft dimension to new and existing worlds when they get loaded.
     */
    @SuppressWarnings("unchecked")
    @Inject(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryOps;registryLoader()Ljava/util/Optional;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private <T> void decode(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<Registry<E>, T>>> ci, DataResult<Pair<WritableRegistry<E>, T>> decodedRegistry, RegistryOps<T> registryOps) {
        decodedRegistry.result().map(Pair::getFirst).ifPresent(registry -> {
            final ResourceKey<?> registryKey = registry.key();
            if (registryKey == Registry.LEVEL_STEM_REGISTRY && registry.get(TropicraftDimension.ID) == null) {
                addDimensions(registryOps, (WritableRegistry<LevelStem>) registry);
            }
        });
    }

    private void addDimensions(RegistryOps<?> ops, WritableRegistry<LevelStem> registry) {
        LevelStem overworld = registry.get(LevelStem.OVERWORLD);
        if (overworld == null) {
            return;
        }

        // steal the seed from the overworld chunk generator.
        // not necessarily the world seed if a datapack changes it, but it's probably a safe bet.
        long seed = overworld.generator().ringPlacementSeed;

        LevelStem dimension = TropicraftDimension.createDimension(
                registryOrThrow(ops, Registry.DIMENSION_TYPE_REGISTRY),
                registryOrThrow(ops, Registry.STRUCTURE_SET_REGISTRY),
                registryOrThrow(ops, Registry.BIOME_REGISTRY),
                registryOrThrow(ops, Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                registryOrThrow(ops, Registry.NOISE_REGISTRY),
                seed
        );
        registry.registerOrOverride(OptionalInt.empty(), TropicraftDimension.DIMENSION, dimension, Lifecycle.stable());
    }

    private static <T> Registry<T> registryOrThrow(RegistryOps<?> ops, ResourceKey<Registry<T>> key) {
        return ops.registry(key).orElseThrow(() -> new IllegalArgumentException("Missing registry for " + key));
    }
}
