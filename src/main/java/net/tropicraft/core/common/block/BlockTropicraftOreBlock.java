package net.tropicraft.core.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.tropicraft.core.common.enums.TropicraftOres;

// TODO pointless class
public class BlockTropicraftOreBlock extends BlockTropicraftEnumVariants<TropicraftOres> {

	public BlockTropicraftOreBlock() {
		super(Material.ROCK, TropicraftOres.class, TropicraftOres.ORES_WITH_BLOCKS);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setDefaultState(this.getDefaultState().withProperty(getProperty(), TropicraftOres.ZIRCON));
	}
}
