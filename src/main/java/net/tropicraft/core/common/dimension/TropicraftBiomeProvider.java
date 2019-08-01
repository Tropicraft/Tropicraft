package net.tropicraft.core.common.dimension;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TropicraftBiomeProvider extends BiomeProvider {
    @Override
    public Biome getBiome(int i, int i1) {
        return null;
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        return new Biome[0];
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        return null;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn) {
        return false;
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        return null;
    }
}
