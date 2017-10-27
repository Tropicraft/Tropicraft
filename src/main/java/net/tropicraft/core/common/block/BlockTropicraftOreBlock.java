package net.tropicraft.core.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;

public class BlockTropicraftOreBlock extends BlockTropicraftEnumVariants<TropicraftOreBlocks> {

	public BlockTropicraftOreBlock() {
		super(Material.ROCK, TropicraftOreBlocks.class);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setDefaultState(this.getDefaultState().withProperty(getProperty(), TropicraftOreBlocks.ZIRCON));
	}
}
