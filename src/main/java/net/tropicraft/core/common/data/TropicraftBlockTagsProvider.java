package net.tropicraft.core.common.data;

import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_POTTED_TROPICS_PLANTS;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_POTTED_VANILLA_PLANTS;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_WALL;
import static net.tropicraft.core.common.block.TropicraftBlocks.CORAL_SAND;
import static net.tropicraft.core.common.block.TropicraftBlocks.FOAMY_SAND;
import static net.tropicraft.core.common.block.TropicraftBlocks.FRUIT_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.GRAPEFRUIT_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.GRAPEFRUIT_SAPLING;
import static net.tropicraft.core.common.block.TropicraftBlocks.KAPOK_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.LARGE_BONGO_DRUM;
import static net.tropicraft.core.common.block.TropicraftBlocks.LEMON_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.LEMON_SAPLING;
import static net.tropicraft.core.common.block.TropicraftBlocks.LIME_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.LIME_SAPLING;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_PLANKS;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_SAPLING;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.MEDIUM_BONGO_DRUM;
import static net.tropicraft.core.common.block.TropicraftBlocks.MINERAL_SAND;
import static net.tropicraft.core.common.block.TropicraftBlocks.ORANGE_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.ORANGE_SAPLING;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_LOG;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_PLANKS;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_SAPLING;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.PURIFIED_SAND;
import static net.tropicraft.core.common.block.TropicraftBlocks.SMALL_BONGO_DRUM;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_DOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_SLAB;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_STAIRS;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_TRAPDOOR;
import static net.tropicraft.core.common.block.TropicraftBlocks.VANILLA_POTTED_TROPICS_PLANTS;
import static net.tropicraft.core.common.block.TropicraftBlocks.VOLCANIC_SAND;
import static net.tropicraft.core.common.block.TropicraftFlower.ACAI_VINE;
import static net.tropicraft.core.common.block.TropicraftFlower.ANEMONE;
import static net.tropicraft.core.common.block.TropicraftFlower.BROMELIAD;
import static net.tropicraft.core.common.block.TropicraftFlower.CANNA;
import static net.tropicraft.core.common.block.TropicraftFlower.COMMELINA_DIFFUSA;
import static net.tropicraft.core.common.block.TropicraftFlower.CROCOSMIA;
import static net.tropicraft.core.common.block.TropicraftFlower.CROTON;
import static net.tropicraft.core.common.block.TropicraftFlower.DRACAENA;
import static net.tropicraft.core.common.block.TropicraftFlower.FOLIAGE;
import static net.tropicraft.core.common.block.TropicraftFlower.MAGIC_MUSHROOM;
import static net.tropicraft.core.common.block.TropicraftFlower.ORANGE_ANTHURIUM;
import static net.tropicraft.core.common.block.TropicraftFlower.ORCHID;
import static net.tropicraft.core.common.block.TropicraftFlower.PATHOS;
import static net.tropicraft.core.common.block.TropicraftFlower.RED_ANTHURIUM;
import static net.tropicraft.core.common.block.TropicraftFlower.TROPICAL_FERN;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;

public class TropicraftBlockTagsProvider extends BlockTagsProvider {

    public TropicraftBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void registerTags() {
        // Filling vanilla tags
        
        createAndAppend(TropicraftTags.Blocks.SAND, BlockTags.SAND,
                PURIFIED_SAND, CORAL_SAND, FOAMY_SAND, MINERAL_SAND, VOLCANIC_SAND);
        
        // Saplings & Leaves
        createAndAppend(TropicraftTags.Blocks.SAPLINGS, BlockTags.SAPLINGS,
                PALM_SAPLING, MAHOGANY_SAPLING, GRAPEFRUIT_SAPLING, LEMON_SAPLING, LIME_SAPLING, ORANGE_SAPLING);
        createAndAppend(TropicraftTags.Blocks.LEAVES, BlockTags.LEAVES,
                MAHOGANY_LEAVES, PALM_LEAVES, KAPOK_LEAVES, FRUIT_LEAVES, GRAPEFRUIT_LEAVES, LEMON_LEAVES, LIME_LEAVES, ORANGE_LEAVES);
        
        // Flowers
        createAndAppend(TropicraftTags.Blocks.SMALL_FLOWERS, BlockTags.SMALL_FLOWERS,
                ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, TROPICAL_FERN, FOLIAGE, MAGIC_MUSHROOM, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM);
        // Some extra flower tags for our own worldgen
        createTag(TropicraftTags.Blocks.TROPICS_FLOWERS,
                ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, TROPICAL_FERN, FOLIAGE, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM);
        createTag(TropicraftTags.Blocks.RAINFOREST_FLOWERS,
                MAGIC_MUSHROOM);
        createTag(TropicraftTags.Blocks.OVERWORLD_FLOWERS,
                ORCHID, PATHOS, RED_ANTHURIUM, COMMELINA_DIFFUSA, ANEMONE, ORANGE_ANTHURIUM);
        
        // Logs & Planks
        createAndAppend(TropicraftTags.Blocks.LOGS, BlockTags.LOGS,
                PALM_LOG, MAHOGANY_LOG);
        createAndAppend(TropicraftTags.Blocks.PLANKS, BlockTags.PLANKS,
                PALM_PLANKS, MAHOGANY_PLANKS);
        
        // Wooden deco blocks
        createAndAppend(TropicraftTags.Blocks.WOODEN_SLABS, BlockTags.WOODEN_SLABS,
                PALM_SLAB, MAHOGANY_SLAB);
        createAndAppend(TropicraftTags.Blocks.WOODEN_STAIRS, BlockTags.WOODEN_STAIRS,
                PALM_STAIRS, MAHOGANY_STAIRS);
        createAndAppend(TropicraftTags.Blocks.WOODEN_DOORS, BlockTags.WOODEN_DOORS,
                PALM_DOOR, MAHOGANY_DOOR);
        createAndAppend(TropicraftTags.Blocks.WOODEN_TRAPDOORS, BlockTags.WOODEN_TRAPDOORS,
                PALM_TRAPDOOR, MAHOGANY_TRAPDOOR);
        createAndAppend(TropicraftTags.Blocks.WOODEN_FENCES, BlockTags.WOODEN_FENCES,
                PALM_FENCE, MAHOGANY_FENCE);
        
        // All deco blocks
        extendAndAppend(TropicraftTags.Blocks.SLABS, TropicraftTags.Blocks.WOODEN_SLABS, BlockTags.SLABS,
                BAMBOO_SLAB, CHUNK_SLAB, THATCH_SLAB);
        extendAndAppend(TropicraftTags.Blocks.STAIRS, TropicraftTags.Blocks.WOODEN_STAIRS, BlockTags.STAIRS,
                BAMBOO_STAIRS, CHUNK_STAIRS, THATCH_STAIRS);
        extendAndAppend(TropicraftTags.Blocks.DOORS, TropicraftTags.Blocks.WOODEN_DOORS, BlockTags.DOORS,
                BAMBOO_DOOR, THATCH_DOOR);
        extendAndAppend(TropicraftTags.Blocks.TRAPDOORS, TropicraftTags.Blocks.WOODEN_TRAPDOORS, BlockTags.TRAPDOORS,
                BAMBOO_TRAPDOOR, THATCH_TRAPDOOR);
        extendAndAppend(TropicraftTags.Blocks.FENCES, TropicraftTags.Blocks.WOODEN_FENCES, BlockTags.FENCES,
                BAMBOO_FENCE, CHUNK_FENCE, THATCH_FENCE);
        createAndAppend(TropicraftTags.Blocks.WALLS, BlockTags.WALLS,
                CHUNK_WALL);
        
        // Flower pots
        createAndAppend(TropicraftTags.Blocks.FLOWER_POTS, BlockTags.FLOWER_POTS,
                Stream.concat(BAMBOO_POTTED_TROPICS_PLANTS.stream(), Stream.concat(BAMBOO_POTTED_VANILLA_PLANTS.stream(), VANILLA_POTTED_TROPICS_PLANTS.stream()))
                        .toArray(RegistryObject[]::new));

        createTag(TropicraftTags.Blocks.BONGOS, SMALL_BONGO_DRUM, MEDIUM_BONGO_DRUM, LARGE_BONGO_DRUM);
    }
    
    @SafeVarargs
    private final <T> T[] resolveAll(IntFunction<T[]> creator, Supplier<? extends T>... suppliers) {
        return Arrays.stream(suppliers).map(Supplier::get).toArray(creator);
    }

    @SafeVarargs
    private final void createTag(INamedTag<Block> tag, Supplier<? extends Block>... blocks) {
        getOrCreateBuilder(tag).add(resolveAll(Block[]::new, blocks));
    }
    
    @SafeVarargs
    private final void appendToTag(INamedTag<Block> tag, INamedTag<Block>... toAppend) {
        getOrCreateBuilder(tag).addTags(toAppend);
    }
    
    @SafeVarargs
    private final void extendTag(INamedTag<Block> tag, INamedTag<Block> toExtend, Supplier<? extends Block>... blocks) {
        getOrCreateBuilder(tag).addTag(toExtend).add(resolveAll(Block[]::new, blocks));
    }
    
    @SafeVarargs
    private final void createAndAppend(INamedTag<Block> tag, INamedTag<Block> to, Supplier<? extends Block>... blocks) {
        createTag(tag, blocks);
        appendToTag(to, tag);
    }

    @SafeVarargs
    private final void extendAndAppend(INamedTag<Block> tag, INamedTag<Block> toExtend, INamedTag<Block> to, Supplier<? extends Block>... blocks) {
        extendTag(tag, toExtend, blocks);
        appendToTag(to, tag);
    }
    
    @Override
    public String getName() {
        return "Tropicraft Block Tags";
    }
}
