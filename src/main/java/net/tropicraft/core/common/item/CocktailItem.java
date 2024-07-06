package net.tropicraft.core.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.tropicraft.core.common.drinks.Cocktail;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipe;
import net.tropicraft.core.common.drinks.MixerRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CocktailItem extends Item {
    public CocktailItem(Drink drink, Properties properties) {
        super(properties.component(TropicraftDataComponents.COCKTAIL, new Cocktail(drink)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        List<Ingredient> ingredients = getIngredients(stack);
        for (Ingredient ingredient : ingredients) {
            tooltip.add(ingredient.getDisplayName());
        }
    }

    public static int getCocktailColor(ItemStack stack) {
        Cocktail cocktail = stack.get(TropicraftDataComponents.COCKTAIL);
        if (cocktail == null) {
            return Cocktail.DEFAULT_COLOR;
        }
        return cocktail.color();
    }

    public static ItemStack makeCocktail(MixerRecipe recipe) {
        ItemStack stack = MixerRecipes.getItemStack(recipe.getCraftingResult());
        Drink drink = recipe.getCraftingResult();
        stack.set(TropicraftDataComponents.COCKTAIL, new Cocktail(
                drink,
                List.of(recipe.getIngredients())
        ));
        return stack;
    }

    public static ItemStack makeCocktail(NonNullList<ItemStack> itemStacks) {
        // TODO fixme this is so ugly ugh
        ItemStack stack = new ItemStack(TropicraftItems.COCKTAILS.get(Drink.COCKTAIL).get());
        stack.set(TropicraftDataComponents.COCKTAIL, new Cocktail(
                Drink.COCKTAIL,
                itemStacks.stream()
                        .flatMap(item -> Ingredient.listIngredients(item).stream())
                        .sorted()
                        .toList()
        ));
        return stack;
    }

    public static List<Ingredient> getIngredients(ItemStack stack) {
        Cocktail cocktail = stack.get(TropicraftDataComponents.COCKTAIL);
        if (cocktail == null) {
            return List.of();
        }
        return cocktail.ingredients();
    }

    @Nullable
    public static Drink getDrink(ItemStack stack) {
        Cocktail cocktail = stack.get(TropicraftDataComponents.COCKTAIL);
        return cocktail != null ? cocktail.drink() : null;
    }

    public static boolean isDrink(ItemStack itemStack) {
        return getDrink(itemStack) != null;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public ItemStack onFoodEaten(ItemStack itemstack, Level world, Player player) {
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);

        for (Ingredient ingredient : getIngredients(itemstack)) {
            ingredient.onDrink(player);
        }

        Drink drink = getDrink(itemstack);

        if (drink != null) {
            drink.onDrink(player);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemstack);
        }

        return new ItemStack(TropicraftItems.BAMBOO_MUG.get());
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof Player player) {
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
    public Component getName(ItemStack stack) {
        Drink drink = getDrink(stack);
        if (drink != null) {
            return super.getName(stack).copy().withStyle(drink.textFormatting).withStyle(ChatFormatting.BOLD);
        }
        return super.getName(stack);
    }
}
