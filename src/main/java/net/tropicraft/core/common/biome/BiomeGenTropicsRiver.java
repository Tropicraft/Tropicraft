package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;


public class BiomeGenTropicsRiver extends BiomeGenTropicraft {

	public BiomeGenTropicsRiver(BiomeProperties props) {
		super(props);
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
	}

}
