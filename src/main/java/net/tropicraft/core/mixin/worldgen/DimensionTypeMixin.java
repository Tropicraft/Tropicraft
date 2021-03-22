package net.tropicraft.core.mixin.worldgen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

/**
 * Add the Tropicraft dimension as a default dimension. If we instead added the dimension through JSON,
 * the world seed would be fixed to a constant that can't be changed per-world as we need.
 */
@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(method = "getDefaultSimpleRegistry", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void getDefaultSimpleRegistry(
            Registry<DimensionType> dimensionTypeRegistry,
            Registry<Biome> biomeRegistry,
            Registry<DimensionSettings> dimensionSettingsRegistry,
            long seed,
            CallbackInfoReturnable<SimpleRegistry<Dimension>> ci,
            SimpleRegistry<Dimension> dimensionRegistry
    ) {
        Supplier<DimensionType> dimensionType = () -> dimensionTypeRegistry.getOrThrow(TropicraftDimension.DIMENSION_TYPE);
        ChunkGenerator generator = TropicraftDimension.createGenerator(biomeRegistry, dimensionSettingsRegistry, seed);

        Dimension dimension = new Dimension(dimensionType, generator);
        dimensionRegistry.register(TropicraftDimension.DIMENSION, dimension, Lifecycle.stable());
    }
}
