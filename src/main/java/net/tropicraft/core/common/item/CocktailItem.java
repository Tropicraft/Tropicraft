package net.tropicraft.core.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.drinks.*;

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
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        Drink drink = getDrink(stack);

        if (drink == Drink.COCKTAIL && stack.hasTag() && stack.getTag().contains("Ingredients")) {
            final ListTag ingredients = stack.getTag().getList("Ingredients", 10);
    
            for (int i = 0; i < ingredients.size(); ++i) {
                CompoundTag ingredient = ingredients.getCompound(i);
                int id = ingredient.getByte("IngredientID");
                Component ingredientName = Ingredient.ingredientsList[id].getDisplayName();
                int ingredientColor = Ingredient.ingredientsList[id].getColor();
                //String lvl = StatCollector.translateToLocal("enchantment.level." + count);
                //par3List.add(ingredientName + " " + lvl);
                tooltip.add(ingredientName);
            }
        }
    }

    public static int getCocktailColor(ItemStack stack) {
        final CompoundTag tag = stack.getTag();
        if (tag != null && !tag.isEmpty()) {
            if (tag.contains("Color")) {
                return tag.getInt("Color");
            }
        }
        return DEFAULT_COLOR;
    }

    public static @Nonnull ItemStack makeCocktail(MixerRecipe recipe) {
        final ItemStack stack = MixerRecipes.getItemStack(recipe.getCraftingResult());
        CompoundTag nbt = new CompoundTag();
        Drink drink = recipe.getCraftingResult();
        nbt.putByte("DrinkID", (byte) drink.drinkId);
        ListTag tagList = new ListTag();

        Ingredient primary = null;
        List<Ingredient> additives = new LinkedList<>();

        for (Ingredient ingredient: recipe.getIngredients()) {
            CompoundTag ingredientNbt = new CompoundTag();
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
        CompoundTag nbt = new CompoundTag();
        nbt.putByte("DrinkID", (byte) Drink.COCKTAIL.drinkId);
        ListTag tagList = new ListTag();

        List<Ingredient> ingredients = new ArrayList<>();
        for (ItemStack ingredientStack : itemStacks) {
            ingredients.addAll(Ingredient.listIngredients(ingredientStack));
        }
        Collections.sort(ingredients);

        Ingredient primary = null;
        List<Ingredient> additives = new LinkedList<>();

        for (Ingredient ingredient : ingredients) {
            CompoundTag ingredientNbt = new CompoundTag();
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

        CompoundTag nbt = stack.getTag();
        ListTag tagList = nbt.getList("Ingredients", 10);
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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public ItemStack onFoodEaten(ItemStack itemstack, Level world, Player player) {
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);

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
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            final Player player = (Player) entityLiving;
            onFoodEaten(stack, worldIn, player);

            Drink drink = getDrink(stack);

            if (worldIn.isRainingAt(player.blockPosition()) && drink == Drink.PINA_COLADA) {
                // TODO 1.17 advancements player.addStat(AchievementRegistry.drinkPinaColada);
            }
            return player.getAbilities().instabuild ? stack : new ItemStack(TropicraftItems.BAMBOO_MUG.get());
        }

        return new ItemStack(TropicraftItems.BAMBOO_MUG.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        Drink drink = getDrink(stack);

        if (drink == null) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }

        playerIn.startUsingItem(hand);

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public int getColor(ItemStack itemstack, int tintIndex) {
        Drink drink = getDrink(itemstack);
        return (tintIndex == 0 || drink == null ? 16777215 : drink.color);
    }
    
    @Override
    public Component getName(ItemStack stack) {
        Drink drink = getDrink(stack);
        if (drink != null) {
            return super.getName(stack).copy().withStyle(drink.textFormatting).withStyle(ChatFormatting.BOLD);
        }
        return super.getName(stack);
    }

    public Drink getDrink() {
        return drink;
    }
}
