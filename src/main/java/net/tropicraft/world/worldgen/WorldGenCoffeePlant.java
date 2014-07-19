package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenCoffeePlant extends TCGenBase {
	
	private static final ForgeDirection[] cardinalDirections = {
		ForgeDirection.NORTH, ForgeDirection.EAST,
		ForgeDirection.SOUTH, ForgeDirection.WEST
	};
	
	public WorldGenCoffeePlant(World world, Random rand) {
		super(world, rand);
	}
	
	@Override
	public boolean generate(int x, int y, int z) {
		int nx = (x + rand.nextInt(8)) - rand.nextInt(8);
		int nz = (z + rand.nextInt(8)) - rand.nextInt(8);
		
		int ny = y;
		
		if (!worldObj.isAirBlock(nx, ny, nz) || worldObj.getBlock(nx, ny - 1, nz) != Blocks.grass) {
			return false;
		}
		
		ForgeDirection viableDirection = ForgeDirection.UNKNOWN;
		
		// Scan for existing water
		for (ForgeDirection dir: cardinalDirections) {
			int neighborx = nx + dir.offsetX;
			int neighborz = nz + dir.offsetZ;
			
			if (worldObj.getBlock(neighborx, ny - 1, neighborz).getMaterial() == Material.water) {
				viableDirection = dir;
				break;
			}
			
		}

		if (viableDirection == ForgeDirection.UNKNOWN) {
			// Scan for places to put a water source block
			for (ForgeDirection dir: cardinalDirections) {
				int neighborx = nx + dir.offsetX;
				int neighborz = nz + dir.offsetZ;

				// isAirBlock call for ny - 2 is to prevent a waterfall from spawning
				if (!worldObj.isAirBlock(neighborx, ny, neighborz)
						|| worldObj.getBlock(neighborx, ny - 1, neighborz) != Blocks.grass
						|| worldObj.isAirBlock(neighborx, ny - 2, neighborz)) {
					continue;
				}
				
				// Check if the water block we'd place would be enclosed by grass (Don't want accidental waterfalls)
				boolean surrounded = true;
				
				for (ForgeDirection surroundingDir: cardinalDirections) {
					int surroundingx = neighborx + surroundingDir.offsetX;
					int surroundingz = neighborz + surroundingDir.offsetZ;
					
					if (!worldObj.isAirBlock(surroundingx, ny, surroundingz)
						|| worldObj.getBlock(surroundingx, ny - 1, surroundingz) != Blocks.grass) {
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
		
		if (viableDirection == ForgeDirection.UNKNOWN) {
			return false;
		}
		
		worldObj.setBlock(nx + viableDirection.offsetX, ny - 1, nz + viableDirection.offsetZ, Blocks.water, 0, 3);
		worldObj.setBlock(nx, ny - 1, nz, Blocks.farmland, 7, 3);
		
		for (int i = 0; i < 3; ++i) {
			if (worldObj.isAirBlock(nx, ny + i, nz)) {
				worldObj.setBlock(nx, ny + i, nz, TCBlockRegistry.coffeePlant, 6, 3);
			} else {
				break;
			}
		}
		
		return true;
	}

}
