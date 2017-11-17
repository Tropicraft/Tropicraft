package net.tropicraft.core.common.drinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.tropicraft.core.registry.DrinkMixerRegistry;

public final class MixerRecipes {

	private static Map<Drink, Ingredient[]> drinkToIngredientsMap = new HashMap<Drink, Ingredient[]>();

	private MixerRecipes() {}

	public static void addMixerRecipes() {
		registerMixerRecipe(Drink.limeade, Ingredient.lime, Ingredient.sugar, Ingredient.waterBucket);
		registerMixerRecipe(Drink.caipirinha, Ingredient.lime, Ingredient.sugarcane, Ingredient.waterBucket);
		registerMixerRecipe(Drink.orangeade, Ingredient.orange, Ingredient.sugar, Ingredient.waterBucket);
		registerMixerRecipe(Drink.lemonade, Ingredient.lemon, Ingredient.sugar, Ingredient.waterBucket);
		registerMixerRecipe(Drink.blackCoffee, Ingredient.roastedCoffeeBean, Ingredient.waterBucket);
		//registerMixerRecipe(Drink.pinaColada, Ingredient.pineapple, Ingredient.coconutChunk);
		// !!!NOTE !!! Make sure pina colada remains the #4 recipe mkay - messes up achievements otherwise
		registerMixerRecipe(Drink.pinaColada, Ingredient.pineappleCubes, Ingredient.coconutChunk);
		registerMixerRecipe(Drink.pinaColada, Ingredient.pineappleCubes, Ingredient.coconut);
		registerMixerRecipe(Drink.pinaColada, Ingredient.pineapple, Ingredient.coconutChunk);
		registerMixerRecipe(Drink.pinaColada, Ingredient.pineapple, Ingredient.coconut);
		registerMixerRecipe(Drink.coconutWater, Ingredient.coconut, Ingredient.waterBucket);
		registerMixerRecipe(Drink.maiTai, Ingredient.orange, Ingredient.lime, Ingredient.waterBucket);
	}

	/**
	 * Helper method for registering a mixer recipe
	 * @param result Result of the mixer recipe to be registered
	 * @param ingredients Ingredients of the mixer recipe to be registered
	 */
	private static void registerMixerRecipe(Drink result, Ingredient...ingredients) {
		DrinkMixerRegistry.registerRecipe(new MixerRecipe(result, ingredients));
		drinkToIngredientsMap.put(result, ingredients);
	}   

	/**
	 * Probably a more efficient way of doing this, but whatever. This gives you an ItemStack result from a Drink object
	 * @param drink Drink you want in ItemStack form
	 * @return ItemStack form of a Drink
	 */
	public static ItemStack getItemStack(Drink drink) {
		NonNullList<ItemStack> stack = NonNullList.create();

		for (Ingredient i : drinkToIngredientsMap.get(drink)) {
			stack.add(i.getIngredient());
		}

		return DrinkMixerRegistry.getResult(stack);
	}

	public static boolean isValidRecipe(NonNullList<ItemStack> ingredientStacks) {
		Set<Ingredient> ingredients = new HashSet<Ingredient>();

		for (ItemStack stack : ingredientStacks) {
			Ingredient ingredient = Ingredient.findMatchingIngredient(stack);
			if (ingredient == null)
				return false;

			ingredients.add(ingredient);

			for (MixerRecipe recipe : DrinkMixerRegistry.getRecipes()) {
				Set<Ingredient> recipeIngredientSet = new HashSet<Ingredient>();
				for (Ingredient i : recipe.getIngredients()) {
					recipeIngredientSet.add(i);
				}

				if (ingredients.equals(recipeIngredientSet)) {
					return true;
				}
			}
		}

		return false;
	}

	public static Drink getDrink(NonNullList<ItemStack> ingredientStacks) {
		Set<Ingredient> ingredients = new HashSet<Ingredient>();

		for (ItemStack stack : ingredientStacks) {
			Ingredient ingredient = Ingredient.findMatchingIngredient(stack);
			if (ingredient == null)
				return null;

			ingredients.add(ingredient);

			for (MixerRecipe recipe : DrinkMixerRegistry.getRecipes()) {
				Set<Ingredient> recipeIngredientSet = new HashSet<Ingredient>();
				for (Ingredient i : recipe.getIngredients()) {
					recipeIngredientSet.add(i);
				}

				if (ingredients.equals(recipeIngredientSet)) {
					return recipe.getCraftingResult();
				}
			}
		}

		return null;
	}
}
