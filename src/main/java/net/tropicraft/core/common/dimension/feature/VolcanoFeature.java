package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.mapgen.MapGenVolcano;

import java.util.Random;
import java.util.function.Function;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

/**
 * Feature class that searches for a volcano in the chunk and places
 * a Volcano tile entity if necessary.
 */
public class VolcanoFeature extends Feature<NoFeatureConfig> {

    public VolcanoFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func) {
        super(func);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        int chunkX = pos.getX() >> 4;
        int chunkY = pos.getZ() >> 4;

        BlockPos volcanoCoords = MapGenVolcano.getVolcanoNear(worldIn, chunkX, chunkY);

        if (volcanoCoords != null) {
            BlockPos posVolcanoTE = new BlockPos(volcanoCoords.getX(), 1, volcanoCoords.getZ());

            // Doesn't go out of chunk boundaries
            if (posVolcanoTE.getX() > pos.getX() + 15 || posVolcanoTE.getX() < pos.getX() || posVolcanoTE.getZ() > pos.getZ() + 15 || posVolcanoTE.getZ() < pos.getZ()) {
                return false;
            }

            if (goesBeyondWorldSize(worldIn, posVolcanoTE.getY(), 1)) {
                return false;
            }

            if (worldIn.getBlockState(posVolcanoTE).getBlock() != TropicraftBlocks.VOLCANO) {
                worldIn.setBlockState(posVolcanoTE, TropicraftBlocks.VOLCANO.getDefaultState(), 3);
                return true;
            }
        }

        return false;
    }

}
