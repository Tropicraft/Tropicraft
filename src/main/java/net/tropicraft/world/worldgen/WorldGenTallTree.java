package net.tropicraft.world.worldgen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

import java.util.Arrays;
import java.util.Random;

public class WorldGenTallTree extends TCGenBase {

	private boolean gen;
	private Block woodBlock = TCBlockRegistry.logs;
	private int woodMetadata = 1;
	private Block leafBlock = TCBlockRegistry.rainforestLeaves;
	
	public WorldGenTallTree(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return generate(i,j,k);
	}
	
	@Override
	public boolean generate(int i, int j, int k) {		
		Block blockUnder = worldObj.getBlock(i, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass)
		{
			return false;
		}
		blockUnder = worldObj.getBlock(i + 1, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass)
		{
			return false;
		}
		blockUnder = worldObj.getBlock(i - 1, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass)
		{
			return false;
		}
		blockUnder = worldObj.getBlock(i, j - 1, k + 1);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass)
		{
			return false;
		}
		blockUnder = worldObj.getBlock(i, j - 1, k - 1);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass)
		{
			return false;
		}
		
		int height = rand.nextInt(15) + 15;
		
		for(int y = j; y < j + height + 6; y++)
		{
			for(int x = i - 1; x <= i + 1; x++)
			{
				for(int z = k - 1; z <= k + 1; z++)
				{
					Block block = worldObj.getBlock(x, y, z);
					if(block != Blocks.air && block != Blocks.tallgrass && block != leafBlock)
					{
						return false;
					}
				}
			}
		}
		
		for(int y = j; y < j + height; y++)
		{
			worldObj.setBlock(i, y, k, woodBlock, woodMetadata, 3);
			worldObj.setBlock(i - 1, y, k, woodBlock, woodMetadata, 3);
			worldObj.setBlock(i + 1, y, k, woodBlock, woodMetadata, 3);
			worldObj.setBlock(i, y, k - 1, woodBlock, woodMetadata, 3);
			worldObj.setBlock(i, y, k + 1, woodBlock, woodMetadata, 3);
			if(y - j > height / 2 && rand.nextInt(3) == 0)
			{
				int ox = rand.nextInt(3) - 1 + i;
				int oz = rand.nextInt(3) - 1 + k;
				genCircle(ox, y + 1, oz, 1, 0, leafBlock, 3, false);
				genCircle(ox, y, oz, 2, 1, leafBlock, 3, false);
				checkAndPlaceBlockLine(new int[] {i, y, k}, new int[] {ox, y, oz}, woodBlock, 1, Arrays.asList(0, leafBlock, Blocks.vine));
				for(int x = i - 3; x <= i + 3; x++)
				{
					for(int z = k - 3; z <= k + 3; z++)
					{
						for(int y1 = y - 1; y1 <= y + 1; y1++)
						{
							if(rand.nextInt(5) == 0)
								genVines(x, y1, z);
						}
					}
				}
			}
			if(y - j > height - (height / 4) && y - j < height - 3 && rand.nextInt(3) == 0)
			{
				int ox = i + rand.nextInt(9) - 4;
				int oz = k + rand.nextInt(9) - 4;
				
				int leafSize = rand.nextInt(3) + 5;
				
				genCircle(ox, y + 3, oz, leafSize - 2, 0, leafBlock, 3, false);
				genCircle(ox, y + 2, oz, leafSize - 1, leafSize - 3, leafBlock, 3, false);
				genCircle(ox, y + 1, oz, leafSize, leafSize - 1, leafBlock, 3, false);
				
				int bn = rand.nextInt(4) + 4;
				
				for(int b = 0; b < bn; b++)
				{
					double randAngle = randAngle();
					int branchLength = rand.nextInt(leafSize - 4) + 3;
					int ax = (int) Math.round((branchLength * Math.sin(randAngle)) + ox);
					int az = (int) Math.round((branchLength * Math.cos(randAngle)) + oz); 
					checkAndPlaceBlockLine(new int[] {i, y, k}, new int[] {ax, y, az}, woodBlock, 1, Arrays.asList(0, leafBlock, Blocks.vine));
				}
				for(int x = i - leafSize; x <= i + leafSize; x++)
				{
					for(int z = k - leafSize; z <= k + leafSize; z++)
					{
						for(int y1 = y; y1 <= y + 3; y1++)
						{
							if(rand.nextInt(4) == 0)
								genVines(x, y1, z);
						}
					}
				}
			}
		}
		
		int leafSize = rand.nextInt(5) + 9;
		
		genCircle(i, j + height /*+ 6*/, k, leafSize - 2, 0, leafBlock, 3, false);
		genCircle(i, j + height - 1/*5*/, k, leafSize - 1, leafSize - 4, leafBlock, 3, false);
		genCircle(i, j + height - 2/*4*/, k, leafSize, leafSize - 1, leafBlock, 3, false);
		
		int bn = rand.nextInt(4) + 6;
		
		for(int b = 0; b < bn; b++)
		{
			double randAngle = randAngle();
			int branchLength = rand.nextInt(leafSize - 6) + 3;
			int ax = (int) Math.round((branchLength * Math.sin(randAngle)) + i);
			int az = (int) Math.round((branchLength * Math.cos(randAngle)) + k); 
			checkAndPlaceBlockLine(new int[] {i, j + height - 1, k}, new int[] {ax, j + height + 5, az}, woodBlock, 1, Arrays.asList(0, leafBlock, Blocks.vine));
		}
		for(int x = i - leafSize; x <= i + leafSize; x++)
		{
			for(int z = k - leafSize; z <= k + leafSize; z++)
			{
				for(int y1 = j + height + 3; y1 <= j + height + 6; y1++)
				{
					if(rand.nextInt(4) == 0)
						genVines(x, y1, z);
				}
			}
		}
		
		return true;
	}
	
	private boolean genVines(int i, int j, int k)
	{
		int m = 2;

        do
        {
            if (m > 5)
            {
            	return false;
            }

            if (Blocks.vine.canPlaceBlockOnSide(worldObj, i, j, k, m) && worldObj.getBlock(i, j, k) == Blocks.air)
            {
                worldObj.setBlock(i, j, k, Blocks.vine, 1 << Direction.facingToDirection[Facing.oppositeSide[m]], 3);
                break;
            }

            m++;
        }
        while (true);
        
        int length = rand.nextInt(4) + 4;
        
        for(int y = j - 1; y > j - length; y--)
        {
        	if(worldObj.getBlock(i, y, k) == Blocks.air)
        	{
        		worldObj.setBlock(i, y, k, Blocks.vine, 1 << Direction.facingToDirection[Facing.oppositeSide[m]], 3);        	
        	}
        	else
        	{
        		return true;
        	}
        }
        
		return true;
	}
	
}