package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenCurvedPalms extends TCDirectionalGen {
	
	private static final int TOP_OFFSET = 3;
	
	private static final IBlockState woodState = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.PALM);
	private static final IBlockState leafState = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.PALM);

	public WorldGenCurvedPalms(World world, Random rand) {
		super(world, rand);
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();

		if (getBlockState(pos.down()).getMaterial() != Material.SAND) {
			return false;
		}
		
		int height = 9 + rand.nextInt(3);
		int dir = this.pickDirection(i, j, k);
		this.setDir(dir);
		this.setOrigin(i, k);
		
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < height; y++) {
				if(this.getBlockWithDir(x, y + j, 0) != Blocks.AIR) {
					return false;
				}
			}
		}
		
		for(int x = 0; x < 9; x++) {
			for(int z = 0; z < 9; z++) {
				for(int y = height - 3; y < height + 4; y++) {
					if(this.getBlockWithDir(x + TOP_OFFSET, y + j, z) != Blocks.AIR) {
						return false;
					}
				}
			}
		}
		
		
		for(int x = 0, y = 0; y < height; y++) {
			this.placeBlockWithDir(x, y + j, 0, woodState);
			if(y == 0 || y == 1 || y == 3) {
				x++;
				this.placeBlockWithDir(x, y + j, 0, woodState);
			}
		}
		
		this.setOrigin(this.getActualXAt(TOP_OFFSET, 0), this.getActualZAt(TOP_OFFSET, 0));

		for(int y = 1; y < 5; y++) {
			if(y == 4) {
				this.placeBlockWithDir(1, y + j + height - 1, 0, leafState);
			} else {
				this.placeBlockWithDir(0, y + j + height - 1, 0, leafState);
			}
		}
		
		for(int curDir = 0; curDir < 4; curDir++) {
			this.setDir(curDir);
			
			int y = height - 1;
			
			this.placeBlockWithDir(1, y - 1 + j, 1, leafState);
			this.placeBlockWithDir(2, y - 2 + j, 1, leafState);
			this.placeBlockWithDir(1, y - 2 + j, 2, leafState);
			this.placeBlockWithDir(2, y - 3 + j, 2, leafState);
			this.placeBlockWithDir(1, y + 1 + j, 1, leafState);
			this.placeBlockWithDir(2, y + 2 + j, 1, leafState);
			this.placeBlockWithDir(1, y + 2 + j, 2, leafState);
			this.placeBlockWithDir(2, y + 3 + j, 2, leafState);
			
			for(int x = 1; x < 5; x++) {
				if(x == 4) {
					y--;
				}
				this.placeBlockWithDir(x, y + j, 0, leafState);
			}
		}
			
		return true;
	}
	
	public int findWater(int i, int j, int k){

		int iPos = 0;
		int iNeg = 0;
		int kPos = 0;
		int kNeg = 0;

		while(iPos < 10 &&  (getMaterial(i + iPos, 62, k) != Material.WATER)) {
			iPos++;
		}

		while(iNeg > -10 && (getMaterial(i + iNeg, 62, k) != Material.WATER)) {
			iNeg--;
		}
		
		while(kPos < 10 &&  (getMaterial(i, 62, k + kPos) != Material.WATER)) {
			kPos++;
		}

		while(kNeg > -10 &&  (getMaterial(i, 62, k + kNeg) != Material.WATER)) {
			kNeg--;
		}

		if(iPos < Math.abs(iNeg) && iPos < kPos && iPos < Math.abs(kNeg)) {
			return X_PLUS;  	 //1   		
		} else if(Math.abs(iNeg) < iPos && Math.abs(iNeg) < kPos && Math.abs(iNeg) < Math.abs(kNeg)) {
			return X_MINUS;    //2
		} else if(kPos < Math.abs(iNeg) && kPos < iPos && kPos < Math.abs(kNeg)) {
			return Z_PLUS;    //3
		} else if(Math.abs(kNeg) < Math.abs(iNeg) && Math.abs(kNeg) < iPos && Math.abs(kNeg) < kPos) {
			return Z_MINUS;    //4
		}

		if(iPos < 10 && iPos == Math.abs(iNeg)) {
			return rand.nextInt(2)+1;
		} else if(iPos < 10 && iPos == kPos) {
			if(rand.nextInt(2) + 1 == 1) {
				return X_PLUS;
			} else {
				return Z_PLUS;
			}
		} else if(iPos < 10 && iPos == Math.abs(kNeg)) {
			if(rand.nextInt(2) + 1 == 1) {
				return X_PLUS;
			} else {
				return Z_MINUS;
			}
		} else if(kPos < 10 && Math.abs(iNeg) == kPos) {
			if(rand.nextInt(2) + 1 == 1) {
				return X_MINUS;
			} else {
				return Z_PLUS;
			}
		} else if(Math.abs(iNeg) < 10 && Math.abs(iNeg) == Math.abs(kNeg)) {
			if(rand.nextInt(2) + 1 == 1) {
				return X_MINUS;
			} else {
				return Z_MINUS;
			}
		} else if(kPos < 10 && kPos == Math.abs(kNeg)) {
			if(rand.nextInt(2) + 1 == 1) {
				return Z_PLUS;
			} else {
				return Z_MINUS;
			}
		} else {
			return -1;
		}
	}
	
	public int pickDirection(int i, int j, int k) {
		int direction = findWater(i, j, k);
		if(direction != -1) {
			return direction;
		} else {
			return rand.nextInt(4) + 1;
		}
	}
}