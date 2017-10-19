package net.tropicraft.client.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.tropicraft.core.common.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.core.common.item.scuba.ItemScubaTank;

public class ContainerScubaHarness extends Container {

    protected final IItemHandler itemHandler;
    private IInventory playerInventory;

    public ContainerScubaHarness(InventoryPlayer playerInventory, IItemHandler iItemHandler, EnumHand hand) {
        this.itemHandler = iItemHandler;
        this.playerInventory = playerInventory;

        // Tank slot 1
        this.addSlotToContainer(new SlotItemHandler(iItemHandler, 0, 67, 53) {
            /**
             * Check if the stack is allowed to be placed in this slot.
             */
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return super.isItemValid(stack) && stack.getItem() instanceof ItemScubaTank && !this.getHasStack();
            }
        });
        // Tank slot 2
        this.addSlotToContainer(new SlotItemHandler(iItemHandler, 1, 93, 53) {
            /**
             * Check if the stack is allowed to be placed in this slot.
             */
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return super.isItemValid(stack) && stack.getItem() instanceof ItemScubaTank && !this.getHasStack();
            }
        });

        for (int i1 = 0; i1 < 3; ++i1)
        {
            for (int k1 = 0; k1 < 9; ++k1)
            {
                this.addSlotToContainer(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1)
        {
            this.addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.playerInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 2)
            {
                if (!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 1, 2, false))
                {
                    return null;
                }
            }
            else if (this.getSlot(0).isItemValid(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return null;
                }
            }
            else if (2 <= 2 || !this.mergeItemStack(itemstack1, 2, 2, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
