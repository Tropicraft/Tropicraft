package net.tropicraft.curare;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

public class CurareMixRegistry {

    /**
     * Singleton instance of this class
     */
    private static CurareMixRegistry instance;

    /**
     * List of recipes for use in the mixer
     */
    private Map<CurareType, CurareMix> recipes;

    private CurareMixRegistry() {
        recipes = new HashMap<CurareType, CurareMix>();
    }

    /**
     * Getter for the singleton MixerRecipeManager instance
     * @return singleton MixerRecipeManager instance
     */
    public static CurareMixRegistry getInstance() {
        if (instance == null) {
            instance = new CurareMixRegistry();
        }

        return instance;
    }

    /**
     * Register a recipe with the mixer
     * @param recipe MixerRecipe instance to register
     */
    public void registerRecipe(CurareMix recipe) {
        recipes.put(recipe.getResult(), recipe);
    }
    
    public Collection<CurareMix> getRecipes() {
        return recipes.values();
    }
    
    public CurareMix getCurareMixFromType(CurareType type) {
        return recipes.get(type);
    }
    
    public int[] getSortedDamageVals(ItemStack[] ingredients) {
        int[] temp = new int[ingredients.length];
        
        int count = 0;
        
        for (ItemStack ing : ingredients) {
            System.out.println(ing.getItemDamage());
            temp[count] = ing.getItemDamage();
            count++;
        }
        
        Arrays.sort(temp);
        
        return temp;
    }
    
    /**
     * Compares the sorted array of shiftedIndex values in the ingredients array with each mix registered to see if the mix in question should provide a specialized result
     * @param ingredients
     * @return
     */
    public CurareType getCurareFromIngredients(ItemStack...ingredients) {
        
   //     System.out.println("Ingredients.length" + ingredients.length);
   //     System.out.println("Recipes.length" + recipes.size());
        for (CurareMix mix : getRecipes()) {
   //         for (int i : mix.getSortedDamageVals())
   //             System.out.print("mix " + i + " ");
            
    //        System.out.println("derp" + ingredients.length);
            
   //         for(int i : getSortedDamageVals(ingredients))
   //             System.out.println("this " + i + " ");
            
            if (Arrays.equals(mix.getSortedDamageVals(), getSortedDamageVals(ingredients))) {
      //          System.out.println("returning result: " + mix.getResult().toString());
                return mix.getResult();
            }
        }
        
        return CurareType.weakness;
    }
}
