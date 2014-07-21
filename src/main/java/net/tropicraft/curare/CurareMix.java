package net.tropicraft.curare;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

public class CurareMix {

    /** The result of this mix */
    private CurareType result;
    
    private ItemStack[] ingredients;
    
    public CurareMix(CurareType result, ItemStack...ingredients) {
        this.result = result;
        this.ingredients = ingredients;
    }
    
    /**
     * @return All ingredients used in this recipe
     */
    public ItemStack[] getIngredients() {
        return ingredients;
    }    
    
    public CurareType getResult() {
        return result;
    }
    
    public int[] getSortedDamageVals() {
        int[] temp = new int[ingredients.length];
        
        int count = 0;
        
        for (ItemStack ing : ingredients) {
            temp[count] = ing.getItemDamage();
            count++;
        }
        
        Arrays.sort(temp);
        
        return temp;
    }
    
    @Override
    public String toString() {
        return getResult().toString();
    }
}
