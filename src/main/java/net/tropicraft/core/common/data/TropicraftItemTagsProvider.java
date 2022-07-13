package net.tropicraft.core.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicalMusicDiscItem;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Arrays;
import java.util.function.Supplier;

import static net.tropicraft.core.common.TropicraftTags.Items.*;

public class TropicraftItemTagsProvider extends ItemTagsProvider {

    public TropicraftItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTags, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Add forge tags for our ores
        addItemsToTag(AZURITE_ORE, TropicraftBlocks.AZURITE_ORE);
        addItemsToTag(EUDIALYTE_ORE, TropicraftBlocks.EUDIALYTE_ORE);
        addItemsToTag(ZIRCON_ORE, TropicraftBlocks.ZIRCON_ORE);
        addItemsToTag(MANGANESE_ORE, TropicraftBlocks.MANGANESE_ORE);
        addItemsToTag(SHAKA_ORE, TropicraftBlocks.SHAKA_ORE);
        appendToTag(Tags.Items.ORES, AZURITE_ORE, EUDIALYTE_ORE, ZIRCON_ORE, MANGANESE_ORE, SHAKA_ORE);
        
        // Add forge tags for our gems/ingots
        addItemsToTag(AZURITE_GEM, TropicraftItems.AZURITE);
        addItemsToTag(EUDIALYTE_GEM, TropicraftItems.EUDIALYTE);
        addItemsToTag(ZIRCON_GEM, TropicraftItems.ZIRCON);
        addItemsToTag(MANGANESE_INGOT, TropicraftItems.MANGANESE);
        addItemsToTag(SHAKA_INGOT, TropicraftItems.SHAKA);
        addItemsToTag(ZIRCONIUM_GEM, TropicraftItems.ZIRCONIUM);
        appendToTag(Tags.Items.GEMS, AZURITE_GEM, EUDIALYTE_GEM, ZIRCON_GEM, ZIRCONIUM_GEM);
        appendToTag(Tags.Items.INGOTS, MANGANESE_INGOT, SHAKA_INGOT);

        addItemsToTag(LEATHER, TropicraftItems.IGUANA_LEATHER);
        appendToTag(Tags.Items.LEATHER, LEATHER);

        // Add bamboo sticks to forge ore tag
        addItemsToTag(Tags.Items.RODS_WOODEN, TropicraftItems.BAMBOO_STICK);
        
        // Add our fish items to stats and dolphin food
        addItemsToTag(ItemTags.FISHES, TropicraftItems.RAW_FISH, TropicraftItems.COOKED_FISH);
        
        // Shells for sifter drops
        addItemsToTag(SHELLS, TropicraftItems.SOLONOX_SHELL, TropicraftItems.FROX_CONCH, TropicraftItems.PAB_SHELL,
                TropicraftItems.RUBE_NAUTILUS, TropicraftItems.STARFISH, TropicraftItems.TURTLE_SHELL);
        
        // Swords for chunk drops
        addItemsToTag(SWORDS, Items.WOODEN_SWORD.delegate, Items.STONE_SWORD.delegate, Items.IRON_SWORD.delegate, Items.GOLDEN_SWORD.delegate, Items.DIAMOND_SWORD.delegate, Items.NETHERITE_SWORD.delegate,
                TropicraftItems.EUDIALYTE_SWORD, TropicraftItems.ZIRCON_SWORD, TropicraftItems.ZIRCONIUM_SWORD);

        for (RegistryObject<AshenMaskItem> item : TropicraftItems.ASHEN_MASKS.values()) {
            addItemsToTag(ASHEN_MASKS, item);
        }

        for (RegistryObject<FlowerBlock> flower : TropicraftBlocks.FLOWERS.values()) {
            addItemsToTag(ItemTags.FLOWERS, flower);
        }

        addItemsToTag(FRUITS, Items.APPLE);
        addItemsToTag(FRUITS, TropicraftItems.GRAPEFRUIT, TropicraftItems.LEMON, TropicraftItems.LIME, TropicraftItems.ORANGE);

        addItemsToTag(MEATS, Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT, Items.MUTTON);

        for (RegistryObject<TropicalMusicDiscItem> item : TropicraftItems.MUSIC_DISCS.values()) {
            addItemsToTag(MUSIC_DISCS, item);
        }
        appendToTag(ItemTags.MUSIC_DISCS, MUSIC_DISCS);

        // Copy block tags
        copy(TropicraftTags.Blocks.SAND, SAND);
        copy(TropicraftTags.Blocks.MUD, MUD);

        copyBlockAndAppendToTag(TropicraftTags.Blocks.SAPLINGS, SAPLINGS, ItemTags.SAPLINGS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.LEAVES, LEAVES, ItemTags.LEAVES);

        copyBlockAndAppendToTag(TropicraftTags.Blocks.SMALL_FLOWERS, SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);

        copyBlockAndAppendToTag(TropicraftTags.Blocks.LOGS, LOGS, ItemTags.LOGS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.PLANKS, PLANKS, ItemTags.PLANKS);

        copyBlockAndAppendToTag(TropicraftTags.Blocks.WOODEN_SLABS, WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.WOODEN_STAIRS, WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.WOODEN_DOORS, WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.WOODEN_TRAPDOORS, WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.WOODEN_FENCES, WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        
        copyBlockAndAppendToTag(TropicraftTags.Blocks.SLABS, SLABS, ItemTags.SLABS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.STAIRS, STAIRS, ItemTags.STAIRS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.DOORS, DOORS, ItemTags.DOORS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.TRAPDOORS, TRAPDOORS, ItemTags.TRAPDOORS);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.FENCES, FENCES, ItemTags.FENCES);
        copyBlockAndAppendToTag(TropicraftTags.Blocks.WALLS, WALLS, ItemTags.WALLS);
    }

    @SafeVarargs
    private final void addItemsToTag(TagKey<Item> tag, Supplier<? extends ItemLike>... items) {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(ItemLike::asItem).toArray(Item[]::new));
    }

    private void addItemsToTag(TagKey<Item> tag, ItemLike... items) {
        tag(tag).add(Arrays.stream(items).map(ItemLike::asItem).toArray(Item[]::new));
    }
    
    @SafeVarargs
    private final void appendToTag(TagKey<Item> tag, TagKey<Item>... toAppend) {
        tag(tag).addTags(toAppend);
    }

    private void copyBlockAndAppendToTag(TagKey<Block> blockTag, TagKey<Item> itemTag, TagKey<Item> toAddTo) {
        copy(blockTag, itemTag);
        appendToTag(toAddTo, itemTag);
    }

    @Override
    public String getName() {
        return "Tropicraft Item Tags";
    }
}
