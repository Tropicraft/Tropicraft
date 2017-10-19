package net.tropicraft.core.recipes;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.tropicraft.core.common.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.core.common.item.scuba.ItemScubaTank;

public class TropicraftShapelessRecipes extends ShapelessRecipes {

    public TropicraftShapelessRecipes(ItemStack output, List<ItemStack> inputList) {
        super(output, inputList);
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack itemstack = this.getRecipeOutput().copy();

        ItemStack scubaTank = null;
        ItemStack scubaHarness = null;
        int numFound = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (itemstack1 == null)
                continue;

            if (itemstack1.getItem() instanceof ItemScubaTank) {
                scubaTank = itemstack1.copy();
            } else            
                if (itemstack1.getItem() instanceof ItemScubaChestplateGear) {
                    scubaHarness = itemstack1.copy();
                }
        }
        
        ItemStack result = new ItemStack(scubaHarness.getItem());
        NBTTagCompound tankNbt = scubaTank.getTagCompound();
        NBTTagCompound harnessNbt = scubaHarness.getTagCompound();
        
        float tankAir = tankNbt.getFloat("AirContained");

//            if (itemstack1 != null && itemstack1.hasTagCompound())
//            {
//                // Set new nbt
//                if (itemstack1.getItem() instanceof ItemScubaTank) {
//                    itemstack = new ItemStack(ItemRegistry.itemscu)
//                }
//                itemstack.setTagCompound(itemstack1.getTagCompound().copy());
//            }
//        }

        return itemstack;
    }

}
