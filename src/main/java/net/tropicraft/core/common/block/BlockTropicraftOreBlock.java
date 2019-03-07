package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockTropicraftOreBlock extends Block {
    public BlockTropicraftOreBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(final IBlockState blockState) {
        return 2;
    }
}
