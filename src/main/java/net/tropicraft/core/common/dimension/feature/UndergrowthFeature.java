package net.tropicraft.core.common.dimension.feature;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class UndergrowthFeature extends AbstractTreeFeature<NoFeatureConfig> {
    private static final int LARGE_BUSH_CHANCE = 5;

    public UndergrowthFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func) {
        super(func, false);
    }
    
    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox bounds) {
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        int size = 2;
        if (rand.nextInt(LARGE_BUSH_CHANCE) == 0) {
            size = 3;
        }

        if (goesBeyondWorldSize(world, pos.getY(), size)) {
            return false;
        }

        if (!isSoil(world, pos.down(), sapling)) {
            return false;
        }

        setBlockState(world, pos, TropicraftBlocks.MAHOGANY_LOG.get().getDefaultState());

        int count = 0;

        for (int round = 0; round < 64; ++round) {
            BlockPos posTemp = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (isAirOrLeaves(world, posTemp) && posTemp.getY() < 255) {
                for (int y = posTemp.getY(); y < posTemp.getY() + size; y++) {
                    int bushWidth = size - (y - posTemp.getY());
                    for (int x = posTemp.getX() - bushWidth; x < posTemp.getX() + bushWidth; x++) {
                        int xVariance = x - posTemp.getX();
                        for (int z = posTemp.getZ() - bushWidth; z < posTemp.getZ() + bushWidth; z++) {
                            int zVariance = z - posTemp.getZ();
                            final BlockPos newPos = new BlockPos(x, y, z);
                            if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && isAirOrLeaves(world, newPos)) {
                                setBlockState(world, newPos, TropicraftBlocks.KAPOK_LEAVES.get().getDefaultState());
                            }
                        }
                    }
                }
                ++count;
            }
        }

        return count > 0;
    }
}
