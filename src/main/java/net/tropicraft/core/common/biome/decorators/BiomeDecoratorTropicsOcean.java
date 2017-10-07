package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.configuration.ConfigGenRates;
import net.tropicraft.core.common.worldgen.WorldGenCoral;
import net.tropicraft.core.common.worldgen.WorldGenSeaweed;
import net.tropicraft.core.common.worldgen.WorldGenSunkenShip;

public class BiomeDecoratorTropicsOcean extends BiomeDecoratorTropicraft {

	public BiomeDecoratorTropicsOcean() {
		
	}
	
    public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
        if (this.decorating)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
            this.chunkPos = pos;
            this.genDecorations(biome, worldIn, random);
            this.decorating = false;
        }
    }
    
    public void genDecorations(Biome biome, World world, Random rand) {
//		if (rand.nextInt(5) == 0) {
//			int x = randCoord(rand, chunkPos.getX(), 16) + 8;
//			int z = randCoord(rand, chunkPos.getZ(), 16) + 8;
//			BlockPos pos = new BlockPos(x, 0, z);
//			new WorldGenCoral().generate(world, rand, pos);
//		}
		
		if (rand.nextInt(8) == 0) {
			int x = randCoord(rand, chunkPos.getX(), 16) + 8;
			int z = randCoord(rand, chunkPos.getZ(), 16) + 8;
			BlockPos pos = new BlockPos(x, 0, z);
			new WorldGenSeaweed().generate(world, rand, pos);
		}

//		if (ConfigGenRates.SHIPWRECK_CHANCE != 0 /*&& rand.nextInt(ConfigGenRates.SHIPWRECK_CHANCE) == 0*/) {
//			int i = randCoord(rand, chunkPos.getX(), 16);
//			int k = randCoord(rand, chunkPos.getZ(), 16);
//			new WorldGenSunkenShip(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
//		}
    }

}
