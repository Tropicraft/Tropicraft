package net.tropicraft.core.common.item;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.core.client.CocktailColorHandler;
import net.tropicraft.core.common.drinks.ColorMixer;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipe;
import net.tropicraft.core.registry.CreativeTabRegistry;
import net.tropicraft.core.registry.DrinkMixerRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class ItemCocktail extends ItemTropicraftColored {

	private static final int DEFAULT_COLOR = 0xf3be36;

	// nbt layout:
	// - byte DrinkID: 0 if no known drink, else the Drink.drinkList index
	// - int Color: alpha blended mix of colors based on ingredients
	// - NBTTagList Ingredients
	//   - byte IngredientID: Ingredient.ingredientList index
	//   - short Count: count of this ingredient in the mixture, typically 1

	public ItemCocktail() {
		super("cocktail");
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(1);
		setContainerItem(ItemRegistry.bambooMug);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
		if (stack.getTagCompound() == null) {
			return;
		}
		
		Drink drink = getDrink(stack);
		if (drink != null) {
			tooltip.add(TextFormatting.ITALIC + I18n.format(getUnlocalizedName() + ".name"));
		}

		NBTTagList ingredients = stack.getTagCompound().getTagList("Ingredients", 10);

		for (int i = 0; i < ingredients.tagCount(); ++i) {
			NBTTagCompound ingredient = (NBTTagCompound) ingredients.getCompoundTagAt(i);
			//int count = ingredient.getShort("Count");
			int id = ingredient.getByte("IngredientID");
			String ingredientName = Ingredient.ingredientsList[id].getIngredient().getDisplayName();
			int ingredientColor = Ingredient.ingredientsList[id].getColor();
			//String lvl = StatCollector.translateToLocal("enchantment.level." + count);
			//par3List.add(ingredientName + " " + lvl);
			tooltip.add(ingredientName);
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
	    if (tab != CreativeTabRegistry.tropicraftTab) return;
		for (MixerRecipe recipe: DrinkMixerRegistry.getRecipes()) {
			list.add(makeCocktail(recipe));
		}
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	public static int getCocktailColor(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			if (stack.getTagCompound().hasKey("Color")) {
				return stack.getTagCompound().getInteger("Color"); 
			} else {
				return DEFAULT_COLOR;
			}
		} else {
			return DEFAULT_COLOR;
		}
	}

	public static @Nonnull ItemStack makeCocktail(MixerRecipe recipe) {
		ItemStack stack = new ItemStack(ItemRegistry.cocktail);
		NBTTagCompound nbt = new NBTTagCompound();
		Drink drink = recipe.getCraftingResult();
		nbt.setByte("DrinkID", (byte)drink.drinkId);
		NBTTagList tagList = new NBTTagList();

		Ingredient primary = null;
		List<Ingredient> additives = new LinkedList<Ingredient>();

		for (Ingredient ingredient: recipe.getIngredients()) {
			NBTTagCompound ingredientNbt = new NBTTagCompound();
			ingredientNbt.setByte("IngredientID", (byte)ingredient.id);
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

		stack.setTagCompound(nbt);
		return stack;
	}

	public static @Nonnull ItemStack makeCocktail(Ingredient[] ingredients) {
		ItemStack stack = new ItemStack(ItemRegistry.cocktail);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("DrinkID", (byte)0);
		NBTTagList tagList = new NBTTagList();

		Ingredient primary = null;
		List<Ingredient> additives = new LinkedList<Ingredient>();

		for (Ingredient ingredient: ingredients) {
			NBTTagCompound ingredientNbt = new NBTTagCompound();
			ingredientNbt.setByte("IngredientID", (byte)ingredient.id);
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

		stack.setTagCompound(nbt);
		return stack;

	}

	public static Ingredient[] getIngredients(ItemStack stack) {
		if (stack.getItem() != ItemRegistry.cocktail || !stack.hasTagCompound()) {
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
		if (stack.getItem() != ItemRegistry.cocktail || !stack.hasTagCompound()) {
			return null;
		}
		NBTTagCompound nbt = stack.getTagCompound();
		return Drink.drinkList[nbt.getByte("DrinkID")];
	}

	@Override
	public int getMaxItemUseDuration(@Nonnull ItemStack par1ItemStack) {
		return 32;
	}

	@Override
	public @Nonnull EnumAction getItemUseAction(@Nonnull ItemStack par1ItemStack) {
		return EnumAction.DRINK;
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer player) {
		world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

		for (Ingredient ingredient: getIngredients(itemstack)) {
			ingredient.onDrink(player);
		}

		Drink drink = getDrink(itemstack);

		if (drink != null) {
			drink.onDrink(player);
		}

		return new ItemStack(ItemRegistry.bambooMug);
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public @Nonnull ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entityLiving;
			this.onFoodEaten(stack, worldIn, player);

			Drink drink = getDrink(stack);

			if (worldIn.isRainingAt(player.getPosition()) && drink == Drink.pinaColada) {
				// TODO advancements player.addStat(AchievementRegistry.drinkPinaColada);
			}
		}

		return new ItemStack(ItemRegistry.bambooMug);
	}

	@Override
	public @Nonnull ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand) {
	    ItemStack stack = playerIn.getHeldItem(hand);
		Drink drink = getDrink(stack);

		if (drink != null) {
			if (!playerIn.canEat(drink.alwaysEdible)) {
				return new ActionResult<>(EnumActionResult.FAIL, stack);
			}
		} else if (!playerIn.canEat(false)) {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}

		playerIn.setActiveHand(hand);

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public int getColor(ItemStack itemstack, int tintIndex) {
		Drink drink = getDrink(itemstack);
		return (tintIndex == 0 || drink == null ? 16777215 : drink.color);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IItemColor getColorHandler() {
	    return new CocktailColorHandler();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		@Nonnull
		String name = getUnlocalizedName();
		Drink drink = getDrink(itemstack);
		if (drink != null) {
			name = Info.MODID + ".drink." + drink.name;
		}

		return name;
	}
	
	@Override
	public @Nonnull String getItemStackDisplayName(@Nonnull ItemStack stack) {
		Drink drink = getDrink(stack);
		if (drink != null) {
			return drink.textFormatting.toString() + TextFormatting.BOLD + super.getItemStackDisplayName(stack);
		}
		return super.getItemStackDisplayName(stack);
	}
}
