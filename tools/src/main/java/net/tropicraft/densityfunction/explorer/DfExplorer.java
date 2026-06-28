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

    private static HolderLookup.Provider createRegistries() {
        HolderLookup.Provider vanillaRegistries = VanillaRegistries.createLookup();
        return RegistryPatchGenerator.createLookup(
                CompletableFuture.completedFuture(vanillaRegistries),
                TropicraftPackRegistries.createRegistrySet()
        ).join().full();
    }

    public static void run() throws BackendCreationException, SurfaceException, IOException {
        HolderLookup.Provider registries = createRegistries();

        NoiseGeneratorSettings dimension = registries.getOrThrow(TropicraftNoiseGenSettings.TROPICS).value();
        DensityFunction density = dimension.noiseRouter().finalDensity();
        float oceanY = dimension.seaLevel() - 0.2f - MIN_Y;

        VoxelChunkGenerator chunkGenerator = new VoxelChunkGenerator(CHUNK_SIZE, MIN_Y, HEIGHT, registries, SEED, density);
        ChunkMap chunkMap = new ChunkMap(chunkGenerator);

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

        Renderer renderer = new Renderer(window, gpuBackend, oceanY);

        Camera camera = new Camera();
        camera.moveTo(0.0f, 200.0f, 0.0f);

        double lastFrameTime = GLFW.glfwGetTime();

        GLFW.glfwShowWindow(window);

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
                break;
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
}
