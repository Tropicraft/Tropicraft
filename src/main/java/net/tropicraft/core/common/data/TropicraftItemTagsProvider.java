package net.tropicraft.core.common.data;

import net.minecraft.block.FlowerBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Arrays;
import java.util.function.Supplier;

import static net.tropicraft.core.common.TropicraftTags.Items.ASHEN_MASKS;
import static net.tropicraft.core.common.TropicraftTags.Items.AZURITE_GEM;
import static net.tropicraft.core.common.TropicraftTags.Items.AZURITE_ORE;
import static net.tropicraft.core.common.TropicraftTags.Items.DOORS;
import static net.tropicraft.core.common.TropicraftTags.Items.EUDIALYTE_GEM;
import static net.tropicraft.core.common.TropicraftTags.Items.EUDIALYTE_ORE;
import static net.tropicraft.core.common.TropicraftTags.Items.FENCES;
import static net.tropicraft.core.common.TropicraftTags.Items.LEATHER;
import static net.tropicraft.core.common.TropicraftTags.Items.LEAVES;
import static net.tropicraft.core.common.TropicraftTags.Items.LOGS;
import static net.tropicraft.core.common.TropicraftTags.Items.MANGANESE_INGOT;
import static net.tropicraft.core.common.TropicraftTags.Items.MANGANESE_ORE;
import static net.tropicraft.core.common.TropicraftTags.Items.PLANKS;
import static net.tropicraft.core.common.TropicraftTags.Items.SAND;
import static net.tropicraft.core.common.TropicraftTags.Items.SAPLINGS;
import static net.tropicraft.core.common.TropicraftTags.Items.SHAKA_INGOT;
import static net.tropicraft.core.common.TropicraftTags.Items.SHAKA_ORE;
import static net.tropicraft.core.common.TropicraftTags.Items.SHELLS;
import static net.tropicraft.core.common.TropicraftTags.Items.SLABS;
import static net.tropicraft.core.common.TropicraftTags.Items.SMALL_FLOWERS;
import static net.tropicraft.core.common.TropicraftTags.Items.STAIRS;
import static net.tropicraft.core.common.TropicraftTags.Items.SWORDS;
import static net.tropicraft.core.common.TropicraftTags.Items.TRAPDOORS;
import static net.tropicraft.core.common.TropicraftTags.Items.WALLS;
import static net.tropicraft.core.common.TropicraftTags.Items.WOODEN_DOORS;
import static net.tropicraft.core.common.TropicraftTags.Items.WOODEN_FENCES;
import static net.tropicraft.core.common.TropicraftTags.Items.WOODEN_SLABS;
import static net.tropicraft.core.common.TropicraftTags.Items.WOODEN_STAIRS;
import static net.tropicraft.core.common.TropicraftTags.Items.WOODEN_TRAPDOORS;
import static net.tropicraft.core.common.TropicraftTags.Items.ZIRCONIUM_GEM;
import static net.tropicraft.core.common.TropicraftTags.Items.ZIRCON_GEM;
import static net.tropicraft.core.common.TropicraftTags.Items.ZIRCON_ORE;

public class TropicraftItemTagsProvider extends ItemTagsProvider {

    public TropicraftItemTagsProvider(DataGenerator p_i49827_1_) {
        super(p_i49827_1_);
    }

    @Override
    protected void registerTags() {
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
        addItemsToTag(SWORDS, Items.WOODEN_SWORD.delegate, Items.STONE_SWORD.delegate, Items.IRON_SWORD.delegate, Items.GOLDEN_SWORD.delegate, Items.DIAMOND_SWORD.delegate,
                TropicraftItems.EUDIALYTE_SWORD, TropicraftItems.ZIRCON_SWORD, TropicraftItems.ZIRCONIUM_SWORD);

        for (RegistryObject<AshenMaskItem> item : TropicraftItems.ASHEN_MASKS.values()) {
            addItemsToTag(ASHEN_MASKS, item);
        }

        for (RegistryObject<FlowerBlock> flower : TropicraftBlocks.FLOWERS.values()) {
            addItemsToTag(ItemTags.FLOWERS, flower);
        }
        
        // Copy block tags
        copy(TropicraftTags.Blocks.SAND, SAND);

        copy(TropicraftTags.Blocks.SAPLINGS, SAPLINGS);
        copy(TropicraftTags.Blocks.LEAVES, LEAVES);
        
        copy(TropicraftTags.Blocks.SMALL_FLOWERS, SMALL_FLOWERS);

        copy(TropicraftTags.Blocks.LOGS, LOGS);
        copy(TropicraftTags.Blocks.PLANKS, PLANKS);
        
        copy(TropicraftTags.Blocks.WOODEN_SLABS, WOODEN_SLABS);
        copy(TropicraftTags.Blocks.WOODEN_STAIRS, WOODEN_STAIRS);
        copy(TropicraftTags.Blocks.WOODEN_DOORS, WOODEN_DOORS);
        copy(TropicraftTags.Blocks.WOODEN_TRAPDOORS, WOODEN_TRAPDOORS);
        copy(TropicraftTags.Blocks.WOODEN_FENCES, WOODEN_FENCES);
        
        copy(TropicraftTags.Blocks.SLABS, SLABS);
        copy(TropicraftTags.Blocks.STAIRS, STAIRS);
        copy(TropicraftTags.Blocks.DOORS, DOORS);
        copy(TropicraftTags.Blocks.TRAPDOORS, TRAPDOORS);
        copy(TropicraftTags.Blocks.FENCES, FENCES);
        copy(TropicraftTags.Blocks.WALLS, WALLS);
    }

    @SafeVarargs
    private final void addItemsToTag(Tag<Item> tag, Supplier<? extends IItemProvider>... items) {
        getBuilder(tag).add(Arrays.stream(items).map(Supplier::get).map(IItemProvider::asItem).toArray(Item[]::new));
    }
    
    @SafeVarargs
    private final void appendToTag(Tag<Item> tag, Tag<Item>... toAppend) {
        getBuilder(tag).add(toAppend);
    }

    @Override
    public String getName() {
        return "Tropicraft Item Tags";
    }
}
