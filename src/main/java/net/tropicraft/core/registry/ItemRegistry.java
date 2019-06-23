package net.tropicraft.core.registry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.tropicraft.Names;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.placeable.EntityBambooItemFrame;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;
import net.tropicraft.core.common.enums.AshenMasks;
import net.tropicraft.core.common.enums.ITropicraftVariant;
import net.tropicraft.core.common.enums.TropicraftShells;
import net.tropicraft.core.common.item.ItemBambooItemFrame;
import net.tropicraft.core.common.item.ItemBeachFloat;
import net.tropicraft.core.common.item.ItemChair;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.common.item.ItemCoconutBomb;
import net.tropicraft.core.common.item.ItemCoffeeBean;
import net.tropicraft.core.common.item.ItemDagger;
import net.tropicraft.core.common.item.ItemEncyclopediaTropica;
import net.tropicraft.core.common.item.ItemFertilizer;
import net.tropicraft.core.common.item.ItemFishBucket;
import net.tropicraft.core.common.item.ItemFishingRod;
import net.tropicraft.core.common.item.ItemLoveTropicsShell;
import net.tropicraft.core.common.item.ItemMobEgg;
import net.tropicraft.core.common.item.ItemMusicDisc;
import net.tropicraft.core.common.item.ItemPortalEnchanter;
import net.tropicraft.core.common.item.ItemRiverFish;
import net.tropicraft.core.common.item.ItemSeaweed;
import net.tropicraft.core.common.item.ItemShell;
import net.tropicraft.core.common.item.ItemTropicalFish;
import net.tropicraft.core.common.item.ItemTropicraft;
import net.tropicraft.core.common.item.ItemTropicraftAxe;
import net.tropicraft.core.common.item.ItemTropicraftBlockSpecial;
import net.tropicraft.core.common.item.ItemTropicraftFood;
import net.tropicraft.core.common.item.ItemTropicraftPickaxe;
import net.tropicraft.core.common.item.ItemTropicsOre;
import net.tropicraft.core.common.item.ItemUmbrella;
import net.tropicraft.core.common.item.ItemWaterWand;
import net.tropicraft.core.common.item.armor.ItemAshenMask;
import net.tropicraft.core.common.item.armor.ItemFireArmor;
import net.tropicraft.core.common.item.armor.ItemNigelStache;
import net.tropicraft.core.common.item.armor.ItemScaleArmor;
import net.tropicraft.core.common.item.scuba.ItemBCD;
import net.tropicraft.core.common.item.scuba.ItemDiveComputer;
import net.tropicraft.core.common.item.scuba.ItemPonyBottle;
import net.tropicraft.core.common.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.core.common.item.scuba.ItemScubaFlippers;
import net.tropicraft.core.common.item.scuba.ItemScubaHelmet;
import net.tropicraft.core.common.item.scuba.ItemScubaTank;
import net.tropicraft.core.common.item.scuba.api.ScubaMaterial;
import net.tropicraft.core.common.sound.TropicraftSounds;

@Mod.EventBusSubscriber
public class ItemRegistry extends TropicraftRegistry {
    
    public interface IBlockItemRegistrar {
        
        Item getItem(Block block);
        
        void postRegister(Block block, Item item);
    }

    // Ore gems
    public static Item azurite, eudialyte, zircon;

    // Yummy delicious fruits
    public static Item grapefruit, lemon, lime, orange;

    // Foodstuffs   
    public static Item freshMarlin;
    public static Item searedMarlin;
    public static Item coconutChunk;
    public static Item pineappleCubes;
    public static Item coffeeBeans;
    public static Item rawNori;
    public static Item toastedNori;
    public static Item rawSeaweed;
    public static ItemTropicalFish rawTropicalFish;
    public static ItemTropicalFish cookedTropicalFish;
    public static ItemRiverFish rawRiverFish;
    public static ItemRiverFish cookedRiverFish;

    // Fishstuffs
    public static Item rawRay;
    public static Item cookedRay;

    // Tool materials
    public static ToolMaterial materialZirconTools = EnumHelper.addToolMaterial("zircon", 2, 200, 4.5F, 1.0F, 14);
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
    public static final ArmorMaterial materialScaleArmor = EnumHelper.addArmorMaterial("scale", "scale", 18, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.5F);
    public static Item scaleBoots;
    public static Item scaleLeggings;
    public static Item scaleChestplate;
    public static Item scaleHelmet;
    
    public static final ArmorMaterial materialFireArmor = EnumHelper.addArmorMaterial("fire", "fire", 12, new int[]{2, 4, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static Item fireBoots;
    public static Item fireLeggings;
    public static Item fireChestplate;
    public static Item fireHelmet;

    public static ItemChair chair;
    public static ItemUmbrella umbrella;
    public static ItemBeachFloat beach_float;

    public static Item portalEnchanter;

    public static ItemShell shell;
    public static ItemCocktail cocktail;

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
    
    public static Item iguanaLeather;

    public static final ArmorMaterial materialPinkSuit = EnumHelper.addArmorMaterial("pink_suit", "pink_suit", 15, new int[]{2, 0, 4, 1}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static Item pinkFlippers;
    //public static Item pinkLeggings;
    public static Item pinkChestplate;
    public static Item pinkChestplateGear;
    public static Item pinkHelmet;

    public static final ArmorMaterial materialYellowSuit = EnumHelper.addArmorMaterial("yellow_suit", "yellow_suit", 15, new int[]{2, 0, 4, 1}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static Item yellowFlippers;
    //public static Item yellowLeggings;
    public static Item yellowChestplate;
    public static Item yellowChestplateGear;
    public static Item yellowHelmet;

    public static Item diveComputer;

    public static Item pinkPonyBottle;
    public static Item pinkScubaGoggles;
    public static Item pinkScubaTank;
    public static Item pinkRegulator;
    public static Item pinkBCD;
    public static Item pinkWeightBelt;

    public static Item yellowPonyBottle;
    public static Item yellowScubaGoggles;
    public static Item yellowScubaTank;
    public static Item yellowRegulator;
    public static Item yellowBCD;
    public static Item yellowWeightBelt;

    public static Item trimix;
    
    public static Item fishingRod;

    public static final ArmorMaterial materialMaskArmor = EnumHelper.addArmorMaterial("mask", "mask", 10, new int[]{0, 0, 0, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static Item maskSquareZord;
    public static Item maskHornMonkey;
    public static Item maskOblongatron;
    public static Item maskHeadinator;
    public static Item maskSquareHorn;
    public static Item maskScrewAttack;
    public static Item maskTheBrain;
    public static Item maskBatBoy;
    public static Item mask1;
    public static Item mask2;
    public static Item mask3;
    public static Item mask4;
    public static Item mask5;
    
    public static final ArmorMaterial materialNigelStache = EnumHelper.addArmorMaterial("nigel", "nigel", 10, new int[]{0, 0, 0, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static Item nigelStache;

    public static ItemLoveTropicsShell ltShell;

    public static final Map<AshenMasks, Item> maskMap = new HashMap<>();

    // Linked as to maintain the same order as the block registry
    private static final Map<Block, IBlockItemRegistrar> blockItemRegistry = new LinkedHashMap<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        blockItemRegistry.entrySet().forEach(e -> {
                Item item = e.getValue().getItem(e.getKey());
                item.setRegistryName(e.getKey().getRegistryName());
                event.getRegistry().register(item);
                e.getValue().postRegister(e.getKey(), item);
        });
        IForgeRegistry<Item> registry = event.getRegistry();
        
        diveComputer = registerItem(registry, new ItemDiveComputer(), "dive_computer");
        
        pinkWeightBelt = registerItem(registry, new ItemTropicraft(), "pink_weight_belt");
        pinkPonyBottle = registerItem(registry, new ItemPonyBottle(), "pink_pony_bottle");
        pinkBCD = registerItem(registry, new ItemBCD(), "pink_bcd");
        pinkRegulator = registerItem(registry, new ItemTropicraft(), "pink_regulator");
        pinkScubaTank = registerItem(registry, new ItemScubaTank(), "pink_scuba_tank");
        pinkFlippers = registerItem(registry, new ItemScubaFlippers(materialPinkSuit, ScubaMaterial.PINK, 0, EntityEquipmentSlot.FEET), "pink_flippers");
        pinkChestplateGear = registerItem(registry, new ItemScubaChestplateGear(materialPinkSuit, ScubaMaterial.PINK, 0, EntityEquipmentSlot.CHEST), "pink_chestplate_gear");
        pinkScubaGoggles = registerItem(registry, new ItemScubaHelmet(materialPinkSuit, ScubaMaterial.PINK, 0, EntityEquipmentSlot.HEAD), "pink_scuba_goggles");

        yellowWeightBelt = registerItem(registry, new ItemTropicraft(), "yellow_weight_belt");
        yellowPonyBottle = registerItem(registry, new ItemPonyBottle(), "yellow_pony_bottle");
        yellowBCD = registerItem(registry, new ItemBCD(), "yellow_bcd");
        yellowRegulator = registerItem(registry, new ItemTropicraft(), "yellow_regulator");
        yellowScubaTank = registerItem(registry, new ItemScubaTank(), "yellow_scuba_tank");
        yellowFlippers = registerItem(registry, new ItemScubaFlippers(materialYellowSuit, ScubaMaterial.YELLOW, 0, EntityEquipmentSlot.FEET), "yellow_flippers");
        yellowChestplateGear = registerItem(registry, new ItemScubaChestplateGear(materialYellowSuit, ScubaMaterial.YELLOW, 0, EntityEquipmentSlot.CHEST), "yellow_chestplate_gear");
        yellowScubaGoggles = registerItem(registry, new ItemScubaHelmet(materialYellowSuit, ScubaMaterial.YELLOW, 0, EntityEquipmentSlot.HEAD), "yellow_scuba_goggles");
        
        recordBuriedTreasure = registerItem(registry, new ItemMusicDisc("buried_treasure", "Punchaface", TropicraftSounds.BURIED_TREASURE), "buried_treasure");
        recordEasternIsles = registerItem(registry, new ItemMusicDisc("eastern_isles", "Frox", TropicraftSounds.EASTERN_ISLES), "eastern_isles");
        recordSummering = registerItem(registry, new ItemMusicDisc("summering", "Billy Christiansen", TropicraftSounds.SUMMERING), "summering");
        recordTheTribe = registerItem(registry, new ItemMusicDisc("the_tribe", "Emile Van Krieken", TropicraftSounds.THE_TRIBE), "the_tribe");
        recordLowTide = registerItem(registry, new ItemMusicDisc("low_tide", "Punchaface", TropicraftSounds.LOW_TIDE), "low_tide");
        recordTradeWinds = registerItem(registry, new ItemMusicDisc("trade_winds", "Frox", TropicraftSounds.TRADE_WINDS), "trade_winds");

        azurite = registerItem(registry, new ItemTropicsOre(), "azurite");
        OreDictionary.registerOre("gemAzurite", azurite);
        eudialyte = registerItem(registry, new ItemTropicsOre(), "eudialyte");
        OreDictionary.registerOre("gemEudialyte", eudialyte);
        zircon = registerItem(registry, new ItemTropicsOre(), "zircon");
        OreDictionary.registerOre("gemZircon", zircon);
        
        grapefruit = registerItem(registry, new ItemTropicraftFood(2, 0.2F), "grapefruit");
        lemon = registerItem(registry, new ItemTropicraftFood(2, 0.2F), "lemon");
        lime = registerItem(registry, new ItemTropicraftFood(2, 0.2F), "lime");
        orange = registerItem(registry, new ItemTropicraftFood(2, 0.2F), "orange");

        hoeEudialyte = registerItem(registry, new ItemHoe(materialEudialyteTools), "hoe_eudialyte");
        hoeZircon = registerItem(registry, new ItemHoe(materialZirconTools), "hoe_zircon");
        pickaxeEudialyte = registerItem(registry, new ItemTropicraftPickaxe(materialEudialyteTools), "pickaxe_eudialyte");
        pickaxeZircon = registerItem(registry, new ItemTropicraftPickaxe(materialZirconTools), "pickaxe_zircon");
        shovelEudialyte = registerItem(registry, new ItemSpade(materialEudialyteTools), "shovel_eudialyte");
        shovelZircon = registerItem(registry, new ItemSpade(materialZirconTools), "shovel_zircon");
        axeEudialyte = registerItem(registry, new ItemTropicraftAxe(materialEudialyteTools, 6.0F, -3.1F), "axe_eudialyte");
        axeZircon = registerItem(registry, new ItemTropicraftAxe(materialZirconTools, 6.0F, -3.2F), "axe_zircon");
        swordEudialyte = registerItem(registry, new ItemSword(materialEudialyteTools), "sword_eudialyte");
        swordZircon = registerItem(registry, new ItemSword(materialZirconTools), "sword_zircon");

        fishingNet = registerItem(registry, new ItemTropicraft(), "fishing_net");

        bambooStick = registerItem(registry, new ItemTropicraft(), "bamboo_stick");
        OreDictionary.registerOre("bamboo", bambooStick);
        bambooMug = registerItem(registry, new ItemTropicraft().setMaxStackSize(16), "bamboo_mug");

        freshMarlin = registerItem(registry, new ItemTropicraftFood(2, 0.3F), "fresh_marlin");
        searedMarlin = registerItem(registry, new ItemTropicraftFood(8, 0.65F), "seared_marlin");

        tropicsWaterBucket = registerItem(registry, (new ItemBucket(BlockRegistry.tropicsWater)).setContainerItem(Items.BUCKET), "tropics_water_bucket");
        fishBucket = registerItem(registry, new ItemFishBucket(), "fish_bucket");

        coconutChunk = registerItem(registry, new ItemTropicraftFood(1, 0.1F), "coconut_chunk");
        OreDictionary.registerOre("cropCoconut", coconutChunk);
        pineappleCubes = registerItem(registry, new ItemTropicraftFood(1, 0.1F), "pineapple_cubes");
        OreDictionary.registerOre("cropPineapple", pineappleCubes);

        coffeeBeans = registerMultiItem(registry, new ItemCoffeeBean(Names.COFFEE_NAMES, BlockRegistry.coffeePlant), "coffee_beans", Names.COFFEE_NAMES);
        OreDictionary.registerItem("seedCoffee", new ItemStack(coffeeBeans, 1, 0));
        OreDictionary.registerItem("cropCoffee", new ItemStack(coffeeBeans, 1, 1));

        frogLeg = registerItem(registry, new ItemTropicraft().setMaxStackSize(64), "frog_leg");
        cookedFrogLeg = registerItem(registry, new ItemTropicraftFood(2, 0.15F), "cooked_frog_leg");
        poisonFrogSkin = registerItem(registry, new ItemTropicraft().setMaxStackSize(64), "poison_frog_skin");

        scale = registerItem(registry, new ItemTropicraft().setMaxStackSize(64), "scale");

        scaleBoots = registerItem(registry, new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.FEET), "scale_boots");
        scaleLeggings = registerItem(registry, new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.LEGS), "scale_leggings");
        scaleChestplate = registerItem(registry, new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.CHEST), "scale_chestplate");
        scaleHelmet = registerItem(registry, new ItemScaleArmor(materialScaleArmor, 0, EntityEquipmentSlot.HEAD), "scale_helmet");

        fireBoots = registerItem(registry, new ItemFireArmor(materialFireArmor, 0, EntityEquipmentSlot.FEET), "fire_boots");
        fireLeggings = registerItem(registry, new ItemFireArmor(materialFireArmor, 0, EntityEquipmentSlot.LEGS), "fire_leggings");
        fireChestplate = registerItem(registry, new ItemFireArmor(materialFireArmor, 0, EntityEquipmentSlot.CHEST), "fire_chestplate");
        fireHelmet = registerItem(registry, new ItemFireArmor(materialFireArmor, 0, EntityEquipmentSlot.HEAD), "fire_helmet");
        
        chair = registerMultiItem(registry, new ItemChair(), "chair", ItemDye.DYE_COLORS.length);
        umbrella = registerMultiItem(registry, new ItemUmbrella(), "umbrella", ItemDye.DYE_COLORS.length);
        beach_float = registerMultiItem(registry, new ItemBeachFloat(), "float", ItemDye.DYE_COLORS.length);

        portalEnchanter = registerItem(registry, new ItemPortalEnchanter(), "portal_enchanter");

        shell = registerMultiItem(registry, new ItemShell(), "shell", TropicraftShells.values());

        cocktail = registerMultiItem(registry, new ItemCocktail(), "cocktail", Drink.drinkList.length);

        whitePearl = registerItem(registry, new ItemTropicraft().setMaxStackSize(64), "white_pearl");
        blackPearl = registerItem(registry, new ItemTropicraft().setMaxStackSize(64), "black_pearl");

        fertilizer = registerItem(registry, new ItemFertilizer(), "fertilizer");

        encyclopedia = registerItem(registry, new ItemEncyclopediaTropica(), "encyclopedia_tropica");

        dagger = registerItem(registry, new ItemDagger(materialZirconTools), "dagger");
        bambooSpear = registerItem(registry, new ItemSword(materialBambooTools), "bamboo_spear");
        coconutBomb = registerItem(registry, new ItemCoconutBomb(), "coconut_bomb");

        flowerPot = registerItem(registry, new ItemTropicraftBlockSpecial(BlockRegistry.flowerPot), "flower_pot");
        bambooDoor = registerItem(registry, new ItemDoor(BlockRegistry.bambooDoor), "bamboo_door");
        bambooItemFrame = registerItem(registry, new ItemBambooItemFrame(EntityBambooItemFrame.class), "bamboo_item_frame");
        Tropicraft.proxy.registerArbitraryBlockVariants("bamboo_item_frame", "normal", "map");

        waterWand = registerItem(registry, new ItemWaterWand(), "water_wand");

        seaUrchinRoe = registerItem(registry, new ItemTropicraftFood(3, 0.3F), "sea_urchin_roe");
        mobEgg = registerMultiItemPrefixed(registry, new ItemMobEgg(), "spawn_egg", Names.EGG_NAMES);
        
        iguanaLeather = registerItem(registry, new ItemTropicraft().setMaxStackSize(64), "iguana_leather");
        OreDictionary.registerOre("leather", iguanaLeather);

        trimix = registerItem(registry, new ItemTropicraft().setMaxStackSize(1), "trimix");

        maskSquareZord = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.SQUARE_ZORD), "mask_square_zord");
        maskHornMonkey = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.HORN_MONKEY), "mask_horn_monkey");
        maskOblongatron = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.OBLONGATRON), "mask_oblongatron");
        maskHeadinator = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.HEADINATOR), "mask_headinator");
        maskSquareHorn = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.SQUARE_HORN), "mask_square_horn");
        maskScrewAttack = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.SCREW_ATTACK), "mask_screw_attack");
        maskTheBrain = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.THE_BRAIN), "mask_the_brain");
        maskBatBoy = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.BAT_BOY), "mask_bat_boy");
        mask1 = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.ASHEN_MASK1), "mask_ashen_mask1");
        mask2 = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.ASHEN_MASK2), "mask_ashen_mask2");
        mask3 = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.ASHEN_MASK3), "mask_ashen_mask3");
        mask4 = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.ASHEN_MASK4), "mask_ashen_mask4");
        mask5 = registerItem(registry, new ItemAshenMask(materialMaskArmor, 0, EntityEquipmentSlot.HEAD, AshenMasks.ASHEN_MASK5), "mask_ashen_mask5");

        fishingRod = registerItem(registry, new ItemFishingRod(), "fishing_rod");
        ltShell = registerMultiItem(registry, new ItemLoveTropicsShell(), "ltshell", Names.LT17_NAMES.length);

        rawSeaweed = registerItem(registry, new ItemSeaweed(), "seaweed");
        rawNori = registerItem(registry, new ItemTropicraft(), "nori_raw");
        toastedNori = registerItem(registry, new ItemTropicraftFood(2, 0.2F), "nori_toasted");
        rawRay = registerItem(registry, new ItemTropicraft(), "ray_raw");
        cookedRay = registerItem(registry, new ItemTropicraftFood(5, 0.5F), "ray_cooked");
        rawTropicalFish = registerMultiItem(registry, new ItemTropicalFish(2, 0.2F, "tropicalfish", "raw"), "raw_fish", EntityTropicalFish.NAMES.length);
        cookedTropicalFish = registerMultiItem(registry, new ItemTropicalFish(4, 0.4F, "tropicalfish", "cooked"), "cooked_fish", EntityTropicalFish.NAMES.length);
        rawRiverFish = registerMultiItem(registry, new ItemRiverFish(2, 0.2F, "riverfish", "raw"), "raw_river_fish", 2);
        cookedRiverFish = registerMultiItem(registry, new ItemRiverFish(4, 0.4F, "riverfish", "cooked"), "cooked_river_fish", 2);

        nigelStache = registerItem(registry, new ItemNigelStache(materialNigelStache, 0, EntityEquipmentSlot.HEAD), "nigel_stache");
    }

    public static void init() {

    }

    public static void clientProxyInit() {
        Tropicraft.proxy.registerColoredItem(chair);
        Tropicraft.proxy.registerColoredItem(umbrella);
        Tropicraft.proxy.registerColoredItem(cocktail);
        Tropicraft.proxy.registerColoredItem(beach_float);
        Tropicraft.proxy.registerColoredItem(ltShell);
        Tropicraft.proxy.registerColoredItem(rawTropicalFish);
        Tropicraft.proxy.registerColoredItem(cookedTropicalFish);
        Tropicraft.proxy.registerColoredItem(rawRiverFish);
        Tropicraft.proxy.registerColoredItem(cookedRiverFish);
    }
    
    public static void addBlockItem(Block block, IBlockItemRegistrar item) {
        blockItemRegistry.put(block, item);
    }
    
    private static <T extends Item> T registerMultiItem(IForgeRegistry<Item> registry, T item, String regName, ITropicraftVariant... variants) {
        return registerMultiItemPrefixed(registry, item, regName, Arrays.stream(variants).map(ITropicraftVariant::getSimpleName).toArray(String[]::new));
    }

    private static <T extends Item> T registerMultiItem(IForgeRegistry<Item> registry, T item, String regName, String... variantNames) {
        T ret = registerItem(registry, item, regName, variantNames[0]);
        for (int i = 1; i < variantNames.length; i++) {
            Tropicraft.proxy.registerItemVariantModel(item, variantNames[i], i);
        }
        return ret;
    }

    private static <T extends Item> T registerMultiItemPrefixed(IForgeRegistry<Item> registry, T item, String name, String[] names) {
        return registerMultiItem(registry, item, name, Arrays.stream(names).map(s -> name + "_" + s).toArray(String[]::new));
    }

    private static <T extends Item> T registerMultiItem(IForgeRegistry<Item> registry, T item, String name, int numPlaces) {
        T ret = registerItem(registry, item, name);
        for (int i = 1; i < numPlaces; i++) {
            Tropicraft.proxy.registerItemVariantModel(item, name, i);
        }
        return ret;
    }

    private static <T extends Item> T registerItem(IForgeRegistry<Item> registry, T item, String name) {
        return registerItem(registry, item, name, name);
    }

    private static <T extends Item> T registerItem(IForgeRegistry<Item> registry, T item, String name, String variantName) {
        item.setUnlocalizedName(getNamePrefixed(name));
        item.setRegistryName(name);

        registry.register(item);
        item.setCreativeTab(CreativeTabRegistry.tropicraftTab);
        Tropicraft.proxy.registerItemVariantModel(item, variantName, 0);

        return item;
    }
}