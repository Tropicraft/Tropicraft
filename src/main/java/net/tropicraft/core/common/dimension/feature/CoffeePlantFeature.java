package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
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

    public CoffeePlantFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        int nx = (x + rand.nextInt(8)) - rand.nextInt(8);
        int nz = (z + rand.nextInt(8)) - rand.nextInt(8);

        int ny = y;

        final BlockPos genPos = new BlockPos(nx, ny, nz);
        if (!worldIn.isAirBlock(genPos) || worldIn.getBlockState(genPos.down()).getBlock() != Blocks.GRASS) {
            return false;
        }

        Direction viableDirection = null;

        // Scan for existing water
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            int neighborx = nx + dir.getXOffset();
            int neighborz = nz + dir.getZOffset();

            if (worldIn.getBlockState(new BlockPos(neighborx, ny - 1, neighborz)).getMaterial() == Material.WATER) {
                viableDirection = dir;
                break;
            }

        }

        if (viableDirection == null) {
            // Scan for places to put a water source block
            for (final Direction dir : Direction.Plane.HORIZONTAL) {
                int neighborx = nx + dir.getXOffset();
                int neighborz = nz + dir.getZOffset();
                final BlockPos waterPos = new BlockPos(neighborx, ny, neighborz);

                // isAirBlock call for ny - 2 is to prevent a waterfall from spawning
                if (!worldIn.isAirBlock(waterPos)
                        || worldIn.getBlockState(waterPos.down()).getBlock() != Blocks.GRASS
                        || worldIn.isAirBlock(waterPos.down(2))) {
                    continue;
                }

                // Check if the water block we'd place would be enclosed by grass (Don't want accidental waterfalls)
                boolean surrounded = true;

                for (final Direction surroundingDir : Direction.Plane.HORIZONTAL) {
                    int surroundingx = neighborx + surroundingDir.getXOffset();
                    int surroundingz = neighborz + surroundingDir.getZOffset();
                    final BlockPos surrPos = new BlockPos(surroundingx, ny, surroundingz);

                    if (!worldIn.isAirBlock(surrPos) || worldIn.getBlockState(surrPos.down()).getBlock() != Blocks.GRASS) {
                        surrounded = false;
                        break;
                    }
                }

                if (surrounded) {
                    viableDirection = dir;
                    break;
                }
            }
        }

        if (viableDirection == null) {
            return false;
        }

        worldIn.setBlockState(new BlockPos(nx + viableDirection.getXOffset(), ny - 1, nz + viableDirection.getZOffset()), Blocks.WATER.getDefaultState(), 3);
        worldIn.setBlockState(new BlockPos(nx, ny - 1, nz), Blocks.FARMLAND.getDefaultState(), 3);

        for (int i = 0; i < 3; ++i) {
            if (worldIn.isAirBlock(new BlockPos(nx, ny + i, nz))) {
                worldIn.setBlockState(new BlockPos(nx, ny + i, nz), TropicraftBlocks.COFFEE_BUSH.get().withAge(6), 3);
            } else {
                break;
            }
        }

        return true;
    }
}
