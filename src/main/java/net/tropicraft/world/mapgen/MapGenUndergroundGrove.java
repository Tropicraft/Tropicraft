package net.tropicraft.world.mapgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.perlin.generator.RidgedMulti;

public class MapGenUndergroundGrove {

	private static final int  CHUNK_SIZE_Y = 256;
	
	private World worldObj;
	
	public boolean isActive = false;
	public int centerX = 0;
	public int centerZ = 0;
	public double length;
	public double width;
	public double height;
	public int y;
	
	public MapGenUndergroundGrove(World worldObj) {
		this.worldObj = worldObj;
	}
	
	public Block[] generate(int x, int z, Block[] blocks, byte[] metas) {
		ChunkCoordinates groveCoords = getGroveNear(worldObj, x, z);
		
		if(groveCoords != null) {
			centerX = groveCoords.posX;
			y = groveCoords.posY;
			centerZ = groveCoords.posZ;
			x *= 16;
			z *= 16;
		} else {
			return blocks;
		} 
		
		isActive = true;
		
    	Random rand = new Random(worldObj.getSeed() * centerX + centerZ * 57647382913L);
    	
    	RidgedMulti ridged = new RidgedMulti(rand.nextLong(), 1);
    	ridged.frequency = 0.0325;
		
    	length = rand.nextInt(20) + 30;
    	width = rand.nextInt(20) + 30;
    	height = rand.nextInt(3) + 5;
    	
    	length *= length;
    	width *= width;
    	height *= height;
    	
    	for(int i = 0; i < 16; i++) {
    		for(int k = 0; k < 16; k++) {
				int relativeX = (x + i) - centerX;
				int relativeZ = (z + k) - centerZ;
				relativeX *= relativeX;
				relativeZ *= relativeZ;
    			for(double j = -height; j < height; j++) {
    				if((relativeX / length) + ((j * j) / height) + (relativeZ / width) <= 1) {
    					placeBlock(i, y + (int)j, k, Blocks.air, blocks);
    				}
    			}

    			double noise1 = ridged.getNoise(x + i, z + k);
    			double noise2 = ridged.getNoise(x + i + 15432, z + k + 42314);
    			if(noise1 > 0.845 || noise2 > 0.855)
    			{
    				int j = (int)Math.sqrt(height - ((height * relativeX) / length) - ((height * relativeZ) / width));
    				placeBlock(i, y - j - 1, k, Blocks.dirt, blocks);
    				
    				double tunnelHeight = (5 - (((relativeX / 2500D) + (relativeZ / 2500D)) * 2)) / 3;
    				for(int j2 = 0; j2 < tunnelHeight; j2++) {
        				placeBlock(i, y - j + j2, k, Blocks.air, blocks);    					
    				}
    			}
    			
    			if((i + x) % 16 == 0 && (k + z) % 16 == 0) {
    				int j = (int)Math.sqrt(height - ((height * relativeX) / length) - ((height * relativeZ) / width));
    				rand.setSeed(i * k * 54325432 * worldObj.getSeed() * relativeX * centerX);
    				if(getBlock(i, y - j, k, blocks) == Blocks.air && getBlock(i, y - j + 1, k, blocks) == Blocks.air && getBlock(i, y - j + 2, k, blocks) == Blocks.air && rand.nextInt(3) != 0) {
    					placeBlockAndMeta(i, y - j, k, TCBlockRegistry.tikiTorch, 1, blocks, metas);
    					placeBlockAndMeta(i, y - j + 1, k, TCBlockRegistry.tikiTorch, 1, blocks, metas);
    					placeBlockAndMeta(i, y - j + 2, k, TCBlockRegistry.tikiTorch, 0, blocks, metas);
    				}
    			}
    		}
    	}
    	
		return blocks;
	}
	
	public int getHeightAt(int x, int z) {
		int relativeX = x - centerX;
		int relativeZ = z - centerZ;
		relativeX *= relativeX;
		relativeZ *= relativeZ;
		return y - (int)Math.sqrt(height - ((height * relativeX) / length) - ((height * relativeZ) / width));
	}
	
    /**
     * Method to choose spawn locations for volcanos (borrowed from village gen)
     * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers
     * mean more spawning)
     */
	protected boolean canGenGroveAtCoords(World worldObj, int i, int j)
    {
		byte numChunks = 32;
        byte offsetChunks = 8;
        int oldi = i;
        int oldj = j;

        if (i < 0)
        {
            i -= numChunks - 1;
        }

        if (j < 0)
        {
            j -= numChunks - 1;
        }

        int randX = i / numChunks;
        int randZ = j / numChunks;
        long seed = (long)randX * 341832132712L + (long)randZ * 422843987541L + worldObj.getWorldInfo().getSeed() + (long)42231726;
        Random rand = new Random(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);

        if (oldi == randX && oldj == randZ)
        {
        	return true;
        }

        return false;
    }

    
    /**
     * Returns the coordinates of a grove if it should be spawned near
     * this chunk, otherwise returns null.
     * The posY of the returned object should be used as the grove radius
     */
    public ChunkCoordinates getGroveNear(World worldObj, int i, int j) {
        //Check 4 chunks in each direction (Groves are never more than 4 chunks wide)
        int range = 4;
        for(int x = i - range; x <= i + range; x++) {
            for(int z = j - range; z <= j + range; z++) {
            	Random rand = new Random(worldObj.getSeed() * x + z * 57647382913L);
                if (canGenGroveAtCoords(worldObj, x, z)) {
                    return new ChunkCoordinates(x * 16 + 8, rand.nextInt(5) + 20, z * 16 + 8);
                }
            }
        }
        
        return null;
    }
	
	private void placeBlock(int x, int y, int z, Block block, Block[] blocks) {
		blocks[x * CHUNK_SIZE_Y * 16 | z * CHUNK_SIZE_Y | y] = block;
	}
	
	private void placeBlockAndMeta(int x, int y, int z, Block block, int meta, Block[] blocks, byte[] metas) {
		blocks[x * CHUNK_SIZE_Y * 16 | z * CHUNK_SIZE_Y | y] = block;
		metas[x * CHUNK_SIZE_Y * 16 | z * CHUNK_SIZE_Y | y] = (byte)(meta & 0xF);
	}
	
	private Block getBlock(int x, int y, int z, Block[] blocks) {
		return blocks[x * CHUNK_SIZE_Y * 16 | z * CHUNK_SIZE_Y | y];
	}
	
}
