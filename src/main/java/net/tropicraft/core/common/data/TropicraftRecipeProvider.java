package net.tropicraft.core.common.data;

import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
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

import static net.tropicraft.core.common.block.TropicraftBlocks.*;
import static net.tropicraft.core.common.block.TropicraftFlower.*;
import static net.tropicraft.core.common.item.TropicraftItems.*;

public class TropicraftRecipeProvider extends RecipeProvider {

    public TropicraftRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
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
            IItemProvider wool = SheepEntity.WOOL_BY_COLOR.get(color);
            ShapedRecipeBuilder.shapedRecipe(UMBRELLAS.get(color).get())
                .patternLine("WWW").patternLine(" B ").patternLine(" B ")
                .setGroup(Constants.MODID + ":umbrellas")
                .key('W', wool)
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + color.getString() + "_wool", this.hasItem(wool))
                .build(consumer);
            
            ShapedRecipeBuilder.shapedRecipe(CHAIRS.get(color).get())
                .patternLine("BWB").patternLine("BWB").patternLine("BWB")
                .setGroup(Constants.MODID + ":chairs")
                .key('W', wool)
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + color.getString() + "_wool", this.hasItem(wool))
                .build(consumer);
            
            ShapedRecipeBuilder.shapedRecipe(BEACH_FLOATS.get(color).get())
                .patternLine("WWW").patternLine("BBB")
                .setGroup(Constants.MODID + ":beach_floats")
                .key('W', wool)
                .key('B', Blocks.BAMBOO)
                .addCriterion("has_" + color.getString() + "_wool", this.hasItem(wool))
                .build(consumer);
        
            // TODO other colored items
        }
        
        // Override the vanilla recipe to output ours, it's tagged so it will behave the same
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_STICK.get())
            .patternLine("X").patternLine("X")
            .key('X', Items.BAMBOO)
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer, new ResourceLocation("stick_from_bamboo_item"));
        
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_SPEAR.get())
            .patternLine("X ").patternLine(" X")
            .key('X', BAMBOO_STICK.get())
            .addCriterion("has_bamboo_stick", this.hasItem(BAMBOO_STICK.get()))
            .build(consumer);
        
        ShapelessRecipeBuilder.shapelessRecipe(RAW_COFFEE_BEAN.get())
            .addIngredient(COFFEE_BERRY.get())
            .addCriterion("has_coffee_bean", this.hasItem(COFFEE_BERRY.get()))
            .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ZIRCONIUM.get())
            .addIngredient(AZURITE.get(), 2)
            .addIngredient(ZIRCON.get(), 2)
            .addCriterion("has_zircon", hasItem(ZIRCON.get()))
            .addCriterion("has_azurite", hasItem(AZURITE.get()))
            .build(consumer);
        
        food(RAW_COFFEE_BEAN, ROASTED_COFFEE_BEAN, 0.1F, consumer);
        
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_MUG.get())
            .patternLine("X X").patternLine("X X").patternLine("XXX")
            .key('X', Items.BAMBOO)
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer);

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

        bark(WHITE_MANGROVE_LOG, WHITE_MANGROVE_WOOD, consumer);
        bark(RED_MANGROVE_LOG, RED_MANGROVE_WOOD, consumer);

        // Stairs
        stairs(PALM_PLANKS, PALM_STAIRS, "wooden_stairs", false, consumer);
        stairs(MAHOGANY_PLANKS, MAHOGANY_STAIRS, "wooden_stairs", false, consumer);
        stairs(WHITE_MANGROVE_PLANKS, WHITE_MANGROVE_STAIRS, "wooden_stairs", false, consumer);
        stairs(RED_MANGROVE_PLANKS, RED_MANGROVE_STAIRS, "wooden_stairs", false, consumer);

        stairs(THATCH_BUNDLE, THATCH_STAIRS, null, false, consumer);
        stairs(BAMBOO_BUNDLE, BAMBOO_STAIRS, null, false, consumer);
        stairs(CHUNK, CHUNK_STAIRS, null, true, consumer);
        
        ShapedRecipeBuilder.shapedRecipe(THATCH_STAIRS_FUZZY.get(), 4)
            .patternLine("C  ").patternLine("XC ").patternLine("XXC")
            .key('X', THATCH_BUNDLE.get())
            .key('C', Items.SUGAR_CANE)
            .addCriterion("has_thatch_bundle", this.hasItem(THATCH_BUNDLE.get()))
            .build(consumer);
        
        // Slabs
        slab(PALM_PLANKS, PALM_SLAB, "wooden_slab", false, consumer);
        slab(MAHOGANY_PLANKS, MAHOGANY_SLAB, "wooden_slab", false, consumer);
        slab(WHITE_MANGROVE_PLANKS, WHITE_MANGROVE_SLAB, "wooden_slab", false, consumer);
        slab(RED_MANGROVE_PLANKS, RED_MANGROVE_SLAB, "wooden_slab", false, consumer);

        slab(THATCH_BUNDLE, THATCH_SLAB, null, false, consumer);
        slab(BAMBOO_BUNDLE, BAMBOO_SLAB, null, false, consumer);
        slab(CHUNK, CHUNK_SLAB, null, true, consumer);
        
        // Fences
        fence(PALM_PLANKS, PALM_FENCE, "wooden_fence", consumer);
        fence(MAHOGANY_PLANKS, MAHOGANY_FENCE, "wooden_fence", consumer);
        fence(WHITE_MANGROVE_PLANKS, WHITE_MANGROVE_FENCE, "wooden_fence", consumer);
        fence(RED_MANGROVE_PLANKS, RED_MANGROVE_FENCE, "wooden_fence", consumer);

        fence(THATCH_BUNDLE, THATCH_FENCE, null, consumer);
        fence(BAMBOO_BUNDLE, BAMBOO_FENCE, null, consumer);
        fence(CHUNK, CHUNK_FENCE, null, consumer);
        
        // Fence Gates
        fenceGate(PALM_PLANKS, PALM_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(MAHOGANY_PLANKS, MAHOGANY_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(WHITE_MANGROVE_PLANKS, WHITE_MANGROVE_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(RED_MANGROVE_PLANKS, RED_MANGROVE_FENCE_GATE, "wooden_fence_gate", consumer);

        fenceGate(THATCH_BUNDLE, THATCH_FENCE_GATE, null, consumer);
        fenceGate(BAMBOO_BUNDLE, BAMBOO_FENCE_GATE, null, consumer);
        fenceGate(CHUNK, CHUNK_FENCE_GATE, null, consumer);
        
        // Walls
        wall(CHUNK, CHUNK_WALL, consumer);
        
        // Doors
        door(PALM_PLANKS, PALM_DOOR, "wooden_door", consumer);
        door(MAHOGANY_PLANKS, MAHOGANY_DOOR, "wooden_door", consumer);
        door(WHITE_MANGROVE_PLANKS, WHITE_MANGROVE_DOOR, "wooden_door", consumer);
        door(RED_MANGROVE_PLANKS, RED_MANGROVE_DOOR, "wooden_door", consumer);

        door(THATCH_BUNDLE, THATCH_DOOR, null, consumer);
        door(BAMBOO_BUNDLE, BAMBOO_DOOR, null, consumer);

        // Trap doors
        trapDoor(PALM_PLANKS, PALM_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(MAHOGANY_PLANKS, MAHOGANY_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(WHITE_MANGROVE_PLANKS, WHITE_MANGROVE_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(RED_MANGROVE_PLANKS, RED_MANGROVE_TRAPDOOR, "wooden_trapdoor", consumer);

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

        ShapedRecipeBuilder.shapedRecipe(WATER_WAND.get(), 1)
            .patternLine("  X")
            .patternLine(" Y ")
            .patternLine("Y  ")
            .key('X', AZURITE.get())
            .key('Y', Items.GOLD_INGOT)
            .addCriterion("has_" + safeName(AZURITE.get()), hasItem(AZURITE.get()))
            .addCriterion("has_gold_ingot", hasItem(Items.GOLD_INGOT))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(BAMBOO_ITEM_FRAME.get(), 1)
            .patternLine("XXX")
            .patternLine("XYX")
            .patternLine("XXX")
            .key('X', Items.BAMBOO)
            .key('Y', Items.LEATHER)
            .addCriterion("has_bamboo", hasItem(Items.BAMBOO))
            .addCriterion("has_leather", hasItem(Items.LEATHER))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(AIR_COMPRESSOR.get(), 1)
            .patternLine("XXX")
            .patternLine("XYX")
            .patternLine("XXX")
            .key('X', CHUNK.get())
            .key('Y', AZURITE.get())
            .addCriterion("has_" + safeName(CHUNK.get()), hasItem(CHUNK.get()))
            .addCriterion("has_" + safeName(AZURITE.get()), hasItem(AZURITE.get()))
            .build(consumer);
        
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_LADDER.get(), 4)
            .patternLine("S S").patternLine("BSB").patternLine("S S")
            .key('S', BAMBOO_STICK.get())
            .key('B', Items.BAMBOO)
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer);
        
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_CHEST.get())
            .patternLine("BBB").patternLine("B B").patternLine("BBB")
            .key('B', Items.BAMBOO)
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer);
        
        ShapedRecipeBuilder.shapedRecipe(SIFTER.get())
            .patternLine("XXX").patternLine("XIX").patternLine("XXX")
            .key('X', ItemTags.PLANKS)
            .key('I', Tags.Items.GLASS)
            .setGroup("tropicraft:sifter")
            .addCriterion("has_glass", this.hasItem(Tags.Items.GLASS))
            .build(consumer);
        
        ShapedRecipeBuilder.shapedRecipe(SIFTER.get())
            .patternLine("XXX").patternLine("XIX").patternLine("XXX")
            .key('X', ItemTags.PLANKS)
            .key('I', Tags.Items.GLASS_PANES)
            .setGroup("tropicraft:sifter")
            .addCriterion("has_glass_pane", this.hasItem(Tags.Items.GLASS_PANES))
            .build(consumer, new ResourceLocation(Constants.MODID, "sifter_with_glass_pane"));
        
        ShapedRecipeBuilder.shapedRecipe(DRINK_MIXER.get())
            .patternLine("XXX").patternLine("XYX").patternLine("XXX")
            .key('X', CHUNK.get())
            .key('Y', BAMBOO_MUG.get())
            .addCriterion("has_bamboo_mug", this.hasItem(BAMBOO_MUG.get()))
            .build(consumer);
        
        ShapedRecipeBuilder.shapedRecipe(TIKI_TORCH.get())
            .patternLine("Y").patternLine("X").patternLine("X")
            .key('X', BAMBOO_STICK.get())
            .key('Y', ItemTags.COALS)
            .addCriterion("has_bamboo_stick", this.hasItem(BAMBOO_STICK.get()))
            .build(consumer);
        
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_FLOWER_POT.get())
            .patternLine("# #").patternLine(" # ")
            .key('#', Items.BAMBOO)
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer);
        
        ShapelessRecipeBuilder.shapelessRecipe(COCKTAILS.get(Drink.PINA_COLADA).get())
            .addIngredient(BAMBOO_MUG.get())
            .addIngredient(COCONUT_CHUNK.get())
            .addIngredient(PINEAPPLE_CUBES.get())
            .addCriterion("has_bamboo_mug", this.hasItem(BAMBOO_MUG.get()))
            .build(consumer);
        
        ShapelessRecipeBuilder.shapelessRecipe(TROPICAL_FERTILIZER.get())
            .addIngredient(TropicraftFlower.MAGIC_MUSHROOM.get())
            .addIngredient(TropicraftFlower.CROTON.get())
            .addCriterion("has_magic_mushroom", this.hasItem(TropicraftFlower.MAGIC_MUSHROOM.get()))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(DAGGER.get())
            .patternLine("X")
            .patternLine("I")
            .key('X', CHUNK.get())
            .key('I', BAMBOO_STICK.get())
            .addCriterion("has_" + safeName(CHUNK.get()), hasItem(CHUNK.get()))
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(BLOW_GUN.get())
            .patternLine("X  ")
            .patternLine(" I ")
            .patternLine("  X")
            .key('X', BAMBOO_STICK.get())
            .key('I', ZIRCON.get())
            .addCriterion("has_" + safeName(ZIRCON.get()), hasItem(ZIRCON.get()))
            .addCriterion("has_" + safeName(BAMBOO_STICK.get()), hasItem(BAMBOO_STICK.get()))
            .build(consumer);
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
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void ore(INamedTag<Item> source, Supplier<T> result, float xp, Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(source), result.get(), xp, 100)
            .addCriterion("has_" + safeName(source.getName()), this.hasItem(source))
            .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(source), result.get(), xp, 100)
            .addCriterion("has_" + safeName(source.getName()), this.hasItem(source))
            .build(consumer, safeId(result.get()) + "_from_blasting");
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void food(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100)
            .addCriterion("has_" + safeName(source.get().getRegistryName()), this.hasItem(source.get()))
            .build(consumer);
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100, IRecipeSerializer.SMOKING)
            .addCriterion("has_" + safeName(source.get().getRegistryName()), this.hasItem(source.get()))
            .build(consumer, safeId(result.get()) + "_from_smoking");
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100, IRecipeSerializer.CAMPFIRE_COOKING)
            .addCriterion("has_" + safeName(source.get().getRegistryName()), this.hasItem(source.get()))
            .build(consumer, safeId(result.get()) + "_from_campfire");
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void storage(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        // TODO probably not ported correctly
        ShapedRecipeBuilder.shapedRecipe(output.get())
            .patternLine("XXX").patternLine("XXX").patternLine("XXX")
            .key('X', input.get())
            .addCriterion("has_at_least_9_" + safeName(input.get()), this.hasItem(input.get()))
            .build(consumer);
        
        ShapelessRecipeBuilder.shapelessRecipe(input.get(), 9)
            .addIngredient(output.get())
            .addCriterion("has_" + safeName(output.get()), this.hasItem(output.get()))
            .build(consumer, safeId(input.get()) + "_from_" + safeName(output.get()));
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void pickaxe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("XXX")
                .patternLine(" B ")
                .patternLine(" B ")
                .key('X', input.get())
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .addCriterion("has_" + safeName(Items.BAMBOO), hasItem(Items.BAMBOO))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void shovel(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine(" X ")
                .patternLine(" B ")
                .patternLine(" B ")
                .key('X', input.get())
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .addCriterion("has_" + safeName(Items.BAMBOO), hasItem(Items.BAMBOO))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void axe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("XX ")
                .patternLine("XB ")
                .patternLine(" B ")
                .key('X', input.get())
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .addCriterion("has_" + safeName(Items.BAMBOO), hasItem(Items.BAMBOO))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void hoe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("XX ")
                .patternLine(" B ")
                .patternLine(" B ")
                .key('X', input.get())
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .addCriterion("has_" + safeName(Items.BAMBOO), hasItem(Items.BAMBOO))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void sword(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine(" X ")
                .patternLine(" X ")
                .patternLine(" B ")
                .key('X', input.get())
                .key('B', BAMBOO_STICK.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .addCriterion("has_" + safeName(Items.BAMBOO), hasItem(Items.BAMBOO))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void helmet(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("XXX")
                .patternLine("X X")
                .key('X', input.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void chestplate(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("X X")
                .patternLine("XXX")
                .patternLine("XXX")
                .key('X', input.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void leggings(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("XXX")
                .patternLine("X X")
                .patternLine("X X")
                .key('X', input.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void boots(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(output.get())
                .patternLine("X X")
                .patternLine("X X")
                .key('X', input.get())
                .addCriterion("has_" + safeName(input.get()), hasItem(input.get()))
                .build(consumer);
    }

    @CheckReturnValue
    private <T extends IItemProvider & IForgeRegistryEntry<?>> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
        return ShapelessRecipeBuilder.shapelessRecipe(result.get(), amount)
            .addIngredient(source.get(), required)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()));
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void dye(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).build(consumer, new ResourceLocation(Constants.MODID, result.get().getRegistryName().getPath()));
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
        singleItemUnfinished(source, result, 1, 4)
            .setGroup("planks")
            .build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void bark(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
            .patternLine("##").patternLine("##")
            .key('#', source.get())
            .setGroup("bark")
            .addCriterion("has_log", this.hasItem(Blocks.ACACIA_LOG))
            .build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void stairs(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
            .patternLine("X  ").patternLine("XX ").patternLine("XXX")
            .key('X', source.get())
            .setGroup(group)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
        if (stone) {
            SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get())
                .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
                .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
        }
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void slab(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
            .patternLine("XXX")
            .key('X', source.get())
            .setGroup(group)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
        if (stone) {
            SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get(), 2)
                .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
                .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
        }
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void fence(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
            .patternLine("W#W").patternLine("W#W")
            .key('W', source.get())
            .key('#', Tags.Items.RODS_WOODEN)
            .setGroup(group)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void fenceGate(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get())
            .patternLine("#W#").patternLine("#W#")
            .key('W', source.get())
            .key('#', Tags.Items.RODS_WOODEN)
            .setGroup(group)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void wall(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
            .patternLine("XXX").patternLine("XXX")
            .key('X', source.get())
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get())
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void door(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
            .patternLine("XX").patternLine("XX").patternLine("XX")
            .key('X', source.get())
            .setGroup(group)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void trapDoor(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 2)
            .patternLine("XXX").patternLine("XXX")
            .key('X', source.get())
            .setGroup(group)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()))
            .build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void bongo(Supplier<? extends T> top, Supplier<? extends T> bottom, int size, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get())
            .patternLine(StringUtils.repeat('T', size))
            .patternLine(StringUtils.repeat('B', size))
            .patternLine(StringUtils.repeat('B', size))
            .key('T', top.get())
            .key('B', bottom.get())
            .setGroup("tropicraft:bongos")
            .addCriterion("has_" + safeName(top.get()), this.hasItem(top.get()))
            .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void goggles(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
            .patternLine("YYY")
            .patternLine("X X")
            .patternLine(" Z ")
            .key('X', Blocks.GLASS_PANE)
            .key('Y', ZIRCON.get())
            .key('Z', source.get())
            .addCriterion("has_" + safeName(source.get()), hasItem(source.get()))
            .addCriterion("has_" + safeName(ZIRCON.get()), hasItem(ZIRCON.get()))
            .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void flippers(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
            .patternLine("XX")
            .patternLine("YY")
            .patternLine("XX")
            .key('X', source.get())
            .key('Y', ZIRCON.get())
            .addCriterion("has_" + safeName(source.get()), hasItem(source.get()))
            .addCriterion("has_" + safeName(ZIRCON.get()), hasItem(ZIRCON.get()))
            .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void ponyBottle(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
            .patternLine("Y")
            .patternLine("X")
            .key('X', Blocks.PINK_STAINED_GLASS_PANE)
            .key('Y', Blocks.LEVER)
            .addCriterion("has_" + safeName(Blocks.PINK_STAINED_GLASS_PANE), hasItem(Blocks.PINK_STAINED_GLASS_PANE))
            .build(consumer);
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void harness(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
            .patternLine("Y Y")
            .patternLine("YXY")
            .patternLine("YZY")
            .key('X', source.get())
            .key('Y', TropicraftTags.Items.LEATHER)
            .key('Z', AZURITE.get())
            .addCriterion("has_" + safeName(AZURITE.get()), hasItem(AZURITE.get()))
            .build(consumer);
    }
}
