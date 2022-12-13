package net.tropicraft.core.common.dimension.carver;

import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public final class TropicraftConfiguredCarvers {
    public static final DeferredRegister<ConfiguredWorldCarver<?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_CARVER_REGISTRY, Constants.MODID);

    public static final RegistryObject<ConfiguredWorldCarver<?>> CAVE = register("cave", TropicraftCarvers.CAVE,
            new CaveCarverConfiguration(
                    0.25F,
                    BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(240), 8),
                    ConstantFloat.of(0.5F),
                    VerticalAnchor.aboveBottom(10),
                    CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
                    ConstantFloat.of(1.0F),
                    ConstantFloat.of(1.0F),
                    ConstantFloat.of(-0.7F)
            )
    );

    public static final RegistryObject<ConfiguredWorldCarver<?>> CANYON = register("canyon", TropicraftCarvers.CANYON,
            new CanyonCarverConfiguration(
                    0.02F,
                    BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(67), 8),
                    ConstantFloat.of(3.0F),
                    VerticalAnchor.aboveBottom(10),
                    CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
                    UniformFloat.of(-0.125F, 0.125F),
                    new CanyonCarverConfiguration.CanyonShapeConfiguration(
                            UniformFloat.of(0.75F, 1.0F),
                            TrapezoidFloat.of(0.0F, 6.0F, 2.0F),
                            3,
                            UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F
                    )
            )
    );

    public static void addLand(BiomeGenerationSettings.Builder generation) {
        generation.addCarver(GenerationStep.Carving.AIR, CAVE.getHolder().orElseThrow());
        generation.addCarver(GenerationStep.Carving.AIR, CANYON.getHolder().orElseThrow());
    }

    public static <C extends CarverConfiguration, WC extends WorldCarver<C>> RegistryObject<ConfiguredWorldCarver<?>> register(String id, RegistryObject<WC> carver, C config) {
        return REGISTER.register(id, () -> carver.get().configured(config));
    }
}
