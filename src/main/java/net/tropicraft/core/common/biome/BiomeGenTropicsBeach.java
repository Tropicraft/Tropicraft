package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsBeach;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

	public BiomeGenTropicsBeach(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorTropicsBeach();
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
	}
}
