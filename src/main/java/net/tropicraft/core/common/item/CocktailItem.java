package net.tropicraft.core.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
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
import net.tropicraft.core.common.drinks.action.TropicraftDrinks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CocktailItem extends Item {
    public CocktailItem(Properties properties) {
        super(properties.component(TropicraftDataComponents.COCKTAIL, Cocktail.EMPTY));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        List<Ingredient> ingredients = getCocktail(stack).ingredients();
        for (Ingredient ingredient : ingredients) {
            tooltip.add(ingredient.getDisplayName());
        }
    }

    public static Cocktail getCocktail(ItemStack itemStack) {
        return itemStack.getOrDefault(TropicraftDataComponents.COCKTAIL, Cocktail.EMPTY);
    }

    public static ItemStack makeCocktail(Cocktail cocktail) {
        ItemStack stack = new ItemStack(TropicraftItems.COCKTAIL.get());
        stack.set(TropicraftDataComponents.COCKTAIL, cocktail);
        return stack;
    }

    public static ItemStack makeDrink(Holder<Drink> drink) {
        return makeCocktail(Cocktail.ofDrink(drink));
    }

    public static ItemStack makeCocktail(HolderLookup.Provider registries, MixerRecipe recipe) {
        Optional<Holder<Drink>> drink = registries.holder(recipe.getCraftingResult()).map(Function.identity());
        if (drink.isEmpty()) {
            return makeCocktail(Cocktail.ofIngredients(List.of(recipe.getIngredients())));
        }
        return makeDrink(drink.get());
    }

    public static ItemStack makeCocktail(NonNullList<ItemStack> itemStacks) {
        return makeCocktail(Cocktail.ofIngredients(
                itemStacks.stream()
                        .flatMap(item -> Ingredient.listIngredients(item).stream())
                        .sorted()
                        .toList()
        ));
    }

    @Nullable
    public static Holder<Drink> getDrink(ItemStack itemStack) {
        return getCocktail(itemStack).drink().orElse(null);
    }

    public static boolean hasDrink(ItemStack itemStack, ResourceKey<Drink> drink) {
        Holder<Drink> actualDrink = getDrink(itemStack);
        return actualDrink != null && actualDrink.is(drink);
    }

    public static boolean isDrink(ItemStack itemStack) {
        return !getCocktail(itemStack).equals(Cocktail.EMPTY);
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

        if (player instanceof ServerPlayer serverPlayer) {
            Cocktail cocktail = itemstack.get(TropicraftDataComponents.COCKTAIL);
            if (cocktail != null) {
                cocktail.onDrink(serverPlayer);
            }
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemstack);
        }

        return new ItemStack(TropicraftItems.BAMBOO_MUG.get());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            onFoodEaten(stack, level, player);

            if (level.isRainingAt(player.blockPosition()) && hasDrink(stack, TropicraftDrinks.PINA_COLADA)) {
                // TODO 1.17 advancements player.addStat(AchievementRegistry.drinkPinaColada);
            }
            return player.getAbilities().instabuild ? stack : new ItemStack(TropicraftItems.BAMBOO_MUG.get());
        }

        return new ItemStack(TropicraftItems.BAMBOO_MUG.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if (!isDrink(stack)) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }

        playerIn.startUsingItem(hand);

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        Holder<Drink> drink = getDrink(stack);
        if (drink != null) {
            return ComponentUtils.mergeStyles(drink.value().name().copy(), Style.EMPTY.withBold(true));
        }
        return super.getName(stack);
    }
}
