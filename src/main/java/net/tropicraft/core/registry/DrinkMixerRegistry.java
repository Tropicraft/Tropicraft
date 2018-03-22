package net.tropicraft.core.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipe;
import net.tropicraft.core.common.item.ItemCocktail;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DrinkMixerRegistry extends TropicraftRegistry {

    /**
     * List of recipes for use in the mixer
     */
    private static List<MixerRecipe> recipes = new LinkedList<MixerRecipe>();

    /**
     * Register a recipe with the mixer
     * @param recipe MixerRecipe instance to register
     */
    public static void registerRecipe(MixerRecipe recipe) {
        recipes.add(recipe);
    }

    /**
     * Returns true if the ItemStack's Item is used in any of the recipes, that way we don't get weird recipes
     * @param item ItemStack to check for validity
     * @return true if the ItemStack sent in is used in any of the registered recipes
     */
    public static boolean isRegisteredIngredient(ItemStack item) {
        if (item.getItem() == ItemRegistry.cocktail) {
            return true; // assuming we didn't put garbage in
        }

        for (MixerRecipe recipe : recipes) {
            for (Ingredient i : recipe.getIngredients()) {
                if (ItemStack.areItemStacksEqual(i.getIngredient(), item)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isRegisteredIngredient(@Nonnull Item item) {
        return isRegisteredIngredient(new ItemStack(item, 1, 0));
    }

    public static @Nonnull ItemStack getResult(NonNullList<ItemStack> ingredients) {
        for (MixerRecipe recipe : recipes) {
            int validIngredientsFound = 0;

            for (Ingredient recipeIngredient : recipe.getIngredients()) {
                for (ItemStack mixerIngredient : ingredients) {
                    if (ItemStack.areItemStacksEqual(recipeIngredient.getIngredient(), mixerIngredient)) {
                        validIngredientsFound++;
                        break;
                    }
                }
            }

            // If mixer ingredients match those of a valid recipe, we have a proper result
            if (validIngredientsFound == ingredients.size()) {
                return ItemCocktail.makeCocktail(recipe);
            }
        }

        List<Ingredient> is = new ArrayList<Ingredient>();
        for (ItemStack ingredientStack : ingredients) {
            is.addAll(Ingredient.listIngredients(ingredientStack));
        }
        Collections.sort(is);

        return ItemCocktail.makeCocktail(is.toArray(new Ingredient[is.size()]));
    }

    public static List<MixerRecipe> getRecipes() {
        return recipes;
    }

}
