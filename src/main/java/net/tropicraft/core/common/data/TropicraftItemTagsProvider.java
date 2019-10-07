package net.tropicraft.core.common.data;

import static net.tropicraft.core.common.TropicraftTags.Items.*;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropicraftItemTagsProvider extends ItemTagsProvider {

    public TropicraftItemTagsProvider(DataGenerator p_i49827_1_) {
        super(p_i49827_1_);
    }

    @Override
    protected void registerTags() {
        // Add forge tags for our ores
        addItemsToTag(AZURITE_ORE, TropicraftItems.AZURITE);
        addItemsToTag(EUDIALYTE_ORE, TropicraftItems.EUDIALYTE);
        addItemsToTag(ZIRCON_ORE, TropicraftItems.ZIRCON);
        addItemsToTag(MANGANESE_ORE, TropicraftItems.MANGANESE);
        addItemsToTag(SHAKA_ORE, TropicraftItems.SHAKA);
        appendToTag(Tags.Items.ORES, AZURITE_ORE, EUDIALYTE_ORE, ZIRCON_ORE, MANGANESE_ORE, SHAKA_ORE);
        
        // Add bamboo sticks to forge ore tag
        addItemsToTag(Tags.Items.RODS_WOODEN, TropicraftItems.BAMBOO_STICK);
        
        // Add our fish items to stats and dolphin food
        addItemsToTag(ItemTags.FISHES, TropicraftItems.RAW_FISH, TropicraftItems.COOKED_FISH);
        
        // Shells for sifter drops
        addItemsToTag(SHELLS, TropicraftItems.SOLONOX_SHELL, TropicraftItems.FROX_CONCH, TropicraftItems.PAB_SHELL,
                TropicraftItems.RUBE_NAUTILUS, TropicraftItems.STARFISH, TropicraftItems.TURTLE_SHELL);
        
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
