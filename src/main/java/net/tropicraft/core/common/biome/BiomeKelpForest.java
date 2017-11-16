package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsOcean;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeKelpForest extends BiomeTropicraft {

	public BiomeKelpForest(BiomeProperties bgprop) {
		super(bgprop);
		this.decorator = new BiomeDecoratorTropicsOcean();
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}
}
