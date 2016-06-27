package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
	
	public Block getBlockWithDir(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		switch(this.dir) {
		case 2:
			return this.worldObj.getBlockState(pos(this.originX + i, j, this.originZ + k)).getBlock();
		case 0:
			return this.worldObj.getBlockState(pos(this.originX + k, j, this.originZ - i)).getBlock();
		case 3:
			return this.worldObj.getBlockState(pos(this.originX - i, j, this.originZ - k)).getBlock();
		case 1:
			return this.worldObj.getBlockState(pos(this.originX - k, j, this.originZ + i)).getBlock();
		}
		return null;
	}
	
	public void placeBlockWithDir(BlockPos pos, IBlockState state) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		switch(this.dir) {
			case 2:
				this.worldObj.setBlockState(pos(this.originX + i, j, this.originZ + k), state, blockGenNotifyFlag);
				return;
			case 0:
				this.worldObj.setBlockState(pos(this.originX + k, j, this.originZ - i), state, blockGenNotifyFlag);
				return;
			case 3:
				this.worldObj.setBlockState(pos(this.originX - i, j, this.originZ - k), state, blockGenNotifyFlag);
				return;
			case 1:
				this.worldObj.setBlockState(pos(this.originX - k, j, this.originZ + i), state, blockGenNotifyFlag);
				return;
		}
	}
	
	public TileEntity getTEWithDir(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		switch(this.dir) {
			case 2:
				return this.worldObj.getTileEntity(pos(this.originX + i, j, this.originZ + k));
			case 0:
				return this.worldObj.getTileEntity(pos(this.originX + k, j, this.originZ - i));
			case 3:
				return this.worldObj.getTileEntity(pos(this.originX - i, j, this.originZ - k));
			case 1:
				return this.worldObj.getTileEntity(pos(this.originX - k, j, this.originZ + i));
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
	
	public BlockPos pos(int x, int y, int z) {
		return new BlockPos(x, y, z);
	}
	
}