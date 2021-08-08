package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;

import java.util.Random;

public class MangroveTreeFeature extends Feature<BaseTreeFeatureConfig> {
    private final TreeFeature backing;

    public MangroveTreeFeature(TreeFeature backing, Codec<BaseTreeFeatureConfig> codec) {
        super(codec);
        this.backing = backing;
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BaseTreeFeatureConfig config) {
        BlockPos genPos;
        boolean secondWaterCheck = false;
        if (!config.forcePlacement) {
            int floorY = reader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
            int surfaceY = reader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            if (surfaceY - floorY > 3 || config.maxWaterDepth == 0) {
                return false;
            } else if (surfaceY - floorY > config.maxWaterDepth) {
                secondWaterCheck = true;
            }

            int y;
            if (config.heightmap == Heightmap.Type.OCEAN_FLOOR) {
                y = floorY;
            } else if (config.heightmap == Heightmap.Type.WORLD_SURFACE) {
                y = surfaceY;
            } else {
                y = reader.getHeight(config.heightmap, pos).getY();
            }

            genPos = new BlockPos(pos.getX(), y, pos.getZ());
        } else {
            genPos = pos;
        }

        if (secondWaterCheck) {
            int surfaceY = reader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            genPos = new BlockPos(pos.getX(), surfaceY - config.maxWaterDepth, pos.getZ());
        }
        BlockState downState = reader.getBlockState(genPos.down());
        boolean reset = false;

        if (downState.isIn(BlockTags.SAND) || downState.getFluidState().isTagged(FluidTags.WATER)) {
            reset = true;
            reader.setBlockState(genPos.down(), Blocks.DIRT.getDefaultState(), 3);
        }

        boolean ret = this.backing.generate(reader, generator, rand, pos, config);

        if (reset) {
            reader.setBlockState(genPos.down(), downState, 3);
        }

        return ret;
    }
}
