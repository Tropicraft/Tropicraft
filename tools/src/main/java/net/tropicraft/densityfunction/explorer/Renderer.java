package net.tropicraft.densityfunction.explorer;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.GpuDebugOptions;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.BackendCreationException;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuBackend;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.GpuSurface;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.SurfaceException;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class Renderer implements AutoCloseable {
    private static final Vector4fc CLEAR_COLOR = new Vector4f(0.3f, 0.3f, 0.6f, 1.0f);

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000.0f;

    private static final Vector4fc OCEAN_COLOR = new Vector4f(0.2f, 0.2f, 1.0f, 0.7f);
    private static final float OCEAN_SIZE = ChunkMap.SIZE * DfExplorer.CHUNK_SIZE;

    private static final String CHUNK_VERTEX_SHADER = """
            #version 330
            
            in ivec4 a_Pos;
            in vec4 a_Color;
            
            layout(std140) uniform Chunk {
                ivec2 u_ChunkOffset[CHUNK_COUNT];
            };
            
            layout(std140) uniform Camera {
                mat4 u_ViewProjMat;
            };
            
            out vec4 v_Color;
            
            void main() {
                ivec2 chunkOffset = u_ChunkOffset[gl_InstanceID];
                ivec3 position = ivec3(
                    a_Pos.x,
                    a_Pos.w << 8 | a_Pos.y,
                    a_Pos.z
                ) + ivec3(chunkOffset.x, 0, chunkOffset.y);
            
                gl_Position = u_ViewProjMat * vec4(vec3(position), 1.0);
                v_Color = a_Color;
            }
            """;
    private static final String CHUNK_FRAGMENT_SHADER = """
            #version 330
            
            in vec4 v_Color;
            
            void main() {
                gl_FragColor = v_Color;
            }
            """;

    private static final String OCEAN_VERTEX_SHADER = """
            #version 330
            
            layout(std140) uniform Ocean {
                vec4 u_OceanColor;
                float u_OceanY;
                float u_OceanSize;
            };
            layout(std140) uniform Camera {
                mat4 u_ViewProjMat;
            };
            
            out vec4 v_Color;
            
            const vec2[] VERTICES = vec2[](
                vec2(-0.5, -0.5),
                vec2(-0.5, 0.5),
                vec2(0.5, 0.5),
                vec2(0.5, -0.5)
            );
            
            void main() {
                vec2 vertex = VERTICES[gl_VertexID];
                gl_Position = u_ViewProjMat * vec4(
                    vertex.x * u_OceanSize,
                    u_OceanY,
                    vertex.y * u_OceanSize,
                    1.0
                );
                v_Color = u_OceanColor;
            }
            """;
    private static final String OCEAN_FRAGMENT_SHADER = """
            #version 330
            
            void main() {
                gl_FragColor = vec4(0.3, 0.3, 1.0, 0.8);
            }
            """;

    private static final ShaderSource SHADER_SOURCE = (id, type) -> switch (id.getPath()) {
        case "chunk" -> switch (type) {
            case VERTEX -> CHUNK_VERTEX_SHADER;
            case FRAGMENT -> CHUNK_FRAGMENT_SHADER;
        };
        case "ocean" -> switch (type) {
            case VERTEX -> OCEAN_VERTEX_SHADER;
            case FRAGMENT -> OCEAN_FRAGMENT_SHADER;
        };
        default -> null;
    };

    private static final int CAMERA_UBO_SIZE = new Std140SizeCalculator()
            .putMat4f()
            .get();
    private static final int CHUNK_UBO_SIZE = Float.BYTES * 4 * ChunkMap.COUNT;
    private static final int OCEAN_UBO_SIZE = new Std140SizeCalculator()
            .putVec4()
            .putFloat()
            .putFloat()
            .get();

    private static final BindGroupLayout CHUNK_BIND_GROUP_LAYOUT = BindGroupLayout.builder()
            .withUniform("Chunk", UniformType.UNIFORM_BUFFER)
            .withUniform("Camera", UniformType.UNIFORM_BUFFER)
            .build();
    private static final RenderPipeline CHUNK_PIPELINE = RenderPipeline.builder()
            .withLocation("chunk")
            .withVertexShader("chunk")
            .withFragmentShader("chunk")
            .withShaderDefine("CHUNK_COUNT", ChunkMap.COUNT)
            .withVertexBinding(0, ChunkMesh.Vertex.FORMAT)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .withBindGroupLayout(CHUNK_BIND_GROUP_LAYOUT)
            .withCull(true)
            .build();

    private static final BindGroupLayout OCEAN_BIND_GROUP_LAYOUT = BindGroupLayout.builder()
            .withUniform("Ocean", UniformType.UNIFORM_BUFFER)
            .withUniform("Camera", UniformType.UNIFORM_BUFFER)
            .build();
    private static final RenderPipeline OCEAN_PIPELINE = RenderPipeline.builder()
            .withLocation("ocean")
            .withVertexShader("ocean")
            .withFragmentShader("ocean")
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .withBindGroupLayout(OCEAN_BIND_GROUP_LAYOUT)
            .withCull(false)
            .build();

    private final GpuDevice device;
    private final GpuSurface surface;
    private final GpuTexture mainColorTexture;
    private final GpuTexture mainDepthTexture;
    private final GpuTextureView mainColorTextureView;
    private final GpuTextureView mainDepthTextureView;

    private final MappableRingBuffer cameraUniformBuffer;
    private final Matrix4fc projectionMatrix;

    private final GpuBuffer chunkUniformBuffer;
    private int lastCenterChunkX = Integer.MIN_VALUE;
    private int lastCenterChunkZ = Integer.MIN_VALUE;

    private GpuBuffer oceanUniformBuffer;

    public Renderer(long window, GpuBackend gpuBackend, float oceanY) throws BackendCreationException, SurfaceException {
        GpuDebugOptions debugOptions = new GpuDebugOptions(0, false, false, false);
        device = gpuBackend.createDevice(window, SHADER_SOURCE, debugOptions, () -> {
        });
        RenderSystem.initRenderer(device);

        if (!device.getDeviceInfo().features().nonZeroFirstInstance()) {
            throw new IllegalStateException("Device does not support non-zero first instance");
        }

        int windowWidth = DfExplorer.WINDOW_WIDTH;
        int windowHeight = DfExplorer.WINDOW_HEIGHT;
        surface = device.createSurface(window);
        GpuSurface.PresentMode presentMode = GpuSurface.PresentMode.getSupportedVsyncMode(surface.supportedPresentModes(), true);
        surface.configure(new GpuSurface.Configuration(windowWidth, windowHeight, presentMode));

        mainColorTexture = device.createTexture("Main Color", GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_COPY_DST, GpuFormat.RGBA8_UNORM, windowWidth, windowHeight, 1, 1);
        mainDepthTexture = device.createTexture("Main Depth", GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_COPY_DST, GpuFormat.D32_FLOAT, windowWidth, windowHeight, 1, 1);

        mainColorTextureView = device.createTextureView(mainColorTexture);
        mainDepthTextureView = device.createTextureView(mainDepthTexture);

        cameraUniformBuffer = new MappableRingBuffer(() -> "Camera", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE, CAMERA_UBO_SIZE);
        projectionMatrix = new Matrix4f().perspective(70.0f * Mth.DEG_TO_RAD, (float) windowWidth / windowHeight, Z_NEAR, Z_FAR, device.getDeviceInfo().isZZeroToOne());

        chunkUniformBuffer = device.createBuffer(() -> "Chunk Info", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, CHUNK_UBO_SIZE);

        oceanUniformBuffer = createOceanUniformBuffer(device, oceanY);
    }

    private static GpuBuffer createOceanUniformBuffer(GpuDevice device, float oceanY) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer oceanBuffer = stack.malloc(OCEAN_UBO_SIZE)
                    .putFloat(OCEAN_COLOR.x()).putFloat(OCEAN_COLOR.y()).putFloat(OCEAN_COLOR.z()).putFloat(OCEAN_COLOR.z())
                    .putFloat(oceanY)
                    .putFloat(OCEAN_SIZE)
                    .flip();
            return device.createBuffer(() -> "Ocean", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, oceanBuffer);
        }
    }

    public void setOceanY(float oceanY) {
        oceanUniformBuffer.close();
        oceanUniformBuffer = createOceanUniformBuffer(device, oceanY);
    }

    public void renderFrame(Camera camera, ChunkMap chunkMap) throws SurfaceException {
        CommandEncoder commandEncoder = device.createCommandEncoder();
        commandEncoder.clearColorAndDepthTextures(mainColorTexture, CLEAR_COLOR, mainDepthTexture, Z_FAR);

        int centerChunkX = chunkMap.centerX();
        int centerChunkZ = chunkMap.centerZ();
        if (centerChunkX != lastCenterChunkX || centerChunkZ != lastCenterChunkZ) {
            updateChunkBuffer(commandEncoder, centerChunkX, centerChunkZ, chunkMap.getChunkPositions());
            lastCenterChunkX = centerChunkX;
            lastCenterChunkZ = centerChunkZ;
        }

        Matrix4f viewMatrix = new Matrix4f()
                .rotate(camera.rotation().conjugate(new Quaternionf()))
                .translate(-(camera.position().x() - centerChunkX * DfExplorer.CHUNK_SIZE), -camera.position().y(), -(camera.position().z() - centerChunkZ * DfExplorer.CHUNK_SIZE));
        Matrix4f viewProjMatrix = projectionMatrix.mul(viewMatrix, new Matrix4f());

        try (GpuBufferSlice.MappedView view = cameraUniformBuffer.currentBuffer().map(false, true)) {
            Std140Builder.intoBuffer(view.data()).putMat4f(viewProjMatrix);
        }

        renderChunks(chunkMap);
        renderOcean();

        cameraUniformBuffer.rotate();

        surface.acquireNextTexture();
        surface.blitFromTexture(commandEncoder, mainColorTextureView);
        commandEncoder.submit();
        surface.present();
    }

    private void updateChunkBuffer(CommandEncoder commandEncoder, int centerChunkX, int centerChunkZ, List<ChunkPos> chunkPositions) {
        GpuBufferSlice stagingChunkBuffer;
        try (GpuBufferSlice.MappedView view = commandEncoder.transientMemory().allocateStaging(chunkUniformBuffer.size(), 1, GpuBuffer.USAGE_COPY_SRC)) {
            stagingChunkBuffer = view.slice();
            Std140Builder builder = Std140Builder.intoBuffer(view.data());
            for (ChunkPos pos : chunkPositions) {
                builder.align(16);
                builder.putIVec2((pos.x() - centerChunkX) * DfExplorer.CHUNK_SIZE, (pos.z() - centerChunkZ) * DfExplorer.CHUNK_SIZE);
            }
        }
        commandEncoder.copyToBuffer(stagingChunkBuffer, chunkUniformBuffer.slice());
    }

    private void renderChunks(ChunkMap chunkMap) {
        int maxIndexCount = 0;

        List<@Nullable ChunkMesh> meshes = chunkMap.getOrUploadMeshes(device);
        for (ChunkMesh mesh : meshes) {
            if (mesh != null) {
                maxIndexCount = Math.max(mesh.indexCount(), maxIndexCount);
            }
        }
        if (maxIndexCount == 0) {
            return;
        }

        RenderSystem.AutoStorageIndexBuffer autoIndexBuffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer indexBuffer = autoIndexBuffer.getBuffer(maxIndexCount);

        try (RenderPass renderPass = device.createCommandEncoder().createRenderPass(() -> "Chunks", mainColorTextureView, Optional.empty(), mainDepthTextureView, OptionalDouble.empty())) {
            renderPass.setPipeline(CHUNK_PIPELINE);
            renderPass.setIndexBuffer(indexBuffer, autoIndexBuffer.type());

            renderPass.setUniform("Camera", cameraUniformBuffer.currentBuffer());
            renderPass.setUniform("Chunk", chunkUniformBuffer.slice());

            for (int i = 0; i < meshes.size(); i++) {
                ChunkMesh mesh = meshes.get(i);
                if (mesh == null) {
                    continue;
                }
                renderPass.setVertexBuffer(0, mesh.vertexBuffer().slice());
                renderPass.drawIndexed(mesh.indexCount(), 1, 0, 0, i);
            }
        }
    }

    private void renderOcean() {
        RenderSystem.AutoStorageIndexBuffer autoIndexBuffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer indexBuffer = autoIndexBuffer.getBuffer(6);

        try (RenderPass renderPass = device.createCommandEncoder().createRenderPass(() -> "Ocean", mainColorTextureView, Optional.empty(), mainDepthTextureView, OptionalDouble.empty())) {
            renderPass.setPipeline(OCEAN_PIPELINE);
            renderPass.setIndexBuffer(indexBuffer, autoIndexBuffer.type());

            renderPass.setUniform("Camera", cameraUniformBuffer.currentBuffer());
            renderPass.setUniform("Ocean", oceanUniformBuffer.slice());

            renderPass.drawIndexed(6, 1, 0, 0, 0);
        }
    }

    @Override
    public void close() {
        mainColorTextureView.close();
        mainDepthTextureView.close();
        mainColorTexture.close();
        mainDepthTexture.close();
        surface.close();
        // Closes device
        RenderSystem.shutdownRenderer();
    }
}
