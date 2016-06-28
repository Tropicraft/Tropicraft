package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsOcean;

public class BiomeGenTropicsOcean extends BiomeGenTropicraft {

	public BiomeGenTropicsOcean(BiomeProperties bgprop) {
		super(bgprop);
		this.theBiomeDecorator = new BiomeDecoratorTropicsOcean();
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
	}

}
