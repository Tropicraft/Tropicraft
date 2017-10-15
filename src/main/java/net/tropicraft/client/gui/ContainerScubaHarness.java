package net.tropicraft.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerScubaHarness extends Container {

    protected final IItemHandler itemHandler;
    
    public ContainerScubaHarness(InventoryPlayer inventory, IItemHandler iItemHandler, EnumHand hand) {
        this.itemHandler = iItemHandler;
        
        // Tank slot 1
        this.addSlotToContainer(new SlotItemHandler(iItemHandler, 0, 0, 0));
        // Tank slot 2
        this.addSlotToContainer(new SlotItemHandler(iItemHandler, 1, 20, 0));
        
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18));
            }
        }
        
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(inventory, i1, 8 + i1 * 18, 161));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
