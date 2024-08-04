package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.drinks.action.DrinkAction;
import net.tropicraft.core.common.drinks.action.FoodDrinkAction;
import net.tropicraft.core.common.drinks.action.PotionDrinkAction;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class Ingredient implements Comparable<Ingredient> {

    // TODO this should be a registry
    public static final Ingredient[] ingredientsList = new Ingredient[24];
    public static final Ingredient sugar = new Ingredient(0, Items.SUGAR.builtInRegistryHolder(), 0xffffff, 0.1f).addAction(new FoodDrinkAction(1, 0.1f));
    public static final Ingredient lemon = new Ingredient(5, TropicraftItems.LEMON, 0xffff00).addAction(new FoodDrinkAction(2, 0.2f));
    public static final Ingredient lime = new Ingredient(6, TropicraftItems.LIME, 0x7fff00).addAction(new FoodDrinkAction(2, 0.2f));
    public static final Ingredient orange = new Ingredient(7, TropicraftItems.ORANGE, 0xffa500).addAction(new FoodDrinkAction(3, 0.2f));
    public static final Ingredient grapefruit = new Ingredient(8, TropicraftItems.GRAPEFRUIT, 0xff6347).addAction(new FoodDrinkAction(4, 0.2f));
    public static final Ingredient pineapple = new Ingredient(9, TropicraftBlocks.PINEAPPLE, 0xeeff00).addAction(new FoodDrinkAction(1, 0.1f));
    public static final Ingredient pineappleCubes = new Ingredient(10, TropicraftItems.PINEAPPLE_CUBES, 0xeeff00, 0.1f).addAction(new FoodDrinkAction(1, 0.1f));
    public static final Ingredient coconut = new Ingredient(11, TropicraftBlocks.COCONUT, 0xefefef).addAction(new FoodDrinkAction(1, 0.1f));
    public static final Ingredient coconutChunk = new Ingredient(12, TropicraftItems.COCONUT_CHUNK, 0xefefef/*, 0.1f*/).addAction(new FoodDrinkAction(1, 0.1f));
    public static final Ingredient sugarcane = new Ingredient(13, Items.SUGAR_CANE.builtInRegistryHolder(), 0xb1ff6b, 0.1f);
    public static final Ingredient roastedCoffeeBean = new Ingredient(14, TropicraftItems.ROASTED_COFFEE_BEAN, 0x68442c, 0.95f).addAction(new FoodDrinkAction(4, 0.2f)).addAction(new PotionDrinkAction(MobEffects.MOVEMENT_SPEED, 5, 1));
    public static final Ingredient waterBucket = new Ingredient(15, Items.WATER_BUCKET.builtInRegistryHolder(), 0xffffff);
    public static final Ingredient milkBucket = new Ingredient(16, Items.MILK_BUCKET.builtInRegistryHolder(), 0xffffff, 0.1f).addAction(new FoodDrinkAction(2, 0.2f));
    public static final Ingredient cocoaBean = new Ingredient(17, Items.COCOA_BEANS.builtInRegistryHolder(), 0x805A3E, 0.95f).addAction(new FoodDrinkAction(4, 0.2f));
    public static final Ingredient passionfruit = new Ingredient(18, TropicraftItems.PASSIONFRUIT, 0x690b2d).addAction(new FoodDrinkAction(4, 0.2f));
    public static final Ingredient jocote = new Ingredient(19, TropicraftItems.JOCOTE, 0xc1cd02).addAction(new FoodDrinkAction(4, 0.2f));
    public static final Ingredient papaya = new Ingredient(20, TropicraftItems.PAPAYA, 0x3fbf3f).addAction(new FoodDrinkAction(4, 0.2f));

    public static final Codec<Ingredient> CODEC = ExtraCodecs.idResolverCodec(value -> value.id, id -> ingredientsList[id], -1);
    public static final StreamCodec<ByteBuf, Ingredient> STREAM_CODEC = ByteBufCodecs.idMapper(id -> ingredientsList[id], value -> value.id);

    /**
     * An ItemStack representing the item this ingredient is
     */
    private final Holder<? extends ItemLike> item;

    /**
     * Render color of this Ingredient in a mug
     */
    private final int color;

    /**
     * Id of this ingredient
     */
    public final int id;

    /**
     * Alpha channel used in color blending. Typically 1 for primary ingredients and lower for additives.
     */
    private float alpha = 1.0f;

    /**
     * DrinkActions to trigger when a cocktail containing this ingredient is ingested.
     */
    private final List<DrinkAction> actions = new LinkedList<>();

    public Ingredient(int id, Holder<? extends ItemLike> ingredientItem, int color) {
        if (ingredientsList[id] != null) {
            throw new IllegalArgumentException("Ingredient Id slot " + id + " already occupied by " + ingredientsList[id] + "!");
        }

        this.id = id;
        item = ingredientItem;
        this.color = color;
        ingredientsList[id] = this;
    }

    public Ingredient(int id, Holder<? extends ItemLike> ingredientItem, int color, float alpha) {
        this(id, ingredientItem, color);
        this.alpha = alpha;
    }

    /**
     * Adds an action to be performed when a cocktail containing this ingredient is ingested.
     *
     * @param action the action to register
     * @return this Ingredient object
     */
    public Ingredient addAction(DrinkAction action) {
        actions.add(action);
        return this;
    }

    /**
     * Returns the ingredient as an Item
     *
     * @return ingredient Item
     */
    public Item getIngredientItem() {
        return item.value().asItem();
    }

    /**
     * Getter for render color
     *
     * @return render color in mug
     */
    public int getColor() {
        return color;
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public int compareTo(Ingredient other) {
        return Integer.compare(id, other.id);
    }

    public void onDrink(ServerPlayer player) {
        for (DrinkAction action : actions) {
            action.onDrink(player);
        }
    }

    @Nullable
    public static Ingredient findMatchingIngredient(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        for (Ingredient ingredient : Ingredient.ingredientsList) {
            if (ingredient != null && ingredient.matches(stack)) {
                return ingredient;
            }
        }
        return null;
    }

    public boolean matches(ItemStack stack) {
        return stack.is(item.value().asItem());
    }

    public static List<Ingredient> listIngredients(ItemStack stack) {
        List<Ingredient> ingredients = CocktailItem.getCocktail(stack).ingredients();
        if (!ingredients.isEmpty()) {
            return ingredients;
        }

        Ingredient matchingIngredient = findMatchingIngredient(stack);
        return matchingIngredient != null ? List.of(matchingIngredient) : List.of();
    }

    public Component getDisplayName() {
        return ComponentUtils.mergeStyles(new ItemStack(getIngredientItem()).getHoverName().copy(), Style.EMPTY.withColor(color));
    }
}
