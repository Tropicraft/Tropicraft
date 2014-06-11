package net.tropicraft.registry;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemBambooDoor;
import net.tropicraft.item.ItemCoffeeBean;
import net.tropicraft.item.ItemFertilizer;
import net.tropicraft.item.ItemFlowerPot;
import net.tropicraft.item.ItemTikiTorch;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.item.ItemTropicraftFood;
import net.tropicraft.item.ItemTropicraftMulti;
import net.tropicraft.item.ItemTropicsWaterBucket;
import net.tropicraft.item.ItemWaterWand;
import net.tropicraft.item.armor.ItemFireArmor;
import net.tropicraft.item.armor.ItemScaleArmor;
import net.tropicraft.item.armor.ItemTropicraftArmor;
import net.tropicraft.item.placeable.ItemChair;
import net.tropicraft.item.placeable.ItemPoolFloat;
import net.tropicraft.item.placeable.ItemUmbrella;
import net.tropicraft.item.scuba.ItemScubaChestplate;
import net.tropicraft.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.item.scuba.ItemScubaFlippers;
import net.tropicraft.item.scuba.ItemScubaGear;
import net.tropicraft.item.scuba.ItemScubaHelmet;
import net.tropicraft.item.scuba.ItemScubaLeggings;
import net.tropicraft.item.scuba.ItemScubaTank;
import net.tropicraft.item.tool.ItemTropicraftAxe;
import net.tropicraft.item.tool.ItemTropicraftHoe;
import net.tropicraft.item.tool.ItemTropicraftPickaxe;
import net.tropicraft.item.tool.ItemTropicraftShovel;
import net.tropicraft.item.tool.ItemTropicraftSword;
import net.tropicraft.item.tool.ItemTropicraftTool;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCItemRegistry {

	public static final ItemTropicraft frogLeg = new ItemTropicraft();
	public static final ItemTropicraftFood cookedFrogLeg = new ItemTropicraftFood(2, 0.15F);
	public static final ItemTropicraft poisonFrogSkin = new ItemTropicraft();
	
	public static final ItemTropicraftFood freshMarlin = new ItemTropicraftFood(2, 0.3F);
	public static final ItemTropicraftFood searedMarlin = new ItemTropicraftFood(8, 0.65F);
	
	public static final ItemTropicraftFood grapefruit = new ItemTropicraftFood(2, 0.2F);
	public static final ItemTropicraftFood lemon = new ItemTropicraftFood(2, 0.2F);
	public static final ItemTropicraftFood lime = new ItemTropicraftFood(2, 0.2F);
	public static final ItemTropicraftFood orange = new ItemTropicraftFood(2, 0.2F);
	
	public static final ItemTropicraft scale = new ItemTropicraft();
	
	public static final ItemTropicraftFood coconutChunk = new ItemTropicraftFood(1, 0.1F);
	public static final ItemTropicraftFood pineappleCubes = new ItemTropicraftFood(1, 0.1F);
	
	public static final ItemTropicraft bambooStick = new ItemTropicraft();
	public static final ItemTropicraftFood seaUrchinRoe = new ItemTropicraftFood(3, 0.3F);
	
	public static final ItemTropicraft pearl = new ItemTropicraftMulti(TCNames.pearlNames);
	public static final ItemTropicraft ore = new ItemTropicraftMulti(TCNames.oreNames);
	
	public static final ItemTropicraft waterWand = new ItemWaterWand();
	public static final ItemTropicraft fishingNet = new ItemTropicraft();
	
	public static final ItemTropicraft coffeeBean = new ItemCoffeeBean();
	
	// Armor	
	public static final ArmorMaterial materialScaleArmor = EnumHelper.addArmorMaterial("scale", 18, new int[]{2, 6, 5, 2}, 9);
	public static final ItemTropicraftArmor scaleBoots = new ItemScaleArmor(materialScaleArmor, 0, 3);
	public static final ItemTropicraftArmor scaleLeggings = new ItemScaleArmor(materialScaleArmor, 0, 2);
	public static final ItemTropicraftArmor scaleChestplate = new ItemScaleArmor(materialScaleArmor, 0, 1);
	public static final ItemTropicraftArmor scaleHelmet = new ItemScaleArmor(materialScaleArmor, 0, 0);
	
	public static final ArmorMaterial materialFireArmor = EnumHelper.addArmorMaterial("fire", 12, new int[]{2, 4, 5, 6}, 9);
	public static final ItemTropicraftArmor fireBoots = new ItemFireArmor(materialFireArmor, 0, 3);
	public static final ItemTropicraftArmor fireLeggings = new ItemFireArmor(materialFireArmor, 0, 2);
	public static final ItemTropicraftArmor fireChestplate = new ItemFireArmor(materialFireArmor, 0, 1);
	public static final ItemTropicraftArmor fireHelmet = new ItemFireArmor(materialFireArmor, 0, 0);
	// End Armor
	
	// Tools
	public static ToolMaterial materialZirconTools = EnumHelper.addToolMaterial("zircon", 2, 250, 6.0F, 2.0F, 14);
	public static ToolMaterial materialEudialyteTools = EnumHelper.addToolMaterial("eudialyte", 2, 250, 6.0F, 2.0F, 14);
	
	public static final ItemHoe hoeEudialyte = new ItemTropicraftHoe(materialEudialyteTools, TCNames.hoeEudialyte);
	public static final ItemHoe hoeZircon = new ItemTropicraftHoe(materialZirconTools, TCNames.hoeZircon);
	
	public static final ItemTropicraftTool shovelEudialyte = new ItemTropicraftShovel(materialEudialyteTools, TCNames.shovelEudialyte);
	public static final ItemTropicraftTool shovelZircon = new ItemTropicraftShovel(materialZirconTools, TCNames.shovelZircon);
	
	public static final ItemTropicraftTool pickaxeEudialyte = new ItemTropicraftPickaxe(materialEudialyteTools, TCNames.pickaxeEudialyte);
	public static final ItemTropicraftTool pickaxeZircon = new ItemTropicraftPickaxe(materialZirconTools, TCNames.pickaxeZircon);
	
	public static final ItemTropicraftTool axeEudialyte = new ItemTropicraftAxe(materialEudialyteTools, TCNames.axeEudialyte);
	public static final ItemTropicraftTool axeZircon = new ItemTropicraftAxe(materialZirconTools, TCNames.axeZircon);

	public static final ItemSword swordEudialyte = new ItemTropicraftSword(materialEudialyteTools, TCNames.swordEudialyte);
	public static final ItemSword swordZircon = new ItemTropicraftSword(materialZirconTools, TCNames.swordZircon);
	// End Tools

	public static final ItemTropicraft tikiTorch = new ItemTikiTorch();
	public static final ItemTropicraft bambooDoor = new ItemBambooDoor();
	public static final ItemTropicsWaterBucket bucketTropicsWater = new ItemTropicsWaterBucket();

	public static final ItemChair chair = new ItemChair();
	public static final ItemUmbrella umbrella = new ItemUmbrella();
	public static final ItemPoolFloat innerTube = new ItemPoolFloat();

	public static final ItemFlowerPot flowerPot = new ItemFlowerPot(TCBlockRegistry.flowerPot);
	public static final ItemFertilizer fertilizer = new ItemFertilizer();
	
//TODO	public static final ItemTropicraft coconutBomb = new ItemCoconutBomb();
	
	public static final ArmorMaterial materialDrySuit = EnumHelper.addArmorMaterial("fire", 50, new int[]{2, 4, 5, 6}, 9);
	public static final ItemTropicraftArmor dryFlippers = new ItemScubaFlippers(materialDrySuit, ItemScubaGear.ScubaMaterial.DRY, 0, 3);
	public static final ItemTropicraftArmor dryLeggings = new ItemScubaLeggings(materialDrySuit, ItemScubaGear.ScubaMaterial.DRY, 0, 2);
	public static final ItemTropicraftArmor dryChestplate = new ItemScubaChestplate(materialDrySuit, ItemScubaGear.ScubaMaterial.DRY, 0, 1);
	public static final ItemTropicraftArmor dryChestplateGear = new ItemScubaChestplateGear(materialDrySuit, ItemScubaGear.ScubaMaterial.DRY, 0, 1);
	public static final ItemTropicraftArmor dryHelmet = new ItemScubaHelmet(materialDrySuit, ItemScubaGear.ScubaMaterial.DRY, 0, 0);
	
	public static final ArmorMaterial materialWetSuit = EnumHelper.addArmorMaterial("fire", 50, new int[]{2, 4, 5, 6}, 9);
	public static final ItemTropicraftArmor wetFlippers = new ItemScubaFlippers(materialWetSuit, ItemScubaGear.ScubaMaterial.WET, 0, 3);
	public static final ItemTropicraftArmor wetLeggings = new ItemScubaLeggings(materialWetSuit, ItemScubaGear.ScubaMaterial.WET, 0, 2);
	public static final ItemTropicraftArmor wetChestplate = new ItemScubaChestplate(materialWetSuit, ItemScubaGear.ScubaMaterial.WET, 0, 1);
	public static final ItemTropicraftArmor wetChestplateGear = new ItemScubaChestplateGear(materialWetSuit, ItemScubaGear.ScubaMaterial.WET, 0, 1);
	public static final ItemTropicraftArmor wetHelmet = new ItemScubaHelmet(materialWetSuit, ItemScubaGear.ScubaMaterial.WET, 0, 0);
	
	public static final ItemScubaTank scubaTank = new ItemScubaTank();

	/**
	 * Register all the items
	 */
	public static void init() {
		registerItem(frogLeg, TCNames.frogLeg);
		registerItem(cookedFrogLeg, TCNames.cookedFrogLeg);
		registerItem(poisonFrogSkin, TCNames.poisonFrogSkin);
		
		registerItem(freshMarlin, TCNames.freshMarlin);
		registerItem(searedMarlin, TCNames.searedMarlin);
		
		registerItem(grapefruit, TCNames.grapefruit);
		registerItem(lemon, TCNames.lemon);
		registerItem(lime, TCNames.lime);
		registerItem(orange, TCNames.orange);
		
		registerItem(scale, TCNames.scale);
		
		registerItem(coconutChunk, TCNames.coconutChunk);
		registerItem(pineappleCubes, TCNames.pineappleCubes);
		
		registerItem(bambooStick, TCNames.bambooStick);
		registerItem(seaUrchinRoe, TCNames.seaUrchinRoe);
		
		registerItem(pearl, TCNames.pearl);
		registerItem(ore, TCNames.ore);
		
		registerItem(waterWand, TCNames.waterWand);
		registerItem(fishingNet, TCNames.fishingNet);
		
		registerItem(coffeeBean, TCNames.coffeeBean);
		
		// Armor
		registerItem(scaleBoots, TCNames.scaleBoots);
		registerItem(scaleLeggings, TCNames.scaleLeggings);
		registerItem(scaleChestplate, TCNames.scaleChestplate);
		registerItem(scaleHelmet, TCNames.scaleHelmet);
		
		registerItem(fireBoots, TCNames.fireBoots);
		registerItem(fireLeggings, TCNames.fireLeggings);
		registerItem(fireChestplate, TCNames.fireChestplate);
		registerItem(fireHelmet, TCNames.fireHelmet);
		
		registerItem(axeEudialyte, TCNames.axeEudialyte);
		registerItem(hoeEudialyte, TCNames.hoeEudialyte);
		registerItem(pickaxeEudialyte, TCNames.pickaxeEudialyte);
		registerItem(shovelEudialyte, TCNames.shovelEudialyte);
		registerItem(swordEudialyte, TCNames.swordEudialyte);
		
		registerItem(axeZircon, TCNames.axeZircon);
		registerItem(hoeZircon, TCNames.hoeZircon);
		registerItem(pickaxeZircon, TCNames.pickaxeZircon);
		registerItem(shovelZircon, TCNames.shovelZircon);
		registerItem(swordZircon, TCNames.swordZircon);
		
		registerItem(tikiTorch, TCNames.tikiTorch);
		registerItem(bambooDoor, TCNames.bambooDoor);
		registerItem(bucketTropicsWater, TCNames.bucketTropicsWater);
		
		registerItem(chair, TCNames.chair);
		registerItem(umbrella, TCNames.umbrella);
		registerItem(innerTube, TCNames.innerTube);
		
		registerItem(flowerPot, TCNames.flowerPot);
		registerItem(fertilizer, TCNames.fertilizer);
		
		registerItem(dryFlippers, TCNames.dryFlippers);
		registerItem(dryLeggings, TCNames.dryLeggings);
		registerItem(dryChestplate, TCNames.dryChestplate);
		registerItem(dryChestplateGear, TCNames.dryChestplateGear);
		registerItem(dryHelmet, TCNames.dryHelmet);
		
		registerItem(wetFlippers, TCNames.wetFlippers);
		registerItem(wetLeggings, TCNames.wetLeggings);
		registerItem(wetChestplate, TCNames.wetChestplate);
		registerItem(wetChestplateGear, TCNames.wetChestplateGear);
		registerItem(wetHelmet, TCNames.wetHelmet);
		
		registerItem(scubaTank, TCNames.scubaTank);
		
	}
	
	/**
	 * Register an item with the game and give it a name
	 * @param item Item to register
	 * @param name Name to give
	 */
	private static void registerItem(Item item, String name) {
		GameRegistry.registerItem(item, name);
		item.setUnlocalizedName(name);
	}
}
