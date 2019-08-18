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
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;

import java.util.function.LongFunction;

public class TropicraftLayerUtil {

    protected static final int OCEAN_ID = Registry.BIOME.getId(TropicraftBiomes.TROPICS_OCEAN);
    protected static final int LAND_ID = Registry.BIOME.getId(TropicraftBiomes.TROPICS);
    protected static final int RIVER_ID = Registry.BIOME.getId(TropicraftBiomes.TROPICS_RIVER);
    protected static final int BEACH_ID = Registry.BIOME.getId(TropicraftBiomes.TROPICS_BEACH);
    protected static final int ISLAND_MOUNTAINS_ID = Registry.BIOME.getId(TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS);
    protected static final int RAINFOREST_PLAINS_ID = Registry.BIOME.getId(TropicraftBiomes.RAINFOREST_PLAINS);
    protected static final int RAINFOREST_HILLS_ID = Registry.BIOME.getId(TropicraftBiomes.RAINFOREST_HILLS);
    protected static final int RAINFOREST_MOUNTAINS_ID = Registry.BIOME.getId(TropicraftBiomes.RAINFOREST_MOUNTAINS);
    protected static final int[] TROPICS_LAND_IDS = new int[]{LAND_ID, RAINFOREST_PLAINS_ID};
    protected static final int[] RAINFOREST_IDS = new int[] {
      //      RAINFOREST_PLAINS_ID,
            RAINFOREST_HILLS_ID,
            RAINFOREST_MOUNTAINS_ID,
            //RAINFOREST_ISLAND_MOUNTAINS_ID
    };

    public static Layer[] buildTropicsProcedure(long seed, WorldType type, TropicraftGeneratorSettings settings) {
        final ImmutableList<IAreaFactory<LazyArea>> immutablelist = buildTropicsProcedure(type, settings, procedure -> new LazyAreaLayerContext(25, seed, procedure));
        final Layer noiseLayer = new Layer(immutablelist.get(0));
        final Layer blockLayer = new Layer(immutablelist.get(1));
        return new Layer[]{noiseLayer, blockLayer};
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildTropicsProcedure(final WorldType type, final TropicraftGeneratorSettings settings, final LongFunction<C> context) {
        IAreaFactory<T> islandLayer = TropicsIslandLayer.INSTANCE.apply(context.apply(1));
        IAreaFactory<T> fuzzyZoomLayer = ZoomLayer.FUZZY.apply(context.apply(2000), islandLayer);
        IAreaFactory<T> addIslandLayer = TropicraftAddIslandLayer.BASIC_3.apply(context.apply(3), fuzzyZoomLayer);
        IAreaFactory<T> zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2000), addIslandLayer);

        IAreaFactory<T> oceanLayer = TropicraftAddInlandLayer.INSTANCE.apply(context.apply(9), zoomLayer);
        oceanLayer = ZoomLayer.NORMAL.apply(context.apply(9), oceanLayer);
        addIslandLayer = TropicraftAddIslandLayer.RAINFOREST_13.apply(context.apply(6), oceanLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2001), addIslandLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2004), zoomLayer);
        addIslandLayer = TropicraftAddIslandLayer.BASIC_2.apply(context.apply(8), zoomLayer);

        IAreaFactory<T> biomeLayerGen = TropicraftBiomesLayer.INSTANCE.apply(context.apply(15), addIslandLayer);
        IAreaFactory<T> oceanLayerGen = TropicraftAddWeightedSubBiomesLayer.OCEANS.apply(context.apply(16), biomeLayerGen);
        IAreaFactory<T> hillsLayerGen = TropicraftAddSubBiomesLayer.RAINFOREST.apply(context.apply(17), oceanLayerGen);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2002), hillsLayerGen);

        IAreaFactory<T> riverLayer = zoomLayer;
        riverLayer = TropicraftRiverInitLayer.INSTANCE.apply(context.apply(12), riverLayer);
        riverLayer = magnify(2007, ZoomLayer.NORMAL, riverLayer, 5, context);
        riverLayer = TropicraftRiverLayer.INSTANCE.apply(context.apply(13), riverLayer);
        riverLayer = SmoothLayer.INSTANCE.apply(context.apply(2008L), riverLayer);

        IAreaFactory<T> magnifyLayer = magnify(2007L, ZoomLayer.NORMAL, zoomLayer, 3, context);
        IAreaFactory<T> biomeLayer = TropicraftBeachLayer.INSTANCE.apply(context.apply(20), magnifyLayer);
        biomeLayer = magnify(20, ZoomLayer.NORMAL, biomeLayer, 2, context);

        biomeLayer = SmoothLayer.INSTANCE.apply(context.apply(17L), biomeLayer);
        biomeLayer = TropicraftRiverMixLayer.INSTANCE.apply(context.apply(17), biomeLayer, riverLayer);

        final IAreaFactory<T> blockLayer = TropicraftVoronoiZoomLayer.INSTANCE.apply(context.apply(10), biomeLayer);

        return ImmutableList.of(biomeLayer, blockLayer);
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> magnify(final long seed, final IAreaTransformer1 zoomLayer, final IAreaFactory<T> layer, final int count, final LongFunction<C> context) {
        IAreaFactory<T> result = layer;
        for (int i = 0; i < count; i++) {
            result = zoomLayer.apply(context.apply(seed + i), result);
        }
        return result;
    }

    public static boolean isOcean(final int biome) {
        return biome == OCEAN_ID;
    }

    public static boolean isRiver(final int biome) {
        return biome == RIVER_ID;
    }

    public static boolean isLand(final int biome) {
        return biome == LAND_ID;
    }
}
