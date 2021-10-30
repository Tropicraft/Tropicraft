package net.tropicraft.core.common.dimension.carver;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftConfiguredCarvers {
    public final ConfiguredWorldCarver<?> cave;
    public final ConfiguredWorldCarver<?> canyon;
    public final ConfiguredWorldCarver<?> underwaterCave;
    public final ConfiguredWorldCarver<?> underwaterCanyon;

    public TropicraftConfiguredCarvers(WorldgenDataConsumer<? extends ConfiguredWorldCarver<?>> worldgen) {
        Register carvers = new Register(worldgen);

        this.cave = carvers.register("cave", TropicraftCarvers.CAVE, new ProbabilityFeatureConfiguration(0.25F));
        this.canyon = carvers.register("canyon", TropicraftCarvers.CANYON, new ProbabilityFeatureConfiguration(0.02F));
        this.underwaterCave = carvers.register("underwater_cave", TropicraftCarvers.UNDERWATER_CAVE, new ProbabilityFeatureConfiguration(0.15F));
        this.underwaterCanyon = carvers.register("underwater_canyon", TropicraftCarvers.UNDERWATER_CANYON, new ProbabilityFeatureConfiguration(0.02F));
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
