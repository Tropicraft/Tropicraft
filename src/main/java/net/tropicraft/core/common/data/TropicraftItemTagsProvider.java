package net.tropicraft.core.common.data;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropicraftItemTagsProvider extends ItemTagsProvider {

    public TropicraftItemTagsProvider(DataGenerator p_i49827_1_) {
        super(p_i49827_1_);
    }

    @Override
    protected void registerTags() {
        addItemsToTag(TropicraftTags.Items.AZURITE_ORE, TropicraftItems.AZURITE);
        addItemsToTag(TropicraftTags.Items.EUDIALYTE_ORE, TropicraftItems.EUDIALYTE);
        addItemsToTag(TropicraftTags.Items.ZIRCON_ORE, TropicraftItems.ZIRCON);
        addItemsToTag(TropicraftTags.Items.MANGANESE_ORE, TropicraftItems.MANGANESE);
        addItemsToTag(TropicraftTags.Items.SHAKA_ORE, TropicraftItems.SHAKA);
    }

    @SafeVarargs
    private final void addItemsToTag(Tag<Item> tag, Supplier<? extends Item>... blocks) {
        getBuilder(tag).add(Arrays.stream(blocks).map(Supplier::get).toArray(Item[]::new));
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
