package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.storage.WorldInfo;
import net.tropicraft.core.common.dimension.config.TropicraftBiomeProviderSettings;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;
import net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil;

import java.util.Set;

public class TropicraftBiomeProvider extends BiomeProvider {
    private final Layer noiseLayer;
    private final Layer blockLayer;

    protected final Set<BlockState> surfaceBlocks = Sets.newHashSet();

    public TropicraftBiomeProvider(final TropicraftBiomeProviderSettings settings) {
        super(ImmutableSet.of(
                TropicraftBiomes.TROPICS.get(),
                TropicraftBiomes.TROPICS_OCEAN.get(),
                TropicraftBiomes.TROPICS_RIVER.get(),
                TropicraftBiomes.TROPICS_BEACH.get(),
                TropicraftBiomes.RAINFOREST_HILLS.get(),
                TropicraftBiomes.RAINFOREST_PLAINS.get(),
                TropicraftBiomes.RAINFOREST_MOUNTAINS.get(),
                TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS.get(),
                TropicraftBiomes.KELP_FOREST.get()
        ));
        final WorldInfo info = settings.getWorldInfo();
        final TropicraftGeneratorSettings generatorSettings = settings.getGeneratorSettings();
        final Layer[] layers = TropicraftLayerUtil.buildTropicsProcedure(info.getSeed(), info.getGenerator(), generatorSettings);
        noiseLayer = layers[0];
        blockLayer = layers[1];
    }

    //TODO 1.15 still need?
//    @Override
//    public Biome getBiome(final BlockPos pos) {
//        return blockLayer.func_215738_a(pos.getX(), pos.getZ());
//    }

    // TODO 1.15 still need?
//    @Override
//    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
//        return blockLayer.generateBiomes(x, z, width, length);
//    }

    // TODO 1.15 still need?
//    @Override
//    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
//        int x0 = centerX - sideLength >> 2;
//        int z0 = centerZ - sideLength >> 2;
//        int x1 = centerX + sideLength >> 2;
//        int z1 = centerZ + sideLength >> 2;
//        int width = x1 - x0 + 1;
//        int height = z1 - z0 + 1;
//        Set<Biome> lvt_10_1_ = Sets.newHashSet();
//        Collections.addAll(lvt_10_1_, this.noiseLayer.generateBiomes(x0, z0, width, height));
//        return lvt_10_1_;
//    }

    // TODO - think super does this for us. do we still need it?
//
//    /**
//     * Checks if an area around a block contains only the specified biomes.
//     * To ensure NO other biomes, add a margin of at least four blocks to the radius
//     */
//    @Nullable
//    @Override
//    public BlockPos func_225531_a_(int x, int y, int z, int range, List<Biome> allowedBiomes, Random random) {
//        final int x0 = (x - range) >> 2;
//        final int z0 = (z - range) >> 2;
//        final int x1 = (x + range) >> 2;
//        final int z1 = (z + range) >> 2;
//
//        final int w = x1 - x0 + 1;
//        final int h = z1 - z0 + 1;
//        final Biome[] biomes = noiseLayer.generateBiomes(x0, z0, w, h);
//        BlockPos result = null;
//        int found = 0;
//        for (int i = 0; i < w * h; i++) {
//            final int xx = (x0 + i % w) << 2;
//            final int zz = (z0 + i / w) << 2;
//            if (allowedBiomes.contains(biomes[i])) {
//                if (result == null || random.nextInt(found + 1) == 0) {
//                    result = new BlockPos(xx, 0, zz);
//                }
//                found++;
//            }
//        }
//
//        return result;
//    }

    // really is "can generate structure?"
    @Override
    public boolean hasStructure(Structure<?> structure) {
        return this.hasStructureCache.computeIfAbsent(structure, (structure1) -> {
            for (final Biome biome : biomes) {
                if (biome.hasStructure(structure1)) {
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        if (surfaceBlocks.isEmpty()) {
            for (final Biome biome : biomes) {
                surfaceBlocks.add(biome.getSurfaceBuilderConfig().getTop());
            }
        }
        return surfaceBlocks;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return noiseLayer.func_215738_a(x, z);
    }
}
