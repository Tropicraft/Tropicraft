package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

public class UndergrowthFeature extends Feature<NoneFeatureConfiguration> {
    private static final int LARGE_BUSH_CHANCE = 5;

    public UndergrowthFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random rand = context.random();
        BlockPos pos = context.origin();

        int size = 2;
        if (rand.nextInt(LARGE_BUSH_CHANCE) == 0) {
            size = 3;
        }

        if (!isValidPosition(world, pos)) {
            return false;
        }

        if (goesBeyondWorldSize(world, pos.getY(), size)) {
            return false;
        }

        if (!TropicraftFeatureUtil.isSoil(world, pos.below())) {
            return false;
        }

        world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);
        setBlock(world, pos, TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState());

        int count = 0;

        for (int round = 0; round < 64; ++round) {
            BlockPos posTemp = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (isValidPosition(world, posTemp) && posTemp.getY() < 255) {
                for (int y = posTemp.getY(); y < posTemp.getY() + size; y++) {
                    int bushWidth = size - (y - posTemp.getY());
                    for (int x = posTemp.getX() - bushWidth; x < posTemp.getX() + bushWidth; x++) {
                        int xVariance = x - posTemp.getX();
                        for (int z = posTemp.getZ() - bushWidth; z < posTemp.getZ() + bushWidth; z++) {
                            int zVariance = z - posTemp.getZ();
                            final BlockPos newPos = new BlockPos(x, y, z);
                            if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && isValidPosition(world, newPos)) {
                                setBlock(world, newPos, TropicraftBlocks.KAPOK_LEAVES.get().defaultBlockState());
                            }
                        }
                    }
                }
                ++count;
            }
        }

        return count > 0;
    }

    protected boolean isValidPosition(LevelSimulatedRW world, BlockPos pos) {
        return TreeFeature.isAirOrLeaves(world, pos) && !world.isStateAtPosition(pos, Blocks.CAVE_AIR.defaultBlockState()::equals);
    }
}
