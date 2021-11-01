package net.tropicraft.core.common.dimension.surfacebuilders;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftConfiguredSurfaceBuilders {
    private static final LazyLoadedValue<BlockState> PURIFIED_SAND = new LazyLoadedValue<>(() -> TropicraftBlocks.PURIFIED_SAND.get().defaultBlockState());
    private static final LazyLoadedValue<BlockState> UNDERWATER_PURIFIED_SAND = new LazyLoadedValue<>(() -> PURIFIED_SAND.get().setValue(BlockTropicraftSand.UNDERWATER, true));

    public final ConfiguredSurfaceBuilder<?> tropics;
    public final ConfiguredSurfaceBuilder<?> sandy;

    public final ConfiguredSurfaceBuilder<?> mangrove;

    public TropicraftConfiguredSurfaceBuilders(WorldgenDataConsumer<? extends ConfiguredSurfaceBuilder<?>> worldgen) {
        Register surfaceBuilders = new Register(worldgen);

        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();

        SurfaceBuilderBaseConfiguration landConfig = new SurfaceBuilderBaseConfiguration(grass, dirt, stone);
        SurfaceBuilderBaseConfiguration sandyConfig = new SurfaceBuilderBaseConfiguration(PURIFIED_SAND.get(), PURIFIED_SAND.get(), UNDERWATER_PURIFIED_SAND.get());
        SurfaceBuilderBaseConfiguration sandyUnderwaterConfig = new SurfaceBuilderBaseConfiguration(UNDERWATER_PURIFIED_SAND.get(), UNDERWATER_PURIFIED_SAND.get(), UNDERWATER_PURIFIED_SAND.get());

        TropicsSurfaceBuilder.Config tropicsConfig = new TropicsSurfaceBuilder.Config(landConfig, sandyConfig, sandyUnderwaterConfig);

        this.tropics = surfaceBuilders.register("tropics", TropicraftSurfaceBuilders.TROPICS, tropicsConfig);
        this.sandy = surfaceBuilders.register("sandy", TropicraftSurfaceBuilders.UNDERWATER,
                new UnderwaterSurfaceBuilder.Config(sandyConfig, landConfig, sandyUnderwaterConfig)
        );

        this.mangrove = surfaceBuilders.register("mangrove", TropicraftSurfaceBuilders.MANGROVE, new SurfaceBuilderBaseConfiguration(grass, dirt, dirt));
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredSurfaceBuilder<?>> worldgen;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredSurfaceBuilder<?>> worldgen) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredSurfaceBuilder<?>>) worldgen;
        }

        public <C extends SurfaceBuilderConfiguration, S extends SurfaceBuilder<C>> ConfiguredSurfaceBuilder<?> register(String id, RegistryObject<S> surfaceBuilder, C config) {
            return this.register(id, surfaceBuilder.get(), config);
        }

        public <C extends SurfaceBuilderConfiguration, S extends SurfaceBuilder<C>> ConfiguredSurfaceBuilder<?> register(String id, S surfaceBuilder, C config) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), surfaceBuilder.configured(config));
        }
    }
}
