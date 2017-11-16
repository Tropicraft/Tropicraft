package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsBeach;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeTropicsBeach extends BiomeTropicraft {

	public BiomeTropicsBeach(BiomeProperties props) {
		super(props);
		this.decorator = new BiomeDecoratorTropicsBeach();
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}
}
