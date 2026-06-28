package net.tropicraft.densityfunction.explorer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.GpuDevice;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkMap implements AutoCloseable {
    public static final int SIZE = 64;
    public static final int COUNT = SIZE * SIZE;

    private static final int INDEX_MASK = SIZE - 1;
    private static final int INDEX_SHIFT = 6;

    private VoxelChunkGenerator generator;
    private final Chunk[] chunks = new Chunk[COUNT];
    private int centerX = Integer.MIN_VALUE;
    private int centerZ = Integer.MIN_VALUE;
    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;

    private final Queue<ChunkPos> chunksToMesh = new ConcurrentLinkedQueue<>();

    public ChunkMap(VoxelChunkGenerator generator) {
        this.generator = generator;
        for (int i = 0; i < chunks.length; i++) {
            chunks[i] = new Chunk();
        }
        setCenter(0, 0);
    }

    public void setChunkGenerator(VoxelChunkGenerator generator) {
        this.generator = generator;
        for (Chunk chunk : chunks) {
            chunk.invalidateAndRebuild(generator);
        }
    }

    public int centerX() {
        return centerX;
    }

    public int centerZ() {
        return centerZ;
    }

    public void update(int centerX, int centerZ) {
        setCenter(centerX, centerZ);

        if (chunksToMesh.isEmpty()) {
            return;
        }

        // Terribly inefficient, but just collect everything that *might* now be able to have a mesh built
        Set<ChunkPos> chunksToCheck = new ObjectOpenHashSet<>(chunksToMesh.size() * 2);
        ChunkPos chunkPos;
        while ((chunkPos = chunksToMesh.poll()) != null) {
            chunksToCheck.add(chunkPos);
            chunksToCheck.add(new ChunkPos(chunkPos.x() - 1, chunkPos.z()));
            chunksToCheck.add(new ChunkPos(chunkPos.x() + 1, chunkPos.z()));
            chunksToCheck.add(new ChunkPos(chunkPos.x(), chunkPos.z() - 1));
            chunksToCheck.add(new ChunkPos(chunkPos.x(), chunkPos.z() + 1));
        }

        for (ChunkPos checkPos : chunksToCheck) {
            Chunk chunk = get(checkPos.x(), checkPos.z());
            if (chunk != null) {
                chunk.tryScheduleMesh();
            }
        }
    }

    private void setCenter(int centerX, int centerZ) {
        if (this.centerX == centerX && this.centerZ == centerZ) {
            return;
        }

        this.centerX = centerX;
        this.centerZ = centerZ;
        minX = centerX - SIZE / 2;
        minZ = centerZ - SIZE / 2;
        maxX = minX + SIZE - 1;
        maxZ = minZ + SIZE - 1;
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                int chunkX = x + minX;
                int chunkZ = z + minZ;
                getOrThrow(chunkX, chunkZ).updatePosition(chunkX, chunkZ, generator);
            }
        }
    }

    private Chunk getOrThrow(int x, int z) {
        Chunk chunk = get(x, z);
        if (chunk == null) {
            throw new IllegalArgumentException("Chunk not in range at (" + x + ", " + z + ")");
        }
        return chunk;
    }

    private @Nullable Chunk get(int x, int z) {
        if (x < minX || z < minZ || x > maxX || z > maxZ) {
            return null;
        }
        return chunks[(x & INDEX_MASK) | (z & INDEX_MASK) << INDEX_SHIFT];
    }

    // Off-thread, and chunk might have been unloaded by the time we receive this!
    private void onVoxelsReady(ChunkPos chunkPos) {
        chunksToMesh.add(chunkPos);
    }

    public List<ChunkPos> getChunkPositions() {
        return Lists.transform(Arrays.asList(chunks), Chunk::pos);
    }

    public List<@Nullable ChunkMesh> getOrUploadMeshes(GpuDevice device) {
        List<@Nullable ChunkMesh> meshes = new ArrayList<>(chunks.length);
        for (Chunk chunk : chunks) {
            meshes.add(chunk.getMeshOrUpload(device));
        }
        return meshes;
    }

    @Override
    public void close() {
        for (Chunk chunk : chunks) {
            chunk.close();
        }
    }

    private class Chunk implements AutoCloseable {
        private @Nullable ChunkPos pos;

        private @Nullable CompletableFuture<VoxelChunk> voxels;
        private @Nullable CompletableFuture<ChunkMesh.@Nullable Data> pendingMesh;
        private @Nullable ChunkMesh mesh;
        private boolean meshInvalidated;

        public void updatePosition(int x, int z, VoxelChunkGenerator generator) {
            ChunkPos newPos = new ChunkPos(x, z);
            if (!newPos.equals(pos)) {
                pos = newPos;
                clearPendingMesh();
                clearMesh();
                invalidateAndRebuild(generator);
            }
        }

        public void invalidateAndRebuild(VoxelChunkGenerator generator) {
            ChunkPos chunkPos = pos();
            meshInvalidated = true;
            clearVoxels();
            voxels = CompletableFuture.supplyAsync(() -> generator.generate(chunkPos.x(), chunkPos.z()), DfExplorer.GENERATE_EXECUTOR);
            voxels.thenRun(() -> onVoxelsReady(chunkPos));
        }

        public void tryScheduleMesh() {
            if (!meshInvalidated && (mesh != null || pendingMesh != null)) {
                return;
            }
            VoxelChunk voxels = getVoxelsNow();
            if (voxels == null) {
                return;
            }
            ChunkPos pos = Objects.requireNonNull(this.pos);
            Chunk westChunk = get(pos.x() - 1, pos.z());
            Chunk eastChunk = get(pos.x() + 1, pos.z());
            Chunk northChunk = get(pos.x(), pos.z() - 1);
            Chunk southChunk = get(pos.x(), pos.z() + 1);
            VoxelChunk westVoxels = westChunk != null ? westChunk.getVoxelsNow() : null;
            VoxelChunk eastVoxels = eastChunk != null ? eastChunk.getVoxelsNow() : null;
            VoxelChunk northVoxels = northChunk != null ? northChunk.getVoxelsNow() : null;
            VoxelChunk southVoxels = southChunk != null ? southChunk.getVoxelsNow() : null;
            if (westVoxels == null || eastVoxels == null || northVoxels == null || southVoxels == null) {
                return;
            }
            clearPendingMesh();
            pendingMesh = CompletableFuture.supplyAsync(
                    () -> ChunkMesh.Data.generate(voxels, westVoxels, eastVoxels, northVoxels, southVoxels),
                    DfExplorer.MESH_EXECUTOR
            );
            meshInvalidated = false;
        }

        private @Nullable VoxelChunk getVoxelsNow() {
            return voxels != null ? voxels.getNow(null) : null;
        }

        public @Nullable ChunkMesh getMeshOrUpload(GpuDevice device) {
            if (pendingMesh != null && pendingMesh.isDone()) {
                ChunkMesh.Data pendingMesh = this.pendingMesh.join();
                this.pendingMesh = null;
                if (pendingMesh != null) {
                    mesh = pendingMesh.upload(device);
                }
            }
            return mesh;
        }

        private void clearVoxels() {
            if (voxels != null) {
                voxels.cancel(false);
                voxels = null;
            }
        }

        private void clearMesh() {
            if (mesh != null) {
                mesh.close();
                mesh = null;
            }
        }

        private void clearPendingMesh() {
            if (pendingMesh != null) {
                pendingMesh.thenAccept(mesh -> {
                    if (mesh != null) {
                        mesh.close();
                    }
                });
                pendingMesh = null;
            }
        }

        public ChunkPos pos() {
            return Objects.requireNonNull(pos, "Chunk not initialized");
        }

        @Override
        public void close() {
            clearPendingMesh();
            clearMesh();
        }
    }
}
