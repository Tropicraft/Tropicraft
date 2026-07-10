package net.tropicraft.densityfunction.explorer;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public record ChunkMesh(
        GpuBuffer vertexBuffer,
        int vertexCount,
        int indexCount
) implements AutoCloseable {
    @Override
    public void close() {
        vertexBuffer.close();
    }

    public static class Data implements AutoCloseable {
        private final ByteBuffer vertexBuffer;
        private final int vertexCount;
        private boolean closed;

        private Data(ByteBuffer vertexBuffer, int vertexCount) {
            this.vertexBuffer = vertexBuffer;
            this.vertexCount = vertexCount;
        }

        public static @Nullable Data generate(VoxelChunk chunk, VoxelChunk westChunk, VoxelChunk eastChunk, VoxelChunk northChunk, VoxelChunk southChunk) {
            class MeshOutput implements VoxelMesher.Output {
                private static final int QUAD_SIZE = Vertex.SIZE * 4;
                private static final int INITIAL_BUFFER_CAPACITY = QUAD_SIZE * 256 * 2;

                private @Nullable ByteBuffer buffer;
                private int vertexCount;

                private ByteBuffer ensureCapacity(int bytes) {
                    if (buffer == null || buffer.remaining() < bytes) {
                        buffer = MemoryUtil.memRealloc(buffer, buffer == null ? INITIAL_BUFFER_CAPACITY : buffer.capacity() * 2);
                    }
                    return buffer;
                }

                @Override
                public void addFace(int x0, int y0, int z0, int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int color) {
                    ByteBuffer buffer = ensureCapacity(QUAD_SIZE);
                    Vertex.put(buffer, x0, y0, z0, color);
                    Vertex.put(buffer, x1, y1, z1, color);
                    Vertex.put(buffer, x2, y2, z2, color);
                    Vertex.put(buffer, x3, y3, z3, color);
                    vertexCount += 4;
                }
            }
            MeshOutput output = new MeshOutput();
            VoxelMesher.generate(chunk, westChunk, eastChunk, northChunk, southChunk, output);
            if (output.buffer == null) {
                return null;
            }
            return new Data(output.buffer.flip(), output.vertexCount);
        }

        public ChunkMesh upload(GpuDevice device) {
            GpuBuffer gpuVertexBuffer = device.createBuffer(() -> "Chunk", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, vertexBuffer);
            close();
            return new ChunkMesh(
                    gpuVertexBuffer,
                    vertexCount,
                    PrimitiveTopology.QUADS.indexCount(vertexCount)
            );
        }

        @Override
        public void close() {
            if (closed) {
                return;
            }
            closed = true;
            MemoryUtil.memFree(vertexBuffer);
        }
    }

    public static class Vertex {
        public static final VertexFormat FORMAT = VertexFormat.builder(0)
                .addAttribute("a_Pos", GpuFormat.RGBA8_UINT)
                .addAttribute("a_Color", GpuFormat.RGBA8_UNORM)
                .build();
        public static final int SIZE = FORMAT.getVertexSize();

        public static void put(ByteBuffer buffer, int x0, int y0, int z0, int color) {
            buffer.put((byte) x0)
                    .put((byte) (y0 & 0xff))
                    .put((byte) z0)
                    .put((byte) (y0 >> 8 & 0xff))
                    .put((byte) ARGB.red(color))
                    .put((byte) ARGB.green(color))
                    .put((byte) ARGB.blue(color))
                    .put((byte) ARGB.alpha(color));
        }
    }

    public static class Instance {
        public static final VertexFormat FORMAT = VertexFormat.builder(1)
                .addAttribute("a_ChunkOffset", GpuFormat.RG16_SINT)
                .build();
        public static final int SIZE = FORMAT.getVertexSize();

        public static void put(ByteBuffer buffer, int x, int z) {
            buffer.putShort((short) x).putShort((short) z);
        }
    }
}
