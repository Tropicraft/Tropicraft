package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

public class SingleUndergrowthFeature extends Feature<NoFeatureConfig> {
    private static final int LARGE_BUSH_CHANCE = 4;

    public SingleUndergrowthFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }
    
    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
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

        if (!TropicraftFeatureUtil.isSoil(world, pos.down())) {
            return false;
        }

        world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);

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
                            setBlockState(world, newPos, TropicraftBlocks.KAPOK_LEAVES.get().getDefaultState());
                        }
                    }
                }
            }
            ++count;
        }

        setBlockState(world, pos, TropicraftBlocks.MAHOGANY_LOG.get().getDefaultState());

        return count > 0;
    }
    
    protected boolean isValidPosition(IWorldGenerationReader world, BlockPos pos) {
        return TreeFeature.isAirOrLeavesAt(world, pos) && !world.hasBlockState(pos, Blocks.CAVE_AIR.getDefaultState()::equals);
    }
}
