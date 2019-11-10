package net.tropicraft.core.client.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.TrashType;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropicraftLangProvider extends LanguageProvider {

    private static class AccessibleLanguageProvider extends LanguageProvider {

        public AccessibleLanguageProvider(DataGenerator gen, String modid, String locale) {
            super(gen, modid, locale);
        }

        @Override
        public void add(String key, String value) {
            super.add(key, value);
        }

        @Override
        protected void addTranslations() {}
    }

    private final AccessibleLanguageProvider upsideDown;

    public TropicraftLangProvider(DataGenerator gen) {
        super(gen, Constants.MODID, "en_us");
        this.upsideDown = new AccessibleLanguageProvider(gen, Constants.MODID, "en_ud");
    }

    @Override
    protected void addTranslations() {
        // BLOCKS

        addBlock(TropicraftBlocks.CHUNK, "Chunk O' Head");

        // Ores and storage blocks
        addBlock(TropicraftBlocks.AZURITE_ORE);
        addBlock(TropicraftBlocks.EUDIALYTE_ORE);
        addBlock(TropicraftBlocks.MANGANESE_ORE);
        addBlock(TropicraftBlocks.SHAKA_ORE);
        addBlock(TropicraftBlocks.ZIRCON_ORE);

        addBlock(TropicraftBlocks.AZURITE_BLOCK);
        addBlock(TropicraftBlocks.EUDIALYTE_BLOCK);
        addBlock(TropicraftBlocks.MANGANESE_BLOCK);
        addBlock(TropicraftBlocks.SHAKA_BLOCK);
        addBlock(TropicraftBlocks.ZIRCON_BLOCK);

        // All flowers
        TropicraftBlocks.FLOWERS.forEach((f, b) -> addBlock(b, f.getEnglishName()));

        // Sands
        addBlock(TropicraftBlocks.PURIFIED_SAND);
        addBlock(TropicraftBlocks.PACKED_PURIFIED_SAND);
        addBlock(TropicraftBlocks.CORAL_SAND);
        addBlock(TropicraftBlocks.FOAMY_SAND);
        addBlock(TropicraftBlocks.VOLCANIC_SAND);
        addBlock(TropicraftBlocks.MINERAL_SAND);

        // Bundles
        addBlock(TropicraftBlocks.BAMBOO_BUNDLE);
        addBlock(TropicraftBlocks.THATCH_BUNDLE);

        // Planks & Logs
        addBlock(TropicraftBlocks.MAHOGANY_PLANKS);
        addBlock(TropicraftBlocks.PALM_PLANKS);

        addBlock(TropicraftBlocks.MAHOGANY_LOG);
        addBlock(TropicraftBlocks.PALM_LOG);

        // Stairs & Slabs
        addBlock(TropicraftBlocks.BAMBOO_STAIRS);
        addBlock(TropicraftBlocks.THATCH_STAIRS);
        addBlock(TropicraftBlocks.CHUNK_STAIRS);
        addBlock(TropicraftBlocks.PALM_STAIRS);
        addBlock(TropicraftBlocks.MAHOGANY_STAIRS);
        addBlock(TropicraftBlocks.THATCH_STAIRS_FUZZY, "Thatch Roof");

        addBlock(TropicraftBlocks.BAMBOO_SLAB);
        addBlock(TropicraftBlocks.THATCH_SLAB);
        addBlock(TropicraftBlocks.CHUNK_SLAB);
        addBlock(TropicraftBlocks.PALM_SLAB);
        addBlock(TropicraftBlocks.MAHOGANY_SLAB);

        // Leaves
        addBlock(TropicraftBlocks.MAHOGANY_LEAVES);
        addBlock(TropicraftBlocks.PALM_LEAVES);
        addBlock(TropicraftBlocks.KAPOK_LEAVES);
        addBlock(TropicraftBlocks.FRUIT_LEAVES);
        addBlock(TropicraftBlocks.GRAPEFRUIT_LEAVES);
        addBlock(TropicraftBlocks.LEMON_LEAVES);
        addBlock(TropicraftBlocks.LIME_LEAVES);
        addBlock(TropicraftBlocks.ORANGE_LEAVES);

        // Saplings
        addBlock(TropicraftBlocks.MAHOGANY_SAPLING);
        addBlock(TropicraftBlocks.PALM_SAPLING);
        addBlock(TropicraftBlocks.GRAPEFRUIT_SAPLING);
        addBlock(TropicraftBlocks.LEMON_SAPLING);
        addBlock(TropicraftBlocks.LIME_SAPLING);
        addBlock(TropicraftBlocks.ORANGE_SAPLING);

        // Fences, Gates, and Walls
        addBlock(TropicraftBlocks.BAMBOO_FENCE);
        addBlock(TropicraftBlocks.THATCH_FENCE);
        addBlock(TropicraftBlocks.CHUNK_FENCE);
        addBlock(TropicraftBlocks.PALM_FENCE);
        addBlock(TropicraftBlocks.MAHOGANY_FENCE);

        addBlock(TropicraftBlocks.BAMBOO_FENCE_GATE);
        addBlock(TropicraftBlocks.THATCH_FENCE_GATE);
        addBlock(TropicraftBlocks.CHUNK_FENCE_GATE);
        addBlock(TropicraftBlocks.PALM_FENCE_GATE);
        addBlock(TropicraftBlocks.MAHOGANY_FENCE_GATE);

        addBlock(TropicraftBlocks.CHUNK_WALL);

        // Doors and Trapdoors
        addBlock(TropicraftBlocks.BAMBOO_DOOR);
        addBlock(TropicraftBlocks.THATCH_DOOR);
        addBlock(TropicraftBlocks.PALM_DOOR);
        addBlock(TropicraftBlocks.MAHOGANY_DOOR);

        addBlock(TropicraftBlocks.BAMBOO_TRAPDOOR);
        addBlock(TropicraftBlocks.THATCH_TRAPDOOR);
        addBlock(TropicraftBlocks.PALM_TRAPDOOR);
        addBlock(TropicraftBlocks.MAHOGANY_TRAPDOOR);

        // Misc remaining blocks
        addBlock(TropicraftBlocks.IRIS);
        addBlock(TropicraftBlocks.PINEAPPLE);

        addBlock(TropicraftBlocks.SMALL_BONGO_DRUM);
        addBlock(TropicraftBlocks.MEDIUM_BONGO_DRUM);
        addBlock(TropicraftBlocks.LARGE_BONGO_DRUM);

        addBlock(TropicraftBlocks.BAMBOO_LADDER);

        addBlock(TropicraftBlocks.BAMBOO_CHEST);
        add(Constants.MODID + ".container.bambooChest", "Bamboo Chest");
        add(Constants.MODID + ".container.bambooChestDouble", "Large Bamboo Chest");
        addBlockWithTooltip(TropicraftBlocks.SIFTER, "Place any type of tropics or regular sand in the sifter. What treasures are hidden inside?");
        addBlockWithTooltip(TropicraftBlocks.DRINK_MIXER, "Place two drink ingredients on the mixer, then place an empty mug on the base, then ???, then enjoy!");

        addBlock(TropicraftBlocks.VOLCANO);

        addBlock(TropicraftBlocks.TIKI_TORCH);

        addBlock(TropicraftBlocks.COCONUT);

        addBlock(TropicraftBlocks.BAMBOO_FLOWER_POT);
        TropicraftBlocks.ALL_POTTED_PLANTS.forEach(this::addBlock);

        addBlock(TropicraftBlocks.WATER_BARRIER);
        Arrays.stream(TrashType.values()).forEach(this::addBlock);
        
        // ITEMS
        
        // Gems/Ingots
        addItem(TropicraftItems.AZURITE);
        addItem(TropicraftItems.EUDIALYTE);
        addItem(TropicraftItems.ZIRCON);
        addItem(TropicraftItems.SHAKA);
        addItem(TropicraftItems.MANGANESE);
        
        // All Umbrellas
        TropicraftItems.UMBRELLAS.values().forEach(this::addItem);
        // All Chairs
        TropicraftItems.CHAIRS.values().forEach(this::addItem);
        // All Beach Floats
        TropicraftItems.BEACH_FLOATS.values().forEach(this::addItem);
        
        // Bamboo Items
        addItem(TropicraftItems.BAMBOO_STICK);
        addItem(TropicraftItems.BAMBOO_SPEAR);
        addItem(TropicraftItems.BAMBOO_ITEM_FRAME);
        
        // Shells
        addItem(TropicraftItems.SOLONOX_SHELL);
        addItem(TropicraftItems.FROX_CONCH);
        addItem(TropicraftItems.PAB_SHELL);
        addItem(TropicraftItems.RUBE_NAUTILUS);
        addItem(TropicraftItems.STARFISH);
        addItem(TropicraftItems.TURTLE_SHELL);
        addItem(TropicraftItems.LOVE_TROPICS_SHELL);
        add("item.tropicraft.shell.owned.normal", "%s's Shell");
        add("item.tropicraft.shell.owned.with_s", "%s' Shell");
        
        // Fruits
        addItem(TropicraftItems.LEMON);
        addItem(TropicraftItems.LIME);
        addItem(TropicraftItems.GRAPEFRUIT);
        addItem(TropicraftItems.ORANGE);
        addItem(TropicraftItems.PINEAPPLE_CUBES);
        addItem(TropicraftItems.COCONUT_CHUNK);
        addItem(TropicraftItems.RAW_COFFEE_BEAN);
        addItem(TropicraftItems.ROASTED_COFFEE_BEAN);
        addItem(TropicraftItems.COFFEE_BERRY);

        // Cocktails
        addItem(TropicraftItems.BAMBOO_MUG);
        // Override pina colada with proper spelling
        TropicraftItems.COCKTAILS.forEach((d, c) -> {
            if (d == Drink.PINA_COLADA) {
                addItem(c, "Pi\u00F1a Colada");
            } else {
                addItem(c);
            }
        });
        
        // Trade items
        addItem(TropicraftItems.WHITE_PEARL);
        addItem(TropicraftItems.BLACK_PEARL);
        addItem(TropicraftItems.SCALE);

        // Food
        addItem(TropicraftItems.FRESH_MARLIN);
        addItem(TropicraftItems.SEARED_MARLIN);
        addItem(TropicraftItems.RAW_RAY);
        addItem(TropicraftItems.COOKED_RAY);
        addItem(TropicraftItems.FROG_LEG);
        addItem(TropicraftItems.COOKED_FROG_LEG);
        addItem(TropicraftItems.SEA_URCHIN_ROE);
        addItem(TropicraftItems.TOASTED_NORI);
        addItem(TropicraftItems.RAW_FISH);
        addItem(TropicraftItems.COOKED_FISH);
        addItem(TropicraftItems.POISON_FROG_SKIN);
        
        // Mob drops
        addItem(TropicraftItems.IGUANA_LEATHER);
        
        // Misc
        addItem(TropicraftItems.TROPICAL_FERTILIZER);
        
        // Discs
        TropicraftItems.MUSIC_DISCS.values().forEach(item -> addItemWithTooltip(item, "Music Disc", item.get().getType().getTooltip()));

        // Spawn Eggs
        addItem(TropicraftItems.KOA_SPAWN_EGG, "Koa Headband");
        addItem(TropicraftItems.TROPICREEPER_SPAWN_EGG, "TropiCreeper Hat");
        addItem(TropicraftItems.IGUANA_SPAWN_EGG);
        addItem(TropicraftItems.TROPISKELLY_SPAWN_EGG, "TropiSkelly Skirt");
        addItem(TropicraftItems.EIH_SPAWN_EGG, "Eye of Head");
        addItem(TropicraftItems.SEA_TURTLE_SPAWN_EGG);
        addItem(TropicraftItems.MARLIN_SPAWN_EGG);
        addItem(TropicraftItems.FAILGULL_SPAWN_EGG);
        addItem(TropicraftItems.DOLPHIN_SPAWN_EGG);
        addItem(TropicraftItems.SEAHORSE_SPAWN_EGG);
        
        // ENTITIES
        
        addEntityType(TropicraftEntities.KOA_HUNTER, "Koa");
        addEntityType(TropicraftEntities.TROPI_CREEPER, "Tropicreeper");
        addEntityType(TropicraftEntities.IGUANA);
        addEntityType(TropicraftEntities.UMBRELLA);
        addEntityType(TropicraftEntities.CHAIR);
        addEntityType(TropicraftEntities.BEACH_FLOAT);
        addEntityType(TropicraftEntities.TROPI_SKELLY, "Tropiskelly");
        addEntityType(TropicraftEntities.EIH, "Easter Island Head");
        addEntityType(TropicraftEntities.WALL_ITEM);
        addEntityType(TropicraftEntities.BAMBOO_ITEM_FRAME);
        // TODO: Register again when volcano eruption is finished
        //addEntityType(TropicraftEntities.LAVA_BALL);
        addEntityType(TropicraftEntities.SEA_TURTLE);
        addEntityType(TropicraftEntities.MARLIN);
        addEntityType(TropicraftEntities.FAILGULL);
        addEntityType(TropicraftEntities.DOLPHIN);
        addEntityType(TropicraftEntities.SEAHORSE);
        
        // MISC
        
        add(Tropicraft.TROPICRAFT_ITEM_GROUP, "Tropicraft");
        add(Tropicraft.LOVE_TROPICS_ITEM_GROUP, "Love Tropics");

        add(TropicraftLangKeys.COMMAND_MINIGAME_ALREADY_REGISTERED, "Minigame already registered with the following ID: %s");
        add(TropicraftLangKeys.COMMAND_MINIGAME_NOT_REGISTERED, "Minigame with that ID has not been registered: %s");
        add(TropicraftLangKeys.COMMAND_MINIGAME_ID_INVALID, "A minigame with that ID doesn't exist!");
        add(TropicraftLangKeys.COMMAND_MINIGAME_ALREADY_STARTED, "Another minigame is already in progress! Stop that one first before polling another.");
        add(TropicraftLangKeys.COMMAND_ANOTHER_MINIGAME_POLLING, "Another minigame is already polling! Stop that one first before polling another.");
        add(TropicraftLangKeys.COMMAND_MINIGAME_POLLING, "Minigame %s is polling. Type %s to get a chance to play!");
        add(TropicraftLangKeys.COMMAND_SORRY_ALREADY_STARTED, "Sorry, the current minigame has already started!");
        add(TropicraftLangKeys.COMMAND_NO_MINIGAME_POLLING, "There is no minigame currently polling.");
        add(TropicraftLangKeys.COMMAND_REGISTERED_FOR_MINIGAME, "You have registered for Minigame %s. When the minigame starts, random registered players will be picked to play. Please wait for hosts to start the minigame. You can continue to do what you were doing until then.");
        add(TropicraftLangKeys.COMMAND_NOT_REGISTERED_FOR_MINIGAME, "You are not currently registered for any minigames.");
        add(TropicraftLangKeys.COMMAND_UNREGISTERED_MINIGAME, "You have unregistered for Minigame %s.");
        add(TropicraftLangKeys.COMMAND_ENTITY_NOT_PLAYER, "Entity that attempted command is not player.");
        add(TropicraftLangKeys.COMMAND_MINIGAME_POLLED, "Minigame successfully polled!");
        add(TropicraftLangKeys.COMMAND_NOT_ENOUGH_PLAYERS, "There aren't enough players to start this minigame. It requires at least %s amount of players.");
        add(TropicraftLangKeys.COMMAND_MINIGAME_STARTED, "You have started the minigame.");
        add(TropicraftLangKeys.MINIGAME_SURVIVE_THE_TIDE, "Survive The Tide");
        add(TropicraftLangKeys.MINIGAME_SIGNATURE_RUN, "Signature Run");
        add(TropicraftLangKeys.MINIGAME_UNDERWATER_TRASH_HUNT, "Underwater Trash Hunt");
        add(TropicraftLangKeys.COMMAND_NO_LONGER_ENOUGH_PLAYERS, "There are no longer enough players to start the minigame!");
        add(TropicraftLangKeys.COMMAND_ENOUGH_PLAYERS, "There are now enough players to start the minigame!");
        add(TropicraftLangKeys.COMMAND_NO_MINIGAME, "There is no currently running minigame to stop!");
        add(TropicraftLangKeys.COMMAND_STOPPED_MINIGAME, "You have stopped the %s minigame.");
        add(TropicraftLangKeys.COMMAND_FINISHED_MINIGAME, "The minigame %s has finished. If you were inside the minigame, you have been teleported back to your original position.");
        add(TropicraftLangKeys.COMMAND_MINIGAME_STOPPED_POLLING, "An operator has stopped polling the minigame %s.");
        add(TropicraftLangKeys.SURVIVE_THE_TIDE_FINISH, "Through the rising sea levels, the volatile and chaotic weather, and the struggle to survive, one player remains. The lone survivor of the island, %s, has won - but at what cost? The world is not what it once was, and they must survive in this new apocalyptic land.");
        add(TropicraftLangKeys.MINIGAME_FINISH, "The minigame will end in 10 seconds...");
        add(TropicraftLangKeys.SURVIVE_THE_TIDE_START, "The year is... 2050. Human caused climate change has gone unmitigated and the human population was forced to flee to higher ground. " +
                "Fortunately for you, you live on an island with mountains nearby. The same cannot be said for the millions on the coastlines. Your task, should you choose to accept " +
                "it, which you have to because of climate change, is to reach the last remaining high places and ride out the storm. Oh right, did I not mention the severe storms " +
                "that happen sporadically and frequently these days because of the instability in the climate? Well, now you know. Work together or work against each other to reach the " +
                "top, your true enemy is the rising sea currents. Just remember... Your resources are as limited as your time. Someone else may have the tool or food you need to survive. " +
                "What kind of person will you be when the world is falling apart?\n\n" +
                "Let's see!");
        add(TropicraftLangKeys.SURVIVE_THE_TIDE_PVP_DISABLED, "NOTE: PvP is disabled for %s minutes! Go fetch resources before time runs out.");
        add(TropicraftLangKeys.SURVIVE_THE_TIDE_PVP_ENABLED, "WARNING: PVP HAS BEEN ENABLED! Beware of other players...");
    }
    
    private String getAutomaticName(Supplier<? extends IForgeRegistryEntry<?>> sup) {
        return Util.toEnglishName(sup.get().getRegistryName().getPath());
    }
    
    private void addBlock(Supplier<? extends Block> block) {
        addBlock(block, getAutomaticName(block));
    }
    
    private void addBlockWithTooltip(Supplier<? extends Block> block, String tooltip) {
        addBlock(block);
        addTooltip(block, tooltip);
    }
    
    private void addItem(Supplier<? extends Item> item) {
        addItem(item, getAutomaticName(item));
    }
    
    private void addItemWithTooltip(Supplier<? extends Item> block, String name, List<String> tooltip) {
        addItem(block, name);
        addTooltip(block, tooltip);
    }
    
    private void addTooltip(Supplier<? extends IItemProvider> item, String tooltip) {
        add(item.get().asItem().getTranslationKey() + ".desc", tooltip);
    }
    
    private void addTooltip(Supplier<? extends IItemProvider> item, List<String> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            add(item.get().asItem().getTranslationKey() + ".desc." + i, tooltip.get(i));
        }
    }
    
    private void add(ItemGroup group, String name) {
        add(group.getTranslationKey(), name);
    }
    
    private void addEntityType(Supplier<? extends EntityType<?>> entity) {
        addEntityType(entity, getAutomaticName(entity));
    }
    
    // Automatic en_ud generation

    private static final String NORMAL_CHARS = 
            /* lowercase */ "abcdefghijklmn\u00F1opqrstuvwxyz" +
            /* uppercase */ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            /*  numbers  */ "0123456789" +
            /*  special  */ "_,;.?!/\\'";
    private static final String UPSIDE_DOWN_CHARS = 
            /* lowercase */ "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029E\u05DF\u026Fuuodb\u0279s\u0287n\u028C\u028Dx\u028Ez" +
            /* uppercase */ "\u2C6F\u15FA\u0186\u15E1\u018E\u2132\u2141HI\u017F\u029E\uA780WNO\u0500\u1F49\u1D1AS\u27D8\u2229\u039BMX\u028EZ" +
            /*  numbers  */ "0\u0196\u1105\u0190\u3123\u03DB9\u312586" +
            /*  special  */ "\u203E'\u061B\u02D9\u00BF\u00A1/\\,";
    
    static {
        if (NORMAL_CHARS.length() != UPSIDE_DOWN_CHARS.length()) {
            throw new AssertionError("Char maps do not match in length!");
        }
    }

    private String toUpsideDown(String normal) {
        char[] ud = new char[normal.length()];
        for (int i = 0; i < normal.length(); i++) {
            char c = normal.charAt(i);
            if (c == '%') {
                String fmtArg = "";
                while (Character.isDigit(c) || c == '%' || c == '$' || c == 's' || c == 'd') { // TODO this is a bit lazy
                    fmtArg += c;
                    i++;
                    c = i == normal.length() ? 0 : normal.charAt(i);
                }
                i--;
                for (int j = 0; j < fmtArg.length(); j++) {
                    ud[normal.length() - 1 - i + j] = fmtArg.charAt(j);
                }
                continue;
            }
            int lookup = NORMAL_CHARS.indexOf(c);
            if (lookup >= 0) {
                c = UPSIDE_DOWN_CHARS.charAt(lookup);
            }
            ud[normal.length() - 1 - i] = c;
        }
        return new String(ud);
    }

    @Override
    protected void add(String key, String value) {
        super.add(key, value);
        upsideDown.add(key, toUpsideDown(value));
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        super.act(cache);
        upsideDown.act(cache);
    }
}
