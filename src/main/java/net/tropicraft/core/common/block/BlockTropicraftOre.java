package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockTropicraftOre extends Block {
    public BlockTropicraftOre(final Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(final IBlockState blockState) {
        return 2;
    }
}
