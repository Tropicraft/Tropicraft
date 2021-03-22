package net.tropicraft.core.common.dimension.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

import java.util.function.LongFunction;

public class TropicraftLayerUtil {
    public static Layer buildTropicsProcedure(long seed, Registry<Biome> biomes) {
        TropicraftBiomeIds biomeIds = new TropicraftBiomeIds(biomes);
        final IAreaFactory<LazyArea> noiseLayer = buildTropicsProcedure(biomeIds, procedure -> new LazyAreaLayerContext(25, seed, procedure));
        return new TropicraftLookupLayer(noiseLayer);
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildTropicsProcedure(TropicraftBiomeIds biomeIds, final LongFunction<C> context) {
        IAreaFactory<T> islandLayer = new TropicsIslandLayer(biomeIds).apply(context.apply(1));
        IAreaFactory<T> fuzzyZoomLayer = ZoomLayer.FUZZY.apply(context.apply(2000), islandLayer);
        IAreaFactory<T> addIslandLayer = TropicraftAddIslandLayer.basic3(biomeIds).apply(context.apply(3), fuzzyZoomLayer);
        IAreaFactory<T> zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2000), addIslandLayer);

        IAreaFactory<T> oceanLayer = new TropicraftAddInlandLayer(20, biomeIds).apply(context.apply(9), zoomLayer);
        oceanLayer = ZoomLayer.NORMAL.apply(context.apply(9), oceanLayer);
        addIslandLayer = TropicraftAddIslandLayer.rainforest13(biomeIds).apply(context.apply(6), oceanLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2001), addIslandLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2004), zoomLayer);
        addIslandLayer = TropicraftAddIslandLayer.basic2(biomeIds).apply(context.apply(8), zoomLayer);

        IAreaFactory<T> biomeLayerGen = new TropicraftBiomesLayer(biomeIds).apply(context.apply(15), addIslandLayer);
        IAreaFactory<T> oceanLayerGen = TropicraftAddWeightedSubBiomesLayer.ocean(biomeIds).apply(context.apply(16), biomeLayerGen);
        IAreaFactory<T> hillsLayerGen = TropicraftAddSubBiomesLayer.rainforest(biomeIds).apply(context.apply(17), oceanLayerGen);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(2002), hillsLayerGen);

        IAreaFactory<T> riverLayer = zoomLayer;
        riverLayer = new TropicraftRiverInitLayer(biomeIds).apply(context.apply(12), riverLayer);
        riverLayer = magnify(2007, ZoomLayer.NORMAL, riverLayer, 5, context);
        riverLayer = new TropicraftRiverLayer(biomeIds).apply(context.apply(13), riverLayer);
        riverLayer = SmoothLayer.INSTANCE.apply(context.apply(2008L), riverLayer);

        IAreaFactory<T> magnifyLayer = magnify(2007L, ZoomLayer.NORMAL, zoomLayer, 3, context);
        IAreaFactory<T> biomeLayer = new TropicraftBeachLayer(biomeIds).apply(context.apply(20), magnifyLayer);
        biomeLayer = magnify(20, ZoomLayer.NORMAL, biomeLayer, 2, context);

        biomeLayer = SmoothLayer.INSTANCE.apply(context.apply(17L), biomeLayer);
        biomeLayer = new TropicraftRiverMixLayer(biomeIds).apply(context.apply(17), biomeLayer, riverLayer);

        return biomeLayer;
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> magnify(final long seed, final IAreaTransformer1 zoomLayer, final IAreaFactory<T> layer, final int count, final LongFunction<C> context) {
        IAreaFactory<T> result = layer;
        for (int i = 0; i < count; i++) {
            result = zoomLayer.apply(context.apply(seed + i), result);
        }
        return result;
    }
}
