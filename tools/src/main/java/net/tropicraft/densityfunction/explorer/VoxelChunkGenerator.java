package net.tropicraft.densityfunction.explorer;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;

public class VoxelChunkGenerator {
    private final int chunkWidth;

    private final NoiseSettings noiseSettings;
    private final NoiseGeneratorSettings noiseGeneratorSettings;
    private final RandomState randomState;
    private final Aquifer.FluidPicker fluidPicker = (_, _, _) -> new Aquifer.FluidStatus(Integer.MIN_VALUE, Blocks.WATER.defaultBlockState());

    public VoxelChunkGenerator(int chunkWidth, int minY, int height, HolderLookup.Provider registries, long seed, DensityFunction densityFunction) {
        this.chunkWidth = chunkWidth;

        noiseSettings = new NoiseSettings(minY, height, 1, 2);
        noiseGeneratorSettings = new NoiseGeneratorSettings(
                noiseSettings,
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                new NoiseRouter(
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        densityFunction,
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero()
                ),
                SurfaceRules.state(Blocks.STONE.defaultBlockState()),
                List.of(),
                0,
                false,
                false,
                false,
                false
        );
        randomState = RandomState.create(noiseGeneratorSettings, registries.lookupOrThrow(Registries.NOISE), seed);
    }

    public VoxelChunk generate(int chunkX, int chunkZ) {
        int minBlockX = chunkX * chunkWidth;
        int minBlockY = noiseSettings.minY();
        int minBlockZ = chunkZ * chunkWidth;
        int cellWidth = noiseSettings.getCellWidth();
        int cellHeight = noiseSettings.getCellHeight();

        int cellCountXz = chunkWidth / cellWidth;
        int cellCountY = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
        int cellMinY = Mth.floorDiv(noiseSettings.minY(), cellHeight);

        NoiseChunk noiseChunk = new NoiseChunk(
                cellCountXz,
                randomState,
                minBlockX,
                minBlockZ,
                noiseSettings,
                Beardifier.EMPTY,
                noiseGeneratorSettings,
                fluidPicker,
                Blender.empty()
        );

        VoxelChunk chunk = new VoxelChunk(chunkWidth, noiseSettings.height(), chunkWidth);

        noiseChunk.initializeForFirstCellX();

        for (int cellXIndex = 0; cellXIndex < cellCountXz; cellXIndex++) {
            noiseChunk.advanceCellX(cellXIndex);

            for (int cellZIndex = 0; cellZIndex < cellCountXz; cellZIndex++) {
                for (int cellYIndex = cellCountY - 1; cellYIndex >= 0; cellYIndex--) {
                    noiseChunk.selectCellYZ(cellYIndex, cellZIndex);

                    for (int yInCell = cellHeight - 1; yInCell >= 0; yInCell--) {
                        int worldY = (cellMinY + cellYIndex) * cellHeight + yInCell;
                        int indexY = worldY - minBlockY;
                        double factorY = (double) yInCell / cellHeight;
                        noiseChunk.updateForY(worldY, factorY);

                        for (int xInCell = 0; xInCell < cellWidth; xInCell++) {
                            int indexX = cellXIndex * cellWidth + xInCell;
                            int worldX = minBlockX + indexX;
                            double factorX = (double) xInCell / cellWidth;
                            noiseChunk.updateForX(worldX, factorX);

                            for (int zInCell = 0; zInCell < cellWidth; zInCell++) {
                                int indexZ = cellZIndex * cellWidth + zInCell;
                                int worldZ = minBlockZ + indexZ;
                                double factorZ = (double) zInCell / cellWidth;
                                noiseChunk.updateForZ(worldZ, factorZ);

                                if (noiseChunk.getInterpolatedDensity() > 0.0) {
                                    chunk.setSolid(indexX, indexY, indexZ);
                                }
                            }
                        }
                    }
                }
            }

            noiseChunk.swapSlices();
        }

        noiseChunk.stopInterpolation();

        return chunk;
    }
}
