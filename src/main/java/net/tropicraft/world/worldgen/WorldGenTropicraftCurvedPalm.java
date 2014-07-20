package net.tropicraft.world.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.registry.TCBlockRegistry;

import java.util.Random;

public class WorldGenTropicraftCurvedPalm extends WorldGenerator
{
	boolean notify;
	private static final Block woodID = TCBlockRegistry.logs;
	private static final Block leafID = TCBlockRegistry.palmLeaves;

	public WorldGenTropicraftCurvedPalm()
	{
		super();
		notify = false;
	}

	public WorldGenTropicraftCurvedPalm(boolean flag)
	{
		super(flag);
		notify = flag;
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		byte byte0 = (byte)(random.nextInt(1) + 4);
		boolean flag = true;
		if(j < 1 || j + byte0 + 1 > 128)
		{
			return false;
		}
		for(int l = j; l <= j + 1 + byte0; l++)
		{
			byte byte1 = 1;
			if(l == j)
			{
				byte1 = 0;
			}
			if(l >= (j + 1 + byte0) - 2)
			{
				byte1 = 2;
			}
			for(int k1 = i - byte1; k1 <= i + byte1 && flag; k1++)
			{
				for(int i2 = k - byte1; i2 <= k + byte1 && flag; i2++)
				{
					if(l >= 0 && l < 128)
					{
						Block k2 = world.getBlock(k1, l, i2);
						if(k2 != Blocks.air && k2 != leafID)
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
		if(i1 != Blocks.sand || j >= 256 - byte0 - 1)
		{
			int ground = world.getHeightValue(i, k);
			i1 = world.getBlock(i, ground - 1, k);
			if(i1 != Blocks.sand)
			{
				return false;
			}
			j = ground;
		}

		int j1 = pickDirection(world, random, i,  j,  k);
		if(j1 == 1)
		{
			world.setBlock(i + 0, j + 0, k + 0, woodID, 0, 3);
			world.setBlock(i + 1, j + 0, k + 0, woodID, 0, 3);
			world.setBlock(i + 2, j + 1, k + 0, woodID, 0, 3);
			world.setBlock(i + 1, j + 1, k + 0, woodID, 0, 3);
			world.setBlock(i + 2, j + 2, k + 0, woodID, 0, 3);
			world.setBlock(i + 2, j + 3, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 3, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 4, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 5, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 6, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 7, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 8, k + 0, woodID, 0, 3);
			world.setBlock(i + 3, j + 9, k + 0, woodID, 0, 3);
			world.setBlock(i + 4, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + 5, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + 6, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + 7, j + 8, k + 0, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + -1, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + -2, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + -3, leafID, 0, 3);
			world.setBlock(i + 3, j + 8, k + -4, leafID, 0, 3);
			world.setBlock(i + 2, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + 1, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + -1, j + 8, k + 0, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + 1, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + 2, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i + 3, j + 8, k + 4, leafID, 0, 3);
			world.setBlock(i + 4, j + 8, k + -1, leafID, 0, 3);
			world.setBlock(i + 5, j + 7, k + -1, leafID, 0, 3);
			world.setBlock(i + 4, j + 7, k + -2, leafID, 0, 3);
			world.setBlock(i + 5, j + 6, k + -2, leafID, 0, 3);
			world.setBlock(i + 4, j + 8, k + 1, leafID, 0, 3);
			world.setBlock(i + 5, j + 7, k + 1, leafID, 0, 3);
			world.setBlock(i + 4, j + 7, k + 2, leafID, 0, 3);
			world.setBlock(i + 5, j + 6, k + 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 8, k + 1, leafID, 0, 3);
			world.setBlock(i + 2, j + 7, k + 2, leafID, 0, 3);
			world.setBlock(i + 1, j + 7, k + 1, leafID, 0, 3);
			world.setBlock(i + 1, j + 6, k + 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 8, k + -1, leafID, 0, 3);
			world.setBlock(i + 2, j + 7, k + -2, leafID, 0, 3);
			world.setBlock(i + 1, j + 7, k + -1, leafID, 0, 3);
			world.setBlock(i + 1, j + 6, k + -2, leafID, 0, 3);
			world.setBlock(i + 2, j + 10, k + -1, leafID, 0, 3);
			world.setBlock(i + 1, j + 11, k + -1, leafID, 0, 3);
			world.setBlock(i + 2, j + 11, k + -2, leafID, 0, 3);
			world.setBlock(i + 1, j + 12, k + -2, leafID, 0, 3);
			world.setBlock(i + 4, j + 10, k + -1, leafID, 0, 3);
			world.setBlock(i + 4, j + 11, k + -2, leafID, 0, 3);
			world.setBlock(i + 5, j + 11, k + -1, leafID, 0, 3);
			world.setBlock(i + 5, j + 12, k + -2, leafID, 0, 3);
			world.setBlock(i + 3, j + 10, k + 0, leafID, 0, 3);
			world.setBlock(i + 4, j + 10, k + 1, leafID, 0, 3);
			world.setBlock(i + 5, j + 11, k + 1, leafID, 0, 3);
			world.setBlock(i + 5, j + 12, k + 2, leafID, 0, 3);
			world.setBlock(i + 4, j + 11, k + 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 10, k + 1, leafID, 0, 3);
			world.setBlock(i + 2, j + 11, k + 2, leafID, 0, 3);
			world.setBlock(i + 1, j + 11, k + 1, leafID, 0, 3);
			world.setBlock(i + 1, j + 12, k + 2, leafID, 0, 3);
			world.setBlock(i + 3, j + 11, k + 0, leafID, 0, 3);
			world.setBlock(i + 3, j + 12, k + 0, leafID, 0, 3);
			world.setBlock(i + 4, j + 13, k + 0, leafID, 0, 3);
			return true;
		} 
		if(j1 == 2)
		{
			world.setBlock(i + 0, j + 0, k + 0, woodID, 0, 3);
			world.setBlock(i - 1, j + 0, k + 0, woodID, 0, 3);
			world.setBlock(i - 2, j + 1, k + 0, woodID, 0, 3);
			world.setBlock(i - 1, j + 1, k + 0, woodID, 0, 3);
			world.setBlock(i - 2, j + 2, k + 0, woodID, 0, 3);
			world.setBlock(i - 2, j + 3, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 3, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 4, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 5, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 6, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 7, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 8, k + 0, woodID, 0, 3);
			world.setBlock(i - 3, j + 9, k + 0, woodID, 0, 3);
			world.setBlock(i - 4, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i - 5, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i - 6, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i - 7, j + 8, k + 0, leafID, 0, 3);
			world.setBlock(i - 3, j + 9, k + -1, leafID, 0, 3);
			world.setBlock(i - 3, j + 9, k + -2, leafID, 0, 3);
			world.setBlock(i - 3, j + 9, k + -3, leafID, 0, 3);
			world.setBlock(i - 3, j + 8, k + -4, leafID, 0, 3);
			world.setBlock(i - 2, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i - 1, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i - 0, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i - -1, j + 8, k + 0, leafID, 0, 3);
			world.setBlock(i - 3, j + 9, k + 1, leafID, 0, 3);
			world.setBlock(i - 3, j + 9, k + 2, leafID, 0, 3);
			world.setBlock(i - 3, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i - 3, j + 8, k + 4, leafID, 0, 3);
			world.setBlock(i - 4, j + 8, k + -1, leafID, 0, 3);
			world.setBlock(i - 5, j + 7, k + -1, leafID, 0, 3);
			world.setBlock(i - 4, j + 7, k + -2, leafID, 0, 3);
			world.setBlock(i - 5, j + 6, k + -2, leafID, 0, 3);
			world.setBlock(i - 4, j + 8, k + 1, leafID, 0, 3);
			world.setBlock(i - 5, j + 7, k + 1, leafID, 0, 3);
			world.setBlock(i - 4, j + 7, k + 2, leafID, 0, 3);
			world.setBlock(i - 5, j + 6, k + 2, leafID, 0, 3);
			world.setBlock(i - 2, j + 8, k + 1, leafID, 0, 3);
			world.setBlock(i - 2, j + 7, k + 2, leafID, 0, 3);
			world.setBlock(i - 1, j + 7, k + 1, leafID, 0, 3);
			world.setBlock(i - 1, j + 6, k + 2, leafID, 0, 3);
			world.setBlock(i - 2, j + 8, k + -1, leafID, 0, 3);
			world.setBlock(i - 2, j + 7, k + -2, leafID, 0, 3);
			world.setBlock(i - 1, j + 7, k + -1, leafID, 0, 3);
			world.setBlock(i - 1, j + 6, k + -2, leafID, 0, 3);
			world.setBlock(i - 2, j + 10, k + -1, leafID, 0, 3);
			world.setBlock(i - 1, j + 11, k + -1, leafID, 0, 3);
			world.setBlock(i - 2, j + 11, k + -2, leafID, 0, 3);
			world.setBlock(i - 1, j + 12, k + -2, leafID, 0, 3);
			world.setBlock(i - 4, j + 10, k + -1, leafID, 0, 3);
			world.setBlock(i - 4, j + 11, k + -2, leafID, 0, 3);
			world.setBlock(i - 5, j + 11, k + -1, leafID, 0, 3);
			world.setBlock(i - 5, j + 12, k + -2, leafID, 0, 3);
			world.setBlock(i - 3, j + 10, k + 0, leafID, 0, 3);
			world.setBlock(i - 4, j + 10, k + 1, leafID, 0, 3);
			world.setBlock(i - 5, j + 11, k + 1, leafID, 0, 3);
			world.setBlock(i - 5, j + 12, k + 2, leafID, 0, 3);
			world.setBlock(i - 4, j + 11, k + 2, leafID, 0, 3);
			world.setBlock(i - 2, j + 10, k + 1, leafID, 0, 3);
			world.setBlock(i - 2, j + 11, k + 2, leafID, 0, 3);
			world.setBlock(i - 1, j + 11, k + 1, leafID, 0, 3);
			world.setBlock(i - 1, j + 12, k + 2, leafID, 0, 3);
			world.setBlock(i - 3, j + 11, k + 0, leafID, 0, 3);
			world.setBlock(i - 3, j + 12, k + 0, leafID, 0, 3);
			world.setBlock(i - 4, j + 13, k + 0, leafID, 0, 3);
			return true;
		}
		if( j1 == 3){
			world.setBlock(i + 0, j + 0, k + 0, woodID, 0, 3);
			world.setBlock(i + 0, j + 0, k + 1, woodID, 0, 3);
			world.setBlock(i + 0, j + 1, k + 2, woodID, 0, 3);
			world.setBlock(i + 0, j + 1, k + 1, woodID, 0, 3);
			world.setBlock(i + 0, j + 2, k + 2, woodID, 0, 3);
			world.setBlock(i + 0, j + 3, k + 2, woodID, 0, 3);
			world.setBlock(i +  0, j + 3, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 4, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 5, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 6, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 7, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 8, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 9, k +  3, woodID, 0, 3);
			world.setBlock(i +  0, j + 9, k + 4, leafID, 0, 3);
			world.setBlock(i +  0, j + 9, k +  5, leafID, 0, 3);
			world.setBlock(i +  0, j + 9, k +  6, leafID, 0, 3);
			world.setBlock(i +  0, j + 8, k +  7, leafID, 0, 3);
			world.setBlock(i + -1, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i + -2, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i + -3, j + 9, k + 3 , leafID, 0, 3);
			world.setBlock(i + -4, j + 8, k + 3, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k + 2, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k + 1, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k + 0, leafID, 0, 3);
			world.setBlock(i + 0, j + 8, k + -1, leafID, 0, 3);
			world.setBlock(i + 1, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i + 2, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k + 3, leafID, 0, 3);
			world.setBlock(i + 4, j + 8, k + 3, leafID, 0, 3);
			world.setBlock(i + -1, j + 8, k + 4, leafID, 0, 3);
			world.setBlock(i + -1, j + 7, k + 5, leafID, 0, 3);
			world.setBlock(i + -2, j + 7, k + 4, leafID, 0, 3);
			world.setBlock(i + -2, j + 6, k + 5, leafID, 0, 3);
			world.setBlock(i + 1, j + 8, k + 4, leafID, 0, 3);
			world.setBlock(i + 1, j + 7, k + 5, leafID, 0, 3);
			world.setBlock(i + 2, j + 7, k + 4, leafID, 0, 3);
			world.setBlock(i + 2, j + 6, k + 5, leafID, 0, 3);
			world.setBlock(i + 1, j + 8, k + 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 7, k + 2, leafID, 0, 3);
			world.setBlock(i + 1, j + 7, k + 1, leafID, 0, 3);
			world.setBlock(i + 2, j + 6, k + 1, leafID, 0, 3);
			world.setBlock(i + -1, j + 8, k + 2, leafID, 0, 3);
			world.setBlock(i + -2, j + 7, k + 2, leafID, 0, 3);
			world.setBlock(i + -1, j + 7, k + 1, leafID, 0, 3);
			world.setBlock(i + -2, j + 6, k + 1, leafID, 0, 3);
			world.setBlock(i + -1, j + 10, k + 2, leafID, 0, 3);
			world.setBlock(i + -1, j + 11, k + 1, leafID, 0, 3);
			world.setBlock(i + -2, j + 11, k + 2, leafID, 0, 3);
			world.setBlock(i + -2, j + 12, k + 1, leafID, 0, 3);
			world.setBlock(i + -1, j + 10, k + 4, leafID, 0, 3);
			world.setBlock(i + -2, j + 11, k + 4, leafID, 0, 3);
			world.setBlock(i + -1, j + 11, k + 5, leafID, 0, 3);
			world.setBlock(i + -2, j + 12, k + 5, leafID, 0, 3);
			world.setBlock(i + 0, j + 10, k + 3, leafID, 0, 3);
			world.setBlock(i + 1, j + 10, k + 4, leafID, 0, 3);
			world.setBlock(i + 1, j + 11, k + 5, leafID, 0, 3);
			world.setBlock(i + 2, j + 12, k + 5, leafID, 0, 3);
			world.setBlock(i + 2, j + 11, k + 4, leafID, 0, 3);
			world.setBlock(i + 1, j + 10, k + 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 11, k + 2, leafID, 0, 3);
			world.setBlock(i + 1, j + 11, k + 1, leafID, 0, 3);
			world.setBlock(i + 2, j + 12, k + 1, leafID, 0, 3);
			world.setBlock(i + 0, j + 11, k + 3, leafID, 0, 3);
			world.setBlock(i + 0, j + 12, k + 3, leafID, 0, 3);
			world.setBlock(i + 0, j + 13, k + 4, leafID, 0, 3);
			return true;
		}
		if(j1 == 4){
			world.setBlock(i + 0, j + 0, k - 0, woodID, 0, 3);
			world.setBlock(i + 0, j + 0, k - 1, woodID, 0, 3);
			world.setBlock(i + 0, j + 1, k - 2, woodID, 0, 3);
			world.setBlock(i + 0, j + 1, k - 1, woodID, 0, 3);
			world.setBlock(i + 0, j + 2, k - 2, woodID, 0, 3);
			world.setBlock(i + 0, j + 3, k - 2, woodID, 0, 3);
			world.setBlock(i +  0, j + 3, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 4, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 5, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 6, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 7, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 8, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 9, k - 3, woodID, 0, 3);
			world.setBlock(i +  0, j + 9, k - 4, leafID, 0, 3);
			world.setBlock(i +  0, j + 9, k  - 5, leafID, 0, 3);
			world.setBlock(i +  0, j + 9, k  - 6, leafID, 0, 3);
			world.setBlock(i +  0, j + 8, k  - 7, leafID, 0, 3);
			world.setBlock(i + -1, j + 9, k - 3, leafID, 0, 3);
			world.setBlock(i + -2, j + 9, k - 3, leafID, 0, 3);
			world.setBlock(i + -3, j + 9, k - 3 , leafID, 0, 3);
			world.setBlock(i + -4, j + 8, k - 3, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k  - 2, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k  - 1, leafID, 0, 3);
			world.setBlock(i + 0, j + 9, k  - 0, leafID, 0, 3);
			world.setBlock(i + 0, j + 8, k  - -1, leafID, 0, 3);
			world.setBlock(i + 1, j + 9, k  - 3, leafID, 0, 3);
			world.setBlock(i + 2, j + 9, k  - 3, leafID, 0, 3);
			world.setBlock(i + 3, j + 9, k  - 3, leafID, 0, 3);
			world.setBlock(i + 4, j + 8, k  - 3, leafID, 0, 3);
			world.setBlock(i + -1, j + 8, k - 4, leafID, 0, 3);
			world.setBlock(i + -1, j + 7, k - 5, leafID, 0, 3);
			world.setBlock(i + -2, j + 7, k - 4, leafID, 0, 3);
			world.setBlock(i + -2, j + 6, k - 5, leafID, 0, 3);
			world.setBlock(i + 1, j + 8, k  - 4, leafID, 0, 3);
			world.setBlock(i + 1, j + 7, k  - 5, leafID, 0, 3);
			world.setBlock(i + 2, j + 7, k  - 4, leafID, 0, 3);
			world.setBlock(i + 2, j + 6, k  - 5, leafID, 0, 3);
			world.setBlock(i + 1, j + 8, k  - 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 7, k  - 2, leafID, 0, 3);
			world.setBlock(i + 1, j + 7, k  - 1, leafID, 0, 3);
			world.setBlock(i + 2, j + 6, k  - 1, leafID, 0, 3);
			world.setBlock(i + -1, j + 8, k - 2, leafID, 0, 3);
			world.setBlock(i + -2, j + 7, k - 2, leafID, 0, 3);
			world.setBlock(i + -1, j + 7, k - 1, leafID, 0, 3);
			world.setBlock(i + -2, j + 6, k - 1, leafID, 0, 3);
			world.setBlock(i + -1, j + 10, k - 2, leafID, 0, 3);
			world.setBlock(i + -1, j + 11, k - 1, leafID, 0, 3);
			world.setBlock(i + -2, j + 11, k - 2, leafID, 0, 3);
			world.setBlock(i + -2, j + 12, k - 1, leafID, 0, 3);
			world.setBlock(i + -1, j + 10, k - 4, leafID, 0, 3);
			world.setBlock(i + -2, j + 11, k - 4, leafID, 0, 3);
			world.setBlock(i + -1, j + 11, k - 5, leafID, 0, 3);
			world.setBlock(i + -2, j + 12, k - 5, leafID, 0, 3);
			world.setBlock(i + 0, j + 10, k  - 3, leafID, 0, 3);
			world.setBlock(i + 1, j + 10, k  - 4, leafID, 0, 3);
			world.setBlock(i + 1, j + 11, k  - 5, leafID, 0, 3);
			world.setBlock(i + 2, j + 12, k  - 5, leafID, 0, 3);
			world.setBlock(i + 2, j + 11, k  - 4, leafID, 0, 3);
			world.setBlock(i + 1, j + 10, k  - 2, leafID, 0, 3);
			world.setBlock(i + 2, j + 11, k  - 2, leafID, 0, 3);
			world.setBlock(i + 1, j + 11, k  - 1, leafID, 0, 3);
			world.setBlock(i + 2, j + 12, k  - 1, leafID, 0, 3);
			world.setBlock(i + 0, j + 11, k  - 3, leafID, 0, 3);
			world.setBlock(i + 0, j + 12, k  - 3, leafID, 0, 3);
			world.setBlock(i + 0, j + 13, k   - 4, leafID, 0, 3);
			return true;
		}
		else{
			return false;
		}

		//return true;
	}
	public int findWater(World world, Random random, int i, int j, int k){

		int iPos = 0;
		int iNeg = 0;
		int kPos = 0;
		int kNeg = 0;

		while(iPos < 10 &&  (world.getBlock(i + iPos, 62, k).getMaterial() != Material.water)){
			//System.out.println( world.getBlockId(i + iPos, 63, k));
			iPos++;

		}


		while(iNeg > -10 && (world.getBlock(i + iNeg, 62, k).getMaterial() != Material.water)){
			//System.out.println(world.getBlockId(i + iNeg, 63, k));
			iNeg--;

		}

		while(kPos < 10 &&  (world.getBlock(i, 62, k + kPos).getMaterial() != Material.water)){
			//System.out.println(world.getBlockId(i, 63, k + kPos));
			kPos++;

		}


		while(kNeg > -10 &&  (world.getBlock(i, 62, k + kNeg).getMaterial() != Material.water)){
			//System.out.println(world.getBlockId(i, 63, k + kNeg));
			kNeg--;

		}
		//System.out.println(kNeg);
		//System.out.println(kPos);
		//System.out.println(iNeg);
		//System.out.println(iPos);



		if(iPos < Math.abs(iNeg) && iPos < kPos && iPos < Math.abs(kNeg) ){
			return 1;  	    		
		}
		if(Math.abs(iNeg) < iPos && Math.abs(iNeg) < kPos && Math.abs(iNeg) < Math.abs(kNeg) ){
			return 2;
		}
		if(kPos < Math.abs(iNeg) && kPos < iPos && kPos < Math.abs(kNeg)  ){
			return 3;
		}
		if(Math.abs(kNeg) < Math.abs(iNeg) && Math.abs(kNeg) < iPos && Math.abs(kNeg) < kPos){
			return 4;
		}

		if(iPos < 10 && iPos == Math.abs(iNeg)){
			return random.nextInt(2)+1;
		}
		if(iPos < 10 && iPos == kPos){
			if(random.nextInt(2) + 1 == 1){
				return 1;

			}
			else{
				return 3;
			}
		}
		if(iPos < 10 && iPos == Math.abs(kNeg)){
			if(random.nextInt(2) + 1 == 1){
				return 1;

			}
			else{
				return 4;
			}
		}
		if(kPos < 10 && Math.abs(iNeg) == kPos){
			if(random.nextInt(2) + 1 == 1){
				return 2;

			}
			else{
				return 3;
			}
		}
		if(Math.abs(iNeg) < 10 && Math.abs(iNeg) == Math.abs(kNeg)){
			if(random.nextInt(2) + 1 == 1){
				return 2;

			}
			else{
				return 4;
			}
		}
		if(kPos < 10 && kPos == Math.abs(kNeg)){
			if(random.nextInt(2) + 1 == 1){
				return 3;

			}
			else{
				return 4;
			}
		}
		else{
			return 0;
		}
	}
	
	public int pickDirection(World world, Random random, int i, int j, int k){
		int direction = random.nextInt(4) + 1;
		if(findWater(world,random, i, j, k) != 0){
			return findWater(world, random, i, j, k);
		}
		else{
			return direction;
		}
	}
	
}
