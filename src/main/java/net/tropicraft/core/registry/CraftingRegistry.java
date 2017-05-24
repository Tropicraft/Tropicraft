package net.tropicraft.core.registry;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.enums.TropicraftLogs;

public class CraftingRegistry {

	public static void preInit() {
		addRecipes();
	}

	private static void addRecipes() {
		// Thatch bundle
		createRecipe(true, new ItemStack(BlockRegistry.bundles, 1, 0), new Object[] {
			"XX", "XX",
			'X', Items.REEDS
		});

		// And back
		createRecipe(true, new ItemStack(Items.REEDS, 4), new Object[] {
			"XX", "XX",
			'X', new ItemStack(BlockRegistry.bundles, 1, 1)
		});

		// Bamboo bundle
		createRecipe(true, new ItemStack(BlockRegistry.bundles, 1, 1), new Object[] {
			"XX", "XX",
			'X', ItemRegistry.bambooShoot
		});

		// And back
		createRecipe(true, new ItemStack(ItemRegistry.bambooShoot, 4), new Object[] {
			"XX", "XX",
			'X', new ItemStack(BlockRegistry.bundles, 1, 0)
		});

		// Thatch stairs - reeds
		createRecipe(true, new ItemStack(BlockRegistry.thatchStairs, 1), new Object[] {
			"X ", "XX",
			'X', Items.REEDS
		});

		// Thatch stairs - thatch bundles
		createRecipe(true, new ItemStack(BlockRegistry.thatchStairs, 4), new Object[] {
			"X ", "XX",
			'X', new ItemStack(BlockRegistry.bundles, 1, 0)
		});

		// Bamboo stairs - bamboo shoots
		createRecipe(true, new ItemStack(BlockRegistry.bambooStairs, 1), new Object[] {
			"X ", "XX",
			'X', ItemRegistry.bambooShoot
		});

		// Bamboo stairs - bamboo bundles
		createRecipe(true, new ItemStack(BlockRegistry.bambooStairs, 4), new Object[] {
			"X ", "XX",
			'X', new ItemStack(BlockRegistry.bundles, 1, 1)
		});

		// Chunk stairs
		createRecipe(true, new ItemStack(BlockRegistry.chunkStairs, 4), new Object[] {
			"X  ", "XX ", "XXX",
			'X', BlockRegistry.chunk
		});

		// Bamboo stick
		createRecipe(true, new ItemStack(ItemRegistry.bambooStick, 4), new Object[] {
			"#", "#",
			'#', ItemRegistry.bambooShoot
		});

		// Zircon pickaxe
		createRecipe(true, new ItemStack(ItemRegistry.pickaxeZircon), new Object[] {
			"XXX", " I ", " I ",
			'X', new ItemStack(ItemRegistry.zircon, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Zircon axe
		createRecipe(true, new ItemStack(ItemRegistry.axeZircon), new Object[] {
			"XX", "XI", " I",
			'X', new ItemStack(ItemRegistry.zircon, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Zircon hoe
		createRecipe(true, new ItemStack(ItemRegistry.hoeZircon), new Object[] {
			"XX", " I", " I",
			'X', new ItemStack(ItemRegistry.zircon, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Zircon sword
		createRecipe(true, new ItemStack(ItemRegistry.swordZircon), new Object[] {
			"X", "X", "I",
			'X', new ItemStack(ItemRegistry.zircon, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Zircon shovel
		createRecipe(true, new ItemStack(ItemRegistry.shovelZircon), new Object[] {
			"X", "I", "I",
			'X', new ItemStack(ItemRegistry.zircon, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Eudialyte pickaxe
		createRecipe(true, new ItemStack(ItemRegistry.pickaxeEudialyte), new Object[] {
			"XXX", " I ", " I ",
			'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Eudialyte axe
		createRecipe(true, new ItemStack(ItemRegistry.axeEudialyte), new Object[] {
			"XX", "XI", " I",
			'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Eudialyte hoe
		createRecipe(true, new ItemStack(ItemRegistry.hoeEudialyte), new Object[] {
			"XX", " I", " I",
			'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Eudialyte sword
		createRecipe(true, new ItemStack(ItemRegistry.swordEudialyte), new Object[] {
			"X", "X", "I",
			'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Eudialyte shovel
		createRecipe(true, new ItemStack(ItemRegistry.shovelEudialyte), new Object[] {
			"X", "I", "I",
			'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
			'I', ItemRegistry.bambooStick
		});

		// Bamboo chest
		createRecipe(true, new ItemStack(BlockRegistry.bambooChest, 1), new Object[] {
			"XXX", "X X", "XXX",
			'X', ItemRegistry.bambooShoot
		});

		// Scale helmet
		createRecipe(true, new ItemStack(ItemRegistry.scaleHelmet, 1), new Object[]{
			"XXX", "X X",
			'X', ItemRegistry.scale
		});

		// Scale chestplate
		createRecipe(true, new ItemStack(ItemRegistry.scaleChestplate, 1), new Object[]{
			"X X", "XXX", "XXX",
			'X', ItemRegistry.scale
		});

		// Scale leggings
		createRecipe(true, new ItemStack(ItemRegistry.scaleLeggings, 1), new Object[]{
			"XXX", "X X", "X X",
			'X', ItemRegistry.scale
		});

		// Scale boots
		createRecipe(true, new ItemStack(ItemRegistry.scaleBoots, 1), new Object[]{
			"X X", "X X",
			'X', ItemRegistry.scale
		});

		// Bamboo mug
		createRecipe(true, new ItemStack(ItemRegistry.bambooMug, 1), new Object[]{
			"X X", "X X", "XXX",
			'X', ItemRegistry.bambooShoot
		});

		// Tiki torch (coal) - diagonal recipe
		createRecipe(true, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
			"  Y", " X ", "X  ",
			'Y', Items.COAL,
			'X', ItemRegistry.bambooStick
		});

		// Tiki torch (coal) - vertical recipe
		createRecipe(true, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
			" Y ", " X ", " X ",
			'Y', Items.COAL,
			'X', ItemRegistry.bambooStick
		});

		// Tiki torch (charcoal) - diagonal recipe
		createRecipe(false, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
			"  Y", " X ", "X  ",
			'Y', new ItemStack(Items.COAL, 1, 1),
			'X', ItemRegistry.bambooStick
		});

		// Tiki torch (charcoal) - vertical recipe
		createRecipe(false, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
			" Y ", " X ", " X ",
			'Y', new ItemStack(Items.COAL, 1, 1),
			'X', ItemRegistry.bambooStick
		});

		// Pina Colada
		createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
			ItemRegistry.coconutChunk,
			new ItemStack(BlockRegistry.pineapple),
			ItemRegistry.bambooMug
		});

		// Pina Colada
		createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
			ItemRegistry.coconutChunk,
			ItemRegistry.pineappleCubes,
			ItemRegistry.bambooMug
		});

		// Pina Colada
		createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
			new ItemStack(BlockRegistry.coconut),
			ItemRegistry.pineappleCubes,
			ItemRegistry.bambooMug
		});

		// Pina Colada
		createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
			new ItemStack(BlockRegistry.coconut),
			new ItemStack(BlockRegistry.pineapple),
			ItemRegistry.bambooMug
		});

		// planks -> logs
		int mahogany_meta = TropicraftLogs.getMetaByName("mahogany_log");
		int palm_meta = TropicraftLogs.getMetaByName("palm_log");

		createShapelessRecipe(true, new ItemStack(BlockRegistry.planks, 4, palm_meta), new Object[] {
			new ItemStack(BlockRegistry.logs, 1, palm_meta)
		});

		createShapelessRecipe(true, new ItemStack(BlockRegistry.planks, 4, mahogany_meta), new Object[] {
			new ItemStack(BlockRegistry.logs, 1, mahogany_meta)
		});

		createFullSingleBlockRecipe(BlockRegistry.oreBlock, ItemRegistry.azurite, 0);
		createFullSingleBlockRecipe(BlockRegistry.oreBlock, ItemRegistry.eudialyte, 1);
		createFullSingleBlockRecipe(BlockRegistry.oreBlock, ItemRegistry.zircon, 2);

		createOreRecipe(ItemRegistry.azurite, 0);
		createOreRecipe(ItemRegistry.eudialyte, 1);
		createOreRecipe(ItemRegistry.zircon, 2);

		GameRegistry.addSmelting(ItemRegistry.frogLeg, new ItemStack(ItemRegistry.cookedFrogLeg), 3);
	}

	private static void createFullSingleBlockRecipe(Block out, Item ingredient, int blockDmg) {
		createRecipe(true, new ItemStack(out, 1, blockDmg), new Object[] {
			"%%%", "%%%", "%%%",
			'%', ingredient
		});
	}

	private static void createOreRecipe(Item oreItem, int blockDmg) {
		createShapelessRecipe(true, new ItemStack(oreItem, 9), new Object[] {
			new ItemStack(BlockRegistry.oreBlock, 1, blockDmg)
		});
	}

	//@SideOnly(Side.CLIENT)
	public static void addToEncyclopedia(ItemStack itemstack, Object obj[]) {
		//Tropicraft.encyclopedia.includeRecipe(itemstack, obj);
	}

	public static void createRecipe(boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
		if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			addToEncyclopedia(itemstack, obj);
		}

		GameRegistry.addRecipe(itemstack, obj);
	}

	public static void createRecipe(boolean isServer, boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
		if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			addToEncyclopedia(itemstack, obj);
		}

		GameRegistry.addRecipe(itemstack, obj);
	}

	public static void createShapelessRecipe(boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
		//cannot add shapeless recipes to the encyclopedia yet
		/*if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			addToEncyclopedia(itemstack, obj);
		}*/
		GameRegistry.addShapelessRecipe(itemstack, obj);
	}
}
