package net.tropicraft.core.common.drinks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Drinks {

    /**
     * List of recipes for use in the mixer
     */
    private static List<MixerRecipe> recipes = new LinkedList<>();

    /**
     * Register a recipe with the mixer
     * @param recipe MixerRecipe instance to register
     */
    public static void register(final MixerRecipe recipe) {
        recipes.add(recipe);
    }

    /**
     * Returns true if the ItemStack's Item is used in any of the recipes, that way we don't get weird recipes
     * @param item ItemStack to check for validity
     * @return true if the ItemStack sent in is used in any of the registered recipes
     */
    public static boolean isRegisteredIngredient(ItemStack item) {
        if (Drink.isDrink(item.getItem())) {
            return true; // assuming we didn't put garbage in
        }

        for (final MixerRecipe recipe : recipes) {
            for (Ingredient i : recipe.getIngredients()) {
                if (i.getIngredientItem() == item.getItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isRegisteredIngredient(@Nonnull Item item) {
        return isRegisteredIngredient(new ItemStack(item));
    }

    public static @Nonnull ItemStack getResult(final NonNullList<ItemStack> ingredients) {
        for (final MixerRecipe recipe : recipes) {
            int validIngredientsFound = 0;

            for (Ingredient recipeIngredient : recipe.getIngredients()) {
                for (ItemStack mixerIngredient : ingredients) {
                    if (recipeIngredient.getIngredientItem() == mixerIngredient.getItem()) {
                        validIngredientsFound++;
                        break;
                    }
                }
            }

            // If mixer ingredients match those of a valid recipe, we have a proper result
            // Make sure to only count valid ingredients (non-empty) when getting size
            if (validIngredientsFound == recipe.getIngredients().length) {
                return CocktailItem.makeCocktail(recipe);
            }
        }

        List<Ingredient> is = new ArrayList<>();
        for (ItemStack ingredientStack : ingredients) {
            is.addAll(Ingredient.listIngredients(ingredientStack));
        }
        Collections.sort(is);

        return CocktailItem.makeCocktail(ingredients);
    }

    public static List<MixerRecipe> getRecipes() {
        return recipes;
    }
}
