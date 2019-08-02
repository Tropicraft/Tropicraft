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
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

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
        final Layer extraLayer = new Layer(immutablelist.get(2));
        return new Layer[]{noiseLayer, blockLayer, extraLayer};
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildTropicsProcedure(final WorldType type, final TropicraftGeneratorSettings settings, final LongFunction<C> context) {
        IAreaFactory<T> islandLayer = TropicsIslandLayer.INSTANCE.apply(context.apply(1));
        islandLayer = ZoomLayer.FUZZY.apply(context.apply(2000), islandLayer);
        islandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(2), islandLayer);
        IAreaFactory<T> addIslandLayer = TropicraftAddIslandLayer.BASIC_3.apply(context.apply(3), islandLayer);
        IAreaFactory<T> zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2000), addIslandLayer);
        islandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(55), zoomLayer);
        // TODO - maybe add a similar RemoveTooMuchOceanLayer here?

        IAreaFactory<T> lakeLayer = TropicraftAddInlandLayer.INSTANCE.apply(context.apply(9), islandLayer);
        addIslandLayer = TropicraftAddIslandLayer.RAINFOREST_13.apply(context.apply(6), lakeLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2001), addIslandLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2004), zoomLayer);
        islandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(7), zoomLayer);
        islandLayer = TropicraftAddIslandLayer.BASIC_2.apply(context.apply(8), islandLayer);
        islandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(10), islandLayer);
        // TODO kelp forest check inside below vvv
        islandLayer = TropicraftBiomesLayer.INSTANCE.apply(context.apply(15), islandLayer);
        islandLayer = TropicraftAddWeightedSubBiomesLayer.RAINFOREST.apply(context.apply(16), islandLayer);
        islandLayer = TropicraftAddSubBiomesLayer.OCEANS.apply(context.apply(17), islandLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2002), islandLayer);
        islandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(10), zoomLayer);

        for (int i = 0; i < 4; i++) {
            islandLayer = TropicraftExpandIslandLayer.INSTANCE.apply(context.apply(10), zoomLayer);
        }

        IAreaFactory<T> riverLayer = islandLayer;
        riverLayer = TropicraftRiverInitLayer.INSTANCE.apply(context.apply(12), riverLayer);
        riverLayer = magnify(1000, ZoomLayer.NORMAL, riverLayer, 5, context);
        riverLayer = TropicraftRiverLayer.INSTANCE.apply(context.apply(13), riverLayer);
        riverLayer = SmoothLayer.INSTANCE.apply(context.apply(2008L), riverLayer);

        islandLayer = magnify(2007L, ZoomLayer.NORMAL, islandLayer, 3, context);
        islandLayer = TropicraftBeachLayer.INSTANCE.apply(context.apply(20), islandLayer);
        islandLayer = SmoothLayer.INSTANCE.apply(context.apply(17L), islandLayer);
        islandLayer = TropicraftRiverMixLayer.INSTANCE.apply(context.apply(5), islandLayer, riverLayer);

        final IAreaFactory<T> blockLayer = VoroniZoomLayer.INSTANCE.apply(context.apply(10), islandLayer);

        return ImmutableList.of(islandLayer, blockLayer, islandLayer);
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
