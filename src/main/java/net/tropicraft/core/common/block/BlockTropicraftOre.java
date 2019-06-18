package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class BlockTropicraftOre extends Block {
    public BlockTropicraftOre(final Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(final BlockState blockState) {
        return 2;
    }
}
