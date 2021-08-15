package net.tropicraft.core.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;

import java.util.function.Function;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final INamedTag<Block> SAND = modTag("sand");
        public static final INamedTag<Block> MUD = modTag("mud");

        public static final INamedTag<Block> SAPLINGS = modTag("saplings");
        public static final INamedTag<Block> LEAVES = modTag("leaves");

        public static final INamedTag<Block> SMALL_FLOWERS = modTag("small_flowers");
        public static final INamedTag<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        public static final INamedTag<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        public static final INamedTag<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");

        public static final INamedTag<Block> LOGS = modTag("logs");
        public static final INamedTag<Block> PLANKS = modTag("planks");

        public static final INamedTag<Block> WOODEN_SLABS = modTag("wooden_slabs");
        public static final INamedTag<Block> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final INamedTag<Block> WOODEN_DOORS = modTag("wooden_doors");
        public static final INamedTag<Block> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final INamedTag<Block> WOODEN_FENCES = modTag("wooden_fences");

        public static final INamedTag<Block> SLABS = modTag("slabs");
        public static final INamedTag<Block> STAIRS = modTag("stairs");
        public static final INamedTag<Block> DOORS = modTag("doors");
        public static final INamedTag<Block> TRAPDOORS = modTag("trapdoors");
        public static final INamedTag<Block> FENCES = modTag("fences");
        public static final INamedTag<Block> WALLS = modTag("walls");

        public static final INamedTag<Block> FLOWER_POTS = modTag("flower_pots");
        public static final INamedTag<Block> BONGOS = modTag("bongos");

        public static final INamedTag<Block> CLIMBABLE = modTag("climbable");

        static INamedTag<Block> tag(String modid, String name) {
            return tag(BlockTags::makeWrapperTag, modid, name);
        }

        static INamedTag<Block> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static INamedTag<Block> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    public static class Items extends TropicraftTags {

        public static final INamedTag<Item> AZURITE_ORE = compatTag("ores/azurite");
        public static final INamedTag<Item> EUDIALYTE_ORE = compatTag("ores/eudialyte");
        public static final INamedTag<Item> MANGANESE_ORE = compatTag("ores/manganese");
        public static final INamedTag<Item> SHAKA_ORE = compatTag("ores/shaka");
        public static final INamedTag<Item> ZIRCON_ORE = compatTag("ores/zircon");
        
        public static final INamedTag<Item> AZURITE_GEM = compatTag("gems/azurite");
        public static final INamedTag<Item> EUDIALYTE_GEM = compatTag("gems/eudialyte");
        public static final INamedTag<Item> MANGANESE_INGOT = compatTag("ingots/manganese");
        public static final INamedTag<Item> SHAKA_INGOT = compatTag("ingots/shaka");
        public static final INamedTag<Item> ZIRCON_GEM = compatTag("gems/zircon");
        public static final INamedTag<Item> ZIRCONIUM_GEM = compatTag("gems/zirconium");
        
        public static final INamedTag<Item> SWORDS = compatTag("swords");

        public static final INamedTag<Item> SAND = modTag("sand");
        public static final INamedTag<Item> MUD = modTag("mud");

        public static final INamedTag<Item> SAPLINGS = modTag("saplings");
        public static final INamedTag<Item> LEAVES = modTag("leaves");
        
        public static final INamedTag<Item> SMALL_FLOWERS = modTag("small_flowers");
        
        public static final INamedTag<Item> LOGS = modTag("logs");
        public static final INamedTag<Item> PLANKS = modTag("planks");
        
        public static final INamedTag<Item> WOODEN_SLABS = modTag("wooden_slabs");
        public static final INamedTag<Item> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final INamedTag<Item> WOODEN_DOORS = modTag("wooden_doors");
        public static final INamedTag<Item> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final INamedTag<Item> WOODEN_FENCES = modTag("wooden_fences");
        
        public static final INamedTag<Item> SLABS = modTag("slabs");
        public static final INamedTag<Item> STAIRS = modTag("stairs");
        public static final INamedTag<Item> DOORS = modTag("doors");
        public static final INamedTag<Item> TRAPDOORS = modTag("trapdoors");
        public static final INamedTag<Item> FENCES = modTag("fences");
        public static final INamedTag<Item> WALLS = modTag("walls");

        public static final INamedTag<Item> LEATHER = modTag("leather");
        
        public static final INamedTag<Item> SHELLS = modTag("shells");

        public static final INamedTag<Item> ASHEN_MASKS = modTag("ashen_masks");

        static INamedTag<Item> tag(String modid, String name) {
            return tag(ItemTags::makeWrapperTag, modid, name);
        }

        static INamedTag<Item> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static INamedTag<Item> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    static <T extends INamedTag<?>> T tag(Function<String, T> creator, String modid, String name) {
        return creator.apply(new ResourceLocation(modid, name).toString());
    }

    static <T extends INamedTag<?>> T modTag(Function<String, T> creator, String name) {
        return tag(creator, Constants.MODID, name);
    }

    static <T extends INamedTag<?>> T compatTag(Function<String, T> creator, String name) {
        return tag(creator, "forge", name);
    }
}
