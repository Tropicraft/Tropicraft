package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.function.BiFunction;

public class SeagrassFeature extends Feature<NoneFeatureConfiguration> {
    private static final BiFunction<Boolean, Boolean, SeagrassData> SEAGRASS_DATA = Util.memoize(
            (deep, selector) -> {
                if (deep) {
                    return selector
                            ? new SeagrassData(TropicraftBlocks.SICKLE_SEAGRASS.get(), TropicraftBlocks.MATTED_SICKLE_SEAGRASS.get(), TropicraftBlocks.TALL_SICKLE_SEAGRASS.get())
                            : new SeagrassData(TropicraftBlocks.NOODLE_SEAGRASS.get(), TropicraftBlocks.MATTED_NOODLE_SEAGRASS.get(), null);
                } else {
                    return selector
                            ? // Eel grass is special, as it has a flowering variety- represent that here
                            new SeagrassData(TropicraftBlocks.EEL_GRASS.get(), TropicraftBlocks.MATTED_EEL_GRASS.get(), TropicraftBlocks.TALL_EEL_GRASS.get(),
                                    new SeagrassData(TropicraftBlocks.FLOWERING_EEL_GRASS.get(), TropicraftBlocks.MATTED_EEL_GRASS.get(), TropicraftBlocks.FLOWERING_TALL_EEL_GRASS.get()))
                            : new SeagrassData(TropicraftBlocks.FERN_SEAGRASS.get(), TropicraftBlocks.MATTED_FERN_SEAGRASS.get(), TropicraftBlocks.TALL_FERN_SEAGRASS.get());
                }
            }
    );

    public SeagrassFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        // Prevent too much homogeneity in the seagrass distribution
        if (random.nextInt(4) == 0) {
            return false;
        }

        int floorOrigin = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int surfaceOrigin = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
        int depth = surfaceOrigin - floorOrigin;
        if (depth <= 0) {
            return false;
        }

        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(level.getSeed()));
        NormalNoise seagrassSelector = NormalNoise.create(worldgenrandom, -6, 1.0);

        int rad = random.nextInt(4) + 8;

        // Check which family of seagrass to use: deep or shallow
        boolean deep = depth >= 18;

        for (int x = -rad; x <= rad; x++) {
            for (int z = -rad; z <= rad; z++) {
                if (x * x + z * z > rad * rad) {
                    continue;
                }

                int floor = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() + x, pos.getZ() + z);
                int surface = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + x, pos.getZ() + z);
                if (surface <= floor) {
                    continue;
                }

                BlockPos local = pos.offset(x, 0, z).atY(floor);
                if (!level.getFluidState(local).is(FluidTags.WATER)) {
                    continue;
                }

                boolean selection = seagrassSelector.getValue(local.getX(), pos.getY(), local.getZ()) > 0.0;

                SeagrassData data = SEAGRASS_DATA.apply(deep, selection);
                Block matted = data.matted;
                Block tall = data.tall;
                Block seagrass = data.seagrass;
                SeagrassData flowering = data.flowering;

                // Make eelgrass flower rarely, for more variety
                if (flowering != null && random.nextInt(8) == 0) {
                    seagrass = flowering.seagrass;
                    tall = flowering.tall;
                }

                // Place matted floor
                if (random.nextInt(8) > 0) {
                    level.setBlock(local.below(), matted.defaultBlockState(), 2);
                }

                // Place seagrass
                boolean placed = false;

                // Attempt to place tall seagrass
                if (tall != null && random.nextInt(10) == 0 && level.getBlockState(local.above()).getFluidState().is(FluidTags.WATER)) {
                    level.setBlock(local.above(), tall.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 2);
                    level.setBlock(local, tall.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER), 2);
                    placed = true;
                }

                // Place regular seagrass

                // Noodle seagrass doesn't have a tall variety, so it should spawn more to compensate
                if (!placed && random.nextInt(tall == null ? 3 : 8) == 0) {
                    level.setBlock(local, seagrass.defaultBlockState(), 2);
                    placed = true;
                }

                // If we've placed nothing so far, place some sea pickles to light up the area
                if (!placed && random.nextInt(80) == 0) {
                    level.setBlock(local, Blocks.SEA_PICKLE.defaultBlockState().setValue(BlockStateProperties.PICKLES, random.nextInt(4) + 1), 2);
                }
            }
        }

        return true;
    }

    record SeagrassData(Block seagrass, Block matted, Block tall, SeagrassData flowering) {
        SeagrassData(Block seagrass, Block matted, Block tall) {
            this(seagrass, matted, tall, null);
        }
    }
}
