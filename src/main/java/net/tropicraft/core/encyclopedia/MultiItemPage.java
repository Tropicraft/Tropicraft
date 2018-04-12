package net.tropicraft.core.encyclopedia;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ParametersAreNonnullByDefault
public class MultiItemPage extends ItemPage {
    
    private final ItemStack[] stacks;
    
    private int tick;
    
    public MultiItemPage(String id, ItemStack... stacks) {
        super(id, stacks[0]);
        this.stacks = stacks;
    }

    @Override
    protected ItemStack getStack() {
        return stacks[tick % stacks.length];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void drawIcon(int x, int y, float cycle) {
        this.tick = MathHelper.floor(cycle * 0.05);
        super.drawIcon(x, y, cycle);
    }

    @Override
    public List<RecipeEntry> getRelevantRecipes() {
        List<RecipeEntry> recipeList = new ArrayList<>();
        for (IRecipe recipe : ForgeRegistries.RECIPES.getValues()) {
            for (ItemStack stack : stacks) {
                if (recipe.getRecipeOutput().isItemEqual(stack)) {
                    recipeList.add(getFormattedRecipe(recipe));
                }
            }
        }
        return recipeList;
    }
    
    @Override
    public boolean discover(World world, EntityPlayer player) {
        for (ItemStack is : player.inventory.mainInventory) {
            if (!is.isEmpty()) {
                for (ItemStack stack : stacks) {
                    if (ItemStack.areItemsEqual(is, stack) && (!stack.hasTagCompound() || ItemStack.areItemStackTagsEqual(is, stack))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
