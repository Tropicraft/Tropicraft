package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.core.common.block.BlockCoral;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenCoral extends WorldGenerator {

    public WorldGenCoral() {

    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        for (int i1 = 0; i1 < 12; i1++) {
            int x = (pos.getX() + random.nextInt(8)) - random.nextInt(8);
            int z = (pos.getZ() + random.nextInt(8)) - random.nextInt(8);
            int y;
            for (y = pos.getY(); (world.isAirBlock(pos.add(x, y, z)) || world.getBlockState(pos.add(x, y, z).down()).getMaterial() == Material.WATER) && y > 0; y--) {}

            BlockPos pos2 = new BlockPos(x, y, z);
            if (((BlockCoral)BlockRegistry.coral).canBlockStay(world, pos2)) {
            	int meta = random.nextInt(TropicraftCorals.VALUES.length);
                world.setBlockState(pos2, BlockRegistry.coral.getDefaultState().withProperty(BlockCoral.VARIANT, TropicraftCorals.byMetadata(meta)), 3);
            }
        }

        return true;
    }
}