package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.Arrays;
import java.util.List;

public class TropicraftPlacementUtil {
    @SafeVarargs
    public static Holder.Reference<PlacedFeature> registerRandomChecked(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<PlacedFeature>... choices) {
        Holder<ConfiguredFeature<?, ?>> randomConfigured = Holder.direct(TropicraftFeatureUtil.randomFeature(Arrays.asList(choices)));
        return context.register(key, new PlacedFeature(randomConfigured, List.of()));
    }

    public static Holder.Reference<PlacedFeature> register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> featureKey, List<PlacementModifier> placement) {
        Holder<ConfiguredFeature<?, ?>> feature = context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(featureKey);
        return context.register(key, new PlacedFeature(feature, placement));
    }

    public static List<PlacementModifier> sparseTreePlacement(float chance) {
        return treePlacement(0, chance, 1);
    }

    public static List<PlacementModifier> treePlacement(int count, float extraChance, int extraCount) {
        return List.of(
                PlacementUtils.countExtra(count, extraChance, extraCount),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        );
    }

    public static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
        return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier height) {
        return orePlacement(CountPlacement.of(count), height);
    }
}
