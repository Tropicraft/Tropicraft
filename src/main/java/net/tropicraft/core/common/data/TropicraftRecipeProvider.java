package net.tropicraft.core.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.drinks.Drink;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.tropicraft.core.common.block.TropicraftBlocks.AIR_COMPRESSOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.AZURITE_BLOCK;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_BOARDWALK;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_BUNDLE;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_CHEST;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_FENCE_GATE;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_FLOWER_POT;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_LADDER;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.BLACK_MANGROVE_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.BLACK_MANGROVE_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_FENCE_GATE;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_WALL;
import static net.tropicraft.core.common.block.TropicraftBlocks.DRINK_MIXER;
import static net.tropicraft.core.common.block.TropicraftBlocks.EUDIALYTE_BLOCK;
import static net.tropicraft.core.common.block.TropicraftBlocks.IRIS;
import static net.tropicraft.core.common.block.TropicraftBlocks.LARGE_BONGO_DRUM;
import static net.tropicraft.core.common.block.TropicraftBlocks.LIGHT_MANGROVE_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.LIGHT_MANGROVE_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_BOARDWALK;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_FENCE_GATE;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_PLANKS;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGANESE_BLOCK;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_BOARDWALK;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_FENCE_GATE;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_PLANKS;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.MANGROVE_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.MEDIUM_BONGO_DRUM;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_BOARDWALK;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_FENCE_GATE;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_PLANKS;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.PAPAYA_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.PAPAYA_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.RED_MANGROVE_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.RED_MANGROVE_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.SHAKA_BLOCK;
import static net.tropicraft.core.common.block.TropicraftBlocks.SIFTER;
import static net.tropicraft.core.common.block.TropicraftBlocks.SMALL_BONGO_DRUM;
import static net.tropicraft.core.common.block.TropicraftBlocks.STRIPPED_MANGROVE_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.STRIPPED_MANGROVE_WOOD;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_BUNDLE;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_FENCE_GATE;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_STAIRS_FUZZY;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.TIKI_TORCH;
import static net.tropicraft.core.common.block.TropicraftBlocks.ZIRCONIUM_BLOCK;
import static net.tropicraft.core.common.block.TropicraftBlocks.ZIRCON_BLOCK;
import static net.tropicraft.core.common.block.TropicraftFlower.CANNA;
import static net.tropicraft.core.common.block.TropicraftFlower.COMMELINA_DIFFUSA;
import static net.tropicraft.core.common.block.TropicraftFlower.DRACAENA;
import static net.tropicraft.core.common.block.TropicraftFlower.ORANGE_ANTHURIUM;
import static net.tropicraft.core.common.block.TropicraftFlower.RED_ANTHURIUM;
import static net.tropicraft.core.common.item.TropicraftItems.AZURITE;
import static net.tropicraft.core.common.item.TropicraftItems.BAMBOO_ITEM_FRAME;
import static net.tropicraft.core.common.item.TropicraftItems.BAMBOO_MUG;
import static net.tropicraft.core.common.item.TropicraftItems.BAMBOO_SPEAR;
import static net.tropicraft.core.common.item.TropicraftItems.BAMBOO_STICK;
import static net.tropicraft.core.common.item.TropicraftItems.BEACH_FLOATS;
import static net.tropicraft.core.common.item.TropicraftItems.BLOW_GUN;
import static net.tropicraft.core.common.item.TropicraftItems.CHAIRS;
import static net.tropicraft.core.common.item.TropicraftItems.COCKTAILS;
import static net.tropicraft.core.common.item.TropicraftItems.COCONUT_CHUNK;
import static net.tropicraft.core.common.item.TropicraftItems.COFFEE_BERRY;
import static net.tropicraft.core.common.item.TropicraftItems.COOKED_FISH;
import static net.tropicraft.core.common.item.TropicraftItems.COOKED_FROG_LEG;
import static net.tropicraft.core.common.item.TropicraftItems.COOKED_RAY;
import static net.tropicraft.core.common.item.TropicraftItems.DAGGER;
import static net.tropicraft.core.common.item.TropicraftItems.EUDIALYTE;
import static net.tropicraft.core.common.item.TropicraftItems.EUDIALYTE_AXE;
import static net.tropicraft.core.common.item.TropicraftItems.EUDIALYTE_HOE;
import static net.tropicraft.core.common.item.TropicraftItems.EUDIALYTE_PICKAXE;
import static net.tropicraft.core.common.item.TropicraftItems.EUDIALYTE_SHOVEL;
import static net.tropicraft.core.common.item.TropicraftItems.EUDIALYTE_SWORD;
import static net.tropicraft.core.common.item.TropicraftItems.FRESH_MARLIN;
import static net.tropicraft.core.common.item.TropicraftItems.FROG_LEG;
import static net.tropicraft.core.common.item.TropicraftItems.IGUANA_LEATHER;
import static net.tropicraft.core.common.item.TropicraftItems.MANGANESE;
import static net.tropicraft.core.common.item.TropicraftItems.PINEAPPLE_CUBES;
import static net.tropicraft.core.common.item.TropicraftItems.PINK_PONY_BOTTLE;
import static net.tropicraft.core.common.item.TropicraftItems.PINK_SCUBA_FLIPPERS;
import static net.tropicraft.core.common.item.TropicraftItems.PINK_SCUBA_GOGGLES;
import static net.tropicraft.core.common.item.TropicraftItems.PINK_SCUBA_HARNESS;
import static net.tropicraft.core.common.item.TropicraftItems.RAW_COFFEE_BEAN;
import static net.tropicraft.core.common.item.TropicraftItems.RAW_FISH;
import static net.tropicraft.core.common.item.TropicraftItems.RAW_RAY;
import static net.tropicraft.core.common.item.TropicraftItems.ROASTED_COFFEE_BEAN;
import static net.tropicraft.core.common.item.TropicraftItems.SCALE;
import static net.tropicraft.core.common.item.TropicraftItems.SCALE_BOOTS;
import static net.tropicraft.core.common.item.TropicraftItems.SCALE_CHESTPLATE;
import static net.tropicraft.core.common.item.TropicraftItems.SCALE_HELMET;
import static net.tropicraft.core.common.item.TropicraftItems.SCALE_LEGGINGS;
import static net.tropicraft.core.common.item.TropicraftItems.SEARED_MARLIN;
import static net.tropicraft.core.common.item.TropicraftItems.SHAKA;
import static net.tropicraft.core.common.item.TropicraftItems.TOASTED_NORI;
import static net.tropicraft.core.common.item.TropicraftItems.TROPICAL_FERTILIZER;
import static net.tropicraft.core.common.item.TropicraftItems.UMBRELLAS;
import static net.tropicraft.core.common.item.TropicraftItems.WATER_WAND;
import static net.tropicraft.core.common.item.TropicraftItems.YELLOW_PONY_BOTTLE;
import static net.tropicraft.core.common.item.TropicraftItems.YELLOW_SCUBA_FLIPPERS;
import static net.tropicraft.core.common.item.TropicraftItems.YELLOW_SCUBA_GOGGLES;
import static net.tropicraft.core.common.item.TropicraftItems.YELLOW_SCUBA_HARNESS;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCON;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCONIUM;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCONIUM_AXE;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCONIUM_HOE;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCONIUM_PICKAXE;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCONIUM_SHOVEL;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCONIUM_SWORD;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCON_AXE;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCON_HOE;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCON_PICKAXE;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCON_SHOVEL;
import static net.tropicraft.core.common.item.TropicraftItems.ZIRCON_SWORD;

public class TropicraftRecipeProvider extends RecipeProvider {

    public TropicraftRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ore(TropicraftTags.Items.AZURITE_ORE, AZURITE, 0.3F, consumer);
        ore(TropicraftTags.Items.EUDIALYTE_ORE, EUDIALYTE, 0.5F, consumer);
        ore(TropicraftTags.Items.ZIRCON_ORE, ZIRCON, 0.5F, consumer);
        ore(TropicraftTags.Items.MANGANESE_ORE, MANGANESE, 0.5F, consumer);
        ore(TropicraftTags.Items.SHAKA_ORE, SHAKA, 0.7F, consumer);
        
        storage(AZURITE, AZURITE_BLOCK, consumer);
        storage(EUDIALYTE, EUDIALYTE_BLOCK, consumer);
        storage(ZIRCON, ZIRCON_BLOCK, consumer);
        storage(ZIRCONIUM, ZIRCONIUM_BLOCK, consumer);
        storage(MANGANESE, MANGANESE_BLOCK, consumer);
        storage(SHAKA, SHAKA_BLOCK, consumer);

        pickaxe(ZIRCON, ZIRCON_PICKAXE, consumer);
        pickaxe(ZIRCONIUM, ZIRCONIUM_PICKAXE, consumer);
        pickaxe(EUDIALYTE, EUDIALYTE_PICKAXE, consumer);

        axe(ZIRCON, ZIRCON_AXE, consumer);
        axe(ZIRCONIUM, ZIRCONIUM_AXE, consumer);
        axe(EUDIALYTE, EUDIALYTE_AXE, consumer);

        shovel(ZIRCON, ZIRCON_SHOVEL, consumer);
        shovel(ZIRCONIUM, ZIRCONIUM_SHOVEL, consumer);
        shovel(EUDIALYTE, EUDIALYTE_SHOVEL, consumer);

        hoe(ZIRCON, ZIRCON_HOE, consumer);
        hoe(ZIRCONIUM, ZIRCONIUM_HOE, consumer);
        hoe(EUDIALYTE, EUDIALYTE_HOE, consumer);

        sword(ZIRCON, ZIRCON_SWORD, consumer);
        sword(ZIRCONIUM, ZIRCONIUM_SWORD, consumer);
        sword(EUDIALYTE, EUDIALYTE_SWORD, consumer);

        helmet(SCALE, SCALE_HELMET, consumer);
        chestplate(SCALE, SCALE_CHESTPLATE, consumer);
        leggings(SCALE, SCALE_LEGGINGS, consumer);
        boots(SCALE, SCALE_BOOTS, consumer);

        for (DyeColor color : DyeColor.values()) {
            ItemLike wool = Sheep.ITEM_BY_DYE.get(color);
            ShapedRecipeBuilder.shaped(UMBRELLAS.get(color).get())
                .pattern("WWW").pattern(" B ").pattern(" B ")
                .group(Constants.MODID + ":umbrellas")
                .define('W', wool)
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + color.getSerializedName() + "_wool", has(wool))
                .save(consumer);
            
            ShapedRecipeBuilder.shaped(CHAIRS.get(color).get())
                .pattern("BWB").pattern("BWB").pattern("BWB")
                .group(Constants.MODID + ":chairs")
                .define('W', wool)
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + color.getSerializedName() + "_wool", has(wool))
                .save(consumer);
            
            ShapedRecipeBuilder.shaped(BEACH_FLOATS.get(color).get())
                .pattern("WWW").pattern("BBB")
                .group(Constants.MODID + ":beach_floats")
                .define('W', wool)
                .define('B', Blocks.BAMBOO)
                .unlockedBy("has_" + color.getSerializedName() + "_wool", has(wool))
                .save(consumer);
        
            // TODO other colored items
        }
        
        // Override the vanilla recipe to output ours, it's tagged so it will behave the same
        ShapedRecipeBuilder.shaped(BAMBOO_STICK.get())
            .pattern("X").pattern("X")
            .define('X', Items.BAMBOO)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .save(consumer, new ResourceLocation("stick_from_bamboo_item"));
        
        ShapedRecipeBuilder.shaped(BAMBOO_SPEAR.get())
            .pattern("X ").pattern(" X")
            .define('X', BAMBOO_STICK.get())
            .unlockedBy("has_bamboo_stick", has(BAMBOO_STICK.get()))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RAW_COFFEE_BEAN.get())
            .requires(COFFEE_BERRY.get())
            .unlockedBy("has_coffee_bean", has(COFFEE_BERRY.get()))
            .save(consumer);

        ShapelessRecipeBuilder.shapeless(ZIRCONIUM.get())
            .requires(AZURITE.get(), 2)
            .requires(ZIRCON.get(), 2)
            .unlockedBy("has_zircon", has(ZIRCON.get()))
            .unlockedBy("has_azurite", has(AZURITE.get()))
            .save(consumer);
        
        food(RAW_COFFEE_BEAN, ROASTED_COFFEE_BEAN, 0.1F, consumer);
        
        ShapedRecipeBuilder.shaped(BAMBOO_MUG.get())
            .pattern("X X").pattern("X X").pattern("XXX")
            .define('X', Items.BAMBOO)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .save(consumer);

        food(Items.SEAGRASS.delegate, TOASTED_NORI, 0.1F, consumer);
        food(FRESH_MARLIN, SEARED_MARLIN, 0.15F, consumer);
        food(RAW_RAY, COOKED_RAY, 0.15F, consumer);
        food(FROG_LEG, COOKED_FROG_LEG, 0.1F, consumer);
        food(RAW_FISH, COOKED_FISH, 0.1F, consumer);
        
        // Flowers to dye
        dye(COMMELINA_DIFFUSA, Items.LIGHT_BLUE_DYE.delegate, 1, 2, consumer);
        dye(CANNA, Items.YELLOW_DYE.delegate, 1, 2, consumer);
        dye(ORANGE_ANTHURIUM, Items.ORANGE_DYE.delegate, 1, 2, consumer);
        dye(RED_ANTHURIUM, Items.RED_DYE.delegate, 1, 2, consumer);
        dye(DRACAENA, Items.GREEN_DYE.delegate, 1, 2, consumer);
        dye(IRIS, Items.PURPLE_DYE.delegate, 1, 4, consumer);

        // Bundles
        singleItem(Blocks.BAMBOO.delegate, BAMBOO_BUNDLE, 9, 1, consumer);
        singleItem(Blocks.SUGAR_CANE.delegate, THATCH_BUNDLE, 9, 1, consumer);
        
        // Planks
        planks(MAHOGANY_LOG, MAHOGANY_PLANKS, consumer);
        planks(PALM_LOG, PALM_PLANKS, consumer);
        
        bark(MAHOGANY_LOG, MAHOGANY_WOOD, consumer);
        bark(PALM_LOG, PALM_WOOD, consumer);

        // Papaya log -> Jungle log
        ShapelessRecipeBuilder.shapeless(Blocks.JUNGLE_LOG)
                .requires(PAPAYA_LOG.get())
                .unlockedBy("has_papaya_log", has(PAPAYA_LOG.get()))
                .save(consumer);

        bark(PAPAYA_LOG, PAPAYA_WOOD, consumer);

        bark(RED_MANGROVE_LOG, RED_MANGROVE_WOOD, consumer);
        bark(LIGHT_MANGROVE_LOG, LIGHT_MANGROVE_WOOD, consumer);
        bark(BLACK_MANGROVE_LOG, BLACK_MANGROVE_WOOD, consumer);
        bark(STRIPPED_MANGROVE_LOG, STRIPPED_MANGROVE_WOOD, consumer);

        // Stairs
        stairs(PALM_PLANKS, PALM_STAIRS, "wooden_stairs", false, consumer);
        stairs(MAHOGANY_PLANKS, MAHOGANY_STAIRS, "wooden_stairs", false, consumer);
        stairs(MANGROVE_PLANKS, MANGROVE_STAIRS, "wooden_stairs", false, consumer);

        stairs(THATCH_BUNDLE, THATCH_STAIRS, null, false, consumer);
        stairs(BAMBOO_BUNDLE, BAMBOO_STAIRS, null, false, consumer);
        stairs(CHUNK, CHUNK_STAIRS, null, true, consumer);
        
        ShapedRecipeBuilder.shaped(THATCH_STAIRS_FUZZY.get(), 4)
            .pattern("C  ").pattern("XC ").pattern("XXC")
            .define('X', THATCH_BUNDLE.get())
            .define('C', Items.SUGAR_CANE)
            .unlockedBy("has_thatch_bundle", has(THATCH_BUNDLE.get()))
            .save(consumer);
        
        // Slabs
        slab(PALM_PLANKS, PALM_SLAB, "wooden_slab", false, consumer);
        slab(MAHOGANY_PLANKS, MAHOGANY_SLAB, "wooden_slab", false, consumer);
        slab(MANGROVE_PLANKS, MANGROVE_SLAB, "wooden_slab", false, consumer);

        slab(THATCH_BUNDLE, THATCH_SLAB, null, false, consumer);
        slab(BAMBOO_BUNDLE, BAMBOO_SLAB, null, false, consumer);
        slab(CHUNK, CHUNK_SLAB, null, true, consumer);
        
        // Fences
        fence(PALM_PLANKS, PALM_FENCE, "wooden_fence", consumer);
        fence(MAHOGANY_PLANKS, MAHOGANY_FENCE, "wooden_fence", consumer);
        fence(MANGROVE_PLANKS, MANGROVE_FENCE, "wooden_fence", consumer);

        fence(THATCH_BUNDLE, THATCH_FENCE, null, consumer);
        fence(BAMBOO_BUNDLE, BAMBOO_FENCE, null, consumer);
        fence(CHUNK, CHUNK_FENCE, null, consumer);
        
        // Fence Gates
        fenceGate(PALM_PLANKS, PALM_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(MAHOGANY_PLANKS, MAHOGANY_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(MANGROVE_PLANKS, MANGROVE_FENCE_GATE, "wooden_fence_gate", consumer);

        fenceGate(THATCH_BUNDLE, THATCH_FENCE_GATE, null, consumer);
        fenceGate(BAMBOO_BUNDLE, BAMBOO_FENCE_GATE, null, consumer);
        fenceGate(CHUNK, CHUNK_FENCE_GATE, null, consumer);
        
        // Walls
        wall(CHUNK, CHUNK_WALL, consumer);
        
        // Doors
        door(PALM_PLANKS, PALM_DOOR, "wooden_door", consumer);
        door(MAHOGANY_PLANKS, MAHOGANY_DOOR, "wooden_door", consumer);
        door(MANGROVE_PLANKS, MANGROVE_DOOR, "wooden_door", consumer);

        door(THATCH_BUNDLE, THATCH_DOOR, null, consumer);
        door(BAMBOO_BUNDLE, BAMBOO_DOOR, null, consumer);

        // Trap doors
        trapDoor(PALM_PLANKS, PALM_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(MAHOGANY_PLANKS, MAHOGANY_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(MANGROVE_PLANKS, MANGROVE_TRAPDOOR, "wooden_trapdoor", consumer);

        trapDoor(THATCH_BUNDLE, THATCH_TRAPDOOR, null, consumer);
        trapDoor(BAMBOO_BUNDLE, BAMBOO_TRAPDOOR, null, consumer);
        
        // Bongos
        bongo(IGUANA_LEATHER, MAHOGANY_PLANKS, 1, SMALL_BONGO_DRUM, consumer);
        bongo(IGUANA_LEATHER, MAHOGANY_PLANKS, 2, MEDIUM_BONGO_DRUM, consumer);
        bongo(IGUANA_LEATHER, MAHOGANY_PLANKS, 3, LARGE_BONGO_DRUM, consumer);

        // Scuba gear
        goggles(PINK_SCUBA_GOGGLES, Items.PINK_DYE.delegate, consumer);
        goggles(YELLOW_SCUBA_GOGGLES, Items.YELLOW_DYE.delegate, consumer);

        flippers(PINK_SCUBA_FLIPPERS, Items.PINK_DYE.delegate, consumer);
        flippers(YELLOW_SCUBA_FLIPPERS, Items.YELLOW_DYE.delegate, consumer);

        harness(PINK_SCUBA_HARNESS, Items.PINK_DYE.delegate, consumer);
        harness(YELLOW_SCUBA_HARNESS, Items.YELLOW_DYE.delegate, consumer);

        ponyBottle(PINK_PONY_BOTTLE, Items.PINK_DYE.delegate, consumer);
        ponyBottle(YELLOW_PONY_BOTTLE, Items.YELLOW_DYE.delegate, consumer);

        ShapedRecipeBuilder.shaped(WATER_WAND.get(), 1)
            .pattern("  X")
            .pattern(" Y ")
            .pattern("Y  ")
            .define('X', AZURITE.get())
            .define('Y', Items.GOLD_INGOT)
            .unlockedBy("has_" + safeName(AZURITE.get()), has(AZURITE.get()))
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(consumer);

        ShapedRecipeBuilder.shaped(BAMBOO_ITEM_FRAME.get(), 1)
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', Items.BAMBOO)
            .define('Y', Items.LEATHER)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(consumer);

        ShapedRecipeBuilder.shaped(AIR_COMPRESSOR.get(), 1)
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', CHUNK.get())
            .define('Y', AZURITE.get())
            .unlockedBy("has_" + safeName(CHUNK.get()), has(CHUNK.get()))
            .unlockedBy("has_" + safeName(AZURITE.get()), has(AZURITE.get()))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(BAMBOO_LADDER.get(), 4)
            .pattern("S S").pattern("BSB").pattern("S S")
            .define('S', BAMBOO_STICK.get())
            .define('B', Items.BAMBOO)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .save(consumer);

        boardwalk(BAMBOO_SLAB, BAMBOO_BOARDWALK, consumer);
        boardwalk(PALM_SLAB, PALM_BOARDWALK, consumer);
        boardwalk(MAHOGANY_SLAB, MAHOGANY_BOARDWALK, consumer);
        boardwalk(MANGROVE_SLAB, MANGROVE_BOARDWALK, consumer);

        ShapedRecipeBuilder.shaped(BAMBOO_CHEST.get())
            .pattern("BBB").pattern("B B").pattern("BBB")
            .define('B', Items.BAMBOO)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(SIFTER.get())
            .pattern("XXX").pattern("XIX").pattern("XXX")
            .define('X', ItemTags.PLANKS)
            .define('I', Tags.Items.GLASS)
            .group("tropicraft:sifter")
            .unlockedBy("has_glass", has(Tags.Items.GLASS))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(SIFTER.get())
            .pattern("XXX").pattern("XIX").pattern("XXX")
            .define('X', ItemTags.PLANKS)
            .define('I', Tags.Items.GLASS_PANES)
            .group("tropicraft:sifter")
            .unlockedBy("has_glass_pane", has(Tags.Items.GLASS_PANES))
            .save(consumer, new ResourceLocation(Constants.MODID, "sifter_with_glass_pane"));
        
        ShapedRecipeBuilder.shaped(DRINK_MIXER.get())
            .pattern("XXX").pattern("XYX").pattern("XXX")
            .define('X', CHUNK.get())
            .define('Y', BAMBOO_MUG.get())
            .unlockedBy("has_bamboo_mug", has(BAMBOO_MUG.get()))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(TIKI_TORCH.get())
            .pattern("Y").pattern("X").pattern("X")
            .define('X', BAMBOO_STICK.get())
            .define('Y', ItemTags.COALS)
            .unlockedBy("has_bamboo_stick", has(BAMBOO_STICK.get()))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(BAMBOO_FLOWER_POT.get())
            .pattern("# #").pattern(" # ")
            .define('#', Items.BAMBOO)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(COCKTAILS.get(Drink.PINA_COLADA).get())
            .requires(BAMBOO_MUG.get())
            .requires(COCONUT_CHUNK.get())
            .requires(PINEAPPLE_CUBES.get())
            .unlockedBy("has_bamboo_mug", has(BAMBOO_MUG.get()))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(TROPICAL_FERTILIZER.get())
            .requires(TropicraftFlower.MAGIC_MUSHROOM.get())
            .requires(TropicraftFlower.CROTON.get())
            .unlockedBy("has_magic_mushroom", has(TropicraftFlower.MAGIC_MUSHROOM.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(DAGGER.get())
            .pattern("X")
            .pattern("I")
            .define('X', CHUNK.get())
            .define('I', BAMBOO_STICK.get())
            .unlockedBy("has_" + safeName(CHUNK.get()), has(CHUNK.get()))
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .save(consumer);

        ShapedRecipeBuilder.shaped(BLOW_GUN.get())
            .pattern("X  ")
            .pattern(" I ")
            .pattern("  X")
            .define('X', BAMBOO_STICK.get())
            .define('I', ZIRCON.get())
            .unlockedBy("has_" + safeName(ZIRCON.get()), has(ZIRCON.get()))
            .unlockedBy("has_" + safeName(BAMBOO_STICK.get()), has(BAMBOO_STICK.get()))
            .save(consumer);
    }
    
    private ResourceLocation safeId(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), safeName(id));
    }
    
    private ResourceLocation safeId(IForgeRegistryEntry<?> registryEntry) {
        return safeId(registryEntry.getRegistryName());
    }
    
    private String safeName(ResourceLocation nameSource) {
        return nameSource.getPath().replace('/', '_');
    }
    
    private String safeName(IForgeRegistryEntry<?> registryEntry) {
        return safeName(registryEntry.getRegistryName());
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void ore(Named<Item> source, Supplier<T> result, float xp, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 100)
            .unlockedBy("has_" + safeName(source.getName()), has(source))
            .save(consumer);
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), result.get(), xp, 100)
            .unlockedBy("has_" + safeName(source.getName()), has(source))
            .save(consumer, safeId(result.get()) + "_from_blasting");
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void food(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 100)
            .unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get()))
            .save(consumer);
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 100, RecipeSerializer.SMOKING_RECIPE)
            .unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get()))
            .save(consumer, safeId(result.get()) + "_from_smoking");
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 100, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
            .unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get()))
            .save(consumer, safeId(result.get()) + "_from_campfire");
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void storage(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        // TODO probably not ported correctly
        ShapedRecipeBuilder.shaped(output.get())
            .pattern("XXX").pattern("XXX").pattern("XXX")
            .define('X', input.get())
            .unlockedBy("has_at_least_9_" + safeName(input.get()), has(input.get()))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(input.get(), 9)
            .requires(output.get())
            .unlockedBy("has_" + safeName(output.get()), has(output.get()))
            .save(consumer, safeId(input.get()) + "_from_" + safeName(output.get()));
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void pickaxe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XXX")
                .pattern(" B ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .unlockedBy("has_" + safeName(Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void shovel(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern(" X ")
                .pattern(" B ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .unlockedBy("has_" + safeName(Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void axe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XX ")
                .pattern("XB ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .unlockedBy("has_" + safeName(Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void hoe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XX ")
                .pattern(" B ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .unlockedBy("has_" + safeName(Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void sword(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern(" X ")
                .pattern(" X ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .unlockedBy("has_" + safeName(Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void helmet(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XXX")
                .pattern("X X")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void chestplate(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void leggings(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void boots(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("X X")
                .pattern("X X")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input.get()), has(input.get()))
                .save(consumer);
    }

    @CheckReturnValue
    private <T extends ItemLike & IForgeRegistryEntry<?>> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
        return ShapelessRecipeBuilder.shapeless(result.get(), amount)
            .requires(source.get(), required)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()));
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void dye(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<FinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).save(consumer, new ResourceLocation(Constants.MODID, result.get().getRegistryName().getPath()));
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<FinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        singleItemUnfinished(source, result, 1, 4)
            .group("planks")
            .save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void bark(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
            .pattern("##").pattern("##")
            .define('#', source.get())
            .group("bark")
            .unlockedBy("has_log", has(Blocks.ACACIA_LOG))
            .save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void stairs(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 4)
            .pattern("X  ").pattern("XX ").pattern("XXX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
        if (stone) {
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
                .unlockedBy("has_" + safeName(source.get()), has(source.get()))
                .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
        }
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void slab(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 6)
            .pattern("XXX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
        if (stone) {
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
                .unlockedBy("has_" + safeName(source.get()), has(source.get()))
                .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
        }
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void boardwalk(Supplier<? extends T> slab, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
                .pattern("XXX")
                .pattern("S S")
                .define('X', slab.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .group("tropicraft:boardwalk")
                .unlockedBy("has_" + safeName(slab.get()), has(slab.get()))
                .save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void fence(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
            .pattern("W#W").pattern("W#W")
            .define('W', source.get())
            .define('#', Tags.Items.RODS_WOODEN)
            .group(group)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void fenceGate(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
            .pattern("#W#").pattern("#W#")
            .define('W', source.get())
            .define('#', Tags.Items.RODS_WOODEN)
            .group(group)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void wall(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 6)
            .pattern("XXX").pattern("XXX")
            .define('X', source.get())
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void door(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
            .pattern("XX").pattern("XX").pattern("XX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void trapDoor(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 2)
            .pattern("XXX").pattern("XXX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .save(consumer);
    }
    
    private <T extends ItemLike & IForgeRegistryEntry<?>> void bongo(Supplier<? extends T> top, Supplier<? extends T> bottom, int size, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
            .pattern(StringUtils.repeat('T', size))
            .pattern(StringUtils.repeat('B', size))
            .pattern(StringUtils.repeat('B', size))
            .define('T', top.get())
            .define('B', bottom.get())
            .group("tropicraft:bongos")
            .unlockedBy("has_" + safeName(top.get()), has(top.get()))
            .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void goggles(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("YYY")
            .pattern("X X")
            .pattern(" Z ")
            .define('X', Blocks.GLASS_PANE)
            .define('Y', ZIRCON.get())
            .define('Z', source.get())
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .unlockedBy("has_" + safeName(ZIRCON.get()), has(ZIRCON.get()))
            .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void flippers(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("XX")
            .pattern("YY")
            .pattern("XX")
            .define('X', source.get())
            .define('Y', ZIRCON.get())
            .unlockedBy("has_" + safeName(source.get()), has(source.get()))
            .unlockedBy("has_" + safeName(ZIRCON.get()), has(ZIRCON.get()))
            .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void ponyBottle(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("Y")
            .pattern("X")
            .define('X', Blocks.PINK_STAINED_GLASS_PANE)
            .define('Y', Blocks.LEVER)
            .unlockedBy("has_" + safeName(Blocks.PINK_STAINED_GLASS_PANE), has(Blocks.PINK_STAINED_GLASS_PANE))
            .save(consumer);
    }

    private <T extends ItemLike & IForgeRegistryEntry<?>> void harness(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("Y Y")
            .pattern("YXY")
            .pattern("YZY")
            .define('X', source.get())
            .define('Y', TropicraftTags.Items.LEATHER)
            .define('Z', AZURITE.get())
            .unlockedBy("has_" + safeName(AZURITE.get()), has(AZURITE.get()))
            .save(consumer);
    }
}
