package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class NormalPalmTreeFeature extends PalmTreeFeature {
    public NormalPalmTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random random = context.random();
        BlockPos pos = context.origin();

        pos = pos.immutable();

        int height = random.nextInt(4) + 6;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!getSapling().canSurvive(getSapling().defaultBlockState(), world, pos)) {
            return false;
        }

        if (world.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK) {
            world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);
        }

        Set<BlockPos> logPositions = new HashSet<>();
        Set<BlockPos> leafPositions = new HashSet<>();

        int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        placeLeaf(world, i, j + height + 2, k, leafPositions::add);
        placeLeaf(world, i, j + height + 1, k + 1, leafPositions::add);
        placeLeaf(world, i, j + height + 1, k + 2, leafPositions::add);
        placeLeaf(world, i, j + height + 1, k + 3, leafPositions::add);
        placeLeaf(world, i, j + height, k + 4, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 1, k, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 1, k, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 1, k, leafPositions::add);
        placeLeaf(world, i + 4, j + height, k, leafPositions::add);
        placeLeaf(world, i, j + height + 1, k - 1, leafPositions::add);
        placeLeaf(world, i, j + height + 1, k - 2, leafPositions::add);
        placeLeaf(world, i, j + height + 1, k - 3, leafPositions::add);
        placeLeaf(world, i, j + height, k - 4, leafPositions::add);
        placeLeaf(world, i - 1, j + height + 1, k, leafPositions::add);
        placeLeaf(world, i - 1, j + height + 1, k - 1, leafPositions::add);
        placeLeaf(world, i - 1, j + height + 1, k + 1, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 1, k - 1, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 1, k + 1, leafPositions::add);
        placeLeaf(world, i - 2, j + height + 1, k, leafPositions::add);
        placeLeaf(world, i - 3, j + height + 1, k, leafPositions::add);
        placeLeaf(world, i - 4, j + height, k, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 1, k + 2, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 1, k - 2, leafPositions::add);
        placeLeaf(world, i - 2, j + height + 1, k + 2, leafPositions::add);
        placeLeaf(world, i - 2, j + height + 1, k - 2, leafPositions::add);
        placeLeaf(world, i + 3, j + height, k + 3, leafPositions::add);
        placeLeaf(world, i + 3, j + height, k - 3, leafPositions::add);
        placeLeaf(world, i - 3, j + height, k + 3, leafPositions::add);
        placeLeaf(world, i - 3, j + height, k - 3, leafPositions::add);

        for (int j1 = 0; j1 < height + 2; j1++) {
            BlockPos logPos = pos.above(j1);
            if (TreeFeature.validTreePos(world, logPos)) {
                placeLog(world, logPos, logPositions::add);
            }
        }

        spawnCoconuts(world, new BlockPos(i, j + height, k), random, 2, getLeaf());

        return BoundingBox.encapsulatingPositions(Iterables.concat(logPositions, leafPositions)).map((p_160521_) -> {
            DiscreteVoxelShape discretevoxelshape = CustomTreeUpdateUtil.updateLeaves(world, p_160521_, logPositions, leafPositions);
            StructureTemplate.updateShapeAtEdge(world, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
            return true;
        }).orElse(false);
    }
}
