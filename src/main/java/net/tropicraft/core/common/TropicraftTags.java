package net.tropicraft.core.common;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final Tag<Block> SAND = modTag("sand");

        public static final Tag<Block> SAPLINGS = modTag("saplings");
        public static final Tag<Block> LEAVES = modTag("leaves");
        
        public static final Tag<Block> SMALL_FLOWERS = modTag("small_flowers");
        public static final Tag<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        public static final Tag<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        public static final Tag<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");

        public static final Tag<Block> LOGS = modTag("logs");
        public static final Tag<Block> PLANKS = modTag("planks");
        
        public static final Tag<Block> WOODEN_SLABS = modTag("wooden_slabs");
        public static final Tag<Block> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final Tag<Block> WOODEN_DOORS = modTag("wooden_doors");
        public static final Tag<Block> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final Tag<Block> WOODEN_FENCES = modTag("wooden_fences");
        
        public static final Tag<Block> SLABS = modTag("slabs");
        public static final Tag<Block> STAIRS = modTag("stairs");
        public static final Tag<Block> DOORS = modTag("doors");
        public static final Tag<Block> TRAPDOORS = modTag("trapdoors");
        public static final Tag<Block> FENCES = modTag("fences");
        public static final Tag<Block> WALLS = modTag("walls");

        public static final Tag<Block> FLOWER_POTS = modTag("flower_pots");

        static Tag<Block> tag(String modid, String name) {
            return tag(BlockTags.Wrapper::new, modid, name);
        }

        static Tag<Block> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static Tag<Block> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    public static class Items extends TropicraftTags {

        public static final Tag<Item> AZURITE_ORE = compatTag("ores/azurite");
        public static final Tag<Item> EUDIALYTE_ORE = compatTag("ores/eudialyte");
        public static final Tag<Item> MANGANESE_ORE = compatTag("ores/manganese");
        public static final Tag<Item> SHAKA_ORE = compatTag("ores/shaka");
        public static final Tag<Item> ZIRCON_ORE = compatTag("ores/zircon");
        
        public static final Tag<Item> AZURITE_GEM = compatTag("gems/azurite");
        public static final Tag<Item> EUDIALYTE_GEM = compatTag("gems/eudialyte");
        public static final Tag<Item> MANGANESE_INGOT = compatTag("ingots/manganese");
        public static final Tag<Item> SHAKA_INGOT = compatTag("ingots/shaka");
        public static final Tag<Item> ZIRCON_GEM = compatTag("gems/zircon");
        public static final Tag<Item> ZIRCONIUM_GEM = compatTag("gems/zirconium");
        
        public static final Tag<Item> SWORDS = compatTag("swords");

        public static final Tag<Item> SAND = modTag("sand");

        public static final Tag<Item> SAPLINGS = modTag("saplings");
        public static final Tag<Item> LEAVES = modTag("leaves");
        
        public static final Tag<Item> SMALL_FLOWERS = modTag("small_flowers");
        
        public static final Tag<Item> LOGS = modTag("logs");
        public static final Tag<Item> PLANKS = modTag("planks");
        
        public static final Tag<Item> WOODEN_SLABS = modTag("wooden_slabs");
        public static final Tag<Item> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final Tag<Item> WOODEN_DOORS = modTag("wooden_doors");
        public static final Tag<Item> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final Tag<Item> WOODEN_FENCES = modTag("wooden_fences");
        
        public static final Tag<Item> SLABS = modTag("slabs");
        public static final Tag<Item> STAIRS = modTag("stairs");
        public static final Tag<Item> DOORS = modTag("doors");
        public static final Tag<Item> TRAPDOORS = modTag("trapdoors");
        public static final Tag<Item> FENCES = modTag("fences");
        public static final Tag<Item> WALLS = modTag("walls");
        
        public static final Tag<Item> FISHES = modTag("fishes");

        public static final Tag<Item> SHELLS = modTag("shells");

        static Tag<Item> tag(String modid, String name) {
            return tag(ItemTags.Wrapper::new, modid, name);
        }

        static Tag<Item> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static Tag<Item> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    static <T extends Tag<?>> T tag(Function<ResourceLocation, T> creator, String modid, String name) {
        return creator.apply(new ResourceLocation(modid, name));
    }

    static <T extends Tag<?>> T modTag(Function<ResourceLocation, T> creator, String name) {
        return tag(creator, Constants.MODID, name);
    }

    static <T extends Tag<?>> T compatTag(Function<ResourceLocation, T> creator, String name) {
        return tag(creator, "forge", name);
    }
}
