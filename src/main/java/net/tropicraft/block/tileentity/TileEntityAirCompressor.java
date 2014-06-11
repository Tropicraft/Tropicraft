package net.tropicraft.block.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public class TileEntityAirCompressor extends TileEntity implements ISidedInventory{

	public int timeCompressed;
	
	public int timeRemaining;
	
	/** index 0 is the power source, the rest are the item(s) being filled */
	private ItemStack[] itemStacks = new ItemStack[2];
	
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
	
	public TileEntityAirCompressor() {

	}
	
	/**
	 * @return Is the air compressor currently filling something?
	 */
	public boolean isFilling() {
		return this.timeRemaining > 0;
	}

	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	/**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
	@Override
    public ItemStack decrStackSize(int slot, int numToRemove) {
        if (this.itemStacks[slot] != null) {
            ItemStack itemstack;

            if (this.itemStacks[slot].stackSize <= numToRemove) {
                itemstack = this.itemStacks[slot];
                this.itemStacks[slot] = null;
                return itemstack;
            } else {
                itemstack = this.itemStacks[slot].splitStack(numToRemove);

                if (this.itemStacks[slot].stackSize == 0) {
                    this.itemStacks[slot] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.itemStacks[slot] != null) {
            ItemStack itemstack = this.itemStacks[slot];
            this.itemStacks[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        this.itemStacks[slot] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }


	@Override
	public String getInventoryName() {
		return StatCollector.translateToLocal("gui.tropicraft:airCompressor");
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemstack) : true);
	}
	
    public boolean isItemFuel(ItemStack itemstack)
    {
        /**
         * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
         * fuel
         */
        return getItemBurnTime(itemstack) > 0;
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
		return this.isItemValidForSlot(slot, itemstack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack var2, int side) {
		return side != 0 || slot != 1;
	}

}
