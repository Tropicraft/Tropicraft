package net.tropicraft.core.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Encyclopedia extends TropicalBook {
    
    /*
     * A mapping of page names to each item that should be included in the
     * Encyclopedia
     */
    private HashMap<String, List<ItemStack>> itemEntries = new HashMap<String, List<ItemStack>>();
    
    public Encyclopedia(String savedDataFile, String contentsFile, String outsideTexture, String insideTexture) {
        super(savedDataFile, contentsFile, outsideTexture, insideTexture);
    }

    @Override
    public boolean hasRecipeList() {
        return true;
    }
    
    /*
     * Adds an item name to ItemStack mapping for this encyclopedia. Multiple
     * items can be registered with the same name, so that having any of them
     * will make the given page visible
     * Note: the item name should match the page name given in the text file
     */
    public void includeItem(String itemname, ItemStack item) {
    	// TODO find out why this is triggering ??
//        if (item.isEmpty()) {
//            throw new IllegalArgumentException("Cannot include an empty stack! Group: " + itemname);
//        }
        if (!itemEntries.containsKey(itemname)) {
            itemEntries.put(itemname, new ArrayList<ItemStack>());
        }
        itemEntries.get(itemname).add(item);
    }
    
    /*
     * Returns all recipes related to a given entry page
     */
    public List<IRecipe> getRecipesForEntry(int page) {
        List<ItemStack> entryItems = itemEntries.get(getPageName(page));
        List<IRecipe> recipeList = new ArrayList<>();
        if (entryItems != null) {
            for (ItemStack item : entryItems) {
                //System.out.println();

                for (IRecipe recipe : ForgeRegistries.RECIPES.getValues()) {
                    if (recipe.getRecipeOutput().isItemEqual(item)) {
                        recipeList.add(recipe);
                    }
                }
            }
        }
        return recipeList;
    }
    
    /*
     * Returns the number of content pages associated with the given entry and
     * content type
     * Note: Individual recipes are counted as 1 page each - the GUI must
     * join them up according to the number of recipes to display per page
     */
    @Override
    public int getContentPageCount(int page, ContentMode mode) {
        
        if (page >= 0 && page < getPageCount()) {
            if (mode == ContentMode.INFO) {
                return 1;
            } else if (mode == ContentMode.RECIPE) {
                List<IRecipe> recipeList = getRecipesForEntry(page);
                if (recipeList != null) {
                    return recipeList.size();
                }
            }
        }
        
        return 0;
    }

    @Override
    public int entriesPerContentPage(ContentMode mode) {
        if (mode == ContentMode.RECIPE) {
            return 3;
        } 
        
        return super.entriesPerContentPage(mode);
    }

    @Override
    public boolean hasIndexIcons() {
        return true;
    }

    @Override
    public ItemStack getPageItemStack(int page) {
        if (page >= 0 && page < getPageCount()) {
            List<ItemStack> items = itemEntries.get(getPageName(page));
            if (items != null && !items.isEmpty()) {
                return items.get(0);
            }
        }
        return null;
    }
    
    @Override
    public void updatePagesFromInventory(InventoryPlayer inv) {
        for (ItemStack is : inv.mainInventory) {
            if (is != null) {
                ItemStack comparison = new ItemStack(is.getItem(), 1, is.getItemDamage());
                for (String entry : itemEntries.keySet()) {
                    if (!isPageVisible(entry)) {
                        List<ItemStack> itemsInBook = itemEntries.get(entry);
                        for (ItemStack itemInBook : itemsInBook) {
                            if (ItemStack.areItemStacksEqual(itemInBook, comparison)) {
                                markPageAsNewlyVisible(entry);
                            }
                        }
                    }
                }
            }
        }
    }

    public RecipeEntry getFormattedRecipe(IRecipe recipe) {
        // TODO support other kinds of recipes
        if (recipe instanceof ShapedRecipes) {
            ShapedRecipes shaped = (ShapedRecipes) recipe;
            int width = shaped.recipeWidth;// (Integer) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "b", "recipeWidth");
            int height = shaped.recipeHeight;// (Integer) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "c", "recipeHeight");
            NonNullList<Ingredient> items = shaped.recipeItems;// (ItemStack[]) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "d", "recipeItems");
            ItemStack output = recipe.getRecipeOutput();// (ItemStack) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "e", "recipeOutput");
            return new RecipeEntry(width, height, items, output);
        } else if (recipe instanceof ShapelessRecipes) {
            return new RecipeEntry(3, 3, recipe.getIngredients(), recipe.getRecipeOutput());
        }
        return null;
    }
    
    public class RecipeEntry {
        
        public int width;
        public int height;
        public NonNullList<Ingredient> ingredients;
        public ItemStack output;
        
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
    }
    
}
