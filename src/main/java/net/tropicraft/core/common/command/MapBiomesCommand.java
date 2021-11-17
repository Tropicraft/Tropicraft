package net.tropicraft.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static net.minecraft.commands.Commands.literal;

public class MapBiomesCommand {
    private static final int SIZE = 4096;
    private static final int SIZE2 = SIZE / 2;
    private static final int SIZE8 = SIZE / 8;
    private static final Object2IntOpenHashMap<ResourceLocation> COLORS = new Object2IntOpenHashMap<>();

    static {
        COLORS.put(TropicraftBiomes.TROPICS.getRegistryName(), 0x7cde73);

        COLORS.put(TropicraftBiomes.RAINFOREST_PLAINS.getRegistryName(), 0x3fb535);
        COLORS.put(TropicraftBiomes.RAINFOREST_HILLS.getRegistryName(), 0x3fb535);
        COLORS.put(TropicraftBiomes.RAINFOREST_MOUNTAINS.getRegistryName(), 0x3fb535);
        COLORS.put(TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS.getRegistryName(), 0x3cc230);
        COLORS.put(TropicraftBiomes.OSA_RAINFOREST.getRegistryName(), 0x58d14d);
        COLORS.put(TropicraftBiomes.BAMBOO_RAINFOREST.getRegistryName(), 0x57c23c);

        COLORS.put(TropicraftBiomes.MANGROVES.getRegistryName(), 0x448733);
        COLORS.put(TropicraftBiomes.OVERGROWN_MANGROVES.getRegistryName(), 0x5d8733);

        COLORS.put(TropicraftBiomes.TROPICS_OCEAN.getRegistryName(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.TROPICS_RIVER.getRegistryName(), 0x4fc1c9);
        COLORS.put(TropicraftBiomes.KELP_FOREST.getRegistryName(), 0x4fc9af);

        COLORS.put(TropicraftBiomes.TROPICS_BEACH.getRegistryName(), 0xe8e397);
    }

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("mapbiomes")
                        .requires(s -> s.hasPermission(2))
                        .executes(c -> execute(c.getSource()))
        );
    }

    private static int execute(CommandSourceStack source) {
        //TODO [PORT]: MAY NOT BE WORKING

        if (!source.getLevel().dimension().equals(TropicraftDimension.WORLD)) {
            source.sendFailure(new TextComponent("Can't execute this in non-tropicraft world!"));
        }

        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);



        WritableRegistry<Biome> biomes = source.getLevel().registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);
        for (int x = -SIZE2; x < SIZE2; x++) {
            if (x % SIZE8 == 0) {
                source.sendSuccess(new TextComponent(((x + SIZE2) / (double)SIZE) * 100 + "%"), false);
            }

            for (int z = -SIZE2; z < SIZE2; z++) {

                Biome biome = source.getLevel().getNoiseBiome(x, 0, z);
                ResourceLocation name = biomes.getKey(biome);

                img.setRGB(x + SIZE2, z + SIZE2, COLORS.getOrDefault(name, 0xFFFFFF));
            }
        }

        Path p = Paths.get("biome_colors.png");
        try {
            ImageIO.write(img, "png", p.toAbsolutePath().toFile());
            source.sendSuccess(new TextComponent("Mapped biome colors!"), false);
        } catch (IOException e) {
            source.sendSuccess(new TextComponent("Something went wrong, check the log!"), true);
            e.printStackTrace();
        }

        return 0;
    }
}
