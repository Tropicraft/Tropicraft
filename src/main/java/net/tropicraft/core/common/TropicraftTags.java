package net.tropicraft.core.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.timeline.Timeline;
import net.tropicraft.Tropicraft;

public interface TropicraftTags {

    interface Blocks extends TropicraftTags {

        TagKey<Block> MUD = modTag("mud");

        TagKey<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        TagKey<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        TagKey<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");
        TagKey<Block> CARVER_REPLACEABLES = modTag("carver_replaceables");

        TagKey<Block> ROOTS = modTag("roots");

        TagKey<Block> BONGOS = modTag("bongos");
        TagKey<Block> BRANCHES = modTag("branches");
        TagKey<Block> BIRDS_LIKE_TO_STAND_ON = modTag("birds_like_to_stand_on");

        static TagKey<Block> tag(String modid, String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Block> modTag(String name) {
            return tag(Tropicraft.ID, name);
        }

        static TagKey<Block> compatTag(String name) {
            return tag("c", name);
        }
    }

    interface Items extends TropicraftTags {

        TagKey<Item> AZURITE_ORE = compatTag("ores/azurite");
        TagKey<Item> EUDIALYTE_ORE = compatTag("ores/eudialyte");
        TagKey<Item> MANGANESE_ORE = compatTag("ores/manganese");
        TagKey<Item> SHAKA_ORE = compatTag("ores/shaka");
        TagKey<Item> ZIRCON_ORE = compatTag("ores/zircon");

        TagKey<Item> AZURITE_GEM = compatTag("gems/azurite");
        TagKey<Item> EUDIALYTE_GEM = compatTag("gems/eudialyte");
        TagKey<Item> MANGANESE_INGOT = compatTag("ingots/manganese");
        TagKey<Item> SHAKA_INGOT = compatTag("ingots/shaka");
        TagKey<Item> ZIRCON_GEM = compatTag("gems/zircon");
        TagKey<Item> ZIRCONIUM_GEM = compatTag("gems/zirconium");

        TagKey<Item> MANGROVE_LOGS = modTag("mangrove_logs");

        TagKey<Item> SHELLS = modTag("shells");

        TagKey<Item> ASHEN_MASKS = modTag("ashen_masks");

        TagKey<Item> FRUITS = modTag("fruits");
        TagKey<Item> MEATS = modTag("meats");
        TagKey<Item> LIME = modTag("lime");
        TagKey<Item> PLANTAIN = modTag("plantain");

        TagKey<Item> REPAIRS_FIRE_ARMOR = modTag("repairs_fire_armor");
        TagKey<Item> REPAIRS_SCUBA_GEAR = modTag("repairs_scuba_gear");
        TagKey<Item> REPAIRS_SCALE_ARMOR = modTag("repairs_scale_armor");
        TagKey<Item> REPAIRS_NIGEL_STACHE = modTag("repairs_nigel_stache");

        TagKey<Item> BAMBOO_TOOL_MATERIALS = modTag("bamboo_tool_materials");

        static TagKey<Item> tag(String modid, String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Item> modTag(String name) {
            return tag(Tropicraft.ID, name);
        }

        static TagKey<Item> compatTag(String name) {
            return tag("c", name);
        }
    }

    interface Biomes extends TropicraftTags {
        TagKey<Biome> HAS_HOME_TREE = modTag("has_structure/home_tree");
        TagKey<Biome> HAS_KOA_VILLAGE = modTag("has_structure/koa_village");
        TagKey<Biome> HAS_LAND_VOLCANO = modTag("has_structure/land_volcano");
        TagKey<Biome> HAS_OCEAN_VOLCANO = modTag("has_structure/ocean_volcano");

        static TagKey<Biome> tag(String modid, String name) {
            return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Biome> modTag(String name) {
            return tag(Tropicraft.ID, name);
        }
    }

    interface Entities extends TropicraftTags {
        TagKey<EntityType<?>> CAN_STAND_ON_BRANCH = modTag("can_stand_on_branch");

        static TagKey<EntityType<?>> tag(String modid, String name) {
            return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(modid, name));
        }

        static TagKey<EntityType<?>> modTag(String name) {
            return tag(Tropicraft.ID, name);
        }
    }

    interface Timelines extends TropicraftTags {
        TagKey<Timeline> IN_TROPICS = modTag("in_tropics");

        static TagKey<Timeline> tag(String modid, String name) {
            return TagKey.create(Registries.TIMELINE, Identifier.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Timeline> modTag(String name) {
            return tag(Tropicraft.ID, name);
        }
    }
}
