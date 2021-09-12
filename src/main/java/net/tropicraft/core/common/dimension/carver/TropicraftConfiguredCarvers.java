package net.tropicraft.core.common.dimension.carver;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ICarverConfig;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftConfiguredCarvers {
    public final ConfiguredCarver<?> cave;
    public final ConfiguredCarver<?> canyon;
    public final ConfiguredCarver<?> underwaterCave;
    public final ConfiguredCarver<?> underwaterCanyon;

    public TropicraftConfiguredCarvers(WorldgenDataConsumer<? extends ConfiguredCarver<?>> worldgen) {
        Register carvers = new Register(worldgen);

        this.cave = carvers.register("cave", TropicraftCarvers.CAVE, new ProbabilityConfig(0.25F));
        this.canyon = carvers.register("canyon", TropicraftCarvers.CANYON, new ProbabilityConfig(0.02F));
        this.underwaterCave = carvers.register("underwater_cave", TropicraftCarvers.UNDERWATER_CAVE, new ProbabilityConfig(0.15F));
        this.underwaterCanyon = carvers.register("underwater_canyon", TropicraftCarvers.UNDERWATER_CANYON, new ProbabilityConfig(0.02F));
    }

    public void addLand(BiomeGenerationSettings.Builder generation) {
        generation.withCarver(GenerationStage.Carving.AIR, this.cave);
        generation.withCarver(GenerationStage.Carving.AIR, this.canyon);
    }

    public void addUnderwater(BiomeGenerationSettings.Builder generation) {
        generation.withCarver(GenerationStage.Carving.LIQUID, this.underwaterCave);
        generation.withCarver(GenerationStage.Carving.LIQUID, this.underwaterCanyon);
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredCarver<?>> worldgen;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredCarver<?>> worldgen) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredCarver<?>>) worldgen;
        }

        public <C extends ICarverConfig, WC extends WorldCarver<C>> ConfiguredCarver<?> register(String id, RegistryObject<WC> carver, C config) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), carver.get().func_242761_a(config));
        }
    }
}
