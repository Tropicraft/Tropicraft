package net.tropicraft.core.common.dimension.carver;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.UnderwaterCaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicsUnderwaterCaveCarver extends UnderwaterCaveWorldCarver {

    public TropicsUnderwaterCaveCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49922_1_) {
        super(p_i49922_1_);
        this.carvableBlocks = ImmutableSet.<Block> builder().addAll(this.carvableBlocks)
                .add(TropicraftBlocks.CORAL_SAND.get())
                .add(TropicraftBlocks.FOAMY_SAND.get())
                .add(TropicraftBlocks.MINERAL_SAND.get())
                .add(TropicraftBlocks.PACKED_PURIFIED_SAND.get())
                .add(TropicraftBlocks.PURIFIED_SAND.get())
                .add(TropicraftBlocks.VOLCANIC_SAND.get()).build();
    }

    @Override
    protected boolean func_222700_a(IChunk chunkIn, int chunkX, int chunkZ, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        return false;
    }

    @Override
    protected boolean carveBlock(IChunk chunk, BitSet carvingMask, Random rand, BlockPos.MutableBlockPos mutablePos, BlockPos.MutableBlockPos mutablePosUnused, BlockPos.MutableBlockPos mutablePosUnused2,
            int seaLevel, int chunkX, int chunkZ, int x, int z, int localX, int y, int localZ, AtomicBoolean modified) {
        return super.carveBlock(chunk, carvingMask, rand, mutablePos, mutablePosUnused, mutablePosUnused2, seaLevel, chunkX, chunkZ, x, z, localX, y, localZ, modified);
    }
    
    @Override
    protected float generateCaveRadius(Random rand) {
        float f = rand.nextFloat() * 3.0F + rand.nextFloat();
        if (rand.nextInt(10) == 0) {
           f *= rand.nextFloat() * rand.nextFloat() * 5.0F + 1.0F;
        }

        return f;
    }
    
    @Override
    protected int generateCaveStartY(Random p_222726_1_) {
        return p_222726_1_.nextInt(p_222726_1_.nextInt(240) + 8);
    }
}
