package net.tropicraft.core.common.dimension.carver;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CanyonWorldCarver;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;

public class TropicraftCarvers {

    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(Registries.CARVER, Tropicraft.ID);

    public static final DeferredHolder<WorldCarver<?>, CaveWorldCarver> CAVE = CARVERS.register("cave", () -> new CaveWorldCarver(CaveCarverConfiguration.CODEC));
    public static final DeferredHolder<WorldCarver<?>, CanyonWorldCarver> CANYON = CARVERS.register("canyon", () -> new CanyonWorldCarver(CanyonCarverConfiguration.CODEC));
}
