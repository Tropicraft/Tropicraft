package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;

public interface TropicraftDensityFunctionTypes {
    DeferredRegister<MapCodec<? extends DensityFunction>> REGISTER = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, Tropicraft.ID);

    DeferredHolder<MapCodec<? extends DensityFunction>, ?> VORONOI = REGISTER.register("voronoi", () -> Voronoi.CODEC);
}
