package net.tropicraft.core.common.command.debug;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.minecraft.commands.Commands.literal;

public class MapBiomesCommand {
    private static final int SIZE = 4096;
    private static final int SIZE2 = SIZE / 2;
    private static final int SIZE8 = SIZE / 8;
    private static final Object2IntOpenHashMap<Identifier> COLORS = new Object2IntOpenHashMap<>();

    static {
        COLORS.put(TropicraftBiomes.TROPICS.identifier(), 0x7cde73);

        COLORS.put(TropicraftBiomes.RAINFOREST.identifier(), 0x3fb535);
        COLORS.put(TropicraftBiomes.OSA_RAINFOREST.identifier(), 0x58d14d);
        COLORS.put(TropicraftBiomes.BAMBOO_RAINFOREST.identifier(), 0x57c23c);

        COLORS.put(TropicraftBiomes.MANGROVES.identifier(), 0x448733);
        COLORS.put(TropicraftBiomes.OVERGROWN_MANGROVES.identifier(), 0x5d8733);

        COLORS.put(TropicraftBiomes.OCEAN.identifier(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.RIVER.identifier(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.KELP_FOREST.identifier(), 0x4fc9af);

        COLORS.put(TropicraftBiomes.BEACH.identifier(), 0xe8e397);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("mapbiomes")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(c -> execute(c.getSource()))
        );
    }

    private static int execute(CommandSourceStack source) {
        if (!source.getLevel().dimension().equals(TropicraftDimension.WORLD)) {
            source.sendFailure(Component.literal("Can't execute this in non-tropicraft world!"));
        }

        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

        Registry<Biome> biomes = source.getLevel().registryAccess().lookupOrThrow(Registries.BIOME);
        for (int x = -SIZE2; x < SIZE2; x++) {
            if (x % SIZE8 == 0) {
                double progress = (x + SIZE2) / (double) SIZE;
                source.sendSuccess(() -> Component.literal(progress * 100 + "%"), false);
            }

            for (int z = -SIZE2; z < SIZE2; z++) {
                Biome biome = source.getLevel().getUncachedNoiseBiome(x, 0, z).value();
                Identifier name = biomes.getKey(biome);

                img.setRGB(x + SIZE2, z + SIZE2, COLORS.getOrDefault(name, 0xFFFFFF));
            }
        }

        Path p = Paths.get("biome_colors.png");
        try {
            ImageIO.write(img, "png", p.toAbsolutePath().toFile());
            source.sendSuccess(() -> Component.literal("Mapped biome colors!"), false);
        } catch (IOException e) {
            source.sendFailure(Component.literal("Something went wrong, check the log!"));
            e.printStackTrace();
        }

        return 0;
    }
}
