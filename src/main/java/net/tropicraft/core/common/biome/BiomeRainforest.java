package net.tropicraft.core.common.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorRainforest;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;
import net.tropicraft.core.common.entity.hostile.EntityTropiSpider;


public class BiomeRainforest extends BiomeTropicraft {

	public BiomeRainforest(BiomeProperties props) {
		super(props);
		this.decorator = new BiomeDecoratorRainforest();

		
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrog.class, 25, 2, 5));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiSpider.class, 10, 1, 1));

	}
	
	@Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

}
