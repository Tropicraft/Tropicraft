package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TropicraftFeatureUtil {
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder.Reference<ConfiguredFeature<?, ?>> register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, DeferredHolder<Feature<?>, F> feature, FC config) {
        return context.register(key, new ConfiguredFeature<>(feature.get(), config));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder.Reference<ConfiguredFeature<?, ?>> register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        return context.register(key, new ConfiguredFeature<>(feature, config));
    }

    public static <F extends Feature<NoneFeatureConfiguration>> Holder.Reference<ConfiguredFeature<?, ?>> register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, DeferredHolder<Feature<?>, F> feature) {
        return context.register(key, new ConfiguredFeature<>(feature.get(), NoneFeatureConfiguration.INSTANCE));
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandom(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, List<Holder<PlacedFeature>> choices) {
        return context.register(key, randomFeature(choices));
    }

    public static ConfiguredFeature<?, ?> randomFeature(List<Holder<PlacedFeature>> choices) {
        if (choices.size() == 2) {
            Holder<PlacedFeature> left = choices.get(0);
            Holder<PlacedFeature> right = choices.get(1);
            return new ConfiguredFeature<>(Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(left, right));
        } else {
            return new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(choices)));
        }
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandom(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, List<Pair<ResourceKey<PlacedFeature>, Float>> choices, ResourceKey<PlacedFeature> defaultFeature) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        return registerRandomPlaced(context, key,
                choices.stream().map(pair -> {
                    Holder<PlacedFeature> holder = placedFeatures.getOrThrow(pair.getFirst());
                    return new WeightedPlacedFeature(holder, pair.getSecond());
                }).toList(),
                defaultFeature
        );
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandomPlaced(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, List<WeightedPlacedFeature> choices, ResourceKey<PlacedFeature> defaultFeatureKey) {
        Holder<PlacedFeature> defaultFeature = context.lookup(Registries.PLACED_FEATURE).getOrThrow(defaultFeatureKey);
        return register(context, key, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(choices, defaultFeature));
    }

    @SafeVarargs
    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandom(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, ResourceKey<ConfiguredFeature<?, ?>>... choiceKeys) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        return registerRandom(context, key, Arrays.stream(choiceKeys).map(k -> PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(k))).toList());
    }

    @SafeVarargs
    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandomPlaced(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, ResourceKey<PlacedFeature>... choices) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        return registerRandom(context, key, Arrays.stream(choices).map(placedFeatures::getOrThrow).collect(Collectors.toList()));
    }

    public static RandomPatchConfiguration randomPatch(Supplier<? extends Block> block) {
        return randomPatch(BlockStateProvider.simple(block.get()));
    }

    public static RandomPatchConfiguration randomPatch(BlockStateProvider blockStateProvider) {
        return FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(blockStateProvider));
    }

    public static OreConfiguration ore(int blobSize, Supplier<? extends Block> block) {
        // TODO add deepslate / tropicraft equivalent replacement here
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        return new OreConfiguration(List.of(OreConfiguration.target(stoneOreReplaceables, block.get().defaultBlockState())), blobSize);
    }

    public static BlockStateProvider stateProvider(Supplier<? extends Block> block) {
        return BlockStateProvider.simple(block.get());
    }

    public static boolean goesBeyondWorldSize(WorldGenLevel world, int y, int height) {
        return y < world.getMinBuildHeight() + 1 || y + height + 1 > world.getMaxBuildHeight();
    }

    public static boolean isBBAvailable(WorldGenLevel world, BlockPos pos, int height) {
        for (int y = 0; y <= 1 + height; y++) {
            BlockPos checkPos = pos.above(y);
            int size = 1;
            if (checkPos.getY() < world.getMinBuildHeight() || checkPos.getY() >= world.getMaxBuildHeight()) {
                return false;
            }

            if (y == 0) {
                size = 0;
            }

            if (y >= (1 + height) - 2) {
                size = 2;
            }

            if (BlockPos.betweenClosedStream(checkPos.offset(-size, 0, -size), checkPos.offset(size, 0, size)).anyMatch(p -> !TreeFeature.isAirOrLeaves(world, p))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isSoil(LevelAccessor world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.PODZOL;
    }
}
