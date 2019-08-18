package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class TallRainforestTreeFeature extends RainforestTreeFeature {

    private static final int VINE_CHANCE = 5;
    private static final int SMALL_LEAF_CHANCE = 3;
    private static final int SECOND_CANOPY_CHANCE = 3;

    public TallRainforestTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, boolean doesNotifyOnPlace) {
        super(func, doesNotifyOnPlace);
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox bb) {
        pos = pos.toImmutable();
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        final int height = rand.nextInt(15) + 15;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!isSoil(world, pos.down(), getSapling())) {
            return false;
        }

        if (!isSoil(world, pos.east().down(), getSapling())) {
            return false;
        }

        if (!isSoil(world, pos.west().down(), getSapling())) {
            return false;
        }

        if (!isSoil(world, pos.north().down(), getSapling())) {
            return false;
        }

        if (!isSoil(world, pos.south().down(), getSapling())) {
            return false;
        }

        setState(changedBlocks, world, new BlockPos(i, j, k), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i - 1, j, k), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i + 1, j, k), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i, j, k - 1), Blocks.DIRT.getDefaultState(), bb);
        setState(changedBlocks, world, new BlockPos(i, j, k + 1), Blocks.DIRT.getDefaultState(), bb);

        for (int y = j; y < j + height; y++) {
            placeLog(changedBlocks, world, bb, i, y, k);
            placeLog(changedBlocks, world, bb, i - 1, y, k);
            placeLog(changedBlocks, world, bb, i + 1, y, k);
            placeLog(changedBlocks, world, bb, i, y, k - 1);
            placeLog(changedBlocks, world, bb, i, y, k + 1);

            if (y - j > height / 2 && rand.nextInt(SMALL_LEAF_CHANCE) == 0) {
                int nx = rand.nextInt(3) - 1 + i;
                int nz = rand.nextInt(3) - 1 + k;

                genCircle(world, new BlockPos(nx, y + 1, nz), 1, 0, LEAF_STATE, false);
                genCircle(world, nx, y, nz, 2, 1, LEAF_STATE, false);

                for (int x = nx - 3; x <= nx + 3; x++) {
                    for (int z = nz - 3; z <= nz + 3; z++) {
                        for (int y1 = y - 6; y1 <= y; y1++) {
                            if (rand.nextInt(VINE_CHANCE) == 0) {
                                // TODO genVines(world, rand, x, y1, z);
                            }
                        }
                    }
                }
            }
            if (y - j > height - (height / 4) && y - j < height - 3 && rand.nextInt(SECOND_CANOPY_CHANCE) == 0) {
                int nx = i + rand.nextInt(9) - 4;
                int nz = k + rand.nextInt(9) - 4;

                int leafSize = rand.nextInt(3) + 5;

                genCircle(world, nx, y + 3, nz, leafSize - 2, 0, LEAF_STATE, false);
                genCircle(world, nx, y + 2, nz, leafSize - 1, leafSize - 3, LEAF_STATE, false);
                genCircle(world, nx, y + 1, nz, leafSize, leafSize - 1, LEAF_STATE, false);

                placeBlockLine(changedBlocks, world, bb, new int[] { i, y - 2, k }, new int[] { nx, y + 2, nz }, LOG_STATE);
                for (int x = nx - leafSize; x <= nx + leafSize; x++) {
                    for (int z = nz - leafSize; z <= nz + leafSize; z++) {
                        for (int y1 = y - 6; y1 <= y; y1++) {
                            if (rand.nextInt(VINE_CHANCE) == 0) {
                                // TODO genVines(world, rand, x, y1, z);
                            }
                        }
                    }
                }
            }
        }

        int leafSize = rand.nextInt(5) + 9;

        genCircle(world, i, j + height, k, leafSize - 2, 0, LEAF_STATE, false);
        genCircle(world, i, j + height - 1, k, leafSize - 1, leafSize - 4, LEAF_STATE, false);
        genCircle(world, i, j + height - 2, k, leafSize, leafSize - 1, LEAF_STATE, false);

        for (int x = i - leafSize; x <= i + leafSize; x++) {
            for (int z = k - leafSize; z <= k + leafSize; z++) {
                for (int y1 = j + height - 3; y1 <= j + height; y1++) {
                    if (rand.nextInt(VINE_CHANCE) == 0) {
                        // TODO genVines(world, rand, x, y1, z);
                    }
                }
            }
        }

        return true;
    }
}
