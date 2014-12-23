package net.tropicraft.world.worldgen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

import java.util.Random;

public class WorldGenEIH extends TCGenBase
{

	private static final int CHUNK_SIZE_Y = 256;
	
	private static final Block EIH_BLOCK = TCBlockRegistry.chunkOHead;
	
    public WorldGenEIH(World worldObj, Random rand) {
    	super(worldObj, rand);
    }

    @Override
    /**
     * Generate the EIH statue with cool eyes and lava inside!
     * @param i x coordinate
     * @param j y coordinate
     * @param k z coordinate
     */
    public boolean generate(int i, int j, int k) {
        byte height = 5;
        
        if(j < 1 || j + height + 1 > CHUNK_SIZE_Y) {
            return false;
        }
    	
        if((worldObj.getBlock(i, j - 1, k) == Blocks.dirt || worldObj.getBlock(i, j - 1, k) == Blocks.grass) 
        		&& worldObj.getBlock(i, j, k) == Blocks.air) {
            j++;
            worldObj.setBlock(i + 0, j + 0, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 0, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 0, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 0, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 0, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 0, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 1, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 1, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 1, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 1, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 1, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 1, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 2, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 2, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 2, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 3, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 3, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 3, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 3, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 4, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 3, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 3, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 2, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 4, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 4, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 4, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 5, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 5, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 5, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 5, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 3, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 4, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 6, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 6, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 6, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 6, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 6, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 1, j + 5, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 1, j + 5, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 1, j + 4, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 1, j + 4, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 2, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 0, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 0, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 6, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 5, k + 0, Blocks.lava);
            worldObj.setBlock(i + -1, j + 4, k + 0, Blocks.lava);
            worldObj.setBlock(i + -1, j + 5, k + 0, Blocks.lava);
            worldObj.setBlock(i + -1, j + 3, k + 0, Blocks.lava);
            worldObj.setBlock(i + -1, j + 4, k + 1, Blocks.lava);
            worldObj.setBlock(i + -1, j + 3, k + 1, Blocks.lava);
            worldObj.setBlock(i + -1, j + 2, k + 1, Blocks.lava);
            worldObj.setBlock(i + -1, j + 3, k + 2, Blocks.lava);
            worldObj.setBlock(i + -1, j + 2, k + 2, Blocks.lava);
            worldObj.setBlock(i + -1, j + 1, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + 3, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 3, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 2, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 1, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 1, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 0, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 0, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 0, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 0, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 1, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 2, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 3, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 4, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 5, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 6, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 6, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 6, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 5, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 5, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 4, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 4, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 5, k + 0, Blocks.lava);
            worldObj.setBlock(i + -2, j + 4, k + 0, Blocks.lava);
            worldObj.setBlock(i + -2, j + 3, k + 0, Blocks.lava);
            worldObj.setBlock(i + -2, j + 4, k + 1, Blocks.lava);
            worldObj.setBlock(i + -2, j + 3, k + 1, Blocks.lava);
            worldObj.setBlock(i + -2, j + 2, k + 1, Blocks.lava);
            worldObj.setBlock(i + -2, j + 3, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + 2, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + 1, k + 2, Blocks.lava);
            worldObj.setBlock(i + -3, j + 0, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 0, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 0, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 0, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 1, k + 4, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 1, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 2, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 1, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 2, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 2, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 3, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 4, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 3, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 3, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 3, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 4, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 5, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 6, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 6, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + 6, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -4, j + 5, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -4, j + 4, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -4, j + 4, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + 4, k + 0, Blocks.lava);
            worldObj.setBlock(i + 0, j + 4, k + 1, Blocks.lava);
            worldObj.setBlock(i + -3, j + 4, k + 0, Blocks.lava);
            worldObj.setBlock(i + -3, j + 4, k + 1, Blocks.lava);
            worldObj.setBlock(i + -3, j + 5, k + 0, Blocks.lava);
            worldObj.setBlock(i + -4, j + 5, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 1, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 1, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + 0, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 0, k + -1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + -1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + -1, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -1, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -1, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + -1, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + -1, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -1, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -1, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + -2, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -2, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -2, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + -2, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -2, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -2, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + -2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + -2, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + -3, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -3, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + 0, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + 0, k + 2, Blocks.lava);
            worldObj.setBlock(i + -1, j + -1, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + -1, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + -2, k + 2, Blocks.lava);
            worldObj.setBlock(i + -1, j + -2, k + 2, Blocks.lava);
            worldObj.setBlock(i + -2, j + -3, k + 3, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -3, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -3, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + -3, k + 2, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -3, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -3, k + 1, EIH_BLOCK);
            worldObj.setBlock(i + -3, j + -3, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -2, j + -3, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + -1, j + -3, k + 0, EIH_BLOCK);
            worldObj.setBlock(i + 0, j + -3, k + 0, EIH_BLOCK);
            
            int k1 = rand.nextInt(7);
            
            // Coords of the first eye
            int eyeOneX = i;
            int eyeOneY = j + 5;
            int eyeOneZ = k + 1;
            
            // Coords of the second eye
            int eyeTwoX = i - 3;
            int eyeTwoY = j + 5;
            int eyeTwoZ = k + 1;
            
            // Place eyes
            placeEye(worldObj, eyeOneX, eyeOneY, eyeOneZ, k1, rand);
            placeEye(worldObj, eyeTwoX, eyeTwoY, eyeTwoZ, k1, rand);
        }
        return true;
    }
    
    /**
     * Place an eye on the head
     * @param worldObj World instance
     * @param x xCoord
     * @param y yCoord
     * @param z zCoord
     * @param eye_rand Randomized int value that determines which block the eye will be
     * @param rand Random object
     */
    private void placeEye(World worldObj, int x, int y, int z, int eye_rand, Random rand) {
    	Block block;
    	int meta = 0;
    	switch (eye_rand) {
	    	case 0:
	    	case 5:
	    		block = Blocks.glowstone;
	    		break;
	    	case 1:
	    		block = Blocks.obsidian;
	    		break;
	    	case 2:
	    		block = Blocks.diamond_block;
	    		break;
	    	case 3:
	    		block = Blocks.iron_block;
	    		break;
	    	case 4:
	    		block = Blocks.gold_block;
	    		break;
	    	case 6:
	    		block = TCBlockRegistry.oreBlocks;
	    		meta = rand.nextInt(3);
	    		break;
	    	default:	// Should never get called, if so, redstone in tropics :o
	    		block = Blocks.redstone_block;
	    		break;
    	}
        
    	worldObj.setBlock(x, y, z, block, meta, blockGenNotifyFlag);
    }   
}
