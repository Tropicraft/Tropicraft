package net.tropicraft.core.registry;

import java.util.Arrays;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.tropicraft.Names;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.core.common.enums.AshenMasks;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.common.enums.TropicraftShells;
import net.tropicraft.core.encyclopedia.EntityPage;
import net.tropicraft.core.encyclopedia.ItemPage;
import net.tropicraft.core.encyclopedia.KoaPage;
import net.tropicraft.core.encyclopedia.LoveTropicsPage;
import net.tropicraft.core.encyclopedia.MultiEntityPage;
import net.tropicraft.core.encyclopedia.MultiItemPage;
import net.tropicraft.core.encyclopedia.SectionPage;
import net.tropicraft.core.encyclopedia.TropicalBook;

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
        TropicalBook enc = Tropicraft.encyclopedia;
        enc.addPage(new ItemPage("encyclopedia", new ItemStack(ItemRegistry.encyclopedia)));
        
        // All Foods
        enc.addPage(new SectionPage("foods"));
        enc.addPage(new ItemPage("pineapple", new ItemStack(BlockRegistry.pineapple, 1, 0)));
        enc.addPage(new ItemPage("pineapplecubes", new ItemStack(ItemRegistry.pineappleCubes)));
        enc.addPage(new ItemPage("coconut", new ItemStack(BlockRegistry.coconut)));
        enc.addPage(new ItemPage("coconutchunks", new ItemStack(ItemRegistry.coconutChunk)));
        enc.addPage(new ItemPage("froglegs", new ItemStack(ItemRegistry.frogLeg)));
        enc.addPage(new ItemPage("froglegscooked", new ItemStack(ItemRegistry.cookedFrogLeg)));
        enc.addPage(new ItemPage("grapefruit", new ItemStack(ItemRegistry.grapefruit)));
        enc.addPage(new ItemPage("lemon", new ItemStack(ItemRegistry.lemon)));
        enc.addPage(new ItemPage("lime", new ItemStack(ItemRegistry.lime)));
        enc.addPage(new ItemPage("orange", new ItemStack(ItemRegistry.orange)));
        enc.addPage(new ItemPage("marlinmeat", new ItemStack(ItemRegistry.freshMarlin)));
        enc.addPage(new ItemPage("marlincooked", new ItemStack(ItemRegistry.searedMarlin)));
        enc.addPage(new ItemPage("seaurchinroe", new ItemStack(ItemRegistry.seaUrchinRoe)));
        enc.addPage(new ItemPage("rayraw", new ItemStack(ItemRegistry.rawRay)));
        enc.addPage(new ItemPage("raycooked", new ItemStack(ItemRegistry.cookedRay)));

        // Drinks
        enc.addPage(new SectionPage("drinks"));
        enc.addPage(new ItemPage("mixer", new ItemStack(BlockRegistry.drinkMixer)));
        enc.addPage(new ItemPage("bamboomug", new ItemStack(ItemRegistry.bambooMug)));
        enc.addPage(new ItemPage("blackcoffee", MixerRecipes.getItemStack(Drink.blackCoffee)));
        enc.addPage(new ItemPage("caipirinha", MixerRecipes.getItemStack(Drink.caipirinha)));
        enc.addPage(new ItemPage("lemonade", MixerRecipes.getItemStack(Drink.lemonade)));
        enc.addPage(new ItemPage("limeade", MixerRecipes.getItemStack(Drink.limeade)));
        enc.addPage(new ItemPage("orangeade", MixerRecipes.getItemStack(Drink.orangeade)));
        enc.addPage(new ItemPage("pinacolada", MixerRecipes.getItemStack(Drink.pinaColada)));

        // Worldgen
        enc.addPage(new SectionPage("world"));
        enc.addPage(new ItemPage("azurite", new ItemStack(ItemRegistry.azurite)));
        enc.addPage(new ItemPage("zircon", new ItemStack(ItemRegistry.zircon)));
        enc.addPage(new ItemPage("eudialyte", new ItemStack(ItemRegistry.eudialyte)));
        for (TropicraftSands sand : TropicraftSands.VALUES) {
            enc.addPage(new ItemPage(sand.getSimpleName() + sand.getTypeName(), sand.makeStack(BlockRegistry.sands)));
        }
        
        // Tribespeople
        enc.addPage(new SectionPage("tribes"));
        enc.addPage(new KoaPage(EntityKoaBase.Roles.FISHERMAN, new ItemStack(Items.FISHING_ROD)));
        enc.addPage(new KoaPage(EntityKoaBase.Roles.HUNTER, new ItemStack(ItemRegistry.dagger)));
        addEntityEggPage(enc, 9); // Ashen
        enc.addPage(new MultiItemPage("ashenmask", Arrays.stream(AshenMasks.VALUES).map(v -> new ItemStack(ItemRegistry.maskMap.get(v))).toArray(ItemStack[]::new)));
        
        // Land Mobs
        enc.addPage(new SectionPage("mobs.land"));
        addEntityEggPage(enc, 0);   // iguana
        enc.addPage(new MultiEntityPage("treefrog", IntStream.range(2, 6).mapToObj(d -> new ItemStack(ItemRegistry.mobEgg, 1, d)).toArray(ItemStack[]::new)) {
            
            @Override
            protected @Nonnull EntityLivingBase[] makeVariants() {
                EntityTreeFrog[] ret = new EntityTreeFrog[4];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = (EntityTreeFrog) makeEntity();
                    ret[i].setType(i);
                }
                return ret;
            }
        });
        addEntityEggPage(enc, 6);   // eih
        addEntityEggPage(enc, 12);  // monkey
        addEntityEggPage(enc, 14);  // tropicreeper
        addEntityEggPage(enc, 15);  // tropiskelly
        addEntityEggPage(enc, 17);  // failgull
        addEntityEggPage(enc, 24);  // tropispider

        // Land Mobs
        enc.addPage(new SectionPage("mobs.water"));
        addEntityEggPage(enc, 1);   // starfish
        addEntityEggPage(enc, 7);   // marlin
        addEntityEggPage(enc, 8);   // fish
        enc.addPage(new EntityPage("turtle", new ItemStack(ItemRegistry.mobEgg, 1, 10)) {  // turtle
            
            @Override
            protected EntityLivingBase makeEntity() {
                EntitySeaTurtle ret = (EntitySeaTurtle) super.makeEntity();
                // All grown up
                ret.setMature();
                return ret;
            };
        });
        addEntityEggPage(enc, 11);  // mow
        addEntityEggPage(enc, 16);  // eagleray
        addEntityEggPage(enc, 18);  // seaurchin
        addEntityEggPage(enc, 19);  // piranha
        addEntityEggPage(enc, 20);  // sardine
        addEntityEggPage(enc, 21);  // dolphin
        addEntityEggPage(enc, 22);  // seahorse
        addEntityEggPage(enc, 23);  // hammerhead
        
        // Tools & Weapons
        enc.addPage(new SectionPage("tools"));
        enc.addPage(new ItemPage("dagger", new ItemStack(ItemRegistry.dagger)));
        enc.addPage(new ItemPage("bamboospear", new ItemStack(ItemRegistry.bambooSpear)));
        enc.addPage(new ItemPage("fishingrod", new ItemStack(ItemRegistry.fishingRod)));
        enc.addPage(new ItemPage("waterwand", new ItemStack(ItemRegistry.waterWand)));
        enc.addPage(new ItemPage("portalstarter",  new ItemStack(ItemRegistry.portalEnchanter)));
        enc.addPage(new ItemPage("fishingnet", new ItemStack(ItemRegistry.fishingNet)));
        enc.addPage(new ItemPage("fishbucket", new ItemStack(ItemRegistry.fishBucket)));
        //  enc.addPage(new ItemPage("enchantwand", new ItemStack(TCItemRegistry.enchantWand)));
        //TODO enc.addPage(new ItemPage("blowgun", new ItemStack(ItemRegistry.blowGun, 1, 0)));
        //  enc.addPage(new ItemPage("firestaff", new ItemStack(TCItemRegistry.staffFire)));
        /*
         * firestaff.title = Fire Staff
firestaff.desc = A mystical weapon usually wielded by a Koa Shaman which allows the holder to shoot fireballs that resemble meteors from the Catacombs
         */
        
        // All Armors
        enc.addPage(new SectionPage("armor"));
        enc.addPage(new ItemPage("firehelm", new ItemStack(ItemRegistry.fireHelmet)));
        enc.addPage(new ItemPage("firechestplate", new ItemStack(ItemRegistry.fireChestplate)));
        enc.addPage(new ItemPage("fireleggings", new ItemStack(ItemRegistry.fireLeggings)));
        enc.addPage(new ItemPage("fireboots", new ItemStack(ItemRegistry.fireBoots)));
        enc.addPage(new ItemPage("scalehelm", new ItemStack(ItemRegistry.scaleHelmet)));
        enc.addPage(new ItemPage("scaleleggings", new ItemStack(ItemRegistry.scaleLeggings)));
        enc.addPage(new ItemPage("scalechestplate", new ItemStack(ItemRegistry.scaleChestplate)));
        enc.addPage(new ItemPage("scaleboots", new ItemStack(ItemRegistry.scaleBoots)));

        // Scuba Gear
        enc.addPage(new SectionPage("scuba"));
        enc.addPage(new MultiItemPage("scubaflippers", new ItemStack(ItemRegistry.pinkFlippers), new ItemStack(ItemRegistry.yellowFlippers)));
        enc.addPage(new MultiItemPage("scubachestplate", new ItemStack(ItemRegistry.pinkChestplateGear), new ItemStack(ItemRegistry.yellowChestplateGear)));
        enc.addPage(new MultiItemPage("scubagoggles", new ItemStack(ItemRegistry.pinkScubaGoggles), new ItemStack(ItemRegistry.yellowScubaGoggles)));
        enc.addPage(new ItemPage("divecomputer", new ItemStack(ItemRegistry.diveComputer)));
        enc.addPage(new MultiItemPage("weightbelt", new ItemStack(ItemRegistry.yellowWeightBelt), new ItemStack(ItemRegistry.pinkWeightBelt)));
        enc.addPage(new MultiItemPage("ponybottle", new ItemStack(ItemRegistry.yellowPonyBottle), new ItemStack(ItemRegistry.pinkPonyBottle)));
        enc.addPage(new MultiItemPage("bcd", new ItemStack(ItemRegistry.pinkBCD), new ItemStack(ItemRegistry.yellowBCD)));
        enc.addPage(new MultiItemPage("regulator", new ItemStack(ItemRegistry.yellowRegulator), new ItemStack(ItemRegistry.pinkRegulator)));
        enc.addPage(new MultiItemPage("scubatank", new ItemStack(ItemRegistry.pinkScubaTank), new ItemStack(ItemRegistry.yellowScubaTank)));
        enc.addPage(new ItemPage("trimix", new ItemStack(ItemRegistry.trimix)));
                
        // Beach Accessories
        enc.addPage(new SectionPage("beach_accessories"));
        enc.addPage(new MultiItemPage("beachchair",     IntStream.range(0, 16).mapToObj(i -> new ItemStack(ItemRegistry.chair, 1, i)).toArray(ItemStack[]::new)));
        enc.addPage(new MultiItemPage("beachfloat",     IntStream.range(0, 16).mapToObj(i -> new ItemStack(ItemRegistry.beach_float, 1, i)).toArray(ItemStack[]::new)));
        enc.addPage(new MultiItemPage("beachumbrella",  IntStream.range(0, 16).mapToObj(i -> new ItemStack(ItemRegistry.umbrella, 1, i)).toArray(ItemStack[]::new)));
        
        // Nature
        enc.addPage(new SectionPage("nature"));
        enc.addPage(new ItemPage("reeds", new ItemStack(Items.REEDS)));
        enc.addPage(new ItemPage("bamboo", new ItemStack(BlockRegistry.bambooShoot)));
        enc.addPage(new ItemPage("coffeebean", new ItemStack(ItemRegistry.coffeeBeans, 1, 0)));
        enc.addPage(new MultiItemPage("coral", Arrays.stream(TropicraftCorals.VALUES).map(v -> new ItemStack(BlockRegistry.coral, 1, v.getMeta())).toArray(ItemStack[]::new)));
        enc.addPage(new ItemPage("kapok", new ItemStack(BlockRegistry.leaves, 1, 2)));
        enc.addPage(new ItemPage("mahogany", new ItemStack(BlockRegistry.logs, 1, 1)));
        enc.addPage(new ItemPage("lemonsapling", new ItemStack(BlockRegistry.saplings, 1, 2)));
        enc.addPage(new ItemPage("limesapling", new ItemStack(BlockRegistry.saplings, 1, 4)));
        enc.addPage(new ItemPage("orangesapling", new ItemStack(BlockRegistry.saplings, 1, 3)));
        enc.addPage(new ItemPage("grapefruitsapling", new ItemStack(BlockRegistry.saplings, 1, 1)));
        enc.addPage(new ItemPage("palmsapling", new ItemStack(BlockRegistry.saplings, 1, 0)));
        enc.addPage(new ItemPage("palmwood", new ItemStack(BlockRegistry.logs, 1, 0)));

        // Decor
        enc.addPage(new SectionPage("decor"));
        enc.addPage(new ItemPage("palmplanks", new ItemStack(BlockRegistry.planks, 1, 0))); //0 is palm, 1 is mahogany
        enc.addPage(new ItemPage("palmfence", new ItemStack(BlockRegistry.palmFence)));
        enc.addPage(new ItemPage("palmfencegate", new ItemStack(BlockRegistry.palmFenceGate)));
        enc.addPage(new ItemPage("mahoganyfence", new ItemStack(BlockRegistry.mahoganyFence)));
        enc.addPage(new ItemPage("mahoganyfencegate", new ItemStack(BlockRegistry.mahoganyFenceGate)));
        enc.addPage(new ItemPage("bambooblock", new ItemStack(BlockRegistry.bundles, 1, 1)));
        enc.addPage(new ItemPage("bamboochest", new ItemStack(BlockRegistry.bambooChest)));
        enc.addPage(new ItemPage("bamboodoor", new ItemStack(ItemRegistry.bambooDoor)));
        enc.addPage(new ItemPage("bamboofence", new ItemStack(BlockRegistry.bambooFence)));
        enc.addPage(new ItemPage("bamboofencegate", new ItemStack(BlockRegistry.bambooFenceGate)));
        enc.addPage(new ItemPage("bamboostick", new ItemStack(ItemRegistry.bambooStick)));
        enc.addPage(new ItemPage("bamboo_ladder", new ItemStack(BlockRegistry.bambooLadder)));
        enc.addPage(new ItemPage("tropiframe", new ItemStack(ItemRegistry.bambooItemFrame)));
        enc.addPage(new ItemPage("chunkohead", new ItemStack(BlockRegistry.chunk)));
        enc.addPage(new ItemPage("chunkfence", new ItemStack(BlockRegistry.chunkFence)));
        enc.addPage(new ItemPage("chunkfencegate", new ItemStack(BlockRegistry.chunkFenceGate)));
        enc.addPage(new ItemPage("tikitorch", new ItemStack(BlockRegistry.tikiTorch)));
        enc.addPage(new ItemPage("thatchblock", new ItemStack(BlockRegistry.bundles, 1, 0)));
        enc.addPage(new ItemPage("thatchfence", new ItemStack(BlockRegistry.thatchFence)));
        enc.addPage(new ItemPage("thatchfencegate", new ItemStack(BlockRegistry.thatchFenceGate)));

        // Flowers
        enc.addPage(new SectionPage("flowers"));
        for (TropicraftFlowers flower : TropicraftFlowers.VALUES) {
            enc.addPage(new ItemPage(flower.getSimpleName(), flower.makeStack(BlockRegistry.flowers)));
        }
        enc.addPage(new ItemPage("flowerpot", new ItemStack(ItemRegistry.flowerPot)));

        // All Shells
        enc.addPage(new SectionPage("shells"));
        for (TropicraftShells shell : TropicraftShells.values()) {
            enc.addPage(new ItemPage(shell.getSimpleName() + shell.getTypeName(), shell.makeStack(ItemRegistry.shell)));
        }
        enc.addPage(new ItemPage("pearlb", new ItemStack(ItemRegistry.blackPearl)));
        enc.addPage(new ItemPage("pearlw", new ItemStack(ItemRegistry.whitePearl)));

        // Music Discs
        enc.addPage(new SectionPage("music"));
        enc.addPage(new ItemPage("easternisles", new ItemStack(ItemRegistry.recordEasternIsles)));
        enc.addPage(new ItemPage("summering", new ItemStack(ItemRegistry.recordSummering)));
        enc.addPage(new ItemPage("thetribe", new ItemStack(ItemRegistry.recordTheTribe)));
        enc.addPage(new ItemPage("tradewinds", new ItemStack(ItemRegistry.recordTradeWinds)));
        enc.addPage(new ItemPage("lowtide", new ItemStack(ItemRegistry.recordLowTide)));
        // TODO buriedtreasure

        // Unsorted / ???
        enc.addPage(new SectionPage("misc"));


        enc.addPage(new ItemPage("coconutbomb", new ItemStack(ItemRegistry.coconutBomb)));



        
        //      TODO for (int i = 0; i < ItemCurare.effectNames.length; i++) {
        //          enc.addPage(new ItemPage("curare", new ItemStack(TCItemRegistry.curare, 1, i)));
        //      }
        //TODO enc.addPage(new ItemPage("curarebowl", new ItemStack(BlockRegistry.curareBowl)));
        //      TODO for (int i = 0; i < ItemCurare.effectNames.length; i++) {
        //          enc.addPage(new ItemPage("dart", new ItemStack(TCItemRegistry.dart, 1, i)));
        //      }
        enc.addPage(new ItemPage("fertilizer", new ItemStack(ItemRegistry.fertilizer)));
        

        //TODO enc.addPage(new ItemPage("flippers", new ItemStack(ItemRegistry.flippers)));
        //TODO enc.addPage(new ItemPage("flippers", new ItemStack(Items.LEATHER)));

        enc.addPage(new ItemPage("frogskin", new ItemStack(ItemRegistry.poisonFrogSkin)));

        //enc.addPage(new ItemPage("icestaff", new ItemStack(TCItemRegistry.staffIce)));
        enc.addPage(new ItemPage("iggyscale", new ItemStack(ItemRegistry.scale)));
        enc.addPage(new ItemPage("iguana_leather", new ItemStack(ItemRegistry.iguanaLeather)));
        //  enc.addPage(new ItemPage("journalpage", new ItemStack(TCItemRegistry.journalPage)));
        //TODO enc.addPage(new ItemPage("koachest", new ItemStack(BlockRegistry.koaChest)));
        //TODO enc.addPage(new ItemPage("leafball", new ItemStack(ItemRegistry.leafBall)));
        //enc.addPage(new ItemPage("leather", new ItemStack(Items.LEATHER)));

        //  enc.addPage(new ItemPage("nigeljournal", new ItemStack(TCItemRegistry.nigelJournal)));

        enc.addPage(new ItemPage("sifter", new ItemStack(BlockRegistry.sifter)));
        //TODO enc.addPage(new ItemPage("smeltedzircon", new ItemStack(ItemRegistry.ore, 1, 4)));
        //TODO enc.addPage(new ItemPage("snaretrap", new ItemStack(ItemRegistry.snareTrap)));
        //TODO enc.addPage(new ItemPage("snorkel", new ItemStack(ItemRegistry.snorkel)));


        //enc.addPage(new ItemPage("zirconium", new ItemStack(TCItemRegistry.ore, 1, 3)));

        enc.addPage(new SectionPage("lovetropics"));
        enc.addPage(new LoveTropicsPage("ltinfo"));
        enc.addPage(new LoveTropicsPage.Names("lt17", Names.LT17_NAMES));
        enc.addPage(new LoveTropicsPage.Names("lt18", Names.LT18_NAMES));

        enc.dumpSections();
    }
    
    private static void addEntityEggPage(TropicalBook book, int id) {
        book.addPage(new EntityPage(Names.EGG_NAMES[id], new ItemStack(ItemRegistry.mobEgg, 1, id)));
    }
}
