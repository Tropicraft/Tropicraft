package net.tropicraft.core.common.drinks;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.item.CocktailItem;

import java.util.*;

public final class MixerRecipes {

    private static Map<Drink, Ingredient[]> drinkToIngredientsMap = new HashMap<>();
    private static Map<Drink, Item> drinkToSpecialItemMap = new HashMap<>();

    private MixerRecipes() {}

    public static void addMixerRecipes() {
        registerMixerRecipe(Drink.LIMEADE, Ingredient.lime, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(Drink.CAIPIRINHA, Ingredient.lime, Ingredient.sugarcane, Ingredient.waterBucket);
        registerMixerRecipe(Drink.ORANGEADE, Ingredient.orange, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(Drink.LEMONADE, Ingredient.lemon, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(Drink.BLACK_COFFEE, Ingredient.roastedCoffeeBean, Ingredient.waterBucket);
        //registerMixerRecipe(Drink.pinaColada, Ingredient.pineapple, Ingredient.coconutChunk);
        // !!!NOTE !!! Make sure pina colada remains the #4 recipe mkay - messes up achievements otherwise
        registerMixerRecipe(Drink.PINA_COLADA, Ingredient.pineappleCubes, Ingredient.coconutChunk);
        registerMixerRecipe(Drink.PINA_COLADA, Ingredient.pineappleCubes, Ingredient.coconut);
        registerMixerRecipe(Drink.PINA_COLADA, Ingredient.pineapple, Ingredient.coconutChunk);
        registerMixerRecipe(Drink.PINA_COLADA, Ingredient.pineapple, Ingredient.coconut);
        registerMixerRecipe(Drink.COCONUT_WATER, Ingredient.coconut, Ingredient.waterBucket);
        registerMixerRecipe(Drink.MAI_TAI, Ingredient.orange, Ingredient.lime, Ingredient.waterBucket);
    }

    /**
     * Helper method for registering a mixer recipe
     * @param result Result of the mixer recipe to be registered
     * @param ingredients Ingredients of the mixer recipe to be registered
     */
    private static void registerMixerRecipe(Drink result, Ingredient...ingredients) {
        Drinks.register(new MixerRecipe(result, ingredients));
        drinkToIngredientsMap.put(result, ingredients);
    }
    
    public static void setDrinkItem(Drink drink, CocktailItem item) {
        drinkToSpecialItemMap.put(drink, item);
    }

    /**
     * Probably a more efficient way of doing this, but whatever. This gives you an ItemStack result from a Drink object
     * @param drink Drink you want in ItemStack form
     * @return ItemStack form of a Drink
     */
    public static ItemStack getItemStack(Drink drink) {
        if (drinkToSpecialItemMap.containsKey(drink)) {
            return new ItemStack(drinkToSpecialItemMap.get(drink));
        }
        NonNullList<ItemStack> stack = NonNullList.create();

        for (Ingredient i : drinkToIngredientsMap.get(drink)) {
            stack.add(new ItemStack(i.getIngredientItem()));
        }

        return Drinks.getResult(stack);
    }

    public static boolean isValidRecipe(NonNullList<ItemStack> ingredientStacks) {
        Set<Ingredient> ingredients = new HashSet<>();

        for (ItemStack stack : ingredientStacks) {
            Ingredient ingredient = Ingredient.findMatchingIngredient(stack);
            if (ingredient == null) {
                return false;
            }

            ingredients.add(ingredient);

            for (MixerRecipe recipe : Drinks.getRecipes()) {
                Set<Ingredient> recipeIngredientSet = new HashSet<>(Arrays.asList(recipe.getIngredients()));
                if (ingredients.equals(recipeIngredientSet)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Drink getDrink(NonNullList<ItemStack> ingredientStacks) {
        final Set<Ingredient> ingredients = new HashSet<>();

        for (ItemStack stack : ingredientStacks) {
            final Ingredient ingredient = Ingredient.findMatchingIngredient(stack);
            if (ingredient == null) {
                return null;
            }

            ingredients.add(ingredient);

            for (MixerRecipe recipe : Drinks.getRecipes()) {
                final HashSet recipeIngredientSet = new HashSet<>(Arrays.asList(recipe.getIngredients()));

                if (ingredients.equals(recipeIngredientSet)) {
                    return recipe.getCraftingResult();
                }
            }
        }

        return null;
    }
}
