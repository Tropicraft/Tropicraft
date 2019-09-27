package net.tropicraft.core.common.dimension.biome;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.core.common.dimension.config.TropicraftBiomeProviderSettings;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;
import net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil;

public class TropicraftBiomeProvider extends BiomeProvider {
    private final Layer noiseLayer;
    private final Layer blockLayer;

    protected final Set<BlockState> surfaceBlocks = Sets.newHashSet();

    private final ImmutableList<Supplier<Biome>> possibleBiomes = ImmutableList.of(
            TropicraftBiomes.TROPICS,
            TropicraftBiomes.TROPICS_OCEAN,
            TropicraftBiomes.TROPICS_RIVER,
            TropicraftBiomes.TROPICS_BEACH,
            TropicraftBiomes.RAINFOREST_HILLS,
            TropicraftBiomes.RAINFOREST_PLAINS,
            TropicraftBiomes.RAINFOREST_MOUNTAINS,
            TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS,
            TropicraftBiomes.KELP_FOREST
    );

    public TropicraftBiomeProvider(final TropicraftBiomeProviderSettings settings) {
        final WorldInfo info = settings.getWorldInfo();
        final TropicraftGeneratorSettings generatorSettings = settings.getGeneratorSettings();
        final Layer[] layers = TropicraftLayerUtil.buildTropicsProcedure(info.getSeed(), info.getGenerator(), generatorSettings);
        noiseLayer = layers[0];
        blockLayer = layers[1];
    }

    @Override
    public Biome getBiome(int x, int z) {
        return blockLayer.func_215738_a(x, z);
    }

    // get noise biome
    @Override
    public Biome func_222366_b(int x, int z) {
        return noiseLayer.func_215738_a(x, z);
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        return blockLayer.generateBiomes(x, z, width, length);
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        int x0 = centerX - sideLength >> 2;
        int z0 = centerZ - sideLength >> 2;
        int x1 = centerX + sideLength >> 2;
        int z1 = centerZ + sideLength >> 2;
        int width = x1 - x0 + 1;
        int height = z1 - z0 + 1;
        Set<Biome> lvt_10_1_ = Sets.newHashSet();
        Collections.addAll(lvt_10_1_, this.noiseLayer.generateBiomes(x0, z0, width, height));
        return lvt_10_1_;
    }

    /**
     * Checks if an area around a block contains only the specified biomes.
     * To ensure NO other biomes, add a margin of at least four blocks to the radius
     */
    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> allowedBiomes, Random random) {
        final int x0 = (x - range) >> 2;
        final int z0 = (z - range) >> 2;
        final int x1 = (x + range) >> 2;
        final int z1 = (z + range) >> 2;

        final int w = x1 - x0 + 1;
        final int h = z1 - z0 + 1;
        final Biome[] biomes = noiseLayer.generateBiomes(x0, z0, w, h);
        BlockPos result = null;
        int found = 0;
        for (int i = 0; i < w * h; i++) {
            final int xx = (x0 + i % w) << 2;
            final int zz = (z0 + i / w) << 2;
            if (allowedBiomes.contains(biomes[i])) {
                if (result == null || random.nextInt(found + 1) == 0) {
                    result = new BlockPos(xx, 0, zz);
                }
                found++;
            }
        }

        return result;
    }

    // really is "can generate structure?"
    @Override
    public boolean hasStructure(Structure<?> structure) {
        return this.hasStructureCache.computeIfAbsent(structure, (structure1) -> {
            for (final Supplier<Biome> biome : possibleBiomes) {
                if (biome.get().hasStructure(structure1)) {
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        if (surfaceBlocks.isEmpty()) {
            for (final Supplier<Biome> biome : possibleBiomes) {
                surfaceBlocks.add(biome.get().getSurfaceBuilderConfig().getTop());
            }
        }
        return surfaceBlocks;
    }
}
