package net.tropicraft.registry;

import net.minecraft.block.material.Material;
import net.tropicraft.block.BlockTropicraftMulti;
import net.tropicraft.info.TCNames;

public class BlockTropicraftPlank extends BlockTropicraftMulti {

	public BlockTropicraftPlank(String[] names) {
		super(names, Material.wood);
		this.setBlockTextureName(TCNames.plank);
		this.disableStats();
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
	}

}
