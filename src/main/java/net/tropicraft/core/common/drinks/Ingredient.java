package net.tropicraft.core.common.drinks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class Ingredient implements Comparable<Ingredient> {

	public static final Ingredient[] ingredientsList = new Ingredient[24];
	public static final Ingredient sugar = new Ingredient(0, new ItemStack(Items.SUGAR), false, 0xffffff, 0.1f).addAction(new DrinkActionFood(1, 0.1f));
	//  public static final Ingredient lemonJuice = new Ingredient(1, new ItemStack(TropicraftMod.lemonJuice), true, 0xffff00).addAction(new DrinkActionFood(2, 0.2f));
	//  public static final Ingredient limeJuice = new Ingredient(2, new ItemStack(TropicraftMod.limeJuice), true, 0x7fff00).addAction(new DrinkActionFood(2, 0.2f));
	//  public static final Ingredient orangeJuice = new Ingredient(3, new ItemStack(TropicraftMod.orangeJuice), true, 0xffa500).addAction(new DrinkActionFood(3, 0.2f));
	//  public static final Ingredient grapefruitJuice = new Ingredient(4, new ItemStack(TropicraftMod.grapefruitJuice), true, 0xff6347).addAction(new DrinkActionFood(4, 0.2f));
	public static final Ingredient lemon = new Ingredient(5, new ItemStack(ItemRegistry.lemon), true, 0xffff00).addAction(new DrinkActionFood(2, 0.2f));
	public static final Ingredient lime = new Ingredient(6, new ItemStack(ItemRegistry.lime), true, 0x7fff00).addAction(new DrinkActionFood(2, 0.2f));
	public static final Ingredient orange = new Ingredient(7, new ItemStack(ItemRegistry.orange), true, 0xffa500).addAction(new DrinkActionFood(3, 0.2f));
	public static final Ingredient grapefruit = new Ingredient(8, new ItemStack(ItemRegistry.grapefruit), true, 0xff6347).addAction(new DrinkActionFood(4, 0.2f));
	public static final Ingredient pineapple = new Ingredient(9, new ItemStack(BlockRegistry.pineapple, 1, 0), false, 0xeeff00).addAction(new DrinkActionFood(1, 0.1f));
	public static final Ingredient pineappleCubes = new Ingredient(10, new ItemStack(ItemRegistry.pineappleCubes), false, 0xeeff00, 0.1f).addAction(new DrinkActionFood(1, 0.1f));
	public static final Ingredient coconut = new Ingredient(11, new ItemStack(BlockRegistry.coconut), true, 0xefefef).addAction(new DrinkActionFood(1, 0.1f));
	public static final Ingredient coconutChunk = new Ingredient(12, new ItemStack(ItemRegistry.coconutChunk), true, 0xefefef/*, 0.1f*/).addAction(new DrinkActionFood(1, 0.1f));
	public static final Ingredient sugarcane = new Ingredient(13, new ItemStack(Items.REEDS), false, 0xb1ff6b, 0.1f);
	//public static final Ingredient roastedCoffeeBean = new Ingredient(14, new ItemStack(TCItemRegistry.coffeeBean, 1, 1), false, 0x68442c, 0.95f).addAction(new DrinkActionFood(4, 0.2f)).addAction(new DrinkActionPotion(Potion.moveSpeed.id, 5, 1));
	public static final Ingredient waterBucket = new Ingredient(15, new ItemStack(ItemRegistry.tropicsWaterBucket), true, 0xffffff);
	public static final Ingredient milkBucket = new Ingredient(16, new ItemStack(Items.MILK_BUCKET), false, 0xffffff, 0.1f).addAction(new DrinkActionFood(2, 0.2f));
	public static final Ingredient cocoaBean = new Ingredient(17, new ItemStack(Items.DYE, 1, 3), false, 0x805A3E, 0.95f).addAction(new DrinkActionFood(4, 0.2f));

	/**
	 * An ItemStack representing the item this ingredient is
	 */
	private ItemStack itemStack;

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
	private List<DrinkAction> actions = new LinkedList<DrinkAction>();

	public Ingredient() {

	}

	public Ingredient(int id, ItemStack ingredientItemStack, boolean primary, int color) {
		if (ingredientsList[id] != null) {
			throw new IllegalArgumentException("Ingredient Id slot " + id + " already occupied by " + ingredientsList[id].itemStack.getUnlocalizedName() + "!");
		}

		this.id = id;
		this.itemStack = ingredientItemStack;
		this.primary = primary;
		this.color = color;
		ingredientsList[id] = this;
	}

	public Ingredient(int id, ItemStack ingredientItemStack, boolean primary, int color, float alpha) {
		this(id, ingredientItemStack, primary, color);
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
        return this.itemStack;
    }
    
    /**
     * Returns the ingredient as an Item
     * @return ingredient Item
     */
    public Item getIngredientItem() {
        return this.itemStack.getItem();
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
        return (this.id < other.id) ? -1 : ((this.id == other.id) ? 0 : 1);
    }
    
    public void onDrink(EntityPlayer player) {
        for (DrinkAction action: actions) {
            action.onDrink(player);
        }
    }
    
    public static Ingredient findMatchingIngredient(ItemStack stack) {
        for (Ingredient ingredient: Ingredient.ingredientsList) {
            if (ingredient == null) {
                continue;
            }
            if (ItemStack.areItemStacksEqual(ingredient.getIngredient(), stack)) {
                return ingredient;
            }
        }
        
        return null;
    }
    
    public static List<Ingredient> listIngredients(ItemStack stack) {
        List<Ingredient> is = new ArrayList<Ingredient>();
        
        if (stack != null && stack.getItem() != null && stack.getItem() == ItemRegistry.cocktail) {
            for (Ingredient ingredient: ItemCocktail.getIngredients(stack)) {
                is.add(ingredient);
            }
        } else {
            Ingredient i = findMatchingIngredient(stack);
            is.add(i);
        }
        
        return is;
    }

}
