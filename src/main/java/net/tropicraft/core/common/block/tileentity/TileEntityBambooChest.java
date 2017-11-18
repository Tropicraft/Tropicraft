package net.tropicraft.core.common.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityBambooChest extends TileEntityChest {

    /** Is this chest unbreakble (Koa chest) */
    private boolean unbreakable = false;

    public TileEntityBambooChest() {
        super();
    }

    @Override
    public String getName() {
        return "Bamboo Chest";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        unbreakable = nbttagcompound.getBoolean("unbreakable");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("unbreakable", unbreakable);
        
        return nbttagcompound;
    }

    /**
     * Performs the check for adjacent chests to determine if this chest is double or not.
     */
//    @Override
//    public void checkForAdjacentChests() {
//        if (!this.adjacentChestChecked) {
//            this.adjacentChestChecked = true;
//            this.adjacentChestZNeg = null;
//            this.adjacentChestXPos = null;
//            this.adjacentChestXNeg = null;
//            this.adjacentChestZPos = null;
//
//            if (this.func_94044_a(this.xCoord - 1, this.yCoord, this.zCoord))
//            {
//                this.adjacentChestXNeg = (TileEntityBambooChest)this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
//            }
//
//            if (this.func_94044_a(this.xCoord + 1, this.yCoord, this.zCoord))
//            {
//                this.adjacentChestXPos = (TileEntityBambooChest)this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
//            }
//
//            if (this.func_94044_a(this.xCoord, this.yCoord, this.zCoord - 1))
//            {
//                this.adjacentChestZNeg = (TileEntityBambooChest)this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
//            }
//
//            if (this.func_94044_a(this.xCoord, this.yCoord, this.zCoord + 1))
//            {
//                this.adjacentChestZPos = (TileEntityBambooChest)this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
//            }
//
//            // func_90009_a is called here
//            if (this.adjacentChestZNeg != null)
//            {
//                this.adjacentChestZNeg.updateContainingBlockInfo();
//            }
//            if (this.adjacentChestZPos != null)
//            {
//                this.adjacentChestZPos.updateContainingBlockInfo();
//            }
//            if (this.adjacentChestXPos != null)
//            {
//                this.adjacentChestXPos.updateContainingBlockInfo();
//            }
//            if (this.adjacentChestXNeg != null)
//            {
//                this.adjacentChestXNeg.updateContainingBlockInfo();
//            }
//        }
//    }

//    private void func_90009_a(TileEntityBambooChest par1TileEntityChest, int par2)
//    {
//        if (par1TileEntityChest.isInvalid())
//        {
//            this.adjacentChestChecked = false;
//        }
//        else if (this.adjacentChestChecked)
//        {
//            switch (par2)
//            {
//                case 0:
//                    if (this.adjacentChestZPos != par1TileEntityChest)
//                    {
//                        this.adjacentChestChecked = false;
//                    }
//
//                    break;
//                case 1:
//                    if (this.adjacentChestXNeg != par1TileEntityChest)
//                    {
//                        this.adjacentChestChecked = false;
//                    }
//
//                    break;
//                case 2:
//                    if (this.adjacentChestZNeg != par1TileEntityChest)
//                    {
//                        this.adjacentChestChecked = false;
//                    }
//
//                    break;
//                case 3:
//                    if (this.adjacentChestXPos != par1TileEntityChest)
//                    {
//                        this.adjacentChestChecked = false;
//                    }
//            }
//        }
//    }

//    /*
//     * Neighbour is the same block?
//     */
//    private boolean func_94044_a(int x, int y, int z)
//    {
//        Block block = worldObj.getBlock(x, y, z);
//        return block != null && 
//        		block instanceof BlockBambooChest ? 
//        				((BlockBambooChest)block).field_149956_a == this.func_145980_j() : false;
//    }

    /**
     *
     * @return Returns if this chest is unbreakable
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * Sets whether this chest is unbreakable or not
     * @param flag Value to set the unbreakable flag to
     */
    public void setIsUnbreakable(boolean flag) {
        unbreakable = flag;
    }
//
//    @Override
//    public AxisAlignedBB getRenderBoundingBox () {
//    	/**
//    	 * The rendering bounding box needs to be bigger for large chests,
//    	 * since these are two blocks wide.
//    	 */
//    	
//    	checkForAdjacentChests();
//    	int xCoord = pos.getX();
//    	int yCoord = pos.getY();
//    	int zCoord = pos.getZ();
//
//    	if (this.adjacentChestZPos != null)
//    	{
//    		return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 2);
//    	}
//    	else if (this.adjacentChestXPos != null)
//        {
//    		return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 2, yCoord + 1, zCoord + 1);
//        }
//    	else
//    	{
//    		return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
//    	}
//    }

}
