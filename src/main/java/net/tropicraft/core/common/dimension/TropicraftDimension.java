package net.tropicraft.core.common.dimension;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProvider;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.function.Supplier;

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
        Path newDimension = save.getDimensionPath(WORLD);
        if (oldDimension.exists() && !newDimension.toFile().exists()) {
            try {
                FileUtils.moveDirectory(oldDimension, newDimension.toFile());
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
            Registry<NormalNoise.NoiseParameters> paramRegistry,
            long seed
    ) {
        Supplier<DimensionType> dimensionType = () -> dimensionTypeRegistry.getOrThrow(TropicraftDimension.DIMENSION_TYPE);
        ChunkGenerator generator = TropicraftDimension.createGenerator(paramRegistry, biomeRegistry, dimensionSettingsRegistry, seed);

        return new LevelStem(dimensionType, generator);
    }

    public static ChunkGenerator createGenerator(Registry<NormalNoise.NoiseParameters> params, Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> dimensionSettingsRegistry, long seed) {
        Supplier<NoiseGeneratorSettings> dimensionSettings = () -> {
            // fallback to overworld so that we don't crash before our datapack is loaded (horrible workaround)
            NoiseGeneratorSettings settings = dimensionSettingsRegistry.get(DIMENSION_SETTINGS);
            return settings != null ? settings : dimensionSettingsRegistry.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
        };
        TropicraftBiomeProvider biomeSource = new TropicraftBiomeProvider(seed, biomeRegistry);
        return new TropicraftChunkGenerator(params, biomeSource, seed, dimensionSettings);
    }

    /**
     *
     * Method that handles teleporting the player to and from the tropics depending on certain parameters.
     * Finds the top Y position relative to the dimension the player is teleporting to and places the entity at that position.
     *
     * Depending on the boolean passed through, a portal will be generated on the players teleport with such position
     * based on the portal's info position.
     *
     * If false,the position will be based on finding the top Y position relative
     * to the dimension the player is teleporting to and places the entity at that position. Avoids portal generation
     * by using player.teleport() instead of player.changeDimension()
     *
     * @param player The player that will be teleported
     * @param dimensionType The Tropicraft Dimension Type for reference
     * @param usingPortal If the method is to generate a portal on teleport
     */

    public static void teleportPlayer(ServerPlayer player, ResourceKey<Level> dimensionType, boolean usingPortal) {
        ResourceKey<Level> destination;

        if (player.level.dimension() == dimensionType) {
            destination = Level.OVERWORLD;
        } else {
            destination = dimensionType;
        }

        ServerLevel destLevel = player.server.getLevel(destination);
        if (destLevel == null) {
            LOGGER.error("Cannot teleport player to dimension {} as it does not exist!", destination.location());
            return;
        }

        if(usingPortal){
            if(!player.isOnPortalCooldown()){
                player.unRide();
                player.changeDimension(destLevel, new TeleporterTropics(destLevel));

                //Note: Stops the player from teleporting right after going through the portal
                ObfuscationReflectionHelper.setPrivateValue(Entity.class, player, 160, "portalCooldown");
            }
        }
        else{
            if (!ForgeHooks.onTravelToDimension(player, destination)) return;

            int x = Mth.floor(player.getX());
            int z = Mth.floor(player.getZ());

            LevelChunk chunk = destLevel.getChunk(x >> 4, z >> 4);
            int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);
            player.teleportTo(destLevel, x + 0.5, topY + 1.0, z + 0.5, player.getYRot(), player.getXRot());

            ForgeEventFactory.firePlayerChangedDimensionEvent(player, destination, destination);
        }
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
