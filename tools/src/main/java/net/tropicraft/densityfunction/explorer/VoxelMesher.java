package net.tropicraft.densityfunction.explorer;

import net.minecraft.util.ARGB;
import net.minecraft.world.level.CardinalLighting;

public class VoxelMesher {
    private static final int COLOR = 0xffa0a0a0;
    private static final int WEST_COLOR = ARGB.scaleRGB(COLOR, CardinalLighting.DEFAULT.west());
    private static final int EAST_COLOR = ARGB.scaleRGB(COLOR, CardinalLighting.DEFAULT.east());
    private static final int NORTH_COLOR = ARGB.scaleRGB(COLOR, CardinalLighting.DEFAULT.north());
    private static final int SOUTH_COLOR = ARGB.scaleRGB(COLOR, CardinalLighting.DEFAULT.south());
    private static final int UP_COLOR = ARGB.scaleRGB(COLOR, CardinalLighting.DEFAULT.up());
    private static final int DOWN_COLOR = ARGB.scaleRGB(COLOR, CardinalLighting.DEFAULT.down());

    public static void generate(VoxelChunk chunk, VoxelChunk westChunk, VoxelChunk eastChunk, VoxelChunk northChunk, VoxelChunk southChunk, Output output) {
        for (int z = 0; z < chunk.sizeZ(); z++) {
            for (int x = 0; x < chunk.sizeX(); x++) {
                boolean solidBelow = false;
                int index = chunk.indexUnchecked(x, 0, z);
                int westColumnIndex;
                VoxelChunk westColumnChunk;
                if (x > 0) {
                    westColumnIndex = chunk.indexUnchecked(x - 1, 0, z);
                    westColumnChunk = chunk;
                } else {
                    westColumnIndex = westChunk.indexUnchecked(westChunk.sizeX() - 1, 0, z);
                    westColumnChunk = westChunk;
                }
                int eastColumnIndex;
                VoxelChunk eastColumnChunk;
                if (x < chunk.sizeX() - 1) {
                    eastColumnIndex = chunk.indexUnchecked(x + 1, 0, z);
                    eastColumnChunk = chunk;
                } else {
                    eastColumnIndex = eastChunk.indexUnchecked(0, 0, z);
                    eastColumnChunk = eastChunk;
                }
                int northColumnIndex;
                VoxelChunk northColumnChunk;
                if (z > 0) {
                    northColumnIndex = chunk.indexUnchecked(x, 0, z - 1);
                    northColumnChunk = chunk;
                } else {
                    northColumnIndex = northChunk.indexUnchecked(x, 0, northChunk.sizeZ() - 1);
                    northColumnChunk = northChunk;
                }
                int southColumnIndex;
                VoxelChunk southChunkIndex;
                if (z < chunk.sizeZ() - 1) {
                    southColumnIndex = chunk.indexUnchecked(x, 0, z + 1);
                    southChunkIndex = chunk;
                } else {
                    southColumnIndex = southChunk.indexUnchecked(x, 0, 0);
                    southChunkIndex = southChunk;
                }
                for (int y = 0; y < chunk.sizeY(); y++) {
                    boolean solid = chunk.isSolid(index);
                    if (solid != solidBelow) {
                        if (solid) {
                            addDownFace(output, x, y, z);
                        } else {
                            addUpFace(output, x, y - 1, z);
                        }
                        solidBelow = solid;
                    }
                    if (solid) {
                        if (!westColumnChunk.isSolid(westColumnIndex)) {
                            addWestFace(output, x, y, z);
                        }
                        if (!eastColumnChunk.isSolid(eastColumnIndex)) {
                            addEastFace(output, x, y, z);
                        }
                        if (!northColumnChunk.isSolid(northColumnIndex)) {
                            addNorthFace(output, x, y, z);
                        }
                        if (!southChunkIndex.isSolid(southColumnIndex)) {
                            addSouthFace(output, x, y, z);
                        }
                    }
                    index++;
                    westColumnIndex++;
                    eastColumnIndex++;
                    northColumnIndex++;
                    southColumnIndex++;
                }
                if (solidBelow) {
                    addUpFace(output, x, chunk.sizeY() - 1, z);
                }
            }
        }
    }

    private static void addUpFace(Output output, int x, int y, int z) {
        output.addFace(
                x, y + 1, z,
                x, y + 1, z + 1,
                x + 1, y + 1, z + 1,
                x + 1, y + 1, z,
                UP_COLOR
        );
    }

    private static void addDownFace(Output output, int x, int y, int z) {
        output.addFace(
                x, y, z,
                x + 1, y, z,
                x + 1, y, z + 1,
                x, y, z + 1,
                DOWN_COLOR
        );
    }

    private static void addWestFace(Output output, int x, int y, int z) {
        output.addFace(
                x, y, z,
                x, y, z + 1,
                x, y + 1, z + 1,
                x, y + 1, z,
                WEST_COLOR
        );
    }

    private static void addEastFace(Output output, int x, int y, int z) {
        output.addFace(
                x + 1, y, z,
                x + 1, y + 1, z,
                x + 1, y + 1, z + 1,
                x + 1, y, z + 1,
                EAST_COLOR
        );
    }

    private static void addNorthFace(Output output, int x, int y, int z) {
        output.addFace(
                x, y, z,
                x, y + 1, z,
                x + 1, y + 1, z,
                x + 1, y, z,
                NORTH_COLOR
        );
    }

    private static void addSouthFace(Output output, int x, int y, int z) {
        output.addFace(
                x, y, z + 1,
                x + 1, y, z + 1,
                x + 1, y + 1, z + 1,
                x, y + 1, z + 1,
                SOUTH_COLOR
        );
    }

    @FunctionalInterface
    public interface Output {
        void addFace(
                int x0, int y0, int z0,
                int x1, int y1, int z1,
                int x2, int y2, int z2,
                int x3, int y3, int z3,
                int color
        );
    }
}
