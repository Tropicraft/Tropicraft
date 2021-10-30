package net.tropicraft.core.client.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.FurnitureItem;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.function.Supplier;

public class TropicraftItemModelProvider extends ItemModelProvider {

    public TropicraftItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Tropicraft Item Models";
    }
    
    @Override
    protected void registerModels() {
        
        // BLOCKS
        
        blockItem(TropicraftBlocks.CHUNK);
        
        // Ores and storage blocks
        blockItem(TropicraftBlocks.AZURITE_ORE);
        blockItem(TropicraftBlocks.EUDIALYTE_ORE);
        blockItem(TropicraftBlocks.MANGANESE_ORE);
        blockItem(TropicraftBlocks.SHAKA_ORE);
        blockItem(TropicraftBlocks.ZIRCON_ORE);
        
        blockItem(TropicraftBlocks.AZURITE_BLOCK);
        blockItem(TropicraftBlocks.EUDIALYTE_BLOCK);
        blockItem(TropicraftBlocks.MANGANESE_BLOCK);
        blockItem(TropicraftBlocks.SHAKA_BLOCK);
        blockItem(TropicraftBlocks.ZIRCON_BLOCK);
        blockItem(TropicraftBlocks.ZIRCONIUM_BLOCK);
        
        // All flowers
        TropicraftBlocks.FLOWERS.entrySet().forEach(e ->
            blockSprite(e.getValue(), modLoc("block/flower/" + e.getKey().getId())));
        
        // Sands
        blockItem(TropicraftBlocks.PURIFIED_SAND);
        blockItem(TropicraftBlocks.PACKED_PURIFIED_SAND);
        blockItem(TropicraftBlocks.CORAL_SAND);
        blockItem(TropicraftBlocks.FOAMY_SAND);
        blockItem(TropicraftBlocks.VOLCANIC_SAND);
        blockItem(TropicraftBlocks.MINERAL_SAND);

        // Mud
        blockItem(TropicraftBlocks.MUD);
        blockItem(TropicraftBlocks.MUD_WITH_PIANGUAS);

        // Bundles
        blockItem(TropicraftBlocks.BAMBOO_BUNDLE);
        blockItem(TropicraftBlocks.THATCH_BUNDLE);
        
        // Planks & Logs
        blockItem(TropicraftBlocks.MAHOGANY_PLANKS);
        blockItem(TropicraftBlocks.PALM_PLANKS);
        
        blockItem(TropicraftBlocks.MAHOGANY_LOG);
        blockItem(TropicraftBlocks.PALM_LOG);
        
        blockItem(TropicraftBlocks.MAHOGANY_WOOD);
        blockItem(TropicraftBlocks.PALM_WOOD);

        blockItem(TropicraftBlocks.RED_MANGROVE_LOG);
        blockItem(TropicraftBlocks.RED_MANGROVE_WOOD);

        blockItem(TropicraftBlocks.LIGHT_MANGROVE_LOG);
        blockItem(TropicraftBlocks.LIGHT_MANGROVE_WOOD);

        blockItem(TropicraftBlocks.BLACK_MANGROVE_LOG);
        blockItem(TropicraftBlocks.BLACK_MANGROVE_WOOD);

        blockItem(TropicraftBlocks.STRIPPED_MANGROVE_LOG);
        blockItem(TropicraftBlocks.STRIPPED_MANGROVE_WOOD);

        blockItem(TropicraftBlocks.MANGROVE_PLANKS);

        withExistingParent(name(TropicraftBlocks.RED_MANGROVE_ROOTS), modLoc("block/red_mangrove_roots_stem"));
        withExistingParent(name(TropicraftBlocks.LIGHT_MANGROVE_ROOTS), modLoc("block/light_mangrove_roots_stem"));
        withExistingParent(name(TropicraftBlocks.BLACK_MANGROVE_ROOTS), modLoc("block/black_mangrove_roots_stem"));

        // Stairs & Slabs
        blockItem(TropicraftBlocks.BAMBOO_STAIRS);
        blockItem(TropicraftBlocks.THATCH_STAIRS);
        blockItem(TropicraftBlocks.CHUNK_STAIRS);
        blockItem(TropicraftBlocks.PALM_STAIRS);
        blockItem(TropicraftBlocks.MAHOGANY_STAIRS);
        blockItem(TropicraftBlocks.THATCH_STAIRS_FUZZY);
        blockItem(TropicraftBlocks.MANGROVE_STAIRS);

        blockItem(TropicraftBlocks.BAMBOO_SLAB);
        blockItem(TropicraftBlocks.THATCH_SLAB);
        blockItem(TropicraftBlocks.CHUNK_SLAB);
        blockItem(TropicraftBlocks.PALM_SLAB);
        blockItem(TropicraftBlocks.MAHOGANY_SLAB);
        blockItem(TropicraftBlocks.MANGROVE_SLAB);

        // Leaves
        blockItem(TropicraftBlocks.MAHOGANY_LEAVES);
        blockItem(TropicraftBlocks.PALM_LEAVES);
        blockItem(TropicraftBlocks.KAPOK_LEAVES);
        blockItem(TropicraftBlocks.FRUIT_LEAVES);
        blockItem(TropicraftBlocks.GRAPEFRUIT_LEAVES);
        blockItem(TropicraftBlocks.LEMON_LEAVES);
        blockItem(TropicraftBlocks.LIME_LEAVES);
        blockItem(TropicraftBlocks.ORANGE_LEAVES);

        blockItem(TropicraftBlocks.RED_MANGROVE_LEAVES);
        blockItem(TropicraftBlocks.TALL_MANGROVE_LEAVES);
        blockItem(TropicraftBlocks.TEA_MANGROVE_LEAVES);
        blockItem(TropicraftBlocks.BLACK_MANGROVE_LEAVES);

        // Saplings
        blockSprite(TropicraftBlocks.MAHOGANY_SAPLING);
        blockSprite(TropicraftBlocks.PALM_SAPLING);
        blockSprite(TropicraftBlocks.GRAPEFRUIT_SAPLING);
        blockSprite(TropicraftBlocks.LEMON_SAPLING);
        blockSprite(TropicraftBlocks.LIME_SAPLING);
        blockSprite(TropicraftBlocks.ORANGE_SAPLING);

        blockSprite(TropicraftBlocks.RED_MANGROVE_PROPAGULE);
        blockSprite(TropicraftBlocks.TALL_MANGROVE_PROPAGULE);
        blockSprite(TropicraftBlocks.TEA_MANGROVE_PROPAGULE);
        blockSprite(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE);

        // Fences, Gates, and Walls
        blockWithInventoryModel(TropicraftBlocks.BAMBOO_FENCE);
        blockWithInventoryModel(TropicraftBlocks.THATCH_FENCE);
        blockWithInventoryModel(TropicraftBlocks.CHUNK_FENCE);
        blockWithInventoryModel(TropicraftBlocks.PALM_FENCE);
        blockWithInventoryModel(TropicraftBlocks.MAHOGANY_FENCE);
        blockWithInventoryModel(TropicraftBlocks.MANGROVE_FENCE);

        blockItem(TropicraftBlocks.BAMBOO_FENCE_GATE);
        blockItem(TropicraftBlocks.THATCH_FENCE_GATE);
        blockItem(TropicraftBlocks.CHUNK_FENCE_GATE);
        blockItem(TropicraftBlocks.PALM_FENCE_GATE);
        blockItem(TropicraftBlocks.MAHOGANY_FENCE_GATE);
        blockItem(TropicraftBlocks.MANGROVE_FENCE_GATE);

        blockWithInventoryModel(TropicraftBlocks.CHUNK_WALL);

        // Doors and Trapdoors
        generated(TropicraftBlocks.BAMBOO_DOOR);
        generated(TropicraftBlocks.THATCH_DOOR);
        generated(TropicraftBlocks.PALM_DOOR);
        generated(TropicraftBlocks.MAHOGANY_DOOR);
        generated(TropicraftBlocks.MANGROVE_DOOR);

        blockItem(TropicraftBlocks.BAMBOO_TRAPDOOR, "_bottom");
        blockItem(TropicraftBlocks.THATCH_TRAPDOOR, "_bottom");
        blockItem(TropicraftBlocks.PALM_TRAPDOOR, "_bottom");
        blockItem(TropicraftBlocks.MAHOGANY_TRAPDOOR, "_bottom");
        blockItem(TropicraftBlocks.MANGROVE_TRAPDOOR, "_bottom");

        // Misc remaining blocks
        blockSprite(TropicraftBlocks.IRIS, modLoc("block/iris_top"));
        blockSprite(TropicraftBlocks.PINEAPPLE, modLoc("block/pineapple_top"));
        
        blockItem(TropicraftBlocks.SMALL_BONGO_DRUM);
        blockItem(TropicraftBlocks.MEDIUM_BONGO_DRUM);
        blockItem(TropicraftBlocks.LARGE_BONGO_DRUM);
        
        blockSprite(TropicraftBlocks.BAMBOO_LADDER);

        blockItem(TropicraftBlocks.BAMBOO_BOARDWALK, "_short");
        blockItem(TropicraftBlocks.PALM_BOARDWALK, "_short");
        blockItem(TropicraftBlocks.MAHOGANY_BOARDWALK, "_short");
        blockItem(TropicraftBlocks.MANGROVE_BOARDWALK, "_short");

        withExistingParent(name(TropicraftBlocks.BAMBOO_CHEST), mcLoc("item/chest"))
            .texture("particle", modLoc("block/bamboo_side"));
        blockItem(TropicraftBlocks.SIFTER);
        withExistingParent(name(TropicraftBlocks.DRINK_MIXER), modLoc("item/tall_machine"))
            .texture("particle", modLoc("block/chunk"));
        withExistingParent(name(TropicraftBlocks.AIR_COMPRESSOR), modLoc("item/tall_machine"))
            .texture("particle", modLoc("block/chunk"));

        generated(TropicraftBlocks.GOLDEN_LEATHER_FERN, modLoc("item/golden_leather_fern"));

        generated(TropicraftBlocks.TIKI_TORCH);
        
        blockSprite(TropicraftBlocks.COCONUT);
        
        generated(TropicraftBlocks.BAMBOO_FLOWER_POT);
        
        generated(TropicraftItems.BAMBOO_ITEM_FRAME);
        generated(TropicraftItems.FISHING_NET);

        blockSprite(TropicraftBlocks.REEDS, modLoc("block/reeds_top_tall"));

        generated(TropicraftItems.PIANGUAS);

        // ITEMS
        
        // Gems/Ingots
        generated(TropicraftItems.AZURITE);
        generated(TropicraftItems.EUDIALYTE);
        generated(TropicraftItems.ZIRCON);
        generated(TropicraftItems.ZIRCONIUM);
        generated(TropicraftItems.SHAKA);
        generated(TropicraftItems.MANGANESE);
        
        // TODO dedupe
        // All Umbrellas
        ModelFile umbrella = getBuilder("umbrella").parent(new UncheckedModelFile("item/generated"))
            .texture("layer0", modLoc(folder + "/umbrella"))
            .texture("layer1", modLoc(folder + "/umbrella_inverted"));
        for (RegistryObject<FurnitureItem<UmbrellaEntity>> umbrellaItem : TropicraftItems.UMBRELLAS.values()) {
            getBuilder(name(umbrellaItem)).parent(umbrella);
        }
        // All chairs
        ModelFile chair = getBuilder("chair").parent(new UncheckedModelFile("item/generated"))
            .texture("layer0", modLoc(folder + "/chair"))
            .texture("layer1", modLoc(folder + "/chair_inverted"));
        for (RegistryObject<FurnitureItem<ChairEntity>> chairItem : TropicraftItems.CHAIRS.values()) {
            getBuilder(name(chairItem)).parent(chair);
        }
        // All beach floats
        ModelFile beachFloat = getBuilder("beach_float").parent(new UncheckedModelFile("item/generated"))
            .texture("layer0", modLoc(folder + "/beach_float"))
            .texture("layer1", modLoc(folder + "/beach_float_inverted"));
        for (RegistryObject<FurnitureItem<BeachFloatEntity>> beachFloatItem : TropicraftItems.BEACH_FLOATS.values()) {
            getBuilder(name(beachFloatItem)).parent(beachFloat);
        }
        
        // Bamboo Items
        generated(TropicraftItems.BAMBOO_STICK);
        ModelFile bambooSpearThrowing = withExistingParent("bamboo_spear_throwing", modLoc("spear_throwing"))
                .texture("layer0", itemTexture(TropicraftItems.BAMBOO_SPEAR));
        withExistingParent(name(TropicraftItems.BAMBOO_SPEAR), modLoc("spear"))
                .texture("layer0", itemTexture(TropicraftItems.BAMBOO_SPEAR))
                .override()
                    .predicate(mcLoc("throwing"), 1)
                    .model(bambooSpearThrowing);
        
        // Shells
        generated(TropicraftItems.SOLONOX_SHELL);
        generated(TropicraftItems.FROX_CONCH);
        generated(TropicraftItems.PAB_SHELL);
        generated(TropicraftItems.RUBE_NAUTILUS);
        generated(TropicraftItems.STARFISH);
        generated(TropicraftItems.TURTLE_SHELL);
        generated(TropicraftItems.LOVE_TROPICS_SHELL, modLoc(folder + "/ltshell"))
            .texture("layer1", modLoc(folder + "/ltshell_inverted"));
        
        // Fruits
        generated(TropicraftItems.LEMON);
        generated(TropicraftItems.LIME);
        generated(TropicraftItems.GRAPEFRUIT);
        generated(TropicraftItems.ORANGE);
        generated(TropicraftItems.PINEAPPLE_CUBES);
        generated(TropicraftItems.COCONUT_CHUNK);
        generated(TropicraftItems.RAW_COFFEE_BEAN);
        generated(TropicraftItems.ROASTED_COFFEE_BEAN);
        generated(TropicraftItems.COFFEE_BERRY);

        // Cocktails
        generated(TropicraftItems.BAMBOO_MUG);
        ModelFile cocktail = getBuilder("template_cocktail").parent(new UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc(folder + "/cocktail"))
                .texture("layer1", modLoc(folder + "/cocktail_contents"));
        for (RegistryObject<CocktailItem> cocktailItem : TropicraftItems.COCKTAILS.values()) {
            getBuilder(name(cocktailItem)).parent(cocktail);
        }
        
        // Trade items
        generated(TropicraftItems.WHITE_PEARL);
        generated(TropicraftItems.BLACK_PEARL);
        generated(TropicraftItems.SCALE);

        // Food
        generated(TropicraftItems.FRESH_MARLIN);
        generated(TropicraftItems.SEARED_MARLIN);
        generated(TropicraftItems.RAW_RAY);
        generated(TropicraftItems.COOKED_RAY);
        generated(TropicraftItems.FROG_LEG);
        generated(TropicraftItems.COOKED_FROG_LEG);
        generated(TropicraftItems.SEA_URCHIN_ROE);
        generated(TropicraftItems.TOASTED_NORI);
        generated(TropicraftItems.RAW_FISH, modLoc(folder + "/smolfish"));
        generated(TropicraftItems.COOKED_FISH, modLoc(folder + "/cooked_smolfish"));
        generated(TropicraftItems.POISON_FROG_SKIN);
        
        // Mob drops
        generated(TropicraftItems.IGUANA_LEATHER);
        
        // Tools
        generated(TropicraftItems.ZIRCON_AXE);
        generated(TropicraftItems.ZIRCON_HOE);
        generated(TropicraftItems.ZIRCON_PICKAXE);
        generated(TropicraftItems.ZIRCON_SWORD);
        generated(TropicraftItems.ZIRCON_SHOVEL);
        generated(TropicraftItems.ZIRCONIUM_AXE);
        generated(TropicraftItems.ZIRCONIUM_HOE);
        generated(TropicraftItems.ZIRCONIUM_PICKAXE);
        generated(TropicraftItems.ZIRCONIUM_SWORD);
        generated(TropicraftItems.ZIRCONIUM_SHOVEL);
        generated(TropicraftItems.EUDIALYTE_AXE);
        generated(TropicraftItems.EUDIALYTE_HOE);
        generated(TropicraftItems.EUDIALYTE_PICKAXE);
        generated(TropicraftItems.EUDIALYTE_SWORD);
        generated(TropicraftItems.EUDIALYTE_SHOVEL);
        
        // Misc
        generated(TropicraftItems.TROPICAL_FERTILIZER);
        generated(TropicraftItems.PIRANHA_BUCKET);
        generated(TropicraftItems.SARDINE_BUCKET);
        generated(TropicraftItems.TROPICAL_FISH_BUCKET);
        generated(TropicraftItems.DAGGER);
        TropicraftItems.ASHEN_MASKS.values().forEach(this::generated);
        generated(TropicraftItems.BLOW_GUN);
        generated(TropicraftItems.NIGEL_STACHE);
        generated(TropicraftItems.WATER_WAND);
        generated(TropicraftItems.EXPLODING_COCONUT);
        
        // Discs
        TropicraftItems.MUSIC_DISCS.values().forEach(this::generated);

        // Spawn Eggs
        generated(TropicraftItems.KOA_SPAWN_EGG);
        generated(TropicraftItems.TROPICREEPER_SPAWN_EGG);
        generated(TropicraftItems.IGUANA_SPAWN_EGG);
        generated(TropicraftItems.TROPISKELLY_SPAWN_EGG);
        generated(TropicraftItems.EIH_SPAWN_EGG);
        generated(TropicraftItems.SEA_TURTLE_SPAWN_EGG);
        generated(TropicraftItems.MARLIN_SPAWN_EGG);
        generated(TropicraftItems.FAILGULL_SPAWN_EGG);
        generated(TropicraftItems.DOLPHIN_SPAWN_EGG);
        generated(TropicraftItems.SEAHORSE_SPAWN_EGG);
        generated(TropicraftItems.TREE_FROG_SPAWN_EGG);
        generated(TropicraftItems.SEA_URCHIN_SPAWN_EGG);
        generated(TropicraftItems.V_MONKEY_SPAWN_EGG);
        generated(TropicraftItems.PIRANHA_SPAWN_EGG);
        generated(TropicraftItems.SARDINE_SPAWN_EGG);
        generated(TropicraftItems.TROPICAL_FISH_SPAWN_EGG);
        generated(TropicraftItems.EAGLE_RAY_SPAWN_EGG);
        generated(TropicraftItems.TROPI_SPIDER_SPAWN_EGG);
        generated(TropicraftItems.ASHEN_SPAWN_EGG);
        generated(TropicraftItems.HAMMERHEAD_SPAWN_EGG);
        generated(TropicraftItems.COWKTAIL_SPAWN_EGG);
        generated(TropicraftItems.MAN_O_WAR_SPAWN_EGG);
        generated(TropicraftItems.TROPIBEE_SPAWN_EGG);
        generated(TropicraftItems.TAPIR_SPAWN_EGG);
        generated(TropicraftItems.JAGUAR_SPAWN_EGG);
        generated(TropicraftItems.BROWN_BASILISK_LIZARD_SPAWN_EGG);
        generated(TropicraftItems.GREEN_BASILISK_LIZARD_SPAWN_EGG);
        generated(TropicraftItems.HUMMINGBIRD_SPAWN_EGG);
        generated(TropicraftItems.FIDDLER_CRAB_SPAWN_EGG);
        generated(TropicraftItems.SPIDER_MONKEY_SPAWN_EGG);
        generated(TropicraftItems.WHITE_LIPPED_PECCARY_SPAWN_EGG);
        generated(TropicraftItems.CUBERA_SPAWN_EGG);

        // Armor
        generated(TropicraftItems.FIRE_BOOTS);
        generated(TropicraftItems.FIRE_CHESTPLATE);
        generated(TropicraftItems.FIRE_HELMET);
        generated(TropicraftItems.FIRE_LEGGINGS);
        generated(TropicraftItems.SCALE_BOOTS);
        generated(TropicraftItems.SCALE_CHESTPLATE);
        generated(TropicraftItems.SCALE_HELMET);
        generated(TropicraftItems.SCALE_LEGGINGS);
        
        // Scuba
        generated(TropicraftItems.YELLOW_SCUBA_GOGGLES);
        generated(TropicraftItems.YELLOW_SCUBA_HARNESS);
        generated(TropicraftItems.YELLOW_SCUBA_FLIPPERS);
        generated(TropicraftItems.PINK_SCUBA_GOGGLES);
        generated(TropicraftItems.PINK_SCUBA_HARNESS);
        generated(TropicraftItems.PINK_SCUBA_FLIPPERS);
        
        generated(TropicraftItems.YELLOW_PONY_BOTTLE);
        generated(TropicraftItems.PINK_PONY_BOTTLE);
    }
    
    private String name(Supplier<? extends ItemLike> item) {
        return item.get().asItem().getRegistryName().getPath();
    }
    
    private ResourceLocation itemTexture(Supplier<? extends ItemLike> item) {
        return modLoc("item/" + name(item));
    }
    
    private ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return blockItem(block, "");
    }
    
    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + suffix));
    }

    private ItemModelBuilder blockWithInventoryModel(Supplier<? extends Block> block) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + "_inventory"));
    }
    
    private ItemModelBuilder blockSprite(Supplier<? extends Block> block) {
        return blockSprite(block, modLoc("block/" + name(block)));
    }
    
    private ItemModelBuilder blockSprite(Supplier<? extends Block> block, ResourceLocation texture) {
        return generated(() -> block.get().asItem(), texture);
    }
    
    private ItemModelBuilder generated(Supplier<? extends ItemLike> item) {
        return generated(item, itemTexture(item));
    }

    private ItemModelBuilder generated(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile("item/generated")).texture("layer0", texture);
    }
    
    private ItemModelBuilder handheld(Supplier<? extends ItemLike> item) {
        return handheld(item, itemTexture(item));
    }
    
    private ItemModelBuilder handheld(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return withExistingParent(name(item), "item/handheld").texture("layer0", texture);
    }
}
