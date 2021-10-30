package net.tropicraft.core.client.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

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

        // Mud
        addBlock(TropicraftBlocks.MUD);
        addBlock(TropicraftBlocks.MUD_WITH_PIANGUAS);

        // Bundles
        addBlock(TropicraftBlocks.BAMBOO_BUNDLE);
        addBlock(TropicraftBlocks.THATCH_BUNDLE);

        // Planks & Logs
        addBlock(TropicraftBlocks.MAHOGANY_PLANKS);
        addBlock(TropicraftBlocks.PALM_PLANKS);

        addBlock(TropicraftBlocks.MAHOGANY_LOG);
        addBlock(TropicraftBlocks.PALM_LOG);

        addBlock(TropicraftBlocks.MAHOGANY_WOOD);
        addBlock(TropicraftBlocks.PALM_WOOD);

        addBlock(TropicraftBlocks.PAPAYA_LOG);
        addBlock(TropicraftBlocks.PAPAYA_WOOD);

        addBlock(TropicraftBlocks.RED_MANGROVE_LOG);
        addBlock(TropicraftBlocks.RED_MANGROVE_WOOD);
        addBlock(TropicraftBlocks.RED_MANGROVE_ROOTS);

        addBlock(TropicraftBlocks.LIGHT_MANGROVE_LOG);
        addBlock(TropicraftBlocks.LIGHT_MANGROVE_WOOD);
        addBlock(TropicraftBlocks.LIGHT_MANGROVE_ROOTS);

        addBlock(TropicraftBlocks.BLACK_MANGROVE_LOG);
        addBlock(TropicraftBlocks.BLACK_MANGROVE_WOOD);
        addBlock(TropicraftBlocks.BLACK_MANGROVE_ROOTS);

        addBlock(TropicraftBlocks.STRIPPED_MANGROVE_LOG);
        addBlock(TropicraftBlocks.STRIPPED_MANGROVE_WOOD);
        addBlock(TropicraftBlocks.MANGROVE_PLANKS);

        // Stairs & Slabs
        addBlock(TropicraftBlocks.BAMBOO_STAIRS);
        addBlock(TropicraftBlocks.THATCH_STAIRS);
        addBlock(TropicraftBlocks.CHUNK_STAIRS);
        addBlock(TropicraftBlocks.PALM_STAIRS);
        addBlock(TropicraftBlocks.MAHOGANY_STAIRS);
        addBlock(TropicraftBlocks.THATCH_STAIRS_FUZZY, "Thatch Roof");
        addBlock(TropicraftBlocks.MANGROVE_STAIRS);

        addBlock(TropicraftBlocks.BAMBOO_SLAB);
        addBlock(TropicraftBlocks.THATCH_SLAB);
        addBlock(TropicraftBlocks.CHUNK_SLAB);
        addBlock(TropicraftBlocks.PALM_SLAB);
        addBlock(TropicraftBlocks.MAHOGANY_SLAB);
        addBlock(TropicraftBlocks.MANGROVE_SLAB);

        // Leaves
        addBlock(TropicraftBlocks.MAHOGANY_LEAVES);
        addBlock(TropicraftBlocks.PALM_LEAVES);
        addBlock(TropicraftBlocks.KAPOK_LEAVES);
        addBlock(TropicraftBlocks.FRUIT_LEAVES);
        addBlock(TropicraftBlocks.GRAPEFRUIT_LEAVES);
        addBlock(TropicraftBlocks.LEMON_LEAVES);
        addBlock(TropicraftBlocks.LIME_LEAVES);
        addBlock(TropicraftBlocks.ORANGE_LEAVES);
        addBlock(TropicraftBlocks.RED_MANGROVE_LEAVES);
        addBlock(TropicraftBlocks.TALL_MANGROVE_LEAVES);
        addBlock(TropicraftBlocks.TEA_MANGROVE_LEAVES);
        addBlock(TropicraftBlocks.BLACK_MANGROVE_LEAVES);
        addBlock(TropicraftBlocks.PAPAYA_LEAVES);

        // Saplings
        addBlock(TropicraftBlocks.MAHOGANY_SAPLING);
        addBlock(TropicraftBlocks.PALM_SAPLING);
        addBlock(TropicraftBlocks.GRAPEFRUIT_SAPLING);
        addBlock(TropicraftBlocks.LEMON_SAPLING);
        addBlock(TropicraftBlocks.LIME_SAPLING);
        addBlock(TropicraftBlocks.ORANGE_SAPLING);
        addBlock(TropicraftBlocks.PAPAYA_SAPLING);
        addBlockWithTooltip(TropicraftBlocks.RED_MANGROVE_PROPAGULE, "Rhizophora mangle");
        addBlockWithTooltip(TropicraftBlocks.TALL_MANGROVE_PROPAGULE, "Rhizophora racemosa");
        addBlockWithTooltip(TropicraftBlocks.TEA_MANGROVE_PROPAGULE, "Pelliciera rhizophorae");
        addBlockWithTooltip(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE, "Avicennia germinans");

        // Fences, Gates, and Walls
        addBlock(TropicraftBlocks.BAMBOO_FENCE);
        addBlock(TropicraftBlocks.THATCH_FENCE);
        addBlock(TropicraftBlocks.CHUNK_FENCE);
        addBlock(TropicraftBlocks.PALM_FENCE);
        addBlock(TropicraftBlocks.MAHOGANY_FENCE);
        addBlock(TropicraftBlocks.MANGROVE_FENCE);

        addBlock(TropicraftBlocks.BAMBOO_FENCE_GATE);
        addBlock(TropicraftBlocks.THATCH_FENCE_GATE);
        addBlock(TropicraftBlocks.CHUNK_FENCE_GATE);
        addBlock(TropicraftBlocks.PALM_FENCE_GATE);
        addBlock(TropicraftBlocks.MAHOGANY_FENCE_GATE);
        addBlock(TropicraftBlocks.MANGROVE_FENCE_GATE);

        addBlock(TropicraftBlocks.CHUNK_WALL);

        // Doors and Trapdoors
        addBlock(TropicraftBlocks.BAMBOO_DOOR);
        addBlock(TropicraftBlocks.THATCH_DOOR);
        addBlock(TropicraftBlocks.PALM_DOOR);
        addBlock(TropicraftBlocks.MAHOGANY_DOOR);
        addBlock(TropicraftBlocks.MANGROVE_DOOR);

        addBlock(TropicraftBlocks.BAMBOO_TRAPDOOR);
        addBlock(TropicraftBlocks.THATCH_TRAPDOOR);
        addBlock(TropicraftBlocks.PALM_TRAPDOOR);
        addBlock(TropicraftBlocks.MAHOGANY_TRAPDOOR);
        addBlock(TropicraftBlocks.MANGROVE_TRAPDOOR);

        // Misc remaining blocks
        addBlock(TropicraftBlocks.IRIS);
        addBlock(TropicraftBlocks.PINEAPPLE);

        addBlock(TropicraftBlocks.SMALL_BONGO_DRUM);
        addBlock(TropicraftBlocks.MEDIUM_BONGO_DRUM);
        addBlock(TropicraftBlocks.LARGE_BONGO_DRUM);

        addBlock(TropicraftBlocks.BAMBOO_LADDER);

        addBlock(TropicraftBlocks.BAMBOO_BOARDWALK);
        addBlock(TropicraftBlocks.PALM_BOARDWALK);
        addBlock(TropicraftBlocks.MAHOGANY_BOARDWALK);
        addBlock(TropicraftBlocks.MANGROVE_BOARDWALK);

        addBlock(TropicraftBlocks.BAMBOO_CHEST);
        add(Constants.MODID + ".container.bambooChest", "Bamboo Chest");
        add(Constants.MODID + ".container.bambooChestDouble", "Large Bamboo Chest");
        addBlockWithTooltip(TropicraftBlocks.SIFTER, "Place any type of tropics or regular sand in the sifter. What treasures are hidden inside?");
        addBlockWithTooltip(TropicraftBlocks.DRINK_MIXER, "Place two drink ingredients on the mixer, then place an empty mug on the base, then ???, then enjoy!");
        addBlockWithTooltip(TropicraftBlocks.AIR_COMPRESSOR, "Place an empty scuba harness in the compressor to fill it with air!");

        addBlock(TropicraftBlocks.VOLCANO);

        addBlock(TropicraftBlocks.TIKI_TORCH);

        addBlock(TropicraftBlocks.COCONUT);

        addBlock(TropicraftBlocks.PAPAYA);

        addBlock(TropicraftBlocks.BAMBOO_FLOWER_POT);
        TropicraftBlocks.ALL_POTTED_PLANTS.forEach(this::addBlock);

        addBlock(TropicraftBlocks.ZIRCONIUM_BLOCK);
        addBlock(TropicraftBlocks.COFFEE_BUSH);

        // TODO: remove explicit names once we change ids to match
        addBlock(TropicraftBlocks.GOLDEN_LEATHER_FERN, "Golden Leather Fern");
        addBlock(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN);
        addBlock(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN, "Large Golden Leather Fern");

        addBlock(TropicraftBlocks.REEDS);
        
        // ITEMS
        
        // Gems/Ingots
        addItem(TropicraftItems.AZURITE);
        addItem(TropicraftItems.EUDIALYTE);
        addItem(TropicraftItems.ZIRCON);
        addItem(TropicraftItems.ZIRCONIUM, "Zirconium");
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
        addItem(TropicraftItems.TROPICAL_FISH_BUCKET);
        addItem(TropicraftItems.PIRANHA_BUCKET);
        addItem(TropicraftItems.SARDINE_BUCKET);
        addItem(TropicraftItems.DAGGER);
        TropicraftItems.ASHEN_MASKS.values().forEach(item ->addItem(item, item.get().getMaskType().getName()));
        addItem(TropicraftItems.BLOW_GUN);
        addItem(TropicraftItems.NIGEL_STACHE, "Nigel's Moustache");
        addItem(TropicraftItems.WATER_WAND);
        addItem(TropicraftItems.EXPLODING_COCONUT);
        addItem(TropicraftItems.FISHING_NET);

        addItem(TropicraftItems.PIANGUAS);
        
        // Tools
        addItem(TropicraftItems.ZIRCON_AXE);
        addItem(TropicraftItems.ZIRCON_HOE);
        addItem(TropicraftItems.ZIRCON_PICKAXE);
        addItem(TropicraftItems.ZIRCON_SHOVEL);
        addItem(TropicraftItems.ZIRCON_SWORD);
        addItem(TropicraftItems.ZIRCONIUM_AXE);
        addItem(TropicraftItems.ZIRCONIUM_HOE);
        addItem(TropicraftItems.ZIRCONIUM_PICKAXE);
        addItem(TropicraftItems.ZIRCONIUM_SHOVEL);
        addItem(TropicraftItems.ZIRCONIUM_SWORD);
        addItem(TropicraftItems.EUDIALYTE_AXE);
        addItem(TropicraftItems.EUDIALYTE_HOE);
        addItem(TropicraftItems.EUDIALYTE_PICKAXE);
        addItem(TropicraftItems.EUDIALYTE_SHOVEL);
        addItem(TropicraftItems.EUDIALYTE_SWORD);
        
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
        addItem(TropicraftItems.TREE_FROG_SPAWN_EGG);
        addItem(TropicraftItems.SEA_URCHIN_SPAWN_EGG);
        addItem(TropicraftItems.V_MONKEY_SPAWN_EGG, "Vervet Monkey Spawn Egg");
        addItem(TropicraftItems.PIRANHA_SPAWN_EGG);
        addItem(TropicraftItems.SARDINE_SPAWN_EGG);
        addItem(TropicraftItems.TROPICAL_FISH_SPAWN_EGG);
        addItem(TropicraftItems.EAGLE_RAY_SPAWN_EGG);
        addItem(TropicraftItems.TROPI_SPIDER_SPAWN_EGG);
        addItem(TropicraftItems.ASHEN_SPAWN_EGG, "Ashen Ash");
        addItem(TropicraftItems.HAMMERHEAD_SPAWN_EGG);
        addItem(TropicraftItems.COWKTAIL_SPAWN_EGG);
        addItem(TropicraftItems.MAN_O_WAR_SPAWN_EGG);
        addItem(TropicraftItems.TROPIBEE_SPAWN_EGG);
        addItem(TropicraftItems.TAPIR_SPAWN_EGG);
        addItem(TropicraftItems.JAGUAR_SPAWN_EGG);
        addItem(TropicraftItems.BROWN_BASILISK_LIZARD_SPAWN_EGG);
        addItem(TropicraftItems.GREEN_BASILISK_LIZARD_SPAWN_EGG);
        addItem(TropicraftItems.HUMMINGBIRD_SPAWN_EGG);
        addItem(TropicraftItems.FIDDLER_CRAB_SPAWN_EGG);
        addItem(TropicraftItems.SPIDER_MONKEY_SPAWN_EGG);
        addItem(TropicraftItems.WHITE_LIPPED_PECCARY_SPAWN_EGG);
        addItem(TropicraftItems.CUBERA_SPAWN_EGG);

        // Armor
        addItem(TropicraftItems.FIRE_BOOTS);
        addItem(TropicraftItems.FIRE_CHESTPLATE);
        addItem(TropicraftItems.FIRE_HELMET);
        addItem(TropicraftItems.FIRE_LEGGINGS);
        addItem(TropicraftItems.SCALE_BOOTS);
        addItem(TropicraftItems.SCALE_CHESTPLATE);
        addItem(TropicraftItems.SCALE_HELMET);
        addItem(TropicraftItems.SCALE_LEGGINGS);
        
        // Scuba
        addItem(TropicraftItems.YELLOW_SCUBA_GOGGLES);
        addItem(TropicraftItems.YELLOW_SCUBA_HARNESS);
        addItem(TropicraftItems.YELLOW_SCUBA_FLIPPERS);
        addItem(TropicraftItems.PINK_SCUBA_GOGGLES);
        addItem(TropicraftItems.PINK_SCUBA_HARNESS);
        addItem(TropicraftItems.PINK_SCUBA_FLIPPERS);
        
        addItem(TropicraftItems.YELLOW_PONY_BOTTLE);
        addItem(TropicraftItems.PINK_PONY_BOTTLE);

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
        addEntityType(TropicraftEntities.TREE_FROG);
        addEntityType(TropicraftEntities.SEA_URCHIN);
        addEntityType(TropicraftEntities.SEA_URCHIN_EGG_ENTITY, "Sea Urchin Egg");
        addEntityType(TropicraftEntities.STARFISH);
        addEntityType(TropicraftEntities.STARFISH_EGG);
        addEntityType(TropicraftEntities.PIRANHA);
        addEntityType(TropicraftEntities.RIVER_SARDINE);
        addEntityType(TropicraftEntities.TROPICAL_FISH);
        addEntityType(TropicraftEntities.EAGLE_RAY, "Spotted Eagle Ray");
        addEntityType(TropicraftEntities.TROPI_SPIDER);
        addEntityType(TropicraftEntities.TROPI_SPIDER_EGG, "Tropics Spider Egg");
        addEntityType(TropicraftEntities.ASHEN);
        addEntityType(TropicraftEntities.ASHEN_MASK);
        addEntityType(TropicraftEntities.EXPLODING_COCONUT);
        addEntityType(TropicraftEntities.SEA_TURTLE_EGG, "Sea Turtle Egg");
        addEntityType(TropicraftEntities.COWKTAIL);
        addEntityType(TropicraftEntities.MAN_O_WAR, "Man o' War");
        addEntityType(TropicraftEntities.TROPI_BEE, "Tropibee");
        addEntityType(TropicraftEntities.V_MONKEY, "Vervet Monkey");
        addEntityType(TropicraftEntities.TAPIR);
        addEntityType(TropicraftEntities.JAGUAR);
        addEntityType(TropicraftEntities.BROWN_BASILISK_LIZARD);
        addEntityType(TropicraftEntities.GREEN_BASILISK_LIZARD);
        addEntityType(TropicraftEntities.HUMMINGBIRD);
        addEntityType(TropicraftEntities.FIDDLER_CRAB);
        addEntityType(TropicraftEntities.SPIDER_MONKEY);
        addEntityType(TropicraftEntities.WHITE_LIPPED_PECCARY);
        addEntityType(TropicraftEntities.CUBERA);

        // BIOMES
        
        addBiome(TropicraftBiomes.TROPICS_OCEAN);
        addBiome(TropicraftBiomes.TROPICS);
        addBiome(TropicraftBiomes.KELP_FOREST);
        addBiome(TropicraftBiomes.RAINFOREST_PLAINS);
        addBiome(TropicraftBiomes.RAINFOREST_HILLS);
        addBiome(TropicraftBiomes.RAINFOREST_MOUNTAINS);
        addBiome(TropicraftBiomes.BAMBOO_RAINFOREST);
        addBiome(TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS);
        addBiome(TropicraftBiomes.TROPICS_RIVER);
        addBiome(TropicraftBiomes.TROPICS_BEACH);
        addBiome(TropicraftBiomes.MANGROVES);
        addBiome(TropicraftBiomes.OVERGROWN_MANGROVES);
        addBiome(TropicraftBiomes.OSA_RAINFOREST);

        // MISC
        
        add(Tropicraft.TROPICRAFT_ITEM_GROUP, "Tropicraft");
        add("attribute.name." + ForgeMod.SWIM_SPEED.get().getRegistryName().getPath(), "Swim Speed");

        // Koa
        add("entity.tropicraft.koa.female.hunter.name", "Koa Hunter");
        add("entity.tropicraft.koa.female.fisherman.name", "Koa Fisher");
        add("entity.tropicraft.koa.male.hunter.name", "Koa Hunter");
        add("entity.tropicraft.koa.male.fisherman.name", "Koa Fisher");
        
        TropicraftLangKeys.generate(this);
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
    
    private void addTooltip(Supplier<? extends ItemLike> item, String tooltip) {
        add(item.get().asItem().getDescriptionId() + ".desc", tooltip);
    }
    
    private void addTooltip(Supplier<? extends ItemLike> item, List<String> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            String key = item.get().asItem().getDescriptionId() + ".desc." + i;
            add(key, tooltip.get(i));
        }
    }
    
    private void add(CreativeModeTab group, String name) {
        add("itemGroup." + group.getRecipeFolderName(), name);
    }
    
    private void addEntityType(Supplier<? extends EntityType<?>> entity) {
        addEntityType(entity, getAutomaticName(entity));
    }
    
    private void addBiome(ResourceKey<Biome> biome) {
        ResourceLocation id = biome.location();
        add("biome." + id.getNamespace() + "." + id.getPath(), Util.toEnglishName(id.getPath()));
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
    public void add(String key, String value) {
        super.add(key, value);
        upsideDown.add(key, toUpsideDown(value));
    }

    @Override
    public void run(HashCache cache) throws IOException {
        super.run(cache);
        upsideDown.run(cache);
    }
}
