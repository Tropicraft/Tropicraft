package net.tropicraft.core.common.data;

import static net.tropicraft.core.common.block.TropicraftBlocks.BAMBOO_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.FRUIT_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.GRAPEFRUIT_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.KAPOK_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.LEMON_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.LIME_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.MAHOGANY_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.ORANGE_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_FENCE;
import static net.tropicraft.core.common.block.TropicraftBlocks.PALM_LEAVES;
import static net.tropicraft.core.common.block.TropicraftBlocks.THATCH_FENCE;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.tropicraft.core.common.TropicraftTags;

public class TropicraftBlockTagsProvider extends BlockTagsProvider {

    public TropicraftBlockTagsProvider(DataGenerator p_i49827_1_) {
        super(p_i49827_1_);
    }

    @Override
    protected void registerTags() {
        addBlocksToTag(TropicraftTags.Blocks.FENCES, BAMBOO_FENCE, CHUNK_FENCE, MAHOGANY_FENCE, PALM_FENCE, THATCH_FENCE);
        addBlocksToTag(TropicraftTags.Blocks.LEAVES, MAHOGANY_LEAVES, PALM_LEAVES, KAPOK_LEAVES, FRUIT_LEAVES, GRAPEFRUIT_LEAVES, LEMON_LEAVES, LIME_LEAVES, ORANGE_LEAVES);
        
        appendToTag(BlockTags.FENCES, TropicraftTags.Blocks.FENCES);
        appendToTag(BlockTags.LEAVES, TropicraftTags.Blocks.LEAVES);
    }

    @SafeVarargs
    private final void addBlocksToTag(Tag<Block> tag, Supplier<? extends Block>... blocks) {
        getBuilder(tag).add(Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new));
    }
    
    @SafeVarargs
    private final void appendToTag(Tag<Block> tag, Tag<Block>... toAppend) {
        getBuilder(tag).add(toAppend);
    }

    @Override
    public String getName() {
        return "Tropicraft Block Tags";
    }
}
