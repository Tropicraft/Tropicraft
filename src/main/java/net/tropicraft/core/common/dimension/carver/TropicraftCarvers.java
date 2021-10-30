package net.tropicraft.core.common.dimension.carver;

import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

public class TropicraftCarvers {

    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, Constants.MODID);

    public static final RegistryObject<TropicsCaveCarver> CAVE = CARVERS.register("cave", () -> new TropicsCaveCarver(ProbabilityFeatureConfiguration.CODEC));
    public static final RegistryObject<TropicsCanyonCarver> CANYON = CARVERS.register("canyon", () -> new TropicsCanyonCarver(ProbabilityFeatureConfiguration.CODEC));

    public static final RegistryObject<TropicsUnderwaterCaveCarver> UNDERWATER_CAVE = CARVERS.register("underwater_cave", () -> new TropicsUnderwaterCaveCarver(ProbabilityFeatureConfiguration.CODEC));
    public static final RegistryObject<TropicsUnderwaterCanyonCarver> UNDERWATER_CANYON = CARVERS.register("underwater_canyon", () -> new TropicsUnderwaterCanyonCarver(ProbabilityFeatureConfiguration.CODEC));

}
