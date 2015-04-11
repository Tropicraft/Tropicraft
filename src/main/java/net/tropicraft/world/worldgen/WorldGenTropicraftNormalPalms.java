package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.block.BlockTropicraftLog;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTropicraftNormalPalms extends WorldGenerator
{
	boolean notify;

	private Block wood = TCBlockRegistry.logs;
	private Block palmLeaves = TCBlockRegistry.palmLeaves;
	
	public WorldGenTropicraftNormalPalms()
	{
		super();
		notify = false;
	}

	public WorldGenTropicraftNormalPalms(boolean flag)
	{
		super(flag);
		notify = flag;
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int b = random.nextInt(2);
		byte height = (byte)(random.nextInt(4) + 6);
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
			for(int k1 = i - byte1; k1 <= i + byte1 && flag; k1++)
			{
				for(int i2 = k - byte1; i2 <= k + byte1 && flag; i2++)
				{
					if(l >= 0 && l < 128)
					{
						Block j2 = world.getBlock(k1, l, i2);
						if(!world.isAirBlock(k1, l, i2) && j2 != palmLeaves)
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
		
		setBlockAndMetadata(world, i, j - 1, k, Blocks.sand, 0);
		setBlockAndMetadata(world, i, j + height + 2, k, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height + 1, k + 1, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height + 1, k + 2, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height + 1, k + 3, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height, k + 4, palmLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 1, k, palmLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 1, k, palmLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height + 1, k, palmLeaves, 0);
		setBlockAndMetadata(world, i + 4, j + height, k, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height + 1, k - 1, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height + 1, k - 2, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height + 1, k - 3, palmLeaves, 0);
		setBlockAndMetadata(world, i, j + height, k - 4, palmLeaves, 0);
		setBlockAndMetadata(world, i - 1, j + height + 1, k, palmLeaves, 0);
		setBlockAndMetadata(world, i - 1, j + height + 1, k - 1, palmLeaves, 0);
		setBlockAndMetadata(world, i - 1, j + height + 1, k + 1, palmLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 1, k - 1, palmLeaves, 0);
		setBlockAndMetadata(world, i + 1, j + height + 1, k + 1, palmLeaves, 0);
		setBlockAndMetadata(world, i - 2, j + height + 1, k, palmLeaves, 0);
		setBlockAndMetadata(world, i - 3, j + height + 1, k, palmLeaves, 0);
		setBlockAndMetadata(world, i - 4, j + height, k, palmLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 1, k + 2, palmLeaves, 0);
		setBlockAndMetadata(world, i + 2, j + height + 1, k - 2, palmLeaves, 0);
		setBlockAndMetadata(world, i - 2, j + height + 1, k + 2, palmLeaves, 0);
		setBlockAndMetadata(world, i - 2, j + height + 1, k - 2, palmLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height, k + 3, palmLeaves, 0);
		setBlockAndMetadata(world, i + 3, j + height, k - 3, palmLeaves, 0);
		setBlockAndMetadata(world, i - 3, j + height, k + 3, palmLeaves, 0);
		setBlockAndMetadata(world, i - 3, j + height, k - 3, palmLeaves, 0);
		
		for(int j1 = 0; j1 < height + 4; j1++)
		{
			Block l1 = world.getBlock(i, j + j1, k);
			if(!world.isAirBlock(i, j + j1, k) && l1 != palmLeaves)
			{
				continue;
			}
			setBlockAndMetadata(world, i, (j + j1) - 2, k, wood, 0);
			BlockTropicraftLog.spawnCoconuts(world, i, (j + j1) - 2, k, random, 2);
			if(j1 <= height - 1 || j1 >= height + 2)
			{
				continue;
			}
		}

		return true;
	}

	/**
	 * Sets the block in the world, notifying neighbors if enabled.
	 */
	protected void setBlockAndMetadata(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		par1World.setBlock(par2, par3, par4, par5, par6, 3);
	}
}