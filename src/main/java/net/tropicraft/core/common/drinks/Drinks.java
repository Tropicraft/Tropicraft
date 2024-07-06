package net.tropicraft.core.common.drinks;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    private static final List<MixerRecipe> recipes = new LinkedList<>();

    /**
     * Register a recipe with the mixer
     *
     * @param recipe MixerRecipe instance to register
     */
    public static void register(MixerRecipe recipe) {
        recipes.add(recipe);
    }

    public static ItemStack getResult(NonNullList<ItemStack> ingredients) {
        for (MixerRecipe recipe : recipes) {
            if (recipe.matches(ingredients)) {
                return CocktailItem.makeCocktail(recipe);
            }
        }
        return CocktailItem.makeCocktail(ingredients);
    }

    public static List<MixerRecipe> getRecipes() {
        return recipes;
    }
}
