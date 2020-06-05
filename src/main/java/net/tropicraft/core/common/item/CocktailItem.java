package net.tropicraft.core.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.drinks.ColorMixer;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipe;
import net.tropicraft.core.common.drinks.MixerRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CocktailItem extends Item implements IColoredItem {

	private static final int DEFAULT_COLOR = 0xf3be36;
	
	private final Drink drink;

	// nbt layout:
	// - byte DrinkID: 0 if no known drink, else the Drink.drinkList index
	// - int Color: alpha blended mix of colors based on ingredients
	// - NBTTagList Ingredients
	//   - byte IngredientID: Ingredient.ingredientList index
	//   - short Count: count of this ingredient in the mixture, typically 1

	public CocktailItem(final Drink drink, final Properties properties) {
		super(properties);
		this.drink = drink;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		Drink drink = getDrink(stack);

		if (drink == Drink.COCKTAIL && stack.hasTag() && stack.getTag().contains("Ingredients")) {
    		final ListNBT ingredients = stack.getTag().getList("Ingredients", 10);
    
    		for (int i = 0; i < ingredients.size(); ++i) {
    			CompoundNBT ingredient = ingredients.getCompound(i);
    			int id = ingredient.getByte("IngredientID");
    			ITextComponent ingredientName = Ingredient.ingredientsList[id].getDisplayName();
    			int ingredientColor = Ingredient.ingredientsList[id].getColor();
    			//String lvl = StatCollector.translateToLocal("enchantment.level." + count);
    			//par3List.add(ingredientName + " " + lvl);
    			tooltip.add(ingredientName);
    		}
		}
	}

	public static int getCocktailColor(ItemStack stack) {
		final CompoundNBT tag = stack.getTag();
		if (tag != null && !tag.isEmpty()) {
			if (tag.contains("Color")) {
				return tag.getInt("Color");
			}
		}
		return DEFAULT_COLOR;
	}

	public static @Nonnull ItemStack makeCocktail(MixerRecipe recipe) {
		final ItemStack stack = MixerRecipes.getItemStack(recipe.getCraftingResult());
		CompoundNBT nbt = new CompoundNBT();
		Drink drink = recipe.getCraftingResult();
		nbt.putByte("DrinkID", (byte) drink.drinkId);
		ListNBT tagList = new ListNBT();

		Ingredient primary = null;
		List<Ingredient> additives = new LinkedList<>();

		for (Ingredient ingredient: recipe.getIngredients()) {
			CompoundNBT ingredientNbt = new CompoundNBT();
			ingredientNbt.putByte("IngredientID", (byte)ingredient.id);
			tagList.add(ingredientNbt);

			if (ingredient.isPrimary()) {
				primary = ingredient;
			} else {
				additives.add(ingredient);
			}
		}

		nbt.put("Ingredients", tagList);

		int color = primary == null ? DEFAULT_COLOR : primary.getColor();

		for (Ingredient additive: additives) {
			color = ColorMixer.getInstance().alphaBlendRGBA(color, additive.getColor(), additive.getAlpha());
		}

		nbt.putInt("Color", color);

		stack.setTag(nbt);
		return stack;
	}

	public static @Nonnull ItemStack makeCocktail(final NonNullList<ItemStack> itemStacks) {
		// TODO fixme this is so ugly ugh
		final ItemStack stack = new ItemStack(TropicraftItems.COCKTAILS.get(Drink.COCKTAIL).get());
		CompoundNBT nbt = new CompoundNBT();
		nbt.putByte("DrinkID", (byte) Drink.COCKTAIL.drinkId);
		ListNBT tagList = new ListNBT();

		List<Ingredient> ingredients = new ArrayList<>();
		for (ItemStack ingredientStack : itemStacks) {
			ingredients.addAll(Ingredient.listIngredients(ingredientStack));
		}
		Collections.sort(ingredients);

		Ingredient primary = null;
		List<Ingredient> additives = new LinkedList<>();

		for (Ingredient ingredient : ingredients) {
			CompoundNBT ingredientNbt = new CompoundNBT();
			ingredientNbt.putByte("IngredientID", (byte)ingredient.id);
			tagList.add(ingredientNbt);

			if (ingredient.isPrimary()) {
				primary = ingredient;
			} else {
				additives.add(ingredient);
			}
		}

		nbt.put("Ingredients", tagList);

		int color = primary == null ? DEFAULT_COLOR : primary.getColor();

		for (Ingredient additive: additives) {
			color = ColorMixer.getInstance().alphaBlendRGBA(color, additive.getColor(), additive.getAlpha());
		}

		nbt.putInt("Color", color);

		stack.setTag(nbt);
		return stack;

	}

	public static Ingredient[] getIngredients(ItemStack stack) {
		if (!Drink.isDrink(stack.getItem()) || !stack.hasTag()) {
			return new Ingredient[0];
		}

		CompoundNBT nbt = stack.getTag();
		ListNBT tagList = nbt.getList("Ingredients", 10);
		Ingredient[] ingredients = new Ingredient[tagList.size()];

		for (int i = 0; i < tagList.size(); ++i) {
			final int ingredientID = (tagList.getCompound(i)).getByte("IngredientID");
			ingredients[i] = Ingredient.ingredientsList[ingredientID];
		}

		return ingredients;
	}

	@Nullable
	public static Drink getDrink(ItemStack stack) {
		if (!Drink.isDrink(stack.getItem())) {
			return null;
		}
		return ((CocktailItem)stack.getItem()).drink;
	}

	@Override
	public int getUseDuration(ItemStack par1ItemStack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, PlayerEntity player) {
		world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

		for (Ingredient ingredient: getIngredients(itemstack)) {
			ingredient.onDrink(player);
		}

		Drink drink = getDrink(itemstack);

		if (drink != null) {
			drink.onDrink(player);
		}

		return new ItemStack(TropicraftItems.BAMBOO_MUG.get());
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity) {
			final PlayerEntity player = (PlayerEntity) entityLiving;
			onFoodEaten(stack, worldIn, player);

			Drink drink = getDrink(stack);

			if (worldIn.isRainingAt(player.getPosition()) && drink == Drink.PINA_COLADA) {
				// TODO advancements player.addStat(AchievementRegistry.drinkPinaColada);
			}
		}

		return new ItemStack(TropicraftItems.BAMBOO_MUG.get());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
	    ItemStack stack = playerIn.getHeldItem(hand);
		Drink drink = getDrink(stack);

		if (drink == null) {
			return new ActionResult<>(ActionResultType.FAIL, stack);
		}

		playerIn.setActiveHand(hand);

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public int getColor(ItemStack itemstack, int tintIndex) {
		Drink drink = getDrink(itemstack);
		return (tintIndex == 0 || drink == null ? 16777215 : drink.color);
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		Drink drink = getDrink(stack);
		if (drink != null) {
			return super.getDisplayName(stack).applyTextStyle(drink.textFormatting).applyTextStyle(TextFormatting.BOLD);
		}
		return super.getDisplayName(stack);
	}

	public Drink getDrink() {
		return drink;
	}
}
