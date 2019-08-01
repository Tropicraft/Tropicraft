package net.tropicraft.core.common.dimension.layer;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import java.util.function.LongFunction;

public class TropicraftLayerUtil {

    protected static final int OCEAN_ID = Registry.BIOME.getId(TropicraftBiomes.TROPICS_OCEAN);
    protected static final int LAND_ID = Registry.BIOME.getId(TropicraftBiomes.TROPICS);

    public static Layer[] buildTropicsProcedure(long seed, WorldType type, TropicraftGeneratorSettings settings) {
        final ImmutableList<IAreaFactory<LazyArea>> immutablelist = buildTropicsProcedure(type, settings, procedure -> new LazyAreaLayerContext(25, seed, procedure));
        final Layer noiseLayer = new Layer(immutablelist.get(0));
        final Layer blockLayer = new Layer(immutablelist.get(1));
        final Layer extraLayer = new Layer(immutablelist.get(2));
        return new Layer[]{noiseLayer, blockLayer, extraLayer};
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildTropicsProcedure(final WorldType type, final TropicraftGeneratorSettings settings, final LongFunction<C> context) {
        IAreaFactory<T> islandLayer = TropicsIslandLayer.INSTANCE.apply(context.apply(1));

        IAreaFactory<T> biomeLayer = islandLayer;
        final IAreaFactory<T> blockLayer = VoroniZoomLayer.INSTANCE.apply(context.apply(10), biomeLayer);

        return ImmutableList.of(islandLayer, blockLayer, biomeLayer);
    }
}
