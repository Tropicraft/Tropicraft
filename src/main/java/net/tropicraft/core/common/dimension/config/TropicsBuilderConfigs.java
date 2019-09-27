package net.tropicraft.core.common.dimension.config;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicsBuilderConfigs {
    private static final Supplier<BlockState> PURIFIED_SAND = () -> TropicraftBlocks.PURIFIED_SAND.get().getDefaultState();

    public static final Supplier<SurfaceBuilderConfig> PURIFIED_SAND_CONFIG = () -> new SurfaceBuilderConfig(PURIFIED_SAND.get(), PURIFIED_SAND.get(), PURIFIED_SAND.get());

    public static final Supplier<SurfaceBuilderConfig> TROPICS_CONFIG = () -> new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState());
}
