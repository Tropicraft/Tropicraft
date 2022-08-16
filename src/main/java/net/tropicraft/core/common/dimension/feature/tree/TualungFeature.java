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

// TODO use TualungConfig, but requires extending Feature instead, which is a pain
public class TualungFeature extends RainforestTreeFeature {

    private int baseHeight;
    private int maxHeight;

    public TualungFeature(Codec<NoneFeatureConfiguration> codec, int maxHeight, int baseHeight) {
        super(codec);
        this.baseHeight = baseHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random rand = context.random();
        BlockPos pos = context.origin();

        pos = pos.immutable();
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        int height = rand.nextInt(maxHeight - baseHeight) + baseHeight + j;
        int branches = rand.nextInt(3) + 3;

        if (goesBeyondWorldSize(world, pos.getY(), height - j)) {
            return false;
        }

        if (height + 6 > world.getHeight()) {
            return false;
        }

        if (!isBBAvailable(world, pos, height - j)) {
            return false;
        }

        if (!getSapling().canSurvive(getSapling().defaultBlockState(), world, pos)) {
            return false;
        }

        Set<BlockPos> logPositions = new HashSet<>();
        Set<BlockPos> leafPositions = new HashSet<>();

        setState(world, new BlockPos(i, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(world, new BlockPos(i - 1, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(world, new BlockPos(i + 1, j - 1, k), Blocks.DIRT.defaultBlockState());
        setState(world, new BlockPos(i, j - 1, k - 1), Blocks.DIRT.defaultBlockState());
        setState(world, new BlockPos(i, j - 1, k + 1), Blocks.DIRT.defaultBlockState());

        for (int y = j; y < height; y++) {
            placeLog(world, i, y, k, logPositions::add);
            placeLog(world, i - 1, y, k, logPositions::add);
            placeLog(world, i + 1, y, k, logPositions::add);
            placeLog(world, i, y, k - 1, logPositions::add);
            placeLog(world, i, y, k + 1, logPositions::add);
        }

        for (int x = 0; x < branches; x++) {
            int branchHeight = rand.nextInt(4) + 2 + height;
            int bx = rand.nextInt(15) - 8 + i;
            int bz = rand.nextInt(15) - 8 + k;

            placeBlockLine(world, new int[] { i + sign((bx - i) / 2), height, k + sign((bz - k) / 2) }, new int[] { bx, branchHeight, bz }, getLog());

            genCircle(world, bx, branchHeight, bz, 2, 1, getLeaf(), false, leafPositions::add);
            genCircle(world, bx, branchHeight + 1, bz, 3, 2, getLeaf(), false, leafPositions::add);
        }

        return BoundingBox.encapsulatingPositions(Iterables.concat(logPositions, leafPositions)).map((p_160521_) -> {
            DiscreteVoxelShape discretevoxelshape = CustomTreeUpdateUtil.updateLeaves(world, p_160521_, logPositions, leafPositions);
            StructureTemplate.updateShapeAtEdge(world, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
            return true;
        }).orElse(false);
    }

    private int sign(int i) {
        return i == 0 ? 0 : i <= 0 ? -1 : 1;
    }
}
