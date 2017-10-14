package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;
import net.tropicraft.core.registry.BlockRegistry;


public class BiomeGenTropicsRiver extends BiomeGenTropicraft {

	public BiomeGenTropicsRiver(BiomeProperties props) {
		super(props);
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}

}
