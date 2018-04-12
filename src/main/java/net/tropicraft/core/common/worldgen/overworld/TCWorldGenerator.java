package net.tropicraft.core.common.worldgen.overworld;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.tropicraft.configuration.GenRates;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.config.TropicsConfigs;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.common.worldgen.WorldGenBamboo;
import net.tropicraft.core.common.worldgen.WorldGenCurvedPalms;
import net.tropicraft.core.common.worldgen.WorldGenEIH;
import net.tropicraft.core.common.worldgen.WorldGenLargePalmTrees;
import net.tropicraft.core.common.worldgen.WorldGenNormalPalms;
import net.tropicraft.core.common.worldgen.WorldGenTallFlower;
import net.tropicraft.core.common.worldgen.WorldGenTropicalFlowers;
import net.tropicraft.core.registry.BlockRegistry;

/**
 * Used by Tropicraft to generate Tropistuff in the overworld. Eventually may be expanded to having our own biomes? Currently just
 * generates tropithings in existing biomes. Possibly modded biomes as well as vanilla? Not sure!
 *
 */
public class TCWorldGenerator implements IWorldGenerator {

    public TCWorldGenerator() {

    }

    /**
     * The generation method used in the older versions of tropicraft to generate things in the
     * main world
     * @param world World to generate in
     * @param random Random!!!
     * @param chunkX chunkX
     * @param chunkZ chunkZ
     */
    public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
    	Biome biome = world.getBiome(new BlockPos(chunkX, 0, chunkZ));

        if (TropicsConfigs.genOverworld) {
            // Convert to block coords rather than chunk coords
            chunkX *= 16;
            chunkZ *= 16;

            if (world.provider.getDimension() == 0 && world.getWorldType() != WorldType.FLAT) {
                int k = chunkX + random.nextInt(16) + 8;
                int l = random.nextInt(62) + 64;
                int i1 = chunkZ + random.nextInt(16) + 8;
                
                Biome biomeFlowers = world.getBiome(new BlockPos(k, l, i1));

                if (TropicsConfigs.genOverworldFlowers && Util.isBiomeTropical(biomeFlowers)) {
                    for (int j3 = 0; j3 < 10; j3++) {
                        l = random.nextInt(62) + 64;
                        BlockPos flowerPos = new BlockPos(k, l, i1);
                        (new WorldGenTropicalFlowers(world, random, BlockRegistry.flowers, TropicraftFlowers.OVERWORLD_FLOWERS)).generate(world, random, flowerPos);
                    }	
                }

                if (TropicsConfigs.genOverworldEIH && random.nextInt(GenRates.EIH_CHANCE) == 0) {
                    l = random.nextInt(62) + 64;
                    BlockPos eihPos = new BlockPos(k, l, i1);
                    (new WorldGenEIH(world, random)).generate(world, random, eihPos);
                }

                //*********** HERE TO BOTTOM ARE THINGS NECESSARY TO GET TO THE TROPICS ***********//

                if (biome.getDefaultTemperature() > 0.5F && !biome.getEnableSnow()) {
	                if (TropicsConfigs.genOverworldPalms && random.nextInt(10) == 0) {
	                    if ((TropicsConfigs.genOverworldPalmsBeachOnly && biome == Biomes.BEACH) || !TropicsConfigs.genOverworldPalmsBeachOnly) {
	                        if (TropicsConfigs.chancePalmOverworld < 0 || random.nextFloat() < (float)(TropicsConfigs.chancePalmOverworld / 100F)) {
	                            for (int j3 = 0; j3 < TropicsConfigs.factorPalmOverworld; j3++) {
	                                l = random.nextInt(62) + 64;

	                                BlockPos pos;
	                                if (random.nextInt(5) == 0) {
	                                    pos = new BlockPos(chunkX + 16, l, chunkZ + 16);
	                                    (new WorldGenLargePalmTrees(world, random)).generate(world, random, pos);
	                                } else if (random.nextInt(5) < 3) {
	                                    int x = chunkX + 13 + random.nextInt(5);
	                                    int z = chunkZ + 13 + random.nextInt(5);
	                                    pos = new BlockPos(x, l, z);
	                                    (new WorldGenCurvedPalms(world, random)).generate(world, random, pos);
	                                } else {
	                                    int x = chunkX + 13 + random.nextInt(5);
	                                    int z = chunkZ + 13 + random.nextInt(5);
	                                    pos = new BlockPos(x, l, z);
	                                    (new WorldGenNormalPalms(world, random)).generate(world, random, pos);
	                                }
	                            }
	                        }
	                    }
	                }
                }

                // Pineapples
                if (biome.getRainfall() > 0.3F && biome.getDefaultTemperature() > 0.0F) {
                    if (TropicsConfigs.genOverworldPineapples && random.nextInt(TropicsConfigs.tallFlowerGenChanceOverworld) == 0) {
                        l = world.getHeight(chunkX, chunkZ);
                        BlockPos pineapplePos = new BlockPos(chunkX, l, chunkZ);
                        for (int t = 0; t < 3; t++) {
                            (new WorldGenTallFlower(world, random, BlockRegistry.pineapple.getDefaultState())).generate(pineapplePos);
                        }
                    }
                }

                // Bamboo
                if (biome.getDefaultTemperature() > 0.2F) {
                    if (TropicsConfigs.genOverworldBamboo && random.nextInt(TropicsConfigs.bambooGenChanceOverworld) == 0) {
                        l = random.nextInt(62) + 64;
                        BlockPos bambooPos = new BlockPos(chunkX, l, chunkZ);
                        (new WorldGenBamboo(world, random)).generate(world, random, bambooPos);
                    }
                }
            }
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateSurface(world, random, chunkX, chunkZ);
    }
}