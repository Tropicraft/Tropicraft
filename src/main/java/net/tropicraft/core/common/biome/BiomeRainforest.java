package net.tropicraft.core.common.biome;

import java.util.Random;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorRainforest;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;
import net.tropicraft.core.common.entity.hostile.EntityTropiSpider;


public class BiomeRainforest extends BiomeTropicraft {

	public BiomeRainforest(BiomeProperties props) {
		super(props);
		this.decorator = new BiomeDecoratorRainforest();

		this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 20, 1, 1));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityParrot.class, 40, 1, 2));
		
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrog.class, 25, 2, 5));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiSpider.class, 30, 1, 1));

	}
	
	@Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

}
