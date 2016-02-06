package net.tropicraft.drinks;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class Ingredient implements Comparable<Ingredient> {

    public static final Ingredient[] ingredientsList = new Ingredient[128];
    public static final Ingredient sugar = new Ingredient(0, new ItemStack(Items.sugar), false, 0xffffff, 0.1f).addAction(new DrinkActionFood(1, 0.1f));
//  public static final Ingredient lemonJuice = new Ingredient(1, new ItemStack(TropicraftMod.lemonJuice), true, 0xffff00).addAction(new DrinkActionFood(2, 0.2f));
//  public static final Ingredient limeJuice = new Ingredient(2, new ItemStack(TropicraftMod.limeJuice), true, 0x7fff00).addAction(new DrinkActionFood(2, 0.2f));
//  public static final Ingredient orangeJuice = new Ingredient(3, new ItemStack(TropicraftMod.orangeJuice), true, 0xffa500).addAction(new DrinkActionFood(3, 0.2f));
//  public static final Ingredient grapefruitJuice = new Ingredient(4, new ItemStack(TropicraftMod.grapefruitJuice), true, 0xff6347).addAction(new DrinkActionFood(4, 0.2f));
    public static final Ingredient lemon = new Ingredient(5, new ItemStack(TCItemRegistry.lemon), true, 0xffff00).addAction(new DrinkActionFood(2, 0.2f));
    public static final Ingredient lime = new Ingredient(6, new ItemStack(TCItemRegistry.lime), true, 0x7fff00).addAction(new DrinkActionFood(2, 0.2f));
    public static final Ingredient orange = new Ingredient(7, new ItemStack(TCItemRegistry.orange), true, 0xffa500).addAction(new DrinkActionFood(3, 0.2f));
    public static final Ingredient grapefruit = new Ingredient(8, new ItemStack(TCItemRegistry.grapefruit), true, 0xff6347).addAction(new DrinkActionFood(4, 0.2f));
    public static final Ingredient pineapple = new Ingredient(9, new ItemStack(TCBlockRegistry.pineapple, 1, 0), false, 0xeeff00).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient pineappleCubes = new Ingredient(10, new ItemStack(TCItemRegistry.pineappleCubes), false, 0xeeff00, 0.1f).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient coconut = new Ingredient(11, new ItemStack(TCBlockRegistry.coconut), true, 0xefefef).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient coconutChunk = new Ingredient(12, new ItemStack(TCItemRegistry.coconutChunk), true, 0xefefef/*, 0.1f*/).addAction(new DrinkActionFood(1, 0.1f));
    public static final Ingredient sugarcane = new Ingredient(13, new ItemStack(Items.reeds), false, 0xb1ff6b, 0.1f);
    public static final Ingredient roastedCoffeeBean = new Ingredient(14, new ItemStack(TCItemRegistry.coffeeBean, 1, 1), false, 0x68442c, 0.95f).addAction(new DrinkActionFood(4, 0.2f)).addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
    public static final Ingredient waterBucket = new Ingredient(15, new ItemStack(TCItemRegistry.bucketTropicsWater), true, 0xffffff);
    public static final Ingredient milkBucket = new Ingredient(16, new ItemStack(Items.milk_bucket), false, 0xffffff, 0.1f).addAction(new DrinkActionFood(2, 0.2f));
    public static final Ingredient cocoaBean = new Ingredient(17, new ItemStack(Items.dye, 1, 3), false, 0x805A3E, 0.95f).addAction(new DrinkActionFood(4, 0.2f));

    /**
     * An ItemStack representing the item this ingredient is
     */
    private ItemStack ingredientItem;
    
    /**
     * Render color of this Ingredient in a mug
     */
    private int color;
    
    /**
     * Id of this ingredient
     */
    public int ingredientId;
    
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
    private List<DrinkAction> actions = new LinkedList<DrinkAction>();
    
    public Ingredient(int id, ItemStack ingredient, boolean primary, int color) {
        if (ingredientsList[id] != null) {
            throw new IllegalArgumentException("Ingredient Id slot " + id + " already occupied by " + ingredientsList[id].ingredientItem.getUnlocalizedName() + "!");
        }
        
        this.ingredientId = id;
        this.ingredientItem = ingredient;
        this.primary = primary;
        this.color = color;
        ingredientsList[id] = this;
    }
    
    public Ingredient(int id, ItemStack ingredient, boolean primary, int color, float alpha) {
        this(id, ingredient, primary, color);
        this.alpha = alpha;
    }
    
    /**
     * Adds an action to be performed when a cocktail containing this ingredient is ingested.
     * @param action the action to register
     * @return this Ingredient object
     */
    public Ingredient addAction(DrinkAction action) {
        this.actions.add(action);
        return this;
    }
    
    /**
     * Returns full ItemStack of ingredient
     * @return full ItemStack of ingredient
     */
    public ItemStack getIngredient() {
        return this.ingredientItem;
    }
    
    /**
     * Returns the ingredient as an Item
     * @return ingredient Item
     */
    public Item getIngredientItem() {
        return this.ingredientItem.getItem();
    }
    
    /**
     * Getter for render color
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
        return (this.ingredientId < other.ingredientId) ? -1 : ((this.ingredientId == other.ingredientId) ? 0 : 1);
    }
    
    public void onDrink(EntityPlayer player) {
        for (DrinkAction action: actions) {
            action.onDrink(player);
        }
    }
}
