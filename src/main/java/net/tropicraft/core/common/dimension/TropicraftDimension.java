package net.tropicraft.core.common.dimension;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProvider;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "tropics");

    public static final RegistryKey<World> WORLD = RegistryKey.create(Registry.DIMENSION_REGISTRY, ID);
    public static final RegistryKey<Dimension> DIMENSION = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, ID);
    public static final RegistryKey<DimensionSettings> DIMENSION_SETTINGS = RegistryKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, ID);

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            if (world.dimension() == World.OVERWORLD) {
                upgradeTropicraftDimension(world.getServer());
            }
        }
    }

    private static void upgradeTropicraftDimension(MinecraftServer server) {
        // forge put dimensions in a different place to where vanilla does with its custom dimension support
        // we need to move our old data to the correct place if it exists

        SaveFormat.LevelSave save = server.storageSource;

        File oldDimension = save.getLevelPath(new FolderName("tropicraft/tropics")).toFile();
        File newDimension = save.getDimensionPath(WORLD);
        if (oldDimension.exists() && !newDimension.exists()) {
            try {
                FileUtils.moveDirectory(oldDimension, newDimension);
            } catch (IOException e) {
                LOGGER.error("Failed to move old tropicraft dimension to new location!", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void addDefaultDimensionKey() {
        try {
            Field dimensionKeysField = ObfuscationReflectionHelper.findField(Dimension.class, "BUILTIN_ORDER");
            LinkedHashSet<RegistryKey<Dimension>> keys = (LinkedHashSet<RegistryKey<Dimension>>) dimensionKeysField.get(null);
            keys.add(DIMENSION);
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Failed to add tropics as a default dimension key", e);
        }
    }

    public static Dimension createDimension(
            Registry<DimensionType> dimensionTypeRegistry,
            Registry<Biome> biomeRegistry,
            Registry<DimensionSettings> dimensionSettingsRegistry,
            long seed
    ) {
        Supplier<DimensionType> dimensionType = () -> dimensionTypeRegistry.getOrThrow(TropicraftDimension.DIMENSION_TYPE);
        ChunkGenerator generator = TropicraftDimension.createGenerator(biomeRegistry, dimensionSettingsRegistry, seed);

        return new Dimension(dimensionType, generator);
    }

    public static ChunkGenerator createGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed) {
        Supplier<DimensionSettings> dimensionSettings = () -> {
            // fallback to overworld so that we don't crash before our datapack is loaded (horrible workaround)
            DimensionSettings settings = dimensionSettingsRegistry.get(DIMENSION_SETTINGS);
            return settings != null ? settings : dimensionSettingsRegistry.getOrThrow(DimensionSettings.OVERWORLD);
        };
        TropicraftBiomeProvider biomeSource = new TropicraftBiomeProvider(seed, biomeRegistry);
        return new TropicraftChunkGenerator(biomeSource, seed, dimensionSettings);
    }

    public static void teleportPlayer(ServerPlayerEntity player, RegistryKey<World> dimensionType) {
        if (player.level.dimension() == dimensionType) {
            teleportPlayerNoPortal(player, World.OVERWORLD);
        } else {
            teleportPlayerNoPortal(player, dimensionType);
        }
    }

    /**
     * Finds the top Y position relative to the dimension the player is teleporting to and places
     * the entity at that position. Avoids portal generation by using player.teleport() instead of
     * player.changeDimension()
     *
     * @param player The player that will be teleported
     * @param destination The target dimension to teleport to
     */
    public static void teleportPlayerNoPortal(ServerPlayerEntity player, RegistryKey<World> destination) {
        ServerWorld world = player.server.getLevel(destination);
        if (world == null) {
            LOGGER.error("Cannot teleport player to dimension {} as it does not exist!", destination.location());
            return;
        }

        if (!ForgeHooks.onTravelToDimension(player, destination)) return;

        int x = MathHelper.floor(player.getX());
        int z = MathHelper.floor(player.getZ());

        Chunk chunk = world.getChunk(x >> 4, z >> 4);
        int topY = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, x & 15, z & 15);
        player.teleportTo(world, x + 0.5, topY + 1.0, z + 0.5, player.yRot, player.xRot);

        BasicEventHooks.firePlayerChangedDimensionEvent(player, destination, destination);
    }

    // hack to get the correct sea level given a world: the vanilla IWorldReader.getSeaLevel() is deprecated and always returns 63 despite the chunk generator
    public static int getSeaLevel(IWorldReader world) {
        if (world instanceof ServerWorld) {
            ServerChunkProvider chunkProvider = ((ServerWorld) world).getChunkSource();
            return chunkProvider.getGenerator().getSeaLevel();
        } else if (world instanceof World) {
            RegistryKey<World> dimensionKey = ((World) world).dimension();
            if (dimensionKey == WORLD) {
                return 127;
            }
        }
        return world.getSeaLevel();
    }
}
