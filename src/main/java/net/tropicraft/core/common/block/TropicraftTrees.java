package net.tropicraft.core.common.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaTreeDecorator;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Supplier;

public class TropicraftTrees {
    public static final Tree GRAPEFRUIT = createFruit(TropicraftBlocks.GRAPEFRUIT_LEAVES);
    public static final Tree LEMON = createFruit(TropicraftBlocks.LEMON_LEAVES);
    public static final Tree LIME = createFruit(TropicraftBlocks.LIME_LEAVES);
    public static final Tree ORANGE = createFruit(TropicraftBlocks.ORANGE_LEAVES);
    public static final Tree PAPAYA = create((server, random, beehive) -> {
        BaseTreeFeatureConfig config = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(TropicraftBlocks.PAPAYA_LOG.get().getDefaultState()),
                new SimpleBlockStateProvider(TropicraftBlocks.PAPAYA_LEAVES.get().getDefaultState()),
                new PapayaFoliagePlacer(FeatureSpread.create(0), FeatureSpread.create(0)),
                new StraightTrunkPlacer(5, 2, 3),
                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
        ).setDecorators(ImmutableList.of(Features.Placements.BEES_005_PLACEMENT, new PapayaTreeDecorator())).setMaxWaterDepth(1).build();

        return Feature.TREE.withConfiguration(config);
    });

    public static final Tree RAINFOREST = create((server, random, beehive) -> {
        final int treeType = random.nextInt(4);
        if (treeType == 0) {
            return TropicraftFeatures.TALL_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE);
        } else if (treeType == 1) {
            return TropicraftFeatures.SMALL_TUALUNG.get().withConfiguration(NoFeatureConfig.INSTANCE);
        } else if (treeType == 2) {
            return TropicraftFeatures.UP_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE);
        } else {
            return TropicraftFeatures.LARGE_TUALUNG.get().withConfiguration(NoFeatureConfig.INSTANCE);
        }
    });

    public static final Tree PALM = create((server, random, beehive) -> {
        final int palmType = random.nextInt(3);
        if (palmType == 0) {
            return TropicraftFeatures.NORMAL_PALM_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE);
        } else if (palmType == 1) {
            return TropicraftFeatures.CURVED_PALM_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE);
        } else {
            return TropicraftFeatures.LARGE_PALM_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE);
        }
    });

    public static final Tree RED_MANGROVE = create("red_mangrove");
    public static final Tree TALL_MANGROVE = create("tall_mangrove");
    public static final Tree TEA_MANGROVE = create("tea_mangrove");
    public static final Tree BLACK_MANGROVE = create("black_mangrove");

    private static Tree createFruit(Supplier<? extends Block> fruitLeaves) {
        return create((server, random, beehive) -> {
            BaseTreeFeatureConfig config = new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
                    new WeightedBlockStateProvider().addWeightedBlockstate(TropicraftBlocks.FRUIT_LEAVES.get().getDefaultState(), 1).addWeightedBlockstate(fruitLeaves.get().getDefaultState(), 1),
                    new CitrusFoliagePlacer(FeatureSpread.create(0), FeatureSpread.create(0)),
                    new CitrusTrunkPlacer(6, 3, 0),
                    new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
            ).build();

           return Feature.TREE.withConfiguration(config);
        });
    }

    private static Tree create(String id) {
        RegistryKey<ConfiguredFeature<?, ?>> key = RegistryKey.getOrCreateKey(
                Registry.CONFIGURED_FEATURE_KEY,
                new ResourceLocation(net.tropicraft.Constants.MODID, id)
        );

        return create((server, random, beehive) -> {
            DynamicRegistries registries = server.getDynamicRegistries();
            MutableRegistry<ConfiguredFeature<?, ?>> features = registries.getRegistry(Registry.CONFIGURED_FEATURE_KEY);
            return features.getValueForKey(key);
        });
    }

    private static Tree create(FeatureProvider featureProvider) {
        return new Tree() {
            @Nullable
            @Override
            protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean beehive) {
                return null;
            }

            @Override
            public boolean attemptGrowTree(ServerWorld world, ChunkGenerator generator, BlockPos pos, BlockState sapling, Random random) {
                ConfiguredFeature<?, ?> feature = featureProvider.getFeature(world.getServer(), random, this.hasFlowers(world, pos));
                if (feature == null) {
                    return false;
                }

                world.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.NO_RERENDER);
                if (feature.generate(world, generator, random, pos)) {
                    return true;
                } else {
                    world.setBlockState(pos, sapling, Constants.BlockFlags.NO_RERENDER);
                    return false;
                }
            }

            private boolean hasFlowers(IWorld world, BlockPos origin) {
                BlockPos min = origin.add(-2, -1, -2);
                BlockPos max = origin.add(2, 1, 2);
                for (BlockPos pos : BlockPos.Mutable.getAllInBoxMutable(min, max)) {
                    if (world.getBlockState(pos).isIn(BlockTags.FLOWERS)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    interface FeatureProvider {
        @Nullable
        ConfiguredFeature<?, ?> getFeature(MinecraftServer server, Random random, boolean beehive);
    }
}
