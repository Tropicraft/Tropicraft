package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropics;

public class BiomeGenTropics extends BiomeGenTropicraft {

	public BiomeGenTropics(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorTropics();
	}

}
