package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockIris;
import net.tropicraft.core.common.block.BlockPineapple;
import net.tropicraft.core.common.block.BlockTallPlant;

public class WorldGenTallFlower extends TCGenBase {

	private int FLOWER_TRIES = 8;
	
	IBlockState plantBlockState;
    
    public WorldGenTallFlower(World world, Random rand, IBlockState plantBlockState) {
    	super(world, rand);
    	this.plantBlockState = plantBlockState;
    }

	@Override
	public boolean generate(BlockPos pos) {
        for(int l = 0; l < FLOWER_TRIES; l++) {
            int i1 = (pos.getX() + rand.nextInt(12)) - rand.nextInt(12);
            int j1 = (pos.getY() + rand.nextInt(3)) - rand.nextInt(3);
            int k1 = (pos.getZ() + rand.nextInt(12)) - rand.nextInt(12);
            BlockPos newPos = new BlockPos(i1, j1, k1);
            if (worldObj.isAirBlock(newPos) && worldObj.isAirBlock(newPos.up())) { 
            	if (plantBlockState.getBlock() instanceof BlockPineapple) {
            		BlockPineapple block = (BlockPineapple)plantBlockState.getBlock();
            		IBlockState bottomState = block.getDefaultState().withProperty(BlockPineapple.HALF, BlockTallPlant.PlantHalf.LOWER).withProperty(BlockPineapple.STAGE, BlockPineapple.TOTAL_GROW_TICKS);
            		IBlockState topState = block.getDefaultState().withProperty(BlockPineapple.HALF, BlockTallPlant.PlantHalf.UPPER);

            		if (block.canBlockStay(worldObj, newPos, worldObj.getBlockState(newPos))) {
                        worldObj.setBlockState(newPos, bottomState, blockGenNotifyFlag);
                        worldObj.setBlockState(newPos.up(), topState, blockGenNotifyFlag);
            		}
            		
            	} else if (plantBlockState.getBlock() instanceof BlockIris) {
            		BlockIris block = (BlockIris)plantBlockState.getBlock();
            		IBlockState bottomState = block.getDefaultState().withProperty(BlockIris.HALF, BlockIris.PlantHalf.LOWER);
            		IBlockState topState = block.getDefaultState().withProperty(BlockIris.HALF, BlockIris.PlantHalf.UPPER);

            		if (block.canBlockStay(worldObj, newPos, worldObj.getBlockState(newPos))) {
                        worldObj.setBlockState(newPos, bottomState, blockGenNotifyFlag);
                        worldObj.setBlockState(newPos.up(), topState, blockGenNotifyFlag);
            		}
            	}
            }

        }

        return true;
	}
}
