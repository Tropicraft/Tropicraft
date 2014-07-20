package net.tropicraft.world.worldgen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;

import java.util.Random;

public class WorldGenTCUndergrowth extends TCGenBase {

	private static final int LARGE_BUSH_CHANCE = 10;
	
	private static final Block WOOD_BLOCK = TCBlockRegistry.logs;
	private static final int WOOD_META = 1;
	private static final Block LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
	private static final int LEAF_META = 1;
	
    public WorldGenTCUndergrowth(World world, Random rand) {
    		super(world, rand);
    }
    
    @Override
    public boolean generate(int i, int j, int k) {
        Block blockUnder = worldObj.getBlock(i, j - 1, k);
        
        if (blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
        	return false;
        }
        
        worldObj.setBlock(i, j, k, WOOD_BLOCK, WOOD_META, 3);

        int size = 2;
        if(rand.nextInt(LARGE_BUSH_CHANCE) == 0) {
        	size = 3;
        }
        
        for (int y = j; y < j + size; y++) {
            int bushWidth = size - (y - j);
            for (int x = i - bushWidth; x < i + bushWidth; x++) {
                int xVariance = x - i;
                for (int z = k - bushWidth; z < k + bushWidth; z++) {
                    int zVariance = z - k;
                    if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && !worldObj.getBlock(x, y, z).isOpaqueCube()) {
                        worldObj.setBlock(x, y, z, LEAF_BLOCK, LEAF_META, 3);
                    }
                }
            }
        }
        
        return true;
    }
    
}