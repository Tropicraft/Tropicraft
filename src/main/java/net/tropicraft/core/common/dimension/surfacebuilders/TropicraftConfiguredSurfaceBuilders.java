package net.tropicraft.core.common.dimension.surfacebuilders;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftConfiguredSurfaceBuilders {
    private static final LazyValue<BlockState> PURIFIED_SAND = new LazyValue<>(() -> TropicraftBlocks.PURIFIED_SAND.get().getDefaultState());
    private static final LazyValue<BlockState> UNDERWATER_PURIFIED_SAND = new LazyValue<>(() -> PURIFIED_SAND.getValue().with(BlockTropicraftSand.UNDERWATER, true));

    public final ConfiguredSurfaceBuilder<?> tropics;
    public final ConfiguredSurfaceBuilder<?> sandy;

    public final ConfiguredSurfaceBuilder<?> mangrove;

    public TropicraftConfiguredSurfaceBuilders(WorldgenDataConsumer<ConfiguredSurfaceBuilder<?>> worldgen) {
        Register surfaceBuilders = new Register(worldgen);

        BlockState grass = Blocks.GRASS_BLOCK.getDefaultState();
        BlockState dirt = Blocks.DIRT.getDefaultState();
        BlockState stone = Blocks.STONE.getDefaultState();

        SurfaceBuilderConfig landConfig = new SurfaceBuilderConfig(grass, dirt, stone);
        SurfaceBuilderConfig sandyConfig = new SurfaceBuilderConfig(PURIFIED_SAND.getValue(), PURIFIED_SAND.getValue(), UNDERWATER_PURIFIED_SAND.getValue());
        SurfaceBuilderConfig sandyUnderwaterConfig = new SurfaceBuilderConfig(UNDERWATER_PURIFIED_SAND.getValue(), UNDERWATER_PURIFIED_SAND.getValue(), UNDERWATER_PURIFIED_SAND.getValue());

        TropicsSurfaceBuilder.Config tropicsConfig = new TropicsSurfaceBuilder.Config(landConfig, sandyConfig, sandyUnderwaterConfig);

        this.tropics = surfaceBuilders.register("tropics", TropicraftSurfaceBuilders.TROPICS, tropicsConfig);
        this.sandy = surfaceBuilders.register("sandy", TropicraftSurfaceBuilders.UNDERWATER,
                new UnderwaterSurfaceBuilder.Config(sandyConfig, landConfig, sandyUnderwaterConfig)
        );

        this.mangrove = surfaceBuilders.register("mangrove", TropicraftSurfaceBuilders.MANGROVE, new SurfaceBuilderConfig(grass, dirt, dirt));
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredSurfaceBuilder<?>> worldgen;

        Register(WorldgenDataConsumer<ConfiguredSurfaceBuilder<?>> worldgen) {
            this.worldgen = worldgen;
        }

        public <C extends ISurfaceBuilderConfig, S extends SurfaceBuilder<C>> ConfiguredSurfaceBuilder<?> register(String id, RegistryObject<S> surfaceBuilder, C config) {
            return this.register(id, surfaceBuilder.get(), config);
        }

        public <C extends ISurfaceBuilderConfig, S extends SurfaceBuilder<C>> ConfiguredSurfaceBuilder<?> register(String id, S surfaceBuilder, C config) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), surfaceBuilder.func_242929_a(config));
        }
    }
}
