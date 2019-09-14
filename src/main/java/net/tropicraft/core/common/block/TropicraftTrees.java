package net.tropicraft.core.common.block;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

import java.util.Random;

public class TropicraftTrees {
    public static final Tree GRAPEFRUIT = new Tree() {
        @Override
        protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
            return TropicraftFeatures.GRAPEFRUIT_TREE;
        }
    };

    public static final Tree LEMON = new Tree() {
        @Override
        protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
            return TropicraftFeatures.LEMON_TREE;
        }
    };

    public static final Tree LIME = new Tree() {
        @Override
        protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
            return TropicraftFeatures.LIME_TREE;
        }
    };

    public static final Tree ORANGE = new Tree() {
        @Override
        protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
            return TropicraftFeatures.ORANGE_TREE;
        }
    };

    public static final Tree RAINFOREST = new Tree() {
        @Override
        protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
            final int treeType = random.nextInt(4);
            if (treeType == 0) {
                return TropicraftFeatures.TALL_TREE;
            } else if (treeType == 1) {
                return TropicraftFeatures.SMALL_TUALUNG;
            } else if (treeType == 2) {
                return TropicraftFeatures.UP_TREE;
            } else {
                return TropicraftFeatures.LARGE_TUALUNG;
            }
        }
    };

    public static final Tree PALM = new Tree() {
        @Override
        protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
            final int palmType = random.nextInt(3);
            if (palmType == 0) {
                return TropicraftFeatures.NORMAL_PALM_TREE;
            } else if (palmType == 1) {
                return TropicraftFeatures.CURVED_PALM_TREE;
            } else {
                return TropicraftFeatures.LARGE_PALM_TREE;
            }
        }
    };
}
