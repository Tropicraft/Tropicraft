package net.tropicraft.core.common.dimension.carver;

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
import net.minecraftforge.fmllegacy.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftConfiguredCarvers {
    public final ConfiguredWorldCarver<?> cave;
    public final ConfiguredWorldCarver<?> canyon;
    public final ConfiguredWorldCarver<?> underwaterCave;
    public final ConfiguredWorldCarver<?> underwaterCanyon;

    //TODO [1.17]: I attempted to do a 1-1 port but I have yet to find out if it is correct within 1.17

    public static CaveCarverConfiguration tropicraftCaveCarverConfig = new CaveCarverConfiguration(0.25F,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(248), 8),
            ConstantFloat.of(0.5F),
            VerticalAnchor.aboveBottom(10),
            false,
            CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
            ConstantFloat.of(1.0F),
            ConstantFloat.of(1.0F),
            ConstantFloat.of(-0.7F));

    public static CanyonCarverConfiguration tropicraftCanyonCarverConfig = new CanyonCarverConfiguration(0.02F,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(67), 8),
            ConstantFloat.of(3.0F),
            VerticalAnchor.aboveBottom(10),
            false,
            CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
            UniformFloat.of(-0.125F, 0.125F),
            new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1.0F),
                    TrapezoidFloat.of(0.0F, 6.0F, 2.0F),
                    3,
                    UniformFloat.of(0.75F, 1.0F),
                    1.0F,
                    0.0F));

    public static CaveCarverConfiguration tropicraftUnderWaterCaveCarverConfig = new CaveCarverConfiguration(0.15F,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(240), 8),
            ConstantFloat.of(0.5F),
            VerticalAnchor.aboveBottom(10),
            false,
            CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
            ConstantFloat.of(1.0F),
            ConstantFloat.of(1.0F),
            ConstantFloat.of(-0.7F));

    public static CanyonCarverConfiguration tropicUnderWaterCanyonCarverConfig = new CanyonCarverConfiguration(0.02F,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(67), 8),
            ConstantFloat.of(3.0F), VerticalAnchor.aboveBottom(10),
            false,
            CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
            UniformFloat.of(-0.125F, 0.125F),
            new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1.0F),
                    TrapezoidFloat.of(0.0F, 6.0F, 2.0F),
                    3, UniformFloat.of(0.75F, 1.0F),
                    1.0F,
                    0.0F));

    public TropicraftConfiguredCarvers(WorldgenDataConsumer<? extends ConfiguredWorldCarver<?>> worldgen) {
        Register carvers = new Register(worldgen);

        this.cave = carvers.register("cave", TropicraftCarvers.CAVE, tropicraftCaveCarverConfig);
        this.canyon = carvers.register("canyon", TropicraftCarvers.CANYON, tropicraftCanyonCarverConfig);
        this.underwaterCave = carvers.register("underwater_cave", TropicraftCarvers.UNDERWATER_CAVE, tropicraftUnderWaterCaveCarverConfig);
        this.underwaterCanyon = carvers.register("underwater_canyon", TropicraftCarvers.UNDERWATER_CANYON, tropicUnderWaterCanyonCarverConfig);
    }

    public void addLand(BiomeGenerationSettings.Builder generation) {
        generation.addCarver(GenerationStep.Carving.AIR, this.cave);
        generation.addCarver(GenerationStep.Carving.AIR, this.canyon);
    }

    public void addUnderwater(BiomeGenerationSettings.Builder generation) {
        generation.addCarver(GenerationStep.Carving.LIQUID, this.underwaterCave);
        generation.addCarver(GenerationStep.Carving.LIQUID, this.underwaterCanyon);
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredWorldCarver<?>> worldgen;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredWorldCarver<?>> worldgen) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredWorldCarver<?>>) worldgen;
        }

        public <C extends CarverConfiguration, WC extends WorldCarver<C>> ConfiguredWorldCarver<?> register(String id, RegistryObject<WC> carver, C config) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), carver.get().configured(config));
        }
    }
}
