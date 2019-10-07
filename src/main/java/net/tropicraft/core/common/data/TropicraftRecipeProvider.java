package net.tropicraft.core.common.data;

import static net.tropicraft.core.common.block.TropicraftBlocks.*;
import static net.tropicraft.core.common.item.TropicraftItems.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.tropicraft.Info;
import net.tropicraft.core.common.TropicraftTags;

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
        
        for (DyeColor color : DyeColor.values()) {
            IItemProvider wool = SheepEntity.WOOL_BY_COLOR.get(color);
            ShapedRecipeBuilder.shapedRecipe(UMBRELLAS.get(color).get())
                .patternLine("WWW").patternLine(" B ").patternLine(" B ")
                .key('W', wool)
                .key('B', Tags.Items.RODS_WOODEN)
                .addCriterion("has_" + color.getName() + "_wool", this.hasItem(wool))
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
        
        food(RAW_COFFEE_BEAN, ROASTED_COFFEE_BEAN, 0.1F, consumer);
        
        ShapedRecipeBuilder.shapedRecipe(BAMBOO_MUG.get())
            .patternLine("X X").patternLine("X X").patternLine("XXX")
            .key('X', Items.BAMBOO)
            .addCriterion("has_bamboo", this.hasItem(Items.BAMBOO))
            .build(consumer);
        
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
        
        // Stairs
        stairs(PALM_PLANKS, PALM_STAIRS, "wooden_stairs", false, consumer);
        stairs(MAHOGANY_PLANKS, MAHOGANY_STAIRS, "wooden_stairs", false, consumer);
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
        slab(THATCH_BUNDLE, THATCH_SLAB, null, false, consumer);
        slab(BAMBOO_BUNDLE, BAMBOO_SLAB, null, false, consumer);
        slab(CHUNK, CHUNK_SLAB, null, true, consumer);
        
        // Fences
        fence(PALM_PLANKS, PALM_FENCE, "wooden_fence", consumer);
        fence(MAHOGANY_PLANKS, MAHOGANY_FENCE, "wooden_fence", consumer);
        fence(THATCH_BUNDLE, THATCH_FENCE, null, consumer);
        fence(BAMBOO_BUNDLE, BAMBOO_FENCE, null, consumer);
        fence(CHUNK, CHUNK_FENCE, null, consumer);
        
        // Fence Gates
        fenceGate(PALM_PLANKS, PALM_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(MAHOGANY_PLANKS, MAHOGANY_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(THATCH_BUNDLE, THATCH_FENCE_GATE, null, consumer);
        fenceGate(BAMBOO_BUNDLE, BAMBOO_FENCE_GATE, null, consumer);
        fenceGate(CHUNK, CHUNK_FENCE_GATE, null, consumer);
        
        // Walls
        wall(CHUNK, CHUNK_WALL, consumer);
        
        // Doors
        door(PALM_PLANKS, PALM_DOOR, "wooden_door", consumer);
        door(MAHOGANY_PLANKS, MAHOGANY_DOOR, "wooden_door", consumer);
        door(THATCH_BUNDLE, THATCH_DOOR, null, consumer);
        door(BAMBOO_BUNDLE, BAMBOO_DOOR, null, consumer);
        
        // Trap doors
        trapDoor(PALM_PLANKS, PALM_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(MAHOGANY_PLANKS, MAHOGANY_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(THATCH_BUNDLE, THATCH_TRAPDOOR, null, consumer);
        trapDoor(BAMBOO_BUNDLE, BAMBOO_TRAPDOOR, null, consumer);
        
        // Bongos
        bongo(IGUANA_LEATHER, MAHOGANY_PLANKS, 1, SMALL_BONGO_DRUM, consumer);
        bongo(IGUANA_LEATHER, MAHOGANY_PLANKS, 2, MEDIUM_BONGO_DRUM, consumer);
        bongo(IGUANA_LEATHER, MAHOGANY_PLANKS, 3, LARGE_BONGO_DRUM, consumer);
        
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
            .build(consumer, new ResourceLocation(Info.MODID, "sifter_with_glass_pane"));
        
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
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void ore(Tag<Item> source, Supplier<T> result, float xp, Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(source), result.get(), xp, 100)
            .addCriterion("has_" + safeName(source.getId()), this.hasItem(source))
            .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(source), result.get(), xp, 100)
            .addCriterion("has_" + safeName(source.getId()), this.hasItem(source))
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

    @CheckReturnValue
    private <T extends IItemProvider & IForgeRegistryEntry<?>> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
        return ShapelessRecipeBuilder.shapelessRecipe(result.get(), amount)
            .addIngredient(source.get(), required)
            .addCriterion("has_" + safeName(source.get()), this.hasItem(source.get()));
    }

    private <T extends IItemProvider & IForgeRegistryEntry<?>> void dye(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).build(consumer, new ResourceLocation(Info.MODID, result.get().getRegistryName().getPath()));
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).build(consumer);
    }
    
    private <T extends IItemProvider & IForgeRegistryEntry<?>> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
        singleItemUnfinished(source, result, 1, 4)
            .setGroup("planks")
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
}
