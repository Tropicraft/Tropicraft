package net.tropicraft.core.encyclopedia;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ParametersAreNonnullByDefault
public class ItemPage extends SimplePage {
    
    private final ItemStack stack;

    public ItemPage(String id, ItemStack stack) {
        super(id);
        this.stack = stack;
    }
    
    protected ItemStack getStack() {
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawIcon(int x, int y, float cycle) {
        GlStateManager.color(0, 0, 0);
        // itemRenderer.renderWithColor = book.isPageVisible(selectedIndex);
        // TODO 1.12 ??
        // itemRenderer.isNotRenderingEffectsInGUI(book.isPageVisible(selectedIndex));
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(getStack(), x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }
    
    @Override
    public List<RecipeEntry> getRelevantRecipes() {
        List<RecipeEntry> recipeList = new ArrayList<>();
        for (IRecipe recipe : ForgeRegistries.RECIPES.getValues()) {
            if (recipe.getRecipeOutput().isItemEqual(getStack())) {
                recipeList.add(getFormattedRecipe(recipe));
            }
        }
        return recipeList;
    }

    @Nullable
    protected RecipeEntry getFormattedRecipe(IRecipe recipe) {
        // TODO support other kinds of recipes
        if (recipe instanceof ShapedRecipes) {
            ShapedRecipes shaped = (ShapedRecipes) recipe;
            int width = shaped.recipeWidth;
            int height = shaped.recipeHeight;
            NonNullList<Ingredient> items = shaped.recipeItems;
            ItemStack output = recipe.getRecipeOutput();
            return new RecipeEntry(width, height, items, output);
        } else if (recipe instanceof ShapelessRecipes) {
            return new RecipeEntry(3, 3, recipe.getIngredients(), recipe.getRecipeOutput());
        }
        return null;
    }
    
    @Override
    public boolean discover(World world, EntityPlayer player) {
        for (ItemStack is : player.inventory.mainInventory) {
            if (!is.isEmpty()) {
                ItemStack stack = getStack();
                if (ItemStack.areItemsEqual(is, stack) && (!stack.hasTagCompound() || ItemStack.areItemStackTagsEqual(is, stack))) {
                    return true;
                }
            }
        }
        return false;
    }
}
