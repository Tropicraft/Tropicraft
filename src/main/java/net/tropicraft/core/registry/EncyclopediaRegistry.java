package net.tropicraft.core.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.enums.AshenMasks;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.enums.TropicraftShells;

public class EncyclopediaRegistry extends TropicraftRegistry {
    
    /**
     * Items that should be recognized by the encyclopedia are added here.
     * The names given MUST match the page names in the encyclopedia text file,
     * and duplicates here are ok - multiple items can be associated with 1 page.
     * Ordering doesn't matter, as the page order is determined by the text file
     * 
     * Note: Items with metadata values must be added individually (use a loop
     * if possible)
     */
    public static void init() {
        Tropicraft.encyclopedia.includeItem("acaivine", new ItemStack(BlockRegistry.flowers, 1, 9));
        Tropicraft.encyclopedia.includeItem("anemone", new ItemStack(BlockRegistry.flowers, 1, 4));
        Tropicraft.encyclopedia.includeItem("anthuriumo", new ItemStack(BlockRegistry.flowers, 1, 5));
        Tropicraft.encyclopedia.includeItem("anthuriumr", new ItemStack(BlockRegistry.flowers, 1, 6));

        for (int i = 0; i < AshenMasks.VALUES.length; i++) {
            Tropicraft.encyclopedia.includeItem("ashenmask", new ItemStack(ItemRegistry.maskMap.get(AshenMasks.VALUES[i]), 1, i));
        }

        Tropicraft.encyclopedia.includeItem("azurite", new ItemStack(ItemRegistry.azurite));
        Tropicraft.encyclopedia.includeItem("bamboo", new ItemStack(ItemRegistry.bambooShoot));
        Tropicraft.encyclopedia.includeItem("bamboomug", new ItemStack(ItemRegistry.bambooMug));
        Tropicraft.encyclopedia.includeItem("bambooblock", new ItemStack(BlockRegistry.bundles, 1, 1));
        Tropicraft.encyclopedia.includeItem("bamboochest", new ItemStack(BlockRegistry.bambooChest));
        Tropicraft.encyclopedia.includeItem("bamboodoor", new ItemStack(ItemRegistry.bambooDoor));
        Tropicraft.encyclopedia.includeItem("bamboofence", new ItemStack(BlockRegistry.bambooFence));
        Tropicraft.encyclopedia.includeItem("bamboofencegate", new ItemStack(BlockRegistry.bambooFenceGate));
        Tropicraft.encyclopedia.includeItem("bamboospear", new ItemStack(ItemRegistry.bambooSpear));
        Tropicraft.encyclopedia.includeItem("bamboostick", new ItemStack(ItemRegistry.bambooStick));

        for (int i = 0; i < 5; i++) {
            Tropicraft.encyclopedia.includeItem("beachchair", new ItemStack(ItemRegistry.chair, 1, i));
            //TODO  Tropicraft.encyclopedia.includeItem("beachfloat", new ItemStack(TCItemRegistry.beachFloat, 1, i));
            /*
             * beachfloat.title = Beach Float
beachfloat.desc  = These uncontrollable floats allow the gentle currents of the tropics to move you. They come in five different colors.
             */
            Tropicraft.encyclopedia.includeItem("beachumbrella", new ItemStack(ItemRegistry.umbrella, 1, i));
        }

        Tropicraft.encyclopedia.includeItem("blackcoffee", MixerRecipes.getItemStack(Drink.blackCoffee));
        Tropicraft.encyclopedia.includeItem("blacksand", new ItemStack(BlockRegistry.sands, 1, 3));
        //TODO Tropicraft.encyclopedia.includeItem("blowgun", new ItemStack(ItemRegistry.blowGun, 1, 0));
        Tropicraft.encyclopedia.includeItem("bromeliad", new ItemStack(BlockRegistry.flowers, 1, 14));
        Tropicraft.encyclopedia.includeItem("caipirinha", MixerRecipes.getItemStack(Drink.caipirinha));
        Tropicraft.encyclopedia.includeItem("canna", new ItemStack(BlockRegistry.flowers, 1, 3));
        Tropicraft.encyclopedia.includeItem("chunkohead", new ItemStack(BlockRegistry.chunk));
        Tropicraft.encyclopedia.includeItem("chunkfence", new ItemStack(BlockRegistry.chunkFence));
        Tropicraft.encyclopedia.includeItem("chunkfencegate", new ItemStack(BlockRegistry.chunkFenceGate));
        Tropicraft.encyclopedia.includeItem("coconut", new ItemStack(BlockRegistry.coconut));
        Tropicraft.encyclopedia.includeItem("coconutchunks", new ItemStack(ItemRegistry.coconutChunk));
        Tropicraft.encyclopedia.includeItem("coconutbomb", new ItemStack(ItemRegistry.coconutBomb));
        Tropicraft.encyclopedia.includeItem("coffeebean", new ItemStack(ItemRegistry.coffeeBeans, 1, 0));
        Tropicraft.encyclopedia.includeItem("commelina", new ItemStack(BlockRegistry.flowers, 1, 0));

        for (int i = 0; i < TropicraftCorals.VALUES.length; i++) {
            Tropicraft.encyclopedia.includeItem("coral", new ItemStack(BlockRegistry.coral, 1, i));
        }

        Tropicraft.encyclopedia.includeItem("coralsand", new ItemStack(BlockRegistry.sands, 1, 1));
        Tropicraft.encyclopedia.includeItem("crocosmia", new ItemStack(BlockRegistry.flowers, 1, 1));
        Tropicraft.encyclopedia.includeItem("croton", new ItemStack(BlockRegistry.flowers, 1, 10));
        //      TODO for (int i = 0; i < ItemCurare.effectNames.length; i++) {
        //          Tropicraft.encyclopedia.includeItem("curare", new ItemStack(TCItemRegistry.curare, 1, i));
        //      }
        //TODO Tropicraft.encyclopedia.includeItem("curarebowl", new ItemStack(BlockRegistry.curareBowl));
        Tropicraft.encyclopedia.includeItem("dagger", new ItemStack(ItemRegistry.dagger));
        //      TODO for (int i = 0; i < ItemCurare.effectNames.length; i++) {
        //          Tropicraft.encyclopedia.includeItem("dart", new ItemStack(TCItemRegistry.dart, 1, i));
        //      }
        Tropicraft.encyclopedia.includeItem("dracaena", new ItemStack(BlockRegistry.flowers, 1, 11));
        Tropicraft.encyclopedia.includeItem("easternisles", new ItemStack(ItemRegistry.recordEasternIsles));
        //  Tropicraft.encyclopedia.includeItem("enchantwand", new ItemStack(TCItemRegistry.enchantWand));
        Tropicraft.encyclopedia.includeItem("encyclopedia", new ItemStack(ItemRegistry.encyclopedia));
        Tropicraft.encyclopedia.includeItem("eudialyte", new ItemStack(ItemRegistry.eudialyte));
        Tropicraft.encyclopedia.includeItem("fern", new ItemStack(BlockRegistry.flowers, 1, 12));
        Tropicraft.encyclopedia.includeItem("fertilizer", new ItemStack(ItemRegistry.fertilizer));
        Tropicraft.encyclopedia.includeItem("fireboots", new ItemStack(ItemRegistry.fireBoots));
        Tropicraft.encyclopedia.includeItem("firechestplate", new ItemStack(ItemRegistry.fireChestplate));
        Tropicraft.encyclopedia.includeItem("firehelm", new ItemStack(ItemRegistry.fireHelmet));
        Tropicraft.encyclopedia.includeItem("fireleggings", new ItemStack(ItemRegistry.fireLeggings));
        //  Tropicraft.encyclopedia.includeItem("firestaff", new ItemStack(TCItemRegistry.staffFire));
        /*
         * firestaff.title = Fire Staff
firestaff.desc = A mystical weapon usually wielded by a Koa Shaman which allows the holder to shoot fireballs that resemble meteors from the Catacombs
         */
        Tropicraft.encyclopedia.includeItem("fishbucket", new ItemStack(ItemRegistry.fishBucket));
        Tropicraft.encyclopedia.includeItem("fishingnet", new ItemStack(ItemRegistry.fishingNet));
        //TODO Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(ItemRegistry.flippers));
        //TODO Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(Items.LEATHER));
        Tropicraft.encyclopedia.includeItem("flowerpot", new ItemStack(ItemRegistry.flowerPot));
        Tropicraft.encyclopedia.includeItem("froglegs", new ItemStack(ItemRegistry.frogLeg));
        Tropicraft.encyclopedia.includeItem("froglegscooked", new ItemStack(ItemRegistry.cookedFrogLeg));
        Tropicraft.encyclopedia.includeItem("frogskin", new ItemStack(ItemRegistry.poisonFrogSkin));
        Tropicraft.encyclopedia.includeItem("froxconch", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.FROX.getMeta()));
        Tropicraft.encyclopedia.includeItem("grapefruit", new ItemStack(ItemRegistry.grapefruit));
        Tropicraft.encyclopedia.includeItem("grapefruitsapling", new ItemStack(BlockRegistry.saplings, 1, 1));
        Tropicraft.encyclopedia.includeItem("greensand", new ItemStack(BlockRegistry.sands, 1, 2));
        //Tropicraft.encyclopedia.includeItem("icestaff", new ItemStack(TCItemRegistry.staffIce));
        Tropicraft.encyclopedia.includeItem("iggyscale", new ItemStack(ItemRegistry.scale));
        Tropicraft.encyclopedia.includeItem("iris", new ItemStack(BlockRegistry.iris));
        //  Tropicraft.encyclopedia.includeItem("journalpage", new ItemStack(TCItemRegistry.journalPage));
        Tropicraft.encyclopedia.includeItem("kapok", new ItemStack(BlockRegistry.leaves, 1, 2));
        //TODO Tropicraft.encyclopedia.includeItem("koachest", new ItemStack(BlockRegistry.koaChest));
        //TODO Tropicraft.encyclopedia.includeItem("leafball", new ItemStack(ItemRegistry.leafBall));
        //Tropicraft.encyclopedia.includeItem("leather", new ItemStack(Items.LEATHER));
        Tropicraft.encyclopedia.includeItem("lemon", new ItemStack(ItemRegistry.lemon));
        Tropicraft.encyclopedia.includeItem("lemonade", MixerRecipes.getItemStack(Drink.lemonade));
        Tropicraft.encyclopedia.includeItem("lemonsapling", new ItemStack(BlockRegistry.saplings, 1, 2));
        Tropicraft.encyclopedia.includeItem("lime", new ItemStack(ItemRegistry.lime));
        Tropicraft.encyclopedia.includeItem("limeade", MixerRecipes.getItemStack(Drink.limeade));
        Tropicraft.encyclopedia.includeItem("limesapling", new ItemStack(BlockRegistry.saplings, 1, 4));
        Tropicraft.encyclopedia.includeItem("lowtide", new ItemStack(ItemRegistry.recordLowTide));
        Tropicraft.encyclopedia.includeItem("magicmushroom", new ItemStack(BlockRegistry.flowers, 1, 7));
        Tropicraft.encyclopedia.includeItem("mahogany", new ItemStack(BlockRegistry.logs, 1, 1));
        Tropicraft.encyclopedia.includeItem("mahoganyfence", new ItemStack(BlockRegistry.mahoganyFence));
        Tropicraft.encyclopedia.includeItem("mahoganyfencegate", new ItemStack(BlockRegistry.mahoganyFenceGate));
        Tropicraft.encyclopedia.includeItem("marlinmeat", new ItemStack(ItemRegistry.freshMarlin));
        Tropicraft.encyclopedia.includeItem("marlincooked", new ItemStack(ItemRegistry.searedMarlin));
        Tropicraft.encyclopedia.includeItem("mineralsand", new ItemStack(BlockRegistry.sands, 1, 4));
        Tropicraft.encyclopedia.includeItem("mixer", new ItemStack(BlockRegistry.drinkMixer));
        //  Tropicraft.encyclopedia.includeItem("nigeljournal", new ItemStack(TCItemRegistry.nigelJournal));
        Tropicraft.encyclopedia.includeItem("orange", new ItemStack(ItemRegistry.orange));
        Tropicraft.encyclopedia.includeItem("orangeade", MixerRecipes.getItemStack(Drink.orangeade));
        Tropicraft.encyclopedia.includeItem("orangesapling", new ItemStack(BlockRegistry.saplings, 1, 3));
        Tropicraft.encyclopedia.includeItem("orchid", new ItemStack(BlockRegistry.flowers, 1, 2));
        Tropicraft.encyclopedia.includeItem("pabshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.PAB.getMeta()));
        Tropicraft.encyclopedia.includeItem("palmplanks", new ItemStack(BlockRegistry.planks, 1, 0)); //0 is palm, 1 is mahogany
        Tropicraft.encyclopedia.includeItem("palmwood", new ItemStack(BlockRegistry.logs, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmsapling", new ItemStack(BlockRegistry.saplings, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmfence", new ItemStack(BlockRegistry.palmFence));
        Tropicraft.encyclopedia.includeItem("palmfencegate", new ItemStack(BlockRegistry.palmFenceGate));
        Tropicraft.encyclopedia.includeItem("pathos", new ItemStack(BlockRegistry.flowers, 1, 8));
        Tropicraft.encyclopedia.includeItem("pearlb", new ItemStack(ItemRegistry.blackPearl));
        Tropicraft.encyclopedia.includeItem("pearlw", new ItemStack(ItemRegistry.whitePearl));
        Tropicraft.encyclopedia.includeItem("pineapple", new ItemStack(BlockRegistry.pineapple, 1, 0));
        Tropicraft.encyclopedia.includeItem("pineapplecubes", new ItemStack(ItemRegistry.pineappleCubes));
        Tropicraft.encyclopedia.includeItem("pinacolada", MixerRecipes.getItemStack(Drink.pinaColada));
        Tropicraft.encyclopedia.includeItem("portalstarter",  new ItemStack(ItemRegistry.portalEnchanter));
        Tropicraft.encyclopedia.includeItem("purifiedsand", new ItemStack(BlockRegistry.sands, 0, 0));
        Tropicraft.encyclopedia.includeItem("reeds", new ItemStack(Items.REEDS));
        Tropicraft.encyclopedia.includeItem("rubenautilus", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.RUBE.getMeta()));
        Tropicraft.encyclopedia.includeItem("scaleboots", new ItemStack(ItemRegistry.scaleBoots));
        Tropicraft.encyclopedia.includeItem("scalechestplate", new ItemStack(ItemRegistry.scaleChestplate));
        Tropicraft.encyclopedia.includeItem("scalehelm", new ItemStack(ItemRegistry.scaleHelmet));
        Tropicraft.encyclopedia.includeItem("scaleleggings", new ItemStack(ItemRegistry.scaleLeggings));
        Tropicraft.encyclopedia.includeItem("scubaflippers", new ItemStack(ItemRegistry.pinkFlippers));
        Tropicraft.encyclopedia.includeItem("scubaflippers", new ItemStack(ItemRegistry.yellowFlippers));
        Tropicraft.encyclopedia.includeItem("scubachestplate", new ItemStack(ItemRegistry.pinkChestplateGear));
        Tropicraft.encyclopedia.includeItem("scubachestplate", new ItemStack(ItemRegistry.yellowChestplateGear));
        Tropicraft.encyclopedia.includeItem("scubagoggles", new ItemStack(ItemRegistry.pinkScubaGoggles));
        Tropicraft.encyclopedia.includeItem("scubagoggles", new ItemStack(ItemRegistry.yellowScubaGoggles));
        Tropicraft.encyclopedia.includeItem("divecomputer", new ItemStack(ItemRegistry.diveComputer));
        Tropicraft.encyclopedia.includeItem("weightbelt", new ItemStack(ItemRegistry.yellowWeightBelt));
        Tropicraft.encyclopedia.includeItem("weightbelt", new ItemStack(ItemRegistry.pinkWeightBelt));
        Tropicraft.encyclopedia.includeItem("ponybottle", new ItemStack(ItemRegistry.yellowPonyBottle));
        Tropicraft.encyclopedia.includeItem("ponybottle", new ItemStack(ItemRegistry.pinkPonyBottle));
        Tropicraft.encyclopedia.includeItem("bcd", new ItemStack(ItemRegistry.pinkBCD));
        Tropicraft.encyclopedia.includeItem("bcd", new ItemStack(ItemRegistry.yellowBCD));
        Tropicraft.encyclopedia.includeItem("regulator", new ItemStack(ItemRegistry.yellowRegulator));
        Tropicraft.encyclopedia.includeItem("regulator", new ItemStack(ItemRegistry.pinkRegulator));
        Tropicraft.encyclopedia.includeItem("scubatank", new ItemStack(ItemRegistry.pinkScubaTank));
        Tropicraft.encyclopedia.includeItem("scubatank", new ItemStack(ItemRegistry.yellowScubaTank));
        Tropicraft.encyclopedia.includeItem("seaurchinroe", new ItemStack(ItemRegistry.seaUrchinRoe));
        Tropicraft.encyclopedia.includeItem("sifter", new ItemStack(BlockRegistry.sifter));
        //TODO Tropicraft.encyclopedia.includeItem("smeltedzircon", new ItemStack(ItemRegistry.ore, 1, 4));
        //TODO Tropicraft.encyclopedia.includeItem("snaretrap", new ItemStack(ItemRegistry.snareTrap));
        //TODO Tropicraft.encyclopedia.includeItem("snorkel", new ItemStack(ItemRegistry.snorkel));
        Tropicraft.encyclopedia.includeItem("solonoxshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.SOLO.getMeta()));
        Tropicraft.encyclopedia.includeItem("starfishshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.STARFISH.getMeta()));
        Tropicraft.encyclopedia.includeItem("summering", new ItemStack(ItemRegistry.recordSummering));
        Tropicraft.encyclopedia.includeItem("tikitorch", new ItemStack(BlockRegistry.tikiTorch));
        Tropicraft.encyclopedia.includeItem("thatchblock", new ItemStack(BlockRegistry.bundles, 1, 0));
        Tropicraft.encyclopedia.includeItem("thatchfence", new ItemStack(BlockRegistry.thatchFence));
        Tropicraft.encyclopedia.includeItem("thatchfencegate", new ItemStack(BlockRegistry.thatchFenceGate));
        Tropicraft.encyclopedia.includeItem("thetribe", new ItemStack(ItemRegistry.recordTheTribe));
        Tropicraft.encyclopedia.includeItem("tradewinds", new ItemStack(ItemRegistry.recordTradeWinds));
        Tropicraft.encyclopedia.includeItem("trimix", new ItemStack(ItemRegistry.trimix));
        Tropicraft.encyclopedia.includeItem("tropiframe", new ItemStack(ItemRegistry.bambooItemFrame));
        Tropicraft.encyclopedia.includeItem("turtleshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.TURTLE.getMeta()));
        Tropicraft.encyclopedia.includeItem("waterwand", new ItemStack(ItemRegistry.waterWand));
        Tropicraft.encyclopedia.includeItem("zircon", new ItemStack(ItemRegistry.zircon));
        Tropicraft.encyclopedia.includeItem("fishingrod", new ItemStack(ItemRegistry.fishingRod));
        Tropicraft.encyclopedia.includeItem("bambooladder", new ItemStack(BlockRegistry.bambooLadder));

        //Tropicraft.encyclopedia.includeItem("zirconium", new ItemStack(TCItemRegistry.ore, 1, 3));
    }
}
