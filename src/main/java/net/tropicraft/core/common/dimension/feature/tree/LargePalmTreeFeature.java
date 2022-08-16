package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class LargePalmTreeFeature extends PalmTreeFeature {
    public LargePalmTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random random = context.random();
        BlockPos pos = context.origin();
        NoneFeatureConfiguration config = context.config();
        pos = pos.immutable();

        int height = random.nextInt(7) + 7;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!getSapling().canSurvive(getSapling().defaultBlockState(), world, pos)) {
            return false;
        }

        Set<BlockPos> logPositions = new HashSet<>();
        Set<BlockPos> leafPositions = new HashSet<>();

        if (world.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK) {
            world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);
        }

        // Place trunk
        for (int y = 0; y <= height; y++) {
            placeLog(world, pos.getX(), pos.getY() + y, pos.getZ(), logPositions::add);
        }
        
        final int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        // Wheeee, auto-generated code!
        placeLeaf(world, i + 0, j + height + 1, k + -7, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 1, k + -6, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 1, k + -6, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 1, k + -5, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 1, k + -5, leafPositions::add);
        placeLeaf(world, i + -6, j + height + 1, k + -1, leafPositions::add);
        placeLog(world, i + 0, j + height + 1, k + -1, leafPositions::add);
        placeLeaf(world, i + 6, j + height + 1, k + -1, leafPositions::add);
        placeLeaf(world, i + -7, j + height + 1, k + 0, leafPositions::add);
        placeLog(world, i + -1, j + height + 1, k + 0, leafPositions::add);
        placeLog(world, i + 0, j + height + 1, k + 0, leafPositions::add);
        placeLog(world, i + 1, j + height + 1, k + 0, leafPositions::add);
        placeLeaf(world, i + 7, j + height + 1, k + 0, leafPositions::add);
        placeLeaf(world, i + -6, j + height + 1, k + 1, leafPositions::add);
        placeLog(world, i + 0, j + height + 1, k + 1, leafPositions::add);
        placeLeaf(world, i + 6, j + height + 1, k + 1, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 1, k + 5, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 1, k + 5, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 1, k + 6, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 1, k + 6, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 1, k + 7, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 2, k + -6, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 2, k + -5, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + -5, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + -5, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 2, k + -5, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 2, k + -4, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 2, k + -4, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + -4, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + -4, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 2, k + -4, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 2, k + -4, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 2, k + -3, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 2, k + -3, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + -3, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + -3, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 2, k + -3, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 2, k + -3, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 2, k + -2, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + -2, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + -2, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 2, k + -2, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 2, k + -1, leafPositions::add);
        placeLeaf(world, i + -6, j + height + 2, k + 0, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + 0, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 2, k + 0, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + 0, leafPositions::add);
        placeLeaf(world, i + 6, j + height + 2, k + 0, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 2, k + 1, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 2, k + 2, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + 2, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + 2, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 2, k + 2, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 2, k + 3, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 2, k + 3, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + 3, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + 3, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 2, k + 3, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 2, k + 3, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 2, k + 4, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 2, k + 4, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + 4, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + 4, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 2, k + 4, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 2, k + 4, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 2, k + 5, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 2, k + 5, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 2, k + 5, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 2, k + 5, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 2, k + 6, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + -5, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 3, k + -4, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + -4, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 3, k + -4, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 3, k + -3, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + -3, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 3, k + -3, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 3, k + -2, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + -2, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 3, k + -2, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 3, k + -1, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + -1, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 3, k + -1, leafPositions::add);
        placeLeaf(world, i + -5, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + 5, j + height + 3, k + 0, leafPositions::add);
        placeLeaf(world, i + -1, j + height + 3, k + 1, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + 1, leafPositions::add);
        placeLeaf(world, i + 1, j + height + 3, k + 1, leafPositions::add);
        placeLeaf(world, i + -2, j + height + 3, k + 2, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + 2, leafPositions::add);
        placeLeaf(world, i + 2, j + height + 3, k + 2, leafPositions::add);
        placeLeaf(world, i + -3, j + height + 3, k + 3, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + 3, leafPositions::add);
        placeLeaf(world, i + 3, j + height + 3, k + 3, leafPositions::add);
        placeLeaf(world, i + -4, j + height + 3, k + 4, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + 4, leafPositions::add);
        placeLeaf(world, i + 4, j + height + 3, k + 4, leafPositions::add);
        placeLeaf(world, i + 0, j + height + 3, k + 5, leafPositions::add);

        for (int c = 0; c < 4; c++) {
            spawnCoconuts(world, new BlockPos(i, j + height + 1, k).relative(Direction.from2DDataValue(i)), random, 2, getLeaf());
        }

        return BoundingBox.encapsulatingPositions(Iterables.concat(logPositions, leafPositions)).map((p_160521_) -> {
            DiscreteVoxelShape discretevoxelshape = CustomTreeUpdateUtil.updateLeaves(world, p_160521_, logPositions, leafPositions);
            StructureTemplate.updateShapeAtEdge(world, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
            return true;
        }).orElse(false);
    }
}
