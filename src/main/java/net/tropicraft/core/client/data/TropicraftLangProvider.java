package net.tropicraft.core.client.data;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.TropicraftBlocks;
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
        super(gen, Info.MODID, "en_us");
        this.upsideDown = new AccessibleLanguageProvider(gen, Info.MODID, "en_ud");
    }

    /**
     * {
  "block.tropicraft.chunk": "Chunk O' Head",
  "block.tropicraft.azurite_ore": "Azurite Ore",
  "block.tropicraft.acai_vine": "Acai Vine",
  "block.tropicraft.bamboo_bundle": "Bamboo Bundle",
  "block.tropicraft.thatch_bundle": "Thatch Bundle",
  "item.tropicraft.shell.owned.normal": "\s's Shell",
  "item.tropicraft.shell.owned.with_s": "\s' Shell"
}
     */
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
        add(Info.MODID + ".container.bambooChest", "Bamboo Chest");
        add(Info.MODID + ".container.bambooChestDouble", "Large Bamboo Chest");
        addBlockWithTooltip(TropicraftBlocks.SIFTER, "Place any type of tropics or regular sand in the sifter. What treasures are hidden inside?");
        addBlockWithTooltip(TropicraftBlocks.DRINK_MIXER, "Place two drink ingredients on the mixer, then place an empty mug on the base, then ???, then enjoy!");

        addBlock(TropicraftBlocks.VOLCANO);

        addBlock(TropicraftBlocks.TIKI_TORCH);

        addBlock(TropicraftBlocks.COCONUT);

        addBlock(TropicraftBlocks.BAMBOO_FLOWER_POT);
        TropicraftBlocks.BAMBOO_POTTED_TROPICS_PLANTS.forEach(this::addBlock);
        TropicraftBlocks.VANILLA_POTTED_TROPICS_PLANTS.forEach(this::addBlock);
        TropicraftBlocks.BAMBOO_POTTED_VANILLA_PLANTS.forEach(this::addBlock);

        addBlock(TropicraftBlocks.WATER_BARRIER);
        
        // ITEMS
        
        // Gems/Ingots
        addItem(TropicraftItems.AZURITE);
        addItem(TropicraftItems.EUDIALYTE);
        addItem(TropicraftItems.ZIRCON);
        addItem(TropicraftItems.SHAKA);
        addItem(TropicraftItems.MANGANESE);
        
        // All Umbrellas
        TropicraftItems.UMBRELLAS.values().forEach(this::addItem);
        
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
        TropicraftItems.COCKTAILS.values().forEach(this::addItem);
        
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
        
        // Discs
        TropicraftItems.MUSIC_DISCS.values().forEach(item -> addItemWithTooltip(item, "Music Disc", item.get().getType().getTooltip()));
        
        // MISC
        
        add(Tropicraft.TROPICRAFT_ITEM_GROUP, "Tropicraft");
        add(Tropicraft.LOVE_TROPICS_ITEM_GROUP, "Love Tropics");
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
    
    // Automatic en_ud generation

    private static final String NORMAL_CHARS = 
            /* lowercase */ "abcdefghijklmnopqrstuvwxyz" +
            /* uppercase */ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            /*  numbers  */ "0123456789" +
            /*  special  */ "_,;.?!/\\'";
    private static final String UPSIDE_DOWN_CHARS = 
            /* lowercase */ "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029E\u05DF\u026Fuodb\u0279s\u0287n\u028C\u028Dx\u028Ez" +
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
                    c = normal.charAt(++i);
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
