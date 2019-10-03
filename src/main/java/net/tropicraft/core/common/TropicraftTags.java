package net.tropicraft.core.common;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final Tag<Block> FENCES = modTag("fences");
        public static final Tag<Block> LEAVES = modTag("leaves");

        static Tag<Block> tag(String modid, String name) {
            return tag(BlockTags.Wrapper::new, modid, name);
        }

        static Tag<Block> modTag(String name) {
            return tag(Info.MODID, name);
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
        
        static Tag<Item> tag(String modid, String name) {
            return tag(ItemTags.Wrapper::new, modid, name);
        }

        static Tag<Item> modTag(String name) {
            return tag(Info.MODID, name);
        }

        static Tag<Item> compatTag(String name) {
            return tag("forge", name);
        }
    }
    
    static <T extends Tag<?>> T tag(Function<ResourceLocation, T> creator, String modid, String name) {
        return creator.apply(new ResourceLocation(modid, name));
    }

    static <T extends Tag<?>> T modTag(Function<ResourceLocation, T> creator, String name) {
        return tag(creator, Info.MODID, name);
    }

    static <T extends Tag<?>> T compatTag(Function<ResourceLocation, T> creator, String name) {
        return tag(creator, "forge", name);
    }
}
