package net.tropicraft.core.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;

import java.util.function.Function;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final Named<Block> SAND = modTag("sand");
        public static final Named<Block> MUD = modTag("mud");

        public static final Named<Block> SAPLINGS = modTag("saplings");
        public static final Named<Block> LEAVES = modTag("leaves");

        public static final Named<Block> SMALL_FLOWERS = modTag("small_flowers");
        public static final Named<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        public static final Named<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        public static final Named<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");

        public static final Named<Block> LOGS = modTag("logs");
        public static final Named<Block> PLANKS = modTag("planks");

        public static final Named<Block> ROOTS = modTag("roots");

        public static final Named<Block> WOODEN_SLABS = modTag("wooden_slabs");
        public static final Named<Block> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final Named<Block> WOODEN_DOORS = modTag("wooden_doors");
        public static final Named<Block> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final Named<Block> WOODEN_FENCES = modTag("wooden_fences");

        public static final Named<Block> SLABS = modTag("slabs");
        public static final Named<Block> STAIRS = modTag("stairs");
        public static final Named<Block> DOORS = modTag("doors");
        public static final Named<Block> TRAPDOORS = modTag("trapdoors");
        public static final Named<Block> FENCES = modTag("fences");
        public static final Named<Block> WALLS = modTag("walls");

        public static final Named<Block> FLOWER_POTS = modTag("flower_pots");
        public static final Named<Block> BONGOS = modTag("bongos");

        public static final Named<Block> CLIMBABLE = modTag("climbable");

        static Named<Block> tag(String modid, String name) {
            return tag(BlockTags::bind, modid, name);
        }

        static Named<Block> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static Named<Block> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    public static class Items extends TropicraftTags {

        public static final Named<Item> AZURITE_ORE = compatTag("ores/azurite");
        public static final Named<Item> EUDIALYTE_ORE = compatTag("ores/eudialyte");
        public static final Named<Item> MANGANESE_ORE = compatTag("ores/manganese");
        public static final Named<Item> SHAKA_ORE = compatTag("ores/shaka");
        public static final Named<Item> ZIRCON_ORE = compatTag("ores/zircon");
        
        public static final Named<Item> AZURITE_GEM = compatTag("gems/azurite");
        public static final Named<Item> EUDIALYTE_GEM = compatTag("gems/eudialyte");
        public static final Named<Item> MANGANESE_INGOT = compatTag("ingots/manganese");
        public static final Named<Item> SHAKA_INGOT = compatTag("ingots/shaka");
        public static final Named<Item> ZIRCON_GEM = compatTag("gems/zircon");
        public static final Named<Item> ZIRCONIUM_GEM = compatTag("gems/zirconium");
        
        public static final Named<Item> SWORDS = compatTag("swords");

        public static final Named<Item> SAND = modTag("sand");
        public static final Named<Item> MUD = modTag("mud");

        public static final Named<Item> SAPLINGS = modTag("saplings");
        public static final Named<Item> LEAVES = modTag("leaves");
        
        public static final Named<Item> SMALL_FLOWERS = modTag("small_flowers");
        
        public static final Named<Item> LOGS = modTag("logs");
        public static final Named<Item> PLANKS = modTag("planks");
        
        public static final Named<Item> WOODEN_SLABS = modTag("wooden_slabs");
        public static final Named<Item> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final Named<Item> WOODEN_DOORS = modTag("wooden_doors");
        public static final Named<Item> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final Named<Item> WOODEN_FENCES = modTag("wooden_fences");
        
        public static final Named<Item> SLABS = modTag("slabs");
        public static final Named<Item> STAIRS = modTag("stairs");
        public static final Named<Item> DOORS = modTag("doors");
        public static final Named<Item> TRAPDOORS = modTag("trapdoors");
        public static final Named<Item> FENCES = modTag("fences");
        public static final Named<Item> WALLS = modTag("walls");

        public static final Named<Item> LEATHER = modTag("leather");
        
        public static final Named<Item> SHELLS = modTag("shells");

        public static final Named<Item> ASHEN_MASKS = modTag("ashen_masks");

        public static final Named<Item> FRUITS = modTag("fruits");
        public static final Named<Item> MEATS = modTag("meats");
        public static final Named<Item> MUSIC_DISCS = modTag("music_discs");

        static Named<Item> tag(String modid, String name) {
            return tag(ItemTags::bind, modid, name);
        }

        static Named<Item> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static Named<Item> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    static <T extends Named<?>> T tag(Function<String, T> creator, String modid, String name) {
        return creator.apply(new ResourceLocation(modid, name).toString());
    }

    static <T extends Named<?>> T modTag(Function<String, T> creator, String name) {
        return tag(creator, Constants.MODID, name);
    }

    static <T extends Named<?>> T compatTag(Function<String, T> creator, String name) {
        return tag(creator, "forge", name);
    }
}
