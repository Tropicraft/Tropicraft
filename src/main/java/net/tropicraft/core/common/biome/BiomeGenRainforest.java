package net.tropicraft.core.common.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorRainforest;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBlue;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogGreen;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogRed;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogYellow;


public class BiomeGenRainforest extends BiomeGenTropicraft {

	public BiomeGenRainforest(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorRainforest();

        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogBlue.class, 25, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogGreen.class, 25, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogRed.class, 25, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrogYellow.class, 25, 1, 2));
	}
	
	@Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

}
