package net.tropicraft.core.common.dimension.carver;

import net.minecraft.world.level.levelgen.carver.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public class TropicraftCarvers {

    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, Constants.MODID);

    public static final RegistryObject<CaveWorldCarver> CAVE = CARVERS.register("cave", () -> new CaveWorldCarver(CaveCarverConfiguration.CODEC));
    public static final RegistryObject<CanyonWorldCarver> CANYON = CARVERS.register("canyon", () -> new CanyonWorldCarver(CanyonCarverConfiguration.CODEC));

//    public static final RegistryObject<TropicsUnderwaterCaveCarver> UNDERWATER_CAVE = CARVERS.register("underwater_cave", () -> new TropicsUnderwaterCaveCarver(CaveCarverConfiguration.CODEC));
//    public static final RegistryObject<TropicsUnderwaterCanyonCarver> UNDERWATER_CANYON = CARVERS.register("underwater_canyon", () -> new TropicsUnderwaterCanyonCarver(CanyonCarverConfiguration.CODEC));

}
