package net.tropicraft.core.common.command.debug;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.command.CommandSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.command.Commands.literal;

public class MapBiomesCommand {
    private static final int SIZE = 4096;
    private static final int SIZE2 = SIZE / 2;
    private static final int SIZE8 = SIZE / 8;
    private static final Object2IntOpenHashMap<ResourceLocation> COLORS = new Object2IntOpenHashMap<>();

    static {
        COLORS.put(TropicraftBiomes.TROPICS.getLocation(), 0x7cde73);

        COLORS.put(TropicraftBiomes.RAINFOREST_PLAINS.getLocation(), 0x3fb535);
        COLORS.put(TropicraftBiomes.RAINFOREST_HILLS.getLocation(), 0x3fb535);
        COLORS.put(TropicraftBiomes.RAINFOREST_MOUNTAINS.getLocation(), 0x3fb535);
        COLORS.put(TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS.getLocation(), 0x3cc230);
        COLORS.put(TropicraftBiomes.OSA_RAINFOREST.getLocation(), 0x58d14d);
        COLORS.put(TropicraftBiomes.BAMBOO_RAINFOREST.getLocation(), 0x57c23c);

        COLORS.put(TropicraftBiomes.MANGROVES.getLocation(), 0x448733);
        COLORS.put(TropicraftBiomes.OVERGROWN_MANGROVES.getLocation(), 0x5d8733);

        COLORS.put(TropicraftBiomes.TROPICS_OCEAN.getLocation(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.TROPICS_RIVER.getLocation(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.KELP_FOREST.getLocation(), 0x4fc9af);
        COLORS.put(TropicraftBiomes.LAKE.getLocation(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.LAGOON.getLocation(), 0x73dee6);

        COLORS.put(TropicraftBiomes.TROPICS_BEACH.getLocation(), 0xe8e397);
    }

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal("mapbiomes")
                        .requires(s -> s.hasPermissionLevel(2))
                        .executes(c -> execute(c.getSource()))
        );
    }

    private static int execute(CommandSource source) {
        if (!source.getWorld().getDimensionKey().equals(TropicraftDimension.WORLD)) {
            source.sendErrorMessage(new StringTextComponent("Can't execute this in non-tropicraft world!"));
        }

        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

        MutableRegistry<Biome> biomes = source.getWorld().func_241828_r().getRegistry(Registry.BIOME_KEY);
        for (int x = -SIZE2; x < SIZE2; x++) {
            if (x % SIZE8 == 0) {
                source.sendFeedback(new StringTextComponent(((x + SIZE2) / (double)SIZE) * 100 + "%"), false);
            }

            for (int z = -SIZE2; z < SIZE2; z++) {

                Biome biome = source.getWorld().getNoiseBiomeRaw(x, 0, z);
                ResourceLocation name = biomes.getKey(biome);

                img.setRGB(x + SIZE2, z + SIZE2, COLORS.getOrDefault(name, 0xFFFFFF));
            }
        }

        Path p = Paths.get("biome_colors.png");
        try {
            ImageIO.write(img, "png", p.toAbsolutePath().toFile());
            source.sendFeedback(new StringTextComponent("Mapped biome colors!"), false);
        } catch (IOException e) {
            source.sendFeedback(new StringTextComponent("Something went wrong, check the log!"), true);
            e.printStackTrace();
        }

        return 0;
    }
}
