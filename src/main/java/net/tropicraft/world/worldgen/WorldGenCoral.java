package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenCoral extends WorldGenerator {

    public WorldGenCoral() {

    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        for (int i1 = 0; i1 < 12; i1++) {
            int x = (i + random.nextInt(8)) - random.nextInt(8);
            int z = (k + random.nextInt(8)) - random.nextInt(8);
            int y;
            for (y = j; (world.isAirBlock(x, y, z) || world.getBlock(x, y - 1, z).getMaterial() == Material.water) && y > 0; y--) {}
            
            if (TCBlockRegistry.coral.canBlockStay(world, x, y, z)) {
                world.setBlock(x, y, z, TCBlockRegistry.coral, random.nextInt(6), 3);
            }
        }

        return true;
    }
}