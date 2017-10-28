package net.tropicraft.core.common.worldgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.core.registry.BlockRegistry;

public abstract class TCGenBase extends WorldGenerator {
	
	protected World worldObj;
	
	protected Random rand;
	
	/**Used in placeBlockLine*/
	static final byte otherCoordPairs[] = {
        2, 0, 0, 1, 2, 1
    };
	
	public static final int MAX_CHUNK_HEIGHT = 256;
	
	/**Blocks normally checked in the check methods*/
	protected List<Block> standardAllowedBlocks = Arrays.asList(Blocks.AIR, Blocks.LEAVES, Blocks.TALLGRASS, Blocks.SNOW_LAYER, Blocks.SNOW);
	
	/** Default flag for the blockGenNotifyFlag below */
	public static final int BLOCK_GEN_NOTIFY_FLAG_DEFAULT = 2;
	
	/** Added by Corosus to reduce/remove the many second client side stallout, was either because of lighting updates or just general client side chunk update spam 
	 * Setting to 0 solves the issue and doesn't seem to cause much issue server side block notify wise as far as I can tell, refine more if it does */	
	public static int blockGenNotifyFlag = BLOCK_GEN_NOTIFY_FLAG_DEFAULT;
	
	public TCGenBase(World world, Random random) {
		worldObj = world;
		rand = random;
	}
	
	/** Checks if the ID is that of a leaf block*/
	public boolean isLeafBlock(Block block) {
		return block == Blocks.LEAVES || block == BlockRegistry.leaves;
	}
	
	public abstract boolean generate(BlockPos pos);
	
	/** Allows this class to work as a WorldGenerator object */
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		worldObj = world;
		this.rand = rand;
		return generate(pos);
	}
	
	protected boolean genCircle(int x, int y, int z, double outerRadius, double innerRadius, IBlockState state, boolean solid) {
		return genCircle(new BlockPos(x, y, z), outerRadius, innerRadius, state, solid);
	}
	
	/** 
	 * Generates a circle
	 * @param x The x coordinate of the center of the circle
 	 * @param y The y coordinate of the center of the circle
	 * @param z The z coordinate of the center of the circle
	 * @param outerRadius The radius of the circle's outside edge
	 * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
	 * @param id The ID to generate with
	 * @param meta The metadata to generate with
	 * @param solid Whether it should place the block if another block is already occupying that space
	 */
	public boolean genCircle(BlockPos pos, double outerRadius, double innerRadius, IBlockState state, boolean solid) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean hasGenned = false;
		for(int i = (int)(-outerRadius - 1) + x; i <= (int)(outerRadius + 1) + x; i++) {
			for(int k = (int)(-outerRadius - 1) + z; k <= (int)(outerRadius + 1) + z; k++) {
				double d = (i - x) * (i - x) + (k - z) * (k - z);
				if(d <= outerRadius * outerRadius && d >= innerRadius * innerRadius) {
					BlockPos pos2 = new BlockPos(i, y, k);
					if(worldObj.isAirBlock(pos2) || solid) {
						if(worldObj.setBlockState(pos2, state, blockGenNotifyFlag)) {
							hasGenned = true;
						}
					}
				}
			}
		}
		return hasGenned;
	}
	
	/**
	 * Checks whether any blocks not specified in allowedBlockList exist in that circle
	 * @param x The x coordinate of the center of the circle
 	 * @param y The y coordinate of the center of the circle
	 * @param z The z coordinate of the center of the circle
	 * @param outerRadius The radius of the circle's outside edge
	 * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
	 * @param allowedBlockList The blocks to exclude from the check
	 * @return Whether any blocks not specified in allowedBlockList exist in that circle
	 */
	public boolean checkCircle(BlockPos pos, double outerRadius, double innerRadius, List<Block> allowedBlockList) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		for(int x = (int)(-outerRadius - 2) + i; x < (int)(outerRadius + 2) + i; x++) {
			for(int z = (int)(-outerRadius - 2) + k; z < (int)(outerRadius + 2) + k; z++) {
				double d = (i - x) * (i - x) + (k - z) * (k - z);
				if(d <= outerRadius * outerRadius && d >= innerRadius * innerRadius) {
					BlockPos pos2 = new BlockPos(x, j, z);
					if(!allowedBlockList.contains(worldObj.getBlockState(pos2).getBlock())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks whether any blocks not specified in allowedBlockList exist in that line
	 * @param ai One end of the line
	 * @param ai1 The other end of the line
	 * @param allowedBlockList The block to exclude from the check
	 * @return Whether any blocks not specified in allowedBlockList exist in that circle
	 */
	public boolean checkBlockLine(int ai[], int ai1[], List<Block> allowedBlockList)
    {
        int ai2[] = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for(; byte0 < 3; byte0++)
        {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
            {
                j = byte0;
            }
        }

        if(ai2[j] == 0)
        {
            return false;
        }
        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;
        if(ai2[j] > 0) {
            byte3 = 1;
        } else {
            byte3 = -1;
        }
        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int ai3[] = {
            0, 0, 0
        };
        int k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3) {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
			if(!allowedBlockList.contains(worldObj.getBlockState(pos).getBlock())) {
				return false;
			}
        }
        return true;
    }
	
	/**
	 * Places a line from coords ai to coords ai1
	 * @param ai One end of the line
	 * @param ai1 The other end of the line
	 * @param state IBlockState to place
	 * @return The coords that blocks were placed on
	 */
	public ArrayList<int[]> placeBlockLine(int ai[], int ai1[], IBlockState state)
    {
		ArrayList<int[]> places = new ArrayList<int[]>();
        int ai2[] = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for(; byte0 < 3; byte0++)
        {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
            {
                j = byte0;
            }
        }

        if(ai2[j] == 0)
        {
            return null;
        }
        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;
        if(ai2[j] > 0)
        {
            byte3 = 1;
        } else
        {
            byte3 = -1;
        }
        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int ai3[] = {
            0, 0, 0
        };
        int k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            worldObj.setBlockState(pos, state, blockGenNotifyFlag);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }
	
	/**
	 * Checks whether any blocks not specified in allowedBlockList exist in that line of circles
	 * @param ai One end of the line
	 * @param ai1 The other end of the line
	 * @param outerRadius The radius of the circle's outside edge
	 * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
	 * @param allowedBlockList The block to exclude from the check
	 * @return
	 */
	public boolean checkBlockCircleLine(int ai[], int ai1[], double outerRadius, double innerRadius, List<Block> allowedBlockList) {
        int ai2[] = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for(; byte0 < 3; byte0++)
        {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
            {
                j = byte0;
            }
        }

        if(ai2[j] == 0)
        {
            return false;
        }
        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;
        if(ai2[j] > 0)
        {
            byte3 = 1;
        } else
        {
            byte3 = -1;
        }
        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int ai3[] = {
            0, 0, 0
        };
        int k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            if(!checkCircle(pos, outerRadius, innerRadius, allowedBlockList))
           	{
            	return false;
      		}
        }
        return true;
    }
	
	/**
	 * Checks whether any blocks not specified in allowedBlockList exist in that line of circles and if not places the block circle line
	 * @param ai One end of the line
	 * @param ai1 The other end of the line
	 * @param outerRadius The radius of the circle's outside edge
	 * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
	 * @param block The block to generate the block circle line with
	 * @param meta The metadata to generate the block circle line with
	 * @param allowedBlockList The block to exclude from the check
	 * @return The coordinates where a circle was generated
	 */
	public ArrayList<int[]> checkAndPlaceBlockCircleLine(int ai[], int ai1[], double outerRadius, double innerRadius, IBlockState state, List<Block> allowedBlockList)
    {
		ArrayList<int[]> places = new ArrayList<int[]>();
        int ai2[] = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for(; byte0 < 3; byte0++)
        {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
            {
                j = byte0;
            }
        }

        if(ai2[j] == 0)
        {
            return null;
        }
        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;
        if(ai2[j] > 0)
        {
            byte3 = 1;
        } else
        {
            byte3 = -1;
        }
        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int ai3[] = {
            0, 0, 0
        };
        int k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            if(!checkCircle(pos, outerRadius, innerRadius, allowedBlockList))
            {
            	return null;
            }
        }
        k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            genCircle(pos, outerRadius, innerRadius, state, true);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }
	
	/**
	 * Checks if any blocks not specified in allowedBlockList exist within the line and if not places the line
	 * @param ai One end of the line
	 * @param ai1 The other end of the line
	 * @param block The block to generate the block circle line with
	 * @param meta The metadata to generate the block circle line with
	 * @param allowedBlockList The block to exclude from the check
	 * @return The coordinates where a block was placed
	 */
	public ArrayList<int[]> checkAndPlaceBlockLine(int ai[], int ai1[], IBlockState state, List allowedBlockList)
    {
		ArrayList<int[]> places = new ArrayList<int[]>();
        int ai2[] = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for(; byte0 < 3; byte0++)
        {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
            {
                j = byte0;
            }
        }

        if(ai2[j] == 0)
        {
            return null;
        }
        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;
        if(ai2[j] > 0)
        {
            byte3 = 1;
        } else
        {
            byte3 = -1;
        }
        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int ai3[] = {
            0, 0, 0
        };
        int k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = TCGenUtils.getBlockPos(ai3);
			if(!allowedBlockList.contains(worldObj.getBlockState(pos)))
			{
				return null;
			}
        }
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = TCGenUtils.getBlockPos(ai3);
            worldObj.setBlockState(pos, state, blockGenNotifyFlag);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }
	
	/**
	 * Places a block circle line
	 * @param ai One end of the line
	 * @param ai1 The other end of the line
	 * @param outerRadius The radius of the circle's outside edge
	 * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
	 * @param block The block to generate the block circle line with
	 * @param meta The metadata to generate the block circle line with
	 * @param allowedBlockList The block to exclude from the check
	 * @return The coordinates where a circle was generated
	 */
	public ArrayList<int[]> placeBlockCircleLine(int ai[], int ai1[], double distance, double distance2, IBlockState state)
    {
		ArrayList<int[]> places = new ArrayList<int[]>();
        int ai2[] = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for(; byte0 < 3; byte0++)
        {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
            {
                j = byte0;
            }
        }

        if(ai2[j] == 0)
        {
            return null;
        }
        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;
        if(ai2[j] > 0)
        {
            byte3 = 1;
        } else
        {
            byte3 = -1;
        }
        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int ai3[] = {
            0, 0, 0
        };
        int k = 0;
        for(int l = ai2[j] + byte3; k != l; k += byte3)
        {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            genCircle(TCGenUtils.getBlockPos(ai3), distance, distance2, state, true);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }
	
	/**
	 * Generates a sphere at the specified coordinates
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @param outerRadius The radius of the sphere's outside edge
	 * @param block The block to generate the sphere with
	 * @param meta The block metadata to generate the sphere with
	 */
	public void genSphere(BlockPos pos, int outerRadius, IBlockState state)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for(int i = x - outerRadius; i < x + outerRadius; i++)
	    {
	         for(int j = y - outerRadius; j < y + outerRadius; j++)
	         {
	            for(int k = z - outerRadius; k < z + outerRadius; k++)
	            {
	            	if(worldObj.isAirBlock(TCGenUtils.getBlockPos(i, j, k)))
               	 	{
		                 int distance1 = (i - x) * (i - x) + (j - y) * (j - y) + (k - z) * (k - z);
	
		                 if(distance1 <= outerRadius)
		                 {
		                	 BlockPos pos2 = TCGenUtils.getBlockPos(i, j, k);
		                	 worldObj.setBlockState(pos2, state, blockGenNotifyFlag);
		                 }
               	 	}
	            }
	         }
	    }
	}
	
	/**
	 * Gets the terrain height at the specified coordinates
	 * @param x The x coordinate
	 * @param z The z coordinate
	 * @return The terrain height at the specified coordinates
	 */
	public int getTerrainHeightAt(int x, int z)
	{
		int height = worldObj.getHeight(TCGenUtils.getBlockPos(x, 0, z)).getY() + 1;
		for(int y = height; y > 0; y--)
		{
			Block block = worldObj.getBlockState(TCGenUtils.getBlockPos(x, y, z)).getBlock();
			if(block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.SAND || block == Blocks.STONE)
			{
				return y + 1;
			}
		}
		return 0;
	}
	
	/**
	 * Gets a random angle in radians
	 * @return A random angle in radians
	 */
	public double randAngle() {
		return rand.nextDouble() * 3.1415926535897931D * 2D;
	}
	
	protected int getHeight(int x, int z) {
		return worldObj.getHeight(new BlockPos(x, 0, z)).getY();
	}
}