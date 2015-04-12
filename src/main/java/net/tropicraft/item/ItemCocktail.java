package net.tropicraft.item;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityBambooMug;
import net.tropicraft.drinks.ColorMixer;
import net.tropicraft.drinks.Drink;
import net.tropicraft.drinks.Ingredient;
import net.tropicraft.drinks.MixerRecipe;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCDrinkMixerRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCocktail extends ItemTropicraft {
    private static final int DEFAULT_COLOR = 0xf3be36;
    private IIcon contentsIcon;

    // nbt layout:
    // - byte DrinkID: 0 if no known drink, else the Drink.drinkList index
    // - int Color: alpha blended mix of colors based on ingredients
    // - NBTTagList Ingredients
    //   - byte IngredientID: Ingredient.ingredientList index
    //   - short Count: count of this ingredient in the mixture, typically 1
    
    public ItemCocktail(CreativeTabs tabs) {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(TCCreativeTabRegistry.tabFood);
        setContainerItem(TCItemRegistry.bambooMug);
    }
    
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (par1ItemStack.stackTagCompound == null) {
            return;
        }
        
        NBTTagList ingredients = par1ItemStack.stackTagCompound.getTagList("Ingredients", 10);

        for (int i = 0; i < ingredients.tagCount(); ++i) {
            NBTTagCompound ingredient = (NBTTagCompound) ingredients.getCompoundTagAt(i);
            //int count = ingredient.getShort("Count");
            int id = ingredient.getByte("IngredientID");
            String ingredientName = Ingredient.ingredientsList[id].getIngredient().getDisplayName();
            int ingredientColor = Ingredient.ingredientsList[id].getColor();
            //String lvl = StatCollector.translateToLocal("enchantment.level." + count);
            //par3List.add(ingredientName + " " + lvl);
            par3List.add(ingredientName);
        }
        
        Drink drink = Drink.drinkList[par1ItemStack.stackTagCompound.getByte("DrinkID")];
        
        if (drink != null) {
            par3List.add("\247o" + drink.displayName);
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List list) {
        for (MixerRecipe recipe: TCDrinkMixerRegistry.getInstance().getRecipes()) {
            list.add(makeCocktail(recipe));
        }
    }
    
    @Override
    public boolean getShareTag() {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
        return par2 == 0 ? this.itemIcon : this.contentsIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        if (par2 == 0) {
            return 0xffffff;
        } else {
            return getCocktailColor(par1ItemStack);
        }
    }
    
    public static int getCocktailColor(ItemStack stack) {
        if (stack.stackTagCompound != null) {
            if (stack.stackTagCompound.hasKey("Color")) {
                return stack.stackTagCompound.getInteger("Color"); 
            } else {
                return DEFAULT_COLOR;
            }
        } else {
            return DEFAULT_COLOR;
        }
    }
    
    public static ItemStack makeCocktail(MixerRecipe recipe) {
        ItemStack stack = new ItemStack(TCItemRegistry.cocktail);
        NBTTagCompound nbt = new NBTTagCompound();
        Drink drink = recipe.getCraftingResult();
        nbt.setByte("DrinkID", (byte)drink.drinkId);
        NBTTagList tagList = new NBTTagList();
        
        Ingredient primary = null;
        List<Ingredient> additives = new LinkedList<Ingredient>();
        
        for (Ingredient ingredient: recipe.getIngredients()) {
            NBTTagCompound ingredientNbt = new NBTTagCompound();
            ingredientNbt.setByte("IngredientID", (byte)ingredient.ingredientId);
            tagList.appendTag(ingredientNbt);
            
            if (ingredient.isPrimary()) {
                primary = ingredient;
            } else {
                additives.add(ingredient);
            }
        }
        
        nbt.setTag("Ingredients", tagList);
        
        int color = primary == null ? DEFAULT_COLOR : primary.getColor();
        
        for (Ingredient additive: additives) {
            color = ColorMixer.getInstance().alphaBlendRGBA(color, additive.getColor(), additive.getAlpha());
        }
        
        nbt.setInteger("Color", color);

        stack.stackTagCompound = nbt;
        return stack;
    }
    
    public static ItemStack makeCocktail(Ingredient[] ingredients) {
        ItemStack stack = new ItemStack(TCItemRegistry.cocktail);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("DrinkID", (byte)0);
        NBTTagList tagList = new NBTTagList();
        
        Ingredient primary = null;
        List<Ingredient> additives = new LinkedList<Ingredient>();
        
        for (Ingredient ingredient: ingredients) {
            NBTTagCompound ingredientNbt = new NBTTagCompound();
            ingredientNbt.setByte("IngredientID", (byte)ingredient.ingredientId);
            tagList.appendTag(ingredientNbt);
            
            if (ingredient.isPrimary()) {
                primary = ingredient;
            } else {
                additives.add(ingredient);
            }
        }
        
        nbt.setTag("Ingredients", tagList);
        
        int color = primary == null ? DEFAULT_COLOR : primary.getColor();
        
        for (Ingredient additive: additives) {
            color = ColorMixer.getInstance().alphaBlendRGBA(color, additive.getColor(), additive.getAlpha());
        }
        
        nbt.setInteger("Color", color);
        
        stack.stackTagCompound = nbt;
        return stack;
        
    }
    
    public static Ingredient[] getIngredients(ItemStack stack) {
        if (stack.getItem() != TCItemRegistry.cocktail || !stack.hasTagCompound()) {
            return new Ingredient[0];
        }
        
        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagList tagList = nbt.getTagList("Ingredients", 10);
        Ingredient[] ingredients = new Ingredient[tagList.tagCount()];
        
        for (int i = 0; i < tagList.tagCount(); ++i) {
            int id = ((NBTTagCompound)tagList.getCompoundTagAt(i)).getByte("IngredientID");
            ingredients[i] = Ingredient.ingredientsList[id];
        }
        
        return ingredients;
    }
    
    public static Drink getDrink(ItemStack stack) {
        if (stack.getItem() != TCItemRegistry.cocktail || !stack.hasTagCompound()) {
            return null;
        }
        NBTTagCompound nbt = stack.getTagCompound();
        return Drink.drinkList[nbt.getByte("DrinkID")];
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        Block var11 = par3World.getBlock(par4, par5, par6);

        if (var11 == Blocks.snow) {
            par7 = 1;
        } else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush && (var11 == null || !var11.isReplaceable(par3World, par4, par5, par6))) {
            if (par7 == 0) {
                --par5;
            } else if (par7 == 1) {
                ++par5;
            } else if (par7 == 2) {
                --par6;
            } else if (par7 == 3) {
                ++par6;
            } else if (par7 == 4) {
                --par4;
            } else if (par7 == 5) {
                ++par4;
            }
        }
        
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        } else if (par3World.canPlaceEntityOnSide(TCBlockRegistry.bambooMug, par4, par5, par6, false, par7, par2EntityPlayer, null)) {
            Block var12 = TCBlockRegistry.bambooMug;
            int var13 = this.getMetadata(par1ItemStack.getItemDamage());
            int var14 = var12.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);

            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14)) {
                par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), var12.stepSound.func_150496_b(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, TCBlockRegistry.bambooMug, metadata, 2)) {
            return false;
        }

        if (world.getBlock(x, y, z) == TCBlockRegistry.bambooMug) {
            TCBlockRegistry.bambooMug.onBlockPlacedBy(world, x, y, z, player, null);
            TCBlockRegistry.bambooMug.onPostBlockPlaced(world, x, y, z, metadata);
            
            TileEntityBambooMug mug = (TileEntityBambooMug) world.getTileEntity(x, y, z);
            mug.setCocktail(stack.copy());
            
            int var6 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            int meta = 2;
            
            if (var6 == 0) {
                meta = 2;
            } else if (var6 == 1) {
                meta = 5;
            } else if (var6 == 2) {
                meta = 3;
            } else if (var6 == 3) {
                meta = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        }

        return true;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        Drink drink = getDrink(par1ItemStack);
        
        if (drink != null) {
            if (!par3EntityPlayer.canEat(drink.alwaysEdible)) {
                return par1ItemStack;
            }
        } else if (!par3EntityPlayer.canEat(false)) {
            return par1ItemStack;
        }
        
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

        return par1ItemStack;
    }
    
    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        
        for (Ingredient ingredient: getIngredients(par1ItemStack)) {
            ingredient.onDrink(par3EntityPlayer);
        }
        
        Drink drink = getDrink(par1ItemStack);
        
        if (drink != null) {
            drink.onDrink(par3EntityPlayer);
        }
        
        return new ItemStack(TCItemRegistry.bambooMug);
    }

    @Override
    public void registerIcons(IIconRegister iconRegistry) {
        super.registerIcons(iconRegistry);
        this.contentsIcon = iconRegistry.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "contents");
    }
}
