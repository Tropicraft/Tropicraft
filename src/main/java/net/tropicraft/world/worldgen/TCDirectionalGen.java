package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TCDirectionalGen extends TCGenBase {

	public static final int Z_PLUS = 0;
	public static final int Z_MINUS = 1;
	public static final int X_PLUS = 2;
	public static final int X_MINUS = 3;
	
	public int originX, originZ;
	public int dir;
	
	public TCDirectionalGen(World world, Random rand) {
		super(world, rand);
	}
	
	public TCDirectionalGen(World world, Random rand, int dir) {
		super(world, rand);
		this.dir = dir;
	}
	
	public void setOrigin(int originX, int originZ) {
		this.originX = originX;
		this.originZ = originZ;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public Block getBlockWithDir(int i, int j, int k) {
		switch(this.dir) {
		case 2:
			return this.worldObj.getBlock(this.originX + i, j, this.originZ + k);
		case 0:
			return this.worldObj.getBlock(this.originX + k, j, this.originZ - i);
		case 3:
			return this.worldObj.getBlock(this.originX - i, j, this.originZ - k);
		case 1:
			return this.worldObj.getBlock(this.originX - k, j, this.originZ + i);
		}
		return null;
	}
	
	public void placeBlockWithDir(int i, int j, int k, Block block, int meta) {
		switch(this.dir) {
			case 2:
				this.worldObj.setBlock(this.originX + i, j, this.originZ + k, block, meta, 3);
				return;
			case 0:
				this.worldObj.setBlock(this.originX + k, j, this.originZ - i, block, meta, 3);
				return;
			case 3:
				this.worldObj.setBlock(this.originX - i, j, this.originZ - k, block, meta, 3);
				return;
			case 1:
				this.worldObj.setBlock(this.originX - k, j, this.originZ + i, block, meta, 3);
				return;
		}
	}
	
	public TileEntity getTEWithDir(int i, int j, int k) {
		switch(this.dir) {
			case 2:
				return this.worldObj.getTileEntity(this.originX + i, j, this.originZ + k);
			case 0:
				return this.worldObj.getTileEntity(this.originX + k, j, this.originZ - i);
			case 3:
				return this.worldObj.getTileEntity(this.originX - i, j, this.originZ - k);
			case 1:
				return this.worldObj.getTileEntity(this.originX - k, j, this.originZ + i);
		}
		return null;
	}
	
	public int getTerrainHeightWithDir(int i, int k) {
		switch(this.dir) {
			case 2:
				return this.getTerrainHeightAt(this.originX + i, this.originZ + k);
			case 0:
				return this.getTerrainHeightAt(this.originX + k, this.originZ - i);
			case 3:
				return this.getTerrainHeightAt(this.originX - i, this.originZ - k);
			case 1:
				return this.getTerrainHeightAt(this.originX - k, this.originZ + i);
		}
		return 64;
	}
	
	public int getActualXAt(int i, int k) {
		switch(this.dir) {
			case 2:
				return this.originX + i;
			case 0:
				return this.originX + k;
			case 3:
				return this.originX - i;
			case 1:
				return this.originX - k;
		}	
		return this.originX;
	}
	
	public int getActualZAt(int i, int k) {
		switch(this.dir) {
			case 2:
				return this.originZ + k;
			case 0:
				return this.originZ - i;
			case 3:
				return this.originZ - k;
			case 1:
				return this.originZ + i;
		}		
		return this.originZ;
	}
	
}
