package net.tropicraft.core.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class Encyclopedia extends TropicalBook {
    
    /*
     * Holds references from each item stack to all of the recipes it is related
     * to (either as result or as an ingredient)
     */
    private HashMap<ItemStack, List<ShapedRecipes>> recipes = new HashMap<ItemStack, List<ShapedRecipes>>();
    
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
        if (item.getItem() == null) {
            throw new IllegalArgumentException("Cannot include a null item! Group: " + itemname);
        }
        if (!itemEntries.containsKey(itemname)) {
            itemEntries.put(itemname, new ArrayList<ItemStack>());
        }
        itemEntries.get(itemname).add(item);
    }
    
    /*
     * Creates a new ShapedRecipe from the given args, and registers it with any
     * Encyclopedia entries related to it
     * Note: This method is borrowed/modified from CraftingManager
     */
    public void includeRecipe(ItemStack result, Object aobj[]) {

        // Note: Must use TreeSet for guaranteed ordering of elements
        Set<ItemStack> recipeContents = new HashSet<ItemStack>();
        addItemToRecipeContents(recipeContents, result);

        String recipeString = "";
        int i = 0;
        int width = 0;
        int height = 0;

        if (aobj[i] instanceof String[]) {
            String[] cols = (String[]) ((String[]) aobj[i++]);

            for (int j = 0; j < cols.length; ++j) {
                String row = cols[j];
                ++height;
                width = row.length();
                recipeString = recipeString + row;
            }
        } else {
            while (aobj[i] instanceof String) {
                String row = (String) aobj[i++];
                ++height;
                width = row.length();
                recipeString = recipeString + row;
            }
        }

        HashMap<Character, ItemStack> charMap;

        for (charMap = new HashMap<Character, ItemStack>(); i < aobj.length; i += 2) {
            Character itemChar = (Character) aobj[i];
            ItemStack itemStack = null;

            if (aobj[i + 1] instanceof Item) {
                itemStack = new ItemStack((Item) aobj[i + 1]);
            } else if (aobj[i + 1] instanceof Block) {
                itemStack = new ItemStack((Block) aobj[i + 1], 1, -1);
            } else if (aobj[i + 1] instanceof ItemStack) {
                itemStack = (ItemStack) aobj[i + 1];
            }

            charMap.put(itemChar, itemStack);
            addItemToRecipeContents(recipeContents, itemStack);
        }

        ItemStack[] slotArray = new ItemStack[width * height];

        for (int slots = 0; slots < width * height; slots++) {
            char itemChar = recipeString.charAt(slots);

            if (charMap.containsKey(itemChar)) {
                slotArray[slots] = ((ItemStack) charMap.get(itemChar)).copy();
            } else {
                slotArray[slots] = null;
            }
            
        }
        
        // Added code to register this recipe with the ingredient lookup
        ShapedRecipes recipe = new ShapedRecipes(width, height, slotArray, result);
        for (ItemStack item : recipeContents) {
            boolean foundKey = false;
            for (ItemStack key : recipes.keySet()) {
                if (item.isItemEqual(key)) {
                    foundKey = true;
                    recipes.get(key).add(recipe);
                    break;
                }
            }
            
            if (foundKey == false) {
                recipes.put(item, new ArrayList<ShapedRecipes>());
                recipes.get(item).add(recipe);
            }
            
        }
    }
    
    /*
     * Returns all recipes related to a given entry page
     */
    public List<ShapedRecipes> getRecipesForEntry(int page) {
        List<ItemStack> entryItems = itemEntries.get(getPageName(page));
        List<ShapedRecipes> recipeList = new ArrayList<ShapedRecipes>();
        if (entryItems != null) {
            for (ItemStack item : entryItems) {
                //System.out.println();

                for (ItemStack recipeItem : recipes.keySet()) {
                    
                    if (recipeItem.isItemEqual(item)) {
                        List<ShapedRecipes> itemRecipes = recipes.get(recipeItem);

                        if (itemRecipes != null) {
                            recipeList.addAll(itemRecipes);
                        }
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
                List<ShapedRecipes> recipeList = getRecipesForEntry(page);
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
    
    /*
     * Adds an ItemStack to the given set, as long as an equivalent item isn't
     * already in the set
     */
    private void addItemToRecipeContents(Set<ItemStack> items, ItemStack i) {
        
        boolean shouldAdd = !(items.contains(i));
        for (ItemStack listItem : items) {
            if (listItem.isItemEqual(i)) {
                shouldAdd = false;
                break;
            }
        }
        
        if (shouldAdd) {
            items.add(i);
        }
        
    }
    
    public RecipeEntry getFormattedRecipe(ShapedRecipes recipe) {
        try {
            int width = recipe.recipeWidth;//(Integer) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "b", "recipeWidth");
            int height = recipe.recipeHeight;//(Integer) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "c", "recipeHeight");
            ItemStack[] items = recipe.recipeItems;//(ItemStack[]) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "d", "recipeItems");
            ItemStack output = recipe.getRecipeOutput();//(ItemStack) TropicraftMod.getPrivateValueBoth(ShapedRecipes.class, recipe, "e", "recipeOutput");
            return new RecipeEntry(width, height, items, output);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public class RecipeEntry {
        
        public int width;
        public int height;
        public ItemStack[] ingredients;
        public ItemStack output;
        
        public RecipeEntry(int width, int height, ItemStack[] ingredients, ItemStack output) {
            this.width = width;
            this.height = height;
            this.ingredients = ingredients;
            this.output = output;
        }
        
    }
    
}
