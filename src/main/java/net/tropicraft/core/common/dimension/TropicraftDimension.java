package net.tropicraft.core.common.dimension;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.core.Registry;
import net.minecraft.world.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
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

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "tropics");

    public static final ResourceKey<Level> WORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, ID);
    public static final ResourceKey<LevelStem> DIMENSION = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, ID);
    public static final ResourceKey<NoiseGeneratorSettings> DIMENSION_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, ID);

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getWorld();
            if (world.dimension() == Level.OVERWORLD) {
                upgradeTropicraftDimension(world.getServer());
            }
        }
    }

    private static void upgradeTropicraftDimension(MinecraftServer server) {
        // forge put dimensions in a different place to where vanilla does with its custom dimension support
        // we need to move our old data to the correct place if it exists

        LevelStorageSource.LevelStorageAccess save = server.storageSource;

        File oldDimension = save.getLevelPath(new LevelResource("tropicraft/tropics")).toFile();
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
            Field dimensionKeysField = ObfuscationReflectionHelper.findField(LevelStem.class, "BUILTIN_ORDER");
            LinkedHashSet<ResourceKey<LevelStem>> keys = (LinkedHashSet<ResourceKey<LevelStem>>) dimensionKeysField.get(null);
            keys.add(DIMENSION);
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Failed to add tropics as a default dimension key", e);
        }
    }

    public static LevelStem createDimension(
            Registry<DimensionType> dimensionTypeRegistry,
            Registry<Biome> biomeRegistry,
            Registry<NoiseGeneratorSettings> dimensionSettingsRegistry,
            long seed
    ) {
        Supplier<DimensionType> dimensionType = () -> dimensionTypeRegistry.getOrThrow(TropicraftDimension.DIMENSION_TYPE);
        ChunkGenerator generator = TropicraftDimension.createGenerator(biomeRegistry, dimensionSettingsRegistry, seed);

        return new LevelStem(dimensionType, generator);
    }

    public static ChunkGenerator createGenerator(Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> dimensionSettingsRegistry, long seed) {
        Supplier<NoiseGeneratorSettings> dimensionSettings = () -> {
            // fallback to overworld so that we don't crash before our datapack is loaded (horrible workaround)
            NoiseGeneratorSettings settings = dimensionSettingsRegistry.get(DIMENSION_SETTINGS);
            return settings != null ? settings : dimensionSettingsRegistry.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
        };
        TropicraftBiomeProvider biomeSource = new TropicraftBiomeProvider(seed, biomeRegistry);
        return new TropicraftChunkGenerator(biomeSource, seed, dimensionSettings);
    }

    public static void teleportPlayer(ServerPlayer player, ResourceKey<Level> dimensionType) {
        if (player.level.dimension() == dimensionType) {
            teleportPlayerNoPortal(player, Level.OVERWORLD);
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
    public static void teleportPlayerNoPortal(ServerPlayer player, ResourceKey<Level> destination) {
        ServerLevel world = player.server.getLevel(destination);
        if (world == null) {
            LOGGER.error("Cannot teleport player to dimension {} as it does not exist!", destination.location());
            return;
        }

        if (!ForgeHooks.onTravelToDimension(player, destination)) return;

        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());

        LevelChunk chunk = world.getChunk(x >> 4, z >> 4);
        int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);
        player.teleportTo(world, x + 0.5, topY + 1.0, z + 0.5, player.yRot, player.xRot);

        BasicEventHooks.firePlayerChangedDimensionEvent(player, destination, destination);
    }

    // hack to get the correct sea level given a world: the vanilla IWorldReader.getSeaLevel() is deprecated and always returns 63 despite the chunk generator
    public static int getSeaLevel(LevelReader world) {
        if (world instanceof ServerLevel) {
            ServerChunkCache chunkProvider = ((ServerLevel) world).getChunkSource();
            return chunkProvider.getGenerator().getSeaLevel();
        } else if (world instanceof Level) {
            ResourceKey<Level> dimensionKey = ((Level) world).dimension();
            if (dimensionKey == WORLD) {
                return 127;
            }
        }
        return world.getSeaLevel();
    }
}
