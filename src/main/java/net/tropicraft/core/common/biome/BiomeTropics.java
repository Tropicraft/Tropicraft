package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropics;

public class BiomeTropics extends BiomeTropicraft {

	public BiomeTropics(BiomeProperties props) {
		super(props);
		this.decorator = new BiomeDecoratorTropics();
	}

}
