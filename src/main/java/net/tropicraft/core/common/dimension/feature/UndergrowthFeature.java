package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.function.Function;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

public class UndergrowthFeature extends TropicraftFeature {
    private static final int LARGE_BUSH_CHANCE = 5;

    private static final BlockState LOG_STATE = TropicraftBlocks.MAHOGANY_LOG.getDefaultState();
    private static final BlockState LEAF_STATE = TropicraftBlocks.KAPOK_LEAVES.getDefaultState();

    public UndergrowthFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func) {
        super(func);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        int size = 2;
        if (rand.nextInt(LARGE_BUSH_CHANCE) == 0) {
            size = 3;
        }

        if (goesBeyondWorldSize(world, pos.getY(), size)) {
            return false;
        }

        if (!isSoil(world, pos.down())) {
            return false;
        }

        setBlockState(world, pos, LOG_STATE);

        int count = 0;

        for (int round = 0; round < 64; ++round) {
            BlockPos posTemp = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (world.isAirBlock(posTemp) && posTemp.getY() < 255 && LEAF_STATE.isValidPosition(world, posTemp)) {
                for (int y = posTemp.getY(); y < posTemp.getY() + size; y++) {
                    int bushWidth = size - (y - posTemp.getY());
                    for (int x = posTemp.getX() - bushWidth; x < posTemp.getX() + bushWidth; x++) {
                        int xVariance = x - posTemp.getX();
                        for (int z = posTemp.getZ() - bushWidth; z < posTemp.getZ() + bushWidth; z++) {
                            int zVariance = z - posTemp.getZ();
                            final BlockPos newPos = new BlockPos(x, y, z);
                            if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && !world.getBlockState(newPos).isOpaqueCube(world, newPos)) {
                                setBlockState(world, newPos, LEAF_STATE);
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
