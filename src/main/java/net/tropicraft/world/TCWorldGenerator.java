package net.tropicraft.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.tropicraft.config.ConfigGenRates;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.worldgen.WorldGenBamboo;
import net.tropicraft.world.worldgen.WorldGenEIH;
import net.tropicraft.world.worldgen.WorldGenTallFlower;
import net.tropicraft.world.worldgen.WorldGenTropicraftCurvedPalm;
import net.tropicraft.world.worldgen.WorldGenTropicraftFlowers;
import net.tropicraft.world.worldgen.WorldGenTropicraftLargePalmTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftNormalPalms;
import cpw.mods.fml.common.IWorldGenerator;

/**
 * Used by Tropicraft to generate Tropistuff in the overworld. Eventually may be expanded to having our own biomes? Currently just
 * generates tropithings in existing biomes. Possibly modded biomes as well as vanilla? Not sure!
 *
 */
public class TCWorldGenerator implements IWorldGenerator {

	public TCWorldGenerator() {
	}

	@Override
	/**
	 * Call the old generate method
	 */
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		generateSurface(world, random, chunkX, chunkZ);
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
		
		if (ConfigGenRates.genTropicraftInOverworld) {
			// Convert to block coords rather than chunk coords
			chunkX *= 16;
			chunkZ *= 16;

			if (world.provider.dimensionId == 0 && world.provider.terrainType != world.provider.terrainType.FLAT) {
				int k = chunkX + random.nextInt(16) + 8;
				int l = random.nextInt(62) + 64;
				int i1 = chunkZ + random.nextInt(16) + 8;

				if (ConfigGenRates.genTropicraftFlowersInOverworld) {
					for (int j3 = 0; j3 < 10; j3++) {
						l = random.nextInt(62) + 64;
						(new WorldGenTropicraftFlowers(world, random, TCBlockRegistry.flowers, BiomeGenTropicraft.DEFAULT_FLOWER_META)).generate(world, random, k, l, i1);
					}	
				}

				if (ConfigGenRates.genTropicraftEIHInOverworld && random.nextInt(27) == 0) {
					l = random.nextInt(62) + 64;
					(new WorldGenEIH(world, random)).generate(world, random, k, l, i1);
				}

				//*********** HERE TO BOTTOM ARE THINGS NECESSARY TO GET TO THE TROPICS ***********//

				if (ConfigGenRates.genPalmsInOverworld && random.nextInt(12) == 0) {
					BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(cx, cz);					

					if ((ConfigGenRates.genOverworldPalmsInBeachOnly && biome == BiomeGenBase.beach) || !ConfigGenRates.genOverworldPalmsInBeachOnly)
						if (ConfigGenRates.palmChanceOfGenInOverworld < 0 || random.nextFloat() < (float)(ConfigGenRates.palmChanceOfGenInOverworld / 100F)) {
							for (int j3 = 0; j3 < ConfigGenRates.palmPopulationFactorInOverworld; j3++) {
								l = random.nextInt(62) + 64;

								if (random.nextInt(5) == 0) {
									(new WorldGenTropicraftLargePalmTrees()).generate(world, random, k, l, i1);
								} else if (random.nextInt(5) < 3) {
									(new WorldGenTropicraftCurvedPalm(world, random)).generate(world, random, k, l, i1);
								} else {
									(new WorldGenTropicraftNormalPalms()).generate(world, random, k, l, i1);
								}
							}
						}
				}

				// Pineapples
				if (ConfigGenRates.genPineapplesInOverworld && random.nextInt(8) == 0) {
					l = random.nextInt(62) + 64;
					(new WorldGenTallFlower(world, random, TCBlockRegistry.pineapple, 7, 8)).generate(world, random, k, l, i1);
				}

				// Bamboo
				if (ConfigGenRates.genBambooInOverworld && random.nextInt(3) == 0) {
					l = random.nextInt(62) + 64;
					(new WorldGenBamboo(world, random)).generate(world, random, k, l, i1);
				}
			}
		}
	}
}