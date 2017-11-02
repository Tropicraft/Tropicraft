package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsBeach;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

	public BiomeGenTropicsBeach(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorTropicsBeach();
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}
}
