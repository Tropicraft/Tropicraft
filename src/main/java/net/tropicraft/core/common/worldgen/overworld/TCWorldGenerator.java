package net.tropicraft.core.common.worldgen.overworld;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.tropicraft.configuration.GenRates;
import net.tropicraft.core.common.config.TropicsConfigs;
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

        int cx = chunkX;
        int cz = chunkZ;

        if (TropicsConfigs.genOverworld) {
            // Convert to block coords rather than chunk coords
            chunkX *= 16;
            chunkZ *= 16;

            if (world.provider.getDimension() == 0 && world.getWorldType() != WorldType.FLAT) {
                int k = chunkX + random.nextInt(16) + 8;
                int l = random.nextInt(62) + 64;
                int i1 = chunkZ + random.nextInt(16) + 8;

                if (TropicsConfigs.genOverworldFlowers) {
                    for (int j3 = 0; j3 < 10; j3++) {
                        l = random.nextInt(62) + 64;
                        BlockPos flowerPos = new BlockPos(k, l, i1);
                        (new WorldGenTropicalFlowers(world, random, BlockRegistry.flowers)).generate(world, random, flowerPos);
                    }	
                }

                if (TropicsConfigs.genOverworldEIH && random.nextInt(GenRates.EIH_CHANCE) == 0) {
                    l = random.nextInt(62) + 64;
                    BlockPos eihPos = new BlockPos(k, l, i1);
                    (new WorldGenEIH(world, random)).generate(world, random, eihPos);
                }

                //*********** HERE TO BOTTOM ARE THINGS NECESSARY TO GET TO THE TROPICS ***********//

                if (TropicsConfigs.genOverworldPalms && random.nextInt(10) == 0) {
                    BlockPos posChunk = new BlockPos(cx, 0, cz);
                    Biome biome = world.getBiomeProvider().getBiome(posChunk);			

                    if ((TropicsConfigs.genOverworldPalmsBeachOnly && biome == Biomes.BEACH) || !TropicsConfigs.genOverworldPalmsBeachOnly)
                        if (TropicsConfigs.chancePalmOverworld < 0 || random.nextFloat() < (float)(TropicsConfigs.chancePalmOverworld / 100F)) {
                            for (int j3 = 0; j3 < TropicsConfigs.factorPalmOverworld; j3++) {
                                l = random.nextInt(62) + 64;

                                BlockPos pos = new BlockPos(k, l, i1);
                                if (random.nextInt(5) == 0) {
                                    (new WorldGenLargePalmTrees(world, random)).generate(world, random, pos);
                                } else if (random.nextInt(5) < 3) {
                                    (new WorldGenCurvedPalms(world, random)).generate(world, random, pos);
                                } else {
                                    (new WorldGenNormalPalms(world, random)).generate(world, random, pos);
                                }
                            }
                        }
                }

                // Pineapples
                if (TropicsConfigs.genOverworldPineapples && random.nextInt(GenRates.TALL_FLOWERS_CHANCE) == 0) {
                    l = random.nextInt(62) + 64;
                    BlockPos pineapplePos = new BlockPos(k, l, i1);
                    (new WorldGenTallFlower(world, random, BlockRegistry.pineapple.getDefaultState())).generate(pineapplePos);
                }

                // Bamboo
                if (TropicsConfigs.genOverworldBamboo && random.nextInt(GenRates.BAMBOO_CHANCE) == 0) {
                    l = random.nextInt(62) + 64;
                    BlockPos bambooPos = new BlockPos(k, l, i1);
                    (new WorldGenBamboo(world, random)).generate(world, random, bambooPos);
                }
            }
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateSurface(world, random, chunkX, chunkZ);
    }
}