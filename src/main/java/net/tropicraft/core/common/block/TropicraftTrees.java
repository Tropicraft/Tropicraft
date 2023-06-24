package net.tropicraft.core.common.block;


import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaTreeDecorator;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class TropicraftTrees {
    public static final BeehiveDecorator BEEHIVE_002 = new BeehiveDecorator(0.02F);
    public static final BeehiveDecorator BEEHIVE_005 = new BeehiveDecorator(0.05F);

    public static final AbstractTreeGrower GRAPEFRUIT = createFruit(TropicraftBlocks.GRAPEFRUIT_LEAVES);
    public static final AbstractTreeGrower LEMON = createFruit(TropicraftBlocks.LEMON_LEAVES);
    public static final AbstractTreeGrower LIME = createFruit(TropicraftBlocks.LIME_LEAVES);
    public static final AbstractTreeGrower ORANGE = createFruit(TropicraftBlocks.ORANGE_LEAVES);
    public static final AbstractTreeGrower PAPAYA = create((server, random, beehive) -> {
        TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LOG.get()),
                new StraightTrunkPlacer(5, 2, 3),
                BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LEAVES.get()),
                new PapayaFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        ).decorators(ImmutableList.of(BEEHIVE_005, new PapayaTreeDecorator())).build();

        return new ConfiguredFeature<>(Feature.TREE, config);
    });

    public static final AbstractTreeGrower RAINFOREST = create((server, random, beehive) -> {
        final int treeType = random.nextInt(4);
        if (treeType == 0) {
            return new ConfiguredFeature<>(TropicraftFeatures.TALL_TREE.get(), NoneFeatureConfiguration.INSTANCE);
        } else if (treeType == 1) {
            return new ConfiguredFeature<>(TropicraftFeatures.SMALL_TUALUNG.get(), NoneFeatureConfiguration.INSTANCE);
        } else if (treeType == 2) {
            return new ConfiguredFeature<>(TropicraftFeatures.UP_TREE.get(), NoneFeatureConfiguration.INSTANCE);
        } else {
            return new ConfiguredFeature<>(TropicraftFeatures.LARGE_TUALUNG.get(), NoneFeatureConfiguration.INSTANCE);
        }
    });

    public static final AbstractTreeGrower PALM = create((server, random, beehive) -> {
        final int palmType = random.nextInt(3);
        if (palmType == 0) {
            return new ConfiguredFeature<>(TropicraftFeatures.NORMAL_PALM_TREE.get(), NoneFeatureConfiguration.INSTANCE);
        } else if (palmType == 1) {
            return new ConfiguredFeature<>(TropicraftFeatures.CURVED_PALM_TREE.get(), NoneFeatureConfiguration.INSTANCE);
        } else {
            return new ConfiguredFeature<>(TropicraftFeatures.LARGE_PALM_TREE.get(), NoneFeatureConfiguration.INSTANCE);
        }
    });

    public static final AbstractTreeGrower RED_MANGROVE = create("red_mangrove");
    public static final AbstractTreeGrower TALL_MANGROVE = create("tall_mangrove");
    public static final AbstractTreeGrower TEA_MANGROVE = create("tea_mangrove");
    public static final AbstractTreeGrower BLACK_MANGROVE = create("black_mangrove");

    private static AbstractTreeGrower createFruit(Supplier<? extends Block> fruitLeaves) {
        return create((server, random, beehive) -> {
            WeightedStateProvider leaves = new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                            .add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1)
                            .add(fruitLeaves.get().defaultBlockState(), 1)
            );

            TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(Blocks.OAK_LOG),
                    new CitrusTrunkPlacer(6, 3, 0),
                    leaves,
                    new CitrusFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    new TwoLayersFeatureSize(1, 0, 2)
            ).build();

            return new ConfiguredFeature<>(Feature.TREE, config);
        });
    }

    private static AbstractTreeGrower create(String id) {
        ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(
                Registry.CONFIGURED_FEATURE_REGISTRY,
                new ResourceLocation(net.tropicraft.Constants.MODID, id)
        );

        return create((server, random, beehive) -> {
            RegistryAccess registries = server.registryAccess();
            Registry<ConfiguredFeature<?, ?>> features = registries.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            return features.get(key);
        });
    }

    private static AbstractTreeGrower create(FeatureProvider featureProvider) {
        return new AbstractTreeGrower() {
            @Nullable
            @Override
            protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean beehive) {
                return null;
            }

            @Override
            public boolean growTree(ServerLevel world, ChunkGenerator generator, BlockPos pos, BlockState sapling, RandomSource random) {
                ConfiguredFeature<?, ?> feature = featureProvider.getFeature(world.getServer(), random, this.hasFlowers(world, pos));
                if (feature == null) {
                    return false;
                }

                world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_INVISIBLE);
                if (feature.place(world, generator, random, pos)) {
                    return true;
                } else {
                    world.setBlock(pos, sapling, Block.UPDATE_INVISIBLE);
                    return false;
                }
            }

            private boolean hasFlowers(LevelAccessor world, BlockPos origin) {
                BlockPos min = origin.offset(-2, -1, -2);
                BlockPos max = origin.offset(2, 1, 2);
                for (BlockPos pos : BlockPos.MutableBlockPos.betweenClosed(min, max)) {
                    if (world.getBlockState(pos).is(BlockTags.FLOWERS)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    interface FeatureProvider {
        @Nullable
        ConfiguredFeature<?, ?> getFeature(MinecraftServer server, RandomSource random, boolean beehive);
    }
}
