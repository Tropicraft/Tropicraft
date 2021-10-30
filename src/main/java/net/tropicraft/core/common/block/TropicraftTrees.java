package net.tropicraft.core.common.block;

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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class TropicraftTrees {
    public static final Tree GRAPEFRUIT = createFruit(TropicraftBlocks.GRAPEFRUIT_LEAVES);
    public static final Tree LEMON = createFruit(TropicraftBlocks.LEMON_LEAVES);
    public static final Tree LIME = createFruit(TropicraftBlocks.LIME_SAPLING);
    public static final Tree ORANGE = createFruit(TropicraftBlocks.ORANGE_SAPLING);

    public static final Tree RAINFOREST = create((server, random, beehive) -> {
        final int treeType = random.nextInt(4);
        if (treeType == 0) {
            return TropicraftFeatures.TALL_TREE.get().configured(NoFeatureConfig.INSTANCE);
        } else if (treeType == 1) {
            return TropicraftFeatures.SMALL_TUALUNG.get().configured(NoFeatureConfig.INSTANCE);
        } else if (treeType == 2) {
            return TropicraftFeatures.UP_TREE.get().configured(NoFeatureConfig.INSTANCE);
        } else {
            return TropicraftFeatures.LARGE_TUALUNG.get().configured(NoFeatureConfig.INSTANCE);
        }
    });

    public static final Tree PALM = create((server, random, beehive) -> {
        final int palmType = random.nextInt(3);
        if (palmType == 0) {
            return TropicraftFeatures.NORMAL_PALM_TREE.get().configured(NoFeatureConfig.INSTANCE);
        } else if (palmType == 1) {
            return TropicraftFeatures.CURVED_PALM_TREE.get().configured(NoFeatureConfig.INSTANCE);
        } else {
            return TropicraftFeatures.LARGE_PALM_TREE.get().configured(NoFeatureConfig.INSTANCE);
        }
    });

    public static final Tree RED_MANGROVE = create("red_mangrove");
    public static final Tree TALL_MANGROVE = create("tall_mangrove");
    public static final Tree TEA_MANGROVE = create("tea_mangrove");
    public static final Tree BLACK_MANGROVE = create("black_mangrove");

    private static Tree createFruit(Supplier<? extends Block> fruitLeaves) {
        return create((server, random, beehive) -> {
            BaseTreeFeatureConfig config = new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                    new WeightedBlockStateProvider().add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1).add(fruitLeaves.get().defaultBlockState(), 1),
                    new CitrusFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(0)),
                    new CitrusTrunkPlacer(6, 3, 0),
                    new TwoLayerFeature(1, 0, 2)
            ).build();

           return Feature.TREE.configured(config);
        });
    }

    private static Tree create(String id) {
        RegistryKey<ConfiguredFeature<?, ?>> key = RegistryKey.create(
                Registry.CONFIGURED_FEATURE_REGISTRY,
                new ResourceLocation(net.tropicraft.Constants.MODID, id)
        );

        return create((server, random, beehive) -> {
            DynamicRegistries registries = server.registryAccess();
            MutableRegistry<ConfiguredFeature<?, ?>> features = registries.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            return features.get(key);
        });
    }

    private static Tree create(FeatureProvider featureProvider) {
        return new Tree() {
            @Nullable
            @Override
            protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean beehive) {
                return null;
            }

            @Override
            public boolean growTree(ServerWorld world, ChunkGenerator generator, BlockPos pos, BlockState sapling, Random random) {
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

            private boolean hasFlowers(IWorld world, BlockPos origin) {
                BlockPos min = origin.offset(-2, -1, -2);
                BlockPos max = origin.offset(2, 1, 2);
                for (BlockPos pos : BlockPos.Mutable.betweenClosed(min, max)) {
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
        ConfiguredFeature<?, ?> getFeature(MinecraftServer server, Random random, boolean beehive);
    }
}
