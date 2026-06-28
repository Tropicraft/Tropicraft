package net.tropicraft.densityfunction.explorer;

import com.mojang.blaze3d.platform.NativeLibrariesBootstrap;
import com.mojang.blaze3d.systems.BackendCreationException;
import com.mojang.blaze3d.systems.GpuBackend;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.SurfaceException;
import com.mojang.blaze3d.vulkan.VulkanBackend;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.tropicraft.core.common.TropicraftPackRegistries;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DfExplorer {
    public static final int CHUNK_SIZE = 16;
    public static final int MIN_Y = -64;
    public static final int HEIGHT = 384;
    private static final long SEED = 123L;

    private static final String WINDOW_TITLE = "Density Function Explorer";
    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 900;

    public static final Executor GENERATE_EXECUTOR = Executors.newFixedThreadPool(14, Thread.ofPlatform().name("generate", 0).daemon().factory());
    public static final Executor MESH_EXECUTOR = Executors.newFixedThreadPool(2, Thread.ofPlatform().name("mesh", 0).daemon().factory());

    private static GeneratorInfo createGeneratorInfo() {
        HolderLookup.Provider registries = createRegistries();
        NoiseGeneratorSettings dimension = registries.getOrThrow(TropicraftNoiseGenSettings.TROPICS).value();
        return new GeneratorInfo(
                registries,
                dimension.noiseRouter().finalDensity(),
                dimension.seaLevel()
        );
    }

    public static void run() throws BackendCreationException, SurfaceException, IOException {
        GeneratorInfo initialGeneratorInfo = createGeneratorInfo();
        ChunkMap chunkMap = new ChunkMap(initialGeneratorInfo.createChunkGenerator());

        NativeLibrariesBootstrap.loadLibraries();
        RenderSystem.initRenderThread();

        if (GLFW.glfwPlatformSupported(GLFW.GLFW_PLATFORM_WAYLAND) && GLFW.glfwPlatformSupported(GLFW.GLFW_PLATFORM_X11)) {
            GLFW.glfwInitHint(GLFW.GLFW_PLATFORM, GLFW.GLFW_PLATFORM_X11);
        }

        GLFW.glfwInit();

        GpuBackend gpuBackend = new VulkanBackend();

        GLFW.glfwDefaultWindowHints();
        gpuBackend.setWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        long window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, 0, 0);

        Renderer renderer = new Renderer(window, gpuBackend, initialGeneratorInfo.oceanY());

        Camera camera = new Camera();
        camera.moveTo(0.0f, 200.0f, 0.0f);

        double lastFrameTime = GLFW.glfwGetTime();

        GLFW.glfwShowWindow(window);

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        boolean reloadPressed = false;

        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
                break;
            }

            int reloadKey = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_R);
            if (reloadKey == GLFW.GLFW_PRESS) {
                reloadPressed = true;
            } else if (reloadPressed) {
                GeneratorInfo newGeneratorInfo = createGeneratorInfo();
                chunkMap.setChunkGenerator(newGeneratorInfo.createChunkGenerator());
                renderer.setOceanY(newGeneratorInfo.oceanY());
                reloadPressed = false;
            }

            double time = GLFW.glfwGetTime();
            float deltaTime = (float) (time - lastFrameTime);
            lastFrameTime = time;

            camera.handleInput(window, deltaTime);

            chunkMap.update(
                    Mth.floor(camera.position().x()) / CHUNK_SIZE,
                    Mth.floor(camera.position().z()) / CHUNK_SIZE
            );

            renderer.renderFrame(camera, chunkMap);
        }

        renderer.close();
        GLFW.glfwDestroyWindow(window);
    }

    private static HolderLookup.Provider createRegistries() {
        HolderLookup.Provider vanillaRegistries = VanillaRegistries.createLookup();
        return RegistryPatchGenerator.createLookup(
                CompletableFuture.completedFuture(vanillaRegistries),
                TropicraftPackRegistries.createRegistrySet()
        ).join().full();
    }

    private record GeneratorInfo(
            HolderLookup.Provider registries,
            DensityFunction density,
            int seaLevel
    ) {
        public VoxelChunkGenerator createChunkGenerator() {
            return new VoxelChunkGenerator(CHUNK_SIZE, MIN_Y, HEIGHT, registries, SEED, density);
        }

        public float oceanY() {
            return seaLevel - 0.2f - MIN_Y;
        }
    }
}
