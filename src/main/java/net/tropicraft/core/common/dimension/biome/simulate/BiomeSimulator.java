package net.tropicraft.core.common.dimension.biome.simulate;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// TODO: Currently does not run due to missing registry data
public class BiomeSimulator {
    private static final Map<ResourceKey<Biome>, Integer> COLORS = new HashMap<>();
    static {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        COLORS.put(biomeKey("ocean"), 0x4eecdf);
        COLORS.put(biomeKey("river"), 0x4eecdf);
        COLORS.put(biomeKey("beach"), 0xFADE55);
        COLORS.put(biomeKey("rainforest"), 0x056621);
        COLORS.put(biomeKey("bamboo_rainforest"), 0x57c23c);
        // TODO: colors too close to each other
        COLORS.put(biomeKey("osa_rainforest"), 0x58d14d);
        COLORS.put(biomeKey("tropics"), 0x8DB360);
        COLORS.put(biomeKey("mangroves"), 0x528a50);
        COLORS.put(biomeKey("overgrown_mangroves"), 0x5d8733);
    }

    private static ResourceKey<Biome> biomeKey(String name) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Constants.MODID, name));
    }

    public static void main(String[] args) {
        long seed = new Random(101).nextLong();
        init(new NoiseSimulationHelper(seed));
    }

    public static void init(NoiseSimulationHelper sampler) {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> builder = ImmutableList.builder();
        new TropicraftBiomeBuilder().addBiomes((point, biome) -> builder.add(Pair.of(point, biome.getKey())));

        Climate.ParameterList<ResourceKey<Biome>> params = new Climate.ParameterList<>(builder.build());


        int size = 1024;
        boolean generateTerrainData = true;


        int size8 = size / 8;
        BufferedImage biomeMap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        BufferedImage baseDepthMap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        BufferedImage biomeDepth = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        BufferedImage biomeScale = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        BufferedImage biomePeaks = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        Reference2IntOpenHashMap<ResourceKey<Biome>> map = new Reference2IntOpenHashMap<>();
        map.defaultReturnValue(0);

        for (int x = 0; x < size; x++) {
            if (x % size8 == 0) {
                System.out.println("Mapping... " + ((x / size8) / 8.0) * 100 + "%");
            }

            for (int z = 0; z < size; z++) {
//                NoiseSampler.FlatNoiseData data = sampler.noiseData(x, z, Blender.empty());
//                int ay = 0;
//                // Find topmost Y coord
//                for (int y = -8; y < 40; y++) {
//                    double offset = sampler.offset(y * 8, data.terrainInfo());
//
//                    if (offset < -4) {
//                        ay = y + 3;
//                        break;
//                    }
//                }

                // TODO: when we have cave biomes, we need to make a top map and a bottom map

                int y = 40;
                ResourceKey<Biome> value = params.findValue(sampler.sample(x, y, z));

                if (!COLORS.containsKey(value)) throw new RuntimeException("Resource key not found: " + value);

                map.addTo(value, 1);

                biomeMap.setRGB(x, z, COLORS.getOrDefault(value, 0));

                if (generateTerrainData) {
                    int prelimSurface = sampler.prelimSurfaceLevel(x, z); // needs to be mapped [0, 255]?
                    int o = (int) (Mth.clampedMap((sampler.offset(x, 40, z)), -0.3, 1.2, 0, 255));
                    int f = (int) ((sampler.factor(x, 40, z)) * 35);
                    int pv = (int) ((sampler.peaksAndValleys(x, y, z)) * 127) + 128;
                    baseDepthMap.setRGB(x, z, getIntFromColor(prelimSurface, prelimSurface, prelimSurface));
                    biomeDepth.setRGB(x, z, getIntFromColor(o, o, o));
                    biomeScale.setRGB(x, z, getIntFromColor(f, f, f));
                    biomePeaks.setRGB(x, z, getIntFromColor(pv, pv, pv));
                }
            }
        }

        for (Reference2IntMap.Entry<ResourceKey<Biome>> entry : map.reference2IntEntrySet()) {
            System.out.println(entry.getKey().location() + ": " + (entry.getIntValue() / (((double)size) * ((double) size))) * 100 + "%");
        }

        Path p = Paths.get(".", "run");
        try {
            ImageIO.write(biomeMap, "png", p.resolve("biomes.png").toAbsolutePath().toFile());
            if (generateTerrainData) {
                ImageIO.write(baseDepthMap, "png", p.resolve("basedepth.png").toAbsolutePath().toFile());
                ImageIO.write(biomeDepth, "png", p.resolve("biomedepth.png").toAbsolutePath().toFile());
                ImageIO.write(biomeScale, "png", p.resolve("biomescale.png").toAbsolutePath().toFile());
                ImageIO.write(biomePeaks, "png", p.resolve("biomepeaks.png").toAbsolutePath().toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getIntFromColor(int red, int green, int blue) {
        red = Mth.clamp(red, 0, 255);
        green = Mth.clamp(green, 0, 255);
        blue = Mth.clamp(blue, 0, 255);

        red = (red << 16) & 0x00FF0000; // Shift red 16-bits and mask out other stuff
        green = (green << 8) & 0x0000FF00; // Shift green 8-bits and mask out other stuff
        blue = blue & 0x000000FF; // Mask out anything not blue.

        return 0xFF000000 | red | green | blue; // 0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
