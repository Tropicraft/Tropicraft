package net.tropicraft.registry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.tropicraft.Tropicraft;
import net.tropicraft.curare.CurareMix;
import net.tropicraft.curare.CurareMixRegistry;
import net.tropicraft.curare.CurareType;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemCurare;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCCraftingRegistry {

    public static void init() {
   //     addRecipes();
        initCurareMixes();
   //     initDartRecipes(true);
    }

    private static void initCurareMixes() {
        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.confusion, 
                flower(7), flower(8), flower(9)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.harm, 
                flower(7), flower(7), flower(7), flower(7), flower(7), flower(7), flower(8), flower(9), flower(2)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.hunger, 
                flower(0), flower(1), flower(2)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.hunger, 
                flower(0), flower(1), flower(3)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.hunger, 
                flower(0), flower(1), flower(4)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.moveSlowdown, 
                flower(7), flower(1), flower(2)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.poison, 
                flower(7), flower(8), flower(4), flower(7)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.poison, 
                flower(7), flower(9), flower(4), flower(7)
                ));

        CurareMixRegistry.getInstance().registerRecipe(new CurareMix(CurareType.weakness, 
                flower(1), flower(3), flower(4)
                ));
    }

    private static ItemStack flower(int damage) {
        return new ItemStack(TCBlockRegistry.flowers, 1, damage);
    }

    private static void initDartRecipes(boolean isServer) {       

        //register all blow types
        for (int damage = 0; damage < TCNames.dartNames.length; damage++) {
            createRecipe(isServer, true, new ItemStack(TCItemRegistry.blowGun, 1, damage), new Object[]{
                "X  ", " I ", "  X",
                'X', TCItemRegistry.bambooStick,
                'I', new ItemStack(TCItemRegistry.curare, 1, damage)
            });
        }

        //keep classic paralysis dart recipe, use for poison frog skin for now :/
        createRecipe(isServer, true, new ItemStack(TCItemRegistry.dart, 4), new Object[]{
            "XI", " C",
            'X', Items.iron_ingot,
            'I', TCItemRegistry.poisonFrogSkin,
            'C', Items.feather
        });

        createRecipe(isServer, true, new ItemStack(TCItemRegistry.dart, 4), new Object[]{
            "X ", "IC",
            'X', Items.iron_ingot,
            'I', TCItemRegistry.poisonFrogSkin,
            'C', Items.feather
        });

        //register all types of curare, including paralysis
        for (int damage = 0; damage < ItemCurare.effectNames.length; damage++) {
            createRecipe(isServer, true, new ItemStack(TCItemRegistry.dart, 4, damage), new Object[] {
                "XI", " C",
                'X', Items.iron_ingot,
                'I', new ItemStack(TCItemRegistry.curare, 1, damage),
                'C', Items.feather
            });

            createRecipe(isServer, true, new ItemStack(TCItemRegistry.dart, 4, damage), new Object[] {
                "X ", "IC",
                'X', Items.iron_ingot,
                'I', new ItemStack(TCItemRegistry.curare, 1, damage),
                'C', Items.feather
            });
        }
    }

    private static void createOreBlockRecipe(int i, int j) {
        createRecipe(true, new ItemStack(TCBlockRegistry.oreBlocks, 1, i), new Object[] {
            "%%%", "%%%", "%%%",
            '%', new ItemStack(TCItemRegistry.ore, 1, j)
        });
    }

    @SideOnly(Side.CLIENT)
    public static void addToEncyclopedia(ItemStack itemstack, Object obj[]) {
        Tropicraft.encyclopedia.includeRecipe(itemstack, obj);
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

    /**
     * Items that should be recognized by the encyclopedia are added here.
     * The names given MUST match the page names in the encyclopedia text file,
     * and duplicates here are ok - multiple items can be associated with 1 page.
     * Ordering doesn't matter, as the page order is determined by the text file
     * 
     * Note: Items with metadata values must be added individually (use a loop
     * if possible)
     */
    public static void addItemsToEncyclopedia() {
        Tropicraft.encyclopedia.includeItem("acaivine", new ItemStack(TCBlockRegistry.flowers, 1, 9));
        Tropicraft.encyclopedia.includeItem("anemone", new ItemStack(TCBlockRegistry.flowers, 1, 4));
        Tropicraft.encyclopedia.includeItem("anthuriumo", new ItemStack(TCBlockRegistry.flowers, 1, 5));
        Tropicraft.encyclopedia.includeItem("anthuriumr", new ItemStack(TCBlockRegistry.flowers, 1, 6));

        /*TODO  for (int i = 0; i < ItemAshenMask.maskTypeNames.length; i++) {
            Tropicraft.encyclopedia.includeItem("ashenmask", new ItemStack(TCItemRegistry.ashenMask, 1, i));
        }*/

        //TODO
        /*
         * ashenmask.title = Ashen Mask
ashenmask.desc  = These masks are the source of an Ashen's identity. Without a mask, an Ashen is naked, fearful, and cowardly, while with one, they are perhaps the most agressive hunters in the Tropics. By wearing a mask, you are considered an Ashen. Masks can also be hung on walls for decoration. Be wary though, as a maskless Ashen will take the closest mask.
         */

        Tropicraft.encyclopedia.includeItem("azurite", new ItemStack(TCItemRegistry.ore, 1, 2));
        Tropicraft.encyclopedia.includeItem("bamboo", new ItemStack(TCItemRegistry.bambooChute));
        Tropicraft.encyclopedia.includeItem("bamboomug", new ItemStack(TCItemRegistry.bambooMugEmpty));
        Tropicraft.encyclopedia.includeItem("bambooblock", new ItemStack(TCBlockRegistry.bambooBundle));
        Tropicraft.encyclopedia.includeItem("bamboochest", new ItemStack(TCBlockRegistry.bambooChest));
        Tropicraft.encyclopedia.includeItem("bamboodoor", new ItemStack(TCItemRegistry.bambooDoor));
        Tropicraft.encyclopedia.includeItem("bamboospear", new ItemStack(TCItemRegistry.bambooSpear));
        Tropicraft.encyclopedia.includeItem("bamboostick", new ItemStack(TCItemRegistry.bambooStick));

        for (int i = 0; i < 5; i++) {
            Tropicraft.encyclopedia.includeItem("beachchair", new ItemStack(TCItemRegistry.chair, 1, i));
            //TODO  Tropicraft.encyclopedia.includeItem("beachfloat", new ItemStack(TCItemRegistry.beachFloat, 1, i));
            /*
             * beachfloat.title = Beach Float
beachfloat.desc  = These uncontrollable floats allow the gentle currents of the tropics to move you. They come in five different colors.
             */
            Tropicraft.encyclopedia.includeItem("beachumbrella", new ItemStack(TCItemRegistry.umbrella, 1, i));
        }

        Tropicraft.encyclopedia.includeItem("blackcoffee", MixerRecipes.getItemStack(Drink.blackCoffee));
        Tropicraft.encyclopedia.includeItem("blowgun", new ItemStack(TCItemRegistry.blowGun));
        Tropicraft.encyclopedia.includeItem("bromeliad", new ItemStack(TCBlockRegistry.flowers, 1, 14));
        Tropicraft.encyclopedia.includeItem("caipirinha", MixerRecipes.getItemStack(Drink.caipirinha));
        Tropicraft.encyclopedia.includeItem("canna", new ItemStack(TCBlockRegistry.flowers, 1, 3));
        Tropicraft.encyclopedia.includeItem("chunkohead", new ItemStack(TCBlockRegistry.chunkOHead));
        Tropicraft.encyclopedia.includeItem("coconut", new ItemStack(TCBlockRegistry.coconut));
        Tropicraft.encyclopedia.includeItem("coconutchunks", new ItemStack(TCItemRegistry.coconutChunk));
        Tropicraft.encyclopedia.includeItem("coconutbomb", new ItemStack(TCItemRegistry.coconutBomb));
        Tropicraft.encyclopedia.includeItem("coffeebean", new ItemStack(TCItemRegistry.coffeeBean));
        Tropicraft.encyclopedia.includeItem("commelina", new ItemStack(TCBlockRegistry.flowers, 1, 0));

        for (int i = 0; i < TCNames.coralNames.length; i++) {
            Tropicraft.encyclopedia.includeItem("coral", new ItemStack(TCBlockRegistry.coral, 1, i));
        }

        Tropicraft.encyclopedia.includeItem("crocosmia", new ItemStack(TCBlockRegistry.flowers, 1, 1));
        Tropicraft.encyclopedia.includeItem("croton", new ItemStack(TCBlockRegistry.flowers, 1, 10));
        Tropicraft.encyclopedia.includeItem("dagger", new ItemStack(TCItemRegistry.dagger));
        Tropicraft.encyclopedia.includeItem("dracaena", new ItemStack(TCBlockRegistry.flowers, 1, 11));
        Tropicraft.encyclopedia.includeItem("easternisles", new ItemStack(TCItemRegistry.recordEasternIsles));
        //  Tropicraft.encyclopedia.includeItem("enchantwand", new ItemStack(TCItemRegistry.enchantWand));
        Tropicraft.encyclopedia.includeItem("encyclopedia", new ItemStack(TCItemRegistry.encTropica));
        Tropicraft.encyclopedia.includeItem("eudialyte", new ItemStack(TCItemRegistry.ore, 1, 0));
        Tropicraft.encyclopedia.includeItem("fern", new ItemStack(TCBlockRegistry.flowers, 1, 12));
        Tropicraft.encyclopedia.includeItem("fertilizer", new ItemStack(TCItemRegistry.fertilizer));
        Tropicraft.encyclopedia.includeItem("fireboots", new ItemStack(TCItemRegistry.fireBoots));
        Tropicraft.encyclopedia.includeItem("firechestplate", new ItemStack(TCItemRegistry.fireChestplate));
        Tropicraft.encyclopedia.includeItem("firehelm", new ItemStack(TCItemRegistry.fireHelmet));
        Tropicraft.encyclopedia.includeItem("fireleggings", new ItemStack(TCItemRegistry.fireLeggings));
        Tropicraft.encyclopedia.includeItem("firestaff", new ItemStack(TCItemRegistry.staffFire));
        Tropicraft.encyclopedia.includeItem("fishbucket", new ItemStack(TCItemRegistry.fishBucket));
        Tropicraft.encyclopedia.includeItem("fishingnet", new ItemStack(TCItemRegistry.fishingNet));
        Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(TCItemRegistry.flippers));
        Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(Items.leather));
        Tropicraft.encyclopedia.includeItem("flowerpot", new ItemStack(TCItemRegistry.flowerPot));
        Tropicraft.encyclopedia.includeItem("froglegs", new ItemStack(TCItemRegistry.frogLeg));
        Tropicraft.encyclopedia.includeItem("froglegscooked", new ItemStack(TCItemRegistry.cookedFrogLeg));
        Tropicraft.encyclopedia.includeItem("frogskin", new ItemStack(TCItemRegistry.poisonFrogSkin));
        Tropicraft.encyclopedia.includeItem("froxconch", new ItemStack(TCItemRegistry.shells, 1, 1));
        Tropicraft.encyclopedia.includeItem("grapefruit", new ItemStack(TCItemRegistry.grapefruit));
        Tropicraft.encyclopedia.includeItem("grapefruitsapling", new ItemStack(TCBlockRegistry.saplings, 1, 1));
        //Tropicraft.encyclopedia.includeItem("icestaff", new ItemStack(TCItemRegistry.staffIce));
        Tropicraft.encyclopedia.includeItem("iggyscale", new ItemStack(TCItemRegistry.scale));
        Tropicraft.encyclopedia.includeItem("iris", new ItemStack(TCBlockRegistry.tallFlowers, 1, 15));
        //  Tropicraft.encyclopedia.includeItem("journalpage", new ItemStack(TCItemRegistry.journalPage));
        Tropicraft.encyclopedia.includeItem("kapok", new ItemStack(TCBlockRegistry.rainforestLeaves, 1, 0)); //0 is kapok, 1 is mahogany
        Tropicraft.encyclopedia.includeItem("koachest", new ItemStack(TCBlockRegistry.koaChest));
        Tropicraft.encyclopedia.includeItem("orchid", new ItemStack(TCBlockRegistry.flowers, 1, 2));
        Tropicraft.encyclopedia.includeItem("leafball", new ItemStack(TCItemRegistry.leafBall));
        Tropicraft.encyclopedia.includeItem("leather", new ItemStack(Items.leather));
        Tropicraft.encyclopedia.includeItem("lemon", new ItemStack(TCItemRegistry.lemon));
        Tropicraft.encyclopedia.includeItem("lemonade", MixerRecipes.getItemStack(Drink.lemonade));
        Tropicraft.encyclopedia.includeItem("lemonsapling", new ItemStack(TCBlockRegistry.saplings, 1, 2));
        Tropicraft.encyclopedia.includeItem("lime", new ItemStack(TCItemRegistry.lime));
        Tropicraft.encyclopedia.includeItem("limeade", MixerRecipes.getItemStack(Drink.limeade));
        Tropicraft.encyclopedia.includeItem("limesapling", new ItemStack(TCBlockRegistry.saplings, 1, 4));
        Tropicraft.encyclopedia.includeItem("lowtide", new ItemStack(TCItemRegistry.recordLowTide));
        Tropicraft.encyclopedia.includeItem("magicmushroom", new ItemStack(TCBlockRegistry.flowers, 1, 7));
        Tropicraft.encyclopedia.includeItem("mahogany", new ItemStack(TCBlockRegistry.logs, 1, 1));
        Tropicraft.encyclopedia.includeItem("marlinmeat", new ItemStack(TCItemRegistry.freshMarlin));
        Tropicraft.encyclopedia.includeItem("marlincooked", new ItemStack(TCItemRegistry.searedMarlin));
        //  Tropicraft.encyclopedia.includeItem("nigeljournal", new ItemStack(TCItemRegistry.nigelJournal));
        Tropicraft.encyclopedia.includeItem("orange", new ItemStack(TCItemRegistry.orange));
        Tropicraft.encyclopedia.includeItem("orangeade", MixerRecipes.getItemStack(Drink.orangeade));
        Tropicraft.encyclopedia.includeItem("orangesapling", new ItemStack(TCBlockRegistry.saplings, 1, 3));
        Tropicraft.encyclopedia.includeItem("pabshell", new ItemStack(TCItemRegistry.shells, 1, 2));
        Tropicraft.encyclopedia.includeItem("palmplanks", new ItemStack(TCBlockRegistry.planks, 1, 0)); //0 is palm, 1 is mahogany
        Tropicraft.encyclopedia.includeItem("palmwood", new ItemStack(TCBlockRegistry.logs, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmsapling", new ItemStack(TCBlockRegistry.saplings, 1, 0));
        Tropicraft.encyclopedia.includeItem("paradart", new ItemStack(TCItemRegistry.dart));
        Tropicraft.encyclopedia.includeItem("pathos", new ItemStack(TCBlockRegistry.flowers, 1, 8));
        Tropicraft.encyclopedia.includeItem("pearlb", new ItemStack(TCItemRegistry.pearl, 1, 1));
        Tropicraft.encyclopedia.includeItem("pearlw", new ItemStack(TCItemRegistry.pearl, 1, 0));
        Tropicraft.encyclopedia.includeItem("pineapple", new ItemStack(TCBlockRegistry.tallFlowers, 1, 9));
        Tropicraft.encyclopedia.includeItem("pineapplecubes", new ItemStack(TCItemRegistry.pineappleCubes));
        Tropicraft.encyclopedia.includeItem("pinacolada", MixerRecipes.getItemStack(Drink.pinaColada));
        Tropicraft.encyclopedia.includeItem("portalstarter",  new ItemStack(TCItemRegistry.portalEnchanter));
        Tropicraft.encyclopedia.includeItem("purifiedsand", new ItemStack(TCBlockRegistry.purifiedSand));
        Tropicraft.encyclopedia.includeItem("reeds", new ItemStack(Items.reeds));
        Tropicraft.encyclopedia.includeItem("rubenautilus", new ItemStack(TCItemRegistry.shells, 1, 3));
        Tropicraft.encyclopedia.includeItem("scaleboots", new ItemStack(TCItemRegistry.scaleBoots));
        Tropicraft.encyclopedia.includeItem("scalechestplate", new ItemStack(TCItemRegistry.scaleChestplate));
        Tropicraft.encyclopedia.includeItem("scalehelm", new ItemStack(TCItemRegistry.scaleHelmet));
        Tropicraft.encyclopedia.includeItem("scaleleggings", new ItemStack(TCItemRegistry.scaleLeggings));
        Tropicraft.encyclopedia.includeItem("seaurchinroe", new ItemStack(TCItemRegistry.seaUrchinRoe));
        Tropicraft.encyclopedia.includeItem("sifter", new ItemStack(TCBlockRegistry.sifter));
        Tropicraft.encyclopedia.includeItem("smeltedzircon", new ItemStack(TCItemRegistry.ore, 1, 4));
        Tropicraft.encyclopedia.includeItem("snaretrap", new ItemStack(TCItemRegistry.snareTrap));
        Tropicraft.encyclopedia.includeItem("snorkel", new ItemStack(TCItemRegistry.snorkel));
        Tropicraft.encyclopedia.includeItem("solonoxshell", new ItemStack(TCItemRegistry.shells, 1, 0));
        Tropicraft.encyclopedia.includeItem("starfishshell", new ItemStack(TCItemRegistry.shells, 1, 4));
        Tropicraft.encyclopedia.includeItem("tikitorch", new ItemStack(TCItemRegistry.tikiTorch));
        Tropicraft.encyclopedia.includeItem("thatchblock", new ItemStack(TCBlockRegistry.thatchBundle, 1, 0));
        Tropicraft.encyclopedia.includeItem("thetribe", new ItemStack(TCItemRegistry.recordTheTribe));
        Tropicraft.encyclopedia.includeItem("tradewinds", new ItemStack(TCItemRegistry.recordTradeWinds));
        Tropicraft.encyclopedia.includeItem("tropiframe", new ItemStack(TCItemRegistry.tropiFrame));
        Tropicraft.encyclopedia.includeItem("turtleshell", new ItemStack(TCItemRegistry.shells, 1, 5));
        Tropicraft.encyclopedia.includeItem("waterwand", new ItemStack(TCItemRegistry.waterWand));
        Tropicraft.encyclopedia.includeItem("zircon", new ItemStack(TCItemRegistry.ore, 1, 1));
        Tropicraft.encyclopedia.includeItem("zirconium", new ItemStack(TCItemRegistry.ore, 1, 3));

        for (int i = 0; i < ItemCurare.effectNames.length; i++) {
            Tropicraft.encyclopedia.includeItem("curare", new ItemStack(TCItemRegistry.curare, 1, i));
            Tropicraft.encyclopedia.includeItem("dart", new ItemStack(TCItemRegistry.dart, 1, i));
            Tropicraft.encyclopedia.includeItem("blowgun", new ItemStack(TCItemRegistry.blowGun, 1, i));
        }
    }

    public static void addRecipes() {

        // Thatch
        createRecipe(true, new ItemStack(TCBlockRegistry.thatchBundle, 1, 0), new Object[]{
            "XX", "XX",
            'X', Items.reeds
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.bambooBundle, 1), new Object[]{
            "XX", "XX",
            'X', TCItemRegistry.bambooChute
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.thatchStairs, 1), new Object[]{//
            "X ", "XX",
            'X', Items.reeds
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.thatchStairs, 4), new Object[]{//
            "X ", "XX",
            'X', new ItemStack(TCBlockRegistry.thatchBundle, 1, 0)
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.bambooStairs, 1), new Object[]{//
            "X ", "XX",
            'X', TCItemRegistry.bambooChute
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.bambooStairs, 4), new Object[]{//
            "X ", "XX",
            'X', TCBlockRegistry.bambooBundle
        });

        // Thatch slab
        createRecipe(true, new ItemStack(TCBlockRegistry.singleSlabs, 1, 1), new Object[]{//
            "XX",
            'X', Items.reeds
        });

        // Thatch slab
        createRecipe(true, new ItemStack(TCBlockRegistry.singleSlabs, 1, 1), new Object[]{//
            "X",
            'X', new ItemStack(TCBlockRegistry.thatchBundle, 1, 0)
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.singleSlabs, 1, 0), new Object[]{//
            "XX",
            'X', TCItemRegistry.bambooChute
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.singleSlabs, 2, 0), new Object[]{//
            "X",
            'X', TCBlockRegistry.bambooBundle
        });

        // Palm  planks -> Palm logs
        createRecipe(true, new ItemStack(TCBlockRegistry.planks, 4, 0), new Object[]{
            "#",
            '#', new ItemStack(TCBlockRegistry.logs, 1, 0)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.bambooMugEmpty, 1), new Object[]{
            "X X", "X X", "XXX",
            'X', TCItemRegistry.bambooChute
        });

        createRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
            "X", "Y", "Z",
            'X', TCItemRegistry.coconutChunk,
            'Y', new ItemStack(TCBlockRegistry.tallFlowers, 1, 9),
            'Z', TCItemRegistry.bambooMugEmpty
        });

        createRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
            "Y", "X", "Z",
            'X', TCItemRegistry.coconutChunk,
            'Y', new ItemStack(TCBlockRegistry.tallFlowers, 1, 9),
            'Z', TCItemRegistry.bambooMugEmpty
        });

        createRecipe(true, new ItemStack(TCItemRegistry.scaleHelmet, 1), new Object[]{
            "XXX", "X X",
            'X', TCItemRegistry.scale
        });

        createRecipe(true, new ItemStack(TCItemRegistry.scaleChestplate, 1), new Object[]{
            "X X", "XXX", "XXX",
            'X', TCItemRegistry.scale
        });

        createRecipe(true, new ItemStack(TCItemRegistry.scaleLeggings, 1), new Object[]{
            "XXX", "X X", "X X",
            'X', TCItemRegistry.scale
        });

        createRecipe(true, new ItemStack(TCItemRegistry.scaleBoots, 1), new Object[]{
            "X X", "X X",
            'X', TCItemRegistry.scale
        });

        createRecipe(true, new ItemStack(TCItemRegistry.coconutBomb, 1), new Object[]{
            " X ", "XYX", " X ",
            'X', Items.gunpowder,
            'Y', TCBlockRegistry.coconut
        });

        createRecipe(true, new ItemStack(TCItemRegistry.tikiTorch, 2), new Object[]{
            "Y  ", " X ", "  X",
            'Y', Items.coal,
            'X', TCItemRegistry.bambooStick
        });

        createRecipe(false, new ItemStack(TCItemRegistry.tikiTorch, 2), new Object[]{
            "Y  ", " X ", "  X",
            'Y', new ItemStack(Items.coal, 1, 1),
            'X', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.bambooFence, 2), new Object[]{
            "XXX", "XXX",
            'X', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.bambooFenceGate, 1), new Object[]{
            "XIX", "XIX",
            'X', TCItemRegistry.bambooStick,
            'I', TCBlockRegistry.bambooBundle
        });

        createRecipe(true, new ItemStack(TCItemRegistry.bambooSpear, 1), new Object[]{
            "X ", " X",
            'X', TCItemRegistry.bambooStick
        });

        // Blowgun recipe used to be here

        // List of wool colors to use for chair/float/umbrella
        int[] beachItemColors = new int[] {3, 14, 4, 2, 5};
        for (int i = 0; i < beachItemColors.length; i++) {

            createRecipe(true, new ItemStack(TCItemRegistry.chair, 1, i), new Object[]{
                "XIX", "XIX", "XIX",
                'X', TCItemRegistry.bambooStick,
                'I', new ItemStack(Blocks.wool, 1, beachItemColors[i])
            });

            createRecipe(true, new ItemStack(TCItemRegistry.umbrella, 1, i), new Object[]{
                "XXX", " I ", " I ",
                'X', new ItemStack(Blocks.wool, 1, beachItemColors[i]),
                'I', TCItemRegistry.bambooStick
            });

            /*createRecipe(true, new ItemStack(TCItemRegistry.beachFloat, 1, i), new Object[]{
                "XXX", "III",
                'X', new ItemStack(Block.cloth, 1, beachItemColors[i]),
                'I', TCItemRegistry.bambooItem
            });*/
        }

        //  createRecipe(true, new ItemStack(TCItemRegistry.enchantWand), new Object[]{
        //      "XY ", "YXY", " YX",
        //      'X', new ItemStack(TCItemRegistry.bambooStick),
        //      'Y', new ItemStack(TCItemRegistry.oreDrops, 1, 3)
        //  });


        //TODO
        /*
         * nigeljournal.title = Journal
nigeljournal.desc = A culmination of all of the notes I took while exploring the realm. Rumor has it finding all pages of my journal comes with a reward that is truly spectacular. Can you collect all of the pages?


journalpage.title = Journal Page
journalpage.desc = Every page is a day's worth of notes on what I explored in the Tropics that day. Rumor has it that collecting all of the pages from my journal comes with a truly spectacular result that will change the way you view the Tropics. Complete quests given by Koa Shaman to collect my journal pages. Best of luck to you, but be careful, you never know what is hiding behind that stalk of bamboo.


enchantwand.title = Wand o' Enchanting
enchantwand.desc = Make a 2 block wide, 2 block long, 1 block high square of tropics water, toss in an item you want enchanted, and toss in the necessary amount of azurite crystals. Right click this area with the Wand o' Enchanting, and voila, you have an awesome enchanted item!


         */

        // Chunk slab
        createRecipe(true, new ItemStack(TCBlockRegistry.singleSlabs, 2, 2), new Object[]{
            "X",
            'X', TCBlockRegistry.chunkOHead
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.chunkStairs, 4), new Object[]{
            "X  ", "XX ", "XXX",
            'X', TCBlockRegistry.chunkOHead
        });

        // Palm slab
        createRecipe(true, new ItemStack(TCBlockRegistry.singleSlabs, 2, 3), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0)
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.palmStairs, 4), new Object[]{
            "X  ", "XX ", "XXX",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0)
        });

        // Palm planks make crafting table
        createRecipe(true, new ItemStack(Blocks.crafting_table, 1), new Object[]{
            "II", "II",
            'I', new ItemStack(TCBlockRegistry.planks, 1, 0)
        });
        
        // Mahogany planks make crafting table
        createRecipe(true, new ItemStack(Blocks.crafting_table, 1), new Object[]{
            "II", "II",
            'I', new ItemStack(TCBlockRegistry.planks, 1, 1)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.pearl, 1, 0), new Object[]{
            "I",
            'I', new ItemStack(TCItemRegistry.shells, 1, 0)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.pearl, 1, 1), new Object[]{
            "I",
            'I', new ItemStack(TCItemRegistry.shells, 1, 1)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.pickaxeZircon), new Object[]{
            "XXX", " I ", " I ",
            'X', new ItemStack(TCItemRegistry.ore, 1, 1),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.axeZircon), new Object[]{
            "XX", "XI ", " I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 1),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.hoeZircon), new Object[]{
            "XX", " I", " I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 1),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.swordZircon), new Object[]{
            "X", "X", "I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 1),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.shovelZircon), new Object[]{
            "X", "I", "I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 1),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.pickaxeEudialyte), new Object[]{
            "XXX", " I ", " I ",
            'X', new ItemStack(TCItemRegistry.ore, 1, 0),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.axeEudialyte), new Object[]{
            "XX", "XI", " I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 0),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.hoeEudialyte), new Object[]{
            "XX", " I", " I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 0),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.swordEudialyte), new Object[]{
            "X", "X", "I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 0),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.shovelEudialyte), new Object[]{
            "X", "I", "I",
            'X', new ItemStack(TCItemRegistry.ore, 1, 0),
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.pineappleCubes), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.tallFlowers, 1, 9)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.flippers), new Object[]{
            "XIX", "X X",
            'X', Items.leather,
            'I', new ItemStack(Items.dye, 1, 4)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.snorkel), new Object[]{
            "X  ", "XII",
            'X', TCItemRegistry.bambooChute,
            'I', Blocks.glass_pane
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.sifter), new Object[]{
            "XXX", "XIX", "XXX",
            'X', Blocks.planks,
            'I', Blocks.glass_pane
        });

        createRecipe(true, new ItemStack(TCItemRegistry.dagger), new Object[]{
            "X", "X", "I",
            'X', TCBlockRegistry.chunkOHead,
            'I', new ItemStack(TCBlockRegistry.planks, 1, 0)    // Palm planks
        });

        createRecipe(true, new ItemStack(TCItemRegistry.fishingNet), new Object[]{
            "  X", " XI", "XII",
            'X', TCItemRegistry.bambooChute,
            'I', Items.string
        });

        createRecipe(true, new ItemStack(TCItemRegistry.fertilizer, 3), new Object[]{
            "XI",
            'X', new ItemStack(TCBlockRegistry.flowers, 1, 7),
            'I', new ItemStack(TCBlockRegistry.flowers, 1, 10)
        });

        //para dart recipes were here

        createRecipe(true, new ItemStack(Items.dye, 4, 5), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.tallFlowers, 1, 15)
        });

        createRecipe(true, new ItemStack(Items.dye, 2, 1), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.flowers, 1, 6)//r antherium
        });

        createRecipe(true, new ItemStack(Items.dye, 2, 14), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.flowers, 1, 5)//o antherium
        });

        createRecipe(true, new ItemStack(Items.dye, 2, 12), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.flowers, 1, 0)//fern
        });

        createRecipe(true, new ItemStack(Items.dye, 2, 2), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.flowers, 1, 12)//c. diffusa
        });

        createRecipe(true, new ItemStack(Items.dye, 2, 11), new Object[]{
            "X",
            'X', new ItemStack(TCBlockRegistry.flowers, 1, 3) //canna
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.bambooChest, 1), new Object[]{
            "XXX", "X X", "XXX",
            'X', TCItemRegistry.bambooChute
        });

        createRecipe(true, new ItemStack(Items.stick, 4), new Object[] {
            "#", "#",
            '#', new ItemStack(TCBlockRegistry.planks, 1, 0)
        });

        createRecipe(true, new ItemStack(Items.wooden_pickaxe), new Object[] {
            "XXX", " # ", " # ",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0),
            '#', Items.stick
        });

        createRecipe(true, new ItemStack(Items.wooden_shovel), new Object[] {
            "X", "#", "#",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0),
            '#', Items.stick
        });

        createRecipe(true, new ItemStack(Items.wooden_axe), new Object[] {
            "XX", "X#", " #",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0),
            '#', Items.stick
        });

        createRecipe(true, new ItemStack(Items.wooden_hoe), new Object[] {
            "XX", " #", " #",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0),
            '#', Items.stick
        });

        createRecipe(true, new ItemStack(Items.wooden_sword), new Object[] {
            "X", "X", "#",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0),
            '#', Items.stick
        });

        createRecipe(true, new ItemStack(Blocks.planks, 4, 3), new Object[] {
            "#",
            '#', new ItemStack(TCBlockRegistry.logs, 1, 1)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.encTropica, 1), new Object[]{
            "###", "#$#", "###",
            '#', TCItemRegistry.bambooChute,
            '$', Items.book
        });

        createRecipe(true, new ItemStack(Items.wooden_door, 1), new Object[]{
            "XX", "XX", "XX",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0)
        });

        createRecipe(true, new ItemStack(Blocks.trapdoor, 2), new Object[]{
            "XXX", "XXX",
            'X', new ItemStack(TCBlockRegistry.planks, 1, 0)
        });
        createRecipe(true, new ItemStack(TCItemRegistry.bambooDoor), new Object[]{
            "XX", "YY", "XX", 'X', TCBlockRegistry.bambooBundle,
            'Y', TCBlockRegistry.thatchBundle
        });
        createRecipe(true, new ItemStack(TCItemRegistry.waterWand), new Object[]{
            "  X", " Y ", "Y  ", 'X', new ItemStack(TCItemRegistry.ore, 1, 2), 'Y', Items.gold_ingot
        });
        createRecipe(true, new ItemStack(TCItemRegistry.snareTrap), new Object[] {
            "  X", "XX ", "XX ", 'X', Items.string
        });

        createRecipe(true, new ItemStack(TCItemRegistry.flowerPot), new Object[] {
            "# #", " # ", '#', TCItemRegistry.bambooChute
        });

        createRecipe(true, new ItemStack(TCItemRegistry.coffeeBean, 1, 0), new Object[] {
            "X", 'X', new ItemStack(TCItemRegistry.coffeeBean, 0, 2)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.tropiFrame, 1), new Object[] {
            "###", "#X#", "###", '#', TCItemRegistry.bambooChute, 'X', Item.leather
        });

        createRecipe(true, new ItemStack(TCItemRegistry.portalEnchanter, 1), new Object[] {
            "%@#", "#@%", " @ ", '@', TCItemRegistry.bambooStick, '#',
            new ItemStack(TCItemRegistry.ore, 1, 2),
            '%', new ItemStack(TCItemRegistry.ore, 1, 3)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.portalEnchanter, 1), new Object[] {
            "#@%", "%@#", " @ ", '@', TCItemRegistry.bambooStick,
            '#', new ItemStack(TCItemRegistry.ore, 1, 2),
            '%', new ItemStack(TCItemRegistry.ore, 1, 3)
        });

        createRecipe(true, new ItemStack(TCItemRegistry.bambooStick, 4),
                new Object[] {"#", "#", '#', TCItemRegistry.bambooChute});

        createRecipe(true, new ItemStack(TCItemRegistry.ore, 1, 3), new Object[] {
            " # ", "#$#", " # ",
            '#', Items.diamond,
            '$', new ItemStack(TCItemRegistry.ore, 1, 4)  //smelted zircon
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.koaChest, 1), new Object[] {
            "###", "#X#", "###", '#', new ItemStack(TCItemRegistry.ore, 1, 3), 'X', TCBlockRegistry.bambooChest
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.eihMixer), new Object[] {
            "XXX", "X X", "XXX", 'X', TCBlockRegistry.chunkOHead
        });

        createRecipe(true, new ItemStack(TCBlockRegistry.curareBowl), new Object[] {
            "X X", " X ", 'X', TCBlockRegistry.chunkOHead
        });

        createRecipe(true, new ItemStack(TCItemRegistry.rodOld), new Object[]{
            "  I",
            " IX",
            "I V",

            'X', Items.string,
            'V', Items.iron_ingot,
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.rodGood), new Object[]{
            "  I",
            " IX",
            "I V",

            'X', Items.string,
            'V', TCItemRegistry.lemon,
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.rodSuper), new Object[]{
            "  I",
            " IX",
            "I V",

            'X', Items.string,
            'V', TCItemRegistry.lureSuper,
            'I', TCItemRegistry.bambooStick
        });

        createRecipe(true, new ItemStack(TCItemRegistry.lureSuper), new Object[]{
            " X ",
            "XOX",
            " X ",
            'X', Items.iron_ingot,
            'O', new ItemStack(Items.dye, 1, 5)
        });

        createOreBlockRecipe(2, 0); //eudialyte
        createOreBlockRecipe(3, 1); //zircon
        createOreBlockRecipe(4, 2); //azurite
        createOreBlockRecipe(5, 3); //zirconium

   //     CurareRecipes.addRecipes(true);
    //    CurareRecipes.addCurareMixerRecipes();

        // Shapeless recipes go here //
        GameRegistry.addShapelessRecipe(
                new ItemStack(TCItemRegistry.fertilizer, 3),
                new Object[]{
                    new ItemStack(TCBlockRegistry.flowers, 1, 7),
                    new ItemStack(TCBlockRegistry.flowers, 1, 10)
                });

        GameRegistry.addShapelessRecipe(
                MixerRecipes.getItemStack(Drink.pinaColada),
                new Object[]{
                    TCItemRegistry.coconutChunk,
                    new ItemStack(TCBlockRegistry.tallFlowers, 1, 9),
                    TCItemRegistry.bambooMugEmpty
                });


        // Smelting recipes go here //
        GameRegistry.addSmelting(TCItemRegistry.frogLeg, new ItemStack(TCItemRegistry.cookedFrogLeg), 3);
        GameRegistry.addSmelting(TCBlockRegistry.purifiedSand, new ItemStack(Blocks.glass), 4);
        GameRegistry.addSmelting(TCItemRegistry.freshMarlin, new ItemStack(TCItemRegistry.searedMarlin), 6);
        GameRegistry.addSmelting((TCBlockRegistry.logs), new ItemStack(Items.coal, 1, 1), 3); // metadata 1 = charcoal
        FurnaceRecipes.smelting().func_151396_a(TCItemRegistry.coffeeBean, new ItemStack(TCItemRegistry.coffeeBean, 1, 1), 0.15f);
        //zircon -> smelted zircon
        FurnaceRecipes.smelting().func_151396_a(TCItemRegistry.ore, new ItemStack(TCItemRegistry.ore, 1, 4), 3F);

        // Custom fuel burn times!
        GameRegistry.registerFuelHandler(new IFuelHandler() {
                @Override
                public int getBurnTime(ItemStack fuel) {
                    // Palm slabs
                    if (fuel.getItem() != null && fuel.getItem() instanceof ItemBlock
                            && Block.getBlockFromItem(fuel.getItem()) == TCBlockRegistry.singleSlabs && fuel.getItemDamage() == 3) {
                        return 150;
                    }
                    return 0;
                }
        });

        /*  TODO    for(int i = 0; i < 2; i++){
            ItemStack fuelItem = new ItemStack(TCItemRegistry.shells,1);
            Item rodItem = null;
            switch(i){
            case 0:
                rodItem = TCItemRegistry.rodOld;
                break;
            case 1:
                rodItem = TCItemRegistry.rodGood;
                fuelItem = new ItemStack(TCItemRegistry.lemon,1,0);
                break;
            case 2:
                rodItem = TCItemRegistry.rodSuper;
                fuelItem = new ItemStack(TCItemRegistry.oreDrops,1,3);
                break;
            }
        createRecipe(false, new ItemStack(rodItem, 1), new Object[] {
            "  #", " #X", "# F", '#', TCItemRegistry.bambooStick, 'X', Item.silk, 'F', fuelItem
        });
        }*/
    }
}
