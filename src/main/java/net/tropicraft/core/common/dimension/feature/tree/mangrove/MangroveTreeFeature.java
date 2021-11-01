package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.grower.JungleTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class MangroveTreeFeature extends Feature<TreeConfiguration> {
    private final TreeFeature backing;

    public MangroveTreeFeature(TreeFeature backing, Codec<TreeConfiguration> codec) {
        super(codec);
        this.backing = backing;
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel world = context.level();
        ChunkGenerator generator = context.chunkGenerator();;
        Random random = context.random();
        BlockPos pos = context.origin();
        TreeConfiguration config = context.config();;

        BlockPos placePos = this.findPlacePos(world, pos, config);
        if (placePos == null) return false;

        BlockPos soilPos = placePos.below();
        BlockState soilState = world.getBlockState(soilPos);

        // Force placement: put dirt under the current position so that the tree always places
        boolean replaceSoil = soilState.is(TropicraftTags.Blocks.MUD) ||
                soilState.getFluidState().is(FluidTags.WATER) ||
                soilState.is(Tags.Blocks.SAND) ||
                (world.getBlockState(soilPos.below()).getFluidState().is(FluidTags.WATER));

        try {
            if (replaceSoil) world.setBlock(soilPos, Blocks.DIRT.defaultBlockState(), Constants.BlockFlags.DEFAULT);
            return this.backing.place(context);
        } finally {
            if (replaceSoil) world.setBlock(soilPos, soilState, Constants.BlockFlags.DEFAULT);
        }
    }

    @Nullable
    private BlockPos findPlacePos(WorldGenLevel world, BlockPos pos, TreeConfiguration config) {
        //TODO [PORT]: It seems that this dosn't exist within 1.17

        if (config.fromSapling) {
            return pos;
        }

        int floorY = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, pos).getY();
        int surfaceY = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos).getY();
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
        if (config.heightmap == Heightmap.Types.OCEAN_FLOOR) {
            y = floorY;
        } else if (config.heightmap == Heightmap.Types.WORLD_SURFACE) {
            y = surfaceY;
        } else {
            y = world.getHeightmapPos(config.heightmap, pos).getY();
        }

        return new BlockPos(pos.getX(), y, pos.getZ());
    }
}
