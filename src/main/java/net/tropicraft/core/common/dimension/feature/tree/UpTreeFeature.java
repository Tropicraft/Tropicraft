package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class UpTreeFeature extends RainforestTreeFeature {

    public UpTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random rand = context.random();
        BlockPos pos = context.origin();
        NoneFeatureConfiguration config = context.config();

        Set<BlockPos> logPositions = new HashSet<>();
        Set<BlockPos> leafPositions = new HashSet<>();

        pos = pos.immutable();
        final int height = rand.nextInt(4) + 6;
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!getSapling().canSurvive(getSapling().defaultBlockState(), world, pos)) {
            return false;
        }

        world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);

        for (int y = j; y < j + height; y++) {
            placeLog(world, i, y, k, logPositions::add);
            if (rand.nextInt(5) == 0) {
                int x = rand.nextInt(3) - 1 + i;
                int z = k;
                if (x - i == 0) {
                    z += rand.nextBoolean() ? 1 : -1;
                }
                placeLeaf(world, x, y, z);
            }

            if (y == j + height - 1) {
                placeLog(world, i + 1, y, k, logPositions::add);
                placeLog(world, i - 1, y, k, logPositions::add);
                placeLog(world, i, y, k + 1, logPositions::add);
                placeLog(world, i, y, k - 1, logPositions::add);
            }
        }

        final int radius = rand.nextInt(2) + 3;

        genCircle(world, i, j + height, k, radius, 0, getLeaf(), false, leafPositions::add);
        genCircle(world, i, j + height + 1, k, radius + 2, radius, getLeaf(), false, leafPositions::add);
        genCircle(world, i, j + height + 2, k, radius + 3, radius + 2, getLeaf(), false, leafPositions::add);

        return BoundingBox.encapsulatingPositions(Iterables.concat(logPositions, leafPositions)).map((p_160521_) -> {
            DiscreteVoxelShape discretevoxelshape = CustomTreeUpdateUtil.updateLeaves(world, p_160521_, logPositions, leafPositions);
            StructureTemplate.updateShapeAtEdge(world, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
            return true;
        }).orElse(false);
    }
}
