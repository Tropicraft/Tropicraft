package net.tropicraft.core.common.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorRainforest;


public class BiomeGenRainforest extends BiomeGenTropicraft {

	public BiomeGenRainforest(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorRainforest();
	}
	
	@Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

}
