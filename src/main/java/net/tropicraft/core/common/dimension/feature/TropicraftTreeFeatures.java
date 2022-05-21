package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftTrees;
import net.tropicraft.core.common.dimension.feature.tree.*;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.*;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

import static net.tropicraft.core.common.block.TropicraftTrees.BEEHIVE_002;

public final class TropicraftTreeFeatures {
    public static final TropicraftFeatures.Register REGISTER = TropicraftFeatures.Register.create();

    public static final RegistryObject<ConfiguredFeature<?, ?>> GRAPEFRUIT_TREE = REGISTER.feature("grapefruit_tree", Feature.TREE, () -> fruitTree(TropicraftBlocks.GRAPEFRUIT_LEAVES));
    public static final RegistryObject<ConfiguredFeature<?, ?>> ORANGE_TREE = REGISTER.feature("orange_tree", Feature.TREE, () -> fruitTree(TropicraftBlocks.ORANGE_LEAVES));
    public static final RegistryObject<ConfiguredFeature<?, ?>> LEMON_TREE = REGISTER.feature("lemon_tree", Feature.TREE, () -> fruitTree(TropicraftBlocks.LEMON_LEAVES));
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIME_TREE = REGISTER.feature("lime_tree", Feature.TREE, () -> fruitTree(TropicraftBlocks.LIME_LEAVES));

    public static final RegistryObject<ConfiguredFeature<?, ?>> NORMAL_PALM_TREE = REGISTER.feature("normal_palm_tree", TropicraftFeatures.NORMAL_PALM_TREE);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_TREE = REGISTER.feature("curved_palm_tree", TropicraftFeatures.CURVED_PALM_TREE);
    public static final RegistryObject<ConfiguredFeature<?, ?>> LARGE_PALM_TREE = REGISTER.feature("large_palm_tree", TropicraftFeatures.LARGE_PALM_TREE);
    public static final RegistryObject<ConfiguredFeature<?, ?>> PALM_TREE = REGISTER.randomFeature("palm_trees", NORMAL_PALM_TREE, CURVED_PALM_TREE, LARGE_PALM_TREE);

    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINFOREST_UP_TREE = REGISTER.feature("rainforest_up_tree", TropicraftFeatures.UP_TREE);
    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINFOREST_SMALL_TUALUNG = REGISTER.feature("rainforest_small_tualung", TropicraftFeatures.SMALL_TUALUNG);
    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINFOREST_LARGE_TUALUNG = REGISTER.feature("rainforest_large_tualung", TropicraftFeatures.LARGE_TUALUNG);
    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINFOREST_TALL_TREE = REGISTER.feature("rainforest_tall_tree", TropicraftFeatures.TALL_TREE);

    public static final RegistryObject<ConfiguredFeature<?, ?>> PLEODENDRON = REGISTER.feature("pleodendron", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(Blocks.JUNGLE_LOG.defaultBlockState()),
                    new PleodendronTrunkPlacer(10, 8, 0),
                    BlockStateProvider.simple(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                    new PleodendronFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1),
                    new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
            ).build());

    public static final RegistryObject<ConfiguredFeature<?, ?>> PAPAYA = REGISTER.feature("papaya", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LOG.get().defaultBlockState()),
                    new StraightTrunkPlacer(5, 2, 3),
                    BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LEAVES.get().defaultBlockState()),
                    new PapayaFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
            )
                    .decorators(ImmutableList.of(TropicraftTrees.BEEHIVE_005, new PapayaTreeDecorator()))
                    .build());

    private static final Supplier<FoliagePlacer> MANGROVE_FOLIAGE = () -> new MangroveFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0));
    private static final Supplier<MangroveTrunkPlacer> RED_MANGROVE_TRUNK = () -> new MangroveTrunkPlacer(3, 3, 0, TropicraftBlocks.RED_MANGROVE_ROOTS.get(), true, false);

    private static final TwoLayersFeatureSize MANGROVE_MINIMUM_SIZE = new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4));

    public static final RegistryObject<ConfiguredFeature<?, ?>> RED_MANGROVE_SHORT = REGISTER.feature("red_mangrove_short", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    REGISTER.stateProvider(TropicraftBlocks.RED_MANGROVE_LOG),
                    RED_MANGROVE_TRUNK.get(),
                    REGISTER.stateProvider(TropicraftBlocks.RED_MANGROVE_LEAVES),
                    MANGROVE_FOLIAGE.get(),
                    MANGROVE_MINIMUM_SIZE
            )
                    .decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR))
                    .build());

    public static final RegistryObject<ConfiguredFeature<?, ?>> RED_MANGROVE_SMALL = REGISTER.feature("red_mangrove_small", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    REGISTER.stateProvider(TropicraftBlocks.RED_MANGROVE_LOG),
                    new SmallMangroveTrunkPlacer(2, 1, 0, TropicraftBlocks.RED_MANGROVE_ROOTS.get()),
                    REGISTER.stateProvider(TropicraftBlocks.RED_MANGROVE_LEAVES),
                    new SmallMangroveFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),
                    MANGROVE_MINIMUM_SIZE
            )
                    .decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.SMALL))
                    .build());
    public static final RegistryObject<ConfiguredFeature<?, ?>> RED_MANGROVE = REGISTER.randomFeature("red_mangrove", RED_MANGROVE_SHORT, RED_MANGROVE_SMALL);

    public static final RegistryObject<ConfiguredFeature<?, ?>> TALL_MANGROVE = REGISTER.feature("tall_mangrove", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    REGISTER.stateProvider(TropicraftBlocks.LIGHT_MANGROVE_LOG),
                    new MangroveTrunkPlacer(7, 4, 2, TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get(), false, false),
                    REGISTER.stateProvider(TropicraftBlocks.TALL_MANGROVE_LEAVES),
                    MANGROVE_FOLIAGE.get(),
                    MANGROVE_MINIMUM_SIZE
            )
                    .decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR))
                    .build());

    public static final RegistryObject<ConfiguredFeature<?, ?>> TEA_MANGROVE = REGISTER.feature("tea_mangrove", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    REGISTER.stateProvider(TropicraftBlocks.LIGHT_MANGROVE_LOG),
                    new MangroveTrunkPlacer(5, 3, 0, TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get(), false, true),
                    REGISTER.stateProvider(TropicraftBlocks.TEA_MANGROVE_LEAVES),
                    MANGROVE_FOLIAGE.get(),
                    MANGROVE_MINIMUM_SIZE
            )
                    .decorators(List.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR, new PneumatophoresTreeDecorator(TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get(), 2, 6, 4)))
                    .build());

    public static final RegistryObject<ConfiguredFeature<?, ?>> BLACK_MANGROVE = REGISTER.feature("black_mangrove", Feature.TREE, () ->
            new TreeConfiguration.TreeConfigurationBuilder(
                    REGISTER.stateProvider(TropicraftBlocks.BLACK_MANGROVE_LOG),
                    new MangroveTrunkPlacer(4, 3, 0, TropicraftBlocks.BLACK_MANGROVE_ROOTS.get(), true, false),
                    REGISTER.stateProvider(TropicraftBlocks.BLACK_MANGROVE_LEAVES),
                    MANGROVE_FOLIAGE.get(),
                    MANGROVE_MINIMUM_SIZE
            )
                    .decorators(List.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR, new PneumatophoresTreeDecorator(TropicraftBlocks.BLACK_MANGROVE_ROOTS.get(), 8, 16, 6)))
                    .build());

    private static TreeConfiguration fruitTree(Supplier<? extends Block> fruitLeaves) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG.defaultBlockState()),
                new CitrusTrunkPlacer(6, 3, 0),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1)
                        .add(fruitLeaves.get().defaultBlockState(), 1)
                        .build()
                ),
                new CitrusFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).build();
    }
}
