package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftOreBlock;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenEIH extends TCGenBase {
	
	private static final int CHUNK_SIZE_Y = 256;
	private static final Block EIH_BLOCK = BlockRegistry.chunk;

	public WorldGenEIH(World world, Random random) {
		super(world, random);
	}

    /**
     * Generate the EIH statue with cool eyes and lava inside!
     * @param pos Block position
     */
    @Override
    public boolean generate(BlockPos pos) {
        byte height = 5;
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        
        if (j < 1 || j + height + 1 > CHUNK_SIZE_Y) {
            return false;
        }

        Material matDown = worldObj.getBlockState(pos.down()).getMaterial();
    	
        if (matDown == Material.GROUND && isAirBlock(i, j, k)) {
            j++;
            setBlock(i + 0, j + 0, k + 2, EIH_BLOCK);
            setBlock(i + 0, j + 0, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + 0, k + 4, EIH_BLOCK);
            setBlock(i + -1, j + 0, k + 4, EIH_BLOCK);
            setBlock(i + -1, j + 0, k + 1, EIH_BLOCK);
            setBlock(i + -1, j + 0, k + 3, EIH_BLOCK);
            setBlock(i + -1, j + 1, k + 4, EIH_BLOCK);
            setBlock(i + 0, j + 1, k + 4, EIH_BLOCK);
            setBlock(i + -1, j + 1, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + 1, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + 1, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + 1, k + 1, EIH_BLOCK);
            setBlock(i + -1, j + 1, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + 2, k + 3, EIH_BLOCK);
            setBlock(i + -1, j + 2, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + 2, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + 2, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + 3, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + 3, k + 2, EIH_BLOCK);
            setBlock(i + 0, j + 3, k + 1, EIH_BLOCK);
            setBlock(i + 0, j + 3, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + 4, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + 3, k + -1, EIH_BLOCK);
            setBlock(i + 0, j + 3, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 2, k + -1, EIH_BLOCK);
            setBlock(i + 0, j + 4, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 4, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + 4, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 5, k + -1, EIH_BLOCK);
            setBlock(i + 0, j + 5, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 5, k + 1, EIH_BLOCK);
            setBlock(i + -1, j + 5, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + 3, k + 4, EIH_BLOCK);
            setBlock(i + -1, j + 4, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + 6, k + -1, EIH_BLOCK);
            setBlock(i + 0, j + 6, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + 6, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 6, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + 6, k + 1, EIH_BLOCK);
            setBlock(i + 1, j + 5, k + 0, EIH_BLOCK);
            setBlock(i + 1, j + 5, k + 1, EIH_BLOCK);
            setBlock(i + 1, j + 4, k + 1, EIH_BLOCK);
            setBlock(i + 1, j + 4, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + 2, k + 1, EIH_BLOCK);
            setBlock(i + 0, j + 2, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + 1, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + 0, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + 0, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + 6, k + 1, EIH_BLOCK);
            setBlock(i + 0, j + 5, k + 0, Blocks.LAVA);
            setBlock(i + -1, j + 4, k + 0, Blocks.LAVA);
            setBlock(i + -1, j + 5, k + 0, Blocks.LAVA);
            setBlock(i + -1, j + 3, k + 0, Blocks.LAVA);
            setBlock(i + -1, j + 4, k + 1, Blocks.LAVA);
            setBlock(i + -1, j + 3, k + 1, Blocks.LAVA);
            setBlock(i + -1, j + 2, k + 1, Blocks.LAVA);
            setBlock(i + -1, j + 3, k + 2, Blocks.LAVA);
            setBlock(i + -1, j + 2, k + 2, Blocks.LAVA);
            setBlock(i + -1, j + 1, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + 3, k + 4, EIH_BLOCK);
            setBlock(i + -2, j + 3, k + 3, EIH_BLOCK);
            setBlock(i + -2, j + 2, k + 3, EIH_BLOCK);
            setBlock(i + -2, j + 1, k + 3, EIH_BLOCK);
            setBlock(i + -2, j + 1, k + 4, EIH_BLOCK);
            setBlock(i + -2, j + 0, k + 4, EIH_BLOCK);
            setBlock(i + -2, j + 0, k + 3, EIH_BLOCK);
            setBlock(i + -2, j + 0, k + 1, EIH_BLOCK);
            setBlock(i + -2, j + 0, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + 1, k + 1, EIH_BLOCK);
            setBlock(i + -2, j + 1, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + 2, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + 2, k + -1, EIH_BLOCK);
            setBlock(i + -2, j + 3, k + -1, EIH_BLOCK);
            setBlock(i + -2, j + 4, k + -1, EIH_BLOCK);
            setBlock(i + -2, j + 5, k + -1, EIH_BLOCK);
            setBlock(i + -2, j + 6, k + -1, EIH_BLOCK);
            setBlock(i + -2, j + 6, k + 1, EIH_BLOCK);
            setBlock(i + -2, j + 6, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + 5, k + 2, EIH_BLOCK);
            setBlock(i + -2, j + 5, k + 1, EIH_BLOCK);
            setBlock(i + -2, j + 4, k + 2, EIH_BLOCK);
            setBlock(i + -2, j + 4, k + 3, EIH_BLOCK);
            setBlock(i + -2, j + 5, k + 0, Blocks.LAVA);
            setBlock(i + -2, j + 4, k + 0, Blocks.LAVA);
            setBlock(i + -2, j + 3, k + 0, Blocks.LAVA);
            setBlock(i + -2, j + 4, k + 1, Blocks.LAVA);
            setBlock(i + -2, j + 3, k + 1, Blocks.LAVA);
            setBlock(i + -2, j + 2, k + 1, Blocks.LAVA);
            setBlock(i + -2, j + 3, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + 2, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + 1, k + 2, Blocks.LAVA);
            setBlock(i + -3, j + 0, k + 0, EIH_BLOCK);
            setBlock(i + -3, j + 0, k + 2, EIH_BLOCK);
            setBlock(i + -3, j + 0, k + 3, EIH_BLOCK);
            setBlock(i + -3, j + 0, k + 4, EIH_BLOCK);
            setBlock(i + -3, j + 1, k + 4, EIH_BLOCK);
            setBlock(i + -3, j + 1, k + 3, EIH_BLOCK);
            setBlock(i + -3, j + 2, k + 3, EIH_BLOCK);
            setBlock(i + -3, j + 1, k + 0, EIH_BLOCK);
            setBlock(i + -3, j + 1, k + 2, EIH_BLOCK);
            setBlock(i + -3, j + 2, k + 2, EIH_BLOCK);
            setBlock(i + -3, j + 2, k + 1, EIH_BLOCK);
            setBlock(i + -3, j + 2, k + 0, EIH_BLOCK);
            setBlock(i + -3, j + 3, k + 2, EIH_BLOCK);
            setBlock(i + -3, j + 4, k + 2, EIH_BLOCK);
            setBlock(i + -3, j + 3, k + 1, EIH_BLOCK);
            setBlock(i + -3, j + 3, k + 0, EIH_BLOCK);
            setBlock(i + -3, j + 3, k + -1, EIH_BLOCK);
            setBlock(i + -3, j + 4, k + -1, EIH_BLOCK);
            setBlock(i + -3, j + 5, k + -1, EIH_BLOCK);
            setBlock(i + -3, j + 6, k + -1, EIH_BLOCK);
            setBlock(i + -3, j + 6, k + 0, EIH_BLOCK);
            setBlock(i + -3, j + 6, k + 1, EIH_BLOCK);
            setBlock(i + -4, j + 5, k + 0, EIH_BLOCK);
            setBlock(i + -4, j + 4, k + 0, EIH_BLOCK);
            setBlock(i + -4, j + 4, k + 1, EIH_BLOCK);
            setBlock(i + 0, j + 4, k + 0, Blocks.LAVA);
            setBlock(i + 0, j + 4, k + 1, Blocks.LAVA);
            setBlock(i + -3, j + 4, k + 0, Blocks.LAVA);
            setBlock(i + -3, j + 4, k + 1, Blocks.LAVA);
            setBlock(i + -3, j + 5, k + 0, Blocks.LAVA);
            setBlock(i + -4, j + 5, k + 1, EIH_BLOCK);
            setBlock(i + -2, j + 1, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 1, k + -1, EIH_BLOCK);
            setBlock(i + -2, j + 0, k + -1, EIH_BLOCK);
            setBlock(i + -1, j + 0, k + -1, EIH_BLOCK);
            setBlock(i + -3, j + -1, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + -1, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + -1, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + -1, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + -1, k + 1, EIH_BLOCK);
            setBlock(i + -1, j + -1, k + 1, EIH_BLOCK);
            setBlock(i + -3, j + -1, k + 2, EIH_BLOCK);
            setBlock(i + 0, j + -1, k + 2, EIH_BLOCK);
            setBlock(i + -2, j + -1, k + 3, EIH_BLOCK);
            setBlock(i + -1, j + -1, k + 3, EIH_BLOCK);
            setBlock(i + -3, j + -2, k + 2, EIH_BLOCK);
            setBlock(i + -2, j + -2, k + 3, EIH_BLOCK);
            setBlock(i + -1, j + -2, k + 3, EIH_BLOCK);
            setBlock(i + 0, j + -2, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + -2, k + 1, EIH_BLOCK);
            setBlock(i + -2, j + -2, k + 1, EIH_BLOCK);
            setBlock(i + -3, j + -2, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + -2, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + -2, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + -2, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + -3, k + 2, EIH_BLOCK);
            setBlock(i + -1, j + -3, k + 3, EIH_BLOCK);
            setBlock(i + -1, j + 0, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + 0, k + 2, Blocks.LAVA);
            setBlock(i + -1, j + -1, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + -1, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + -2, k + 2, Blocks.LAVA);
            setBlock(i + -1, j + -2, k + 2, Blocks.LAVA);
            setBlock(i + -2, j + -3, k + 3, EIH_BLOCK);
            setBlock(i + -1, j + -3, k + 2, EIH_BLOCK);
            setBlock(i + -2, j + -3, k + 2, EIH_BLOCK);
            setBlock(i + -3, j + -3, k + 2, EIH_BLOCK);
            setBlock(i + -2, j + -3, k + 1, EIH_BLOCK);
            setBlock(i + -1, j + -3, k + 1, EIH_BLOCK);
            setBlock(i + -3, j + -3, k + 0, EIH_BLOCK);
            setBlock(i + -2, j + -3, k + 0, EIH_BLOCK);
            setBlock(i + -1, j + -3, k + 0, EIH_BLOCK);
            setBlock(i + 0, j + -3, k + 0, EIH_BLOCK);
            
            int k1 = rand.nextInt(7);
    		int tropiBlockMeta = rand.nextInt(3);
            
            // Coords of the first eye
            int eyeOneX = i;
            int eyeOneY = j + 5;
            int eyeOneZ = k + 1;
            
            // Coords of the second eye
            int eyeTwoX = i - 3;
            int eyeTwoY = j + 5;
            int eyeTwoZ = k + 1;
            
            // Place eyes
            placeEye(eyeOneX, eyeOneY, eyeOneZ, k1, tropiBlockMeta);
            placeEye(eyeTwoX, eyeTwoY, eyeTwoZ, k1, tropiBlockMeta);
        }
        return true;
    }
    
    /**
     * Place an eye on the head
     * @param x xCoord
     * @param y yCoord
     * @param z zCoord
     * @param eye_rand Randomized int value that determines which block the eye will be
     * @param tropiBlockMeta Metadata of Tropicraft OreBlock, if it ends up being the one used
     */
    private void placeEye(int x, int y, int z, int eye_rand, int tropiBlockMeta) {
    	IBlockState blockstate;
    	switch (eye_rand) {
	    	case 0:
	    	case 5:
	    		blockstate = Blocks.GLOWSTONE.getDefaultState();
	    		break;
	    	case 1:
	    		blockstate = Blocks.OBSIDIAN.getDefaultState();
	    		break;
	    	case 2:
	    		blockstate = Blocks.DIAMOND_BLOCK.getDefaultState();
	    		break;
	    	case 3:
	    		blockstate = Blocks.IRON_BLOCK.getDefaultState();
	    		break;
	    	case 4:
	    		blockstate = Blocks.GOLD_BLOCK.getDefaultState();
	    		break;
	    	case 6:
	    		blockstate = BlockRegistry.oreBlock.getDefaultState().withProperty(BlockTropicraftOreBlock.VARIANT, TropicraftOreBlocks.VALUES[tropiBlockMeta]);
	    		break;
	    	default:	// Should never get called, if so, redstone in tropics :o
	    		blockstate = Blocks.REDSTONE_BLOCK.getDefaultState();
	    		break;
    	}
        
    	setBlockState(x, y, z, blockstate, blockGenNotifyFlag);
    }   
}
