package net.tropicraft.core.common.dimension.feature;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class NormalPalmTreeFeature extends PalmTreeFeature {

    public NormalPalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, boolean doesNotifyOnPlace) {
        super(func, doesNotifyOnPlace);
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox bb) {
        pos = pos.toImmutable();

        int height = rand.nextInt(4) + 6;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!isSoil(world, pos.down(), getSapling())) {
            return false;
        }

        int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        placeLeaf(changedBlocks, world, bb, i, j + height + 2, k);
        placeLeaf(changedBlocks, world, bb, i, j + height + 1, k + 1);
        placeLeaf(changedBlocks, world, bb, i, j + height + 1, k + 2);
        placeLeaf(changedBlocks, world, bb, i, j + height + 1, k + 3);
        placeLeaf(changedBlocks, world, bb, i, j + height, k + 4);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 1, k);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 1, k);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height + 1, k);
        placeLeaf(changedBlocks, world, bb, i + 4, j + height, k);
        placeLeaf(changedBlocks, world, bb, i, j + height + 1, k - 1);
        placeLeaf(changedBlocks, world, bb, i, j + height + 1, k - 2);
        placeLeaf(changedBlocks, world, bb, i, j + height + 1, k - 3);
        placeLeaf(changedBlocks, world, bb, i, j + height, k - 4);
        placeLeaf(changedBlocks, world, bb, i - 1, j + height + 1, k);
        placeLeaf(changedBlocks, world, bb, i - 1, j + height + 1, k - 1);
        placeLeaf(changedBlocks, world, bb, i - 1, j + height + 1, k + 1);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 1, k - 1);
        placeLeaf(changedBlocks, world, bb, i + 1, j + height + 1, k + 1);
        placeLeaf(changedBlocks, world, bb, i - 2, j + height + 1, k);
        placeLeaf(changedBlocks, world, bb, i - 3, j + height + 1, k);
        placeLeaf(changedBlocks, world, bb, i - 4, j + height, k);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 1, k + 2);
        placeLeaf(changedBlocks, world, bb, i + 2, j + height + 1, k - 2);
        placeLeaf(changedBlocks, world, bb, i - 2, j + height + 1, k + 2);
        placeLeaf(changedBlocks, world, bb, i - 2, j + height + 1, k - 2);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height, k + 3);
        placeLeaf(changedBlocks, world, bb, i + 3, j + height, k - 3);
        placeLeaf(changedBlocks, world, bb, i - 3, j + height, k + 3);
        placeLeaf(changedBlocks, world, bb, i - 3, j + height, k - 3);

        for (int j1 = 0; j1 < height + 2; j1++) {
            BlockPos logPos = pos.up(j1);
            if (isAirOrLeaves(world, logPos) || isTallPlants(world, logPos)) {
                placeLog(changedBlocks, world, bb, logPos.getX(), logPos.getY(), logPos.getZ());
            }
        }
        
        spawnCoconuts(world, new BlockPos(i, j + height, k), rand, 2, getLeaf());

        return true;
    }
}
