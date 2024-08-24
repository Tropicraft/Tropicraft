package net.tropicraft.core.common.drinks;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public final class MixerRecipes {
    /**
     * List of recipes for use in the mixer
     */
    private static final List<MixerRecipe> recipes = new LinkedList<>();

    private MixerRecipes() {
    }

    public static void addMixerRecipes() {
        registerMixerRecipe(TropicraftDrinks.LIMEADE, Ingredient.lime, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(TropicraftDrinks.CAIPIRINHA, Ingredient.lime, Ingredient.sugarcane, Ingredient.waterBucket);
        registerMixerRecipe(TropicraftDrinks.ORANGEADE, Ingredient.orange, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(TropicraftDrinks.LEMONADE, Ingredient.lemon, Ingredient.sugar, Ingredient.waterBucket);
        registerMixerRecipe(TropicraftDrinks.BLACK_COFFEE, Ingredient.roastedCoffeeBean, Ingredient.waterBucket);
        //registerMixerRecipe(TropicraftDrinks.pinaColada, Ingredient.pineapple, Ingredient.coconutChunk);
        // !!!NOTE !!! Make sure pina colada remains the #4 recipe mkay - messes up achievements otherwise
        registerMixerRecipe(TropicraftDrinks.PINA_COLADA, Ingredient.pineappleCubes, Ingredient.coconutChunk);
        registerMixerRecipe(TropicraftDrinks.PINA_COLADA, Ingredient.pineappleCubes, Ingredient.coconut);
        registerMixerRecipe(TropicraftDrinks.PINA_COLADA, Ingredient.pineapple, Ingredient.coconutChunk);
        registerMixerRecipe(TropicraftDrinks.PINA_COLADA, Ingredient.pineapple, Ingredient.coconut);
        registerMixerRecipe(TropicraftDrinks.COCONUT_WATER, Ingredient.coconut, Ingredient.waterBucket);
        registerMixerRecipe(TropicraftDrinks.MAI_TAI, Ingredient.orange, Ingredient.lime, Ingredient.waterBucket);
    }

    /**
     * Helper method for registering a mixer recipe
     *
     * @param result Result of the mixer recipe to be registered
     * @param ingredients Ingredients of the mixer recipe to be registered
     */
    private static void registerMixerRecipe(ResourceKey<Drink> result, Ingredient... ingredients) {
        recipes.add(new MixerRecipe(result, ingredients));
    }

    public static boolean isValidRecipe(NonNullList<ItemStack> ingredientStacks) {
        return getRecipe(ingredientStacks) != null;
    }

    @Nullable
    public static ResourceKey<Drink> getDrink(NonNullList<ItemStack> ingredientStacks) {
        MixerRecipe recipe = getRecipe(ingredientStacks);
        return recipe != null ? recipe.getCraftingResult() : null;
    }

    @Nullable
    public static MixerRecipe getRecipe(NonNullList<ItemStack> ingredientStacks) {
        for (MixerRecipe recipe : recipes) {
            if (recipe.matches(ingredientStacks)) {
                return recipe;
            }
        }
        return null;
    }

    public static ItemStack getResult(HolderLookup.Provider registries, NonNullList<ItemStack> ingredients) {
        MixerRecipe recipe = getRecipe(ingredients);
        if (recipe != null) {
            return CocktailItem.makeCocktail(registries, recipe);
        }
        return CocktailItem.makeCocktail(ingredients);
    }
}
