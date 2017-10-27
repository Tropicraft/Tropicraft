package net.tropicraft.core.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.tropicraft.core.common.enums.TropicraftPlanks;

// TODO pointless class
public class BlockTropicraftPlank extends BlockTropicraftEnumVariants<TropicraftPlanks> {

	public BlockTropicraftPlank(Material mat) {
		super(mat, TropicraftPlanks.class);
		this.disableStats();
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
	}
}
