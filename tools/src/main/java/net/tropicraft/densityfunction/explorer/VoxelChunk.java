package net.tropicraft.densityfunction.explorer;

import java.util.BitSet;

public class VoxelChunk {
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final BitSet solid;

    public VoxelChunk(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        solid = new BitSet(sizeX * sizeY * sizeZ);
    }

    public void setSolid(int x, int y, int z) {
        solid.set(indexUnchecked(x, y, z));
    }

    public boolean isSolid(int x, int y, int z) {
        return solid.get(indexUnchecked(x, y, z));
    }

    public boolean isSolid(int index) {
        return solid.get(index);
    }

    public int indexUnchecked(int x, int y, int z) {
        return y + (x + z * sizeX) * sizeY;
    }

    public int sizeX() {
        return sizeX;
    }

    public int sizeY() {
        return sizeY;
    }

    public int sizeZ() {
        return sizeZ;
    }
}
