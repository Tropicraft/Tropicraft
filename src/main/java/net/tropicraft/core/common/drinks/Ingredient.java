package net.tropicraft.core.common.drinks;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Ingredient implements Comparable<Ingredient> {

    // TODO this should be a registry
    public static final Ingredient[] ingredientsList = new Ingredient[24];
    public static final Ingredient sugar = new Ingredient(0, Items.SUGAR.builtInRegistryHolder(), false, 0xffffff, 0.1f).addAction(new DrinkActionFood(1, 0.1f));
    //  public static final Ingredient lemonJuice = new Ingredient(1, new ItemStack(TropicraftMod.lemonJuice), true, 0xffff00).addAction(new DrinkActionFood(2, 0.2f));
    //  public static final Ingredient limeJuice = new Ingredient(2, new ItemStack(TropicraftMod.limeJuice), true, 0x7fff00).addAction(new DrinkActionFood(2, 0.2f));
    //  public static final Ingredient orangeJuice = new Ingredient(3, new ItemStack(TropicraftMod.orangeJuice), true, 0xffa500).addAction(new DrinkActionFood(3, 0.2f));
    //  public static final Ingredient grapefruitJuice = new Ingredient(4, new ItemStack(TropicraftMod.grapefruitJuice), true, 0xff6347).addAction(new DrinkActionFood(4, 0.2f));
    public static final Ingredient lemon = new Ingredient(5, TropicraftItems.LEMON, true, 0xffff00).addAction(new DrinkActionFood(2, 0.2f));
    public static final Ingredient lime = new Ingredient(6, TropicraftItems.LIME, true, 0x7fff00).addAction(new DrinkActionFood(2, 0.2f));
    public static final Ingredient orange = new Ingredient(7, TropicraftItems.ORANGE, true, 0xffa500).addAction(new DrinkActionFood(3, 0.2f));
    public static final Ingredient grapefruit = new Ingredient(8, TropicraftItems.GRAPEFRUIT, true, 0xff6347).addAction(new DrinkActionFood(4, 0.2f));
    public static final Ingredient pineapple = new Ingredient(9, TropicraftBlocks.PINEAPPLE, false, 0xeeff00).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient pineappleCubes = new Ingredient(10, TropicraftItems.PINEAPPLE_CUBES, false, 0xeeff00, 0.1f).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient coconut = new Ingredient(11, TropicraftBlocks.COCONUT, false, 0xefefef).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient coconutChunk = new Ingredient(12, TropicraftItems.COCONUT_CHUNK, false, 0xefefef/*, 0.1f*/).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient sugarcane = new Ingredient(13, Items.SUGAR_CANE.builtInRegistryHolder(), false, 0xb1ff6b, 0.1f);
    public static final Ingredient roastedCoffeeBean = new Ingredient(14, TropicraftItems.ROASTED_COFFEE_BEAN, false, 0x68442c, 0.95f).addAction(new DrinkActionFood(4, 0.2f)).addAction(new DrinkActionPotion(MobEffects.MOVEMENT_SPEED, 5, 1));
    public static final Ingredient waterBucket = new Ingredient(15, Items.WATER_BUCKET.builtInRegistryHolder(), false, 0xffffff);
    public static final Ingredient milkBucket = new Ingredient(16, Items.MILK_BUCKET.builtInRegistryHolder(), false, 0xffffff, 0.1f).addAction(new DrinkActionFood(2, 0.2f));
    public static final Ingredient cocoaBean = new Ingredient(17, Items.COCOA_BEANS.builtInRegistryHolder(), false, 0x805A3E, 0.95f).addAction(new DrinkActionFood(4, 0.2f));
    public static final Ingredient passionfruit = new Ingredient(18, TropicraftItems.PASSIONFRUIT, true, 0x690b2d).addAction(new DrinkActionFood(4, 0.2f));
    public static final Ingredient jocote = new Ingredient(19, TropicraftItems.JOCOTE, true, 0xc1cd02).addAction(new DrinkActionFood(4, 0.2f));

    public static final Codec<Ingredient> CODEC = ExtraCodecs.idResolverCodec(value -> value.id, id -> ingredientsList[id], -1);
    public static final StreamCodec<ByteBuf, Ingredient> STREAM_CODEC = ByteBufCodecs.idMapper(id -> ingredientsList[id], value -> value.id);

    /**
     * An ItemStack representing the item this ingredient is
     */
    @Nonnull
    private Holder<? extends ItemLike> item;

    /**
     * Render color of this Ingredient in a mug
     */
    private int color;

    /**
     * Id of this ingredient
     */
    public int id;

    /**
     * Whether this ingredient determines the base color of the resulting drink. Primary ingredients cannot be mixed,
     * as opposed to additives.
     */
    private boolean primary;

    /**
     * Alpha channel used in color blending. Typically 1 for primary ingredients and lower for additives.
     */
    private float alpha = 1f;

    /**
     * DrinkActions to trigger when a cocktail containing this ingredient is ingested.
     */
    private List<DrinkAction> actions = new LinkedList<>();

    public Ingredient() {
        this.item = Items.AIR.builtInRegistryHolder();
    }

    public Ingredient(int id, @Nonnull Holder<? extends ItemLike> ingredientItem, boolean primary, int color) {
        if (ingredientsList[id] != null) {
            throw new IllegalArgumentException("Ingredient Id slot " + id + " already occupied by " + ingredientsList[id] + "!");
        }

        this.id = id;
        this.item = ingredientItem;
        this.primary = primary;
        this.color = color;
        ingredientsList[id] = this;
    }

    public Ingredient(int id, @Nonnull Holder<? extends ItemLike> ingredientItem, boolean primary, int color, float alpha) {
        this(id, ingredientItem, primary, color);
        this.alpha = alpha;
    }

    /**
     * Adds an action to be performed when a cocktail containing this ingredient is ingested.
     *
     * @param action the action to register
     * @return this Ingredient object
     */
    public Ingredient addAction(DrinkAction action) {
        this.actions.add(action);
        return this;
    }

    /**
     * Returns the ingredient as an Item
     *
     * @return ingredient Item
     */
    public Item getIngredientItem() {
        return this.item.value().asItem();
    }

    /**
     * Getter for render color
     *
     * @return render color in mug
     */
    public int getColor() {
        return this.color;
    }

    public boolean isPrimary() {
        return primary;
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public int compareTo(Ingredient other) {
        return Integer.compare(id, other.id);
    }

    public void onDrink(Player player) {
        for (final DrinkAction action : actions) {
            action.onDrink(player);
        }
    }

    public static Ingredient findMatchingIngredient(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) return null;
        for (Ingredient ingredient : Ingredient.ingredientsList) {
            if (ingredient == null) {
                continue;
            }
            if (ingredient.getIngredientItem() == stack.getItem()) {
                return ingredient;
            }
        }

        return null;
    }

    public static List<Ingredient> listIngredients(ItemStack stack) {
        List<Ingredient> is = new ArrayList<>();

        if (!stack.isEmpty() && Drink.isDrink(stack.getItem())) {
            is.addAll(CocktailItem.getIngredients(stack));
        } else if (!stack.isEmpty()) {
            is.add(findMatchingIngredient(stack));
        }

        return is;
    }

    public Component getDisplayName() {
        return new ItemStack(getIngredientItem()).getHoverName();
    }
}
