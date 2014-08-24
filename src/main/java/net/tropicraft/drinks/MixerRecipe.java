package net.tropicraft.drinks;

import java.util.Arrays;


public class MixerRecipe implements IMixerRecipe {

    /**
     * Result possible when all ingredients are mixed together
     */
    private Drink result;
    
    /**
     * All ingredients that go into creating the result
     */
    private Ingredient[] ingredients;
    
    public MixerRecipe(Drink result, Ingredient...ingredients) {
        this.result = result;
        
        this.ingredients = ingredients;
        Arrays.sort(ingredients);
    }

    /**
     * @return All ingredients used in this recipe
     */
    @Override
    public Ingredient[] getIngredients() {
        return ingredients;
    }

    /**
     * @return Result possible when all the ingredients are in the mixer
     */
    @Override
    public Drink getCraftingResult() {
        return result;
    }
}
