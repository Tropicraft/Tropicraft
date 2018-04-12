package net.tropicraft.core.encyclopedia;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class RecipeEntry {
    
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;
    
    public RecipeEntry(int width, int height, NonNullList<Ingredient> items, ItemStack output) {
        this.width = width;
        this.height = height;
        this.ingredients = items;
        // Assure we always have a full list
        while (this.ingredients.size() < width * height) {
            this.ingredients.add(Ingredient.EMPTY);
        }
        this.output = output;
    }
    
    @Nonnull
    public ItemStack getCycledStack(int index, float cycle) {
        if (index >= ingredients.size() || index < 0) {
            return ItemStack.EMPTY;
        }
        Ingredient ing = ingredients.get(index);
        int i = MathHelper.floor(cycle / 30); // 30 tick = 1.5 second cycle
        ItemStack[] stacks = ing.getMatchingStacks();
        if (i < 0 || stacks.length == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack ret = stacks[i % stacks.length];
        if (ret == null) { // impossible?
            ret = ItemStack.EMPTY;
        }
        return ret;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getOutput() {
        return output;
    }
}
