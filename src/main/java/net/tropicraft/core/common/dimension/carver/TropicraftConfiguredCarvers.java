package net.tropicraft.core.common.dimension.carver;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftConfiguredCarvers {
    public final Holder<ConfiguredWorldCarver<?>> cave;
    public final Holder<ConfiguredWorldCarver<?>> canyon;

    public TropicraftConfiguredCarvers(WorldgenDataConsumer<? extends ConfiguredWorldCarver<?>> worldgen) {
        Register carvers = new Register(worldgen);

        // 1.18 cave reference
//        new CaveCarverConfiguration(
//                0.15F,
//                UniformHeight.of(
//                        VerticalAnchor.aboveBottom(8),
//                        VerticalAnchor.absolute(180)),
//                UniformFloat.of(0.1F, 0.9F),
//                VerticalAnchor.aboveBottom(8),
//                CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
//                UniformFloat.of(0.7F, 1.4F),
//                UniformFloat.of(0.8F, 1.3F),
//                UniformFloat.of(-1.0F, -0.4F)
//        );

        this.cave = carvers.register("cave", TropicraftCarvers.CAVE,
                new CaveCarverConfiguration(
                        0.25F,
                        BiasedToBottomHeight.of(VerticalAnchor.absolute(0),VerticalAnchor.absolute(240), 8),
                        ConstantFloat.of(0.5F),
                        VerticalAnchor.aboveBottom(10),
                        CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
                        ConstantFloat.of(1.0F),
                        ConstantFloat.of(1.0F),
                        ConstantFloat.of(-0.7F)
                )
        );

        this.canyon = carvers.register("canyon", TropicraftCarvers.CANYON,
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
    }

    public void addLand(BiomeGenerationSettings.Builder generation) {
        generation.addCarver(GenerationStep.Carving.AIR, this.cave);
        generation.addCarver(GenerationStep.Carving.AIR, this.canyon);
    }

    @Deprecated
    public void addUnderwater(BiomeGenerationSettings.Builder generation) {

    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredWorldCarver<?>> worldgen;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredWorldCarver<?>> worldgen) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredWorldCarver<?>>) worldgen;
        }

        public <C extends CarverConfiguration, WC extends WorldCarver<C>> Holder<ConfiguredWorldCarver<?>> register(String id, RegistryObject<WC> carver, C config) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), carver.get().configured(config));
        }
    }
}
