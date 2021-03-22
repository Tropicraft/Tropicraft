package net.tropicraft.core.mixin.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.Dimension;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(WorldSettingsImport.class)
public class WorldSettingsImportMixin {
    @Shadow
    @Final
    private DynamicRegistries.Impl dynamicRegistries;

    /**
     * Add the tropicraft dimension to both new worlds and existing worlds when they get loaded.
     */
    @Inject(
            method = "decode(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
            at = @At("HEAD")
    )
    @SuppressWarnings("unchecked")
    private void decode(SimpleRegistry<?> registry, RegistryKey<?> registryKey, Codec<?> codec, CallbackInfoReturnable<DataResult<SimpleRegistry<?>>> ci) {
        if (registryKey == Registry.DIMENSION_KEY && registry.getOrDefault(TropicraftDimension.ID) == null) {
            this.addDimensions((SimpleRegistry<Dimension>) registry);
        }
    }

    private void addDimensions(SimpleRegistry<Dimension> registry) {
        Dimension overworld = registry.getValueForKey(Dimension.OVERWORLD);
        if (overworld == null) {
            return;
        }

        // steal the seed from the overworld chunk generator.
        // not necessarily the world seed if a datapack changes it, but it's probably a safe bet.
        long seed = overworld.getChunkGenerator().field_235950_e_;

        Dimension dimension = TropicraftDimension.createDimension(
                this.dynamicRegistries.getRegistry(Registry.DIMENSION_TYPE_KEY),
                this.dynamicRegistries.getRegistry(Registry.BIOME_KEY),
                this.dynamicRegistries.getRegistry(Registry.NOISE_SETTINGS_KEY),
                seed
        );
        registry.validateAndRegister(OptionalInt.empty(), TropicraftDimension.DIMENSION, dimension, Lifecycle.stable());
    }

    /**
     * Remove the experimental world settings warning screen for tropicraft content.
     */
    @ModifyVariable(
            method = "createRegistry",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/util/registry/WorldSettingsImport$IResourceAccess;decode(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/RegistryKey;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Decoder;)Lcom/mojang/serialization/DataResult;"
            ),
            ordinal = 1
    )
    private <E> DataResult<Pair<E, OptionalInt>> modifyDataResult(
            DataResult<Pair<E, OptionalInt>> result,
            RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry, Codec<E> mapCodec, ResourceLocation id
    ) {
        if (id.getNamespace().equals(Constants.MODID)) {
            return result.setLifecycle(Lifecycle.stable());
        }
        return result;
    }
}
