package net.tropicraft.world.mapgen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.perlin.NoiseModule;
import net.tropicraft.world.perlin.generator.Billowed;

public class MapGenVolcano {
    
    protected HashMap coordMap = new HashMap();
    
    public static List<BiomeGenBase> volcanoSpawnBiomesLand = Arrays.asList(new BiomeGenBase[] {
        BiomeGenTropicraft.tropics, BiomeGenTropicraft.rainforestPlains
    });
    public static List<BiomeGenBase> volcanoSpawnBiomesOcean = Arrays.asList(new BiomeGenBase[] {
            BiomeGenTropicraft.tropicsOcean
        });
    
    private World worldObj;
    
    private boolean useArrays;
    
    private final static int CHUNK_SIZE_X = 16;
    private final static int CHUNK_SIZE_Z = 16;
    private final static int CHUNK_SIZE_Y = 256;
    private final static int MAX_RADIUS = 65;
    private final static int MIN_RADIUS = 45;
    private final static int LAND_STEEPNESS_MOD = 4;
    private final static int OCEAN_STEEPNESS_MOD = 8;
    private final static int CALDERA_CUTOFF = 110; //The Y level where if the height of the volcano would pass becomes the caldera
    private final static int VOLCANO_TOP = 103; //The Y level cut off of the sides of the volcano
    private final static int VOLCANO_CRUST = 100; //The Y level where the crust of the volcano generates
    private final static int LAVA_LEVEL = 95; //The Y level where the top of the lava column is
    private final static int CRUST_HOLE_CHANCE = 15; //1 / x chance a certain block of the crust will be missing
    
    private final static Block VOLCANO_BLOCK = TCBlockRegistry.chunkOHead;
    private final static Block LAVA_BLOCK = Blocks.lava;
    
    public MapGenVolcano(World worldObj, boolean useArrays)
    {
    	this.worldObj = worldObj;
    	this.useArrays = useArrays;
    }
    
	public Block[] generate(int i, int k, Block[] blocks, byte[] metas)
	{
        ChunkCoordinates volcanoCoords = getVolcanoNear(worldObj, i, k);
        
        if (volcanoCoords == null) {
            return blocks;
        }
		int[] heightmap = new int[256];

		for(int x = 0; x < 16; x++)
		{
			for(int z = 0; z < 16; z++)
			{
				for(int y = 0; y < 127; y++)
				{
					Block blockID = getBlock(x, y, z, blocks);
					if(blockID == Blocks.air || blockID == TCBlockRegistry.tropicsWater)
					{
						heightmap[x * 16 + z] = y;
						break;
					}
					if(y > 75)
					{ 
						heightmap[x * 16 + z] = y;
						break;
					}
				}
			}
		}

        i *= CHUNK_SIZE_X;
        k *= CHUNK_SIZE_Z;
        
        int volcCenterX = volcanoCoords.posX;
        int volcCenterZ = volcanoCoords.posZ;
        int steepnessMod = volcanoCoords.posY == 1 ? LAND_STEEPNESS_MOD : OCEAN_STEEPNESS_MOD;
        
        long seed = (long)volcCenterX * 341873128712L + (long)volcCenterZ * 132897987541L + worldObj.getWorldInfo().getSeed() + (long)4291726;
        Random rand = new Random(seed);
        
        int radiusX = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
        int radiusZ = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
        
        NoiseModule volcNoise = new Billowed(seed, 1, 1);
        volcNoise.amplitude = 0.45;

        for(int x = 0; x < CHUNK_SIZE_X; x++)
        {
            for(int z = 0; z < CHUNK_SIZE_Z; z++)
            {
                float relativeX = ((x + i) - volcCenterX);
                float relativeZ = ((z + k) - volcCenterZ);

                float distanceSquared = ((relativeX / radiusX) * (relativeX / radiusX) + (relativeZ / radiusZ) * (relativeZ / radiusZ));

                float perlin = (float)volcNoise.getNoise(relativeX * 0.05 + 0.0001, relativeZ * 0.05 + 0.0001) + 1;
                
                double volcanoHeight = steepnessMod / (distanceSquared) * perlin - steepnessMod - 2;
                
                int groundHeight = heightmap[x * 16 + z];
                if(distanceSquared < 1)
                {
	                for(int y = CHUNK_SIZE_Y; y > 0; y--)
	                {
	                	if(volcanoHeight + groundHeight < CALDERA_CUTOFF)
	                	{
	                		if(volcanoHeight + groundHeight <= VOLCANO_TOP)
	                		{
	                			if(y <= volcanoHeight + groundHeight && y >= groundHeight)
		                		{
		                			placeBlock(x, y, z, VOLCANO_BLOCK, blocks);
		                		}
	                		}
	                		else if(y <= VOLCANO_TOP)
	                		{
	                			placeBlock(x, y, z, VOLCANO_BLOCK, blocks);
	                		}
	                	}
	                	else
	                	{
	                		if(y == VOLCANO_CRUST && rand.nextInt(CRUST_HOLE_CHANCE) != 0)
	                		{
	                			placeBlock(x, y, z, VOLCANO_BLOCK, blocks);
	                		}
	                		if(y <= LAVA_LEVEL)
	                		{
	                			placeBlock(x, y, z, LAVA_BLOCK, blocks);
	                		}
	                	}
	                }
                }
            }
        }
		
		return blocks;
	}
	
	public void placeBlock(int x, int y, int z, Block block, Block[] blocks)
	{
		blocks[x * CHUNK_SIZE_Y * 16 | z * CHUNK_SIZE_Y | y] = block;
	}
	
	public Block getBlock(int x, int y, int z, Block[] blocks)
	{
		return blocks[x * CHUNK_SIZE_Y * 16 | z * CHUNK_SIZE_Y | y];
	}
	
    /**
     * Method to choose spawn locations for volcanos (borrowed from village gen)
     * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers
     * mean more spawning)
     */
    protected int canGenVolcanoAtCoords(World worldObj, int i, int j)
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
        long seed = (long)randX * 341873128712L + (long)randZ * 132897987541L + worldObj.getWorldInfo().getSeed() + (long)4291726;
        Random rand = new Random(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);

        if (oldi == randX && oldj == randZ)
        {
            if(worldObj.getWorldChunkManager().areBiomesViable(oldi * 16 + 8, oldj * 16 + 8, 0, volcanoSpawnBiomesLand))
            {
            	return 1;
            }
            if(worldObj.getWorldChunkManager().areBiomesViable(oldi * 16 + 8, oldj * 16 + 8, 0, volcanoSpawnBiomesOcean))
            {
            	return 2;
            }
        }

        return 0;
    }

    /**
     * Returns the coordinates of a volcano if it should be spawned near
     * this chunk, otherwise returns null.
     * The posY of the returned object should be used as the volcano radius
     */
    public ChunkCoordinates getVolcanoNear(World worldObj, int i, int j) {
        //Check 4 chunks in each direction (volcanoes are never more than 4 chunks wide)
        int range = 4;
        for(int x = i - range; x <= i + range; x++) {
            for(int z = j - range; z <= j + range; z++) {
            	int biome = canGenVolcanoAtCoords(worldObj, x, z);
                if (biome != 0) {
                    return new ChunkCoordinates(x * 16 + 8, biome, z * 16 + 8);
                }
            }
        }
        
        return null;
    }
   
}