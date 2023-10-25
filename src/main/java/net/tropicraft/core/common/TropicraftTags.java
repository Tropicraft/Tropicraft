package net.tropicraft.core.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.tropicraft.Constants;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final TagKey<Block> MUD = modTag("mud");

        public static final TagKey<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        public static final TagKey<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        public static final TagKey<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");
        public static final TagKey<Block> CARVER_REPLACEABLES = modTag("carver_replaceables");

        public static final TagKey<Block> ROOTS = modTag("roots");

        public static final TagKey<Block> BONGOS = modTag("bongos");

        static TagKey<Block> tag(String modid, String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
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
        
        public static final TagKey<Item> MANGROVE_LOGS = modTag("mangrove_logs");
        
        public static final TagKey<Item> SHELLS = modTag("shells");

        public static final TagKey<Item> ASHEN_MASKS = modTag("ashen_masks");

        public static final TagKey<Item> FRUITS = modTag("fruits");
        public static final TagKey<Item> MEATS = modTag("meats");
        public static final TagKey<Item> LIME = modTag("lime");
        public static final TagKey<Item> PLANTAIN = modTag("plantain");

        public static final TagKey<Item> REPAIRS_FIRE_ARMOR = modTag("repairs_fire_armor");
        public static final TagKey<Item> REPAIRS_SCUBA_GEAR = modTag("repairs_scuba_gear");

        static TagKey<Item> tag(String modid, String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Item> modTag(String name) {
            return tag(Constants.MODID, name);
        }

        static TagKey<Item> compatTag(String name) {
            return tag("forge", name);
        }
    }

    public static class Biomes extends TropicraftTags {
        public static final TagKey<Biome> HAS_HOME_TREE = modTag("has_structure/home_tree");
        public static final TagKey<Biome> HAS_KOA_VILLAGE = modTag("has_structure/koa_village");
        public static final TagKey<Biome> HAS_LAND_VOLCANO = modTag("has_structure/land_volcano");
        public static final TagKey<Biome> HAS_OCEAN_VOLCANO = modTag("has_structure/ocean_volcano");

        static TagKey<Biome> tag(String modid, String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Biome> modTag(String name) {
            return tag(Constants.MODID, name);
        }
    }
}
