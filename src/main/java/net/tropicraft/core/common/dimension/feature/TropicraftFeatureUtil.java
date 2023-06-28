package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
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
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TropicraftFeatureUtil {
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder.Reference<ConfiguredFeature<?, ?>> register(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final RegistryObject<F> feature, final FC config) {
        return context.register(key, new ConfiguredFeature<>(feature.get(), config));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder.Reference<ConfiguredFeature<?, ?>> register(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final F feature, final FC config) {
        return context.register(key, new ConfiguredFeature<>(feature, config));
    }

    public static <F extends Feature<NoneFeatureConfiguration>> Holder.Reference<ConfiguredFeature<?, ?>> register(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final RegistryObject<F> feature) {
        return context.register(key, new ConfiguredFeature<>(feature.get(), NoneFeatureConfiguration.INSTANCE));
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandom(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final List<Holder<PlacedFeature>> choices) {
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

    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandom(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final List<Pair<ResourceKey<PlacedFeature>, Float>> choices, final ResourceKey<PlacedFeature> defaultFeature) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        return registerRandomPlaced(context, key,
                choices.stream().map(pair -> {
                    final Holder<PlacedFeature> holder = placedFeatures.getOrThrow(pair.getFirst());
                    return new WeightedPlacedFeature(holder, pair.getSecond());
                }).toList(),
                defaultFeature
        );
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandomPlaced(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final List<WeightedPlacedFeature> choices, final ResourceKey<PlacedFeature> defaultFeatureKey) {
        final Holder<PlacedFeature> defaultFeature = context.lookup(Registries.PLACED_FEATURE).getOrThrow(defaultFeatureKey);
        return register(context, key, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(choices, defaultFeature));
    }

    @SafeVarargs
    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandom(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final ResourceKey<ConfiguredFeature<?, ?>>... choiceKeys) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        return registerRandom(context, key, Arrays.stream(choiceKeys).map(k -> PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(k))).toList());
    }

    @SafeVarargs
    public static Holder.Reference<ConfiguredFeature<?, ?>> registerRandomPlaced(final BootstapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final ResourceKey<PlacedFeature>... choices) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        return registerRandom(context, key, Arrays.stream(choices).map(placedFeatures::getOrThrow).collect(Collectors.toList()));
    }

    public static RandomPatchConfiguration randomPatch(final Supplier<? extends Block> block) {
        return randomPatch(BlockStateProvider.simple(block.get()));
    }

    public static RandomPatchConfiguration randomPatch(final BlockStateProvider blockStateProvider) {
        return FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(blockStateProvider));
    }

    public static OreConfiguration ore(final int blobSize, final Supplier<? extends Block> block) {
        // TODO add deepslate / tropicraft equivalent replacement here
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        return new OreConfiguration(List.of(OreConfiguration.target(stoneOreReplaceables, block.get().defaultBlockState())), blobSize);
    }

    public static BlockStateProvider stateProvider(Supplier<? extends Block> block) {
        return BlockStateProvider.simple(block.get());
    }

    public static boolean goesBeyondWorldSize(final WorldGenLevel world, final int y, final int height) {
        return y < world.getMinBuildHeight() + 1 || y + height + 1 > world.getMaxBuildHeight();
    }

    public static boolean isBBAvailable(final WorldGenLevel world, final BlockPos pos, final int height) {
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

    public static boolean isSoil(final LevelAccessor world, final BlockPos pos) {
        final BlockState blockState = world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.PODZOL;
    }
}
