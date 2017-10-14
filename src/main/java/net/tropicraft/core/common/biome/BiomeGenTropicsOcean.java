package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsOcean;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeGenTropicsOcean extends BiomeGenTropicraft {

	public BiomeGenTropicsOcean(BiomeProperties bgprop) {
		super(bgprop);
		this.theBiomeDecorator = new BiomeDecoratorTropicsOcean();
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}

}
