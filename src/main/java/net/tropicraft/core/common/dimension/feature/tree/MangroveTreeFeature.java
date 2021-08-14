package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class MangroveTreeFeature extends Feature<BaseTreeFeatureConfig> {
    private final TreeFeature backing;

    public MangroveTreeFeature(TreeFeature backing, Codec<BaseTreeFeatureConfig> codec) {
        super(codec);
        this.backing = backing;
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, BaseTreeFeatureConfig config) {
        BlockPos placePos = this.findPlacePos(world, pos, config);
        if (placePos == null) return false;

        BlockPos soilPos = placePos.down();
        BlockState soilState = world.getBlockState(soilPos);

        // Force placement: put dirt under the current position so that the tree always places
        boolean replaceSoil = soilState.matchesBlock(TropicraftBlocks.MUD.get()) || soilState.matchesBlock(TropicraftBlocks.MUD_WITH_PIANGUAS.get()) ||
                soilState.getFluidState().isTagged(FluidTags.WATER) ||
                (world.getBlockState(soilPos.down()).getFluidState().isTagged(FluidTags.WATER));

        try {
            if (replaceSoil) world.setBlockState(soilPos, Blocks.DIRT.getDefaultState(), Constants.BlockFlags.DEFAULT);
            return this.backing.generate(world, generator, random, pos, config);
        } finally {
            if (replaceSoil) world.setBlockState(soilPos, soilState, Constants.BlockFlags.DEFAULT);
        }
    }

    @Nullable
    private BlockPos findPlacePos(ISeedReader world, BlockPos pos, BaseTreeFeatureConfig config) {
        if (config.forcePlacement) {
            return pos;
        }

        int floorY = world.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
        int surfaceY = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
        int waterDepth = surfaceY - floorY; // Water depth is the distance from the surface to the floor

        // If we're in water and we're not allowed to be, cancel placement
        if (config.maxWaterDepth == 0 && waterDepth > 0) {
            return null;
        }

        if (waterDepth > 3) { // If we're more than 3 blocks deep, cancel placement
            return null;
        } else if (waterDepth > config.maxWaterDepth) { // If we're more than our max water depth (but not more than 3 blocks deep!) suspend the tree underwater
            // Calculated by getting the surface of the water and going down the max water water depth, so there's water below the tree and roots can generate connecting it.
            return new BlockPos(pos.getX(), surfaceY - config.maxWaterDepth, pos.getZ());
        }

        int y;
        if (config.heightmap == Heightmap.Type.OCEAN_FLOOR) {
            y = floorY;
        } else if (config.heightmap == Heightmap.Type.WORLD_SURFACE) {
            y = surfaceY;
        } else {
            y = world.getHeight(config.heightmap, pos).getY();
        }

        return new BlockPos(pos.getX(), y, pos.getZ());
    }
}
