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

public class UndergrowthFeature extends Feature<NoFeatureConfig> {
    private static final int LARGE_BUSH_CHANCE = 5;

    public UndergrowthFeature(Codec<NoFeatureConfig> codec) {
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
        setBlockState(world, pos, TropicraftBlocks.MAHOGANY_LOG.get().getDefaultState());

        int count = 0;

        for (int round = 0; round < 64; ++round) {
            BlockPos posTemp = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (isValidPosition(world, posTemp) && posTemp.getY() < 255) {
                for (int y = posTemp.getY(); y < posTemp.getY() + size; y++) {
                    int bushWidth = size - (y - posTemp.getY());
                    for (int x = posTemp.getX() - bushWidth; x < posTemp.getX() + bushWidth; x++) {
                        int xVariance = x - posTemp.getX();
                        for (int z = posTemp.getZ() - bushWidth; z < posTemp.getZ() + bushWidth; z++) {
                            int zVariance = z - posTemp.getZ();
                            final BlockPos newPos = new BlockPos(x, y, z);
                            if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && isValidPosition(world, newPos)) {
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
    
    protected boolean isValidPosition(IWorldGenerationReader world, BlockPos pos) {
        return TreeFeature.isAirOrLeavesAt(world, pos) && !world.hasBlockState(pos, Blocks.CAVE_AIR.getDefaultState()::equals);
    }
}
