package net.tropicraft.core.registry;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Info;
import net.tropicraft.Names;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.placeable.EntityBambooItemFrame;
import net.tropicraft.core.common.item.ItemBambooItemFrame;
import net.tropicraft.core.common.item.ItemChair;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.common.item.ItemCoconutBomb;
import net.tropicraft.core.common.item.ItemDagger;
import net.tropicraft.core.common.item.ItemEncyclopediaTropica;
import net.tropicraft.core.common.item.ItemFertilizer;
import net.tropicraft.core.common.item.ItemFishBucket;
import net.tropicraft.core.common.item.ItemMobEgg;
import net.tropicraft.core.common.item.ItemMusicDisc;
import net.tropicraft.core.common.item.ItemPortalEnchanter;
import net.tropicraft.core.common.item.ItemShell;
import net.tropicraft.core.common.item.ItemTropicraft;
import net.tropicraft.core.common.item.ItemTropicraftAxe;
import net.tropicraft.core.common.item.ItemTropicraftBlockSpecial;
import net.tropicraft.core.common.item.ItemTropicraftFood;
import net.tropicraft.core.common.item.ItemTropicraftPickaxe;
import net.tropicraft.core.common.item.ItemTropicsOre;
import net.tropicraft.core.common.item.ItemUmbrella;
import net.tropicraft.core.common.item.ItemWaterWand;
import net.tropicraft.core.common.item.armor.ItemScaleArmor;

public class ItemRegistry extends TropicraftRegistry {

	// Ore gems
	public static Item azurite, eudialyte, zircon;

	// Yummy delicious fruits
	public static Item grapefruit, lemon, lime, orange;

	// Foodstuffs
	public static Item freshMarlin;
	public static Item searedMarlin;
	public static Item coconutChunk;
	public static Item pineappleCubes;

	// Tool materials
	public static ToolMaterial materialZirconTools = EnumHelper.addToolMaterial("zircon", 1, 200, 4.5F, 1.0F, 14);
	public static ToolMaterial materialEudialyteTools = EnumHelper.addToolMaterial("eudialyte", 2, 750, 6.0F, 2.0F, 14);
	public static ToolMaterial materialZirconiumTools = EnumHelper.addToolMaterial("zirconium", 3, 1800, 8.5F, 3.0F, 10);
	public static ToolMaterial materialBambooTools = EnumHelper.addToolMaterial("bamboo", 1, 110, 1.2F, 1F, 6);

	// Tools and weapons
	public static Item hoeEudialyte;
	public static Item hoeZircon;
	public static Item pickaxeEudialyte;
	public static Item pickaxeZircon;
	public static Item shovelEudialyte;
	public static Item shovelZircon;
	public static Item axeEudialyte;
	public static Item axeZircon;
	public static Item swordEudialyte;
	public static Item swordZircon;
	public static Item bambooSpear;
	public static Item dagger;
	//TODO public static Item leafBall;
	public static Item coconutBomb;
	public static Item fishingNet;

	// Bamboo n stuff
	public static Item bambooShoot;
	public static Item bambooStick;
	public static Item bambooMug;

	// Buckets
	public static Item tropicsWaterBucket;
	public static Item fishBucket;

	// Music
	public static Item recordBuriedTreasure;
	public static Item recordEasternIsles;
	public static Item recordLowTide;
	public static Item recordSummering;
	public static Item recordTheTribe;
	public static Item recordTradeWinds;

	// Frogs
	public static Item frogLeg;
	public static Item cookedFrogLeg;
	public static Item poisonFrogSkin;

	public static Item scale;

	// Armor	
	public static final ArmorMaterial materialScaleArmor = EnumHelper.addArmorMaterial("scale", "scale", 18, new int[]{2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 9.0F);
	public static Item scaleBoots;
	public static Item scaleLeggings;
	public static Item scaleChestplate;
	public static Item scaleHelmet;

	public static Item chair;
	public static Item umbrella;

	public static Item portalEnchanter;

	public static Item shellFrox;
	public static Item shellPab;
	public static Item shellRube;
	public static Item shellSolo;
	public static Item shellStarfish;
	public static Item shellTurtle;

	public static Item cocktail;

	public static Item whitePearl;
	public static Item blackPearl;

	public static Item fertilizer;

	public static Item encyclopedia;

	// Decorations
	public static Item flowerPot;
	public static Item bambooDoor;
	public static Item bambooItemFrame;

	public static Item waterWand;

	public static Item seaUrchinRoe;
	public static Item mobEgg;

	public static void preInit() {
		recordBuriedTreasure = registerItem(new ItemMusicDisc("buried_treasure", "Punchaface", SoundRegistry.get("buried_treasure")), "buried_treasure");
		recordEasternIsles = registerItem(new ItemMusicDisc("eastern_isles", "Frox", SoundRegistry.get("eastern_isles")), "eastern_isles");
		recordSummering = registerItem(new ItemMusicDisc("summering", "Billy Christiansen", SoundRegistry.get("summering")), "summering");
		recordTheTribe = registerItem(new ItemMusicDisc("the_tribe", "Emile Van Krieken", SoundRegistry.get("the_tribe")), "the_tribe");
		recordLowTide = registerItem(new ItemMusicDisc("low_tide", "Punchaface", SoundRegistry.get("low_tide")), "low_tide");
		recordTradeWinds = registerItem(new ItemMusicDisc("trade_winds", "Frox", SoundRegistry.get("trade_winds")), "trade_winds");

		azurite = registerItem(new ItemTropicsOre(), "azurite");
		eudialyte = registerItem(new ItemTropicsOre(), "eudialyte");
		zircon = registerItem(new ItemTropicsOre(), "zircon");

		grapefruit = registerItem(new ItemTropicraftFood(2, 0.2F), "grapefruit");
		lemon = registerItem(new ItemTropicraftFood(2, 0.2F), "lemon");
		lime = registerItem(new ItemTropicraftFood(2, 0.2F), "lime");
		orange = registerItem(new ItemTropicraftFood(2, 0.2F), "orange");

		hoeEudialyte = registerItem(new ItemHoe(materialEudialyteTools), "hoe_eudialyte");
		hoeZircon = registerItem(new ItemHoe(materialZirconTools), "hoe_zircon");
		pickaxeEudialyte = registerItem(new ItemTropicraftPickaxe(materialEudialyteTools), "pickaxe_eudialyte");
		pickaxeZircon = registerItem(new ItemTropicraftPickaxe(materialZirconTools), "pickaxe_zircon");
		shovelEudialyte = registerItem(new ItemSpade(materialEudialyteTools), "shovel_eudialyte");
		shovelZircon = registerItem(new ItemSpade(materialZirconTools), "shovel_zircon");
		axeEudialyte = registerItem(new ItemTropicraftAxe(materialEudialyteTools, 6.0F, -3.1F), "axe_eudialyte");
		axeZircon = registerItem(new ItemTropicraftAxe(materialZirconTools, 6.0F, -3.2F), "axe_zircon");
		swordEudialyte = registerItem(new ItemSword(materialEudialyteTools), "sword_eudialyte");
		swordZircon = registerItem(new ItemSword(materialZirconTools), "sword_zircon");
		
		fishingNet = registerItem(new ItemTropicraft(), "fishing_net");

		bambooShoot = registerItem(new ItemBlockSpecial(BlockRegistry.bambooShoot), "bamboo_shoots");
		bambooStick = registerItem(new ItemTropicraft(), "bamboo_stick");
		bambooMug = registerItem(new ItemTropicraft().setMaxStackSize(16), "bamboo_mug");

		freshMarlin = registerItem(new ItemTropicraftFood(2, 0.3F), "fresh_marlin");
		searedMarlin = registerItem(new ItemTropicraftFood(8, 0.65F), "seared_marlin");

		tropicsWaterBucket = registerItem((new ItemBucket(BlockRegistry.tropicsWater)).setContainerItem(Items.BUCKET), "tropics_water_bucket");
		fishBucket = registerItem(new ItemFishBucket(), "fish_bucket");
		
		coconutChunk = registerItem(new ItemTropicraftFood(1, 0.1F), "coconut_chunk");
		pineappleCubes = registerItem(new ItemTropicraftFood(1, 0.1F), "pineapple_cubes");

		frogLeg = registerItem(new ItemTropicraft().setMaxStackSize(64), "frog_leg");
		cookedFrogLeg = registerItem(new ItemTropicraftFood(2, 0.15F), "cooked_frog_leg");
		poisonFrogSkin = registerItem(new ItemTropicraft().setMaxStackSize(64), "poison_frog_skin");

		scale = registerItem(new ItemTropicraft().setMaxStackSize(64), "scale");

		scaleBoots = registerItem(new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.FEET), "scale_boots");
		scaleLeggings = registerItem(new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.LEGS), "scale_leggings");
		scaleChestplate = registerItem(new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.CHEST), "scale_chestplate");
		scaleHelmet = registerItem(new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.HEAD), "scale_helmet");

		chair = registerMultiItem(new ItemChair(), "chair", ItemDye.DYE_COLORS.length);
		umbrella = registerMultiItem(new ItemUmbrella(), "umbrella", ItemDye.DYE_COLORS.length);

		portalEnchanter = registerItem(new ItemPortalEnchanter(), "portal_enchanter");

		shellFrox = registerItem(new ItemShell(), "shell_frox");
		shellPab = registerItem(new ItemShell(), "shell_pab");
		shellRube = registerItem(new ItemShell(), "shell_rube");
		shellSolo = registerItem(new ItemShell(), "shell_solo");
		//TODO make shellfish ItemShell(true) once we figure out how to get hangables to work
		shellStarfish = registerItem(new ItemShell(), "shell_starfish");
		shellTurtle = registerItem(new ItemShell(), "shell_turtle");

		cocktail = registerMultiItem(new ItemCocktail(), "cocktail", Drink.drinkList.length);

		whitePearl = registerItem(new ItemTropicraft().setMaxStackSize(64), "white_pearl");
		blackPearl = registerItem(new ItemTropicraft().setMaxStackSize(64), "black_pearl");

		fertilizer = registerItem(new ItemFertilizer(), "fertilizer");

		encyclopedia = registerItem(new ItemEncyclopediaTropica("encTropica"), "encyclopedia_tropica");

		dagger = registerItem(new ItemDagger(materialZirconTools), "dagger");
		bambooSpear = registerItem(new ItemSword(materialBambooTools), "bamboo_spear");
		coconutBomb = registerItem(new ItemCoconutBomb(), "coconut_bomb");

		flowerPot = registerItem(new ItemTropicraftBlockSpecial(BlockRegistry.flowerPot), "flower_pot");
		bambooDoor = registerItem(new ItemDoor(BlockRegistry.bambooDoor), "bamboo_door");
		bambooItemFrame = registerItem(new ItemBambooItemFrame(EntityBambooItemFrame.class), "bamboo_item_frame");
		Tropicraft.proxy.registerArbitraryBlockVariants("bamboo_item_frame", "normal", "map");

		waterWand = registerItem(new ItemWaterWand(), "water_wand");

		seaUrchinRoe = registerItem(new ItemTropicraftFood(3, 0.3F), "sea_urchin_roe");
		mobEgg = registerMultiItemTextured(new ItemMobEgg(), "spawn_egg", Names.EGG_NAMES);
	}

	public static void init() {

	}

	public static void clientProxyInit() {
		Tropicraft.proxy.registerColoredItem(chair);
		Tropicraft.proxy.registerColoredItem(umbrella);
		Tropicraft.proxy.registerColoredItem(cocktail);
	}

	private static Item registerMultiItemTextured(Item item, String name, String[] names) {
		item.setUnlocalizedName(getNamePrefixed(name));
		item.setRegistryName(new ResourceLocation(Info.MODID, name));

		GameRegistry.register(item);
		item.setCreativeTab(CreativeTabRegistry.tropicraftTab);

		for (int metadata = 0; metadata < names.length; metadata++) {
			Tropicraft.proxy.registerItemVariantModel(item, name + "_" + names[metadata], metadata);
		}

		return item;
	}

	private static Item registerMultiItem(Item item, String name, int numPlaces) {
		item.setUnlocalizedName(getNamePrefixed(name));
		item.setRegistryName(new ResourceLocation(Info.MODID, name));

		GameRegistry.register(item);
		item.setCreativeTab(CreativeTabRegistry.tropicraftTab);

		for (int metadata = 0; metadata < numPlaces; metadata++) {
			Tropicraft.proxy.registerItemVariantModel(item, name, metadata);
		}

		return item;
	}

	private static Item registerItem(Item item, String name) {
		item.setUnlocalizedName(getNamePrefixed(name));
		item.setRegistryName(new ResourceLocation(Info.MODID, name));

		GameRegistry.register(item);
		item.setCreativeTab(CreativeTabRegistry.tropicraftTab);
		Tropicraft.proxy.registerItemVariantModel(item, name, 0);

		return item;
	}

}