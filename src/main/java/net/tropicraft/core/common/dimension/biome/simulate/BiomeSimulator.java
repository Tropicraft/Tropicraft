package net.tropicraft.core.common.dimension.biome.simulate;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomeSimulator {
    private static final Map<ResourceKey<Biome>, Integer> COLORS = new HashMap<>();
    static {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        COLORS.put(TropicraftBiomes.TROPICS_OCEAN, 0x4eecdf);
        COLORS.put(TropicraftBiomes.TROPICS_RIVER, 0x4eecdf);
        COLORS.put(TropicraftBiomes.TROPICS_BEACH, 0xFADE55);
        COLORS.put(TropicraftBiomes.RAINFOREST_PLAINS, 0x056621);
        COLORS.put(TropicraftBiomes.TROPICS, 0x8DB360);
    }

    public static void main(String[] args) {

        init(new NoiseSimulationHelper(new Random().nextLong()));
    }

    public static void init(NoiseSimulationHelper sampler) {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> builder = ImmutableList.builder();
        new TropicraftBiomeBuilder().addBiomes(builder::add);

        Climate.ParameterList<ResourceKey<Biome>> params = new Climate.ParameterList<>(builder.build());

        BufferedImage image = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);

        Reference2IntOpenHashMap<ResourceKey<Biome>> map = new Reference2IntOpenHashMap<>();
        map.defaultReturnValue(0);

        for (int x = 0; x < 2048; x++) {
            if (x % 256 == 0) {
                System.out.println("Mapping... " + ((x / 256) / 8.0) * 100 + "%");
            }

            for (int z = 0; z < 2048; z++) {
                NoiseSampler.FlatNoiseData data = sampler.noiseData(x, z, Blender.empty());
                int ay = 0;
                for (int y = -8; y < 40; y++) {
                    double offset = sampler.offset(y * 8, data.terrainInfo());

                    if (offset < -4) {
                        ay = y + 3;
                        break;
                    }
                }

                ResourceKey<Biome> value = params.findValue(sampler.sample(x, ay, z), Biomes.THE_VOID);

                if (!COLORS.containsKey(value)) throw new RuntimeException("Resource key not found: " + value);

                map.addTo(value, 1);

                image.setRGB(x, z, COLORS.getOrDefault(value, 0));
            }
        }

        for (Reference2IntMap.Entry<ResourceKey<Biome>> entry : map.reference2IntEntrySet()) {
            System.out.println(entry.getKey().location() + ": " + (entry.getIntValue() / (2048.0 * 2048.0)) * 100 + "%");
        }

        Path p = Paths.get(".", "run", "biomes.png");
        try {
            ImageIO.write(image, "png", p.toAbsolutePath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}