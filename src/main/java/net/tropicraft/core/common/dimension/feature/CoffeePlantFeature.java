package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class CoffeePlantFeature extends Feature<NoneFeatureConfiguration> {
    public static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
    public static final BlockState COFE = TropicraftBlocks.COFFEE_BUSH.get().getStateForAge(6);
    public static final BlockState FARMLAND = Blocks.FARMLAND.defaultBlockState();
    public static final BlockState WATER = Blocks.WATER.defaultBlockState();

    public CoffeePlantFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        final BlockPos genPos = new BlockPos(
                (pos.getX() + random.nextInt(8)) - random.nextInt(8),
                pos.getY(),
                (pos.getZ() + random.nextInt(8)) - random.nextInt(8)
        );

        // Find a suitable place to generate
        if (!world.isEmptyBlock(genPos) || world.getBlockState(genPos.below()).getBlock() != GRASS_BLOCK.getBlock() || world.isEmptyBlock(genPos.below(2))) {
            return false;
        }

        Direction viableDirection = null;

        // Scan for potential water spot
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final int neighborx = genPos.getX() + dir.getStepX();
            final int neighborz = genPos.getZ() + dir.getStepZ();

            if (!world.isEmptyBlock(new BlockPos(neighborx, pos.getY() - 1, neighborz))) {
                viableDirection = dir;
                break;
            }
        }

        if (viableDirection == null) {
            return false;
        }

        final BlockPos waterPos = new BlockPos(genPos.getX() + viableDirection.getStepX(), pos.getY() - 1, genPos.getZ() + viableDirection.getStepZ());
        world.setBlock(waterPos, WATER, Block.UPDATE_ALL);
        world.setBlock(genPos.below(), FARMLAND, Block.UPDATE_ALL);

        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            world.setBlock(waterPos.relative(dir), GRASS_BLOCK, Block.UPDATE_ALL);
        }

        for (int i = 0; i < 3; ++i) {
            final BlockPos upPos = genPos.above(i);
            if (world.isEmptyBlock(upPos)) {
                world.setBlock(upPos, COFE, Block.UPDATE_ALL);
            } else {
                break;
            }
        }

        return true;
    }
}
