package net.tropicraft.core.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.tropicraft.core.common.block.TropicraftBlocks.*;
import static net.tropicraft.core.common.block.TropicraftFlower.*;

public class TropicraftBlockTagsProvider extends BlockTagsProvider {

    public TropicraftBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags() {
        // Filling vanilla tags
        
        createAndAppend(TropicraftTags.Blocks.SAND, BlockTags.SAND,
                PURIFIED_SAND, CORAL_SAND, FOAMY_SAND, MINERAL_SAND, VOLCANIC_SAND);

        createTag(TropicraftTags.Blocks.MUD, MUD, MUD_WITH_PIANGUAS);

        // Saplings & Leaves
        createAndAppend(TropicraftTags.Blocks.SAPLINGS, BlockTags.SAPLINGS,
                PALM_SAPLING, MAHOGANY_SAPLING, GRAPEFRUIT_SAPLING, LEMON_SAPLING, LIME_SAPLING, ORANGE_SAPLING, PAPAYA_SAPLING,
                RED_MANGROVE_PROPAGULE, TALL_MANGROVE_PROPAGULE, TEA_MANGROVE_PROPAGULE, BLACK_MANGROVE_PROPAGULE
        );
        createAndAppend(TropicraftTags.Blocks.LEAVES, BlockTags.LEAVES,
                MAHOGANY_LEAVES, PALM_LEAVES, KAPOK_LEAVES, FRUIT_LEAVES, GRAPEFRUIT_LEAVES, LEMON_LEAVES, LIME_LEAVES, ORANGE_LEAVES,
                RED_MANGROVE_LEAVES, TALL_MANGROVE_LEAVES, TEA_MANGROVE_LEAVES, BLACK_MANGROVE_LEAVES, PAPAYA_LEAVES);
        
        // Flowers
        createAndAppend(TropicraftTags.Blocks.SMALL_FLOWERS, BlockTags.SMALL_FLOWERS,
                ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, TROPICAL_FERN, FOLIAGE, MAGIC_MUSHROOM, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM);
        // Some extra flower tags for our own worldgen
        createTag(TropicraftTags.Blocks.TROPICS_FLOWERS,
                ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, TROPICAL_FERN, FOLIAGE, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM);
        createTag(TropicraftTags.Blocks.RAINFOREST_FLOWERS,
                MAGIC_MUSHROOM, CROCOSMIA, COMMELINA_DIFFUSA, ORCHID, ANEMONE, BROMELIAD, CANNA, RED_ANTHURIUM, DRACAENA, ORANGE_ANTHURIUM);
        createTag(TropicraftTags.Blocks.OVERWORLD_FLOWERS,
                ORCHID, PATHOS, RED_ANTHURIUM, COMMELINA_DIFFUSA, ANEMONE, ORANGE_ANTHURIUM);
        
        // Logs & Planks
        createAndAppend(TropicraftTags.Blocks.LOGS, BlockTags.LOGS,
                PALM_LOG, MAHOGANY_LOG, LIGHT_MANGROVE_LOG, RED_MANGROVE_LOG, BLACK_MANGROVE_LOG, STRIPPED_MANGROVE_LOG, PAPAYA_LOG);
        createAndAppend(TropicraftTags.Blocks.PLANKS, BlockTags.PLANKS,
                PALM_PLANKS, MAHOGANY_PLANKS, MANGROVE_PLANKS);

        createTag(TropicraftTags.Blocks.ROOTS,
                RED_MANGROVE_ROOTS, LIGHT_MANGROVE_ROOTS, BLACK_MANGROVE_ROOTS);
        
        // Wooden deco blocks
        createAndAppend(TropicraftTags.Blocks.WOODEN_SLABS, BlockTags.WOODEN_SLABS,
                PALM_SLAB, MAHOGANY_SLAB, MANGROVE_SLAB);
        createAndAppend(TropicraftTags.Blocks.WOODEN_STAIRS, BlockTags.WOODEN_STAIRS,
                PALM_STAIRS, MAHOGANY_STAIRS, MANGROVE_STAIRS);
        createAndAppend(TropicraftTags.Blocks.WOODEN_DOORS, BlockTags.WOODEN_DOORS,
                PALM_DOOR, MAHOGANY_DOOR, MANGROVE_DOOR);
        createAndAppend(TropicraftTags.Blocks.WOODEN_TRAPDOORS, BlockTags.WOODEN_TRAPDOORS,
                PALM_TRAPDOOR, MAHOGANY_TRAPDOOR, MANGROVE_TRAPDOOR);
        createAndAppend(TropicraftTags.Blocks.WOODEN_FENCES, BlockTags.WOODEN_FENCES,
                PALM_FENCE, MAHOGANY_FENCE, MANGROVE_FENCE);
        
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

        createAndAppend(TropicraftTags.Blocks.CLIMBABLE, BlockTags.CLIMBABLE, BAMBOO_LADDER);

        // Flower pots
        createAndAppend(TropicraftTags.Blocks.FLOWER_POTS, BlockTags.FLOWER_POTS,
                Stream.concat(BAMBOO_POTTED_TROPICS_PLANTS.stream(), Stream.concat(BAMBOO_POTTED_VANILLA_PLANTS.stream(), VANILLA_POTTED_TROPICS_PLANTS.stream()))
                        .toArray(RegistryObject[]::new));

        appendToTag(BlockTags.MINEABLE_WITH_HOE, TropicraftTags.Blocks.LEAVES);
        
        createTag(BlockTags.MINEABLE_WITH_AXE,
                MAHOGANY_PLANKS, MAHOGANY_LOG, MAHOGANY_STAIRS, MAHOGANY_SLAB, MAHOGANY_FENCE, MAHOGANY_DOOR, MAHOGANY_TRAPDOOR, MAHOGANY_BOARDWALK, MAHOGANY_FENCE_GATE, MAHOGANY_WOOD,
                BLACK_MANGROVE_ROOTS, RED_MANGROVE_ROOTS, LIGHT_MANGROVE_ROOTS,
                BLACK_MANGROVE_LOG, RED_MANGROVE_LOG, LIGHT_MANGROVE_LOG, STRIPPED_MANGROVE_LOG,
                BLACK_MANGROVE_WOOD, RED_MANGROVE_WOOD, LIGHT_MANGROVE_WOOD, STRIPPED_MANGROVE_WOOD,
                MANGROVE_PLANKS, MANGROVE_STAIRS, MANGROVE_SLAB, MANGROVE_FENCE, MANGROVE_DOOR, MANGROVE_TRAPDOOR, MANGROVE_BOARDWALK, MANGROVE_FENCE_GATE,
                PALM_PLANKS, PALM_LOG, PALM_STAIRS, PALM_SLAB, PALM_FENCE, PALM_DOOR, PALM_TRAPDOOR, PALM_BOARDWALK, PALM_FENCE_GATE, PALM_WOOD,
                PAPAYA_LOG, PAPAYA_WOOD,
                BAMBOO_BOARDWALK, BAMBOO_LADDER, COCONUT);

        createTag(BlockTags.MINEABLE_WITH_SHOVEL,
                MUD, MUD_WITH_PIANGUAS, PURIFIED_SAND, CORAL_SAND, FOAMY_SAND, MINERAL_SAND, VOLCANIC_SAND);

        createTag(BlockTags.MINEABLE_WITH_PICKAXE,
                AZURITE_ORE, ZIRCON_ORE, EUDIALYTE_ORE, SHAKA_ORE, MANGANESE_ORE,
                AZURITE_BLOCK, ZIRCON_BLOCK, EUDIALYTE_BLOCK, SHAKA_BLOCK, MANGANESE_BLOCK);

        createTag(BlockTags.NEEDS_IRON_TOOL,
                AZURITE_ORE, ZIRCON_ORE, EUDIALYTE_ORE, SHAKA_ORE, MANGANESE_ORE,
                AZURITE_BLOCK, ZIRCON_BLOCK, EUDIALYTE_BLOCK, SHAKA_BLOCK, MANGANESE_BLOCK);

        createTag(TropicraftTags.Blocks.BONGOS, SMALL_BONGO_DRUM, MEDIUM_BONGO_DRUM, LARGE_BONGO_DRUM);
    }
    
    @SafeVarargs
    private final <T> T[] resolveAll(IntFunction<T[]> creator, Supplier<? extends T>... suppliers) {
        return Arrays.stream(suppliers).map(Supplier::get).toArray(creator);
    }

    @SafeVarargs
    private final void createTag(TagKey<Block> tag, Supplier<? extends Block>... blocks) {
        tag(tag).add(resolveAll(Block[]::new, blocks));
    }
    
    @SafeVarargs
    private final void appendToTag(TagKey<Block> tag, TagKey<Block>... toAppend) {
        tag(tag).addTags(toAppend);
    }
    
    @SafeVarargs
    private final void extendTag(TagKey<Block> tag, TagKey<Block> toExtend, Supplier<? extends Block>... blocks) {
        tag(tag).addTag(toExtend).add(resolveAll(Block[]::new, blocks));
    }
    
    @SafeVarargs
    private final void createAndAppend(TagKey<Block> tag, TagKey<Block> to, Supplier<? extends Block>... blocks) {
        createTag(tag, blocks);
        appendToTag(to, tag);
    }

    @SafeVarargs
    private final void extendAndAppend(TagKey<Block> tag, TagKey<Block> toExtend, TagKey<Block> to, Supplier<? extends Block>... blocks) {
        extendTag(tag, toExtend, blocks);
        appendToTag(to, tag);
    }
    
    @Override
    public String getName() {
        return "Tropicraft Block Tags";
    }
}
