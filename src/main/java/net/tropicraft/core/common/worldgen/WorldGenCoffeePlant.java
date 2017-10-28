package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockCoffeeBush;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenCoffeePlant extends TCGenBase {
	
	private static final EnumFacing[] cardinalDirections = {
	        EnumFacing.NORTH, EnumFacing.EAST,
	        EnumFacing.SOUTH, EnumFacing.WEST
	};
	
	public WorldGenCoffeePlant(World world, Random rand) {
		super(world, rand);
	}
	
	@Override
	public boolean generate(BlockPos pos) {
	    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		int nx = (x + rand.nextInt(8)) - rand.nextInt(8);
		int nz = (z + rand.nextInt(8)) - rand.nextInt(8);
		
		int ny = y;
		
		if (!TCGenUtils.isAirBlock(worldObj, nx, ny, nz) || TCGenUtils.getBlock(worldObj, nx, ny - 1, nz) != Blocks.GRASS) {
			return false;
		}
		
		EnumFacing viableDirection = null;
		
		// Scan for existing water
		for (EnumFacing dir: cardinalDirections) {
			int neighborx = nx + dir.getFrontOffsetX();
			int neighborz = nz + dir.getFrontOffsetZ();
			
			if (TCGenUtils.getMaterial(worldObj, neighborx, ny - 1, neighborz) == Material.WATER) {
				viableDirection = dir;
				break;
			}
			
		}

		if (viableDirection == null) {
			// Scan for places to put a water source block
			for (EnumFacing dir: cardinalDirections) {
				int neighborx = nx + dir.getFrontOffsetX();
				int neighborz = nz + dir.getFrontOffsetZ();

				// isAirBlock call for ny - 2 is to prevent a waterfall from spawning
				if (!TCGenUtils.isAirBlock(worldObj, neighborx, ny, neighborz)
						|| TCGenUtils.getBlock(worldObj, neighborx, ny - 1, neighborz) != Blocks.GRASS
						|| TCGenUtils.isAirBlock(worldObj, neighborx, ny - 2, neighborz)) {
					continue;
				}
				
				// Check if the water block we'd place would be enclosed by grass (Don't want accidental waterfalls)
				boolean surrounded = true;
				
				for (EnumFacing surroundingDir: cardinalDirections) {
					int surroundingx = neighborx + surroundingDir.getFrontOffsetX();
					int surroundingz = neighborz + surroundingDir.getFrontOffsetZ();
					
					if (!TCGenUtils.isAirBlock(worldObj, surroundingx, ny, surroundingz)
						|| TCGenUtils.getBlock(worldObj, surroundingx, ny - 1, surroundingz) != Blocks.GRASS) {
						surrounded = false;
						break;
					}
				}
				
				if (surrounded) {
					viableDirection = dir;
					break;
				}
			}
		}
		
		if (viableDirection == null) {
			return false;
		}
		
		TCGenUtils.setBlockState(worldObj, nx + viableDirection.getFrontOffsetX(), ny - 1, nz + viableDirection.getFrontOffsetZ(), Blocks.WATER.getDefaultState(), blockGenNotifyFlag);
		TCGenUtils.setBlockState(worldObj, nx, ny - 1, nz, Blocks.FARMLAND.getDefaultState(), blockGenNotifyFlag);
		
		for (int i = 0; i < 3; ++i) {
			if (TCGenUtils.isAirBlock(worldObj, nx, ny + i, nz)) {
				TCGenUtils.setBlockState(worldObj, nx, ny + i, nz, BlockRegistry.coffeePlant.getDefaultState().withProperty(BlockCoffeeBush.AGE, Integer.valueOf(6)), blockGenNotifyFlag);
			} else {
				break;
			}
		}
		
		return true;
	}

}
