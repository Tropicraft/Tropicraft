package net.tropicraft.core.common;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.tropicraft.Constants;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final TagKey<Block> SAND = modTag("sand");
        public static final TagKey<Block> MUD = modTag("mud");

        public static final TagKey<Block> SAPLINGS = modTag("saplings");
        public static final TagKey<Block> LEAVES = modTag("leaves");

        public static final TagKey<Block> SMALL_FLOWERS = modTag("small_flowers");
        public static final TagKey<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        public static final TagKey<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        public static final TagKey<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");

        public static final TagKey<Block> LOGS = modTag("logs");
        public static final TagKey<Block> PLANKS = modTag("planks");

        public static final TagKey<Block> ROOTS = modTag("roots");

        public static final TagKey<Block> WOODEN_SLABS = modTag("wooden_slabs");
        public static final TagKey<Block> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final TagKey<Block> WOODEN_DOORS = modTag("wooden_doors");
        public static final TagKey<Block> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final TagKey<Block> WOODEN_FENCES = modTag("wooden_fences");

        public static final TagKey<Block> SLABS = modTag("slabs");
        public static final TagKey<Block> STAIRS = modTag("stairs");
        public static final TagKey<Block> DOORS = modTag("doors");
        public static final TagKey<Block> TRAPDOORS = modTag("trapdoors");
        public static final TagKey<Block> FENCES = modTag("fences");
        public static final TagKey<Block> WALLS = modTag("walls");

        public static final TagKey<Block> FLOWER_POTS = modTag("flower_pots");
        public static final TagKey<Block> BONGOS = modTag("bongos");

        public static final TagKey<Block> CLIMBABLE = modTag("climbable");

        static TagKey<Block> tag(String modid, String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(modid, name));
        }

        static TagKey<Block> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static TagKey<Block> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    public static class Items extends TropicraftTags {

        public static final TagKey<Item> AZURITE_ORE = compatTag("ores/azurite");
        public static final TagKey<Item> EUDIALYTE_ORE = compatTag("ores/eudialyte");
        public static final TagKey<Item> MANGANESE_ORE = compatTag("ores/manganese");
        public static final TagKey<Item> SHAKA_ORE = compatTag("ores/shaka");
        public static final TagKey<Item> ZIRCON_ORE = compatTag("ores/zircon");
        
        public static final TagKey<Item> AZURITE_GEM = compatTag("gems/azurite");
        public static final TagKey<Item> EUDIALYTE_GEM = compatTag("gems/eudialyte");
        public static final TagKey<Item> MANGANESE_INGOT = compatTag("ingots/manganese");
        public static final TagKey<Item> SHAKA_INGOT = compatTag("ingots/shaka");
        public static final TagKey<Item> ZIRCON_GEM = compatTag("gems/zircon");
        public static final TagKey<Item> ZIRCONIUM_GEM = compatTag("gems/zirconium");
        
        public static final TagKey<Item> SWORDS = compatTag("swords");

        public static final TagKey<Item> SAND = modTag("sand");
        public static final TagKey<Item> MUD = modTag("mud");

        public static final TagKey<Item> SAPLINGS = modTag("saplings");
        public static final TagKey<Item> LEAVES = modTag("leaves");
        
        public static final TagKey<Item> SMALL_FLOWERS = modTag("small_flowers");
        
        public static final TagKey<Item> LOGS = modTag("logs");
        public static final TagKey<Item> PLANKS = modTag("planks");
        
        public static final TagKey<Item> WOODEN_SLABS = modTag("wooden_slabs");
        public static final TagKey<Item> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final TagKey<Item> WOODEN_DOORS = modTag("wooden_doors");
        public static final TagKey<Item> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final TagKey<Item> WOODEN_FENCES = modTag("wooden_fences");

        public static final TagKey<Item> MANGROVE_LOGS = modTag("mangrove_logs");
        
        public static final TagKey<Item> SLABS = modTag("slabs");
        public static final TagKey<Item> STAIRS = modTag("stairs");
        public static final TagKey<Item> DOORS = modTag("doors");
        public static final TagKey<Item> TRAPDOORS = modTag("trapdoors");
        public static final TagKey<Item> FENCES = modTag("fences");
        public static final TagKey<Item> WALLS = modTag("walls");

        public static final TagKey<Item> LEATHER = modTag("leather");
        
        public static final TagKey<Item> SHELLS = modTag("shells");

        public static final TagKey<Item> ASHEN_MASKS = modTag("ashen_masks");

        public static final TagKey<Item> FRUITS = modTag("fruits");
        public static final TagKey<Item> MEATS = modTag("meats");
        public static final TagKey<Item> MUSIC_DISCS = modTag("music_discs");

        static TagKey<Item> tag(String modid, String name) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(modid, name));
        }

        static TagKey<Item> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static TagKey<Item> compatTag(String name) {
            return tag("forge", name);
        }
    }
}
