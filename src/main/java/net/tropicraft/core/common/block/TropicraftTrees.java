package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

import javax.annotation.Nullable;
import java.util.Random;

public class TropicraftTrees {
    private static abstract class TropicraftTree extends Tree {

        protected abstract Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive);

        @Override
        public boolean place(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, BlockPos blockPosIn, BlockState blockStateIn, Random randomIn) {
            Feature feature = getTropicraftTreeFeature(randomIn, hasAdjacentFlower(worldIn, blockPosIn));
            if (feature == null) {
                return false;
            } else {
                worldIn.setBlockState(blockPosIn, Blocks.AIR.getDefaultState(), 4);
                if (feature.place(worldIn, chunkGeneratorIn, randomIn, blockPosIn, NoFeatureConfig.NO_FEATURE_CONFIG)) {
                    return true;
                } else {
                    worldIn.setBlockState(blockPosIn, blockStateIn, 4);
                    return false;
                }
            }
        }

        private boolean hasAdjacentFlower(IWorld world, BlockPos pos) {
            for(BlockPos blockpos : BlockPos.Mutable.getAllInBoxMutable(pos.down().north(2).west(2), pos.up().south(2).east(2))) {
                if (world.getBlockState(blockpos).isIn(BlockTags.FLOWERS)) {
                    return true;
                }
            }

            return false;
        }

        @Nullable
        @Override
        protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
            return null;
        }
    }

    public static final TropicraftTree GRAPEFRUIT = new TropicraftTree() {
        @Override
        protected Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive) {
            return TropicraftFeatures.GRAPEFRUIT_TREE.get();
        }
    };

    public static final Tree LEMON = new TropicraftTree() {
        @Override
        protected Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive) {
            return TropicraftFeatures.LEMON_TREE.get();
        }
    };

    public static final Tree LIME = new TropicraftTree() {
        @Override
        protected Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive) {
            return TropicraftFeatures.LIME_TREE.get();
        }
    };

    public static final Tree ORANGE = new TropicraftTree() {
        @Override
        protected Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive) {
            return TropicraftFeatures.ORANGE_TREE.get();
        }
    };

    public static final Tree RAINFOREST = new TropicraftTree() {
        @Override
        protected Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive) {
            final int treeType = random.nextInt(4);
            if (treeType == 0) {
                return TropicraftFeatures.TALL_TREE.get();
            } else if (treeType == 1) {
                return TropicraftFeatures.SMALL_TUALUNG.get();
            } else if (treeType == 2) {
                return TropicraftFeatures.UP_TREE.get();
            } else {
                return TropicraftFeatures.LARGE_TUALUNG.get();
            }
        }
    };

    public static final Tree PALM = new TropicraftTree() {
        @Override
        protected Feature<? extends NoFeatureConfig> getTropicraftTreeFeature(Random random, boolean generateBeehive) {
            final int palmType = random.nextInt(3);
            if (palmType == 0) {
                return TropicraftFeatures.NORMAL_PALM_TREE.get();
            } else if (palmType == 1) {
                return TropicraftFeatures.CURVED_PALM_TREE.get();
            } else {
                return TropicraftFeatures.LARGE_PALM_TREE.get();
            }
        }
    };
}
