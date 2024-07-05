package net.tropicraft.core.common.dimension.carver;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;

public final class TropicraftConfiguredCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE = createKey("cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> CANYON = createKey("canyon");

    public static void bootstrap(final BootstrapContext<ConfiguredWorldCarver<?>> context) {
        context.register(CAVE, TropicraftCarvers.CAVE.get().configured(new CaveCarverConfiguration(
                0.25F,
                BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(240), 8),
                ConstantFloat.of(0.5F),
                VerticalAnchor.aboveBottom(10),
                BuiltInRegistries.BLOCK.getOrCreateTag(TropicraftTags.Blocks.CARVER_REPLACEABLES),
                ConstantFloat.of(1.0F),
                ConstantFloat.of(1.0F),
                ConstantFloat.of(-0.7F)
        )));

        context.register(CANYON, TropicraftCarvers.CANYON.get().configured(new CanyonCarverConfiguration(
                0.02F,
                BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(106), 8),
                ConstantFloat.of(3.0F),
                VerticalAnchor.aboveBottom(10),
                CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
                BuiltInRegistries.BLOCK.getOrCreateTag(TropicraftTags.Blocks.CARVER_REPLACEABLES),
                UniformFloat.of(-0.125F, 0.125F),
                new CanyonCarverConfiguration.CanyonShapeConfiguration(
                        UniformFloat.of(0.75F, 1.0F),
                        TrapezoidFloat.of(0.0F, 6.0F, 2.0F),
                        3,
                        UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F
                )
        )));
    }

    public static void addLand(BiomeGenerationSettings.Builder generation) {
        generation.addCarver(GenerationStep.Carving.AIR, CAVE);
        generation.addCarver(GenerationStep.Carving.AIR, CANYON);
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> createKey(final String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }
}
