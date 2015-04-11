package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.block.BlockTropicraftLog;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTropicraftLargePalmTrees extends WorldGenerator {
	
	private Block wood =  TCBlockRegistry.logs;
	private Block tropicsLeaves = TCBlockRegistry.palmLeaves;
	
	protected Random rand;
	
	public WorldGenTropicraftLargePalmTrees()
	{
		super();
	}

	public WorldGenTropicraftLargePalmTrees(boolean flag)
	{
		super(flag);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int b = random.nextInt(2);
		byte height = (byte)(random.nextInt(4) + 7);
		boolean flag = true;
		if(j < 1 || j + height + 1 > 128)
		{
			return false;
		}
		for(int l = j; l <= j + 1 + height; l++)
		{
			byte byte1 = 1;
			if(l == j)
			{
				byte1 = 0;
			}
			if(l >= (j + 1 + height) - 2)
			{
				byte1 = 2;
			}
			for(int j1 = i - byte1; j1 <= i + byte1 && flag; j1++)
			{
				for(int k1 = k - byte1; k1 <= k + byte1 && flag; k1++)
				{
					if(l >= 0 && l < 128)
					{
						Block l1 = world.getBlock(j1, l, k1);
						if(l1 != Blocks.air && l1 != TCBlockRegistry.palmLeaves)
						{
							flag = false;
						}
					} else
					{
						flag = false;
					}
				}

			}

		}

		if(!flag)
		{
			return false;
		}
		Block i1 = world.getBlock(i, j - 1, k);
		if(i1 != Blocks.sand || j >= 128 - height - 1)
		{
			int ground = world.getHeightValue(i, k);
			i1 = world.getBlock(i, ground - 1, k);
			if(i1 != Blocks.sand || j >= 128 - height - 1)
			{
				return false;
			}
			j = ground;
		}
		
		for(int y = 0; y <= height; y++)
		{
			setBlockAndMetadata(world, i, (j + y), k, wood, 0);
		}
		
		// Wheeee, auto-generated code!
		setBlockAndMetadata(world, i + 0, j + height + 1, k + -7, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 1, k + -6, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 1, k + -6, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 1, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 1, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -6, j + height + 1, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 1, k + -1, wood, 0);
		setBlockAndMetadata(world, i + 6, j + height + 1, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -7, j + height + 1, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 1, k + 0, wood, 0);
		setBlockAndMetadata(world, i + 0, j + height + 1, k + 0, wood, 0);
		setBlockAndMetadata(world, i + 1, j + height + 1, k + 0, wood, 0);
		setBlockAndMetadata(world, i + 7, j + height + 1, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -6, j + height + 1, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 1, k + 1, wood, 0);
		setBlockAndMetadata(world, i + 6, j + height + 1, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 1, k + 5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 1, k + 5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 1, k + 6, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 1, k + 6, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 1, k + 7, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 2, k + -6, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 2, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 2, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 2, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 2, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 2, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 2, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 2, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 2, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 2, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 2, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 2, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 2, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 2, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -6, j + height + 2, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 2, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 6, j + height + 2, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 2, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 2, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 2, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 2, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 2, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 2, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 2, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 2, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 2, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 2, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 2, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 2, k + 5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 2, k + 5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 2, k + 5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 2, k + 5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 2, k + 6, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + -5, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 3, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 3, k + -4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 3, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 3, k + -3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 3, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 3, k + -2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 3, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 3, k + -1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -5, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 5, j + height + 3, k + 0, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -1, j + height + 3, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 3, k + 1, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -2, j + height + 3, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 3, k + 2, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -3, j + height + 3, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 3, k + 3, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + -4, j + height + 3, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height + 3, k + 4, tropicsLeaves, 0);
		setBlockAndMetadata(world, i + 0, j + height + 3, k + 5, tropicsLeaves, 0);
		
		for(int y = 0; y <= height; y++) {
			// TODO: Move this to a utils class?
			BlockTropicraftLog.spawnCoconuts(world, i, (j + y), k, random, 2);
		}
		return true;
	}

	/**
	 * Sets the block in the world, notifying neighbors if enabled.
	 */
	public void setBlockAndMetadata(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		par1World.setBlock(par2, par3, par4, par5, par6, 3);
	}
}