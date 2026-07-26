package net.tropicraft.densityfunction;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.ColorRamp;
import net.tropicraft.ColorRamps;
import net.tropicraft.core.common.TropicraftPackRegistries;
import net.tropicraft.core.common.dimension.df.TropicraftDensityFunctions;
import net.tropicraft.map.MapController;
import net.tropicraft.map.MapPanel;
import net.tropicraft.map.feature.MapFeature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public class DfMap {
    private static final ForkJoinPool EXECUTOR = ForkJoinPool.commonPool();

    private static final long SEED = 123L;
    private static final PositionalRandomFactory RANDOM_FACTORY = RandomSource.create(SEED).forkPositional();

    public static void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame("Density Function Mapper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);

        JPanel leftSideBar = new JPanel();
        JList<FeatureEntry> featureList = new JList<>(createFeatures().toArray(FeatureEntry[]::new));
        featureList.setSelectedIndex(0);
        leftSideBar.add(featureList);

        MapController mapController = new MapController();
        MapPanel map = new MapPanel(mapController, featureList.getSelectedValue().createFeature(ColorRamps.GRAYSCALE));
        featureList.addListSelectionListener(_ -> {
            FeatureEntry selectedFeature = featureList.getSelectedValue();
            if (selectedFeature != null) {
                map.setFeature(selectedFeature.createFeature(ColorRamps.GRAYSCALE));
            }
        });
        featureList.setFocusable(false);

        frame.setFocusable(true);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    FeatureEntry[] newFeatures = createFeatures().toArray(new FeatureEntry[0]);
                    int oldIndex = featureList.getSelectedIndex();
                    featureList.setListData(newFeatures);
                    if (newFeatures.length > 0) {
                        featureList.setSelectedIndex(Math.min(oldIndex, newFeatures.length - 1));
                    }
                }
            }
        });

        frame.getContentPane().add(BorderLayout.WEST, leftSideBar);
        frame.getContentPane().add(BorderLayout.CENTER, map);

        frame.setVisible(true);
    }

    private static List<FeatureEntry> createFeatures() {
        HolderLookup.Provider registries = createRegistries();
        HolderLookup<DensityFunction> functions = registries.lookupOrThrow(Registries.DENSITY_FUNCTION);

        return List.of(
                create2dFeature(functions, TropicraftDensityFunctions.OFFSET, -1.0f, 1.0f),
                create2dFeature(functions, TropicraftDensityFunctions.CONTINENTS, -1.0f, 1.0f),
                create2dFeature(functions, NoiseRouterData.OFFSET, -0.75f, 1.0f),
                create2dFeature(functions, NoiseRouterData.FACTOR, 0.0f, 10.0f),
                create2dFeature(functions, NoiseRouterData.CONTINENTS, -1.0f, 1.0f),
                create2dFeature(functions, NoiseRouterData.EROSION, -1.0f, 1.0f),
                create2dFeature(functions, NoiseRouterData.RIDGES, -1.0f, 1.0f)
        );
    }

    private static FeatureEntry create2dFeature(HolderLookup<DensityFunction> functions, ResourceKey<DensityFunction> id, float minValue, float maxValue) {
        DensityFunction function = functions.getOrThrow(id).value().mapAll(new DensityFunction.Visitor() {
            @Override
            public DensityFunction apply(DensityFunction input) {
                return input instanceof DensityFunctions.HolderHolder(Holder<DensityFunction> inner) ? inner.value() : input;
            }

            @Override
            public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
                Holder<NormalNoise.NoiseParameters> parameters = noise.noiseData();
                RandomSource random = RANDOM_FACTORY.fromHashOf(parameters.getRegisteredName());
                return new DensityFunction.NoiseHolder(parameters, NormalNoise.create(random, parameters.value()));
            }
        });
        return new FeatureEntry(id.identifier().toString(), ramp ->
                (zoomLevel, x0, y0, x1, y1, width, height) -> CompletableFuture.supplyAsync(() -> {
                    double[] output = fillTile(function, zoomLevel, x0, y0, width, height);
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            image.setRGB(x, y, ramp.get((float) output[x + y * width]));
                        }
                    }
                    return image;
                }, EXECUTOR),
                minValue, maxValue
        );
    }

    private static double[] fillTile(DensityFunction function, int zoomLevel, int x0, int y0, int width, int height) {
        double[] output = new double[width * height];
        function.fillArray(output, new DensityFunction.ContextProvider() {
            @Override
            public DensityFunction.FunctionContext forIndex(int index) {
                return new DensityFunction.SinglePointContext(
                        x0 + (index % width) << zoomLevel,
                        0,
                        y0 + (index / width) << zoomLevel
                );
            }

            @Override
            public void fillAllDirectly(double[] output, DensityFunction function) {
                for (int z = 0; z < width; z++) {
                    for (int x = 0; x < width; x++) {
                        output[x + z * width] = function.compute(new DensityFunction.SinglePointContext(
                                x0 + x << zoomLevel,
                                0,
                                y0 + z << zoomLevel
                        ));
                    }
                }
            }
        });
        return output;
    }

    private static HolderLookup.Provider createRegistries() {
        HolderLookup.Provider vanillaRegistries = VanillaRegistries.createLookup();
        return RegistryPatchGenerator.createLookup(
                CompletableFuture.completedFuture(vanillaRegistries),
                TropicraftPackRegistries.createRegistrySet()
        ).join().full();
    }

    private record FeatureEntry(String name, Function<ColorRamp, MapFeature> feature, float minValue, float maxValue) {
        public MapFeature createFeature(ColorRamp ramp) {
            return feature.apply(ramp.remap(0.0f, 1.0f, minValue, maxValue));
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
