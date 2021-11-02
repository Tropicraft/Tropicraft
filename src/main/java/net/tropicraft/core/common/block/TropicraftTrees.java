package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class TropicraftTrees {
    public static final AbstractTreeGrower GRAPEFRUIT = createFruit(TropicraftBlocks.GRAPEFRUIT_LEAVES);
    public static final AbstractTreeGrower LEMON = createFruit(TropicraftBlocks.LEMON_LEAVES);
    public static final AbstractTreeGrower LIME = createFruit(TropicraftBlocks.LIME_LEAVES);
    public static final AbstractTreeGrower ORANGE = createFruit(TropicraftBlocks.ORANGE_LEAVES);

    public static final AbstractTreeGrower RAINFOREST = create((server, random, beehive) -> {
        final int treeType = random.nextInt(4);
        if (treeType == 0) {
            return TropicraftFeatures.TALL_TREE.get().configured(NoneFeatureConfiguration.INSTANCE);
        } else if (treeType == 1) {
            return TropicraftFeatures.SMALL_TUALUNG.get().configured(NoneFeatureConfiguration.INSTANCE);
        } else if (treeType == 2) {
            return TropicraftFeatures.UP_TREE.get().configured(NoneFeatureConfiguration.INSTANCE);
        } else {
            return TropicraftFeatures.LARGE_TUALUNG.get().configured(NoneFeatureConfiguration.INSTANCE);
        }
    });

    public static final AbstractTreeGrower PALM = create((server, random, beehive) -> {
        final int palmType = random.nextInt(3);
        if (palmType == 0) {
            return TropicraftFeatures.NORMAL_PALM_TREE.get().configured(NoneFeatureConfiguration.INSTANCE);
        } else if (palmType == 1) {
            return TropicraftFeatures.CURVED_PALM_TREE.get().configured(NoneFeatureConfiguration.INSTANCE);
        } else {
            return TropicraftFeatures.LARGE_PALM_TREE.get().configured(NoneFeatureConfiguration.INSTANCE);
        }
    });

    public static final AbstractTreeGrower RED_MANGROVE = create("red_mangrove");
    public static final AbstractTreeGrower TALL_MANGROVE = create("tall_mangrove");
    public static final AbstractTreeGrower TEA_MANGROVE = create("tea_mangrove");
    public static final AbstractTreeGrower BLACK_MANGROVE = create("black_mangrove");

    private static AbstractTreeGrower createFruit(Supplier<? extends Block> fruitLeaves) {
        return create((server, random, beehive) -> {
            TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                    new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                    new CitrusTrunkPlacer(6, 3, 0),
                    new WeightedStateProvider(weightedBlockStateBuilder().add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1).add(fruitLeaves.get().defaultBlockState(), 1)),
                    new SimpleStateProvider(Blocks.AIR.defaultBlockState()),
                    new CitrusFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    new TwoLayersFeatureSize(1, 0, 2)
            ).build();

           return Feature.TREE.configured(config);
        });
    }

    private static AbstractTreeGrower create(String id) {
        ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(
                Registry.CONFIGURED_FEATURE_REGISTRY,
                new ResourceLocation(net.tropicraft.Constants.MODID, id)
        );

        return create((server, random, beehive) -> {
            RegistryAccess registries = server.registryAccess();
            WritableRegistry<ConfiguredFeature<?, ?>> features = (WritableRegistry<ConfiguredFeature<?, ?>>) registries.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            return features.get(key);
        });
    }

    private static AbstractTreeGrower create(FeatureProvider featureProvider) {
        return new AbstractTreeGrower() {
            @Nullable
            @Override
            protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean beehive) {
                return null;
            }

            @Override
            public boolean growTree(ServerLevel world, ChunkGenerator generator, BlockPos pos, BlockState sapling, Random random) {
                ConfiguredFeature<?, ?> feature = featureProvider.getFeature(world.getServer(), random, this.hasFlowers(world, pos));
                if (feature == null) {
                    return false;
                }

                world.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.NO_RERENDER);
                if (feature.place(world, generator, random, pos)) {
                    return true;
                } else {
                    world.setBlock(pos, sapling, Constants.BlockFlags.NO_RERENDER);
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

    public static SimpleWeightedRandomList.Builder<BlockState> weightedBlockStateBuilder() {
        return SimpleWeightedRandomList.builder();
    }

    interface FeatureProvider {
        @Nullable
        ConfiguredFeature<?, ?> getFeature(MinecraftServer server, Random random, boolean beehive);
    }
}
