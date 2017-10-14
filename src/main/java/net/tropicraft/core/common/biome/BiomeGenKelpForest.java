package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsOcean;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeGenKelpForest extends BiomeGenTropicraft {

	public BiomeGenKelpForest(BiomeProperties bgprop) {
		super(bgprop);
		this.theBiomeDecorator = new BiomeDecoratorTropicsOcean();
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}
}
