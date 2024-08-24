package net.tropicraft.core.common.drinks;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MixerRecipe implements IMixerRecipe {

    /**
     * Result possible when all ingredients are mixed together
     */
    private final ResourceKey<Drink> result;

    /**
     * All ingredients that go into creating the result
     */
    private final Ingredient[] ingredients;

    public MixerRecipe(ResourceKey<Drink> result, Ingredient... ingredients) {
        this.result = result;
        this.ingredients = ingredients;
        Arrays.sort(ingredients);
    }

    public boolean matches(List<ItemStack> input) {
        for (Ingredient requiredIngredient : ingredients) {
            if (!hasIngredient(input, requiredIngredient)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasIngredient(List<ItemStack> input, Ingredient requiredIngredient) {
        for (ItemStack stack : input) {
            if (requiredIngredient.matches(stack)) {
                return true;
            }
        }
        return false;
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
    public ResourceKey<Drink> getCraftingResult() {
        return result;
    }
}
