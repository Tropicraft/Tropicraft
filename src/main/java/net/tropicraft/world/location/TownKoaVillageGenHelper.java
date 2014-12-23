package net.tropicraft.world.location;

import java.util.Iterator;

import build.world.BuildDirectionHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.world.WorldProviderTropicraft;
import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import CoroUtil.world.location.ManagedLocation;

public class TownKoaVillageGenHelper {

	//relative from the scehamtic center, the entry stairs are in the -x, shaman hut in the +x, so 'left' (at stairs looking to center) is -z, and 'right' is +z
	//buildmod rotations do:
	//0: +x (assumed)
	//1: -z
	//2: -x
	//3: +z
	
	//from schematic
	public static int areaLength = 76; //Z for rot 0
	public static int areaWidth = 86; //X for rot 0
	public static int areaHeight = 16;
	
	
	
	/* Takes coords that are assumed to be a beach, scans to find the ocean side and checks if theres enough ocean space to gen */
	public static boolean hookTryGenVillage(ChunkCoordinates parCoords, World parWorld) {
		/*int x = MathHelper.floor_double(player.posX);
		int z = MathHelper.floor_double(player.posZ);
		int y = player.worldObj.getHeightValue(x, z);*/
		
		int directionTry = getBestGenDirection(parCoords, parWorld);
		
		if (directionTry != -1) {
			
			System.out.println("test success! dir: " + directionTry);
			
			ChunkCoordinates centerCoords = getCoordsFromAdjustedDirection(parCoords, directionTry);
			
			System.out.println("centerCoords: " + centerCoords);
			
			TownKoaVillage village = new TownKoaVillage();
			
			WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(parWorld);
			
			int minDistBetweenVillages = 128;
			
			Iterator it = wd.lookupTickingManagedLocations.values().iterator();
			while (it.hasNext()) {
				ManagedLocation town = (ManagedLocation) it.next();
				
				if (Math.sqrt(town.spawn.getDistanceSquaredToChunkCoordinates(parCoords)) < minDistBetweenVillages) {
					return false;
				}
			}
			//questionable ID setting
			int newID = wd.lookupTickingManagedLocations.size();
			village.initData(newID, parWorld.provider.dimensionId, centerCoords);
			village.direction = directionTry;
			village.initFirstTime();
			wd.addTickingLocation(village);
			
			return true;
		} else {
			System.out.println("test fail!");
		}
		
		return false;
	}
	
	/* 
	 * 
	 * tests from the beach position, looks for the most watery direction if any
	 * return value: -1 is fail, 0-3 is direction
	 * 
	 */
	public static int getBestGenDirection(ChunkCoordinates parCoords, World parWorld) {
		
		//doing very cheap water check
		//- check per direction
		//-- middle
		//-- end
		//scanning a square shaped area so width by width
		
		//this should possibly get the best angle instead of any working angle, so a circular beach uses the best opposing angle instead of lining up parallel to beach
		//- check corners! that sould solve that
		//- try front left and front right first
		//- trying front left in 4 pieces, same for right
		
		if (isClear(parCoords, parWorld, 1, 0)) return 0;
		if (isClear(parCoords, parWorld, 0, -1)) return 1;
		if (isClear(parCoords, parWorld, -1, 0)) return 2;
		if (isClear(parCoords, parWorld, 0, 1)) return 3;
		
		
		return -1;
	}
	
	public static boolean isClear(ChunkCoordinates parCoords, World parWorld, int scanX, int scanZ) {
		int sizeHorizMax = areaWidth;
		int sizeMiddle = areaWidth/2;
		
		int topYBeach = WorldProviderTropicraft.MID_HEIGHT - 1;//parWorld.getHeightValue(parCoords.posX, parCoords.posZ) - 1;
		
		//if (topYBeach < WorldProviderTropicraft.MID_HEIGHT) return false;//topYBeach = WorldProviderTropicraft.MID_HEIGHT+1;
		
		Block blockScanBeach = parWorld.getBlock(parCoords.posX, topYBeach, parCoords.posZ);
		
		
		if (blockScanBeach.getMaterial() == Material.sand) {
			//tropiwater isnt counted in getHeightValue apparently so lets hardcode the observed water height val
			int topYMiddle = WorldProviderTropicraft.MID_HEIGHT-1;//parWorld.getHeightValue(parCoords.posX + (sizeMiddle * scanX), parCoords.posZ + (sizeMiddle * scanZ)) - 1;
			Block blockScanMiddle = parWorld.getBlock(parCoords.posX + (sizeMiddle * scanX), topYMiddle, parCoords.posZ + (sizeMiddle * scanZ));
			
			System.out.println("testing scanX: " + scanX + " scanZ: " + scanZ);
			
			//if middle of area is water, lets us also know we have our water level to check against for other areas
			if (blockScanMiddle.getMaterial() == Material.water) {
				Block blockScanEnd = parWorld.getBlock(parCoords.posX + (sizeHorizMax * scanX), topYMiddle, parCoords.posZ + (sizeHorizMax * scanZ));
				System.out.println("testing blockScanEnd x: " + (parCoords.posX + (sizeHorizMax * scanX)) + " z: " + (parCoords.posZ + (sizeHorizMax * scanZ)));
				
				if (blockScanEnd.getMaterial() == Material.water) {
					//purposely inverting scanX and scanZ here to make it check the perpendicular
					for (int i = 1; i <= 4; i++) {
						
						int sizeStep = sizeHorizMax / 4 * i;
						
						Block blockScanFrontLeft = parWorld.getBlock(parCoords.posX + (sizeStep * scanZ), topYMiddle, parCoords.posZ + (sizeStep * scanX));
						Block blockScanFrontRight = parWorld.getBlock(parCoords.posX + (sizeStep * scanZ*-1), topYMiddle, parCoords.posZ + (sizeStep * scanX*-1));
						
						System.out.println("testing blockScanFrontLeft x: " + (parCoords.posX + (sizeStep * scanZ)) + " z: " + (parCoords.posZ + (sizeStep * scanX)));
						System.out.println("testing blockScanFrontRight x: " + (parCoords.posX + (sizeStep * scanZ*-1)) + " z: " + (parCoords.posZ + (sizeStep * scanX*-1)));
						
						if (blockScanFrontLeft.getMaterial() != Material.water || blockScanFrontRight.getMaterial() != Material.water) return false;
					}
					
					return true;
				}
				
			}
		}
		
		//int topYEnd = parWorld.getHeightValue(parCoords.posX + (sizeHorizMax * scanX), parCoords.posZ + (sizeHorizMax * scanZ)) - 1;
		
		
		
		
		//if (blockScanMiddle.getMaterial() == Material.water && blockScanEnd.getMaterial() == Material.water) return true;
		
		return false;
	}
	
	public static ChunkCoordinates getCoordsFromAdjustedDirection(ChunkCoordinates parCoords, int parDirection) {
		return new ChunkCoordinates(parCoords.posX + (areaWidth/2 * BuildDirectionHelper.getDirectionToCoords(parDirection).posX), parCoords.posY, parCoords.posZ + (areaWidth/2 * BuildDirectionHelper.getDirectionToCoords(parDirection).posZ));
	}
	
}
