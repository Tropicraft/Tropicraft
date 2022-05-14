package net.tropicraft.core.mixin.worldgen;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryLoader;
import net.minecraft.resources.RegistryResourceAccess;
import net.minecraft.resources.ResourceKey;
import net.tropicraft.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    /**
     * Remove the experimental world settings warning screen for tropicraft content.
     */
    @ModifyVariable(
            method = "overrideElementFromResources",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/resources/RegistryResourceAccess;parseElement(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Decoder;)Ljava/util/Optional;"
            )
    )
    private <E> Optional<DataResult<RegistryResourceAccess.ParsedEntry<E>>> modifyDataResult(
            Optional<DataResult<RegistryResourceAccess.ParsedEntry<E>>> result,
            WritableRegistry<E> registry, ResourceKey<? extends Registry<E>> registryKey, Codec<E> codec, ResourceKey<E> key, DynamicOps<JsonElement> ops
    ) {
        if (key.location().getNamespace().equals(Constants.MODID)) {
            return result.map(r -> r.setLifecycle(Lifecycle.stable()));
        }
        return result;
    }
}
