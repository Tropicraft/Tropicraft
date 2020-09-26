package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.function.Function;

public class CoffeePlantFeature extends Feature<NoFeatureConfig> {

    public static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
    public static final BlockState COFE = TropicraftBlocks.COFFEE_BUSH.get().withAge(6);
    public static final BlockState FARMLAND = Blocks.FARMLAND.getDefaultState();
    public static final BlockState WATER = Blocks.WATER.getDefaultState();

    public CoffeePlantFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        final BlockPos genPos = new BlockPos((pos.getX() + rand.nextInt(8)) - rand.nextInt(8), pos.getY(), (pos.getZ() + rand.nextInt(8)) - rand.nextInt(8));

        // Find a suitable place to generate
        if (!worldIn.isAirBlock(genPos) || worldIn.getBlockState(genPos.down()).getBlock() != GRASS_BLOCK.getBlock() || worldIn.isAirBlock(genPos.down(2))) {
            return false;
        }

        Direction viableDirection = null;

        // Scan for potential water spot
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final int neighborx = genPos.getX() + dir.getXOffset();
            final int neighborz = genPos.getZ() + dir.getZOffset();

            if (!worldIn.isAirBlock(new BlockPos(neighborx, pos.getY() - 1, neighborz))) {
                viableDirection = dir;
                break;
            }
        }

        if (viableDirection == null) {
            return false;
        }

        final BlockPos waterPos = new BlockPos(genPos.getX() + viableDirection.getXOffset(), pos.getY() - 1, genPos.getZ() + viableDirection.getZOffset());
        worldIn.setBlockState(waterPos, WATER, 3);
        worldIn.setBlockState(genPos.down(), FARMLAND, 3);

        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            worldIn.setBlockState(waterPos.offset(dir), GRASS_BLOCK, 3);
        }

        for (int i = 0; i < 3; ++i) {
            final BlockPos upPos = genPos.up(i);
            if (worldIn.isAirBlock(upPos)) {
                worldIn.setBlockState(upPos, COFE, 3);
            } else {
                break;
            }
        }

        return true;
    }
}
