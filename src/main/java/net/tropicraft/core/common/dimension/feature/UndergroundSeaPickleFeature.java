package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class UndergroundSeaPickleFeature extends Feature<NoFeatureConfig> {
    public UndergroundSeaPickleFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockState surface = world.getBlockState(pos.below());
        if (!surface.is(Blocks.STONE) && !surface.is(Blocks.DIRT)) {
            return false;
        }

        if (world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)) {
            int count = random.nextInt(random.nextInt(4) + 1) + 1;
            if (surface.is(Blocks.DIRT)) {
                count = Math.min(count + random.nextInt(2), 4);
            }

            BlockState pickle = Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, count);
            world.setBlock(pos, pickle, Constants.BlockFlags.BLOCK_UPDATE);
            return true;
        }

        return false;
    }
}
