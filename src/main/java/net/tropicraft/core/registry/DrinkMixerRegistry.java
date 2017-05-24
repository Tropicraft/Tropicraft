package net.tropicraft.core.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipe;
import net.tropicraft.core.common.item.ItemCocktail;

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
		if (item.getItem() != null && item.getItem() == ItemRegistry.cocktail) {
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

	public static boolean isRegisteredIngredient(Item item) {
		return isRegisteredIngredient(new ItemStack(item, 1, 0));
	}

	public static ItemStack getResult(ItemStack[] ingredients) {
		for (MixerRecipe recipe : recipes) {
			if (ItemStack.areItemStacksEqual(recipe.getIngredients()[0].getIngredient(), ingredients[0]) && ItemStack.areItemStacksEqual(recipe.getIngredients()[1].getIngredient(), ingredients[1])) {
				return ItemCocktail.makeCocktail(recipe);
			}
			if (ItemStack.areItemStacksEqual(recipe.getIngredients()[0].getIngredient(), ingredients[1]) && ItemStack.areItemStacksEqual(recipe.getIngredients()[1].getIngredient(), ingredients[0])) {
				return ItemCocktail.makeCocktail(recipe);
			}
		}

		List<Ingredient> is = new ArrayList<Ingredient>();
		is.addAll(Ingredient.listIngredients(ingredients[0]));
		is.addAll(Ingredient.listIngredients(ingredients[1]));
		Collections.sort(is);

		return ItemCocktail.makeCocktail(is.toArray(new Ingredient[is.size()]));
	}

	public static List<MixerRecipe> getRecipes() {
		return recipes;
	}

}
