package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

public class SingleUndergrowthFeature extends Feature<NoneFeatureConfiguration> {
    private static final int LARGE_BUSH_CHANCE = 4;

    public SingleUndergrowthFeature(Codec<NoneFeatureConfiguration> codec) {
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

        int count = 0;

        if (isValidPosition(world, pos) && pos.getY() < 255) {
            for (int y = pos.getY(); y < pos.getY() + size; y++) {
                int bushWidth = size - (y - pos.getY());
                for (int x = pos.getX() - bushWidth; x < pos.getX() + bushWidth; x++) {
                    int xVariance = x - pos.getX();
                    for (int z = pos.getZ() - bushWidth; z < pos.getZ() + bushWidth; z++) {
                        int zVariance = z - pos.getZ();
                        final BlockPos newPos = new BlockPos(x, y, z);
                        if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && isValidPosition(world, newPos)) {
                            setBlock(world, newPos, TropicraftBlocks.KAPOK_LEAVES.get().defaultBlockState());
                        }
                    }
                }
            }
            ++count;
        }

        setBlock(world, pos, TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState());

        return count > 0;
    }
    
    protected boolean isValidPosition(LevelSimulatedRW world, BlockPos pos) {
        return TreeFeature.isAirOrLeaves(world, pos) && !world.isStateAtPosition(pos, Blocks.CAVE_AIR.defaultBlockState()::equals);
    }
}
