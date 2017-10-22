package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenUndergrowth extends TCGenBase {

	private static final int LARGE_BUSH_CHANCE = 5;
	
	private static final IBlockState WOOD_BLOCK = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.MAHOGANY);
	private static final IBlockState LEAF_BLOCK = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.MAHOGANY);
	
    public WorldGenUndergrowth(World world, Random rand) {
    		super(world, rand);
    }
    
    @Override
    public boolean generate(BlockPos pos) {
    	int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        Block blockUnder = worldObj.getBlockState(pos.down()).getBlock();
        
        if (blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
        	return false;
        }
        
        TCGenUtils.setBlockState(worldObj, pos, WOOD_BLOCK, blockGenNotifyFlag);

        int size = 2;
        if (rand.nextInt(LARGE_BUSH_CHANCE) == 0) {
        	size = 3;
        }
        
        for (int y = j; y < j + size; y++) {
            int bushWidth = size - (y - j);
            for (int x = i - bushWidth; x < i + bushWidth; x++) {
                int xVariance = x - i;
                for (int z = k - bushWidth; z < k + bushWidth; z++) {
                    int zVariance = z - k;
                    if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && !TCGenUtils.getBlockState(worldObj, x, y, z).isOpaqueCube()) {
                        TCGenUtils.setBlockState(worldObj, x, y, z, LEAF_BLOCK, blockGenNotifyFlag);
                    }
                }
            }
        }
        
        return true;
    }
    
}