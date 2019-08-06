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
        IAreaFactory<T> expandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(2), fuzzyZoomLayer);
        IAreaFactory<T> addIslandLayer = TropicraftAddIslandLayer.BASIC_3.apply(context.apply(3), expandLayer);
        IAreaFactory<T> zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2000), addIslandLayer);
        expandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(55), zoomLayer);
        // TODO - maybe add a similar RemoveTooMuchOceanLayer here?

        IAreaFactory<T> oceanLayer = TropicraftAddInlandLayer.INSTANCE.apply(context.apply(9), expandLayer);
        addIslandLayer = TropicraftAddIslandLayer.RAINFOREST_13.apply(context.apply(6), oceanLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2001), addIslandLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2004), zoomLayer);
        expandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(7), zoomLayer);
        addIslandLayer = TropicraftAddIslandLayer.BASIC_2.apply(context.apply(8), expandLayer);
        expandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(10), addIslandLayer);
        // TODO kelp forest check inside below vvv
        IAreaFactory<T> biomeLayerGen = TropicraftBiomesLayer.INSTANCE.apply(context.apply(15), expandLayer);
        IAreaFactory<T> oceanLayerGen = TropicraftAddWeightedSubBiomesLayer.OCEANS.apply(context.apply(16), biomeLayerGen);
        IAreaFactory<T> hillsLayerGen = TropicraftAddSubBiomesLayer.RAINFOREST.apply(context.apply(17), oceanLayerGen);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2002), hillsLayerGen);
        expandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(10), zoomLayer);

        for (int i = 0; i < settings.getBiomeSize(); i++) {
            expandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(10), expandLayer);
        }

        IAreaFactory<T> riverLayer = TropicraftRiverInitLayer.INSTANCE.apply(context.apply(12), expandLayer);
        riverLayer = magnify(2007, ZoomLayer.NORMAL, riverLayer, 5, context);
        riverLayer = TropicraftRiverLayer.INSTANCE.apply(context.apply(13), riverLayer);
        IAreaFactory<T> riverSmoothLayer = SmoothLayer.INSTANCE.apply(context.apply(2008L), riverLayer);

        IAreaFactory<T> magnifyLayer = magnify(2007L, ZoomLayer.NORMAL, expandLayer, 3, context);
        IAreaFactory<T> beachlayer = TropicraftBeachLayer.INSTANCE.apply(context.apply(20), magnifyLayer);
        IAreaFactory<T> smoothingBiomesLayer = SmoothLayer.INSTANCE.apply(context.apply(17L), beachlayer);
        IAreaFactory<T> riverMixLayer = TropicraftRiverMixLayer.INSTANCE.apply(context.apply(5), smoothingBiomesLayer, riverSmoothLayer);

        final IAreaFactory<T> blockLayer = TropicraftVoronoiZoomLayer.INSTANCE.apply(context.apply(10), riverMixLayer);

        return ImmutableList.of(riverMixLayer, blockLayer);
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
